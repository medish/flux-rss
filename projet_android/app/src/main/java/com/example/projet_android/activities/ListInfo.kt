package com.example.projet_android.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.adapters.InfoAdapter
import com.example.projet_android.models.FluxModel
import com.example.projet_android.models.InfoModel

class ListInfo : AppCompatActivity(), InfoAdapter.OnItemClickListener {
    private lateinit var infoModel: InfoModel
    lateinit var e :EditText
    private val recyclerViewAdapter: InfoAdapter = InfoAdapter()
    private var lsinfo = emptyList<Info>()


    override fun OnItemClick(position: Int) {
        
        AlertDialog.Builder(this)
            .setMessage("Action à faire?")
            .setTitle("ACTION")    .setCancelable(false)
            .setPositiveButton("affiche pageweb") { dialog: DialogInterface, t: Int ->
                val aj = Intent(this, WebView::class.java)
                aj.putExtra("link",lsinfo[position].link)
                startActivity(aj)
                dialog.dismiss()    }
            .setNegativeButton("Supprime info") { _ , _ ->
                alert(position)
                Log.d("Message", "NO")}
            .setNeutralButton("Cancel") { dialogue, _ ->
                dialogue.cancel()
                Log.d("Message", "cancel")}.show()


    }

    fun alert(position: Int){
        AlertDialog.Builder(this)
            .setMessage("Supprimer l'info?")
            .setTitle("ALERT")    .setCancelable(false)
            .setPositiveButton("OUI") { dialog: DialogInterface, t: Int ->
                infoModel.delete(lsinfo[position].id)
                dialog.dismiss()    }
            .setNeutralButton("Cancel") { dialogue, _ ->
                dialogue.cancel()
                Log.d("Message", "cancel")}.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_info)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //just a test
        //val i = Info("test","just a test2222","www.test.com",true,6)

        e = findViewById(R.id.recherche)

        infoModel = ViewModelProvider(this).get(InfoModel::class.java)
        //infoModel.addInfo(i)

        lsinfo = infoModel.allInfo()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.setListener(this)

        infoModel.allInfos.observe(this, Observer {
            recyclerViewAdapter.setListInfo(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            // android.R.id.home    -> { finish(); true}

            R.id.nouveau -> {

                              lsinfo = infoModel.nouveau()
                              recyclerViewAdapter.setListInfo(lsinfo)
                               ;true }
            else -> { super.onOptionsItemSelected(item) }
        }
    }


    fun recherche(view: View) {

        lsinfo = infoModel.recherche(e.text.toString().trim())
        recyclerViewAdapter.setListInfo(lsinfo)

    }
}