package com.example.projet_android

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL


@Entity
data class Flux(
    @PrimaryKey
    var url: String,

    var source: String,
    var tag: String
)