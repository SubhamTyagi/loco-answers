/*
 *   Copyright (C) 2018,2019 SHUBHAM TYAGI
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


package ai.loko.hk.ui.services.option4;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ai.loko.hk.ui.MainActivity;
import ai.loko.hk.ui.answers.option4.Engine4;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.model.option4.Question4;
import ai.loko.hk.ui.ocr.Points;
import ai.loko.hk.ui.ocr.Screenshotter;
import ai.loko.hk.ui.ocr.option4.ImageTextReader4;
import ai.loko.hk.ui.ocr.option4.TesseractImageTextReader4;
import ai.loko.hk.ui.utils.Logger;
import ui.R;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OCRFloating4 extends Service {

    private static final String TAG = OCRFloating4.class.getSimpleName();
    public static boolean isGoogle = true;
    ActionProcessButton getAnswer;

    int[] coordinate = new int[4];
    private NotificationManager notificationManager;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private TextView option1, option2, option3, option4;
    private WindowManager.LayoutParams params;
    private ImageTextReader4 imageTextReader4;
    private int width, height;
    private TesseractImageTextReader4 tesseractImageTextReader4;

    public OCRFloating4() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating4, new LinearLayout(this));

        notification();
        int LAYOUT_FLAG;
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

        if (mWindowManager != null) {
            mWindowManager.addView(mFloatingView, params);
            //
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
        getAnswer.setMode(ActionProcessButton.Mode.PROGRESS);

        option1 = mFloatingView.findViewById(R.id.optionA);
        option2 = mFloatingView.findViewById(R.id.optionB);
        option3 = mFloatingView.findViewById(R.id.optionC);
        option4 = mFloatingView.findViewById(R.id.optionD);

        imageTextReader4 = new ImageTextReader4(getApplicationContext());
        String path;
        switch (Data.TESSERACT_DATA){
            case "best":
                path = Constant.TESSERACT_PATH_BEST;
                break;
            case "standard":
                path = Constant.TESSERACT_PATH_STANDARD;
                break;
            default:
                path = Constant.TESSERACT_PATH_FAST;
        }
        tesseractImageTextReader4 = TesseractImageTextReader4.geInstance(path,Data.TESSERACT_LANGUAGE);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRFloating4.isGoogle = true;
                getAnswer.setProgress(10);
                synchronized (OCRFloating4.this) {
                    Screenshotter.getInstance(getApplicationContext()).setSize(width, height).takeScreenshot(new Screenshotter.ScreenshotCallback() {
                        @Override
                        public void onScreenshot(final Bitmap bitmap) {
                            getAnswer.setProgress(25);
                            new TakeScreenShot().execute(bitmap);
                        }
                    });
                }
            }
        });
        coordinate[0] = (int) Math.ceil((double) Points.X1);
        coordinate[1] = (int) Math.ceil((double) Points.Y1);
        coordinate[2] = (int) Math.ceil((double) Points.X2);
        coordinate[3] = (int) Math.ceil((double) Points.Y2);
    }


    private void writeToStorage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File picFile = new File(Constant.PATH, "SCR_" + Long.toString(System.currentTimeMillis()) + ".jpg");
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        try {
            Log.d(TAG, "Writing images");
            picFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(picFile);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            Logger.logException(e);
        }
    }

    private void writeToStorage(String[] questionAndOption) {
        File picFile = new File(Constant.PATH, "QaOption_" + Long.toString(System.currentTimeMillis()) + ".txt");
        StringBuilder value = new StringBuilder();
        if (questionAndOption.length == 5) {
            value.append(questionAndOption[4]);
        } else {
            value.append("ERROR: ");
            for (String s : questionAndOption) {
                value.append(s).append("\n");
            }
        }
        try {
            picFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(picFile);
            outputStream.write(value.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            Logger.logException(e);
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = "";
        if (intent != null)
            action = intent.getAction();
        if (action != null && action.equalsIgnoreCase("stop")) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void notification() {
        Intent i = new Intent(this, OCRFloating4.class);
        i.setAction("stop");
        PendingIntent pi = PendingIntent.getService(this, 171, i, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 172, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentText("Trivia Hack: Committed to speed and performance :)")
                .setContentTitle("Tap to remove overlay screen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true).setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_more, "Open Trivia Hack", pendingIntent);

        notificationManager.notify(1695, mBuilder.build());

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TakeScreenShot extends AsyncTask<Bitmap, Integer, String> {
        private String[] questionAndOption;
        private Bitmap croppedGrayscaleImage;
        private Engine4 engine;

        @Override
        protected void onPostExecute(final String s) {
            if (s != null) {
                Log.d(TAG, "onPostExecute: setting values on screen");
                option1.setText(engine.getA1());
                option2.setText(engine.getB2());
                option3.setText(engine.getC3());
                option4.setText(engine.getD4());
                switch (s) {
                    case "a":
                        option1.setTextColor(Color.RED);
                        option2.setTextColor(Color.BLACK);
                        option3.setTextColor(Color.BLACK);
                        option4.setTextColor(Color.BLACK);
                        break;
                    case "b":
                        option2.setTextColor(Color.RED);
                        option3.setTextColor(Color.BLACK);
                        option4.setTextColor(Color.BLACK);
                        option1.setTextColor(Color.BLACK);
                        break;
                    case "c":
                        option3.setTextColor(Color.RED);
                        option4.setTextColor(Color.BLACK);
                        option1.setTextColor(Color.BLACK);
                        option2.setTextColor(Color.BLACK);
                        break;
                    case "d":
                        option4.setTextColor(Color.RED);
                        option1.setTextColor(Color.BLACK);
                        option2.setTextColor(Color.BLACK);
                        option3.setTextColor(Color.BLACK);
                }
            } else if (questionAndOption.length > 0) {
                Log.d(TAG, "onPostExecute: question and option lenght==" + questionAndOption.length);
                Toast.makeText(getApplicationContext(), questionAndOption[0], Toast.LENGTH_SHORT).show();
            }
            getAnswer.setProgress(100);

            if (Data.IMAGE_LOGS_STORAGE) {
                new Thread() {
                    @Override
                    public void run() {
                        writeToStorage(croppedGrayscaleImage);
                        writeToStorage(questionAndOption);
                    }
                }.start();
            }
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            if (coordinate[2] == 0 || coordinate[3] == 0) {
                coordinate[2] = width;
                coordinate[3] = height;
            }
            croppedGrayscaleImage = Bitmap.createBitmap(bitmaps[0], coordinate[0], coordinate[1], coordinate[2] - coordinate[0], coordinate[3] - coordinate[1]);
            publishProgress(40);

            if (Data.IS_TESSERACT_OCR_USE)
                questionAndOption = tesseractImageTextReader4.getTextFromBitmap(croppedGrayscaleImage);
            else
                questionAndOption = imageTextReader4.getTextFromBitmap(croppedGrayscaleImage);

            publishProgress(65);

            if (questionAndOption.length >= 5) {
                engine = new Engine4(new Question4(questionAndOption[0], questionAndOption[1], questionAndOption[2], questionAndOption[3], questionAndOption[5]));
                engine.search();
                if (!engine.isError()) {
                    publishProgress(90);
                    return engine.getAnswer();
                } else {
                    engine = new Engine4(new Question4(questionAndOption[0], questionAndOption[1], questionAndOption[2], questionAndOption[3], questionAndOption[5]));
                    publishProgress(90);
                    return engine.search();
                }
            }

            Log.d(TAG, "doInBackground: wtf is this");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            getAnswer.setProgress(values[0]);
        }
    }


}
