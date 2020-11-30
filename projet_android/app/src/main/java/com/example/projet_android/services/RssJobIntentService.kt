package com.example.projet_android.services

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.lifecycle.ViewModelProvider
import com.example.projet_android.InfoData
import com.example.projet_android.models.InfoModel
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
            val infoList = RssXmlParser.analyseRssXml(doc)

            val db = InfoData.getInstance(this)
            try {
                db.Daoinsert.insertInfo(*infoList)
            }catch (e : SQLiteConstraintException){
                Log.e(TAG, e.message.toString())
            }
        }catch (e : FileNotFoundException){
            Log.e(TAG, e.message.toString())
        }
    }
}