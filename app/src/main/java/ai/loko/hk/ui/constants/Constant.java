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
    //TODO:
    public static final int VERSION = 24;
    public static final String VERSION_NAME = "2.1.2";
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final int CODE_FOR_SCREEN_CAPTURE = 1349;
    public static final String LATEST = "latest";

    public static final String path = Environment.getExternalStorageDirectory() + "/TriviaHacK/";
    public static final String pathToErrors = Environment.getExternalStorageDirectory() + "/TriviaHacK/errors";
    public static final String pathToTesseract = Environment.getExternalStorageDirectory() + "/tesseract/tessdata/";
    public static final String tesseractPath = Environment.getExternalStorageDirectory() + "/tesseract/";

    public static final String X1_FLOAT = "X1_FLOAT";
    public static final String X2_FLOAT = "X2_FLOAT";
    public static final String Y1_FLOAT = "Y1_FLOAT";
    public static final String Y2_FLOAT = "Y2_FLOAT";
    public static final String CLIP_POINTS = "clipsPoints";
    public static final String PROFILE_NAME = "profileName";
}
