package ai.loko.hk.ui;

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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
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
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

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
    private final static int VERSION = 14;
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int CODE_FOR_SCREEN_CAPTURE = 13493;

    private static final String LATEST = "latest";

    final Handler mHandler = new Handler();

    InterstitialAd mInterstitialAd;

    boolean isFirst, isFirstFacebook;
    MediaProjectionManager mgr;
    FirebaseAnalytics mFirebaseAnalytics;
    Intent floatingIntent;
    com.facebook.ads.InterstitialAd mFacebookInterstitialAd;
    private Intent screenshotIntent;
    private Button overlayPermmission, accessibilityPermission, startStopBtn, ocr;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private SharedPreferences preferences;
    private int openCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        floatingIntent = new Intent(MainActivity.this, Floating.class);
        isFirst = true;
        isFirstFacebook = true;

        setupActionBar();
        // setupMenuButton();
        overlayPermmission = findViewById(R.id.olpermission);
        accessibilityPermission = findViewById(R.id.accpermission1);
        startStopBtn = findViewById(R.id.start);
        ocr = findViewById(R.id.ocr_btn);

        overlayPermmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveOverlayPermission();
            }
        });

        accessibilityPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveAccessibilityPermission();
            }
        });


        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatingWindow();
            }
        });

        //TODO: Enable Ads
        setupAds();
        setupSupport();
        getUpdate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupOCR();
        } else {
            ocr.setVisibility(View.GONE);
        }
    }

    public void startFloatingWindow() {

        if (isServiceRunning(Floating.class)) {
            startStopBtn.setText(R.string.start);
            startStopBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            //closeNotification();
            stopService(floatingIntent);
        } else {
            if (canOverdraw()) {
                if (isAccessibilityEnabled1()) {
                    //DO SOMETHING
                   
                    startStopBtn.setText(R.string.stop);
                    startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
                    // showNotification();
                    startService(floatingIntent);
                   
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
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);

        BoomMenuButton rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_attach_money_black_24dp).subNormalText("Your contribution is need to make this app great").normalText("Support Me").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/shubhamtyagi1")));

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

    private void setupSupport() {
       
    }

    private void getUpdate() {

           }

    private void setupAds() {


    }

    private void loadInterstitialAds() {
    }

    private void showInterstitialAds() {
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
                    screenshotIntent = new Intent(this, OCRFloating.class).putExtra(EXTRA_RESULT_CODE, resultCode).putExtra(EXTRA_RESULT_INTENT, data);
                    startService(screenshotIntent);
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

        if (isServiceRunning(Floating.class)) {
            startStopBtn.setText(R.string.stop);
            startStopBtn.setBackgroundColor(getResources().getColor(R.color.btnred));
        } else {
            startStopBtn.setText(R.string.start);
            startStopBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (isServiceRunning(OCRFloating.class)) {
            ocr.setText(R.string.ocr_btn_txt_stop);
            ocr.setBackgroundColor(getResources().getColor(R.color.btnred));
        } else {
            ocr.setText(R.string.ocr_btn_txt);
            ocr.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        //storePresfs();
        super.onResume();
    }


//THESE CODES ARE UNNECESSARY I DOES NOT USING THIS CURRENTLY
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                about();
                break;
            }
            case R.id.menu_test: {
                startActivity(new Intent(this, Test.class));
                break;
            }
            case R.id.supportme: {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/shubhamtyagi1")));
                break;
            }
            case R.id.supportedapps: {
                supportedApps();
                break;
            }
        }
        return true;
    }

    private void about() {
        new FancyAlertDialog.Builder(this).setAnimation(Animation.SLIDE)
                .setTitle("Loko Hack version 1.7.4")
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
    }

    private void supportedApps() {
        new FancyAlertDialog.Builder(this).setAnimation(Animation.SIDE)
                .setTitle("Supported Apps")
                .setMessage(getResources().getString(R.string.supported_apps))
                .setPositiveBtnBackground(Color.parseColor("#388d3b"))
                .setBackgroundColor(Color.parseColor("#d1245e"))
                .setPositiveBtnText("Close")
                .setNegativeBtnText("Support Me")
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
        mgr = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isServiceRunning(OCRFloating.class)) {
                    ocr.setText(R.string.ocr_btn_txt_stop);
                    ocr.setBackgroundColor(getResources().getColor(R.color.btnred));
                    startOCR();
                } else {
                    ocr.setText(R.string.ocr_btn_txt);
                    ocr.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    //closeNotification();
                    stopService(screenshotIntent);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startOCR() {
              startActivityForResult(mgr.createScreenCaptureIntent(), CODE_FOR_SCREEN_CAPTURE);
    }

    
    @Override
    protected void onDestroy() {
        if (mFacebookInterstitialAd!=null){
            mFacebookInterstitialAd.destroy();
        }
            super.onDestroy();
    }
}
