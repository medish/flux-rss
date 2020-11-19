package com.example.projet_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    fun AjouterFlux(button:View){
            val aj = Intent(this, AjouterFlux::class.java)
            startActivity(aj)
    }

    fun ListFlux(button:View){
        val aj = Intent(this, ListFlux::class.java)
        startActivity(aj)
    }

    fun ListInfo(button:View){
        val aj = Intent(this, ListInfo::class.java)
        startActivity(aj)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}