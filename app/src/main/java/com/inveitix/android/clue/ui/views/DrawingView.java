package com.inveitix.android.clue.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.QR;

import java.util.List;

public class DrawingView extends View {

    private static final String TAG = "DrawingView";
    private static final long ANIMATION_DURATION = 2000;
    //Metrics
    private float height;
    private float width;
    private float shapePointRadius;
    private float clickPrecision;

    //Drawing components
    private Paint shapePaint;
    private Paint roomPaint;
    private Paint doorPaint;
    private Paint qrPaint;
    private Bitmap bmpDoor;
    private Bitmap bmpQr;

    //Data
    private List<MapPoint> shape;
    private List<Door> doors;
    private List<QR> qrs;
    private MapPoint userPosition;
    private MapPoint userPositionOnScreen;
    private Bitmap personPoint;

    //States
    private boolean isFloorFinished;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private Context context;
    private OnViewClickedListener listener;
    private OnTouchListener touchListener;
    float dX, dY;
    private ValueAnimator animator;
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
            Log.e(TAG, "View clicked");
            if (listener != null) {
                listener.onViewClicked(e.getX() / width, e.getY() / height);
                Door clickedDoor = getIsDoorClicked(e.getX(), e.getY());
                if (clickedDoor != null) {
                    listener.onDoorClicked(clickedDoor);
                }
                QR clickedQr = getIsQrClicked(e.getX(), e.getY());
                if (clickedQr != null) {
                    listener.onQrClicked(clickedQr);
                }
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
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        handleDragging();
        shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePaint.setColor(context.getResources().getColor(R.color.dark_blue));
        shapePointRadius = context.getResources().getDimensionPixelSize(R.dimen.room_view_point_radius);
        clickPrecision = context.getResources().getDimensionPixelSize(R.dimen.room_view_click_precision);

        roomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roomPaint.setStyle(Paint.Style.FILL);
        roomPaint.setColor(Color.RED);

        bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.door32);
        bmpQr = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_white_36dp);
        personPoint = BitmapFactory.decodeResource(getResources(), R.drawable.ic_room_white_36dp);
    }

    private void handleDragging() {
        touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.setX(event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        invalidate();
                        requestLayout();
                        break;
                    default:
                        return false;
                }
                return true;
            }

        };
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
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);
        if (isFloorFinished) {
            drawFloor(canvas);
            drawDoors(canvas);
            drawQrs(canvas);
            drawUser(canvas);
        } else {
            drawShape(canvas);
        }
        canvas.restore();
    }

    private void drawUser(Canvas canvas) {
        if (userPositionOnScreen != null) {
            canvas.drawBitmap(personPoint,
                    userPositionOnScreen.getX() * width - personPoint.getWidth() / 2,
                    userPositionOnScreen.getY() * height - personPoint.getHeight() + 10,
                    null);
        }
    }

    private void drawShape(Canvas canvas) {
        if (shape != null) {
            for (MapPoint point : shape) {
                canvas.drawCircle(point.getX() * width, point.getY() * height, shapePointRadius, shapePaint);
            }
        }
    }

    private void drawFloor(Canvas canvas) {
        if (shape != null) {
            Path path = new Path();
            path.reset();
            for (MapPoint p : shape) {
                path.lineTo(width * p.getX(), height * p.getY());
            }
            if (shape.size() > 0) {
                path.lineTo(width * shape.get(0).getX(), height * shape.get(0).getY());
            }
            path.close();
            canvas.drawPath(path, shapePaint);
        }
    }

    private void drawDoors(Canvas canvas) {
        if (doors != null && doors.size() > 0) {
            for (Door door : doors) {
                canvas.drawBitmap(bmpDoor, width * door.getX() - bmpDoor.getWidth() / 2,
                        height * door.getY() - bmpDoor.getHeight() / 2, null);
                canvas.drawCircle(width * door.getX(), height * door.getY(), 10, roomPaint);
            }
        }
    }

    private void drawQrs(Canvas canvas) {
        if (qrs != null && qrs.size() > 0) {
            for (QR qr : qrs) {
                canvas.drawBitmap(bmpQr, width * qr.getX() - bmpQr.getWidth() / 2,
                        height * qr.getY() - bmpQr.getHeight() / 2, null);
                canvas.drawCircle(width * qr.getX(), height * qr.getY(), 10, roomPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        touchListener.onTouch(this, event);
        return true;
    }

    private QR getIsQrClicked(float x, float y) {
        for (QR qr : qrs) {
            float qrX = qr.getX() * width;
            float qrY = qr.getY() * height;
            if (Math.abs(x - qrX) < clickPrecision &&
                    Math.abs(y - qrY) < clickPrecision) {
                return qr;
            }
        }
        return null;
    }

    private Door getIsDoorClicked(float x, float y) {
        for (Door door :
                doors) {
            float doorX = door.getX() * width;
            float doorY = door.getY() * height;
            if (Math.abs(x - doorX) < clickPrecision &&
                    Math.abs(y - doorY) < clickPrecision) {
                return door;
            }
        }
        return null;
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

    public void setWidthToHeightRatio(float widthToHeight) {
        this.height = widthToHeight * width;
        invalidate();
        requestLayout();
    }

    public void updateUserPosition(MapPoint mapPoint) {
        this.userPosition = mapPoint;
        final float startX = userPositionOnScreen.getX();
        final float startY = userPositionOnScreen.getY();
        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0, 1).setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float x = userPosition.getX() * fraction + startX * (1 - fraction);
                float y = userPosition.getY() * fraction + startY * (1 - fraction);
                userPositionOnScreen.setX(x);
                userPositionOnScreen.setY(y);
                invalidate();
            }
        });
        animator.start();
    }

    public void setUserPosition(MapPoint userPosition) {
        this.userPosition = userPosition;
        this.userPositionOnScreen = userPosition;
        invalidate();
        requestLayout();
    }

    public interface OnViewClickedListener {
        void onViewClicked(float proportionX, float proportionY);

        void onDoorClicked(Door door);

        void onQrClicked(QR qr);
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

}