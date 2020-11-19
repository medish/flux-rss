package com.example.projet_android

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.net.URL


@Entity
data class Info(
    @PrimaryKey
    var title: String,
    var description:String,
    var Link: String,
    var nouveau:Boolean,
    var Fluxurl: String
)