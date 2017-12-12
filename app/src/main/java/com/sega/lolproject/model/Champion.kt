package com.sega.lolproject.model

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Sega on 23/03/2017.
 */


open class Champion : RealmObject() {
   var id : String ?= null
   var key : String ? = null
   var name : String ? = null
   var title    : Title ? = Title()
   var imageAvatar : String ?= null
   var skins : RealmList<Skin>? = null
   var lore : Lore ?= Lore()
   var blurb : Blurb ?= Blurb()
   var allytips : Allytips?= Allytips()
   var enemytips : Enemytips?= Enemytips()
   var tags : RealmList<String> ?= null
   var partype : Partype ?= Partype()
   var info : Info ?= Info()
   var stats : Stat ?= null
   var spells : RealmList<Spell> ?= null
   var passive :Passive ?= null



}