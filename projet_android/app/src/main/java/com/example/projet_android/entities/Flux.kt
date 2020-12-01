package com.example.projet_android.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.net.URL


@Entity( indices = [Index( value = ["url"], unique = true)])
data class Flux(
    @PrimaryKey(autoGenerate =true)
    var id:Long= 0,

    var url: String,
    var source: String,
    var tag: String,
    @Ignore
    var isChecked : Boolean
){
    constructor(url: String, source: String, tag: String) : this(0, url, source, tag,false)
}