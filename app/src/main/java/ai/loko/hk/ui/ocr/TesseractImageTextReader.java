/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of LoKo HacK.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    LoKo hacK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LoKo Hack.  If not, see <http://www.gnu.org/licenses/>.
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

package ai.loko.hk.ui.ocr;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.ArrayList;

import ai.loko.hk.ui.constants.Constant;


public class TesseractImageTextReader {
    public String[] getTextFromBitmap(Bitmap src,String language){
        TessBaseAPI api=new TessBaseAPI();
        api.init(Constant.pathToTesseract,language);
        api.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        api.setImage(src);

        String textOnImage=api.getUTF8Text();

        ResultIterator iterator=api.getResultIterator();
        iterator.begin();

        Pixa pixa=api.getWords();
        ArrayList<Rect> rects=pixa.getBoxRects();
        String text="";
        for (int i=0;i<rects.size();i++){
            text+=iterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
            iterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD);
        }

        pixa.recycle();
        api.end();

        return textOnImage.split("\n");
    }
}


