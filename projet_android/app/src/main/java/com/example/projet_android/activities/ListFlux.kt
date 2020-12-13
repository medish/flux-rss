package com.example.projet_android.activities

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Network
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
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
import com.example.projet_android.broadcasts.AlarmReceiver
import com.example.projet_android.models.FluxModel
import com.example.projet_android.utils.AlarmNotificationBuilder
import com.example.projet_android.utils.NetworkConnectivity
import com.example.projet_android.utils.NetworkConnectivity.Companion.OFFLINE
import com.example.projet_android.utils.NetworkConnectivity.Companion.ON_MOBILE_DATA
import com.example.projet_android.utils.NetworkConnectivity.Companion.getNetworkStatus
import kotlinx.android.synthetic.main.activity_list_flux.*
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min

class ListFlux : AppCompatActivity() {
    private val REQUEST_ADD_FLUX = 1
    private val REQUEST_ALARM_NOTIFICATION = 2

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

        // register a filter to cancel downloads
        val cancelFilter = IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        registerReceiver(cancelReceiver, cancelFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(cancelReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_FLUX && resultCode == RESULT_OK){
            val fluxId = data?.getLongExtra("fluxId", -1)
            Toast.makeText(this@ListFlux,"Le flux $fluxId est bien ajouté", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun downloadFluxAction(button: View) {
        val urls = recyclerViewAdapter.getMapFromSelectedFlux()

        if (urls.isEmpty()) {
            Toast.makeText(this@ListFlux, "Vous devez séléctionnez au moins un flux", Toast.LENGTH_LONG)
                .show()
            return
        }

        if(button == downloadButton)
            connectivityDialog(urls)
        else if(button == scheduleButton) {
            scheduleFlux()
        }
    }


    private fun connectivityDialog(urls: Map<Long, String>) {
        val networkStatus = getNetworkStatus(this@ListFlux)
        when (networkStatus) {
            ON_MOBILE_DATA -> {
                AlertDialog.Builder(this@ListFlux)
                    .setMessage(R.string.network_mobile)
                    .setPositiveButton(R.string.yes) { _, _ -> launchDownload(this@ListFlux, urls)}
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
            else -> { launchDownload(this@ListFlux,  urls)}
        }
    }

    private fun scheduleFlux() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this@ListFlux, onTimePickListener, hour, minute, DateFormat.is24HourFormat(this@ListFlux))
        timePicker.show()
    }

    private val onTimePickListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        Toast.makeText(this@ListFlux, "Hour: $hourOfDay - Minute: $minute", Toast.LENGTH_LONG).show()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)


        val alarmNotification = AlarmNotificationBuilder.build(this@ListFlux)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, alarmNotification?.build())

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@ListFlux, AlarmReceiver::class.java)
        val urls = HashMap(recyclerViewAdapter.getMapFromSelectedFlux())
        intent.putExtra("downloadMap", urls)

        val pendingIntent = PendingIntent.getBroadcast(this@ListFlux, REQUEST_ALARM_NOTIFICATION, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    private val cancelReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager


            val sharedPreferences = getSharedPreferences("DownloadsIds", MODE_PRIVATE)
            val sharedEditor = sharedPreferences.edit()
            val ids = sharedPreferences.all
            for(id in ids){
                dm.remove(id.key.toLong())
                sharedEditor.remove(id.key)
            }

            sharedEditor.apply()
            //Toast.makeText(this@ListFlux, "Aborted", Toast.LENGTH_LONG).show()
        }
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

    companion object {
        fun launchDownload(context : Context, urls: Map<Long, String>) {
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val sharedPreferences = context.getSharedPreferences("DownloadsIds", MODE_PRIVATE)
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
    }



}


