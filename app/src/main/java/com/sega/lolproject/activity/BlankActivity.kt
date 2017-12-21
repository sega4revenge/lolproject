package com.sega.lolproject.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sega.lolproject.MyApplication
import com.sega.lolproject.R
import com.sega.lolproject.model.Champion
import com.sega.lolproject.presenter.SkinDetailPresenter
import com.sega.lolproject.services.RealmController
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_blank.*
import java.io.File
import java.io.FileOutputStream




class BlankActivity : AppCompatActivity(), SkinDetailPresenter.skinsDetail {
    override fun getListChampion(productlist: ArrayList<Champion>) {
        listChampion = productlist
        if (onlydata == 1)
        {
            if(MyApplication.getManager()!!.checkOnlyData())
            {
                println("database da ton tai")

            }
            else
            temp = MyApplication.getManager()!!.getResumeOnly()
        }

        else {
            if(MyApplication.getManager()!!.checkFullData())
            {
                println("database da ton tai")

            }
            else
                temp = (MyApplication.getManager()!!.getResumeFull())
        }

        mListChampions!!.getDatachampion(listChampion!![temp].id!!)
    }

    var onlydata = 0

    var temp = 0
    var tempskin = 0
    var tempiconspell = 0
    var activity: Activity? = null
    var rootFolder: File? = null
    var listChampion: ArrayList<Champion>? = null
    private var realm: Realm? = null
    var mListChampions: SkinDetailPresenter? = null
    val options = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
        activity = this
        rootFolder = File(filesDir.toString() + "/files")
        if (!rootFolder!!.exists()) {
            rootFolder!!.mkdirs()
        }
        this.realm = RealmController.with(this).realm
        mListChampions = SkinDetailPresenter(this)
        button.setOnClickListener {
            startActivity(Intent(this, ChampionListActivity::class.java))
        }
        data.setOnClickListener {
            tempskin = 0
            tempiconspell = 0
            mListChampions!!.getListChampions()
        }
        only.setOnClickListener {
            onlydata = 1
            mListChampions!!.getListChampions()
        }
    }

    fun downloadSkin(champion: Champion) {


        Glide.with(this)
                .asBitmap()
                .load(champion.skins!![tempskin]!!.imageLoading)
                .apply(options)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resourceLoading: Bitmap?, transition: Transition<in Bitmap>?) {
                        Glide.with(activity)
                                .asBitmap()
                                .load(champion.skins!![tempskin]!!.imageFull)
                                .apply(options)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resourceFull: Bitmap?, transition: Transition<in Bitmap>?) {

                                        try {


                                            val nameLoading = champion.id + "loading" + champion.skins!![tempskin]!!.num + ".jpg"
                                            val nameFull = champion.id + "full" + champion.skins!![tempskin]!!.num + ".jpg"
                                            var myDir = File(rootFolder, nameLoading)
                                            println(myDir)
                                            if (myDir.exists())
                                                myDir.delete()

                                            val out = FileOutputStream(myDir)

                                            resourceLoading?.compress(Bitmap.CompressFormat.JPEG, 90, out)

                                            champion.skins!![tempskin]!!.imageLoading = Uri.fromFile(myDir).toString()


                                            myDir = File(rootFolder, nameFull)
                                            println(myDir)
                                            if (myDir.exists())
                                                myDir.delete()

                                            val out2 = FileOutputStream(myDir)

                                            resourceFull?.compress(Bitmap.CompressFormat.JPEG, 90, out2)

                                            champion.skins!![tempskin]!!.imageFull = Uri.fromFile(myDir).toString()

                                            tempskin++
                                            out.flush()
                                            out.close()
                                            out2.flush()
                                            out2.close()

                                            if (tempskin < champion.skins!!.size) {
                                                downloadSkin(champion)
                                            } else {
                                                Glide.with(activity)
                                                        .asBitmap()
                                                        .load(champion.passive!!.imagePassive)
                                                        .apply(options)
                                                        .into(object : SimpleTarget<Bitmap>() {
                                                            override fun onResourceReady(resourcePassive: Bitmap?, transition: Transition<in Bitmap>?) {
                                                                Glide.with(activity)
                                                                        .asBitmap()
                                                                        .load(champion.imageAvatar)
                                                                        .apply(options)
                                                                        .into(object : SimpleTarget<Bitmap>() {
                                                                            override fun onResourceReady(resourceAvatar: Bitmap?, transition: Transition<in Bitmap>?) {

                                                                                try {

                                                                                    val namePassive = champion.id + "passive" + ".jpg"
                                                                                    val nameAvatar = champion.id + "avatar" + ".jpg"
                                                                                    myDir = File(rootFolder, namePassive)
                                                                                    println(myDir)
                                                                                    if (myDir.exists())
                                                                                        myDir.delete()

                                                                                    val out3 = FileOutputStream(myDir)

                                                                                    resourcePassive?.compress(Bitmap.CompressFormat.JPEG, 90, out3)

                                                                                    champion.passive!!.imagePassive = Uri.fromFile(myDir).toString()


                                                                                    myDir = File(rootFolder, nameAvatar)
                                                                                    println(myDir)
                                                                                    if (myDir.exists())
                                                                                        myDir.delete()

                                                                                    val out4 = FileOutputStream(myDir)

                                                                                    resourceAvatar?.compress(Bitmap.CompressFormat.JPEG, 90, out4)

                                                                                    champion.imageAvatar = Uri.fromFile(myDir).toString()


                                                                                    out3.flush()
                                                                                    out3.close()
                                                                                    out4.flush()
                                                                                    out4.close()
                                                                                    downloadIconspell(champion)


                                                                                } catch (e: Exception) {
                                                                                    println(e)
                                                                                }


                                                                            }
                                                                        })
                                                            }
                                                        })


                                            }
                                        } catch (e: Exception) {
                                            println(e)
                                        }

                                    }
                                })
                    }
                })


    }

    fun downloadIconspell(champion: Champion) {

        Glide.with(this)
                .asBitmap()
                .load(champion.spells!![tempiconspell]!!.image)
                .apply(options)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resourceSpell: Bitmap?, transition: Transition<in Bitmap>?) {
                        try {


                            val nameSpell = champion.id + "spell" + champion.spells!![tempiconspell]!!.id + ".jpg"
                            val myDir = File(rootFolder, nameSpell)
                            println(myDir)
                            if (myDir.exists())
                                myDir.delete()

                            val out = FileOutputStream(myDir)

                            resourceSpell?.compress(Bitmap.CompressFormat.JPEG, 90, out)

                            champion.spells!![tempiconspell]!!.image = Uri.fromFile(myDir).toString()




                            tempiconspell++
                            out.flush()
                            out.close()


                            if (tempiconspell < champion.spells!!.size) {
                                downloadIconspell(champion)
                            } else {
                                champion.allytips!!.parentid = champion.id
                                champion.enemytips!!.parentid = champion.id
                                champion.info!!.parentid =  champion.id
                                champion.stats!!.parentid =  champion.id
                                champion.partype!!.parentid =  champion.id
                                realm!!.beginTransaction()
                                realm!!.copyToRealmOrUpdate(champion)
                                realm!!.commitTransaction()

                                temp++
                                MyApplication.getManager()!!.update_fulldata_champion(temp)
                                if (temp < listChampion!!.size) {

                                    tempiconspell = 0
                                    tempskin = 0
                                    println(temp)
                                    println(listChampion!![temp])
                                    mListChampions!!.getDatachampion(listChampion!![temp].id!!)

                                } else {
                                    MyApplication.getManager()!!.storeFullData(true)
                                    tempskin = 0
                                    temp = 0
                                    tempiconspell = 0
                                }
                            }
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                })

    }

    override fun setErrorNotFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setErrorMessage(errorMessage: String) {
        println(errorMessage)
    }

    override fun getDetail(productlist: Champion) {
        if (onlydata == 1) {
            val champion = realm!!.where(Champion::class.java).equalTo("id", productlist.id).findFirst()
            if (champion != null) {
                productlist.imageAvatar = champion.imageAvatar
                productlist.passive!!.imagePassive = champion.passive!!.imagePassive
                for (i in 0..productlist.skins!!.size - 1) {
                    productlist.skins!![i]!!.imageFull = champion.skins!![i]!!.imageFull
                    productlist.skins!![i]!!.imageLoading = champion.skins!![i]!!.imageLoading
                }
                for (i in 0..productlist.spells!!.size - 1) {
                    productlist.spells!![i]!!.image = champion.spells!![i]!!.image
                }
                productlist.allytips!!.parentid =  productlist.id
                productlist.enemytips!!.parentid =  productlist.id
                productlist.info!!.parentid = productlist.id
                productlist.stats!!.parentid = productlist.id
                productlist.partype!!.parentid = productlist.id
                realm!!.beginTransaction()
                realm!!.copyToRealmOrUpdate(productlist)
                realm!!.commitTransaction()
            } else {
                productlist.allytips!!.parentid =  productlist.id
                productlist.enemytips!!.parentid = productlist.id
                for (i in 0..productlist.skins!!.size - 1) {

                    productlist.skins!![i]!!.imageFull = productlist.skins!![i]!!.imageFull
                    productlist.skins!![i]!!.imageLoading = productlist.skins!![i]!!.imageLoading
                }
                for (i in 0..productlist.spells!!.size - 1) {

                    productlist.spells!![i]!!.image = productlist.spells!![i]!!.image
                }
                productlist.allytips!!.parentid =  productlist.id
                productlist.enemytips!!.parentid =  productlist.id

                productlist.info!!.parentid = productlist.id
                productlist.stats!!.parentid = productlist.id
                productlist.partype!!.parentid = productlist.id
                realm!!.beginTransaction()
                realm!!.copyToRealmOrUpdate(productlist)
                realm!!.commitTransaction()
            }
            MyApplication.getManager()!!.update_onlydata_champion(temp)
            temp++

            if (temp < listChampion!!.size) {
                mListChampions!!.getDatachampion(listChampion!![temp].id!!)
                println(listChampion!![temp])
            } else {
                MyApplication.getManager()!!.storeOnlylData(true)
            }

        } else {
            downloadSkin(productlist)
        }

    }

}
