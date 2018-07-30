package ai.loko.hk.ui.ocr;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.Surface;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class ImageListener implements ImageReader.OnImageAvailableListener {
    private final int width;
    private final int height;
    private final ImageReader imageReader;
    private final OCRFloating svc;
    private Bitmap latestBitmap = null;

    ImageListener(OCRFloating svc) {
        this.svc = svc;
        Display display = svc.getWindowManager().getDefaultDisplay();
        Point size = new Point();
///SOME CODE HERE
        display.getSize(size);
        int width = size.x;
        int height = size.y;
       ///SOME CODE HERE
        imageReader.setOnImageAvailableListener(this, svc.getHandler());
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        final Image image = imageReader.acquireLatestImage();
///SOME CODE HERE
///SOME CODE HERE
        
    }

    Surface getSurface() {
        return (imageReader.getSurface());
    }

    int getWidth() {
        return (width);
    }

    int getHeight() {
        return (height);
    }

    void close() {
        imageReader.close();
    }
}
