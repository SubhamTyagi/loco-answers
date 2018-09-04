package ai.loko.hk.ui.ocr;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import ai.loko.hk.ui.MainActivity;
import ai.loko.hk.ui.utils.CustomToast;
import ai.myfancy.button.iml.ActionProcessButton;
import ui.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OCRFloating extends Service {
    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";
    public static final int VIRT_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    // final static int CODE_SCREENSHOT_RESULT_CODE = 13493;
    public static boolean isGoogle = true;
    private static boolean isUserForcedToCheckAnswer;
    DisplayMetrics metrics;

    // final private HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_BACKGROUND);
    VirtualDisplay vdisplay;
    MediaProjection.Callback cb;
    String TAG = "OCRFloating";
    TextRecognizer detecter;
    String optionsString = "";
    ActionProcessButton getAnswer;//, wiki;
    //////////////////////////////////
    private NotificationManager notificationManager;
    private int resultCode;
    private Intent resultData;
    private Handler handler;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private TextView option1, option2, option3;//answer;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private String lines;

    public OCRFloating() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        //TODO:
        LinearLayout linearLayout = new LinearLayout(this);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating, linearLayout);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notification();
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        detecter = new TextRecognizer.Builder(getApplicationContext()).build();

        metrics = new DisplayMetrics();

        if (mWindowManager != null) {
            mWindowManager.addView(mFloatingView, params);
        }

        mFloatingView.findViewById(R.id.head).setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        //Button getAnswer = mFloatingView.findViewById(R.id.getanswer1);
        //Button wiki = mFloatingView.findViewById(R.id.wiki1);

        getAnswer = mFloatingView.findViewById(R.id.getanswer1);
        //wiki = mFloatingView.findViewById(R.id.wiki1);

        getAnswer.setMode(ActionProcessButton.Mode.ENDLESS);
        // wiki.setMode(ActionProcessButton.Mode.ENDLESS);

        option1 = mFloatingView.findViewById(R.id.optionA);
        option2 = mFloatingView.findViewById(R.id.optionB);
        option3 = mFloatingView.findViewById(R.id.optionC);

        //answer = mFloatingView.findViewById(R.id.answer);

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRFloating.isGoogle = true;
                isUserForcedToCheckAnswer = true;
                getAnswer.setProgress(1);
                setAnswers();

                //showSupportDialog();
            }
        });
/*
        wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRFloating.isGoogle = false;
                isUserForcedToCheckAnswer = true;
                wiki.setProgress(1);
                // setAnswers();
                showSupportDialog();
            }
        });
*/
    }



    private void notification() {
        Intent i = new Intent(this, OCRFloating.class);
        i.setAction("stop");
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentText("Currently this service is not fully developed, Launching soon:>")
                .setContentTitle("Tap to remove overlay screen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true).setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_more, "Open Loko hack", pendingIntent);

        notificationManager.notify(1545, mBuilder.build());

    }

    private void setAnswers() {
        //TODO: here we capture screen

        setUpMediaProjection();
        ImageListener it = new ImageListener(this);

        cb = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                vdisplay.release();
            }
        };

        vdisplay = mediaProjection.createVirtualDisplay("lokoshot",
                it.getWidth(), it.getHeight(),
                getResources().getDisplayMetrics().densityDpi,
                VIRT_DISPLAY_FLAGS, it.getSurface(), null, handler);
        mediaProjection.registerCallback(cb, handler);


        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (mediaProjection != null) {
                    mediaProjection.unregisterCallback(cb);
                    mediaProjection.stop();
                    mediaProjection = null;
                }
            }
        }.start();


    }

    private void setUpMediaProjection() {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null && action.equalsIgnoreCase("search")) {

            String option11 = intent.getStringExtra("option1");
            String option22 = intent.getStringExtra("option2");
            String option33 = intent.getStringExtra("option3");
            String s = intent.getStringExtra("optionRed");

            option1.setText(option11);
            option2.setText(option22);
            option3.setText(option33);

            OCRFloating.isGoogle = true;
            // wiki.setProgress(0);
            getAnswer.setProgress(0);

            switch (s) {
                case "a":
                    option1.setTextColor(Color.RED);
                    option2.setTextColor(Color.BLACK);
                    option3.setTextColor(Color.BLACK);
                    break;
                case "b":
                    option2.setTextColor(Color.RED);
                    option3.setTextColor(Color.BLACK);
                    option1.setTextColor(Color.BLACK);
                    break;
                case "c":
                    option3.setTextColor(Color.RED);
                    option1.setTextColor(Color.BLACK);
                    option2.setTextColor(Color.BLACK);
                    break;
                default:
                    Toast.makeText(this, "I am soryy could not find any answer", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (action == null) {
            resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 5948);
            resultData = intent.getParcelableExtra(EXTRA_RESULT_INTENT);
            //setUpMediaProjection();
        } else if (action != null && action.equalsIgnoreCase("stop")) {
            stopSelf();
        }

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
        notificationManager.cancelAll();

    }

    WindowManager getWindowManager() {
        return (mWindowManager);
    }


    // here we process screen images to extract question and answer
    public void processImage(Bitmap bitmap) {

        if (detecter.isOperational() && isUserForcedToCheckAnswer) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlocks = detecter.detect(frame);
            // String blocks = "";
            //String lines = "";
            // String words = "";
            boolean start = false;
            boolean startOption = false;
            boolean find = false;
            for (int index = 0; index < textBlocks.size(); index++) {
                TextBlock tBlock = textBlocks.valueAt(index);
                // blocks = blocks + tBlock.getValue() + "\n" + "\n";
                for (Text line : tBlock.getComponents()) {
                    // Log.d(TAG, "processImage: lines:  "+line.getValue());

                    if ((!start) && line.getValue().contentEquals("GET ANSWER")) {
                        start = true;
                        continue;
                    }
                    if (start) {
                        lines = lines + line.getValue() + "\n";
                    } else continue;

                    if ((!startOption) && line.getValue().contains("?")) {
                        startOption = true;
                        continue;
                    }
                    if (startOption) {
                        optionsString += line.getValue();
                    }
                    find = true;
                }

            }
            if (find) {
                Log.d(TAG, "processImage: question is:::" + lines);
                Log.d(TAG, "processImage: option is::::" + optionsString);
            }
            isUserForcedToCheckAnswer = false;
        }

    }

    public Handler getHandler() {
        return handler;
    }
}
