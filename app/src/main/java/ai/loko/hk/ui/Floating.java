package ai.loko.hk.ui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ai.loko.hk.ui.data.Which;
import ai.myfancy.button.iml.ActionProcessButton;
import ui.R;


public class Floating extends Service {

    ActionProcessButton getAnswer;//, wiki;
    private NotificationManager notificationManager;
    //public static boolean isGoogle = true;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private TextView option1, option2, option3;//answer;


    public Floating() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LinearLayout linearLayout = new LinearLayout(this);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating, linearLayout);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //Add the view to the window.

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
        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //if (Build.VERSION.SDK_INT>=22)
        //  params.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        //else
        //    params.type=WindowManager.LayoutParams.TYPE_PHONE;
        //params.format = PixelFormat.TRANSLUCENT;
        // params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
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

        //Button getAnswer = mFloatingView.findViewById(R.id.getanswer);
        //Button wiki = mFloatingView.findViewById(R.id.wiki);
        getAnswer = mFloatingView.findViewById(R.id.getanswer1);
        //wiki = mFloatingView.findViewById(R.id.wiki1);

        getAnswer.setMode(ActionProcessButton.Mode.ENDLESS);
        //wiki.setMode(ActionProcessButton.Mode.ENDLESS);

        option1 = mFloatingView.findViewById(R.id.optionA);
        option2 = mFloatingView.findViewById(R.id.optionB);
        option3 = mFloatingView.findViewById(R.id.optionC);

        //answer = mFloatingView.findViewById(R.id.answer);

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Which.itIsGoogle = true;
                setAnswers();
                getAnswer.setProgress(1);
            }
        });

        /*wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Which.itIsGoogle = false;
                setAnswers();
                wiki.setProgress(1);
            }
        });*/

    }

    private void setAnswers() {
        Intent i = new Intent(getApplicationContext(), Forground.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //getApplicationContext().startActivity(new Intent(getApplicationContext(),Forground.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action =null;

        if (intent != null)
            action = intent.getAction();
        if (action != null && action.equalsIgnoreCase("search")) {
            String option11 = intent.getStringExtra("option1");
            String option22 = intent.getStringExtra("option2");
            String option33 = intent.getStringExtra("option3");
            String s = intent.getStringExtra("optionRed");

            //answer.setText(answer1);
            option1.setText(option11);
            option2.setText(option22);
            option3.setText(option33);

            Which.itIsGoogle = true;
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
        } else if (action != null && action.equalsIgnoreCase("stop")) {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        notificationManager.cancelAll();

    }

    private void notification() {
        Intent i = new Intent(this, Floating.class);
        i.setAction("stop");
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentText("Loko hack most accurate answer")
                .setContentTitle("Tap to remove overlay screen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true).setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_more, "Open Loko hack", pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1234, mBuilder.build());
    }
}
