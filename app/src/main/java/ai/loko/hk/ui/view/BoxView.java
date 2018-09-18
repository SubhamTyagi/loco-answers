package ai.loko.hk.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class BoxView extends View {
    private final Paint mPaint = new Paint(1);
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public BoxView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(4.0f);
    }

    public void updateRegion(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRect(this.x1, this.y1, this.x2, this.y2, this.mPaint);
    }

}
