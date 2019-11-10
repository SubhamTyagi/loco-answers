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

package ai.loko.hk.ui.constants;

import android.os.Environment;

public class Constant {

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final int CODE_FOR_SCREEN_CAPTURE = 1349;
    public static final String CLIP_POINTS = "clipsPoints";
    public static final String PROFILE_NAME = "profileName";


    public static final String PATH = Environment.getExternalStorageDirectory() + "/TriviaHacK/";
    public static final String PATH_TO_ERRORS = Environment.getExternalStorageDirectory() + "/TriviaHacK/errors";



    public static final String PATH_OF_TESSERACT_DATA_BEST = Environment.getExternalStorageDirectory() + "/tesseract4/best/tessdata/";
    public static final String PATH_OF_TESSERACT_DATA_FAST = Environment.getExternalStorageDirectory() + "/tesseract4/fast/tessdata/";
    public static final String PATH_OF_TESSERACT_DATA_STANDARD = Environment.getExternalStorageDirectory() + "/tesseract4/standard/tessdata/";


    //TESSDATA PATHS
    public static final String TESSERACT_PATH_BEST = Environment.getExternalStorageDirectory() + "/tesseract4/best/";
    public static final String TESSERACT_PATH_FAST = Environment.getExternalStorageDirectory() + "/tesseract4/fast/";
    public static final String TESSERACT_PATH_STANDARD = Environment.getExternalStorageDirectory() + "/tesseract4/standard/";


    //TRAINING DATA URL TEMPLATES
    public static final String TESSERACT_DATA_DOWNLOAD_URL_BEST = "https://github.com/tesseract-ocr/tessdata_best/raw/4.0.0/%s.traineddata";
    public static final String TESSERACT_DATA_DOWNLOAD_URL_STANDARD = "https://github.com/tesseract-ocr/tessdata/raw/4.0.0/%s.traineddata";
    public static final String TESSERACT_DATA_DOWNLOAD_URL_FAST = "https://github.com/tesseract-ocr/tessdata_fast/raw/4.0.0/%s.traineddata";


    //TRAINING DATA FILE PATHS
    public static final String TESSERACT_DATA_FILE_NAME_BEST = Environment.getExternalStorageDirectory() + "/tesseract4/best/tessdata/%s.traineddata";
    public static final String TESSERACT_DATA_FILE_NAME_FAST = Environment.getExternalStorageDirectory() + "/tesseract4/fast/tessdata/%s.traineddata";
    public static final String TESSERACT_DATA_FILE_NAME_STANDARD = Environment.getExternalStorageDirectory() + "/tesseract4/standard/tessdata/%s.traineddata";

    //shared prefs keys
    public static final String IS_TESSERACT_IN_USE = "tesseract_key";
    public static final String LANGUAGE_FOR_TESSERACT_OCR = "language_for_tesseract";
    public static final String TESS_TRAINING_DATA_SOURCE ="tess_training_data_source";
    public static final String IS_GRAYSCALE_IMAGE = "grayscale_image_ocr";
}
