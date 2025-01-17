package com.example.projet_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.adapters.InfoAdapter
import com.example.projet_android.models.InfoModel
import kotlinx.android.synthetic.main.activity_list_flux.*
import kotlinx.android.synthetic.main.activity_list_info.*
import kotlinx.android.synthetic.main.filter_layout.view.*

class ListInfo : AppCompatActivity(){
    private lateinit var infoModel: InfoModel
    private val recyclerViewAdapter: InfoAdapter = InfoAdapter()
    private var lsinfo = emptyList<Info>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_info)

        infoModel = ViewModelProvider(this).get(InfoModel::class.java)

        lsinfo = infoModel.allInfo()

        recyclerViewInfo.layoutManager = LinearLayoutManager(this)
        recyclerViewInfo.adapter = recyclerViewAdapter
        var firstVisibleItem = -1
        var lastVisibleItem = -1
        recyclerViewInfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layout = recyclerViewInfo.layoutManager as LinearLayoutManager
                val first = layout.findFirstCompletelyVisibleItemPosition()
                val last = layout.findLastCompletelyVisibleItemPosition()
                if(firstVisibleItem == first && lastVisibleItem == last)
                    return

                firstVisibleItem = first
                lastVisibleItem  = last
                //Log.i("ACTIVITY-CONSULTED", "FIRST $first  LAST $last")
                //recyclerView.itemAnimator = null
                recyclerViewAdapter.changeInfosEtat(first, last)
            }
        })


        infoModel.allInfos.observe(this, Observer { listInfo ->
            val ids = recyclerViewAdapter.lsInfo.mapNotNull { if(it.isConsulted) it.id else null}
            if(ids.isNotEmpty()) infoModel.changeEtatInfo(ids)

            if(listInfo.isEmpty()){
                infoRecyclerLayoutEmpty.visibility = View.VISIBLE
                recyclerViewInfo.visibility = View.GONE
            }else {
                infoRecyclerLayoutEmpty.visibility = View.GONE
                recyclerViewInfo.visibility = View.VISIBLE
            }
            recyclerViewAdapter.setListInfo(listInfo)
        })

        // swipe to delete an info
        val itemTouchHelper = ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerViewInfo)

    }

    override fun onPause() {
        super.onPause()
        val ids = recyclerViewAdapter.lsInfo.mapNotNull { if(it.isConsulted) it.id else null}
        if(ids.isNotEmpty()) infoModel.changeEtatInfo(ids)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.edit_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Filtrer"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                lsinfo = infoModel.recherche(query!!)
                recyclerViewAdapter.setListInfo(lsinfo)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        searchView.setOnCloseListener {
            lsinfo = infoModel.allInfo()
            recyclerViewAdapter.setListInfo(lsinfo)
            false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_button -> { openFilterDialog() ;true }
            R.id.rss_flux_item -> {
                val fluxIntent = Intent(this, ListFlux::class.java)
                startActivity(fluxIntent)
                true
            }
            else -> { super.onOptionsItemSelected(item) }

        }
    }

    private fun openFilterDialog(){
        val dialogBuilder = android.app.AlertDialog.Builder(this@ListInfo)
        dialogBuilder.setTitle("Filter")

        val layout = layoutInflater.inflate(R.layout.filter_layout, null, false)
        dialogBuilder
            .setView(layout)
            .setPositiveButton("Appliquer"){_,_ ->
                val sortPos = layout.sort.selectedItemPosition
                val pubDatePos = layout.publication_date.selectedItemPosition
                val filtrePos = layout.filter_etat.selectedItemPosition
                filterSearchDialog(sortPos, pubDatePos, filtrePos)

                //Toast.makeText(this@ListInfo,"DIALOGUE OK- $sortId", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Annuler"){_,_ ->}
        val alertDialog = dialogBuilder.create()

        alertDialog.show()
    }

    private fun filterSearchDialog(recentSort : Int, pubDatePos : Int, consultSort : Int){

        val sortFilter =
            when(recentSort){
                0 -> "'DESC"
                1 -> null
                else -> null
            }

        val sortPubDate =
            when(pubDatePos){
                0 -> "-1000 days"
                1 -> "-0 days"
                2 -> "-1 days"
                else -> "-1000 days"
            }

        val sortNouveauFilter =
            when(consultSort){
                0 -> ""
                1 -> "1"
                else -> ""
            }


        val ids = recyclerViewAdapter.lsInfo.mapNotNull { if(it.isConsulted) it.id else null}
        if(ids.isNotEmpty()) infoModel.changeEtatInfo(ids)

        lsinfo = infoModel.filterQuery(sortFilter, sortPubDate, sortNouveauFilter)
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
                        if(info != null)
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
                webView.putExtra("link", info?.link)
                recyclerViewAdapter.notifyItemChanged(position)
                startActivity(webView)
            }

        }
    }

}