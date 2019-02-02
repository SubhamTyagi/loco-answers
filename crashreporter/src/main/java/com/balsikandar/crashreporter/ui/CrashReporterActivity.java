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
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.balsikandar.crashreporter.CrashReporter;
import com.balsikandar.crashreporter.R;
import com.balsikandar.crashreporter.adapter.MainPagerAdapter;
import com.balsikandar.crashreporter.utils.Constants;
import com.balsikandar.crashreporter.utils.CrashUtil;
import com.balsikandar.crashreporter.utils.FileUtils;
import com.balsikandar.crashreporter.utils.SimplePageChangeListener;

import java.io.File;

public class CrashReporterActivity extends AppCompatActivity {

    private MainPagerAdapter mainPagerAdapter;
    private int selectedTabPosition = 0;

    //region activity callbacks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_crash_logs) {
            clearCrashLog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_reporter_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.crash_reporter));
        toolbar.setSubtitle(getApplicationName());
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    //endregion

    private void clearCrashLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String crashReportPath = TextUtils.isEmpty(CrashReporter.getCrashReportPath()) ?
                        CrashUtil.getDefaultPath() : CrashReporter.getCrashReportPath();

                File[] logs = new File(crashReportPath).listFiles();
                for (File file : logs) {
                    FileUtils.delete(file);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainPagerAdapter.clearLogs();
                    }
                });
            }
        }).start();
    }

    private void setupViewPager(ViewPager viewPager) {
        String[] titles = {getString(R.string.crashes), getString(R.string.exceptions)};
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(mainPagerAdapter);

        viewPager.addOnPageChangeListener(new SimplePageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
            }
        });

        Intent intent = getIntent();
        if (intent != null && !intent.getBooleanExtra(Constants.LANDING, false)) {
            selectedTabPosition = 1;
        }
        viewPager.setCurrentItem(selectedTabPosition);
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }

}
