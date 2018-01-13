package com.nightshade.lolproject.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Stat() : RealmObject() {
    @PrimaryKey
    var parentid : String?=null
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


}