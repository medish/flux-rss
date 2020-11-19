package com.example.projet_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListInfo : AppCompatActivity() {
    private lateinit var infomodel: InfoModel
    private val recyclerViewAdapter: InfoAdapter = InfoAdapter()
    var lsinfo = emptyList<Info>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //just a test
        val i : Info
        i= Info("test","just a test","www.test.com",true,"www.test.com")

        infomodel = ViewModelProvider(this).get(InfoModel::class.java)
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