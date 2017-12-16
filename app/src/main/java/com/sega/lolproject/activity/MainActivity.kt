package com.sega.lolproject.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import com.sega.lolproject.R
import com.sega.lolproject.cards.SliderAdapter
import com.sega.lolproject.lib.detail.CardSliderLayoutManager
import com.sega.lolproject.lib.detail.CardSnapHelper
import com.sega.lolproject.model.Champion
import com.sega.lolproject.services.RealmController
import com.sega.lolproject.util.DecodeBitmapTask
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by sega4 on 06/12/2017.
 */


class MainActivity :YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener{
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

//    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        if (null == p1) return
        youTubePlayer = p1

        // Start buffering
        if (!p2) {
            youTubePlayer!!.cueVideo(setCueVideo())

        }    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show()
    }

    var champion : Champion?=null
    fun setCueVideo(): String? {
        return linkSkin
    }



    companion object {
        val API_KEY = "AIzaSyCXAMVTzLpym1OnufwXhW3_jWy98dmfjto"

        //https://www.youtube.com/watch?v=<VIDEO_ID>
    }

    private val dotCoords = Array(5) { IntArray(2) }
    var youTubePlayerView : YouTubePlayerView?= null
    var youTubePlayer : YouTubePlayer ?= null
    var linkSkin : String ?= null

    private var sliderAdapter : SliderAdapter? = null
    private val pics = intArrayOf(R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5)
    private val maps = intArrayOf(R.drawable.map_paris, R.drawable.map_seoul, R.drawable.map_london, R.drawable.map_beijing, R.drawable.map_greece)
    private val descriptions = intArrayOf(R.string.text1, R.string.text2, R.string.text3, R.string.text4, R.string.text5)
    val listchampnomana = arrayOf("Aatrox","DrMundo","Vladimir","Mordekaiser","Garen","Katarina","Riven","Tryndamere","Renekton","Shyvana","Rengar","Rumble")
    private var layoutManger: CardSliderLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var mapSwitcher: ImageSwitcher? = null
    private var temperatureSwitcher: TextSwitcher? = null
    private var placeSwitcher: TextSwitcher? = null
    private var clockSwitcher: TextSwitcher? = null
    private var descriptionsSwitcher: TextSwitcher? = null
//    private var greenDot: View? = null

    private var country1TextView: TextView? = null
    private var country2TextView: TextView? = null
    private var countryOffset1: Int = 0
    private var countryOffset2: Int = 0
    private var countryAnimDuration: Long = 0
    private var currentPosition: Int = 0

    private var decodeMapBitmapTask: DecodeBitmapTask? = null
    private var mapLoadListener: DecodeBitmapTask.Listener? = null
    private val options = RequestOptions()
            .centerCrop()
            .dontAnimate()
            .placeholder(R.drawable.img_error)
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



       champion =  RealmController.with(this).getChampion("Aatrox")
        setContentView(R.layout.activity_main)
        attack.max= 10F
        attack.progress = champion!!.info?.attack?.toFloat()!!
        magic.max = 10F
        magic.progress = champion!!.info?.magic?.toFloat()!!
        defense.max= 10F
        defense.progress = champion!!.info?.defense?.toFloat()!!
        difficult.max= 10F
        difficult.progress = champion!!.info?.difficulty?.toFloat()!!
       champion =  RealmController.with(this).getChampion("Azir")
        println(champion!!.name)
        sliderAdapter = SliderAdapter(champion!!.skins!!, champion!!.skins!!.size, OnCardClickListener())
        val adapter_level = ArrayAdapter<String> (this,R.layout.spinner_item, resources.getStringArray(R.array.level))
        adapter_level.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner_level.adapter = adapter_level
        spinner_level.setSelection(0)
        spinner_level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
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
        initRecyclerView()
        initSwitchers(champion!!)

    }


    private fun initRecyclerView() {
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
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
    fun getFullStat118(champion : Champion) {
        health.text = champion.stats?.hp + " - " + getStat("18", champion.stats?.hp!!, champion.stats?.hpperlevel!!)
        healthregen.text = champion.stats?.hpregen + " - " + getStat("18", champion.stats?.hpregen!!, champion.stats?.hpregenperlevel!!)
        if (listchampnomana.contains(champion.id)) {
            mana.text = ""
            manaregen.text = ""
        } else {
            mana.text = champion.stats?.mp + " - " + getStat("18", champion.stats?.mp!!, champion.stats?.mpperlevel!!)
            manaregen.text = champion.stats?.mpregen + " - " + getStat("18", champion.stats?.mpregen!!, champion.stats?.mpregenperlevel!!)

        }
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
            if(listchampnomana.contains(champion.id)){
                mana.text = ""
                manaregen.text = ""
            } else {
                mana.text = getStat(level, champion.stats?.mp!!,champion.stats?.mpperlevel!!).toString()
                manaregen.text = getStat(level, champion.stats?.mpregen!!,champion.stats?.mpregenperlevel!!).toString()

            }
            range.text = champion.stats?.attackrange
            attackdmg.text = getStat(level, champion.stats?.attackdamage!!,champion.stats?.attackdamageperlevel!!).toString()
            attackspeed.text =  getAttackSpeed(champion.stats?.attackspeedoffset!!).toString()
            armor.text = getStat(level, champion.stats?.armor!!,champion.stats?.armorperlevel!!).toString()
            magicresist.text = getStat(level, champion.stats?.spellblock!!,champion.stats?.spellblockperlevel!!).toString()
            movespeed.text = champion.stats?.movespeed
        }
    private fun initSwitchers(champion : Champion) {


        Log.e("stat",getStat("8","68","3.2").toString())
//        price.text = champion?.price

        temperatureSwitcher = findViewById<View>(R.id.ts_temperature) as TextSwitcher
        temperatureSwitcher!!.setFactory(TextViewFactory(R.style.TemperatureTextView, true))
        temperatureSwitcher!!.setCurrentText("220 RP")

        placeSwitcher = findViewById<View>(R.id.ts_place) as TextSwitcher
        placeSwitcher!!.setFactory(TextViewFactory(R.style.TemperatureTextView, false))
        placeSwitcher!!.setCurrentText(champion!!.skins!![0]!!.name!!.vn)

        clockSwitcher = findViewById<View>(R.id.ts_clock) as TextSwitcher
        clockSwitcher!!.setFactory(TextViewFactory(R.style.TemperatureTextView, false))
        clockSwitcher!!.setCurrentText(champion!!.skins!![0]!!.name!!.vn)

        descriptionsSwitcher = findViewById<View>(R.id.ts_description) as TextSwitcher
        descriptionsSwitcher!!.setInAnimation(this, android.R.anim.fade_in)
        descriptionsSwitcher!!.setOutAnimation(this, android.R.anim.fade_out)
        descriptionsSwitcher!!.setFactory(TextViewFactory(R.style.TemperatureTextView, false))
        descriptionsSwitcher!!.setCurrentText(champion!!.skins!![0]!!.num)
        descriptionsSwitcher!!.setCurrentText(champion.skins!![0]!!.num)
        youTubePlayerView = youtube_player_view
        youTubePlayerView?.initialize(API_KEY, this)

        linkSkin = champion.skins!![0]!!.link
        Glide.with(this)
                .load(champion!!.imageAvatar)
                .thumbnail(0.1f)
                .apply(options)
                .into(avattarChamp)
        onActiveCardChange(0)
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
        countries_text.text =    (champion!!.name + " - " + champion!!.title!!.vn)
        countries_text!!.typeface = Typeface.createFromAsset(assets, "open-sans-extrabold.ttf")
        countryOffset1 = resources.getDimensionPixelSize(R.dimen.left_offset)



        temperatureSwitcher!!.setInAnimation(this@MainActivity, animH[0])
        temperatureSwitcher!!.setOutAnimation(this@MainActivity, animH[1])
        temperatureSwitcher!!.setText("220 RP")

        placeSwitcher!!.setInAnimation(this@MainActivity, animV[0])
        placeSwitcher!!.setOutAnimation(this@MainActivity, animV[1])
        placeSwitcher!!.setText(champion!!.skins!![pos % champion!!.skins!!.size]!!.name!!.vn)

        clockSwitcher!!.setInAnimation(this@MainActivity, animV[0])
        clockSwitcher!!.setOutAnimation(this@MainActivity, animV[1])
        clockSwitcher!!.setText(champion!!.skins!![pos % champion!!.skins!!.size]!!.num!!)

        descriptionsSwitcher!!.setText(getString(descriptions[pos % descriptions.size]))

//        attack.progressColor = R.color.blue
//        attack.progressBackgroundColor = R.color.design_snackbar_background_color
//        attack.setIconBackgroundColor(R.color.abc_btn_colored_borderless_text_material)

//        attack.progress = champion!!.info?.attack?.toFloat()!!

//        defense.progressColor = R.color.blue
//        defense.progressBackgroundColor = R.color.design_snackbar_background_color
//        defense.setIconBackgroundColor(R.color.abc_btn_colored_borderless_text_material)
//        defense.max = 10F
//        defense.progress = champion!!.info?.defense?.toFloat()!!
//        defense.iconImageResource = R.drawable.img_error
        linkSkin = champion!!.skins!![pos % champion!!.skins!!.size]!!.link!!
//        youTubePlayerView?.initialize(API_KEY, this)
        youTubePlayer?.cueVideo(linkSkin)
//        youTubePlayer?.loadVideo(linkSkin)
//        youTubePlayer?.cueVideo(linkSkin)
//        showMap(maps[pos % maps.size])

//        ViewCompat.animate(greenDot)
//                .translationX(dotCoords[pos % dotCoords.size][0].toFloat())
//                .translationY(dotCoords[pos % dotCoords.size][1].toFloat())
//                .start()

        currentPosition = pos
    }

//    private fun showMap(@DrawableRes resId: Int) {
//        if (decodeMapBitmapTask != null) {
//            decodeMapBitmapTask!!.cancel(true)
//        }
//
//        val w = mapSwitcher!!.width
//        val h = mapSwitcher!!.height
//
//        decodeMapBitmapTask = DecodeBitmapTask(resources, resId, w, h, mapLoadListener!!)
//        decodeMapBitmapTask!!.execute()
//    }

    private inner class TextViewFactory internal constructor(@param:StyleRes @field:StyleRes
                                                             internal val styleId: Int, internal val center: Boolean) : ViewSwitcher.ViewFactory {

        override fun makeView(): View {
            val textView = TextView(this@MainActivity)

            if (center) {
                textView.gravity = Gravity.CENTER
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(this@MainActivity, styleId)
            } else {
                textView.setTextAppearance(styleId)
            }

            return textView
        }

    }

    private inner class ImageViewFactory : ViewSwitcher.ViewFactory {
        override fun makeView(): View {
            val imageView = ImageView(this@MainActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.layoutParams = lp

            return imageView
        }
    }
    fun  getStat(level : String, base : String, growth : String ): Double {
        var stat = base.toDouble() + growth.toDouble() * (level.toInt() - 1) * (0.685 + 0.0175 * level.toInt())
        return Math.round(stat*10)/10.toDouble()
    }
    fun getAttackSpeed(attackdelay : String): Double {
        var speed = 0.625/(1+attackdelay.toDouble())
        return Math.round(speed*1000)/1000.toDouble()
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
                val intent = Intent(this@MainActivity, Fullscreen::class.java)
                intent.putExtra("pos", activeCardPosition)
                intent.putExtra("id", champion!!.id)
//                startActivity(i)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent)
                } else {
                    val cardView = view as CardView
                    val sharedView = cardView.getChildAt(cardView.childCount - 1)
                    val options = ActivityOptions
                            .makeSceneTransitionAnimation(this@MainActivity, sharedView, "shared")
                    startActivity(intent, options.toBundle())
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView!!.smoothScrollToPosition(clickedPosition)
                onActiveCardChange(clickedPosition)
            }
        }
    }

}
