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

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by bali on 12/08/17.
 */

public class AppUtils {
    private static String getCurrentLauncherApp(Context context) {
        String str = "";
        PackageManager localPackageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        try {
            ResolveInfo resolveInfo = localPackageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                str = resolveInfo.activityInfo.packageName;
            }
        } catch (Exception e) {
            Log.e("AppUtils", "Exception : " + e.getMessage());
        }
        return str;
    }

    private static String getUserIdentity(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) ==
                PackageManager.PERMISSION_GRANTED) {
            AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
            @SuppressLint("MissingPermission") Account[] list = manager.getAccounts();
            String emailId = null;
            for (Account account : list) {
                if (account.type.equalsIgnoreCase("com.google")) {
                    emailId = account.name;
                    break;
                }
            }
            if (emailId != null) {
                return emailId;
            }
        }
        return "";
    }

    public static String getDeviceDetails(Context context) {

        return "Device Information\n"
               /* + "\nDEVICE.ID : " + getDeviceId(context)*/
                + "\nUSER.ID : " + getUserIdentity(context)
                + "\nAPP.VERSION : " + getAppVersion(context)
                + "\nLAUNCHER.APP : " + getCurrentLauncherApp(context)
                + "\nTIMEZONE : " + timeZone()
                + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
               // + "\nBOARD : " + Build.BOARD
               // + "\nBOOTLOADER : " + Build.BOOTLOADER
                + "\nBRAND : " + Build.BRAND
                + "\nCPU_ABI : " + Build.CPU_ABI
                + "\nCPU_ABI2 : " + Build.CPU_ABI2
                //+ "\nDISPLAY : " + Build.DISPLAY
                //+ "\nFINGERPRINT : " + Build.FINGERPRINT
                //+ "\nHARDWARE : " + Build.HARDWARE
                //+ "\nHOST : " + Build.HOST
                /*+ "\nID : " + Build.ID*/
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT;
               /* + "\nSERIAL : " + Build.SERIAL*/
               // + "\nTAGS : " + Build.TAGS
               // + "\nTIME : " + Build.TIME
                //+ "\nTYPE : " + Build.TYPE
                //+ "\nUNKNOWN : " + Build.UNKNOWN
                //+ "\nUSER : " + Build.USER;
    }

    private static String timeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    private static String getDeviceId(Context context) {
        String androidDeviceId = getAndroidDeviceId(context);
        if (androidDeviceId == null)
            androidDeviceId = UUID.randomUUID().toString();
        return androidDeviceId;

    }

    private static String getAndroidDeviceId(Context context) {
        final String INVALID_ANDROID_ID = "9774d56d682e549c";
        final String androidId = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        if (androidId == null
                || androidId.toLowerCase().equals(INVALID_ANDROID_ID)) {
            return null;
        }
        return androidId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
