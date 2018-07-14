package ai.loko.hk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import ui.R;

public class Forground extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
//SOME CODES HERE
        
    }

    @Override
    protected void onResume() {
        super.onResume();
       //SOME CODES HERE
    }

    @Override
    protected void onPause() {
        super.onPause();
     //SOME CODES HERE

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
