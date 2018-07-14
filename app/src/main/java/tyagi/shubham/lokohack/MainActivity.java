package ai.loko.hk.ui;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import ai.loko.hk.ui.ocr.OCRFloating;
import ai.myfancy.dialog.Animation;
import ai.myfancy.dialog.FancyAlertDialog;
import ai.myfancy.dialog.FancyAlertDialogListener;
import ai.myfancy.dialog.Icon;
import io.fabric.sdk.android.Fabric;
import ui.BuildConfig;
import ui.R;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";
    //TODO:
    private final static int VERSION = 13;
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int CODE_FOR_SCREEN_CAPTURE = 13493;
    private static final String LATEST = "latest";

    final Handler mHandler = new Handler();
    
    com.facebook.ads.AdView facebookAds;

    InterstitialAd mInterstitialAd;
//SOME CODES HERE

    boolean isFirst;
    // MediaProjectionManager mgr;
    FirebaseAnalytics mFirebaseAnalytics;
    private Button overlayPermmission, accessibilityPermission, startStopBtn;//ocr;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private SharedPreferences preferences;
    private int openCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        isFirst = true;
//SOME CODES HERE

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overlayPermmission = findViewById(R.id.olpermission);
        accessibilityPermission = findViewById(R.id.accpermission1);
        startStopBtn = findViewById(R.id.start);
        //ocr = findViewById(R.id.ocr_btn);
        //ocr.setVisibility(View.GONE);

        overlayPermmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveOverlayPermission();
            }
        });

        accessibilityPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveAccessibilityPermision();
            }
        });

        final Intent i = new Intent(MainActivity.this, Floating.class);


        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//SOME CODES HERE

                           }
        });

        setupAds();
        setupSupport();
        getUpdate();

        //ocr.setVisibility(View.GONE);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    setupOCR();
        //} else {
        //   ocr.setVisibility(View.GONE);
        //}


    }


    /*
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setupOCR() {
            mgr = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            ocr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startOCR();
                }
            });
    
        }
    
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void startOCR() {
            startActivityForResult(mgr.createScreenCaptureIntent(), CODE_FOR_SCREEN_CAPTURE);
        }
    */
    private void getUpdate() {
//SOME CODES HERE
    }

    private void setupAds() {
//SOME CODES HERE
    }

    private void loadInterstitialAds() {
        if (!mInterstitialAd.isLoaded())
            if (DEBUG)
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("test device id").build());
            else
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterstitialAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            //loadInterstitialAds();
        }
    }

    private boolean isServiceRunning() {
        //SOME CODES HERE
        return false;
    }

    public boolean isAccessibilityEnabled1() {
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE = "ai.loko.hk.ui/ai.loko.hk.ui.Accessibilty";
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

    private void giveAccessibilityPermision() {
        startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));

    }

    boolean canOverdraw() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    private void giveOverlayPermission() {
        //&& !SearchSettings.canDrawOverlays(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            overlayPermmission.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            Toast.makeText(this, "You do not need it", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                //Check if the permission is granted or not.
                if (resultCode == RESULT_OK) {
                    overlayPermmission.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                break;
            case CODE_FOR_SCREEN_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //Intent i = new Intent(this, ScreenshotService.class).putExtra(ScreenshotService.EXTRA_RESULT_CODE, resultCode).putExtra(ScreenshotService.EXTRA_RESULT_INTENT, data);
                    startService(new Intent(this, OCRFloating.class).putExtra(EXTRA_RESULT_CODE, resultCode).putExtra(EXTRA_RESULT_INTENT, data));
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            overlayPermmission.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            overlayPermmission.setBackgroundColor(getResources().getColor(R.color.btnred));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            overlayPermmission.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (isAccessibilityEnabled1()) {
            accessibilityPermission.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            accessibilityPermission.setBackgroundColor(getResources().getColor(R.color.btnred));
        }

        if (isServiceRunning()) {
            startStopBtn.setText("STOP");
            startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        }
        //storePresfs();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                new FancyAlertDialog.Builder(this).setAnimation(Animation.SLIDE)
                        .setTitle("Loko Hack version 1.7.1")
                        .setMessage("\nWant to support development of this app")
                        .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                        .setBackgroundColor(Color.parseColor("#d1245e"))
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnBackground(Color.parseColor("#FFCC0000"))
                        .isCancellable(false)
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
                break;

            }
            case R.id.menu_test: {
                startActivity(new Intent(this, Test.class));
                break;
            }
            case R.id.supportme: {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/shubhamtyagi1")));
            }

        }
        return true;
    }


}
