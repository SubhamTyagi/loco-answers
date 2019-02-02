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

package com.balsikandar.crashreporter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.balsikandar.crashreporter.R;
import com.balsikandar.crashreporter.utils.AppUtils;
import com.balsikandar.crashreporter.utils.FileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LogMessageActivity extends AppCompatActivity {

    String crashLog;
    private TextView appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_message);
        appInfo = (TextView) findViewById(R.id.appInfo);

        Intent intent = getIntent();
        if (intent != null) {
            String dirPath = intent.getStringExtra("LogMessage");
            File file = new File(dirPath);
            crashLog = FileUtils.readFromFile(file);
            TextView textView = (TextView) findViewById(R.id.logMessage);
            textView.setText(crashLog);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle(getString(R.string.crash_reporter));
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAppInfo();
    }

    private void getAppInfo() {
        appInfo.setText(AppUtils.getDeviceDetails(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crash_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();
        String filePath = null;
        if (intent != null) {
            filePath = intent.getStringExtra("LogMessage");
        }

        if (item.getItemId() == R.id.delete_log) {
            if (FileUtils.delete(filePath)) {
                finish();
            }
            return true;
        } else if (item.getItemId() == R.id.share_crash_log) {
            shareCrashReport(filePath);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void shareCrashReport(String filePath) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SubhamTyagi/loco-answers/issues/new?title=" + URLEncoder.encode("App Crash", "UTF-8") + "&body=" + URLEncoder.encode(crashLog + "-----------------\n\n" + appInfo.getText().toString(), "UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
