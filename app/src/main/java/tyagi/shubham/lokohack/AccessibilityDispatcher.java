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



    private void printChild(AccessibilityNodeInfo source) {
        if (source==null) return;

        int count = source.getChildCount();
        int p = source.getParent().getChildCount();


        Log.i(TAG, "printChild: child count::::<<<<" + count + "    >>>>all are in tis " + p);
        if (count == 0) {
            Log.i(TAG, "printChild: this are the child " + source.getText() + " class name   <<<" + source.getClassName());

            return;
        }
        for (int i = 0; i < count; i++) {
            printChild(source.getChild(i));
            printChild(source.getTraversalAfter());
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.x = 0;
        lp.y = 100;

        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        final View mview = inflater.inflate(R.layout.floatingwindow, mLayout);
        wm.addView(mLayout, lp);
        mLayout.findViewById(R.id.head).setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = lp.x;
                        initialY = lp.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.
                            Intent intent = new Intent(AccessibilityDispatcher.this, AccessibilityDispatcher.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            //close the service and remove the chat heads
                            stopSelf();
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        lp.x = initialX + (int) (event.getRawX() - initialTouchX);
                        lp.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        wm.updateViewLayout(mview, lp);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });

        setupWidget();

    }

    private void setupWidget() {
        optionA = mLayout.findViewById(R.id.optionA);
        optionB = mLayout.findViewById(R.id.optionB);
        optionC = mLayout.findViewById(R.id.optionC);
        probableAnswer = mLayout.findViewById(R.id.answer);

        getAnswer = mLayout.findViewById(R.id.getanswer);

        getAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccessibilityDispatcher.this, "get answered clicked", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onClick: get answered clicked");
                findAnswer();

            }
        });

    }

    private void findAnswer() {

        String A = "option A", B = "option B", C = "option c";


        if (options.size() > 2) {
            A = (options.get(0));
            B = (options.get(1));
            C = (options.get(2));
        }
/*
        FindAnswers obj = new FindAnswers(question, A, B, C);
        obj.googleSearch();

        String prob = obj.getAnswer();
        probableAnswer.setText(prob);

        A = obj.getAcount() + ": " + A;
        B = obj.getBcount() + ": " + B;
        C = obj.getCcount() + ": " + C;*/

        optionA.setText(A);
        optionB.setText(B);
        optionC.setText(C);


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
