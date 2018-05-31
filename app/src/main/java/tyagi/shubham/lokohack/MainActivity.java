package tyagi.shubham.lokohack;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ol = (Button) findViewById(R.id.olpermission);
        Button acp = (Button) findViewById(R.id.accpermission);
        Button start = (Button) findViewById(R.id.start);
        ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("This permission is required for reading text from screen\n\n" + "" +
                        "By allowing this app will read the content of screen when question is appeared ." +
                        "only supports Loco version 2.1.9 or prior version" + "");
                builder.setCancelable(true);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                    }
                });
                builder.create().show();

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main act", "onClick: start is clicked");
                ComponentName cm=startService(new Intent(MainActivity.this, AccessibilityDispatcher.class));
            }
        });
    }
}
