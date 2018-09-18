package ai.loko.hk.ui.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Logger {
    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void initialize(Context context) {
        if (context != null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static void log(String... args) {
        Bundle b = new Bundle();
        for (String s : args) {
            b.putString(s, s);
        }
        if (mFirebaseAnalytics != null)
            mFirebaseAnalytics.logEvent("custom", b);
    }
}
