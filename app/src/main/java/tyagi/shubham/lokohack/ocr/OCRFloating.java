package ai.loko.hk.ui.ocr;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import ui.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OCRFloating extends Service {
    //////////////////////////////////
    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";

    private static boolean isUserForcedToCheckAnswer;


    public static final int VIRT_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
   // final static int CODE_SCREENSHOT_RESULT_CODE = 13493;
    public static boolean isGoogle = true;

   // final private HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_BACKGROUND);

    DisplayMetrics metrics;
    VirtualDisplay vdisplay;
    MediaProjection.Callback cb;
    String TAG = "OCRFloating";
    private int resultCode;
    private Intent resultData;
    private Handler handler;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private TextView option1, option2, option3;//answer;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    public OCRFloating() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       
        Button getAnswer = mFloatingView.findViewById(R.id.getanswer);
        option1 = mFloatingView.findViewById(R.id.optionA);
        option2 = mFloatingView.findViewById(R.id.optionB);
        option3 = mFloatingView.findViewById(R.id.optionC);
        Button wiki = mFloatingView.findViewById(R.id.wiki);

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRFloating.isGoogle = true;
                isUserForcedToCheckAnswer=true;
                setAnswers();
            }
        });

        wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRFloating.isGoogle = false;
                isUserForcedToCheckAnswer=true;
                setAnswers();
            }
        });

    }

///SOME CODE HERE
    private void setAnswers() {
        //TODO: here we capture screen and parse its text

           }

    private void setUpMediaProjection() {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

	//some code here
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(cb);
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    WindowManager getWindowManager() {
        return (mWindowManager);
    }

    
    public Handler getHandler() {
        return handler;
    }
}
