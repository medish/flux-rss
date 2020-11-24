package com.example.projet_android.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import kotlinx.android.synthetic.main.item_layout.view.*

class FluxAdapter: RecyclerView.Adapter<FluxAdapter.VH>() {

    var lsFlux = emptyList<Flux>()

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return lsFlux.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.item_layout, parent,false)

        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.source.text = lsFlux[position].source
        holder.itemView.tags.text = lsFlux[position].tag
        holder.itemView.url.text = lsFlux[position].url

        holder.itemView.setBackgroundColor(
            if(position % 2 == 0){
                Color.argb(30,0,220,0)
            }else{
                Color.argb(30,0,0,220)
            }
        )
    }

    fun setListFlux(flux: List<Flux>){
        this.lsFlux = flux
        notifyDataSetChanged()
    }

}