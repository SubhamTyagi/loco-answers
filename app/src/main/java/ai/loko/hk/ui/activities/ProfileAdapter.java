package ai.loko.hk.ui.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ai.loko.hk.ui.model.Profile;
import ui.R;

class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground, viewForeground;
        TextView profileName, profileX1, profileY1, profileX2, profileY2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            profileX1 = itemView.findViewById(R.id.profile_x1);
            profileY1 = itemView.findViewById(R.id.profile_y1);
            profileX2 = itemView.findViewById(R.id.profile_x2);
            profileY2 = itemView.findViewById(R.id.profile_y2);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }
}
