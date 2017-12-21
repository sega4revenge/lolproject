package com.sega.lolproject.cards

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.sega.lolproject.MyApplication
import com.sega.lolproject.R
import com.sega.lolproject.model.Champion

class ChampionAdapter(private val context: Context, private val onchampionClickListener: OnchampionClickListener) : RecyclerView.Adapter<ChampionViewHolders>(){



    var championList: ArrayList<Champion> = ArrayList()
    private val options = RequestOptions()
            .centerCrop()
            .placeholder(R.color.transparent)
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)!!

    fun removeItem(position: Int): Champion {
        val model = championList.removeAt(position)
        notifyItemRemoved(position)
        return model
    }

    fun addItem(position: Int, model: Champion) {
        championList.add(position, model)
        notifyItemInserted(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val model = championList.removeAt(fromPosition)
        championList.add(toPosition, model)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun animateTo(models: ArrayList<Champion>) {
        applyAndAnimateRemovals(models)
        applyAndAnimateAdditions(models)
        applyAndAnimateMovedItems(models)
    }
    private fun applyAndAnimateRemovals(newModels: List<Champion>) {
        for (i in championList.size - 1 downTo 0) {
            val model =championList[i]
            if (!newModels.contains(model)) {
                removeItem(i)
            }
        }
    }

    private fun applyAndAnimateAdditions(newModels: List<Champion>) {
        var i = 0
        val count = newModels.size
        while (i < count) {
            val model = newModels[i]
            if (!championList.contains(model)) {
                addItem(i, model)
            }
            i++
        }
    }

    private fun applyAndAnimateMovedItems(newModels: List<Champion>) {
        for (toPosition in newModels.indices.reversed()) {
            val model = newModels[toPosition]
            val fromPosition = championList.indexOf(model)
            if (fromPosition >= 0 && fromPosition != toPosition) {
               moveItem(fromPosition, toPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionViewHolders {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, null)
        return ChampionViewHolders(layoutView)
    }

    override fun onBindViewHolder(holder: ChampionViewHolders, position: Int) {
        holder.countryName.text = championList[position].name
        if(MyApplication.getManager()!!.checkFullData())
        {
            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(championList[position].imageAvatar))
            holder.countryPhoto.setImageBitmap(bitmap)
        }
        else
        {
            Glide.with(context)
                    .load(championList[position].imageAvatar)
                    .apply(options)
                    .into(holder.countryPhoto)
        }


        holder.itemView.setOnClickListener { onchampionClickListener.onchampionClicked(position) }
    }

    override fun getItemCount(): Int {
        return this.championList.size
    }

    interface OnchampionClickListener {
        fun onchampionClicked(position: Int)
    }


}