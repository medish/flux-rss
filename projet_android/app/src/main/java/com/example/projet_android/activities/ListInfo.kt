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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.adapters.InfoAdapter
import com.example.projet_android.models.FluxModel
import com.example.projet_android.models.InfoModel
import kotlinx.android.synthetic.main.activity_list_flux.*
import kotlinx.android.synthetic.main.activity_list_info.*

class ListInfo : AppCompatActivity(){
    private lateinit var infoModel: InfoModel
    private val recyclerViewAdapter: InfoAdapter = InfoAdapter()
    private var lsinfo = emptyList<Info>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_info)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        infoModel = ViewModelProvider(this).get(InfoModel::class.java)

        lsinfo = infoModel.allInfo()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        infoModel.allInfos.observe(this, Observer {
            recyclerViewAdapter.setListInfo(it)
        })

        // swipe to delete an info
        val itemTouchHelper = ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.nouveau -> {

                              lsinfo = infoModel.nouveau()
                              recyclerViewAdapter.setListInfo(lsinfo)
                               ;true }
            else -> { super.onOptionsItemSelected(item) }
        }
    }


    fun recherche(view: View) {

        lsinfo = infoModel.recherche(recherche.text.toString().trim())
        recyclerViewAdapter.setListInfo(lsinfo)

    }


    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT.or(ItemTouchHelper.LEFT)) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // swipe LEFT, to delete
            val position = viewHolder.adapterPosition
            val info = recyclerViewAdapter.getInfoAt(position)

            if(direction == ItemTouchHelper.LEFT){

                android.app.AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage(R.string.confirm_delete_flux)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        infoModel.delete(info.id)
                    }
                    .setNegativeButton(R.string.cancel){_,_->
                        recyclerViewAdapter.notifyItemChanged(position)
                    }
                    .create()
                    .show()
            }

            // swipe RIGHT, to open PageWeb
            else if(direction == ItemTouchHelper.RIGHT){
                val webView = Intent(applicationContext, WebView::class.java)
                webView.putExtra("link", info.link)
                recyclerViewAdapter.notifyItemChanged(position)
                startActivity(webView)
            }



        }
    }
}