package com.example.projet_android.broadcasts

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.projet_android.utils.RssXmlParser
import java.io.FileNotFoundException

class RssDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val sharedPreferences = context.getSharedPreferences("DownloadsIds", Context.MODE_PRIVATE)

        if(sharedPreferences.contains(downloadID.toString())) {
            Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
            Log.d("KEY-1", downloadID.toString())
            clearDownloadReference(downloadID, sharedPreferences)

            // Get path
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            try {
                val uri = dm.getUriForDownloadedFile(downloadID)
                val istream = context.contentResolver.openInputStream(uri)
                val doc = RssXmlParser.xmlToDocument(istream)
                RssXmlParser.analyseRssXml(doc)

            }catch (e : FileNotFoundException){
            }
            //dm.remove(downloadID)
        }
    }

    private fun clearDownloadReference(downloadId : Long, sharedPreferences: SharedPreferences){
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.remove(downloadId.toString())
        sharedEditor.apply()
    }
}

