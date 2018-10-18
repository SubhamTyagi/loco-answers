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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import ai.loko.hk.ui.constants.Constant;
import ui.R;

public class CropActivity extends AppCompatActivity {

    private static final int IMAGE_PIC_INTENT_CODE = 789;
    private CropImageView mCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        mCropImageView = findViewById(R.id.crop_image_view);

        findViewById(R.id.fab_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RectF rect2 = mCropImageView.getCropWindowRect();

                //float[] points = {rect.left, rect.top, rect.right, rect.bottom};
                float[] points = {rect2.left, rect2.top, rect2.right, rect2.bottom};

                //Log.d("CropActivity", "onClick: point are=" + points[0] + ", " + points[1] + ", " + points[2] + ", " + points[3]);
                //Log.d("CropActivity", "onClick: 0:rect points are" + rect.left + " ," + rect.top + " ," + rect.right + " ," + rect.bottom);
                // Log.d("CropActivity", "onClick: 2:rect points are" + rect2.left + " ," + rect2.top + " ," + rect2.right + " ," + rect2.bottom);

                final Intent backData = new Intent();
                backData.putExtra(Constant.CLIP_POINTS, points);

                AlertDialog.Builder builder = new AlertDialog.Builder(CropActivity.this);
                builder.setTitle("Set Name");
                final EditText input = new EditText(CropActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        backData.putExtra(Constant.PROFILE_NAME, input.getText().toString());
                        setResult(RESULT_OK, backData);
                        finish();
                    }
                });
                builder.show();

            }
        });
        startActivityForResult(getImageUri(), IMAGE_PIC_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_PIC_INTENT_CODE: {
                    Uri imageUri = getPickImageResultUri(data);
                    mCropImageView.setImageUriAsync(imageUri);
                    //Toast.makeText(this, "Please select valid screenshot", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Intent getImageUri() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

}
