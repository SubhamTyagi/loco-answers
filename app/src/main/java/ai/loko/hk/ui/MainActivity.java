package ai.loko.hk.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

import ai.loko.hk.ui.activities.ProfileActivity;
import ai.loko.hk.ui.activities.Test;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.ocr.MediaProjectionHelper;
import ai.loko.hk.ui.services.Floating;
import ai.loko.hk.ui.services.OCRFloating;
import ai.loko.hk.ui.activities.SettingsActivity;
import ai.loko.hk.ui.utils.Logger;
import ai.myfancy.dialog.Animation;
import ai.myfancy.dialog.FancyAlertDialog;
import ai.myfancy.dialog.FancyAlertDialogListener;
import ai.myfancy.dialog.Icon;
import io.fabric.sdk.android.Fabric;
import ui.BuildConfig;
import ui.R;


public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    SharedPreferences sharedPref;
    private InterstitialAd mInterstitialAd;
    //private boolean isFirst;
    private MediaProjectionManager mMediaProjectionManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Intent mFloatingIntent;
    private Intent mScreenshotIntent;
    private Button mOverlayPermmissionBtn, mAccessibilityPermissionBtn, startStopBtn, ocrBtn;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Logger.initialize(this);
        mFloatingIntent = new Intent(MainActivity.this, Floating.class);
        mScreenshotIntent = new Intent(this, OCRFloating.class);

//        isFirst = false;
        takeStoragePermission();
        setupActionBar();
        //setupAds();

        mOverlayPermmissionBtn = findViewById(R.id.olpermission);
        mAccessibilityPermissionBtn = findViewById(R.id.accpermission1);
        startStopBtn = findViewById(R.id.start);
        ocrBtn = findViewById(R.id.ocr_btn);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatingWindow();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupOCR();
        } else {
            ocrBtn.setVisibility(View.GONE);
        }

        givePermission();
        getUpdate();
        File directory = new File(Constant.path);
        directory.mkdirs();
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
                2);
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
            //closeNotification();
            stopService(mFloatingIntent);
        } else {
            if (canOverdraw()) {
                if (isAccessibilityEnabled()) {

                    startStopBtn.setText(R.string.stop);
                    startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
                    // showNotification();
                    startService(mFloatingIntent);
                    //isFirst = true;
                    showInterstitialAds();
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
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_directions_run_black_24dp).subNormalText("Here you can test your question").normalText("Test").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(MainActivity.this, Test.class));
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

        //Log.d(TAG, "getUpdate: "+cacheExpiration);
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    mFirebaseRemoteConfig.activateFetched();
                /// Log.d(TAG, "onComplete: ");
            }
        });

        int latest = Integer.valueOf(mFirebaseRemoteConfig.getString(Constant.LATEST));

        //Log.i(TAG, "getUpdate: "+latest);

        if (latest - Constant.VERSION > 1) {
            new FancyAlertDialog.Builder(this).setAnimation(Animation.SLIDE)
                    .setTitle("Update is Available")
                    //.setMessage("This app is Developed by Shubham Tyagi(shubham2tyagi7@gmail.com)\nApp is in Development\nVersion:0.1")
                    .setMessage("Kindly update the app, Some problems are fixed and accuracy is extremely improved and much more.. ")
                    .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                    .setBackgroundColor(Color.parseColor("#d1245e"))
                    .setPositiveBtnText("Update Now")
                    .isCancellable(false)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert, Icon.Visible).build();
        } else if (latest > Constant.VERSION) {
            new FancyAlertDialog.Builder(this).setAnimation(Animation.SLIDE)
                    .setTitle("Update is Available")
                    //.setMessage("This app is Developed by Shubham Tyagi(shubham2tyagi7@gmail.com)\nApp is in Development\nVersion:0.1")
                    .setMessage("Kindly update the app, Some problems are fixed and accuracy is extremely improved and much more.. ")
                    .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                    .setBackgroundColor(Color.parseColor("#d1245e"))
                    .setPositiveBtnText("Update Now")
                    .isCancellable(false)
                    .setNegativeBtnText("Later")
                    .setNegativeBtnBackground(Color.parseColor("#FFCC0000"))
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));

                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            //
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert, Icon.Visible).build();

        }
    }

    private void setupAds() {

        String appId = "ca-app-pub-4301584724850632~9402202755";
        //String adInterstitial = "ca-app-pub-4301584724850632/1930906844 ";
        String adInterstitial2 = "ca-app-pub-4301584724850632/5990069101";

        //  String adBanner = "ca-app-pub-4301584724850632/8429171472";
        String testId = "ca-app-pub-3940256099942544/5224354917";
        MobileAds.initialize(this, Constant.DEBUG ? testId : appId);

        //AdView mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constant.DEBUG ? testId : adInterstitial2);
        loadInterstitialAds();
        showInterstitialAds();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // if (isFirst) {
                //    showInterstitialAds();
                //    isFirst = false;
                // }
                Log.d("ADS", "onAdLoaded: add loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("ADS", "onAdFailedToLoad: errorCode" + errorCode);
                loadInterstitialAds();

            }

            @Override
            public void onAdClosed() {
                loadInterstitialAds();
            }
        });
    }

    private void loadInterstitialAds() {
        if (!mInterstitialAd.isLoaded())
            if (Constant.DEBUG)
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("A6967EDFD302F200CB79E422827FFD16").build());
            else
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void showInterstitialAds() {
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        loadInterstitialAds();
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        // Class<?> serviceClass = Floating.class;
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

    boolean canOverdraw() {
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
            case Constant.CODE_FOR_SCREEN_CAPTURE:
                if (resultCode == RESULT_OK) {
                    MediaProjectionHelper.setMediaProjectionManager(mMediaProjectionManager);
                    MediaProjectionHelper.setScreenshotPermission(data);
                    startService(mScreenshotIntent);
                    finish();

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {

        if (sharedPref.getBoolean(getString(R.string.custom_search_engine), false))
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.custom_search_engine_url), "https://www.google.com/search?q=");
        else
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.search_engine_key), "https://www.google.com/search?q=");

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
        new FancyAlertDialog.Builder(this).setAnimation(Animation.SLIDE)
                .setTitle("LOKO HACK VERSION 1.7.7")
                .setMessage("Do you want to support ")
                .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                .setBackgroundColor(Color.parseColor("#d1245e"))
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.parseColor("#FFCC0000"))
                .isCancellable(true)
                .setNegativeBtnText("Later")
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/shubhamtyagi1")));
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        //
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info, Icon.Visible).build();
    }

    private void supportedApps() {
        new FancyAlertDialog.Builder(this).setAnimation(Animation.SIDE)
                .setTitle("Supported Apps")
                .setMessage(getResources().getString(R.string.supported_apps))
                .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                .setBackgroundColor(Color.parseColor("#d1245e"))
                .setPositiveBtnText("Close")
                .setNegativeBtnText("Support")
                .setNegativeBtnBackground(Color.parseColor("#FFCC0000"))
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/shubhamtyagi1")));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info, Icon.Visible).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupOCR() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        ocrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canOverdraw()) {
                    if (!isServiceRunning(OCRFloating.class)) {
                        ocrBtn.setText(R.string.ocr_btn_txt_stop);
                        ocrBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
                        startOCR();
                    } else {
                        ocrBtn.setText(R.string.ocr_btn_txt);
                        ocrBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        //closeNotification();
                        stopService(mScreenshotIntent);
                    }
                } else {
                    giveOverlayPermission();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startOCR() {
        //showInterstitialAds();
        //startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), Constant.CODE_FOR_SCREEN_CAPTURE);

        startActivity(new Intent(this, ProfileActivity.class));
    }

}