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

package ai.loko.hk.ui.data;

import android.content.Context;
import android.content.SharedPreferences;

import ai.loko.hk.ui.constants.Constant;

public class SharedPreferencesHelper {
    private static final String ClipBounds = "ClipBounds";
    private static SharedPreferencesHelper instance;
    SharedPreferences sharedPref;
    private Context context;

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) return new SharedPreferencesHelper();
        return instance;
    }


    public SharedPreferencesHelper setContext(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(ClipBounds, Context.MODE_PRIVATE);
        return this;
    }

    public boolean setClipBounds(float[] box) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(Constant.X1_FLOAT, box[0]);
        editor.putFloat(Constant.Y1_FLOAT, box[1]);
        editor.putFloat(Constant.X2_FLOAT, box[2]);
        editor.putFloat(Constant.Y2_FLOAT, box[3]);
        return editor.commit();

    }

    public float[] getClipBounds() {
        float[] box = new float[4];
        box[0] = sharedPref.getFloat(Constant.X1_FLOAT, 0f);
        box[1] = sharedPref.getFloat(Constant.Y1_FLOAT, 0f);
        box[2] = sharedPref.getFloat(Constant.X2_FLOAT, 0f);
        box[3] = sharedPref.getFloat(Constant.Y2_FLOAT, 0f);
        return box;
    }


}
