package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Sega on 23/03/2017.
 */


class Champion() : Parcelable {
   var id : String ?= null
   var key : String ? = null
   var name : String ? = null
   var title    : String ? = null
   var imageAvatar : String ?= null
   var skins : ArrayList<Skin> ?= ArrayList()
   var lore : String ?= null
   var blurb : String ?= null
   var allytips : ArrayList<String> ?= ArrayList()
   var enemytips : ArrayList<String> ?= ArrayList()
   var tags : ArrayList<String> ?= ArrayList()
   var partype : String ?= null
   var info : Info ?= null
   var stats : Stat ?= null
   var spells : ArrayList<Spell> ?= ArrayList()
   var passive :Passive ?= null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        key = parcel.readString()
        name = parcel.readString()
        title = parcel.readString()
        imageAvatar = parcel.readString()
        lore = parcel.readString()
        blurb = parcel.readString()
        partype = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(title)
        parcel.writeString(imageAvatar)
        parcel.writeString(lore)
        parcel.writeString(blurb)
        parcel.writeString(partype)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Champion> {
        override fun createFromParcel(parcel: Parcel): Champion {
            return Champion(parcel)
        }

        override fun newArray(size: Int): Array<Champion?> {
            return arrayOfNulls(size)
        }
    }


}