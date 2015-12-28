package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.inveitix.android.clue.cmn.Point;

import java.util.List;

public class RoomView extends View {

    private static final String TAG = "RoomView";
    private static final float TOUCH_PRECISION = 30;
    private static final float DOOR_SIZE = 30;
    private static final float QR_SIZE = 25;
    private Paint textPaint;
    private Paint roomPaint;
    private float textHeight = 25;
    private int maxHeight;
    private int maxWidth;
    private List<Point> shape;
    private float ratio;
    private List<Point> doors;
    private List<Point> qrs;
    private OnDoorClickedListener doorListener;
    private OnQrClickedListener qrListener;

    public RoomView(Context context) {
        super(context);
        init();
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GREEN);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }

        roomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roomPaint.setStyle(Paint.Style.FILL);
        roomPaint.setColor(Color.BLACK);
    }

    public void setDoors(List<Point> doors) {
        this.doors = doors;
    }

    public void setQrs(List<Point> qrs) {
        this.qrs = qrs;
    }

    public void setShape(List<Point> shape) {
        this.shape = shape;
        invalidate();
        requestLayout();
    }

    public void setWidthToHeightRatio(float ratio) {
        this.ratio = ratio;
        maxHeight = (int) (maxWidth / ratio);
        invalidate();
        requestLayout();
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

        Log.e(TAG, "onMeasure width:" + maxWidth);
        Log.e(TAG, "onMeasure height:" + maxHeight);
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        roomPaint.setColor(Color.BLACK);
        canvas.drawCircle(0, 0, 15, roomPaint);
        canvas.drawCircle(maxWidth, 0, 15, roomPaint);
        canvas.drawCircle(0, maxHeight, 15, roomPaint);
        canvas.drawCircle(maxWidth, maxHeight, 15, roomPaint);
        Path path = new Path();
        path.reset();
        if (shape != null) {
            for (Point p : shape) {
                path.lineTo(maxWidth * p.x, maxHeight * p.y);
            }
            if (shape.size() > 0) {
                path.lineTo(maxWidth * shape.get(0).x, maxHeight * shape.get(0).y);
            }
            canvas.drawPath(path, roomPaint);
        }
        if (doors != null && doors.size() > 0) {
            roomPaint.setColor(Color.GREEN);
            for (Point door :
                    doors) {
                canvas.drawCircle(maxWidth * door.x, maxWidth * door.y, DOOR_SIZE, roomPaint);
            }
        }
        if (qrs != null && qrs.size() > 0) {
            roomPaint.setColor(Color.RED);
            for (Point qr :
                    qrs) {
                canvas.drawCircle(maxWidth * qr.x, maxWidth * qr.y, QR_SIZE, roomPaint);
            }
        }
    }

    public void setOnDoorClickedListener(OnDoorClickedListener doorListener) {
        this.doorListener = doorListener;
    }

    public void setOnDoorQrListener(OnQrClickedListener qrListener) {
        this.qrListener = qrListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (doorListener != null) {
                for (Point door :
                        doors) {
                    if (Math.abs(maxWidth * door.x - event.getX()) < DOOR_SIZE + TOUCH_PRECISION &&
                            Math.abs(maxHeight * door.y - event.getY()) < DOOR_SIZE + TOUCH_PRECISION) {
                        doorListener.onDoorClicked(door);
                    }
                }
            }

            if (qrListener != null) {
                for (Point qr :
                        qrs) {
                    if (Math.abs(maxWidth * qr.x - event.getX()) < QR_SIZE + TOUCH_PRECISION &&
                            Math.abs(maxHeight * qr.y - event.getY()) < QR_SIZE + TOUCH_PRECISION) {
                        doorListener.onDoorClicked(qr);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public interface OnDoorClickedListener {
        void onDoorClicked(Point door);
    }

    public interface OnQrClickedListener {
        void onQrClicked(Point qr);
    }
}
