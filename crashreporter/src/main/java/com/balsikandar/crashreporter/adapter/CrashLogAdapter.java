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

package com.balsikandar.crashreporter.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balsikandar.crashreporter.R;
import com.balsikandar.crashreporter.ui.LogMessageActivity;
import com.balsikandar.crashreporter.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bali on 10/08/17.
 */

public class CrashLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<File> crashFileList;

    public CrashLogAdapter(Context context, ArrayList<File> allCrashLogs) {
        this.context = context;
        crashFileList = allCrashLogs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item, null);
        return new CrashLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CrashLogViewHolder) holder).setUpViewHolder(context, crashFileList.get(position));
    }

    @Override
    public int getItemCount() {
        return crashFileList.size();
    }


    public void updateList(ArrayList<File> allCrashLogs) {
        crashFileList = allCrashLogs;
        notifyDataSetChanged();
    }


    private class CrashLogViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMsg, messageLogTime;

        CrashLogViewHolder(View itemView) {
            super(itemView);
            messageLogTime = (TextView) itemView.findViewById(R.id.messageLogTime);
            textViewMsg = (TextView) itemView.findViewById(R.id.textViewMsg);
        }

        void setUpViewHolder(final Context context, final File file) {
            final String filePath = file.getAbsolutePath();
            messageLogTime.setText(file.getName().replaceAll("[a-zA-Z_.]", ""));
            textViewMsg.setText(FileUtils.readFirstLineFromFile(new File(filePath)));

            textViewMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LogMessageActivity.class);
                    intent.putExtra("LogMessage", filePath);
                    context.startActivity(intent);
                }
            });
        }
    }
}
