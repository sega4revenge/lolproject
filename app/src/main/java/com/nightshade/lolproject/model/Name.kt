package com.nightshade.lolproject.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Name :  RealmObject() {
    @PrimaryKey
    var en: String? = null
    var vn: String? = null
}