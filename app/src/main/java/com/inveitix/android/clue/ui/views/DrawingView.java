package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends SurfaceView {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Canvas canvas;
    private int width;
    private int height;
    private SurfaceHolder surfaceHolder;
    private WindowManager wm;
    private List<Float> pointsWidth;
    private List<Float> pointsHeight;

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
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            public void surfaceCreated(SurfaceHolder holder) {
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                holder.unlockCanvasAndPost(canvas);
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        getScreenSize();
        pointsWidth = new ArrayList<>();
        pointsHeight = new ArrayList<>();
    }


    private void getScreenSize() {
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(event.getX(), event.getY(), 10, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
                pointsWidth.add(event.getX());
                pointsHeight.add(event.getY());
            }
        }
        return false;
    }
}
