package com.sega.lolproject.model

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by sega4 on 06/12/2017.
 */
open class Enemytips : RealmObject() {
    var en: RealmList<String>? = RealmList()
    var vn: RealmList<String>? = RealmList()

}