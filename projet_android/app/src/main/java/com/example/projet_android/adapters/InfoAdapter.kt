package com.example.projet_android.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_android.entities.Info
import com.example.projet_android.R
import com.example.projet_android.entities.DateConverter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.info_item_layout.view.*
import android.os.Handler
import android.os.Looper

class InfoAdapter (): RecyclerView.Adapter<InfoAdapter.VH>(){
    var lsInfo = emptyList<Info>()

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

        // visibility
        val visibleView = infoView.newInfo
        if(info.nouveau) {
            visibleView.visibility = View.VISIBLE
        }else
            visibleView.visibility = View.GONE



        val pubDate = DateConverter().dateToOutputFormat(info.pubDate)
        infoView.pubDate.text = pubDate

        if(info.imageUrl.isEmpty())
            infoView.imageInfo.setImageResource(R.drawable.ic_image_info_error)
        else
            Picasso.get().load(info.imageUrl).into(infoView.imageInfo)

    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            return super.onBindViewHolder(holder, position, payloads)
        }

        Log.i("BIND-VIEW", "BIND $position")
        val v = holder.itemView.newInfo
        v.animate()
            .alpha(0f)
            .setDuration(1200)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationStart(animation: Animator?) {
                    v.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?) {
                    v.visibility = View.GONE
                }
            })
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.itemView.newInfo.clearAnimation()
    }

    fun setListInfo(info: List<Info>){
        this.lsInfo = info
        notifyDataSetChanged()
    }

    fun getInfoAt(pos : Int) : Info?{
        return try {
            lsInfo[pos]
        }catch (e : ArrayIndexOutOfBoundsException){
            null
        }
    }

    fun changeInfosEtat(firstItem : Int, lastItem : Int){
        for(i in firstItem..lastItem){
            val info = getInfoAt(i)
            if(info != null && info.nouveau){
                info.nouveau = false
                info.isConsulted = true
                Handler(Looper.getMainLooper()).post{
                    notifyItemChanged(i, true)
                }
            }
        }
    }

}