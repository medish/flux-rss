package com.example.projet_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Flux::class ), version = 1)
abstract class FluxData: RoomDatabase()  {
    abstract val Daoinsert : UserDao

    companion object {

        @Volatile
        private var INSTANCE: FluxData? = null

        fun getInstance(context: Context): FluxData {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FluxData::class.java,
                        "Flux"
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