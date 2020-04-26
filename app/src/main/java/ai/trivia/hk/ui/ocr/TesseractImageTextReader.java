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

package ai.trivia.hk.ui.ocr;


import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.ArrayList;

import ai.trivia.hk.ui.utils.Logger;

public class TesseractImageTextReader {

    private static volatile TessBaseAPI api;
    //  private static volatile TesseractImageTextReader INSTANCE;

    public static TesseractImageTextReader geInstance(String path,String language) {
        api = new TessBaseAPI();
        api.init(path, language);
        api.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        return new TesseractImageTextReader();
    }

    public String[] getTextFromBitmap(Bitmap src) {

        api.setImage(src);
        String textOnImage;

        try {
            textOnImage = api.getUTF8Text();

        } catch (Exception e) {
            Logger.logException(e);
            return new String[]{"Scan Failed: WTF: Must be submitted to developer!"};
        }

        if (textOnImage == null) {
            return new String[]{"Scan Failed: No Text on screen!"};
        }

        //RTL language should be implemented other way
        //char rleChar = (char)0x202B;

        String[] textOnScreenArray = textOnImage.split("\n");
        ArrayList<String> textOnScreen = new ArrayList<>();

        for (String s : textOnScreenArray) {
            if (!s.equals("")) {
                textOnScreen.add(s);
            }
        }
        int lineCount = textOnScreen.size();
        if (lineCount > 3) {
            StringBuilder question = new StringBuilder();
            for (int i = 0; i < lineCount - 3; i++) {
                question.append(textOnScreen.get(i));
            }
            return new String[]{question.toString(), textOnScreen.get(lineCount - 3), textOnScreen.get(lineCount - 2), textOnScreen.get(lineCount - 1), textOnImage};
        } else {
            StringBuilder question = new StringBuilder();
            for (int i = 0; i < lineCount; i++) {
                question.append(textOnScreen.get(i));
            }
            return new String[]{"Scan Failed: Could not read all options\n" + question};
        }

    }
}
