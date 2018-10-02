/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of LoKo HacK.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    LoKo hacK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LoKo Hack.  If not, see <http://www.gnu.org/licenses/>.
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

package ai.loko.hk.ui.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


/**
 * The type Image text reader.
 */
public class ImageTextReader {

    private static final String TAG = "ImageTextReader";

    private TextRecognizer textRecognizer;

    /**
     * Instantiates a new Image text reader.
     *
     * @param context the context
     */
    public ImageTextReader(Context context) {
        textRecognizer = new TextRecognizer
                .Builder(context)
                .build();

        if (!textRecognizer.isOperational()) {
            Log.d(TAG, "OCR dependencies not available ");
        }
    }

    //will return String[4] 0-> question 1->option1 2->option2 3-> option3
    public String[] getTextFromBitmap(Bitmap src) {
        if (textRecognizer.isOperational() && src != null) {
            Frame frame = new Frame.Builder().setBitmap(src).build();
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
            String blocks = "";
            String lines = "";
            for (int index = 0; index < textBlocks.size(); index++) {
                TextBlock tBlock = textBlocks.valueAt(index);
                blocks = blocks + tBlock.getValue() + "\n";
                for (Text line : tBlock.getComponents()) {
                    lines = lines + line.getValue() + "\n";
                }
            }

            if (textBlocks.size() == 0) {
                // Log.d(TAG, "getTextFromBitmap: Scan Failed: Found nothing to scan");
                return new String[]{"Scan Failed: Found nothing to scan"};
            } else {
                String[] textOnScreen = lines.split("\n");
                int lineCount = textOnScreen.length;
                if (lineCount > 3) {
                    String question = "";
                    for (int i = 0; i < lineCount - 3; i++) {
                        question += textOnScreen[i];
                    }
                    return new String[]{question, textOnScreen[lineCount - 3], textOnScreen[lineCount - 2], textOnScreen[lineCount - 1]};

                }
                return new String[]{"Scan Failed: Could not read options"};

            }
        } else {
            Log.d(TAG, "getTextFromBitmap: Could not set up the detector!");
            return new String[]{"Scan Failed:  Could not set up the detector!"};
        }
    }

    public String[] getTextFromBitmap2(Bitmap src) {
        if (textRecognizer.isOperational() && src != null) {
            Frame frame = new Frame.Builder().setBitmap(src).build();
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
            //String blocks = "";
            String lines = "";
            for (int index = 0; index < textBlocks.size(); index++) {
                TextBlock tBlock = textBlocks.valueAt(index);
                // blocks = blocks + tBlock.getValue() + " \n";
                for (Text line : tBlock.getComponents()) {
                    lines = lines + line.getValue() + " \n";
                }
            }

            if (textBlocks.size() == 0) {
                return new String[]{"Scan Failed: Found nothing to scan"};
            } else {
                if (lines.contains("?")) {
                    int indexOfQuestionMark = lines.indexOf('?');
                    String question = lines.substring(0, indexOfQuestionMark);
                    if (indexOfQuestionMark != lines.length()) {
                        String[] options = lines.substring(indexOfQuestionMark + 1).split("\n");
                        if (options.length == 3)
                            return new String[]{question, options[0], options[1], options[2]};
                    }

                } else if (lines.contains(".")) {
                    int indexOfQuestionMark = lines.indexOf('.');
                    String question = lines.substring(0, indexOfQuestionMark);
                    if (indexOfQuestionMark != lines.length()) {
                        String[] options = lines.substring(indexOfQuestionMark + 1).split("\n");
                        if (options.length == 3)
                            return new String[]{question, options[0], options[1], options[2]};
                    }
                }
                String[] textOnScreen = lines.split("\n");
                int lineCount = textOnScreen.length;
                if (lineCount > 3) {
                    String question = "";
                    for (int i = 0; i < lineCount - 3; i++) {
                        question += textOnScreen[i];
                    }
                    return new String[]{question, textOnScreen[lineCount - 3], textOnScreen[lineCount - 2], textOnScreen[lineCount - 1]};
                }
                return new String[]{"Scan Failed: Could not read options"};
            }
        } else {
            return new String[]{"Scan Failed:  Could not set up the detector!"};
        }
    }


    class RecognizeText extends AsyncTask<Bitmap,Void,String[]>{

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
        }

        @Override
        protected String[] doInBackground(Bitmap... bitmaps) {
            if (textRecognizer.isOperational() && bitmaps != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmaps[0]).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                //String blocks = "";
                String lines = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    for (Text line : tBlock.getComponents()) {
                        lines = lines + line.getValue() + " \n";
                    }
                }
                if (textBlocks.size() == 0) {
                    return new String[]{"Scan Failed: Found nothing to scan"};
                } else {
                    if (lines.contains("?")) {
                        int indexOfQuestionMark = lines.indexOf('?');
                        String question = lines.substring(0, indexOfQuestionMark);
                        if (indexOfQuestionMark != lines.length()) {
                            String[] options = lines.substring(indexOfQuestionMark + 1).split("\n");
                            if (options.length == 3)
                                return new String[]{question, options[0], options[1], options[2]};
                        }

                    } else if (lines.contains(".")) {
                        int indexOfQuestionMark = lines.indexOf('.');
                        String question = lines.substring(0, indexOfQuestionMark);
                        if (indexOfQuestionMark != lines.length()) {
                            String[] options = lines.substring(indexOfQuestionMark + 1).split("\n");
                            if (options.length == 3)
                                return new String[]{question, options[0], options[1], options[2]};
                        }
                    }
                    String[] textOnScreen = lines.split("\n");
                    int lineCount = textOnScreen.length;
                    if (lineCount > 3) {
                        String question = "";
                        for (int i = 0; i < lineCount - 3; i++) {
                            question += textOnScreen[i];
                        }
                        return new String[]{question, textOnScreen[lineCount - 3], textOnScreen[lineCount - 2], textOnScreen[lineCount - 1]};
                    }
                    return new String[]{"Scan Failed: Could not read options"};
                }
            } else {
                return new String[]{"Scan Failed:  Could not set up the detector!"};
            }
        }
    }
}