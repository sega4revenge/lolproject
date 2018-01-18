package com.nightshade.lolproject.activity

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import com.nightshade.lolproject.R
import com.nightshade.lolproject.customview.ExtendedViewPager
import com.nightshade.lolproject.customview.ImageLoader
import com.nightshade.lolproject.customview.TouchImageView
import kotlinx.android.synthetic.main.custom_fullscreen.*
import java.io.IOException
import java.util.*

/**a
 * Created by Sega on 09/08/2016.
 */
class Fullscreen : Activity(), View.OnTouchListener {
    // These matrices will be used to move and zoom image
    internal var matrix = Matrix()
    internal var savedMatrix = Matrix()
    internal var start = PointF()
    internal var oldDist: Float = 0.toFloat()
    internal var listimage: ArrayList<String>? = ArrayList()
    internal var pos: Int = 0
    val options = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .error(R.drawable.img_error)
            .priority(Priority.HIGH)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_fullscreen)
        verifyStoragePermissions(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        val view = v as ImageView
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                start.set(event.x, event.y)
                Log.d(TAG, "mode=DRAG")
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                Log.d(TAG, "oldDist=" + oldDist)
                if (oldDist > 10f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                    Log.d(TAG, "mode=ZOOM")
                }
            }
            MotionEvent.ACTION_MOVE ->
                /*  *//*  if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }*//*
                else*/
                if (mode == ZOOM) {
                    val newDist = spacing(event)
                    Log.d(TAG, "newDist=" + newDist)
                    if (newDist > 10f) {
                        matrix.set(savedMatrix)
                        val scale = newDist / oldDist
                        matrix.postScale(scale, scale, mid.x, mid.y)
                    }
                }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                Log.e(TAG, "mode=NONE")
            }
        }
        // Perform the transformation
        view.imageMatrix = matrix
        return true // indicate event was handled
    }

    private fun spacing(event: MotionEvent): Float {

        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {

        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    internal inner class TouchImageAdapter : PagerAdapter() {
        override fun getCount(): Int {

            return listimage!!.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {


            setWallpaper.setOnClickListener {
                //            val permission = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.SET_WALLPAPER )
//
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // We don't have permission so prompt the user
//                Log.e("Per","fail")
//                ActivityCompat.requestPermissions(
//                        this,
//                        PERMISSIONS_WALLPAPER,
//                        REQUEST_SET_WALLPAPER
//                )
//            } else {
//                Log.e("Per","success")
//
//
//            }

                val permissionlistener = object : PermissionListener {
                    override fun onPermissionGranted() {
                        Glide.with(application)
                                .asBitmap()
                                .load(listimage!![position])
                                .apply(options)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resourceLoading: Bitmap?, transition: Transition<in Bitmap>?) {
                                        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                                        try {
                                            wallpaperManager.setBitmap(resourceLoading)
                                            Toast.makeText(applicationContext, "Set wallpaper successfully!", Toast.LENGTH_LONG).show()
                                        } catch (e : IOException) {
                                            e.printStackTrace()
                                        }
                                    }
                                })
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) =
                            Toast.makeText(this@Fullscreen, getString(R.string.per_deni) + deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
                }

                TedPermission.with(this@Fullscreen)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage(getString(R.string.per_turnon))
                        .setPermissions(Manifest.permission.SET_WALLPAPER)
                        .check()
            }
            val img = TouchImageView(container.context)
            val mLoader = ImageLoader(container.context)
            mLoader.DisplayImageFull(listimage!![position], img)
            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            return img
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {

            return view === `object`
        }
    }


    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            )
        } else {
            init()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER) == PackageManager.PERMISSION_GRANTED) {
                init()
            }
        }
    }

    fun init() {

        val mViewPager = findViewById<View>(R.id.view_pager) as ExtendedViewPager
        val extras = intent.extras
        pos = extras!!.getInt("pos")
//        println(pos.toString() + "ok")

        listimage = extras.getStringArrayList("listskin")

//        println(listimage!![pos])
        assert(listimage != null)
//        println(listimage!![pos])

        mViewPager.adapter = TouchImageAdapter()
        mViewPager.currentItem = pos
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                Log.e("Imageeeeee", position.toString())
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
    }

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        private val REQUEST_EXTERNAL_STORAGE = 1
        private val REQUEST_SET_WALLPAPER = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private val PERMISSIONS_WALLPAPER = arrayOf(Manifest.permission.SET_WALLPAPER )
        // We can be in one of these 3 states
        val NONE = 0
        val DRAG = 1
        val ZOOM = 2
        private val TAG = "Touch"
        var mid = PointF()
        var mode = NONE
    }

}