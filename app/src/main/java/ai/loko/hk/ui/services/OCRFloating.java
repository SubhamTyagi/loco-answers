package ai.loko.hk.ui.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ai.loko.hk.ui.MainActivity;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.ocr.ImageTextReader;
import ai.loko.hk.ui.ocr.Screenshotter;
import ai.loko.hk.ui.data.SharedPreferencesHelper;
import ai.loko.hk.ui.view.BoxListener;
import ai.loko.hk.ui.view.BoxView;
import ai.myfancy.button.iml.ActionProcessButton;
import ui.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OCRFloating extends Service {

    private static final String TAG = OCRFloating.class.getSimpleName();
    public static boolean isGoogle = true;
    ActionProcessButton getAnswer;


    private NotificationManager notificationManager;

    private WindowManager mWindowManager;

    private View mWebViewContainer;
    private View mBoxView;
    private View mFloatingView;
    private BoxView mClipBoxView;
    private WebView webView;
    private TextView option1, option2, option3;
    private WindowManager.LayoutParams webViewParams, params;//, boxParams;
    private BoxListener mBoxViewListener;
    private ImageTextReader imageTextReader;
    private int width, height;

    private boolean webViewVisible = false;
    private boolean isClipMode;

    private int LAYOUT_FLAG;


    private Bitmap mBitmap;

    float[] clipBox = new float[4];
    float[] clipBox1 = new float[4];
    int[] clipRegion2 = new int[4];


    public OCRFloating() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating, new LinearLayout(this));
        mWebViewContainer = LayoutInflater.from(this).inflate(R.layout.web_view, new LinearLayout(this));
        mBoxView = LayoutInflater.from(this).inflate(R.layout.box_view, new LinearLayout(this));

        mClipBoxView = mBoxView.findViewById(R.id.clip_box);
        mBoxViewListener = new BoxListener(this);
        mClipBoxView.setOnTouchListener(new BoxListener(this));
        clipBox= SharedPreferencesHelper.getInstance().setContext(getApplicationContext()).getClipBounds();


        ///////////////////////////////////////////////////////////////////////////////
        webView = mWebViewContainer.findViewById(R.id.webview);                      //
        webView.setWebViewClient(new WebViewClient() {                               //
            @Override                                                                //
            public boolean shouldOverrideUrlLoading(WebView view, String url) {      //
                return false;                                                        //
            }                                                                        //
        });                                                                          //
                                                                                     //
        webView.getSettings().setJavaScriptEnabled(true);                            //
        webView.setVerticalScrollBarEnabled(true);                                   //
        webView.setHorizontalScrollBarEnabled(true);                                 //
        //////////////////////////////////////////////////////////////////////////// //

        notification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        ///////////////////////////Web view screen ///////////////////////////////////

        webViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                (int) getResources().getDimension(R.dimen.webview_height),
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        webViewParams.x = 0;
        webViewParams.y = 0;
        webViewParams.gravity = Gravity.TOP | Gravity.CENTER;
        ///////////////////////////////////////////////////

        if (mWindowManager != null) {
            mWindowManager.addView(mFloatingView, params);
            //
        }

        mFloatingView.findViewById(R.id.head).setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

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

        getAnswer = mFloatingView.findViewById(R.id.getanswer1);
        getAnswer.setMode(ActionProcessButton.Mode.ENDLESS);

        option1 = mFloatingView.findViewById(R.id.optionA);
        option2 = mFloatingView.findViewById(R.id.optionB);
        option3 = mFloatingView.findViewById(R.id.optionC);
        imageTextReader = new ImageTextReader(getApplicationContext());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webViewVisible) {
                    removeWebView();
                } else {
                    OCRFloating.isGoogle = true;
                    getAnswer.setProgress(1);
                    Log.d(TAG, "onClick: getanswers clicked");
                    captureScreenshot();
                }

            }
        });
    }

    private void captureScreenshot() {
        showBox();
        Screenshotter.getInstance(getApplicationContext()).setSize(width, height).takeScreenshot(new Screenshotter.ScreenshotCallback() {
            @Override
            public void onScreenshot(Bitmap bitmap) {
                mBitmap=bitmap;
                processImage(clipRegion2);
            }
        });

    }

    // here we process screen images to extract question and answer
    public void processImage(int[]coordinate) {
        //now we have to process bitmap
        Log.d(TAG, "processImage: ");
        if(coordinate[2]==0||coordinate[3]==0){
            coordinate[2]=width;
            coordinate[3]=height;
        }
        Bitmap cropped = Bitmap.createBitmap(mBitmap,coordinate[0],coordinate[1],coordinate[2],coordinate[3]);
        String textOnScreen = imageTextReader.fromImage(cropped);
        createImage(cropped);
        Log.d(TAG, "processImage: text on screen is==:" + textOnScreen);
        getAnswer.setProgress(0);
        try {
            webView.loadUrl(Data.BASE_SEARCH_URL + URLEncoder.encode(textOnScreen, "UTF-8"));
            addWebView();
            removeBox();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    private void showBox() {
        mClipBoxView.updateRegion(this.clipBox[0], this.clipBox[1], this.clipBox[2], this.clipBox[3]);
        mWindowManager.addView(mBoxView, getLayoutParamsForBox());
    }

    public void finishClipMode(int[] iBox1, float[] fBox2) {
        this.isClipMode = false;
        SharedPreferencesHelper.getInstance().setContext(getApplicationContext()).setClipBounds(fBox2);
        if (iBox1[2] >= 50) {
            if (iBox1[3] >= 50) {
                this.clipBox1 = this.clipBox.clone();
                this.clipBox = fBox2.clone();
                ///take screenshot using bounds in iBox1;
                //Save iBox1
                processImage(iBox1);
                return;
            }
        }
        this.clipRegion2[2] = (int) Math.ceil((double) (this.clipBox1[2] - this.clipBox1[0]));
        this.clipRegion2[3] = (int) Math.ceil((double) (this.clipBox1[3] - this.clipBox1[1]));
        this.clipRegion2[0] = (int) Math.ceil((double) this.clipBox1[0]);
        this.clipRegion2[1] = (int) Math.ceil((double) this.clipBox1[1]);
        processImage(clipRegion2);

        //take screenshot using clipRegion2
        //save clipRegion2

    }

    private void addWebView() {
        if (!webViewVisible) {
            mWindowManager.addView(mWebViewContainer, webViewParams);
            webViewVisible = true;
            getAnswer.setText("Remove window");
        }
    }

    private void removeWebView() {
        if (webViewVisible) {
            mWindowManager.removeView(mWebViewContainer);
            webViewVisible = false;
            getAnswer.setText("Get Answer");
        }
    }

    private void removeBox(){
        mWindowManager.removeView(mBoxView);
    }

    private void createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File picFile = new File(Constant.path, Long.toString(System.currentTimeMillis()) + ".jpg");
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        try {
            Log.d(TAG, "Writing images");
            picFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(picFile);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    Toast.makeText(this, "wtf", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (action != null && action.equalsIgnoreCase("stop")) {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);

        notificationManager.cancelAll();

    }

    WindowManager getWindowManager() {
        return (mWindowManager);
    }




    WindowManager.LayoutParams getLayoutParamsForBox() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        Point point2 = new Point();
        defaultDisplay.getSize(point2);
        int diff;
        if (point2.x == point.x) {
            diff = point.y - point2.y;
        } else {
            diff = point.x - point2.x;
        }
        int screenWidth = point.x + diff;
        int screenHeight = point.y + diff;

        return new WindowManager.LayoutParams(
                screenWidth,
                screenHeight,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


    }
}
