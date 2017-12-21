package com.sega.lolproject.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sega4 on 06/12/2017.
 */
open class Enemytips : RealmObject() {
    @PrimaryKey
    var parentid : String?=null
    var en: RealmList<String>? = RealmList()
    var vn: RealmList<String>? = RealmList()

}