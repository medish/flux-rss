package com.example.projet_android.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

class RssJobIntentService : JobIntentService() {
    companion object {
        private val TAG = "RSS-JOB-SERVICE"
        private val JOB_ID = 1

        fun enqueueWork(context : Context, intent: Intent){
            enqueueWork(context, RssJobIntentService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        // receive files
        // parse files to info model
        // add models to info table
    }
}