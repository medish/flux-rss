package com.example.projet_android.broadcasts

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.projet_android.services.RssJobIntentService
import com.example.projet_android.utils.RssXmlParser
import java.io.FileNotFoundException
import java.lang.Exception

class RssDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val sharedPreferences = context.getSharedPreferences("DownloadsIds", Context.MODE_PRIVATE)

        if(sharedPreferences.contains(downloadID.toString())) {
            val fluxId = sharedPreferences.getLong(downloadID.toString(), -1)
            Toast.makeText(context, "Finished $fluxId", Toast.LENGTH_SHORT).show()
            Log.d("DOWNLOAD-RECEIVER", downloadID.toString())
            clearDownloadReference(downloadID, sharedPreferences)

            // Send uri's file to the job intent
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = dm.getUriForDownloadedFile(downloadID)
            intent.putExtra("uri", uri.toString())
            intent.putExtra("fluxId", fluxId)
            RssJobIntentService.enqueueWork(context, intent)
            //dm.remove(downloadID)
        }
    }

    private fun clearDownloadReference(downloadId : Long, sharedPreferences: SharedPreferences){
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.remove(downloadId.toString())
        sharedEditor.apply()
    }
}

