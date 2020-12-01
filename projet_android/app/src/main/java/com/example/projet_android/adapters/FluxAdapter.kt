package com.example.projet_android.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Flux
import com.example.projet_android.R
import kotlinx.android.synthetic.main.flux_item_layout.view.*

class FluxAdapter (): RecyclerView.Adapter<FluxAdapter.VH>() {

    private var lsFlux = emptyList<Flux>()

    private var checkBoxListener = View.OnClickListener { view->
        val checkBox = view as CheckBox
        val position = checkBox.tag as Int
        val flux = lsFlux[position]
        flux.isChecked = checkBox.isChecked
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return lsFlux.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.flux_item_layout, parent,false)

        v.flux_checkbox.setOnClickListener(checkBoxListener)

        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val fluxView = holder.itemView
        val flux = lsFlux[position]

        fluxView.source.text = flux.source
        fluxView.tags.text = flux.tag
        fluxView.url.text = flux.url
        fluxView.flux_checkbox.isChecked = flux.isChecked
        fluxView.flux_checkbox.tag = position

        fluxView.setBackgroundColor(
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