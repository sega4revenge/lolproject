package com.sega.lolproject.model

import io.realm.RealmObject

/**
 * Created by sega4 on 06/12/2017.
 */
open class Spell() :  RealmObject() {
    var id: String? = null
    var name: Name? = Name()
    var description: Description? = Description()
    var tooltip: Tooltip? = Tooltip()
    var image : String?=null

}