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

import java.nio.Buffer;

import ai.loko.hk.ui.utils.CustomToast;


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
        mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
        if (mMediaProjection == null) {
            mMediaProjection = MediaProjectionHelper.getMediaProjection(context);
            Log.d(TAG, "takeScreenshot: mediaprojection =="+mMediaProjection);
            if (mMediaProjection == null) {
                Log.e(TAG, "MediaProjection null. Cannot take the screenshot.");
            }



        }
        try {
            virtualDisplay = mMediaProjection.createVirtualDisplay("Screenshotter", width, height, 50,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
            Log.d(TAG, "takeScreenshot: virtual display=="+virtualDisplay);
            mImageReader.setOnImageAvailableListener(Screenshotter.this, null);
            Log.d(TAG, "takeScreenshot: after");

        } catch (Exception e) {
            e.printStackTrace();


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
        Log.d(TAG, "onImageAvailable: start");
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
