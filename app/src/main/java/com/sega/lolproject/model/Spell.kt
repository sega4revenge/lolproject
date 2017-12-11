package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Spell() : Parcelable {
    var id: String? = null
    var name: Name? = Name()
    var description: Description? = Description()
    var tooltip: Tooltip? = Tooltip()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)


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