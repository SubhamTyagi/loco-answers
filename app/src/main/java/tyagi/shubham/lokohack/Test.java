package ai.loko.hk.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ai.loko.hk.ui.answers.Engine;
import ai.loko.hk.ui.answers.FindAnswers;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.model.Question;
import ui.R;

public class Test extends AppCompatActivity {
    String TAG = "TEST";

    String qu = "Who wrote the poem \"O Captain! My Captain!\"?";
    String o1 = "William Shakespeare";
    String o2 = "Walt Whitman";
    String o3 = "Sarah Palin";


    TextView a1, b2, c3;
    EditText q, a, b, c;

    Button wiki, google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        a1 = findViewById(R.id.optionAtest);
        b2 = findViewById(R.id.optionBtest);
        c3 = findViewById(R.id.optionCtest);

        wiki = findViewById(R.id.wiki);
        google = findViewById(R.id.getanswer);

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
                new Update().execute("nothing is send by me here");
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Which.itIsGoogle = true;
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
                new Update().execute("nothing is send by me here");
            }
        });


    }

    private class Update extends AsyncTask<String, Void, String> {
        Engine obj;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ans.setText(s);
            a1.setText(obj.getA1());
            b2.setText(obj.getB2());
            c3.setText(obj.getC3());
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
            //return obj.getAnswer();
        }
    }
}
