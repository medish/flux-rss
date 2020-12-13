package com.example.projet_android.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.projet_android.R

class AlarmNotificationBuilder {
    companion object {
        private const val CHANNEL_ID = "channel2"
        private const val CHANNEL_NAME = "alarmChannel"
        private const val REQUEST_CODE = 2
        fun build (context: Context): NotificationCompat.Builder? {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(channel)
            }


            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.resources.getString(R.string.alarm_notif_title))
                .setContentText(context.resources.getString(R.string.alarm_notif_content))
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_alarm)

            return notification
        }
    }
}