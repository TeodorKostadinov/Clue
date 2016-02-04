package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends SurfaceView {

    private static final String TAG = "DrawingView";
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Canvas canvas;
    private int maxHeight;
    private int maxWidth;
    private SurfaceHolder surfaceHolder;
    private WindowManager wm;
    private List<MapPoint> points;
    private float ratio;

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
        points = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int minh = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        this.maxWidth = resolveSizeAndState(minw, widthMeasureSpec, 1);
        this.maxHeight = resolveSizeAndState(minh, heightMeasureSpec, 1);
        if (ratio != 0) {
            maxHeight = (int) (maxWidth / ratio);
        }

        Log.i(TAG, "onMeasure width:" + maxWidth);
        Log.i(TAG, "onMeasure height:" + maxHeight);
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (surfaceHolder.getSurface().isValid()) {
                points.add(new MapPoint(event.getX(), event.getY()));
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (MapPoint point : points) {
                    canvas.drawCircle(point.getX(), point.getY(), 10, paint);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        return false;
    }


    public void drawFloor() {
        Bitmap bmpFloorPattern = BitmapFactory.decodeResource(getResources(), R.drawable.floor_pattern6);
        BitmapShader patternBMPshader = new BitmapShader(bmpFloorPattern,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        canvas = surfaceHolder.lockCanvas();
        Path path = new Path();
        path.reset();

        if (points != null) {
            path.moveTo(points.get(0).getX(), points.get(0).getY());
            MapPoint temp = null;
            for (int i = 0; i < points.size(); i++) {
                float x = points.get(i).getX();
                float y = points.get(i).getY();
                if (temp != null) {
                    float deltaX = Math.abs(x - temp.getX());
                    float deltaY = Math.abs(y - temp.getY());

                    if (Math.max(deltaX, deltaY) == deltaX) {
                        x = temp.getX();
                    } else {

                        y = temp.getY();
                    }
                }

                path.lineTo(x, y);
                temp = points.get(i);


            }

            canvas.drawColor(Color.WHITE);
                for (int i = 0; i < points.size(); i++) {
                    path.lineTo(points.get(i).getX(), points.get(i).getY());
                }
            }

        paint.setShader(patternBMPshader);
            path.close();
        canvas.drawPath(path, paint);
            paint.setShader(null);


        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setWidthToHeightRatio(float ratio) {
        this.ratio = ratio;
        maxHeight = (int) (maxWidth / ratio);
        invalidate();
        requestLayout();
    }
}
