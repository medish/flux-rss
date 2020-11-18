package com.example.projet_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFlux : AppCompatActivity() {
    private lateinit var ajoutfluxmodel: AjouteFluxModel
    private val recyclerViewAdapter: FluxAdapter = FluxAdapter()
    var lsFlux = emptyList<Flux>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flux)

        ajoutfluxmodel = ViewModelProvider(this).get(AjouteFluxModel::class.java)
        lsFlux = ajoutfluxmodel.allflux()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.setListFlux(lsFlux)
        recyclerView.adapter = recyclerViewAdapter

       // ajoutfluxmodel.allfluxs.observe(this, Observer {recyclerViewAdapter.setListFlux(it)})




    }
}