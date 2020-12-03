package com.example.projet_android.activities

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.adapters.FluxAdapter
import com.example.projet_android.models.FluxModel
import kotlinx.android.synthetic.main.activity_list_flux.*
import java.lang.IllegalArgumentException

class ListFlux : AppCompatActivity() {
    private val REQUEST_ADD_FLUX = 1

    private lateinit var ajoutfluxmodel: FluxModel
    private val recyclerViewAdapter: FluxAdapter = FluxAdapter()
    var lsFlux = emptyList<Flux>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flux)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ajoutfluxmodel = ViewModelProvider(this).get(FluxModel::class.java)
        lsFlux = ajoutfluxmodel.allflux()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        ajoutfluxmodel.allfluxs.observe(this, Observer {
            recyclerViewAdapter.setListFlux(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_FLUX && resultCode == RESULT_OK){
            val fluxId = data?.getLongExtra("fluxId", -1)
            Toast.makeText(this@ListFlux,"Le flux $fluxId est bien ajouté", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun downloadFlux(button: View) {
        // Map< Key = fluxid, value = url>
        val urls = recyclerViewAdapter.lsFlux.mapNotNull {
            if (it.isChecked) { it.id to it.url } else null
        }.toMap()

        if (urls.isEmpty()) {
            Toast.makeText(this@ListFlux, "Vous devez cochez au moins un flux", Toast.LENGTH_LONG)
                .show()
            return
        }

        launchDownload(urls)
    }


    private fun launchDownload(urls: Map<Long, String>) {
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val sharedPreferences = getSharedPreferences("DownloadsIds", MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()

        for (url in urls) {
            try {
                val request = DownloadManager.Request(Uri.parse(url.value))
                val id = dm.enqueue(request)
                sharedEditor.putLong(id.toString(), url.key)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        sharedEditor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.flux_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
           // android.R.id.home    -> { finish(); true}
            R.id.add_flux_item -> { addFlux() ;true }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    private fun addFlux(){
        val aj = Intent(this, AjouterFlux::class.java)
        startActivityForResult(aj, REQUEST_ADD_FLUX)
    }
}
