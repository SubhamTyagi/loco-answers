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

import ui.BuildConfig;

public class Constant {

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final int CODE_FOR_SCREEN_CAPTURE = 1349;
    public static final String path = Environment.getExternalStorageDirectory() + "/TriviaHacK/";
    public static final String pathToErrors = Environment.getExternalStorageDirectory() + "/TriviaHacK/errors";
    public static final String pathOfTesseractData = Environment.getExternalStorageDirectory() + "/tesseract4/tessdata/";
    public static final String tesseractPath = Environment.getExternalStorageDirectory() + "/tesseract4/";
    public static final String X1_FLOAT = "X1_FLOAT";
    public static final String X2_FLOAT = "X2_FLOAT";

    public static final String Y1_FLOAT = "Y1_FLOAT";
    public static final String Y2_FLOAT = "Y2_FLOAT";
    public static final String CLIP_POINTS = "clipsPoints";
    public static final String PROFILE_NAME = "profileName";

    //TRAINING DATA TEMPLATES
    public static final String TESSERACT_DATA_DOWNLOAD_URL="https://github.com/tesseract-ocr/tessdata_fast/raw/4.0.0/%s.traineddata";
    public static final String TESSERACT_DATA_FILE_NAME=Environment.getExternalStorageDirectory() + "/tesseract4/tessdata/%s.traineddata";

    //shared prefs keys

    public static final String IS_TESSERACT_IN_USE="tesseract_key";
    public static final String CUSTOM_SEARCH_ENGINE="custom_search_engine";
    public static final String CUSTOM_SEARCH_ENGINE_URL="custom_search_engine_url";
    public static final String SEARCH_ENGINE_KEY="search_engine_key";
    public static final String IMAGE_LOGS_STORAGE="save_images_and_file_to_storage_key";
    public static final String FAST_MODE_OCR="fast_mode_key";
    public static final String LANGUAGE_FOR_TESSERACT_OCR="language_for_tesseract";
    public static final String IS_GRAYSCALE_IMAGE="grayscale_image_ocr";
}
