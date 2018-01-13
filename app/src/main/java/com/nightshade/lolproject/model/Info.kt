package com.nightshade.lolproject.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Info() :  RealmObject() {
    @PrimaryKey
    var parentid : String?=null
    var attack: String? = null
    var defense: String? = null
    var magic: String? = null
    var difficulty: String? = null


}