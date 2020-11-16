package com.example.projet_android

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL


@Entity
data class Flux(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    var source: String,
    var tag: String,
    var url: String
)