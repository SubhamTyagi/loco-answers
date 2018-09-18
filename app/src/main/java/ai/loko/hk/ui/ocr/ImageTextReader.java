package ai.loko.hk.ui.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


/**
 * The type Image text reader.
 */
public class ImageTextReader {

    private static final String TAG = "ImageTextReader";

    private TextRecognizer textRecognizer;

    /**
     * Instantiates a new Image text reader.
     *
     * @param context the context
     */
    public ImageTextReader(Context context) {
        textRecognizer = new TextRecognizer
                .Builder(context)
                .build();

        if (!textRecognizer.isOperational()) {
            Log.d(TAG, "OCR dependencies not available ");
        }
    }

    /**
     * From image string.
     *
     * @param bitimg the bitimg
     * @return the string
     */
    public String fromImage(Bitmap bitimg) {
        Frame img = new Frame.Builder()
                .setBitmap(bitimg)
                .build();
        String searchString = "";
        if (textRecognizer.isOperational()) {
            final SparseArray<TextBlock> items = textRecognizer.detect(img);
            if (items.size() != 0) {
                int max_size = Integer.MIN_VALUE, index = -1;
                for (int i = 0; i < items.size(); i++) {
                    searchString = items.valueAt(i).getValue();
                    Log.d(TAG, "FOUND TEXT: " + searchString);
                    if (searchString.length() > max_size) {
                        max_size = searchString.length();
                        index = i;
                    }
                }
                searchString = items.valueAt(index).getValue().replace("\n", " ");
                Log.d(TAG, "Biggest text: " + searchString);
            }
        } else Log.d(TAG, "TextRecognizer not available.");
        return searchString;
    }
}