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
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

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

    static final int BUFFER = 1024 * 10;
    private static final String TAG = "SettingsActivity";
    static boolean change = true;
    static ConnectivityManager cm;
    // private static DownloadTrainingTask downloadTask;
    private static Resources res;
    private static SweetAlertDialog mSweetAlertDialogForProgressBar;//, confirmDialog;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                String entry = index >= 0 ? (listPreference.getEntries()[index]).toString() : null;
                preference.setSummary(entry);

                Data.IS_TESSERACT_OCR_USE = SpUtil.getInstance().getBoolean(Constant.IS_TESSERACT_IN_USE, false);

                if (preference.getKey().equals(Constant.LANGUAGE_FOR_TESSERACT_OCR) && Data.IS_TESSERACT_OCR_USE) {
                    final String lang = listPreference.getEntryValues()[index >= 0 ? index : 0].toString();
                    if (!isLanguageDataExists(lang)) {
                        mSweetAlertDialogForProgressBar.show();
                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        if (ni == null) {
                            change = false;
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        } else if (ni.isConnected()) {
                            change = true;
                            new DownloadTrainingTask().execute(lang);
                        } else {
                            mSweetAlertDialogForProgressBar.setTitleText("Downloading Failed").setContentText("You are not connected to Internet").setConfirmText("Ok").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            change = false;
                        }
                    } else {
                        change = true;
                    }
                }
            } else if (preference instanceof RingtonePreference) {
                if (!TextUtils.isEmpty(stringValue)) {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));
                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return change;
        }
    };

    private static boolean isLanguageDataExists(String lang) {
        File t = new File(String.format(Constant.TESSERACT_DATA_FILE_NAME,lang));
        boolean r = t.exists();
        Log.v(TAG, "training data for " + lang + " exists? " + r);
        return r;
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

 /*   public static void untarTGzFile(String tar_gz_file, String dest_path) throws IOException {

        File zf = new File(tar_gz_file);
        TarInputStream tis = new TarInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(zf))));
        untar(tis, dest_path);
        tis.close();
    }

    private static void untar(TarInputStream tis, String destFolder) throws IOException {
        BufferedOutputStream dest = null;
        TarEntry entry;
        while ((entry = tis.getNextEntry()) != null) {
            Log.v(TAG, "Extracting: " + entry.getName());
            int count;
            byte data[] = new byte[BUFFER];
            String file_name = entry.getName();
            if (entry.isDirectory()) {
                *//*new File(destFolder + "/" + entry.getName()).mkdirs();*//*
                continue;
            } else {
                int di = entry.getName().lastIndexOf('/');
                if (di != -1) {
                    *//*new File(destFolder + "/" + entry.getName().substring(0, di)).mkdirs();*//*
                    file_name = entry.getName().substring(di + 1, entry.getName().length());
                }
            }
            Log.v(TAG, "writing to " + file_name);
            FileOutputStream fos = new FileOutputStream(destFolder + "/" + file_name *//*entry.getName()*//*);
            dest = new BufferedOutputStream(fos);
            while ((count = tis.read(data)) != -1) {
                dest.write(data, 0, count);
            }

            dest.flush();
            dest.close();
        }
    }*/

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
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
        res = getResources();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
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


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            setHasOptionsMenu(true);

            // Note in Trivia Hack version 2.0 i have ignored the the value set by user because i think google search engine is best for our engine
            //other search engines are not so efficient as Google

            bindPreferenceSummaryToValue(findPreference(getString(R.string.search_engine_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.custom_search_engine_url)));
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


    private static class DownloadTrainingTask extends AsyncTask<String, Integer, Boolean> {
        int totalLenght = 1;
        int downloads_count=0;

        @Override
        protected void onPostExecute(Boolean bool) {
            // finish downloading
            mSweetAlertDialogForProgressBar.setTitleText("Success").setConfirmText("Ok").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }

        @Override
        protected Boolean doInBackground(String... languages) {
            return downloadTraingData(languages[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // update progress bar
            int percent = values[0] / totalLenght;
            mSweetAlertDialogForProgressBar.setTitleText("Please wait.." + downloads_count/ totalLenght + "% downloads");
            mSweetAlertDialogForProgressBar.getProgressHelper().setProgress(percent);
        }

        private boolean downloadTraingData(String lang) {
            boolean result = true;

            String downloadURL=String.format(Constant.TESSERACT_DATA_DOWNLOAD_URL,lang);
            //String downloadURL = res.getString(R.string.training_data_tgz_url_template, lang);
            String location;
            String destFileName=String.format(Constant.TESSERACT_DATA_FILE_NAME,lang);
            //String destFileName = Constant.pathOfTesseractData + res.getString(R.string.training_data_tgz_file_name_template, lang);

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

                totalLenght = conn.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(destFileName);

                byte data[] = new byte[1024 * 6];
               // int total_downloaded = 0;
                int count;

                //upadte progress bar with totalLenght

                while ((count = input.read(data)) != -1) {
                    downloads_count += count;
                    output.write(data, 0, count);
                    publishProgress(downloads_count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                result = false;
                Logger.logException(e);
                Log.e(TAG, "failed to download " + downloadURL + " : " + e);
            }

            if (result) {
               // Log.v(TAG, "unarchive " + destFileName);
                /*try {
                    untarTGzFile(destFileName, Constant.pathOfTesseractData);
                } catch (IOException e) {
                    result = false;
                    Logger.logException(e);
                    Log.e(TAG, "failed to ungzip/untar " + destFileName + " : " + e);
                }*/
            }

            return result;
        }


    }
}
