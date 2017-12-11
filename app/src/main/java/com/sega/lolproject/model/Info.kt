package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Info() : Parcelable {
    var attack: String? = null
    var defense: String? = null
    var magic: String? = null
    var difficulty: String? = null

    constructor(parcel: Parcel) : this() {
        attack = parcel.readString()
        defense = parcel.readString()
        magic = parcel.readString()
        difficulty = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(attack)
        parcel.writeString(defense)
        parcel.writeString(magic)
        parcel.writeString(difficulty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Info> {
        override fun createFromParcel(parcel: Parcel): Info {
            return Info(parcel)
        }

        override fun newArray(size: Int): Array<Info?> {
            return arrayOfNulls(size)
        }
    }

}