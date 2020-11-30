package com.example.projet_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.adapters.InfoAdapter
import com.example.projet_android.models.FluxModel

class ListInfo : AppCompatActivity() {
    private lateinit var infomodel: FluxModel
    private val recyclerViewAdapter: InfoAdapter = InfoAdapter()
    private var lsinfo = emptyList<Info>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //just a test
        val i = Info("test","just a test","www.test.com",true,1)

        infomodel = ViewModelProvider(this).get(FluxModel::class.java)
        infomodel.ajouterInfo(i)
        lsinfo = infomodel.allInfo()



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.setListInfo(lsinfo)
        recyclerView.adapter = recyclerViewAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){
            android.R.id.home     -> { finish() }

        }
        return super.onOptionsItemSelected(item)
    }

}