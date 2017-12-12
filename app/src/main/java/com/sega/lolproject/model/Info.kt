package com.sega.lolproject.model

import io.realm.RealmObject

/**
 * Created by sega4 on 06/12/2017.
 */
open class Info() :  RealmObject() {
    var attack: String? = null
    var defense: String? = null
    var magic: String? = null
    var difficulty: String? = null


}