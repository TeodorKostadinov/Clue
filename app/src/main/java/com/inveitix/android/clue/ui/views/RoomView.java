package com.inveitix.android.clue.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.Point;
import com.inveitix.android.clue.cmn.QR;

import java.util.List;

public class RoomView extends SurfaceView implements Runnable {

    private static final String TAG = "RoomView";
    private static final float TOUCH_PRECISION = 30;
    private static final float DOOR_SIZE = 30;
    private static final float QR_SIZE = 20;
    private static final float USER_SIZE = 20;
    Canvas canvas;
    Bitmap personPoint;
    float personX;
    float personY;
    SurfaceHolder surface;
    private Paint textPaint;
    private Paint roomPaint;
    private float textHeight = 25;
    private int maxHeight;
    private int maxWidth;
    private List<Point> shape;
    private float ratio;
    private List<Door> doors;
    private Point userPosition;
    private List<QR> qrs;
    private OnDoorClickedListener doorListener;
    private OnQrClickedListener qrListener;
    private boolean canDraw;
    private Thread thread;
    boolean isFirstTime;
    float newPersonX;
    float newPersonY;

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
        personPoint = BitmapFactory.decodeResource(getResources(), R.drawable.ic_room_white_36dp);
        canDraw = false;
        thread = null;
        surface = getHolder();
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



    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public void setQrs(List<QR> qrs) {
        this.qrs = qrs;
    }

    public void setShape(List<Point> shape) {
        this.shape = shape;
        invalidate();
        requestLayout();
    }

    public void updateUserPosition(Point userPosition) {
        this.userPosition = userPosition;
        if (isFirstTime) {
                personX = maxWidth * userPosition.getX() - 30;
                personY = maxHeight * userPosition.getY() - 20;
            isFirstTime = false;
        } else {
            newPersonX = maxWidth * userPosition.getX() - 30;
            newPersonY = maxHeight * userPosition.getY() - 20;
        }
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
    public void run() {
        while (canDraw) {

            if (!surface.getSurface().isValid()) {
                continue;
            }
            canvas = surface.lockCanvas();
            Bitmap bitmap;
            roomPaint.setColor(Color.BLACK);
            canvas.drawCircle(0, 0, 15, roomPaint);
            canvas.drawCircle(maxWidth, 0, 15, roomPaint);
            canvas.drawCircle(0, maxHeight, 15, roomPaint);
            canvas.drawCircle(maxWidth, maxHeight, 15, roomPaint);
            Path path = new Path();
            path.reset();
            if (shape != null) {
                for (Point p : shape) {
                    path.lineTo(maxWidth * p.getX(), maxHeight * p.getY());
                }
                if (shape.size() > 0) {
                    path.lineTo(maxWidth * shape.get(0).getX(), maxHeight * shape.get(0).getY());
                }
                canvas.drawPath(path, roomPaint);
            }
            if (doors != null && doors.size() > 0) {
                roomPaint.setColor(Color.GREEN);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.door32);
                for (Door door :
                        doors) {
                    canvas.drawBitmap(bitmap, maxWidth * door.getX(), maxHeight * door.getY(), roomPaint);
                }
            }
            if (qrs != null && qrs.size() > 0) {
                roomPaint.setColor(Color.BLUE);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_white_36dp);
                for (QR qr :
                        qrs) {
                    canvas.drawBitmap(bitmap, maxWidth * qr.getX(), maxHeight * qr.getY(), roomPaint);
                }
            }
            if (userPosition != null) {
                roomPaint.setColor(Color.BLUE);

                motionPerson(1.5f);
                canvas.drawBitmap(personPoint, personX, personY, null);

            }
            surface.unlockCanvasAndPost(canvas);

        }
    }


    public void pause() {
        canDraw = false;

        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        thread = null;

    }

    public void resume() {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
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


    }

    public void setOnDoorClickedListener(OnDoorClickedListener doorListener) {
        this.doorListener = doorListener;
    }

    public void setOnQrClickedListener(OnQrClickedListener qrListener) {
        this.qrListener = qrListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (doorListener != null && doors != null) {
                for (Door door :
                        doors) {
                    if (Math.abs(maxWidth * door.getX() - event.getX()) < DOOR_SIZE + TOUCH_PRECISION &&
                            Math.abs(maxHeight * door.getY() - event.getY()) < DOOR_SIZE + TOUCH_PRECISION) {
                        doorListener.onDoorClicked(door);
                    }
                }
            }

            if (qrListener != null && qrs != null) {
                for (QR qr :
                        qrs) {
                    if (Math.abs(maxWidth * qr.getX() - event.getX()) < QR_SIZE + TOUCH_PRECISION &&
                            Math.abs(maxHeight * qr.getY() - event.getY()) < QR_SIZE + TOUCH_PRECISION) {
                        qrListener.onQrClicked(qr);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void motionPerson(float speed) {

//        if (personX < newPersonX && personY < newPersonY) {
//            personX += speed;
//            personY += speed;
//        }
//
//        if (personX > newPersonX && personY > newPersonY) {
//            personX -= speed;
//            personY -= speed;
//        }
//
        if (personY < newPersonY) {
            personY += speed;
        }

        if (personX < newPersonX) {
            personX += speed;
        }


        if (personY > newPersonY) {
            personY -= speed;
        }

        if (personX > newPersonX) {
            personX -= speed;
        }
    }


    public interface OnDoorClickedListener {
        void onDoorClicked(Door door);
    }

    public interface OnQrClickedListener {
        void onQrClicked(QR qr);
    }
}
