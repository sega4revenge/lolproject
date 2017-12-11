package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Blurb() : Parcelable {
    var en: String? = null
    var vn: String? = null

    constructor(parcel: Parcel) : this() {
        en = parcel.readString()
        vn = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(en)
        parcel.writeString(vn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Blurb> {
        override fun createFromParcel(parcel: Parcel): Blurb {
            return Blurb(parcel)
        }

        override fun newArray(size: Int): Array<Blurb?> {
            return arrayOfNulls(size)
        }
    }


}