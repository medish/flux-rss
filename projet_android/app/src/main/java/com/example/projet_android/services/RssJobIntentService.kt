package com.example.projet_android.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.projet_android.utils.RssXmlParser
import java.io.FileNotFoundException

class RssJobIntentService : JobIntentService() {
    companion object {
        private val TAG = "RSS-JOB-SERVICE"
        private val JOB_ID = 1

        fun enqueueWork(context : Context, intent: Intent){
            enqueueWork(context, RssJobIntentService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val uri = intent.getStringExtra("uri") ?: ""
        Log.d(TAG, uri)

        try{
            val istream = contentResolver.openInputStream(Uri.parse(uri))
            val doc = RssXmlParser.xmlToDocument(istream)
            RssXmlParser.analyseRssXml(doc)
        }catch (e : FileNotFoundException){
            Log.e(TAG, e.message.toString())
        }
    }
}