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

package ai.loko.hk.ui.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ai.loko.hk.ui.MainActivity;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.utils.Logger;
import ai.loko.hk.ui.utils.SpUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ui.R;

public class SettingsActivity extends AppCompatPreferenceActivity {


    private static final String TAG = "SettingsActivity";
    static boolean change = true;
    static ConnectivityManager cm;

    private static SweetAlertDialog mSweetAlertDialogForProgressBar;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                String entry = index >= 0 ? (listPreference.getEntries()[index]).toString() : null;
                preference.setSummary(entry);
                Log.d(TAG, "onPreferenceChange: preference changed:::" + entry);

                Data.IS_TESSERACT_OCR_USE = SpUtil.getInstance().getBoolean(Constant.IS_TESSERACT_IN_USE, false);

                if (preference.getKey().equals(Constant.LANGUAGE_FOR_TESSERACT_OCR) && Data.IS_TESSERACT_OCR_USE) {
                    Data.TESSERACT_DATA = SpUtil.getInstance().getString(Constant.TESS_TRAINING_DATA_SOURCE, "fast");
                    final String lang = listPreference.getEntryValues()[index >= 0 ? index : 0].toString();
                    if (!isLanguageDataExists(Data.TESSERACT_DATA, lang)) {
                        mSweetAlertDialogForProgressBar.show();
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (ni == null) {
                            change = false;
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        } else if (ni.isConnected()) {
                            change = true;
                            new DownloadTrainingTask().execute(Data.TESSERACT_DATA, lang);
                        } else {
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            change = false;
                        }
                    } else {
                        change = true;
                    }
                } else if (preference.getKey().equals(Constant.TESS_TRAINING_DATA_SOURCE) && Data.IS_TESSERACT_OCR_USE) {
                    Data.TESSERACT_LANGUAGE = SpUtil.getInstance().getString(Constant.LANGUAGE_FOR_TESSERACT_OCR, "eng");
                    final String data = listPreference.getEntryValues()[index >= 0 ? index : 0].toString();
                    if (!isLanguageDataExists(data, Data.TESSERACT_LANGUAGE)) {
                        mSweetAlertDialogForProgressBar.show();
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (ni == null) {
                            change = false;
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        } else if (ni.isConnected()) {
                            change = true;
                            new DownloadTrainingTask().execute(data, Data.TESSERACT_LANGUAGE);
                        } else {
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            change = false;
                        }
                    } else {
                        change = true;
                    }
                }
            } else {
                preference.setSummary(stringValue);
            }
            return change;
        }
    };

    private static boolean isLanguageDataExists(final String dataFile, final String lang) {
        String fileName;
        switch (dataFile) {
            case "best":
                fileName = Constant.TESSERACT_DATA_FILE_NAME_BEST;
                break;
            case "standard":
                fileName = Constant.TESSERACT_DATA_FILE_NAME_STANDARD;
                break;
            default:
                fileName = Constant.TESSERACT_DATA_FILE_NAME_FAST;
        }
        File t = new File(String.format(fileName, lang));
        boolean r = t.exists();
        Log.v(TAG, "training data for " + lang + " exists? " + r);
        return r;
    }


    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        mSweetAlertDialogForProgressBar = new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialogForProgressBar.setTitleText(getString(R.string.download_language_data_please_wait)).setCancelable(false);

        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // downloadManager = DownloadService.getDownloadManager(getApplicationContext());
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSweetAlertDialogForProgressBar = new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialogForProgressBar.setTitleText(getString(R.string.download_language_data_please_wait)).setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || MainPreferenceFragment.class.getName().equals(fragmentName);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private static class DownloadTrainingTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean bool) {
            mSweetAlertDialogForProgressBar.setTitleText("Success").setConfirmText("Ok").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }

        @Override
        protected Boolean doInBackground(String... languages) {

            return downloadTraningData(languages[0], languages[1]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        private boolean downloadTraningData(String dataSource, String lang) {
            boolean result = true;
            String downloadURL;
            String location;
            String destFileName;

            switch (dataSource) {
                case "best":
                    destFileName = String.format(Constant.TESSERACT_DATA_FILE_NAME_BEST, lang);
                    downloadURL = String.format(Constant.TESSERACT_DATA_DOWNLOAD_URL_BEST, lang);
                    break;
                case "standard":
                    destFileName = String.format(Constant.TESSERACT_DATA_FILE_NAME_STANDARD, lang);
                    downloadURL = String.format(Constant.TESSERACT_DATA_DOWNLOAD_URL_STANDARD, lang);
                    break;
                default:
                    destFileName = String.format(Constant.TESSERACT_DATA_FILE_NAME_FAST, lang);
                    downloadURL = String.format(Constant.TESSERACT_DATA_DOWNLOAD_URL_FAST, lang);
            }


            URL url, base, next;
            HttpURLConnection conn;
            try {
                while (true) {
                    Log.v(TAG, "downloading " + downloadURL);
                    try {
                        url = new URL(downloadURL);
                    } catch (java.net.MalformedURLException ex) {
                        Log.e(TAG, "url " + downloadURL + " is bad: " + ex);
                        Logger.logException(ex);
                        return false;
                    }
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setInstanceFollowRedirects(false);
                    switch (conn.getResponseCode()) {
                        case HttpURLConnection.HTTP_MOVED_PERM:
                        case HttpURLConnection.HTTP_MOVED_TEMP:
                            location = conn.getHeaderField("Location");
                            base = new URL(downloadURL);
                            next = new URL(base, location);  // Deal with relative URLs
                            downloadURL = next.toExternalForm();
                            continue;
                    }
                    break;
                }

                conn.connect();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(destFileName);
                byte[] data = new byte[1024 * 6];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                result = false;
                Logger.logException(e);
                Log.e(TAG, "failed to download " + downloadURL + " : " + e);
            }
            return result;
        }

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.search_engine_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.custom_search_engine_url)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.tess_training_data_source)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.language_for_tesseract)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
