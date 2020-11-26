package com.example.projet_android.broadcasts

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast

class RssDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val sharedPreferences = context.getSharedPreferences("DownloadsIds", Context.MODE_PRIVATE)

        if(sharedPreferences.contains(downloadID.toString())){
            Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
            clearDownloadReference(downloadID, sharedPreferences)
        }

        val allKeys = sharedPreferences.all.map { it.key }
        for(key in allKeys) {
            Log.d("KEY", key)
        }
    }

    private fun clearDownloadReference(downloadId : Long, sharedPreferences: SharedPreferences){
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.remove(downloadId.toString())
        sharedEditor.apply()
    }
}

