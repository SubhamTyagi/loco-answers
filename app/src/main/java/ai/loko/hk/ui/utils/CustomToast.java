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

package ai.loko.hk.ui.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ui.R;

public  class CustomToast {
    private Context context = null;
    private String msg;
    private int duration=0;

    public CustomToast(Context context) {
        this.context = context;
    }

    public CustomToast(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public CustomToast setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public CustomToast setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public void show() {
        //Context context = context.getApplicationContext();
        View inflater = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tv = inflater.findViewById(R.id.toast_msg);
        tv.setText(msg == null ? "Alert" : msg);
        Toast toast = new Toast(context);
        toast.setView(inflater);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.show();
    }

/*
    private void show(String msg) {
        //Context context = context.getApplicationContext();
        View inflater = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tv = inflater.findViewById(R.id.toast_msg);
        tv.setText(msg);
        Toast toast = new Toast(context);
        toast.setView(inflater);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(duration);
        toast.show();
    }
 */
}
