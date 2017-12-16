package com.sega.lolproject.cards

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sega.lolproject.R
import com.sega.lolproject.util.DecodeBitmapTask


class SliderCard(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView
    private var task: DecodeBitmapTask? = null
    val options = RequestOptions()
            .fitCenter()

            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)
    init {
        imageView = itemView.findViewById<View>(R.id.image) as ImageView
    }
    internal fun setContent(image : String) {
        if (viewWidth == 0) {
            itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    viewWidth = itemView.width
                    viewHeight = itemView.height
                    println(viewWidth)
                    println(viewHeight)

                    loadBitmap(image)
                }
            })
        } else {
            loadBitmap(image)
        }
    }

    internal fun clearContent() {
        if (task != null) {
            task!!.cancel(true)
        }
    }

    private fun loadBitmap(image :String) {
        Glide.with(itemView)
                .load(image)
                .thumbnail(0.1f)
                .apply(options)
                .into(imageView)
    }


    companion object {

        private var viewWidth = 0
        private var viewHeight = 0
    }

}