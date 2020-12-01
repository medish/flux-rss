package com.example.projet_android.activities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.adapters.FluxAdapter
import com.example.projet_android.models.FluxModel
import kotlinx.android.synthetic.main.activity_list_flux.*
import java.lang.IllegalArgumentException

class ListFlux : AppCompatActivity() {
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
        recyclerViewAdapter.setListFlux(lsFlux)
        recyclerView.adapter = recyclerViewAdapter

       // ajoutfluxmodel.allfluxs.observe(this, Observer {recyclerViewAdapter.setListFlux(it)})
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){
            android.R.id.home     -> { finish() }

        }
        return super.onOptionsItemSelected(item)
    }

    fun downloadFlux(button : View){
        // Map< Key = fluxid, value = url>
        val urls = lsFlux.mapNotNull {
            if(it.isChecked) {it.id to it.url} else null
        }.toMap()

        if(urls.isEmpty()){
            Toast.makeText(this@ListFlux, "Vous devez cochez au moins un flux", Toast.LENGTH_LONG).show()
            return
        }

        for(url in urls){
            Log.i("FLUX", url.key.toString())
            Log.i("FLUX", url.value)
        }

        launchDownload(urls)
    }


    private fun launchDownload(urls : Map<Long, String>){
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val sharedPreferences = getSharedPreferences("DownloadsIds", MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()

        for(url in urls){
            try {
                val request = DownloadManager.Request(Uri.parse(url.value))
                val id = dm.enqueue(request)
                sharedEditor.putLong(id.toString(), url.key)
            }catch (e : IllegalArgumentException){
                e.printStackTrace()
            }
        }

        sharedEditor.apply()
    }
}