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

package com.balsikandar.crashreporter.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.balsikandar.crashreporter.CrashReporter;
import com.balsikandar.crashreporter.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CrashUtil {

    private static final String TAG = CrashUtil.class.getSimpleName();

    private CrashUtil() {
        //this class is not publicly instantiable
    }

    private static String getCrashLogTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public static void saveCrashReport(final Throwable throwable) {

        String crashReportPath = CrashReporter.getCrashReportPath();
        String filename = getCrashLogTime() + Constants.CRASH_SUFFIX + Constants.FILE_EXTENSION;
        writeToFile(crashReportPath, filename, getStackTrace(throwable));

        showNotification(throwable.getLocalizedMessage(), true);
    }

    public static void logException(final Exception exception) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String crashReportPath = CrashReporter.getCrashReportPath();
                final String filename = getCrashLogTime() + Constants.EXCEPTION_SUFFIX + Constants.FILE_EXTENSION;
                writeToFile(crashReportPath, filename, getStackTrace(exception));
                showNotification(exception.getLocalizedMessage(), false);

            }
        }).start();
    }

    private static void writeToFile(String crashReportPath, String filename, String crashLog) {

        if (TextUtils.isEmpty(crashReportPath)) {
            crashReportPath = getDefaultPath();
        }

        File crashDir = new File(crashReportPath);
        if (!crashDir.exists() || !crashDir.isDirectory()) {
            crashReportPath = getDefaultPath();
            Log.e(TAG, "Path provided doesn't exists : " + crashDir + "\nSaving crash report at : " + getDefaultPath());
        }

        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(
                    crashReportPath + File.separator + filename));

            bufferedWriter.write(crashLog);
            bufferedWriter.flush();
            bufferedWriter.close();
            Log.d(TAG, "crash report saved in : " + crashReportPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showNotification(String localisedMsg, boolean isCrash) {

        if (CrashReporter.isNotificationEnabled()) {
            Context context = CrashReporter.getContext();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_warning_black_24dp);

            Intent intent = CrashReporter.getLaunchIntent();
            intent.putExtra(Constants.LANDING, isCrash);
            intent.setAction(Long.toString(System.currentTimeMillis()));

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);

            builder.setContentTitle(context.getString(R.string.view_crash_report));

            if (TextUtils.isEmpty(localisedMsg)) {
                builder.setContentText(context.getString(R.string.check_your_message_here));
            } else {
                builder.setContentText(localisedMsg);
            }

            builder.setAutoCancel(true);
            builder.setColor(ContextCompat.getColor(context, R.color.colorAccent_CrashReporter));

            NotificationManager notificationManager = (NotificationManager) context.
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
        }
    }

    private static String getStackTrace(Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        e.printStackTrace(printWriter);
        String crashLog = result.toString();
        printWriter.close();
        return crashLog;
    }

    public static String getDefaultPath() {
        String defaultPath = CrashReporter.getContext().getExternalFilesDir(null).getAbsolutePath()
                + File.separator + Constants.CRASH_REPORT_DIR;

        File file = new File(defaultPath);
        file.mkdirs();
        return defaultPath;
    }
}
