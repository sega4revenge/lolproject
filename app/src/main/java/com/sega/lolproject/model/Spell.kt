package com.sega.lolproject.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Spell() :  RealmObject() {
    @PrimaryKey
    var id: String? = null
    var name: Name? = Name()
    var description: Description? = Description()
    var tooltip: Tooltip? = Tooltip()
    var image : String?=null

}