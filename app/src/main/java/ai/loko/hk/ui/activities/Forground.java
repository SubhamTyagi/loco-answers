package ai.loko.hk.ui.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import ai.loko.hk.ui.Accessibility;

/**
 * The type Forground.
 */
public class Forground extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        finish();
    }

    private boolean isServiceRunning() {
        Class<?> serviceClass = Accessibility.class;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
