package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Spell() : Parcelable {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var tooltip: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        tooltip = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(tooltip)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spell> {
        override fun createFromParcel(parcel: Parcel): Spell {
            return Spell(parcel)
        }

        override fun newArray(size: Int): Array<Spell?> {
            return arrayOfNulls(size)
        }
    }


}