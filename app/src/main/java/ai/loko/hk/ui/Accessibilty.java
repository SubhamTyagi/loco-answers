package ai.loko.hk.ui;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import ai.loko.hk.ui.answers.FindAnswers;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.utils.CustomToast;
import ui.R;

public class Accessibilty extends AccessibilityService {
    //  private static final String TAG = "Accessibility";
    static String option1 = "", option2 = "", option3 = "";
    static String question = "";

    //public static boolean isGoogle=true;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        ///AccessibilityEvent.
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                try {
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

                } catch (Exception e) {

                }
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
            showCustomAlert("Some error occured");

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
            showCustomAlert("Some error occured");

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
            showCustomAlert("Some error occured");
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
            showCustomAlert("Some error occured");
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
            showCustomAlert("Some error occured");
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
            showCustomAlert("Some error occured");
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
            showCustomAlert("Some error occured");
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
            io.printStackTrace();
           // Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Some error occured");
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
            io.printStackTrace();
            Toast.makeText(this, "Error:::" + io.getMessage(), Toast.LENGTH_SHORT).show();
            showCustomAlert("Some error occured");
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

    private void findAnswer(String question, String option1, String option2, String option3) {
        new Update().execute(question, option1, option2, option3);
    }

    public void showCustomAlert(String msg) {
       new CustomToast(this,msg).show();
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
    }

    private class Update extends AsyncTask<String, Void, String> {
        FindAnswers obj;
        //Engine obj;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Which.itIsGoogle = true;
            Intent i = new Intent(getApplicationContext(), Floating.class);

            i.putExtra("option1", obj.getAcount());
            i.putExtra("option2", obj.getBcount());
            i.putExtra("option3", obj.getCcount());

            ///
            //i.putExtra("option1", obj.getA1());
            //i.putExtra("option2", obj.getB2());
            //i.putExtra("option3", obj.getC3());


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
            try {
                obj = new FindAnswers(strings[0], strings[1], strings[2], strings[3]);
                obj.search();

                //obj = new Engine(new Question(strings[0], strings[1], strings[2], strings[3]));
                //obj.search();

                if (!obj.isError()) {
                    return obj.getOptionRed();
                } else {
                    // obj = new Engine(new Question(strings[0], strings[1], strings[2], strings[3]));
                    obj = new FindAnswers(strings[0], strings[1], strings[2], strings[3]);
                    obj.search();
                    return obj.getOptionRed();
                }
            } catch (Exception e) {
                Crashlytics.log(e.getMessage());
                return "error";
            }

        }
    }

}
