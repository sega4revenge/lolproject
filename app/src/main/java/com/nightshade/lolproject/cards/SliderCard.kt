package com.nightshade.lolproject.cards

import android.graphics.*
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.nightshade.lolproject.R
import com.nightshade.lolproject.util.DecodeBitmapTask


class SliderCard(itemView: View) : RecyclerView.ViewHolder(itemView), DecodeBitmapTask.Listener {

    private val imageView: ImageView
    private var task: DecodeBitmapTask? = null

    init {
        imageView = itemView.findViewById<View>(R.id.image) as ImageView
    }
    internal fun setContent(image : Bitmap) {
        if (viewWidth == 0) {
            itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    viewWidth = itemView.width
                    viewHeight = itemView.height


                    loadBitmap(image)
                }
            })
        } else {
            loadBitmap(image)
        }
    }

    internal fun clearContent() {
        if (task != null) {
            task!!.cancel(true)
        }
    }

    private fun loadBitmap(image :Bitmap) {
        println(viewWidth)
        println(viewHeight)
        val decodedBitmap = Bitmap.createScaledBitmap( image,viewWidth, viewHeight, true);
        val result: Bitmap
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            result =getRoundedCornerBitmap(decodedBitmap,
                  10f ,viewWidth,viewHeight)
            decodedBitmap.recycle()
        } else {
            result = decodedBitmap
        }
        imageView.setImageBitmap(result)

    }
    override fun onPostExecuted(bitmap: Bitmap) {

    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Float, width: Int, height: Int): Bitmap {
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height

        val xScale = width.toFloat() / bitmap.width
        val yScale = height.toFloat() / bitmap.height
        val scale = Math.max(xScale, yScale)

        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        val left = (width - scaledWidth) / 2
        val top = (height - scaledHeight) / 2

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)

        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, pixels, pixels, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, null, targetRect, paint)

        return output
    }

    companion object {

        private var viewWidth = 0
        private var viewHeight = 0
    }

}