package com.example.projet_android.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.net.URL


@Entity(foreignKeys = [ForeignKey(entity = Flux::class, parentColumns = ["id"] , childColumns = ["fluxid"],deferred = true, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)])
data class Info(
    @PrimaryKey(autoGenerate =true)
    var id: Long,
    var title: String,
    var description:String,
    var Link: String,
    var nouveau:Boolean,
    var fluxid: Long
){
    constructor(title: String, description: String, Link: String, nouveau: Boolean, fluxid: Long) :
            this(0, title, description, Link, nouveau, fluxid)
}