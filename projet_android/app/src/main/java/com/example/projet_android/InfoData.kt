package com.example.projet_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Info::class ), version = 1)
abstract class InfoData: RoomDatabase()  {
    abstract val Daoinsert : InfoDao

    companion object {

        @Volatile
        private var INSTANCE: InfoData? = null

        fun getInstance(context: Context): InfoData {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InfoData::class.java,
                        "Info"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}