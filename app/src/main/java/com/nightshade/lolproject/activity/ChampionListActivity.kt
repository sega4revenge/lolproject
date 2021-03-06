package com.nightshade.lolproject.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.lapism.searchview.SearchView
import com.nightshade.lolproject.MyApplication
import com.nightshade.lolproject.R
import com.nightshade.lolproject.cards.ChampionAdapter
import com.nightshade.lolproject.customview.FadeInAnimator
import com.nightshade.lolproject.model.Champion
import com.nightshade.lolproject.presenter.SkinDetailPresenter
import com.nightshade.lolproject.services.RealmController
import kotlinx.android.synthetic.main.activity_list_champ.*




/**
 * Created by sega4 on 16/12/2017.
 */

class ChampionListActivity : AppCompatActivity(), ChampionAdapter.OnchampionClickListener, SkinDetailPresenter.skinsDetail {
    override fun setErrorNotFound() {

    }

    override fun setErrorMessage(errorMessage: String) {
      println(errorMessage)
    }

    override fun getDetail(productlist: Champion) {

    }

    override fun getListChampion(productlist: ArrayList<Champion>) {
        listChampion = productlist
        initalize()
    }

    override fun onchampionClicked(position: Int) {
        val intent = Intent(this, DetailChampionActivity::class.java)
        intent.putExtra("id", rcAdapter!!.championList[position].id)
        startActivity(intent)
    }

    var search: String? = ""
    var querry: String? = ""
    private var lLayout: GridLayoutManager? = null
    var rcAdapter: ChampionAdapter? = null
    var listChampion: ArrayList<Champion> = ArrayList()
    private var mListChampions: SkinDetailPresenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_champ)
        mListChampions = SkinDetailPresenter(this)
        window.setBackgroundDrawableResource(R.drawable.background)
        searchView.setNavigationIcon(null)

        val adRequest = AdRequest.Builder()
                .addTestDevice("90EB406796F0320FB33422EF5287E80D")
                .build()

        adView.loadAd(adRequest)
        adView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                println("Loi "+ errorCode )
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        }



        if(MyApplication.getManager()!!.checkFullData())
        {
            listChampion = RealmController.with(this).champions
            initalize()
        }
        else
        {
            mListChampions!!.getListChampions()
        }

    }

    public override fun onPause() {
        if (adView != null) {
            adView.pause()
        }
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        if (adView != null) {
            adView.resume()
        }
    }

    public override fun onDestroy() {
        if (adView != null) {
            adView.destroy()
        }
        super.onDestroy()
    }
    fun initalize(){
        rcAdapter = ChampionAdapter(this@ChampionListActivity, this)
        rcAdapter!!.championList.addAll(listChampion)
        println(rcAdapter!!.championList.size)
        lLayout = GridLayoutManager(application, getNumberOfColumns())
        listchampion_view.setHasFixedSize(true)
        listchampion_view.layoutManager = this.lLayout
        listchampion_view.adapter = rcAdapter
        listchampion_view.itemAnimator = FadeInAnimator()
        listchampion_view.itemAnimator.addDuration = 50
        listchampion_view.itemAnimator.removeDuration = 50
        initSearch()
        initButtonFilter()

    }
    fun initSearch() {
        if (searchView != null) {
            searchView.versionMargins = SearchView.VersionMargins.TOOLBAR_SMALL
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.close(false)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    search = newText
                    search()
                    return true
                }
            })

            searchView.setVoiceText("Set permission on Android 6.0+ !")
            searchView.setOnVoiceIconClickListener(object : SearchView.OnVoiceIconClickListener {
                override fun onVoiceIconClick() {
                    // permission
                }
            })
        }
    }

    fun search() {

        val filteredModelList: ArrayList<Champion> = ArrayList()
        for (model in listChampion) {
            val text = model.name!!.toLowerCase()
            if (text.contains(search!!) && ((model.tags!!.contains(querry)) || querry.equals(""))) {
                filteredModelList.add(model)
            }
        }
        rcAdapter!!.animateTo(filteredModelList)
        listchampion_view.scrollToPosition(0)
    }

    private fun initButtonFilter() {
        marksman.setOnClickListener {
            if (querry.equals("Marksman")) {
                querry = ""
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Marksman"
                marksman.background = resources.getDrawable(R.drawable.round)
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
                support.setBackgroundColor(resources.getColor(R.color.transparent))
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            search()
        }
        mage.setOnClickListener {
            if (querry.equals("Mage")) {
                querry = ""
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Mage"
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
                mage.background = resources.getDrawable(R.drawable.round)
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
                support.setBackgroundColor(resources.getColor(R.color.transparent))
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            search()
        }
        fighter.setOnClickListener {
            if (querry.equals("Fighter")) {
                querry = ""
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Fighter"
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
                fighter.background = resources.getDrawable(R.drawable.round)
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
                support.setBackgroundColor(resources.getColor(R.color.transparent))
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            search()
        }
        tank.setOnClickListener {
            if (querry.equals("Tank")) {
                querry = ""
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Tank"
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
                tank.background = resources.getDrawable(R.drawable.round)
                support.setBackgroundColor(resources.getColor(R.color.transparent))
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            search()
        }
        support.setOnClickListener {
            if (querry.equals("Support")) {
                querry = ""
                support.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Support"
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
                support.background = resources.getDrawable(R.drawable.round)
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            search()
        }
        assasin.setOnClickListener {
            if (querry.equals("Assassin")) {
                querry = ""
                assasin.setBackgroundColor(resources.getColor(R.color.transparent))
            } else {
                querry = "Assassin"
                marksman.setBackgroundColor(resources.getColor(R.color.transparent))
                mage.setBackgroundColor(resources.getColor(R.color.transparent))
                fighter.setBackgroundColor(resources.getColor(R.color.transparent))
                tank.setBackgroundColor(resources.getColor(R.color.transparent))
                support.setBackgroundColor(resources.getColor(R.color.transparent))
                assasin.background = resources.getDrawable(R.drawable.round)
            }
            search()
        }
    }

    private fun getNumberOfColumns(): Int {
        // Get screen width
        val displayMetrics = this.resources.displayMetrics
        val widthPx = displayMetrics.widthPixels.toFloat()


        val desiredPx = 280
        val columns = Math.round(widthPx / desiredPx)
        return if (columns > 2) columns else 2

    }

}