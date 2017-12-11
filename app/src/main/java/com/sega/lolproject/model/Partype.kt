package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Partype() : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<Partype> {
        override fun createFromParcel(parcel: Parcel): Partype {
            return Partype(parcel)
        }

        override fun newArray(size: Int): Array<Partype?> {
            return arrayOfNulls(size)
        }
    }


}