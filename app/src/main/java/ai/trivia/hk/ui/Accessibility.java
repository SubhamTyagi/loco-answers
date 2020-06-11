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

package ai.trivia.hk.ui;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import ai.trivia.hk.ui.answers.Engine;
import ai.trivia.hk.ui.model.Question;
import ai.trivia.hk.ui.services.Floating;
import ai.trivia.hk.ui.utils.CustomToast;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;

/**
 * This accessibility may be discontinue in future;
 */
public class Accessibility extends AccessibilityService {

    static String option1 = "",
            option2 = "",
            option3 = "";
    static String question = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) try {
                switch (source.getPackageName().toString().toLowerCase()) {
                    case "com.showtimeapp":
                        getQuestionAndOptionFromScreen(source);
                        break;
                    case "com.brainbaazi":
                        brainbazzi(source);
                        break;
                    case "com.intermedia.hq":
                        hq(source);
                        break;
                    case "qureka.live.game.show":
                        qureka(source);
                        break;
                    case "com.portkey.mobshow":
                        mobshow(source);
                        break;
                    case "com.beamnext.jusplay":
                        justplay(source);
                        break;
                    case "com.ushareit.weshow":
                        weshow(source);
                        break;
                    case "com.kryptolabs.android.speakerswire":
                        swoo(source);
                        break;
                    case "live.trivia.theq":
                        theQ(source);
                        break;
                    case "com.prodege.swagiq":
                        swagiq(source);
                        break;
                }
            } catch (Exception ignored) {

            }
        }

    }

    private void swagiq(AccessibilityNodeInfo source) {
        String str = "com.prodege.swagiq";
        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/txt_question");
            List<AccessibilityNodeInfo> option1id = source.findAccessibilityNodeInfosByViewId(str + ":id/lyt_answers");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1id.get(0).getText().toString();
                option2 = option1id.get(1).getText().toString();
                option3 = option1id.get(2).getText().toString();
                findAnswer(question, option1, option2, option3);

                // isQuestionDisplayed = true;
                questionId.clear();
                option1id.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");

        }

    }

    private void theQ(AccessibilityNodeInfo source) {
        String str = "live.trivia.theq";
        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/questionTitle");
            List<AccessibilityNodeInfo> option1id = source.findAccessibilityNodeInfosByViewId(str + ":id/choiceText");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1id.get(0).getText().toString();
                option2 = option1id.get(1).getText().toString();
                option3 = option1id.get(2).getText().toString();
                findAnswer(question, option1, option2, option3);

                // isQuestionDisplayed = true;
                questionId.clear();
                option1id.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");

        }

    }

    private void swoo(AccessibilityNodeInfo source) {
        String str = "com.kryptolabs.android.speakerswire";
        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/question");
            List<AccessibilityNodeInfo> option1id = source.findAccessibilityNodeInfosByViewId(str + ":id/option_text");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1id.get(0).getText().toString();
                option2 = option1id.get(1).getText().toString();
                option3 = option1id.get(2).getText().toString();
                findAnswer(question, option1, option2, option3);

                // isQuestionDisplayed = true;
                questionId.clear();
                option1id.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }
    }


    private void weshow(AccessibilityNodeInfo source) {

        String str = "m.ushareit.weshow";
        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/question_title");
            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer_options");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1ID.get(0).getText().toString();
                option2 = option1ID.get(1).getText().toString();
                option3 = option1ID.get(2).getText().toString();
                findAnswer(question, option1, option2, option3);

                // isQuestionDisplayed = true;
                questionId.clear();
                option1ID.clear();
            }

        } catch (Exception io) {
            io.printStackTrace();
            //  Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }

    }

    private void justplay(AccessibilityNodeInfo source) {

        String str = "com.beamnext.jusplay";

        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/titleTextView");
            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer1TV");
            List<AccessibilityNodeInfo> option2ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer2TV");
            List<AccessibilityNodeInfo> option3ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer3TV");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1ID.get(0).getText().toString();
                option2 = option2ID.get(0).getText().toString();
                option3 = option3ID.get(0).getText().toString();
                findAnswer(question, option1, option2, option3);


                questionId.clear();
                option1ID.clear();
                option2ID.clear();
                option3ID.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            //  Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }
    }


    private void mobshow(AccessibilityNodeInfo source) {

        String str = "com.portkey.mobshow";

        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/question_text");
            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_button_a");
            List<AccessibilityNodeInfo> option2ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_button_b");
            List<AccessibilityNodeInfo> option3ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_button_c");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1ID.get(0).getText().toString();
                option2 = option2ID.get(0).getText().toString();
                option3 = option3ID.get(0).getText().toString();
                findAnswer(question, option1, option2, option3);

                questionId.clear();
                option1ID.clear();
                option2ID.clear();
                option3ID.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }
    }

    private void hq(AccessibilityNodeInfo source) {
        String str = "com.intermedia.hq";

        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/question");
            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer_button_one");
            List<AccessibilityNodeInfo> option2ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer_button_two");
            List<AccessibilityNodeInfo> option3ID = source.findAccessibilityNodeInfosByViewId(str + ":id/answer_button_three");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1ID.get(0).getText().toString();
                option2 = option2ID.get(0).getText().toString();
                option3 = option3ID.get(0).getText().toString();
                findAnswer(question, option1, option2, option3);


                questionId.clear();
                option1ID.clear();
                option2ID.clear();
                option3ID.clear();
            }
        } catch (Exception io) {
            io.printStackTrace();
            //  Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }
    }

    private void qureka(AccessibilityNodeInfo source) {

        String str = "qureka.live.game.show";

        try {
            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/question");

            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_one");
            List<AccessibilityNodeInfo> option2ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_two");
            List<AccessibilityNodeInfo> option3ID = source.findAccessibilityNodeInfosByViewId(str + ":id/option_three");
            int sizeOfQuestion = questionId.size();

            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();
                option1 = option1ID.get(0).getText().toString();
                option2 = option2ID.get(0).getText().toString();
                option3 = option3ID.get(0).getText().toString();

                findAnswer(question, option1, option2, option3);

                questionId.clear();
                option1ID.clear();
                option2ID.clear();
                option3ID.clear();
            }
        } catch (Exception io) {
          
            // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }
    }

    private void brainbazzi(AccessibilityNodeInfo source) {
        String str = "com.brainbaazi";
        try {

            List<AccessibilityNodeInfo> questionId = source.findAccessibilityNodeInfosByViewId(str + ":id/text_question");
            List<AccessibilityNodeInfo> option1ID = source.findAccessibilityNodeInfosByViewId(str + ":id/button_option1");
            List<AccessibilityNodeInfo> option2ID = source.findAccessibilityNodeInfosByViewId(str + ":id/button_option2");
            List<AccessibilityNodeInfo> option3ID = source.findAccessibilityNodeInfosByViewId(str + ":id/button_option3");
            int sizeOfQuestion = questionId.size();
            if (sizeOfQuestion > 0) {

                question = questionId.get(0).getText().toString();

                option1 = option1ID.get(0).getText().toString();
                option2 = option2ID.get(0).getText().toString();
                option3 = option3ID.get(0).getText().toString();
                findAnswer(question, option1, option2, option3);

                questionId.clear();
                option1ID.clear();
                option2ID.clear();
                option3ID.clear();
            }
        } catch (Exception io) {
            
            showCustomAlert("Error:Accessibility is either switch off or disabled on this screen ");
        }

    }


    void getQuestionAndOptionFromScreen(AccessibilityNodeInfo source) {

        String str = "com.showtimeapp";
        try {
            List<AccessibilityNodeInfo> questionIDs = source.findAccessibilityNodeInfosByViewId(str + ":id/question");
            List<AccessibilityNodeInfo> optionsIDs = source.findAccessibilityNodeInfosByViewId(str + ":id/answer");
            int sizeOfQuestion = questionIDs.size();

            if (sizeOfQuestion > 0) {
                question = questionIDs.get(0).getText().toString();
                option1 = optionsIDs.get(0).getText().toString();
                option2 = optionsIDs.get(1).getText().toString();
                option3 = optionsIDs.get(2).getText().toString();
                findAnswer(question, option1, option2, option3);

                questionIDs.clear();
                optionsIDs.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized private void findAnswer(String question, String option1, String option2, String option3) {
        synchronized (this) {
            new Update().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,question, option1, option2, option3);
        }
    }

    public void showCustomAlert(String msg) {
        new CustomToast(this, msg).show();
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
    }

    private class Update extends AsyncTask<String, Void, String>{
        //FindAnswers obj;

        Engine engine;


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           
            Intent i = new Intent(getApplicationContext(), Floating.class);
            i.putExtra("option1", engine.getA1());
            i.putExtra("option2", engine.getB2());
            i.putExtra("option3", engine.getC3());
            i.putExtra("optionRed", s);
            i.setAction("search");
            startService(i);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);

            engine = new Engine(new Question(strings[0], strings[1], strings[2], strings[3]));
            engine.search();

            if (!engine.isError()) {
                return engine.getAnswer();
            } else {
                engine = new Engine(new Question(strings[0], strings[1], strings[2], strings[3]));
                return engine.search();
            }


        }

    }


}
