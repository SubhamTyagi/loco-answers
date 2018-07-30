package ai.loko.hk.ui.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ui.R;

public  class CustomToast {
    private Context context = null;
    private String msg;
    private int duration=0;

    public CustomToast(Context context) {
        this.context = context;
    }

    public CustomToast(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public CustomToast setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public CustomToast setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public void show() {
        //Context context = context.getApplicationContext();
        View inflater = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tv = inflater.findViewById(R.id.toast_msg);
        tv.setText(msg == null ? "Alert" : msg);
        Toast toast = new Toast(context);
        toast.setView(inflater);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.show();
    }

/*
    private void show(String msg) {
        //Context context = context.getApplicationContext();
        View inflater = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tv = inflater.findViewById(R.id.toast_msg);
        tv.setText(msg);
        Toast toast = new Toast(context);
        toast.setView(inflater);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(duration);
        toast.show();
    }
 */
}
