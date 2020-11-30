package com.example.projet_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.models.FluxModel


class AjouterFlux : AppCompatActivity() {
    private lateinit var ajoutfluxmodel: FluxModel

    fun ajouter(button:View){

        val s: EditText = findViewById(R.id.SOURCE)
        val t: EditText = findViewById(R.id.TAG)
        val u: EditText = findViewById(R.id.URL)

        if (button == findViewById(R.id.ajouter) && s.text.toString().trim() != "" && t.text.toString().trim()!="" && u.text.toString().trim()!="") {
            val f : Flux
            f=Flux(0,u.text.toString().trim(),s.text.toString().trim(),t.text.toString().trim())
            var ls = listOf<Long>()
            ls = ajoutfluxmodel.ajouterFlux(f)
            Toast.makeText(this, ls.size.toString(), Toast.LENGTH_SHORT).show()
            s.text.clear()
            t.text.clear()
            u.text.clear()

        }else{
            Toast.makeText(this, "les champs ne doit pas etre vide ", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_flux)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ajoutfluxmodel = ViewModelProvider(this).get(FluxModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){
            android.R.id.home     -> { finish() }

        }
        return super.onOptionsItemSelected(item)
    }
}