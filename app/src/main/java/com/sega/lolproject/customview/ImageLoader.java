package com.sega.lolproject.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Using LazyList via https://github.com/thest1/LazyList/tree/master/src/com/fedorvlasov/lazylist
 * for the example since its super lightweight
 * I barely modified this file
 */
public class ImageLoader {
    private Context context;
    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler = new Handler();//handler to display images in UI thread

    public ImageLoader(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    //    final int stub_id= android.R.drawable.alert_dark_frame;

    public void DisplayImageFull(String url, ImageView imageView) {


        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            queuePhoto(url, imageView);
            imageView.setImageDrawable(null);
        }

    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }




    //decodes image and scales it to reduce memory consumption


    //Task for the queue
    private class PhotoToLoad {
        public String url;
        ImageView imageView;

        PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    private class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad)) {
                    return;
                }

                Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(photoToLoad.url));
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad)) {
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    //Used to display bitmap in the UI thread
    private class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad)) {
                return;
            }
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            }
            else {
                photoToLoad.imageView.setImageDrawable(null);
            }
        }
    }



}
