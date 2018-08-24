package ai.loko.hk.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ai.loko.hk.ui.answers.FindAnswers;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.utils.CustomToast;
import ai.myfancy.button.iml.ActionProcessButton;
import ui.R;

public class Test extends AppCompatActivity {
    // String TAG = "TEST";

    String qu = "Who wrote the poem \"O Captain! My Captain!\"?";
    String o1 = "William Shakespeare";
    String o2 = "Walt Whitman";
    String o3 = "Sarah Palin";


    TextView a1, b2, c3;
    EditText q, a, b, c;

    // Button wiki, google;
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
                wiki.setProgress(1);

                a1.setText("");
                b2.setText("");
                c3.setText("");

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
        FindAnswers obj;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ans.setText(s);

            a1.setText(obj.getAcount());
            b2.setText(obj.getBcount());
            c3.setText(obj.getCcount());

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
            // obj = new Engine(new Question(qu, o1, o2, o3));
            //return obj.search();

            obj = new FindAnswers(qu, o1, o2, o3);
            obj.search();
            return obj.getOptionRed();
            //return obj.getAnswer();
        }
    }
}
