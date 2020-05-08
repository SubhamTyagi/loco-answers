
package ai.trivia.hk.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import ai.trivia.hk.ui.activities.LoginActivity;
import ai.trivia.hk.ui.activities.ProfileActivity;
import ai.trivia.hk.ui.activities.SettingsActivity;
import ai.trivia.hk.ui.constants.Constant;
import ai.trivia.hk.ui.data.Data;
import ai.trivia.hk.ui.services.Floating;
import ai.trivia.hk.ui.services.OCRFloating;
import ai.trivia.hk.ui.services.option4.OCRFloating4;
import ai.trivia.hk.ui.utils.Logger;
import ai.trivia.hk.ui.utils.Utils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ui.BuildConfig;
import ui.R;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 153;
    private static final String TAG = "MainActivity";
    private final Handler mHandler = new Handler();
    SharedPreferences sharedPref;
    private Intent mFloatingIntent;
    private Button mOverlayPermmissionBtn, mAccessibilityPermissionBtn, startStopBtnLegacy, ocrBtn;
    private Button mOCRBtn4;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_launcher);
        mAuth = FirebaseAuth.getInstance();
        setupActionBar();

        mFloatingIntent = new Intent(MainActivity.this, Floating.class);
        takeStoragePermission();
        //checkForUpdates();

        mOverlayPermmissionBtn = findViewById(R.id.olpermission5);
        mAccessibilityPermissionBtn = findViewById(R.id.accpermission5);
        startStopBtnLegacy = findViewById(R.id.start5);
        ocrBtn = findViewById(R.id.ocr_btn5);

        mOCRBtn4 = findViewById(R.id.ocr_btn5_4);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startStopBtnLegacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable())
                    startFloatingWindow();
                else
                    showAlertNetworkNotAvailable();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ocrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isServiceRunning(OCRFloating.class)) {
                        stopService(new Intent(MainActivity.this, OCRFloating.class));
                    } else if (isNetworkAvailable()) {
                        Data.IS_THIS_REQUEST_FOR_OPTION_FOUR = false;
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    } else {
                        showAlertNetworkNotAvailable();
                    }
                }
            });


            mOCRBtn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isServiceRunning(OCRFloating4.class)) {
                        Data.IS_THIS_REQUEST_FOR_OPTION_FOUR = false;
                        stopService(new Intent(MainActivity.this, OCRFloating4.class));
                    } else if (isNetworkAvailable()) {
                        Data.IS_THIS_REQUEST_FOR_OPTION_FOUR = true;
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    } else {
                        showAlertNetworkNotAvailable();

                    }
                }
            });

        } else {
            ocrBtn.setVisibility(View.GONE);
            mOCRBtn4.setVisibility(View.GONE);
        }

        givePermission();


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
                REQUEST_CODE_STORAGE_PERMISSION);
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
            startStopBtnLegacy.setText(R.string.start);
            startStopBtnLegacy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            stopService(mFloatingIntent);
        } else {
            if (canOverdraw()) {
                if (isAccessibilityEnabled()) {
                    startStopBtnLegacy.setText(R.string.stop);
                    startStopBtnLegacy.setBackgroundColor(getResources().getColor(R.color.colorAccent));

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
        mTitleTextView.setTextColor(getResources().getColor(R.color.white));
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);

        BoomMenuButton rightBmb = actionBar.findViewById(R.id.action_bar_right_bmb);

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);

        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_settings_black_24dp).subNormalText("Common Settings related to App,Search Engine").normalText("Settings").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        }));

        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_help_white_24dp).subNormalText("If you are getting any error than get online help").normalText("Help Me").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rollychop/loco-answers/HELP.md")));
            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_info_white_24dp).normalText("About").subNormalText("About me").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                about();
            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_directions_run_black_24dp).subNormalText("Click here to go to github release page").normalText("Update.").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rollychop/loco-answers/releases/")));
            }
        }));
        rightBmb.addBuilder(new HamButton.Builder().normalImageRes(R.drawable.ic_person_black_24dp).normalText("Sign out").subNormalText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() + "").listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }));
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
        final String ACCESSIBILITY_SERVICE = "ai.trivia.hk.ui/ai.trivia.hk.ui.Accessibility";
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
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, "You do not need it", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                //Check if the permission is granted or not.
                if (resultCode == RESULT_OK) {
                    mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
        Utils.updater(this);
        if (sharedPref.getBoolean(getString(R.string.custom_search_engine), false))
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.custom_search_engine_url), "https://www.google.com/search?q=");
        else
            Data.BASE_SEARCH_URL = sharedPref.getString(getString(R.string.search_engine_key), "https://www.google.com/search?q=");
        if (sharedPref.getBoolean(getString(R.string.user_agent_key),false))
            Data.USER_AGENT = System.getProperty("http.agent");
        else
            Data.USER_AGENT = sharedPref.getString(getString(R.string.user_agent_text),System.getProperty("http.agent"));
        Data.NORMAL_FALLBACK_MODE = sharedPref.getBoolean(getString(R.string.fallback_mode), true);
        Data.FALLBACK_SEARCH_ENGINE = sharedPref.getString(getString(R.string.fallback_search_engine_key), "https://www.startpage.com/do/search?query=");
        Data.GRAYSCALE_IAMGE_FOR_OCR = sharedPref.getBoolean(getString(R.string.grayscale_image_ocr), false);
        Data.ENLARGE_IMAGE_FOR_OCR = sharedPref.getBoolean(getString(R.string.enlarge_image_key), false);

        Data.IMAGE_LOGS_STORAGE = sharedPref.getBoolean(getString(R.string.save_image_and_file_to_storage_key), true);
        Data.IS_TESSERACT_OCR_USE = sharedPref.getBoolean(getString(R.string.tesseract_key), false);
        Data.FAST_MODE_FOR_OCR = sharedPref.getBoolean(getString(R.string.fast_mode_key), false);

        if (Data.IS_TESSERACT_OCR_USE) {
            Data.TESSERACT_LANGUAGE = sharedPref.getString(getString(R.string.language_for_tesseract), "eng");
            Data.TESSERACT_DATA = sharedPref.getString(getString(R.string.tess_training_data_source), "fast");
        }

        setButtonsColorsAndText();
        super.onResume();
    }

    private void setButtonsColorsAndText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mOverlayPermmissionBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        if (isAccessibilityEnabled()) {
            mAccessibilityPermissionBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mAccessibilityPermissionBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        if (isServiceRunning(Floating.class)) {
            startStopBtnLegacy.setText(R.string.stop);
            startStopBtnLegacy.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            startStopBtnLegacy.setText(R.string.start);
            startStopBtnLegacy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        if (isServiceRunning(OCRFloating.class)) {
            ocrBtn.setText(R.string.ocr_btn_txt_stop);
            ocrBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            ocrBtn.setText(R.string.ocr_btn_txt);
            ocrBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        if (isServiceRunning(OCRFloating4.class)) {
            mOCRBtn4.setText(R.string.ocr_btn_txt_stop);
            mOCRBtn4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            mOCRBtn4.setText(R.string.ocr_btn_txt_4);
            mOCRBtn4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void about() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Trivia Hack V. " + BuildConfig.VERSION_NAME)
                .setContentText("This app is free and open source app hosted on GitHub " +
                        "\n Originally Developed by Shubham Tyagi " +
                        "\n Modified By Rohit Kumar")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/releases/")));
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showAlertNetworkNotAvailable() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Network not Available")
                .setContentText("Your Device is not connected to internet\n First Connect to internet")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new File(Constant.PATH).mkdirs();
                new File(Constant.PATH_TO_ERRORS).mkdirs();
                new File(Constant.PATH_OF_TESSERACT_DATA_STANDARD).mkdirs();
                new File(Constant.PATH_OF_TESSERACT_DATA_FAST).mkdirs();
                new File(Constant.PATH_OF_TESSERACT_DATA_BEST).mkdirs();

                try {
                    new File(Constant.PATH, ".nomedia").createNewFile();
                } catch (IOException e) {
                    Logger.logException(e);
                }
            } else {
                finish();
            }
        }

    }
}