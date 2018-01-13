package com.nightshade.lolproject.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.StyleRes
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.nightshade.lolproject.MyApplication
import com.nightshade.lolproject.R
import com.nightshade.lolproject.cards.SliderAdapter
import com.nightshade.lolproject.lib.detail.CardSliderLayoutManager
import com.nightshade.lolproject.lib.detail.CardSnapHelper
import com.nightshade.lolproject.model.Champion
import com.nightshade.lolproject.presenter.SkinDetailPresenter
import com.nightshade.lolproject.services.RealmController
import com.nightshade.lolproject.util.DecodeBitmapTask
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by sega4 on 18/12/2017.
 */
class DetailChampionActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,SkinDetailPresenter.skinsDetail {
    override fun setErrorNotFound() {

    }

    override fun setErrorMessage(errorMessage: String) {
       println(errorMessage)
    }

    override fun getDetail(productlist: Champion) {
        champion = productlist
        initalize()
    }

    override fun getListChampion(productlist: ArrayList<Champion>) {

    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        if (null == p1) return
        youTubePlayer = p1

        if (!p2) {
            youTubePlayer!!.cueVideo(setCueVideo())

        }    }
       fun setCueVideo(): String? {
               return linkSkin
    }
    companion object {
                val API_KEY = "AIzaSyCXAMVTzLpym1OnufwXhW3_jWy98dmfjto"


    }
    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show()
        println(p1)
        println(p0)
    }
        var youTubePlayerView : YouTubePlayerView?= null
    var youTubePlayer : YouTubePlayer ?= null
    var linkSkin : String ?= null
    private var mListChampions: SkinDetailPresenter?=null

    var querry: String? = ""

    private var sliderAdapter  : SliderAdapter?=null

    private var layoutManger: CardSliderLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var priceSwitcher: TextSwitcher? = null
    private var placeSwitcher: TextSwitcher? = null
    private var clockSwitcher: TextSwitcher? = null
    private var currentPosition: Int = 0
    var champion : Champion?=null
    private var decodeMapBitmapTask: DecodeBitmapTask? = null
    private val options = RequestOptions()
            .centerCrop()
            .placeholder(R.color.transparent)
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mListChampions = SkinDetailPresenter(this)
        window.setBackgroundDrawableResource(R.drawable.background)
        if(MyApplication.getManager()!!.checkFullData())
        {
            champion =  RealmController.with(this).getChampion(intent.getStringExtra("id"))
            initalize()
        }

        else
        {
            mListChampions!!.getDatachampion(intent.getStringExtra("id"))
        }

    }
    fun initalize(){
        if(MyApplication.getManager()!!.checkFullData()){
            passive.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.passive?.imagePassive)))
            skillQ.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.spells!![0]?.image)))
            skillW.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.spells!![1]?.image)))
            skillE.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.spells!![2]?.image)))
            skillR.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.spells!![3]?.image)))

        }
        else {
            Glide.with(this)
                    .load(champion!!.passive?.imagePassive)
                    .apply(options)
                    .into(passive)
            Glide.with(this)
                    .load(champion!!.spells!![0]?.image)
                    .apply(options)
                    .into(skillQ)
            Glide.with(this)
                    .load(champion!!.spells!![1]?.image)
                    .apply(options)
                    .into(skillW)
            Glide.with(this)
                    .load(champion!!.spells!![2]?.image)
                    .apply(options)
                    .into(skillE)
            Glide.with(this)
                    .load(champion!!.spells!![3]?.image)
                    .apply(options)
                    .into(skillR)

        }

        querry = "Passive"
        passive.background = resources.getDrawable(R.drawable.round)
        skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
        skillW.setBackgroundColor(resources.getColor(R.color.transparent))
        skillE.setBackgroundColor(resources.getColor(R.color.transparent))
        skillR.setBackgroundColor(resources.getColor(R.color.transparent))
        setSkill(querry!!,0)

        attack.progress = champion!!.info?.attack!!.toInt() * 10

        magic.progress = champion!!.info?.magic!!.toInt() * 10

        defense.progress = champion!!.info?.defense!!.toInt() * 10

        difficult.progress = champion!!.info?.difficulty!!.toInt() * 10

        sliderAdapter = SliderAdapter(champion!!.skins!!, champion!!.skins!!.size,  OnCardClickListener())
        val adapter_level = ArrayAdapter<String> (this,R.layout.spinner_item, resources.getStringArray(R.array.level))
        adapter_level.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner_level.adapter = adapter_level
        spinner_level.setSelection(0)
        spinner_level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val a = resources.getStringArray(R.array.level)
//                Log.e("SELECT",a[pos])
                when(pos) {
                    0 -> getFullStat118(champion!!)
                    else -> getFullStatLevel(a[pos],champion!!)
                }


            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }

        }
        if(MyApplication.getManager()!!.checkFullData())
        avattarChamp.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(champion!!.imageAvatar)))
        else
            Glide.with(this)
                    .load(champion!!.imageAvatar)
                    .apply(options)
                    .into(avattarChamp)
        countries_text.text =    (champion!!.name + " - " + champion!!.title!!.en)
        countries_text!!.typeface = Typeface.createFromAsset(assets, "open-sans-extrabold.ttf")
        initRecyclerView()
        initSwitchers()
        initButtonFilter()
    }
    fun getFullStat118(champion : Champion) {
                health.text = champion.stats?.hp + " - " + getStat("18", champion.stats?.hp!!, champion.stats?.hpperlevel!!)
        healthregen.text = champion.stats?.hpregen + " - " + getStat("18", champion.stats?.hpregen!!, champion.stats?.hpregenperlevel!!)
        mana.text = champion.stats?.mp + " - " + getStat("18", champion.stats?.mp!!, champion.stats?.mpperlevel!!)
        manaregen.text = champion.stats?.mpregen + " - " + getStat("18", champion.stats?.mpregen!!, champion.stats?.mpregenperlevel!!)
        range.text = champion.stats?.attackrange
        attackdmg.text = champion.stats?.attackdamage + " - " + getStat("18", champion.stats?.attackdamage!!, champion.stats?.attackdamageperlevel!!)
        attackspeed.text = getAttackSpeed(champion.stats?.attackspeedoffset!!).toString()
        armor.text = champion.stats?.armor + " - " + getStat("18", champion.stats?.armor!!, champion.stats?.armorperlevel!!)
        magicresist.text = champion.stats?.spellblock + " - " + getStat("18", champion.stats?.spellblock!!, champion.stats?.spellblockperlevel!!)
        movespeed.text = champion.stats?.movespeed
    }
        fun getFullStatLevel(level : String , champion : Champion){
            health.text = getStat(level, champion.stats?.hp!!,champion.stats?.hpperlevel!!).toString()
            healthregen.text = getStat(level, champion.stats?.hpregen!!,champion.stats?.hpregenperlevel!!).toString()
            mana.text = getStat(level, champion.stats?.mp!!,champion.stats?.mpperlevel!!).toString()
            manaregen.text = getStat(level, champion.stats?.mpregen!!,champion.stats?.mpregenperlevel!!).toString()
            range.text = champion.stats?.attackrange
            attackdmg.text = getStat(level, champion.stats?.attackdamage!!,champion.stats?.attackdamageperlevel!!).toString()
            attackspeed.text =  getAttackSpeed(champion.stats?.attackspeedoffset!!).toString()
            armor.text = getStat(level, champion.stats?.armor!!,champion.stats?.armorperlevel!!).toString()
            magicresist.text = getStat(level, champion.stats?.spellblock!!,champion.stats?.spellblockperlevel!!).toString()
            movespeed.text = champion.stats?.movespeed
        }
    fun  getStat(level : String, base : String, growth : String ): Double {
                val stat = base.toDouble() + growth.toDouble() * (level.toInt() - 1) * (0.685 + 0.0175 * level.toInt())
        return Math.round(stat*10)/10.toDouble()
    }
    fun getAttackSpeed(attackdelay : String): Double {
                val speed = 0.625/(1+attackdelay.toDouble())
        return Math.round(speed*1000)/1000.toDouble()
    }
    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView!!.adapter = sliderAdapter
        recyclerView!!.setHasFixedSize(true)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange()
                }
            }
        })

        layoutManger = recyclerView!!.layoutManager as CardSliderLayoutManager

        CardSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing && decodeMapBitmapTask != null) {
            decodeMapBitmapTask!!.cancel(true)
        }
    }

    private fun initSwitchers() {
        priceSwitcher = findViewById(R.id.ts_price) as TextSwitcher
        priceSwitcher!!.setFactory(TextViewFactory(R.style.TemperatureTextView, true))
        priceSwitcher!!.setCurrentText("220")

        placeSwitcher = findViewById(R.id.ts_place) as TextSwitcher
        placeSwitcher!!.setFactory(TextViewFactory(R.style.PlaceTextView, false))
        placeSwitcher!!.setCurrentText(champion!!.skins!![0]!!.name!!.en)

        clockSwitcher = findViewById(R.id.ts_clock) as TextSwitcher
        clockSwitcher!!.setFactory(TextViewFactory(R.style.ClockTextView, false))
        clockSwitcher!!.setCurrentText(champion!!.skins!![0]!!.num)



        linkSkin = champion!!.skins!![0]!!.link
        youTubePlayerView = youtube_player_view
        youTubePlayerView?.initialize(API_KEY, this)


    }
    fun setSkill(querry : String, pos: Int){
        if(querry.equals("Passive")){
            name_skill.text = champion?.passive?.name?.en
            description_skill.text = champion?.passive?.description?.en
        }
        else {
            name_skill.text = champion?.spells!![pos]?.name?.en
            description_skill.text = champion?.spells!![pos]?.description?.en
        }
    }
    private fun initButtonFilter() {
        passive.setOnClickListener {
//            if (querry.equals("Passive")) {
//                querry = ""
//                passive.setBackgroundColor(resources.getColor(R.color.transparent))
//            } else {
                querry = "Passive"
                passive.background = resources.getDrawable(R.drawable.round)
                skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
                skillW.setBackgroundColor(resources.getColor(R.color.transparent))
                skillE.setBackgroundColor(resources.getColor(R.color.transparent))
                skillR.setBackgroundColor(resources.getColor(R.color.transparent))
//            }
            setSkill(querry!!,0)
        }
        skillQ.setOnClickListener {
//            if (querry.equals("SkillQ")) {
//                querry = ""
//                skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
//            } else {
                querry = "SkillQ"
                skillQ.background = resources.getDrawable(R.drawable.round)
                passive.setBackgroundColor(resources.getColor(R.color.transparent))
                skillW.setBackgroundColor(resources.getColor(R.color.transparent))
                skillE.setBackgroundColor(resources.getColor(R.color.transparent))
                skillR.setBackgroundColor(resources.getColor(R.color.transparent))

//            }
            setSkill(querry!!,0)
        }
        skillW.setOnClickListener {
//            if (querry.equals("SkillW")) {
//                querry = ""
//                skillW.setBackgroundColor(resources.getColor(R.color.transparent))
//            } else {
                querry = "SkillW"
                passive.setBackgroundColor(resources.getColor(R.color.transparent))
                skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
                skillW.background = resources.getDrawable(R.drawable.round)
                skillE.setBackgroundColor(resources.getColor(R.color.transparent))
                skillR.setBackgroundColor(resources.getColor(R.color.transparent))
//            }
            setSkill(querry!!,1)
        }
        skillE.setOnClickListener {
//            if (querry.equals("SkillE")) {
//                querry = ""
//                skillE.setBackgroundColor(resources.getColor(R.color.transparent))
//            } else {
                querry = "SkillE"
                passive.setBackgroundColor(resources.getColor(R.color.transparent))
                skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
                skillW.setBackgroundColor(resources.getColor(R.color.transparent))
                skillE.background = resources.getDrawable(R.drawable.round)
                skillR.setBackgroundColor(resources.getColor(R.color.transparent))
//            }
            setSkill(querry!!,2)
        }
        skillR.setOnClickListener {
//            if (querry.equals("SkillR")) {
//                querry = ""
//                skillR.setBackgroundColor(resources.getColor(R.color.transparent))
//            } else {
                querry = "SkillR"
                passive.setBackgroundColor(resources.getColor(R.color.transparent))
                skillQ.setBackgroundColor(resources.getColor(R.color.transparent))
                skillW.setBackgroundColor(resources.getColor(R.color.transparent))
                skillE.setBackgroundColor(resources.getColor(R.color.transparent))
                skillR.background = resources.getDrawable(R.drawable.round)
//            }
            setSkill(querry!!,3)
        }
    }




    private fun onActiveCardChange() {
        val pos = layoutManger!!.activeCardPosition
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return
        }

        onActiveCardChange(pos)
    }

    private fun onActiveCardChange(pos: Int) {
        val animH = intArrayOf(R.anim.slide_in_right, R.anim.slide_out_left)
        val animV = intArrayOf(R.anim.slide_in_top, R.anim.slide_out_bottom)

        val left2right = pos < currentPosition
        if (left2right) {
            animH[0] = R.anim.slide_in_left
            animH[1] = R.anim.slide_out_right

            animV[0] = R.anim.slide_in_bottom
            animV[1] = R.anim.slide_out_top
        }



    /*    priceSwitcher!!.setInAnimation(this@DetailChampionActivity, animH[0])
        priceSwitcher!!.setOutAnimation(this@DetailChampionActivity, animH[1])
        priceSwitcher!!.setText("220")*/

        placeSwitcher!!.setInAnimation(this@DetailChampionActivity, animV[0])
        placeSwitcher!!.setOutAnimation(this@DetailChampionActivity, animV[1])
        placeSwitcher!!.setText(champion!!.skins!![pos % champion!!.skins!!.size]!!.name!!.en)
      /*  clockSwitcher!!.setInAnimation(this@DetailChampionActivity, animV[0])
        clockSwitcher!!.setOutAnimation(this@DetailChampionActivity, animV[1])
        clockSwitcher!!.setText("Tối thượng")*/


        linkSkin = champion!!.skins!![pos % champion!!.skins!!.size]!!.link!!
        println(linkSkin)
        if ( youTubePlayer != null) {
            try {
                youTubePlayer!!.cueVideo(linkSkin)
            } catch (e: IllegalStateException) {
                youTubePlayerView?.initialize(API_KEY, this)
            }

        }
        currentPosition = pos

    }
    private inner class TextViewFactory internal constructor(@param:StyleRes @field:StyleRes internal val styleId: Int, internal val center: Boolean) : ViewSwitcher.ViewFactory {

        override fun makeView(): View {
            val textView = TextView(this@DetailChampionActivity)
            if(styleId == R.style.TemperatureTextView)
            {
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.riot_point),null)
                textView.compoundDrawablePadding = 7
            }


            if (center) {
                textView.gravity = Gravity.CENTER
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(this@DetailChampionActivity, styleId)
            } else {
                textView.setTextAppearance(styleId)
            }

            return textView
        }

    }

    private inner class ImageViewFactory : ViewSwitcher.ViewFactory {
        override fun makeView(): View {
            val imageView = ImageView(this@DetailChampionActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.layoutParams = lp

            return imageView
        }
    }

    private inner class OnCardClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val lm = recyclerView!!.layoutManager as CardSliderLayoutManager
            if (lm.isSmoothScrolling) {
                return
            }

            val activeCardPosition = lm.activeCardPosition
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return
            }

            val clickedPosition = recyclerView!!.getChildAdapterPosition(view)
            if (clickedPosition == activeCardPosition) {
//                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
//                Log.e("linkkk",champion!!.skins!![activeCardPosition % champion!!.skins!!.size].imageFull)
//                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, champion!!.skins!![activeCardPosition % champion!!.skins!!.size].imageFull)
                val intent = Intent(this@DetailChampionActivity, Fullscreen::class.java)
                intent.putExtra("pos", activeCardPosition)


                    val listskin : ArrayList<String> = ArrayList()
                    for(i in 0 .. champion!!.skins!!.size-1)
                        listskin.add( champion!!.skins!![i]!!.imageFull!!)
                    intent.putExtra("listskin", listskin)

//                startActivity(i)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent)
                } else {
                    val cardView = view as CardView
                    val sharedView = cardView.getChildAt(cardView.childCount - 1)
                    val options = ActivityOptions
                            .makeSceneTransitionAnimation(this@DetailChampionActivity, sharedView, "shared")
                    startActivity(intent, options.toBundle())
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView!!.smoothScrollToPosition(clickedPosition)
                onActiveCardChange(clickedPosition)
            }
        }
    }

}
