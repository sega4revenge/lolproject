package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Passive() : Parcelable {
    var name: String? = null
    var description: String? = null
    var imagePassive: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        description = parcel.readString()
        imagePassive = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(imagePassive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Passive> {
        override fun createFromParcel(parcel: Parcel): Passive {
            return Passive(parcel)
        }

        override fun newArray(size: Int): Array<Passive?> {
            return arrayOfNulls(size)
        }
    }
}