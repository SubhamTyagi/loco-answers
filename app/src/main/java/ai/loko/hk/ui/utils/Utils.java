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

package ai.loko.hk.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.loko.hk.ui.data.Data;

public class Utils {


    public static String getSimplifiedString(String text, @Nullable String optionalRemoveWord) {
        String split[] = text.split(" ");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        list.removeAll(Data.removeWords);

        if (optionalRemoveWord != null) {
            String x[] = optionalRemoveWord.split(" ");
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
        String split[] = text.split(" ");
        return new ArrayList<>(Arrays.asList(split));
    }

    public static ArrayList<String> getSimplifiedQuestion(String question) {
        String splitQuestion[] = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeWords);
        return simplifiedQuestion;
    }

    public static ArrayList<String> getSimplifiedQuestion(String question, int a) {
        String splitQuestion[] = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeNegativeWords);
        return simplifiedQuestion;
    }


    public static int count(String subString, String string) {
        int cnt = 0;
        String regex = "\\b" + subString + "\\b";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }

    public static Bitmap getGrayscaleImage(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                } else {
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }
}
