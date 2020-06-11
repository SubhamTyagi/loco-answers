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

package ai.trivia.hk.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.trivia.hk.ui.MyApplication;
import ai.trivia.hk.ui.constants.Constant;
import ai.trivia.hk.ui.data.Data;

public class Utils {


    public static String getSimplifiedString(String text, @Nullable String optionalRemoveWord) {
        String[] split = text.split(" ");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        list.removeAll(Data.removeWords);

        if (optionalRemoveWord != null) {
            String[] x = optionalRemoveWord.split(" ");
            ArrayList<String> x2 = new ArrayList<>(Arrays.asList(x));
            list.removeAll(x2);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> stringToArrayList(String text) {
        String[] split = text.split(" ");
        return new ArrayList<>(Arrays.asList(split));
    }

    public static ArrayList<String> getSimplifiedQuestion(String question) {
        String[] splitQuestion = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeWords);
        return simplifiedQuestion;
    }

    public static ArrayList<String> getSimplifiedQuestion(String question, int a) {
        String[] splitQuestion = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeNegativeWords);
        return simplifiedQuestion;
    }


    public static int count(String subString, String string) {
        int cnt = 0;
        Pattern p = Pattern.compile("\\b" + Pattern.quote(subString) + "\\b");
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }

    public static int count2(String subString, String string) {
        int cnt = 0;
        Pattern p = Pattern.compile(""+Pattern.quote(subString)+"");
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }

    public static Bitmap convertToGrayscale(Bitmap bmpOriginal) {


        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static void  writeToStorage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File picFile = new File(Constant.PATH, "SCR_" + Long.toString(System.currentTimeMillis()) + ".jpg");
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        try {
            picFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(picFile);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            Logger.logException(e);
        }
    }

    public static void writeToStorage(String[] questionAndOption) {
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
    //public static String getDataUrl()
    public static String getDomainName(String url) {
        final String hostExtractorRegexString = "(https?://)(www)?(\\w+\\.)?(\\w+\\.)?(\\w+)(\\.\\w+)";
        final Pattern hostExtractorRegexPattern = Pattern.compile(hostExtractorRegexString);

        if (url == null)
            return "Search";
        url = url.trim();
        Matcher m = hostExtractorRegexPattern.matcher(url);
        if (m.find() && m.groupCount() == 6) {
            return m.group(5).substring(0, 1).toUpperCase() + m.group(5).substring(1);
        }
        return "Search";
    }

    public static void updater(Context c) {
        AppUpdater appUpdater = new AppUpdater(c);
        appUpdater.setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://raw.githubusercontent.com/rollychop/ChangeLogsAndUpdater/master/update-changelog.json")
                .setCancelable(false)
                .setButtonDoNotShowAgain(null)
                .setButtonDismiss(null)
                .start();
    }

    public static void showToast(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
