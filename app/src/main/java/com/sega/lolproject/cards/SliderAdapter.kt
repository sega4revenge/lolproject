package com.sega.lolproject.cards


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sega.lolproject.MyApplication
import com.sega.lolproject.R
import com.sega.lolproject.model.Skin
import io.realm.RealmList


class SliderAdapter(private val content: RealmList<Skin>, private val count: Int, private val listener: View.OnClickListener?) : RecyclerView.Adapter<SliderCard>() {
    var context : Context?=null
    private val options = RequestOptions()
            .centerCrop()
            .placeholder(R.color.transparent)
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderCard {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_slider_card, parent, false)
        context = parent.context
        if (listener != null) {
            view.setOnClickListener { view ->
                listener.onClick(view) }
        }

        return SliderCard(view)
    }

    override fun onBindViewHolder(holder: SliderCard, position: Int) {
        val arrayskin = Array(content.size, { i -> content[i]!!.imageLoading })
        var bitmap : Bitmap?=null
        if(MyApplication.getManager()!!.checkFullData())
        {
            bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(arrayskin[position % arrayskin.size].toString()))
            holder.setContent(bitmap!!)
        }

        else
            Glide.with(context)
                    .asBitmap()
                    .load(arrayskin[position % arrayskin.size].toString())
                    .apply(options)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resourceAvatar: Bitmap?, transition: Transition<in Bitmap>?) {
                            bitmap = resourceAvatar
                            holder.setContent(bitmap!!)
                        }
                    })

    }

    override fun onViewRecycled(holder: SliderCard?) {
        holder!!.clearContent()
    }

    override fun getItemCount(): Int {
        return count
    }

}
