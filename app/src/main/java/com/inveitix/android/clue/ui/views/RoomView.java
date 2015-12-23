package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fos on 23.12.2015 г..
 */
public class RoomView extends View {

    private Paint textPaint;
    private Paint roomPaint;
    private float textHeight = 25;
    private int textColor = 0x000000;
    private int maxHeight;
    private int maxWidth;

    public RoomView(Context context) {
        super(context);
        init();
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }

        roomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roomPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.maxWidth = w;
        this.maxHeight = h;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(0, 0, 5, roomPaint);
        canvas.drawCircle(maxWidth, 0, 5, roomPaint);
        canvas.drawCircle(0, maxHeight, 5, roomPaint);
        canvas.drawCircle(maxWidth, maxHeight, 5, roomPaint);
    }
}
