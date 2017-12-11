package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Description() : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<Description> {
        override fun createFromParcel(parcel: Parcel): Description {
            return Description(parcel)
        }

        override fun newArray(size: Int): Array<Description?> {
            return arrayOfNulls(size)
        }
    }


}