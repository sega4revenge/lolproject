package com.nightshade.lolproject.activity

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import java.io.File
import java.io.FileOutputStream
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
    internal var listimagename: ArrayList<String>? = ArrayList()
    internal var listimage: ArrayList<String>? = ArrayList()
    internal var listimagepath: ArrayList<String>? = ArrayList()
    internal var pos: Int = 0
    var rootFolder: File? = null
    var positionImg: Int = 0

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
        rootFolder = File(Environment.getExternalStorageDirectory().toString() + "/LolWallpaper")
        if (!rootFolder!!.exists()) {
            rootFolder!!.mkdirs()
        }
        Log.e("rootFolder", rootFolder.toString())
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
        listimagename = extras.getStringArrayList("listskinname")
        listimage = extras.getStringArrayList("listskin")
        listimagepath = extras.getStringArrayList("listskinpath")
        positionImg = pos

//        println(listimage!![pos])
        assert(listimage != null)
//        println(listimage!![pos])
        setWallpaper.setOnClickListener {
            val permissionlistener = object : PermissionListener {
                override fun onPermissionGranted() {
                    Glide.with(application)
                            .asBitmap()
                            .load(listimage!![positionImg])
                            .apply(options)
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resourceLoading: Bitmap?, transition: Transition<in Bitmap>?) {
                                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                                    try {
                                        wallpaperManager.setBitmap(resourceLoading)
                                        Toast.makeText(applicationContext, "Set wallpaper successfully!", Toast.LENGTH_LONG).show()
                                    } catch (e: IOException) {
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
        downloadImage.setOnClickListener {
            Glide.with(applicationContext)
                    .asBitmap()
                    .load(listimage!![positionImg])
                    .apply(options)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resourceFull: Bitmap?, transition: Transition<in Bitmap>?) {

                            try {
                                val nameLoading = listimagepath!![positionImg]
                                var myDir = File(rootFolder, nameLoading)
                                Toast.makeText(applicationContext, "Download wallpaper successfully!", Toast.LENGTH_LONG).show()
                                println(myDir)
                                if (myDir.exists())
                                    myDir.delete()

                                val out = FileOutputStream(myDir)

                                resourceFull!!.compress(Bitmap.CompressFormat.JPEG, 90, out)

                                out.flush()
                                out.close()
                                var values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, listimagename!![positionImg])
                                values.put(MediaStore.Images.Media.DESCRIPTION, listimagename!![positionImg])
                                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                                values.put(MediaStore.Images.ImageColumns.BUCKET_ID, Locale.getDefault().displayLanguage)
                                values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, Locale.getDefault().displayLanguage)
                                values.put("_data", myDir.absolutePath)

                               var cr = contentResolver
                                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)


                            } catch (e: Exception) {
                                println(e)
                            }

                        }
                    })
        }
        mViewPager.adapter = TouchImageAdapter()
        mViewPager.currentItem = pos
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                positionImg = position
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
        private val PERMISSIONS_WALLPAPER = arrayOf(Manifest.permission.SET_WALLPAPER)
        // We can be in one of these 3 states
        val NONE = 0
        val DRAG = 1
        val ZOOM = 2
        private val TAG = "Touch"
        var mid = PointF()
        var mode = NONE
    }

}