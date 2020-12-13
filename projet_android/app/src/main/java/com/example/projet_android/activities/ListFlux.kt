package com.example.projet_android.activities

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Network
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.adapters.FluxAdapter
import com.example.projet_android.models.FluxModel
import com.example.projet_android.utils.NetworkConnectivity
import com.example.projet_android.utils.NetworkConnectivity.Companion.OFFLINE
import com.example.projet_android.utils.NetworkConnectivity.Companion.ON_MOBILE_DATA
import com.example.projet_android.utils.NetworkConnectivity.Companion.getNetworkStatus
import kotlinx.android.synthetic.main.activity_list_flux.*
import java.lang.IllegalArgumentException

class ListFlux : AppCompatActivity() {
    private val REQUEST_ADD_FLUX = 1

    private lateinit var fluxModel: FluxModel
    private val recyclerViewAdapter: FluxAdapter = FluxAdapter()
    private var lsFlux = emptyList<Flux>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flux)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fluxModel = ViewModelProvider(this).get(FluxModel::class.java)
        lsFlux = fluxModel.allflux()

        recyclerView.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerViewAdapter

        fluxModel.allfluxs.observe(this, Observer {
            recyclerViewAdapter.setListFlux(it)
        })

        // swipe to delete a flux
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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
            Toast.makeText(this@ListFlux, "Vous devez séléctionnez au moins un flux", Toast.LENGTH_LONG)
                .show()
            return
        }

        connectivityDialog(urls)
    }

    private fun connectivityDialog(urls: Map<Long, String>) {
        val networkStatus = getNetworkStatus(this@ListFlux)
        when (networkStatus) {
            ON_MOBILE_DATA -> {
                AlertDialog.Builder(this@ListFlux)
                    .setMessage(R.string.network_mobile)
                    .setPositiveButton(R.string.yes) { _, _ -> launchDownload(urls)}
                    .setNeutralButton(R.string.cancel) { _, _ ->}
                    .create()
                    .show()
            }
            OFFLINE -> {
                AlertDialog.Builder(this@ListFlux)
                    .setMessage(R.string.network_offline)
                    .setPositiveButton(R.string.ok) { _, _ ->}
                    .create()
                    .show()
            }
            else -> { launchDownload(urls)}
        }
    }
    private fun launchDownload(urls: Map<Long, String>) {
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val sharedPreferences = getSharedPreferences("DownloadsIds", MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.clear()

        for (url in urls) {
            try {
                val request = DownloadManager.Request(Uri.parse(url.value))
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI.or(DownloadManager.Request.NETWORK_MOBILE))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setTitle("RSS flux")
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
            R.id.add_flux_item -> { addFluxActivity() ;true }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    private fun addFluxActivity(){
        val aj = Intent(this, AjouterFlux::class.java)
        startActivityForResult(aj, REQUEST_ADD_FLUX)
    }

    private val swipeToDeleteCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT.or(ItemTouchHelper.LEFT)) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            AlertDialog.Builder(viewHolder.itemView.context)
                .setMessage(R.string.confirm_delete_flux)
                .setPositiveButton(R.string.yes) { _, _ ->
                        val flux = recyclerViewAdapter.getFluxAt(position)
                        fluxModel.deleteFlux(flux.id)
                }
                .setNegativeButton(R.string.cancel){_,_->
                    recyclerViewAdapter.notifyItemChanged(position)
                }
                .create()
                .show()
        }
    }



}


