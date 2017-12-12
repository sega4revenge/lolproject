package com.sega.lolproject.model

import io.realm.RealmObject

/**
 * Created by sega4 on 06/12/2017.
 */
open class Passive :  RealmObject() {
    var name: Name? = Name()
    var description: Description? = Description()
    var imagePassive: String? = null


}