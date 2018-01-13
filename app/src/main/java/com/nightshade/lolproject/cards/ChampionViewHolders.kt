package com.nightshade.lolproject.cards

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.nightshade.lolproject.R

class ChampionViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView){

    var countryName: TextView
    var countryPhoto: ImageView

    init {
        countryName = itemView.findViewById<View>(R.id.champion_name) as TextView
        countryPhoto = itemView.findViewById<View>(R.id.champion_photo) as ImageView
    }


}