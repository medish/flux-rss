package com.example.projet_android.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService

class NetworkConnectivity {
    companion object {
        const val ON_WIFI = 0
        const val ON_MOBILE_DATA = 1
        const val OFFLINE = -1

        fun getNetworkStatus(context: Context) : Int{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo ?: return OFFLINE

            when(networkInfo.type){
                ConnectivityManager.TYPE_WIFI -> return ON_WIFI
                ConnectivityManager.TYPE_MOBILE -> return ON_MOBILE_DATA
            }

            return OFFLINE
        }
    }
}