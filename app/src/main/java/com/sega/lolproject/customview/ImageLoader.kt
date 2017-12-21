package com.sega.lolproject.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageView
import com.sega.lolproject.MyApplication
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



/**
 * Using LazyList via https://github.com/thest1/LazyList/tree/master/src/com/fedorvlasov/lazylist
 * for the example since its super lightweight
 * I barely modified this file
 */
class ImageLoader(private val context: Context) {
    private val memoryCache = MemoryCache()
    private val fileCache: FileCache
    private val imageViews = Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    private val executorService: ExecutorService
    private val handler = Handler()//handler to display images in UI thread

    init {
        fileCache = FileCache(context)
        executorService = Executors.newFixedThreadPool(5)
    }

    //    final int stub_id= android.R.drawable.alert_dark_frame;

    fun DisplayImageFull(url: String, imageView: ImageView) {


        imageViews.put(imageView, url)
        val bitmap = memoryCache.get(url)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            queuePhoto(url, imageView)
            imageView.setImageDrawable(null)
        }

    }

    private fun queuePhoto(url: String, imageView: ImageView) {
        val p = PhotoToLoad(url, imageView)
        executorService.submit(PhotosLoader(p))
    }


    private fun getBitmap(url: String): Bitmap? {
        val f = fileCache.getFile(url)

        //from SD cache
        val b = decodeFile(f)
        if (b != null) {
            return b
        }

        //from web
        try {
            val bitmap: Bitmap?
            val imageUrl = URL(url)
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.instanceFollowRedirects = true
            val `is` = conn.inputStream
            val os = FileOutputStream(f)
            Utils.CopyStream(`is`, os)
            os.close()
            bitmap = decodeFile(f)
            return bitmap
        } catch (ex: Throwable) {
            ex.printStackTrace()
            if (ex is OutOfMemoryError) {
                memoryCache.clear()
            }
            return null
        }

    }

    //decodes image and scales it to reduce memory consumption
    private fun decodeFile(f: File): Bitmap? {
        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(f)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()

            //Find the correct scale value. It should be the power of 2.
            val REQUIRED_SIZE = 1000
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break
                }
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            if (scale >= 2) {
                scale /= 2
            }

            //decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(f)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (ignored: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    //Task for the queue
    private inner class PhotoToLoad internal constructor(var url: String, internal var imageView: ImageView)

    private inner class PhotosLoader internal constructor(internal var photoToLoad: PhotoToLoad) : Runnable {

        override fun run() {
            try {
                if (imageViewReused(photoToLoad)) {
                    return
                }
                val bmp: Bitmap?
                if(MyApplication.getManager()!!.checkFullData())
                {
                   bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(photoToLoad.url))
                }
                else
                {
                    bmp = getBitmap(photoToLoad.url)
                }
                memoryCache.put(photoToLoad.url, bmp)
                if (imageViewReused(photoToLoad)) {
                    return
                }
                val bd = BitmapDisplayer(bmp, photoToLoad)
                handler.post(bd)
            } catch (th: Throwable) {
                th.printStackTrace()
            }

        }
    }

    private fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag = imageViews[photoToLoad.imageView]
        return tag == null || tag != photoToLoad.url
    }

    //Used to display bitmap in the UI thread
    private inner class BitmapDisplayer internal constructor(internal var bitmap: Bitmap?, internal var photoToLoad: PhotoToLoad) : Runnable {

        override fun run() {
            if (imageViewReused(photoToLoad)) {
                return
            }
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap)
            } else {
                photoToLoad.imageView.setImageDrawable(null)
            }
        }
    }


}
