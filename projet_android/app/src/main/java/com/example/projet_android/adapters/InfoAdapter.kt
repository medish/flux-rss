package com.example.projet_android.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.entities.DateConverter
import com.example.projet_android.models.InfoModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.info_item_layout.view.*

class InfoAdapter (): RecyclerView.Adapter<InfoAdapter.VH>() {
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
        val infoView = holder.itemView
        val info = lsInfo[position]

        infoView.title.text = info.title
        infoView.description.text = info.description
        if(! info.nouveau)
            infoView.newInfo.visibility = View.INVISIBLE

        val pubDate = DateConverter().dateToFormat(info.pubDate)
        infoView.pubDate.text = pubDate

        if(info.imageUrl.isEmpty())
            infoView.imageInfo.setImageResource(R.drawable.ic_image_info_error)
        else
            Picasso.get().load(info.imageUrl).into(infoView.imageInfo)
        
    }

    fun setListInfo(info: List<Info>){
        this.lsInfo = info
        notifyDataSetChanged()
    }

    fun getInfoAt(pos : Int) : Info{
        return lsInfo[pos]
    }

}