package com.sega.lolproject.cards


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sega.lolproject.R
import com.sega.lolproject.model.Skin
import io.realm.RealmList


class SliderAdapter(private val content: RealmList<Skin>, private val count: Int, private val listener: View.OnClickListener?) : RecyclerView.Adapter<SliderCard>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderCard {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_slider_card, parent, false)

        if (listener != null) {
            view.setOnClickListener { view ->
                listener.onClick(view) }
        }

        return SliderCard(view)
    }

    override fun onBindViewHolder(holder: SliderCard, position: Int) {
        val arrayskin = Array(content.size, { i -> content[i]!!.imageLoading })
        println(arrayskin.size.toString() + "ffdhfh")
        holder.setContent(arrayskin[position % arrayskin.size].toString())
//        holder.itemView.setOnClickListener {
//            Log.e("gdfg",arrayskin[position])
////            val intent = Intent(context, DetailsActivity::class.java)
////            intent.putExtra(Constants.BUNDLE_IMAGE_ID,arrayskin[position])
//        }
    }

    override fun onViewRecycled(holder: SliderCard?) {
        holder!!.clearContent()
    }

    override fun getItemCount(): Int {
        return count
    }

}
