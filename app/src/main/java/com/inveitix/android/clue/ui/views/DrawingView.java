package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends SurfaceView {

    private static final String TAG = "DrawingView";
    private static final float DOOR_SIZE = 30;
    private static final float POINT_RADIUS = 10;

    private Paint paint;
    private Canvas canvas;
    private int maxHeight;
    private int maxWidth;
    private SurfaceHolder surfaceHolder;
    private List<MapPoint> shape;

    private List<Door> doors;
    private float ratio;
    private boolean isFloorFinished;
    private boolean isDoorSelected;
    private DrawDoorListener drawDoorListener;

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

    public void setIsDoorSelected(boolean isDoorSelected) {
        this.isDoorSelected = isDoorSelected;
    }

    public void setIsFloorFinished(boolean isFloorFinished) {
        this.isFloorFinished = isFloorFinished;
    }

    public void setDrawDoorListener(DrawDoorListener drawDoorListener) {
        this.drawDoorListener = drawDoorListener;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        isFloorFinished = false;
        surfaceHolder = this.getHolder();
        prepareCanvas();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        shape = new ArrayList<>();
        doors = new ArrayList<>();
    }

    private void prepareCanvas() {
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
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        int minh = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
//        this.maxWidth = resolveSizeAndState(minw, widthMeasureSpec, 1);
//        this.maxHeight = resolveSizeAndState(minh, heightMeasureSpec, 1);
//        if (ratio != 0) {
//            maxHeight = (int) (maxWidth / ratio);
//        }
//        Log.i(TAG, "onMeasure width:" + maxWidth);
//        Log.i(TAG, "onMeasure height:" + maxHeight);
//        setMeasuredDimension(maxWidth, maxHeight);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isFloorFinished) {
            if (surfaceHolder.getSurface().isValid()) {
                shape.add(new MapPoint(event.getX(), event.getY()));
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (MapPoint point : shape) {
                    canvas.drawCircle(point.getX(), point.getY(), POINT_RADIUS, paint);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && isDoorSelected) {
            if (surfaceHolder.getSurface().isValid()) {
                Door door = new Door();
                door.setConnectedTo("door1"); //this is for test
                door.setX(event.getX());
                door.setY(event.getY());
                doors.add(door);
                drawFloor();
                drawDoorListener.onDoorDrawn(door);
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

        if (shape != null) {
            path.moveTo(shape.get(0).getX(), shape.get(0).getY());
            alignPoints(path);
            canvas.drawColor(Color.WHITE);
            for (int i = 0; i < shape.size(); i++) {
                path.lineTo(shape.get(i).getX(), shape.get(i).getY());
            }
        }
        paint.setShader(patternBMPshader);
        path.close();
        canvas.drawPath(path, paint);
        paint.setShader(null);
        if (doors.size() > 0) {
            drawDoors();
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void drawDoors() {
        Bitmap bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.door32);
        paint.setFilterBitmap(true);

        synchronized (surfaceHolder) {
            if (doors != null && doors.size() > 0) {
                for (Door door : doors) {
                    canvas.drawBitmap(bmpDoor, door.getX() - DOOR_SIZE, door.getY() - DOOR_SIZE, paint);
                }
            }
        }
    }

    private void alignPoints(Path path) {
        MapPoint previousPoint = null;
        for (int i = 0; i < shape.size(); i++) {
            float x = shape.get(i).getX();
            float y = shape.get(i).getY();
            if (previousPoint != null) {
                float deltaX = Math.abs(x - previousPoint.getX());
                float deltaY = Math.abs(y - previousPoint.getY());
                if (Math.max(deltaX, deltaY) == deltaX) {
                    x = previousPoint.getX();
                } else {
                    y = previousPoint.getY();
                }
            }
            path.lineTo(x, y);
            previousPoint = shape.get(i);
        }
    }

//    public void setWidthToHeightRatio(float ratio) {
//        this.ratio = ratio;
//        maxHeight = (int) (maxWidth / ratio);
//        invalidate();
//        requestLayout();
//    }

    public interface DrawDoorListener {
        void onDoorDrawn(Door door);
    }
}

