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
   var title    : Title ? = Title()
   var imageAvatar : String ?= null
   var skins : ArrayList<Skin> ?= ArrayList()
   var lore : Lore ?= Lore()
   var blurb : Blurb ?= Blurb()
   var allytips : Allytips?= Allytips()
   var enemytips : Enemytips?= Enemytips()
   var tags : ArrayList<String> ?= ArrayList()
   var partype : Partype ?= Partype()
   var info : Info ?= Info()
   var stats : Stat ?= null
   var spells : ArrayList<Spell> ?= ArrayList()
   var passive :Passive ?= null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        key = parcel.readString()
        name = parcel.readString()

        imageAvatar = parcel.readString()



    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(key)
        parcel.writeString(name)

        parcel.writeString(imageAvatar)



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