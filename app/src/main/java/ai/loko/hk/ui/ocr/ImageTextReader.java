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

package ai.loko.hk.ui.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ai.loko.hk.ui.data.Data;


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

            if (textBlocks.size() == 0) {
                return new String[]{"Scan Failed: Found nothing to scan"};
            }

            StringBuilder lines = new StringBuilder();

            if (!Data.FAST_MODE_FOR_OCR) {
                List<Text> textLines = new ArrayList<>();
                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.valueAt(i);
                    // blocks.append(textBlock.getValue()).append(" \n");
                    List<? extends Text> textComponents = textBlock.getComponents();
                    textLines.addAll(textComponents);
                }
                Collections.sort(textLines, new Comparator<Text>() {
                    @Override
                    public int compare(Text t1, Text t2) {
                        int diffOfTops = t1.getBoundingBox().top - t2.getBoundingBox().top;
                        int diffOfLefts = t1.getBoundingBox().left - t2.getBoundingBox().left;
                        if (diffOfTops != 0) {
                            return diffOfTops;
                        }
                        return diffOfLefts;
                    }
                });

                for (Text text : textLines) {
                    if (text != null && text.getValue() != null) {
                        lines.append(text.getValue() + " \n");
                    }
                }
            } else {
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    for (Text line : tBlock.getComponents()) {
                        lines.append(line.getValue()).append(" \n");
                    }
                }
            }
            String lines2 = lines.toString();


            int indexOfQuestionMark = 0;
            if ((indexOfQuestionMark = lines2.indexOf("?")) != -1) {
                String question = lines2.substring(0, indexOfQuestionMark);
                if (indexOfQuestionMark != lines2.length()) {
                    String[] options = lines2.substring(indexOfQuestionMark + 1).split("\n");
                    if (options.length == 3)
                        return new String[]{question, options[0], options[1], options[2], lines2};
                }
            } else if ((indexOfQuestionMark = lines2.indexOf(".")) != -1) {
                String question = lines2.substring(0, indexOfQuestionMark);
                if (indexOfQuestionMark != lines2.length()) {
                    String[] options = lines2.substring(indexOfQuestionMark + 1).split("\n");
                    if (options.length == 3)
                        return new String[]{question, options[0], options[1], options[2], lines2};
                }
            }

            String[] textOnScreen = lines2.split("\n");
            int lineCount = textOnScreen.length;
            if (lineCount > 3) {
                StringBuilder question = new StringBuilder();
                for (int i = 0; i < lineCount - 3; i++) {
                    question.append(textOnScreen[i]);
                }
                return new String[]{question.toString(), textOnScreen[lineCount - 3], textOnScreen[lineCount - 2], textOnScreen[lineCount - 1], lines2};
            }
            return new String[]{"Scan Failed: Could not read options"};

        } else {
            return new String[]{"Scan Failed:  Could not set up the detector!"};
        }
    }

    public String[] getTextFromBitmap2(Bitmap src) {
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
}