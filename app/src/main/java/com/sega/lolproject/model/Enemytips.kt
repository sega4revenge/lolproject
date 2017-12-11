package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Enemytips() : Parcelable {
    var en: ArrayList<String>? = ArrayList()
    var vn: ArrayList<String>? = ArrayList()
    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }



    constructor(parcel: Parcel) : this()

    companion object CREATOR : Parcelable.Creator<Enemytips> {
        override fun createFromParcel(parcel: Parcel): Enemytips {
            return Enemytips(parcel)
        }

        override fun newArray(size: Int): Array<Enemytips?> {
            return arrayOfNulls(size)
        }
    }


}