package com.nightshade.lolproject.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Skin:  RealmObject() {
    @PrimaryKey
    var id: String? = null
    var num: String? = null
    var name: Name? = Name()
    var chromas: String? = null
    var price : Price ?= Price()
    var type : String ?= null
    var imageLoading : String? = null
    var imageFull : String? = null
    var link : String ?= null

}