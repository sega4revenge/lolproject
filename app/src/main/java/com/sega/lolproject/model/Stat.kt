package com.sega.lolproject.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sega4 on 06/12/2017.
 */
class Stat() : Parcelable {
    var armor : String? = null
    var armorperlevel : String? = null
    var attackdamage : String? = null
    var attackdamageperlevel: String? = null
    var attackrange : String? = null
    var attackspeedoffset : String? = null
    var attackspeedperlevel : String? = null
    var crit : String? = null
    var critperlevel : String? = null
    var hp : String? = null
    var hpperlevel : String? = null
    var hpregen : String? = null
    var hpregenperlevel : String? = null
    var movespeed : String? = null
    var mp : String? = null
    var mpperlevel : String? = null
    var mpregen : String? = null
    var mpregenperlevel : String? = null
    var spellblock : String? = null
    var spellblockperlevel : String? = null

    constructor(parcel: Parcel) : this() {
        armor = parcel.readString()
        armorperlevel = parcel.readString()
        attackdamage = parcel.readString()
        attackdamageperlevel = parcel.readString()
        attackrange = parcel.readString()
        attackspeedoffset = parcel.readString()
        attackspeedperlevel = parcel.readString()
        crit = parcel.readString()
        critperlevel = parcel.readString()
        hp = parcel.readString()
        hpperlevel = parcel.readString()
        hpregen = parcel.readString()
        hpregenperlevel = parcel.readString()
        movespeed = parcel.readString()
        mp = parcel.readString()
        mpperlevel = parcel.readString()
        mpregen = parcel.readString()
        mpregenperlevel = parcel.readString()
        spellblock = parcel.readString()
        spellblockperlevel = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(armor)
        parcel.writeString(armorperlevel)
        parcel.writeString(attackdamage)
        parcel.writeString(attackdamageperlevel)
        parcel.writeString(attackrange)
        parcel.writeString(attackspeedoffset)
        parcel.writeString(attackspeedperlevel)
        parcel.writeString(crit)
        parcel.writeString(critperlevel)
        parcel.writeString(hp)
        parcel.writeString(hpperlevel)
        parcel.writeString(hpregen)
        parcel.writeString(hpregenperlevel)
        parcel.writeString(movespeed)
        parcel.writeString(mp)
        parcel.writeString(mpperlevel)
        parcel.writeString(mpregen)
        parcel.writeString(mpregenperlevel)
        parcel.writeString(spellblock)
        parcel.writeString(spellblockperlevel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stat> {
        override fun createFromParcel(parcel: Parcel): Stat {
            return Stat(parcel)
        }

        override fun newArray(size: Int): Array<Stat?> {
            return arrayOfNulls(size)
        }
    }


}