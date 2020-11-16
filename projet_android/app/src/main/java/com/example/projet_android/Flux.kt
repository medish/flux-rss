package com.example.projet_android

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL


@Entity
data class Flux(
    @PrimaryKey
    val id: Int?,
    var source: String,
    var tag: String,
    var url: String
)