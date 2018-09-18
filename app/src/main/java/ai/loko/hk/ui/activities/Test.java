package ai.loko.hk.ui.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import ai.loko.hk.ui.answers.Engine;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.model.Question;
import ai.loko.hk.ui.utils.CustomToast;
import ai.myfancy.button.iml.ActionProcessButton;
import ui.R;

public class Test extends AppCompatActivity {
    private static final String TAG="Test";

    String qu = "Who wrote the poem \"O Captain! My Captain!\"?";
    String o1 = "William Shakespeare";
    String o2 = "Walt Whitman";
    String o3 = "Sarah Palin";

    String[] quA = {"Who wrote the poem \"O Captain! My Captain!\"?", "Which one of these Japanese alcoholic drinks is made from rice, yams and wear or brown sugar?", "Bogota is the high altitude capital of which country?", "The Trout Memo was an espionage guidebook written by what British author during WWII?", "What was Mohammed Ali’s birth name?", "Which planet is the closest to Earth?"};
    String[] o1A = {"William Shakespeare", "Umeshu", "Colombia", "Adolf Hitler", "Umayyad", "Venus"};
    String[] o2A = {"Walt Whitman", "Shochu", "Cuba", "Ian Fleming", "Muhammad bin qasim", "Mars"};
    String[] o3A = {"Sarah Palin", "Chubai", "Peru", "John F. Kennedy", "Cassius Clay", "mercury"};


    TextView a1, b2, c3;
    EditText q, a, b, c;

    ActionProcessButton wiki, google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        a1 = findViewById(R.id.optionAtest);
        b2 = findViewById(R.id.optionBtest);
        c3 = findViewById(R.id.optionCtest);

        wiki = findViewById(R.id.wiki1);
        google = findViewById(R.id.getanswer1);
        google.setMode(ActionProcessButton.Mode.ENDLESS);
        wiki.setMode(ActionProcessButton.Mode.ENDLESS);


        new CustomToast(this, "In future this will be used for AI learning").setDuration(1).show();
        q = findViewById(R.id.q_test);
        a = findViewById(R.id.a_test);
        b = findViewById(R.id.b_test);
        c = findViewById(R.id.c_test);

        q.setText(qu);
        a.setText(o1);
        b.setText(o2);
        c.setText(o3);

        wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Which.itIsGoogle = false;
                Which.isWikiDone = false;
                wiki.setProgress(1);

                a1.setText("");
                b2.setText("");
                c3.setText("");

                Random random = new Random(System.currentTimeMillis());
                int index = random.nextInt(5);
                qu = quA[index];
                o1 = o1A[index];
                o2 = o2A[index];
                o3 = o3A[index];

                q.setText(qu);
                a.setText(o1);
                b.setText(o2);
                c.setText(o3);

                if (q.getText().toString().length() > 0) {
                    qu = q.getText().toString();
                }
                if (a.getText().toString().length() > 0) {
                    o1 = a.getText().toString();
                }
                if (b.getText().toString().length() > 0) {
                    o2 = b.getText().toString();
                }
                if (c.getText().toString().length() > 0) {
                    o3 = c.getText().toString();
                }
                new Update().execute("save this to AI database");
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Which.itIsGoogle = true;
                google.setProgress(1);

                a1.setText("");
                b2.setText("");
                c3.setText("");

                Random random = new Random(System.currentTimeMillis());
                int index = random.nextInt(5);
                qu = quA[index];
                o1 = o1A[index];
                o2 = o2A[index];
                o3 = o3A[index];

                q.setText(qu);
                a.setText(o1);
                b.setText(o2);
                c.setText(o3);

               if (q.getText().toString().length() > 0) {
                    qu = q.getText().toString();
                }
                if (a.getText().toString().length() > 0) {
                    o1 = a.getText().toString();
                }
                if (b.getText().toString().length() > 0) {
                    o2 = b.getText().toString();
                }
                if (c.getText().toString().length() > 0) {
                    o3 = c.getText().toString();
                }
                new Update().execute("checking AI>Who are you");
            }
        });


    }

    private class Update extends AsyncTask<String, Void, String> {
        // FindAnswers obj;
        Engine obj;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ans.setText(s);

            Log.d(TAG, "Option 1==>"+obj.getA1());
            Log.d(TAG, "Option 2==>"+obj.getB2());
            Log.d(TAG, "Option 3==>"+obj.getC3());

            a1.setText(obj.getA1());
            b2.setText(obj.getB2());
            c3.setText(obj.getC3());

            wiki.setProgress(0);
            google.setProgress(0);
            switch (s) {
                case "a":
                    a1.setTextColor(Color.RED);
                    b2.setTextColor(Color.BLACK);
                    c3.setTextColor(Color.BLACK);
                    break;
                case "b":
                    b2.setTextColor(Color.RED);
                    c3.setTextColor(Color.BLACK);
                    a1.setTextColor(Color.BLACK);
                    break;
                case "c":
                    c3.setTextColor(Color.RED);
                    a1.setTextColor(Color.BLACK);
                    b2.setTextColor(Color.BLACK);
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            obj = new Engine(new Question(qu, o1, o2, o3));
            return obj.search();

            // obj = new FindAnswers(qu, o1, o2, o3);
            //obj.search();
            //return obj.getOptionRed();
            //return obj.getAnswer();
        }
    }
}
