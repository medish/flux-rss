package com.example.projet_android.services

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.projet_android.FluxData
import com.example.projet_android.utils.RssXmlParser
import java.io.FileNotFoundException

class RssJobIntentService : JobIntentService() {
    companion object {
        private const val TAG = "RSS-JOB-SERVICE"
        private const val JOB_ID = 1

        fun enqueueWork(context : Context, intent: Intent){
            enqueueWork(context, RssJobIntentService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val uri = intent.getStringExtra("uri") ?: ""
        val fluxId = intent.getLongExtra("fluxId", -1)
        Log.d(TAG, uri)

        try{
            val istream = contentResolver.openInputStream(Uri.parse(uri)) ?: return
            val doc = RssXmlParser.xmlToDocument(istream)
            val infoList = RssXmlParser.analyseRssXml(doc, fluxId)

            val infoDao = FluxData.getInstance(this).infoDao
            try {
                infoDao.insertInfo(*infoList)
            }catch (e : SQLiteConstraintException){
                e.printStackTrace()
            }
        }catch (e : FileNotFoundException){
            e.printStackTrace()
        }


    }
}