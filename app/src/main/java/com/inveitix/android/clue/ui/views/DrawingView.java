package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Tito on 3.2.2016 г..
 */
public class DrawingView extends SurfaceView {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SurfaceHolder surfaceHolder;


    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(event.getX(), event.getY(), 20, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        return true;
    }
}
