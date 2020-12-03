package com.example.projet_android.activities

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.models.FluxModel
import kotlinx.android.synthetic.main.activity_ajouter_flux.*
import java.lang.IllegalArgumentException


class AjouterFlux : AppCompatActivity() {
    private lateinit var fluxModel: FluxModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_flux)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fluxModel = ViewModelProvider(this).get(FluxModel::class.java)
    }


    fun ajouter(button:View){


        if(source_edit.text.isEmpty() || tag_edit.text.isEmpty() || url_edit.text.isEmpty()){
            Toast.makeText(this@AjouterFlux, "Vous devez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val url = url_edit.text.toString().trim()
        // check if the entered url is valid
        if(url.isNotEmpty()){
            try {
                DownloadManager.Request(Uri.parse(url))
            }catch (e : IllegalArgumentException){
                Toast.makeText(this@AjouterFlux, "URL n'est pas valide!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                return
            }
        }

        val f = Flux(url, source_edit.text.toString().trim(), tag_edit.text.toString().trim())
        val fluxId = fluxModel.ajouterFlux(f)
        if(fluxId < 0){
            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra("fluxId", fluxId)
        setResult(RESULT_OK, data)
        finish()
    }
}