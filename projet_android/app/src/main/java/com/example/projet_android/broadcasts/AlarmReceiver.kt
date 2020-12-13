package com.example.projet_android.broadcasts

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.projet_android.activities.ListFlux
import com.example.projet_android.utils.AlarmNotificationBuilder

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val urls = intent.getSerializableExtra("downloadMap") as HashMap<Long, String>

        ListFlux.launchDownload(context, urls)
    }
}
