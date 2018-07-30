package tyagi.shubham.lokohack;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AccessibilityDispatcher extends AccessibilityService {
    public static final String name = AccessibilityDispatcher.class.getSimpleName();
    String TAG = "AccessibilityService";
    ArrayList<String> options = new ArrayList<>();
    String question;
    FrameLayout mLayout;
    TextView optionA, optionB, optionC, probableAnswer;
    Button getAnswer;

    public AccessibilityDispatcher() {

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.e(TAG, "onAccessibilityEvent: event ceated  " + event.getEventType());
        Log.e(TAG, "onAccessibilityEvent: time" + event.getEventTime());

        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {

            AccessibilityNodeInfo source = event.getSource();
            //printChild(source);
            if (source != null) {
                try {
                    // this if is redundant see xml/accessibility.xml file packageName attribute
                    // this if is used fro exten
                    if ("com.showtimeapp".equalsIgnoreCase(source.getPackageName().toString())) {
                        getQuestionAndOptionFromScreen(source);

                    }


                } catch (Exception e) {

                }
            }
        }
    }



    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
    }

    private void findAnswer() {

        String A = "option A", B = "option B", C = "option c";


        if (options.size() > 2) {
            A = (options.get(0));
            B = (options.get(1));
            C = (options.get(2));
        }
        
        Engine  obj = new FindAnswers(new Question(question, A, B, C));
        obj.startDeepLearnigToGetAnswer();

        String prob = obj.getAnswer();
        
        setAnswerForUser();

    }

    void getQuestionAndOptionFromScreen(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str = accessibilityNodeInfo.getPackageName().toString();

        Log.e(TAG, "getQuestionAndOptionFromScreen: getting values");
        Toast.makeText(this, "Reading screen", Toast.LENGTH_SHORT).show();
        printChild(accessibilityNodeInfo);

        try {
            List<AccessibilityNodeInfo> questionIDs = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str + ":id/question");
            List<AccessibilityNodeInfo> optionsIDs = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str + ":id/answer");

            Log.i(TAG, "getQuestionAndOptionFromScreen: question size " + questionIDs.size());
            Log.i(TAG, "getQuestionAndOptionFromScreen: option size " + optionsIDs.size());

            for (AccessibilityNodeInfo question : questionIDs) {
                this.question = question.getText().toString();
                Log.e("", "$$ Question" + accessibilityNodeInfo.getViewIdResourceName() + " -> " + question.getText().toString());
            }
            this.options.clear();

            for (AccessibilityNodeInfo option : optionsIDs) {
                this.options.add(option.getText().toString());
                Log.e("", "$$ Answer " + accessibilityNodeInfo.getViewIdResourceName() + " -> " + option.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
