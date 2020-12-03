package com.example.projet_android.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import kotlinx.android.synthetic.main.info_item_layout.view.*

class InfoAdapter: RecyclerView.Adapter<InfoAdapter.VH>() {
    private var lsInfo = emptyList<Info>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return lsInfo.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.info_item_layout, parent,false)

        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.title.text = lsInfo[position].title
        holder.itemView.description.text = lsInfo[position].description

        holder.itemView.setBackgroundColor(
            if(position % 2 == 0){
                Color.argb(30,0,220,0)
            }else{
                Color.argb(30,0,0,220)
            }
        )
    }

    fun setListInfo(info: List<Info>){
        this.lsInfo = info
        notifyDataSetChanged()
    }
}