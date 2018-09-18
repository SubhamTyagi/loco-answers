package ai.loko.hk.ui.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import ai.loko.hk.ui.services.OCRFloating;

public class BoxListener implements View.OnTouchListener {
    private final OCRFloating service;
    private int[] clipBox = new int[4];
    private float[] clipBox1 = new float[4];
    private float rawX1;
    private float rawX2;
    private float rawY1;
    private float rawY2;
    private float x1;
    private float y1;

    public BoxListener(OCRFloating service) {
        this.service = service;
    }

    private void updateClipBox(float x1, float y1, float x2, float y2) {
        this.clipBox[0] = (int) Math.ceil((double) x1);
        this.clipBox1[0] = x1;
        this.clipBox[1] = (int) Math.ceil((double) y1);
        this.clipBox1[1] = y1;
        this.clipBox[2] = (int) Math.ceil((double) (x2 - x1));
        this.clipBox1[2] = x2;
        this.clipBox[3] = (int) Math.ceil((double) (y2 - y1));
        this.clipBox1[3] = y2;
    }

    private void updateCustomView(BoxView boxView, float x1, float y1, float x2, float y2) {
        int i = (x1 > x2 ? 1 : (x1 == x2 ? 0 : -1));
        if (i > 0 && y1 > y2) {
            boxView.updateRegion(x2, y2, x1, y1);
            updateClipBox(this.rawX2, this.rawY2, this.rawX1, this.rawY1);
        } else if (y1 > y2) {
            boxView.updateRegion(x1, y2, x2, y1);
            updateClipBox(this.rawX1, this.rawY2, this.rawX2, this.rawY1);
        } else if (i > 0) {
            boxView.updateRegion(x2, y1, x1, y2);
            updateClipBox(this.rawX2, this.rawY1, this.rawX1, this.rawY2);
        } else {
            boxView.updateRegion(x1, y1, x2, y2);
            updateClipBox(this.rawX1, this.rawY1, this.rawX2, this.rawY2);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.x1 = motionEvent.getX();
                this.rawX2 = this.rawX1 = motionEvent.getRawX();
                this.y1 = motionEvent.getY();
                this.rawY2 = this.rawY1 = motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                this.service.finishClipMode(this.clipBox, this.clipBox1);
                break;
            case MotionEvent.ACTION_MOVE:
                this.rawX2 = motionEvent.getRawX();
                this.rawY2 = motionEvent.getRawY();
                updateCustomView((BoxView) view, this.x1, this.y1, this.rawX2, this.rawY2);
                break;
            default:
                break;
        }
        return true;
    }
}
