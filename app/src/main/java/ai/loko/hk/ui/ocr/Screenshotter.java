/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of LoKo HacK.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    LoKo hacK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LoKo Hack.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *
 */

package ai.loko.hk.ui.ocr;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.util.Log;

import com.balsikandar.crashreporter.CrashReporter;

import java.nio.Buffer;


/**
 * The type Screenshotter.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Screenshotter implements ImageReader.OnImageAvailableListener {

    private static final String TAG = "Screenshotter";
    private static Screenshotter mInstance;
    private static Context context;
    private VirtualDisplay virtualDisplay;
    private int width;
    private int height;
    private ScreenshotCallback cb;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private volatile int imageAvailable = 0;

    private Screenshotter() {
    }

    /**
     * Get the single instance of the Screenshotter class.
     *
     * @param context1 the context 1
     * @return the instance
     */
    public static Screenshotter getInstance(Context context1) {
        context = context1;
        if (mInstance == null) {
            mInstance = new Screenshotter();
        }
        return mInstance;
    }


    /**
     * Take screenshot.
     *
     * @param cb the cb
     */
    public void takeScreenshot(final ScreenshotCallback cb) {
        this.cb = cb;
        mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        if (mMediaProjection == null) {
            mMediaProjection = MediaProjectionHelper.getMediaProjection(context);
            if (mMediaProjection == null) {
                Log.e(TAG, "MediaProjection null. Cannot take the screenshot.");
            }


        }
        try {
            virtualDisplay = mMediaProjection.createVirtualDisplay("Screenshotter", width, height, 50,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);

            mImageReader.setOnImageAvailableListener(Screenshotter.this, null);


        } catch (Exception e) {
            e.printStackTrace();
            CrashReporter.logException(e);


        }
        // return this;
    }

    /**
     * Set the size of the screenshot to be taken
     *
     * @param width  width of the requested bitmap
     * @param height height of the request bitmap
     * @return the singleton instance
     */
    public Screenshotter setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image;
        // Log.d(TAG, "onImageAvailable: start");
       /*synchronized (this) {
            ++imageAvailable;
            if (imageAvailable != 2) {
                image = reader.acquireLatestImage();
                if (image == null) return;
                image.close();
               return;
           }
        }*/
        image = reader.acquireLatestImage();
        if (image == null) {
            Log.d(TAG, "onImageAvailable: image is null");
            return;
        }
        final Image.Plane[] planes = image.getPlanes();
        final Buffer buffer = planes[0].getBuffer().rewind();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);

        tearDown();
        image.close();
        cb.onScreenshot(bitmap);
    }

    private void tearDown() {
        virtualDisplay.release();
        if(mMediaProjection != null) mMediaProjection.stop();
        mMediaProjection = null;
        mImageReader = null;
    }

    /**
     * The interface Screenshot callback.
     */
    public interface ScreenshotCallback {
        /**
         * On screenshot.
         *
         * @param bitmap the bitmap
         */
        void onScreenshot(Bitmap bitmap);

    }
}
