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

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balsikandar.crashreporter.CrashReporter;
import com.balsikandar.crashreporter.R;
import com.balsikandar.crashreporter.adapter.CrashLogAdapter;
import com.balsikandar.crashreporter.utils.Constants;
import com.balsikandar.crashreporter.utils.CrashUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by bali on 11/08/17.
 */

public class ExceptionLogFragment extends Fragment {

    private CrashLogAdapter logAdapter;

    private RecyclerView exceptionRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exception_log, container, false);
        exceptionRecyclerView = (RecyclerView) view.findViewById(R.id.exceptionRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAdapter(getActivity(), exceptionRecyclerView);
    }

    private void loadAdapter(Context context, RecyclerView exceptionRecyclerView) {

        logAdapter = new CrashLogAdapter(context, getAllExceptions());
        exceptionRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        exceptionRecyclerView.setAdapter(logAdapter);
    }

    public void clearLog() {
        if (logAdapter != null) {
            logAdapter.updateList(getAllExceptions());
        }
    }

    public ArrayList<File> getAllExceptions() {
        String directoryPath;
        String crashReportPath = CrashReporter.getCrashReportPath();

        if (TextUtils.isEmpty(crashReportPath)) {
            directoryPath = CrashUtil.getDefaultPath();
        } else {
            directoryPath = crashReportPath;
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("The path provided doesn't exists : " + directoryPath);
        }

        ArrayList<File> listOfFiles = new ArrayList<>(Arrays.asList(directory.listFiles()));
        for (Iterator<File> iterator = listOfFiles.iterator(); iterator.hasNext(); ) {
            if (iterator.next().getName().contains(Constants.CRASH_SUFFIX)) {
                iterator.remove();
            }
        }
        Collections.sort(listOfFiles, Collections.reverseOrder());
        return listOfFiles;
    }

}
