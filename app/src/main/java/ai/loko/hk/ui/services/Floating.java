/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of Trivia Hack.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    Trivia Hack is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Trivia Hack.  If not, see <http://www.gnu.org/licenses/>.
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

package ai.loko.hk.ui.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dd.processbutton.iml.ActionProcessButton;

import ai.loko.hk.ui.activities.ForegroundActivity;
import ui.R;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;


public class Floating extends Service {

    ActionProcessButton getAnswer;//, wiki;
    private NotificationManagerCompat notificationManager;
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
        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);

        LinearLayout linearLayout = new LinearLayout(this);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating, linearLayout);

        notificationManager = NotificationManagerCompat.from(this);
        //Add the view to the window.

//        notification();

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
                setAnswers();
                getAnswer.setProgress(1);
            }
        });

    }


    private void setAnswers() {
        Intent i = new Intent(getApplicationContext(), ForegroundActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;

        if (intent != null)
            action = intent.getAction();
        if (action != null && action.equalsIgnoreCase("search")) {
            String option11 = intent.getStringExtra("option1");
            String option22 = intent.getStringExtra("option2");
            String option33 = intent.getStringExtra("option3");


            String s = intent.getStringExtra("optionRed");
            option1.setText(option11);
            option2.setText(option22);
            option3.setText(option33);


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
                    Toast.makeText(this, "You are also genius:Some error occurred", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (action != null && action.equalsIgnoreCase("stop")) {
            stopSelf();
        }
        Intent i = new Intent(this, Floating.class);
        i.setAction("stop");
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(this, "crash")
                .setContentText("Trivia Hack: Committed to speed and performance :)")
                .setContentTitle("Tap to remove overlay screen")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_search_white_24dp)
                .build();
        startForeground(1545, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        notificationManager.cancelAll();

    }




}
