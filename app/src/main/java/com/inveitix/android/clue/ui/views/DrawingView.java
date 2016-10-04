package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.QR;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    //Metrics
    private float height;
    private float width;
    private float shapePointRadius;

    //Drawing components
    private Paint shapePaint;
    private Paint doorPaint;
    private Paint qrPaint;

    //Data
    private List<MapPoint> shape;
    private List<Door> doors;
    private List<QR> qrs;

    //States
    private boolean isFloorFinished;

    private Context context;
    private OnViewClickedListener listener;

    public DrawingView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePaint.setColor(context.getResources().getColor(R.color.dark_blue));
        shapePointRadius = context.getResources().getDimensionPixelSize(R.dimen.room_view_point_radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFloorFinished) {
            drawFloor(canvas);
            drawDoors(canvas);
            drawQrs(canvas);
        } else {
            drawShape(canvas);
        }
    }

    private void drawShape(Canvas canvas) {
        for (MapPoint point :
                shape) {
            canvas.drawCircle(point.getX() * width, point.getY() * height, shapePointRadius, shapePaint);
        }
    }

    private void drawFloor(Canvas canvas) {

    }

    private void drawDoors(Canvas canvas) {
        Bitmap bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.door32);

    }

    private void drawQrs(Canvas canvas) {
        Bitmap bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_white_36dp);
        //paint.setFilterBitmap(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(listener != null) {
                listener.onViewClicked(e.getX() / width, e.getY() / height);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

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

    public void setIsFloorFinished(boolean isFloorFinished) {
        this.isFloorFinished = isFloorFinished;
        invalidate();
        requestLayout();
    }

    public void setShape(List<MapPoint> shape) {
        this.shape = shape;
        invalidate();
        requestLayout();
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
        invalidate();
        requestLayout();
    }

    public void setQrs(List<QR> qrs) {
        this.qrs = qrs;
        invalidate();
        requestLayout();
    }

    public void setOnViewClickedListener(OnViewClickedListener listener) {
        this.listener = listener;
    }

    public interface OnViewClickedListener {
        void onViewClicked(float proportionX, float proportionY);
    }

}