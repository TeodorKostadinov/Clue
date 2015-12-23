package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.inveitix.android.clue.cmn.Point;

import java.util.List;

/**
 * Created by fos on 23.12.2015 г..
 */
public class RoomView extends View {

    private static final String TAG = "RoomView";
    private Paint textPaint;
    private Paint roomPaint;
    private float textHeight = 25;
    private int textColor = 0x000000;
    private int maxHeight;
    private int maxWidth;
    private List<Point> shape;

    public RoomView(Context context) {
        super(context);
        Log.e(TAG, "Room created");
        init();
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "Room created");
        init();
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG, "Room created");
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
        Log.e(TAG, "View width:" + maxWidth);
        Log.e(TAG, "View height:" + maxHeight);
    }

    public void setShape(List<Point> shape) {
        this.shape = shape;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(0, 0, 15, roomPaint);
        canvas.drawCircle(maxWidth, 0, 15, roomPaint);
        canvas.drawCircle(0, maxHeight, 15, roomPaint);
        canvas.drawCircle(maxWidth, maxHeight, 15, roomPaint);
        Path path = new Path();
        path.reset();
        if(shape != null) {
            for(Point p : shape) {
                path.lineTo(maxWidth * p.x, maxHeight * p.y);
            }
            if(shape.size() > 0) {
                path.lineTo(maxWidth * shape.get(0).x, maxHeight * shape.get(0).y);
            }
            canvas.drawPath(path, roomPaint);
        }
    }
}
