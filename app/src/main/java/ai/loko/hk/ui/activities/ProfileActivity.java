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

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ai.loko.hk.ui.adapters.ProfileAdapter;
import ai.loko.hk.ui.constants.Constant;
import ai.loko.hk.ui.db.AppDatabase;
import ai.loko.hk.ui.db.ProfileEntity;
import ai.loko.hk.ui.listeners.ListItemSwipeListener;
import ai.loko.hk.ui.listeners.ListItemTouchListener;
import ai.loko.hk.ui.model.Profile;
import ai.loko.hk.ui.ocr.MediaProjectionHelper;
import ai.loko.hk.ui.ocr.Points;
import ai.loko.hk.ui.services.OCRFloating;
import cn.pedant.SweetAlert.SweetAlertDialog;

import ui.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ProfileActivity extends AppCompatActivity implements ListItemSwipeListener.RecyclerItemTouchHelperListener {

    private static final int CODE_FOR_CROP = 272;
    private static final int storageReadPermission = 412;
    private final String TAG = "ProfileActivity";
    List<Profile> profiles = new ArrayList<>();

    private CoordinatorLayout coordinatorLayout;

    private RecyclerView mRecyclerView;
    private ProfileAdapter mProfileAdapter;

    private AppDatabase db;
    private MediaProjectionManager mMediaProjectionManager;
    private Intent mScreenshotIntent;

    private void takeNecessaryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    storageReadPermission);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        findViewById(R.id.fab_add_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Select Screenshot image")
                        .setContentText("Crop Region containing the Question and Option")
                        .setConfirmText("I understood")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                startActivityForResult(new Intent(ProfileActivity.this, CropActivity.class), CODE_FOR_CROP);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        });

        takeNecessaryPermission();
        mScreenshotIntent = new Intent(this, OCRFloating.class);

        mRecyclerView = findViewById(R.id.recycler_view);
        mProfileAdapter = new ProfileAdapter(getApplicationContext(), profiles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new ListItemTouchListener(getApplicationContext(), mRecyclerView, new ListItemTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Profile profile = profiles.get(position);
                Points.X1 = profile.getX1();
                Points.X2 = profile.getX2();
                Points.Y1 = profile.getY1();
                Points.Y2 = profile.getY2();
                startOCR();
            }

            @Override
            public void onLongClick(View view, int position) {
                // long click is not supported yet
                // Toast.makeText(ProfileActivity.this, position + "long clicked", Toast.LENGTH_SHORT).show();
            }
        }));

        ItemTouchHelper.SimpleCallback itemSimpleCallback = new ListItemSwipeListener(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mProfileAdapter);

        db = AppDatabase.getDatabase(getApplicationContext());
        setUpDataFromDB();
    }


    private void startOCR() {
        if (canOverdraw()) {
            if (!isServiceRunning(OCRFloating.class)) {
                mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), Constant.CODE_FOR_SCREEN_CAPTURE);
            }
        } else {
            requestOverlayPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) switch (requestCode) {
            case CODE_FOR_CROP: {
                float[] points = data.getFloatArrayExtra(Constant.CLIP_POINTS);
                String name = data.getStringExtra(Constant.PROFILE_NAME);

                insertDataToDB(new ProfileEntity(1, name, points[0], points[1], points[2], points[3]));
                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Image Cropped").setConfirmText("Ok").show();
                profiles.add(new Profile(name, points[0], points[1], points[2], points[3]));
                mProfileAdapter.notifyDataSetChanged();
                break;
            }
            case Constant.CODE_FOR_SCREEN_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    MediaProjectionHelper.setMediaProjectionManager(mMediaProjectionManager);
                    MediaProjectionHelper.setScreenshotPermission(data);
                    startService(mScreenshotIntent);
                    finish();
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case storageReadPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
            }
        }
    }

    private boolean canOverdraw() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApplicationContext());
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Toast.makeText(this, "You do not need it", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Profile getProfileForView(ProfileEntity profileEntity) {
        return new Profile(profileEntity.getName(), profileEntity.getX1(), profileEntity.getY1(), profileEntity.getX2(), profileEntity.getY2());
    }

    private ProfileEntity profileFromEntity(Profile deletedProfile) {
        return new ProfileEntity(1, deletedProfile.getName(), deletedProfile.getX1(), deletedProfile.getY1(), deletedProfile.getX2(), deletedProfile.getY2());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ProfileAdapter.MyViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Profile deletedProfile = profiles.get(deletedIndex);

            String name = deletedProfile.getName();
            final ProfileEntity deletedEntity = profileFromEntity(deletedProfile);
            // deletedEntity.setUid()
            mProfileAdapter.removeProfile(deletedIndex);

            deleteDataFromDB(deletedEntity);

            Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removed from list", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProfileAdapter.restoreProfile(deletedProfile, deletedIndex);
                    insertDataToDB(deletedEntity);
                }
            });
            snackbar.show();

        }
    }

    private void setUpDataFromDB() {
         new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<ProfileEntity> profileEntities = db.profileDAO().getAll();
                for (ProfileEntity profileEntity : profileEntities) {
                    // if (profileEntity.getUid() != 0)
                    profiles.add(getProfileForView(profileEntity));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mProfileAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void insertDataToDB(final ProfileEntity profileEntity) {
        new Thread() {
            @Override
            public void run() {
                db.profileDAO().insert(profileEntity);
            }
        }.start();
    }

    private void deleteDataFromDB(final ProfileEntity profileEntity) {
        new Thread() {
            @Override
            public void run() {
                final int uid1 = db.profileDAO().getPrimaryKey(profileEntity.getY1());
                Log.d(TAG, "deleteDataFromDB: " + uid1);
                profileEntity.setUid(uid1);
                db.profileDAO().deleteBY_UID(uid1);
                //db.profileDAO().delete(profileEntity);
            }
        }.start();
    }

}
