package com.example.projet_android.activities

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projet_android.R
import com.example.projet_android.broadcasts.RssDownloadReceiver
import com.example.projet_android.services.RssJobIntentService

class DownloadActivity : AppCompatActivity() {
    //private val br = RssDownloadReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

      //  registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    override fun onStop() {
        super.onStop()
        Log.d("STOP", "STOP")
        //unregisterReceiver(br)
    }

    fun launchDownload(button: View) {
        //val URL = "https://www.lemonde.fr/football/rss_full.xml"
        //val URL2 = "https://rss.nytimes.com/services/xml/rss/nyt/World.xml"
        //val request = DownloadManager.Request(Uri.parse(URL))
        //val request2 = DownloadManager.Request(Uri.parse(URL2))

        for(i in 0..0){
            val URL = "https://rss.nytimes.com/services/xml/rss/nyt/World.xml"
            val request = DownloadManager.Request(Uri.parse(URL))
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadID = dm.enqueue(request)
            saveDownloadReference(downloadID)
        }
    }

    private fun saveDownloadReference(downloadId : Long){
        val sharedPreferences = getSharedPreferences("DownloadsIds", MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.putLong(downloadId.toString(), downloadId)
        sharedEditor.apply()
    }

}
