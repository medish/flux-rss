package com.example.projet_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.projet_android.dao.FluxDao
import com.example.projet_android.dao.InfoDao
import com.example.projet_android.entities.DateConverter
import com.example.projet_android.entities.Flux
import com.example.projet_android.entities.Info


@Database(entities = [Flux::class, Info::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class FluxData: RoomDatabase()  {
    abstract val fluxDao : FluxDao
    abstract val infoDao : InfoDao

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