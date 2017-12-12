package com.sega.lolproject.model

import io.realm.RealmObject

/**
 * Created by sega4 on 06/12/2017.
 */
open class Name :  RealmObject() {
    var en: String? = null
    var vn: String? = null

}