package com.example.projet_android.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.projet_android.R
import com.example.projet_android.activities.ListInfo

class DownloadNotificationBuilder {

    companion object {
        private const val CHANNEL_ID = "channel"
        private const val CHANNEL_NAME = "downloadCompletedChannel"
        private const val REQUEST_CODE = 1
        fun build (context: Context): NotificationCompat.Builder? {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(channel)
            }


            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.resources.getString(R.string.download_notif_title))
                .setContentText(context.resources.getString(R.string.download_notif_content))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon1)

            val intent = Intent(context, ListInfo::class.java)
            val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE,intent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification.setContentIntent(pendingIntent)

            return notification
        }
    }
}