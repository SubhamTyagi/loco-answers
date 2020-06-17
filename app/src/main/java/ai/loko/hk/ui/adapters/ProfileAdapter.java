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

package ai.loko.hk.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ai.loko.hk.ui.adapters.ProfileAdapter.MyViewHolder;
import ai.loko.hk.ui.model.Profile;
import ui.R;

public class ProfileAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Profile> profileList;

    private Context context;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.profileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_list_item, viewGroup, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Profile profile = profileList.get(i);
        holder.profileName.setText(profile.getName());
        holder.profileX1.setText(Float.toString(profile.getX1()));
        holder.profileX2.setText(Float.toString(profile.getX2()));
        holder.profileY1.setText(Float.toString(profile.getY1()));
        holder.profileY2.setText(Float.toString(profile.getY2()));
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public void removeProfile(int position) {
        profileList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreProfile(Profile profile, int position) {
        profileList.add(position, profile);
        notifyItemInserted(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout viewForeground;
        TextView profileName, profileX1, profileY1, profileX2, profileY2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            profileX1 = itemView.findViewById(R.id.profile_x1);
            profileY1 = itemView.findViewById(R.id.profile_y1);
            profileX2 = itemView.findViewById(R.id.profile_x2);
            profileY2 = itemView.findViewById(R.id.profile_y2);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }
}
