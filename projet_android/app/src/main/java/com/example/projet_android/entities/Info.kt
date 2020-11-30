package com.example.projet_android.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.net.URL


@Entity(foreignKeys = arrayOf(ForeignKey(entity = Flux::class, parentColumns = ["id"] , childColumns = ["fluxid"],deferred = true, onDelete = ForeignKey.CASCADE)))
data class Info(
    @PrimaryKey(autoGenerate =true)
    var id: Long= 0,
    var title: String,
    var description:String,
    var Link: String,
    var nouveau:Boolean,
    var fluxid: Long
)