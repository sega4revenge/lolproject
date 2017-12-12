package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Skin() : Parcelable {
    var id: String? = null
    var num: String? = null
    var name: Name? = Name()
    var chromas: String? = null
    var price : Price ?= Price()
    var type : String ?= null
    var link : String ?= null
    var imageLoading : String? = null
    var imageFull : String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        num = parcel.readString()

        chromas = parcel.readString()

        type = parcel.readString()
        link = parcel.readString()
        imageLoading = parcel.readString()
        imageFull = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(num)

        parcel.writeString(chromas)

        parcel.writeString(type)
        parcel.writeString(link)
        parcel.writeString(imageLoading)
        parcel.writeString(imageFull)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Skin> {
        override fun createFromParcel(parcel: Parcel): Skin {
            return Skin(parcel)
        }

        override fun newArray(size: Int): Array<Skin?> {
            return arrayOfNulls(size)
        }
    }


}