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

package ai.loko.hk.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
import java.io.IOException;

import ai.loko.hk.ui.activities.ProfileActivity;
import ai.loko.hk.ui.activities.SettingsActivity;
import ai.loko.hk.ui.activities.TestActivity;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.services.Floating;
import ai.loko.hk.ui.services.OCRFloating;
import ai.loko.hk.ui.utils.Logger;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;
import ui.BuildConfig;
import ui.R;


public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    SharedPreferences sharedPref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Intent mFloatingIntent;
    private Button mOverlayPermmissionBtn, mAccessibilityPermissionBtn, startStopBtn, ocrBtn;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_launcher);
        setupActionBar();

        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFloatingIntent = new Intent(MainActivity.this, Floating.class);
        takeStoragePermission();

        mOverlayPermmissionBtn = findViewById(R.id.olpermission5);
        mAccessibilityPermissionBtn = findViewById(R.id.accpermission5);
        startStopBtn = findViewById(R.id.start5);
        ocrBtn = findViewById(R.id.ocr_btn5);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatingWindow();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ocrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isServiceRunning(OCRFloating.class)){
                        stopService(new Intent(MainActivity.this,OCRFloating.class));
                    }else
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
        } else {
            ocrBtn.setVisibility(View.GONE);
        }

        givePermission();
        getUpdate();

    }

    private void takeStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.INTERNET,
                        Manifest.permission.GET_TASKS
                },
                296);
    }

    private void givePermission() {
        mOverlayPermmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveOverlayPermission();
            }
        });

        mAccessibilityPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveAccessibilityPermission();
            }
        });
    }

    public void startFloatingWindow() {

        if (isServiceRunning(Floating.class)) {
            startStopBtn.setText(R.string.start);
            startStopBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            stopService(mFloatingIntent);
        } else {
            if (canOverdraw()) {
                if (isAccessibilityEnabled()) {
                    startStopBtn.setText(R.string.stop);
                    startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
                    startService(mFloatingIntent);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else {
                    giveAccessibilityPermission();
                }
            } else {
                giveOverlayPermission();
            }

        }
    }

    private void setupActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar == null) throw new AssertionError();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.actionbar, null);
        TextView mTitleTextView = actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);

        BoomMenuButton rightBmb = actionBar.findViewById(R.id.action_bar_right_bmb);

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);

        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_settings_black_24dp).subNormalText("Common Settings related to App,Search Engine").normalText("Settings").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_directions_run_black_24dp).subNormalText("Here you can test your question").normalText("Test your app").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_apps_black_24dp).subNormalText("Supported App that are currently working").normalText("Supported Apps").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                supportedApps();
            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_info_white_24dp).normalText("About").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                about();
            }
        }));
    }

    private void getUpdate() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings;
        if (Constant.DEBUG) {
            configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
        } else {
            configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .build();
        }
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.updates);
        long cacheExpiration = 6 * 60 * 60;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    mFirebaseRemoteConfig.activateFetched();

            }
        });

        int latest = Integer.valueOf(mFirebaseRemoteConfig.getString(Constant.LATEST));
        if (latest - Constant.VERSION > 1) {

            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("New update is available")
                    .setContentText("Kindly update the app, Some problems are fixed and accuracy is extremely improved and much more.. ")
                    .setConfirmText("Update now")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
        } else if (latest > Constant.VERSION) {
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("New update is available")
                    .setContentText("Kindly update the app, Some problems are fixed and accuracy is extremely improved and much more.. ")
                    .setConfirmText("Update now")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));

                        }
                    })
                    .setCancelText("Later")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE = "ai.loko.hk.ui/ai.loko.hk.ui.Accessibility";
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //setting not found so your phone is not supported
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void giveAccessibilityPermission() {
        startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));

    }

    private boolean canOverdraw() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    private void giveOverlayPermission() {
        //&& !SearchSettings.canDrawOverlays(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            Toast.makeText(this, "You do not need it", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                //Check if the permission is granted or not.
                if (resultCode == RESULT_OK) {
                    mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
         /*
         Note this this list preference value will be ignored by Trivia hack 2.0
         because i think google search engine is best for our services .
         as search engine does not allow bot search google is some time flexibly due to his high usage
*/
         if (sharedPref.getBoolean(getString(R.string.custom_search_engine), false))
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.custom_search_engine_url), "https://www.google.com/search?q=");
        else
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.search_engine_key), "https://www.google.com/search?q=");
        //   Data.GRAYSCALE_IAMGE_FOR_OCR = sharedPref.getBoolean(getString(R.string.grayscale_image_ocr), false);


         //these values are setted before due to performance
        Data.IMAGE_LOGS_STORAGE = sharedPref.getBoolean(getString(R.string.save_image_and_file_to_storage_key), true);
        Data.IS_TESSERACT_OCR_USE=sharedPref.getBoolean(getString(R.string.tesseract_key),false);
        Data.FAST_MODE_FOR_OCR=sharedPref.getBoolean(getString(R.string.fast_mode_key),false);

        if (Data.IS_TESSERACT_OCR_USE){
            Data.TESSERACT_LANGUAGE=sharedPref.getString(getString(R.string.language_for_tesseract),"en");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (isAccessibilityEnabled()) {
            mAccessibilityPermissionBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            mAccessibilityPermissionBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        }

        if (isServiceRunning(Floating.class)) {
            startStopBtn.setText(R.string.stop);
            startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        } else {
            startStopBtn.setText(R.string.start);
            startStopBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (isServiceRunning(OCRFloating.class)) {
            ocrBtn.setText(R.string.ocr_btn_txt_stop);
            ocrBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        } else {
            ocrBtn.setText(R.string.ocr_btn_txt);
            ocrBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        super.onResume();
    }

    private void about() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Trivia Hack VERSION " + Constant.VERSION_NAME)
                .setContentText("Trivia Hack " + Constant.VERSION_NAME)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();


    }

    private void supportedApps() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Supported Apps")
                .setContentText(getResources().getString(R.string.supported_apps))
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //if ()
        switch (requestCode) {
            case 296: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new File(Constant.path).mkdirs();
                    new File(Constant.pathToErrors).mkdirs();
                    new File(Constant.pathToTesseract).mkdirs();
                    try {
                        new File(Constant.path,".nomedia").createNewFile();
                    } catch (IOException e) {
                       Logger.logException(e);
                    }
                } else {
                    finish();
                }
            }
        }

    }
}