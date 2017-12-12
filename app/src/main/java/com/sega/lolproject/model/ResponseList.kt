package com.sega.lolproject.model

import io.realm.RealmList

/**
 * Created by Sega on 23/03/2017.
 */


class ResponseList {
    val status : Int?=null
    var listchampion : RealmList<Champion>? = null
}