package com.example.projet_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import com.example.projet_android.adapters.FluxAdapter
import com.example.projet_android.models.FluxModel

class ListFlux : AppCompatActivity() {
    private lateinit var ajoutfluxmodel: FluxModel
    private val recyclerViewAdapter: FluxAdapter = FluxAdapter()
    var lsFlux = emptyList<Flux>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flux)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ajoutfluxmodel = ViewModelProvider(this).get(FluxModel::class.java)
        lsFlux = ajoutfluxmodel.allflux()


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.setListFlux(lsFlux)
        recyclerView.adapter = recyclerViewAdapter

       // ajoutfluxmodel.allfluxs.observe(this, Observer {recyclerViewAdapter.setListFlux(it)})
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){
            android.R.id.home     -> { finish() }

        }
        return super.onOptionsItemSelected(item)
    }

}