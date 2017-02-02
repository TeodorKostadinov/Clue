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
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.QR;

import java.util.List;

public class RoomView extends SurfaceView implements Runnable {

    public static final int RECT_LEFT_POINT = 0;
    public static final int RECT_TOP_POINT = 0;
    public static final int DISTANCE_X_FROM_QR = 30;
    public static final int DISTANCE_Y_FROM_QR = 20;
    public static final int CIRCLE_RADIUS = 15;
    public static final int CIRCLE_X = 0;
    public static final int CIRCLE_Y = 0;
    public static final float USER_SPEED = 3f;
    private static final String TAG = "RoomView";
    private static final float TOUCH_PRECISION = 20;
    private static final float DOOR_SIZE = 30;
    private static final float QR_SIZE = 30;
    float personX;
    float personY;
    boolean isFirstTime;
    float newPersonX;
    float newPersonY;
    private Canvas canvas;
    private Bitmap personPoint;
    private SurfaceHolder surface;
    private WindowManager wm;
    private Paint roomPaint;
    private float textHeight = 25;
    private int maxHeight;
    private int maxWidth;
    private List<MapPoint> shape;
    private float ratio;
    private List<Door> doors;
    private MapPoint userPosition;
    private List<QR> qrs;
    private OnDoorClickedListener doorListener;
    private OnQrClickedListener qrListener;
    private boolean canDraw;
    private Thread thread;
    private Bitmap bmpDoor;
    private Bitmap bmpQr;
    private BitmapShader patternBMPshader;

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
        bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.door32);
        bmpQr = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_white_36dp);
        Bitmap bmpFloorPattern = BitmapFactory.decodeResource(getResources(), R.drawable.floor_pattern6);
        patternBMPshader = new BitmapShader(bmpFloorPattern,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        canDraw = false;
        isFirstTime = true;
        thread = null;
        surface = getHolder();
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GREEN);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }

        roomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roomPaint.setStyle(Paint.Style.FILL);

        roomPaint.setColor(Color.LTGRAY);
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public void setQrs(List<QR> qrs) {
        this.qrs = qrs;
    }

    public void setShape(List<MapPoint> shape) {
        this.shape = shape;
        invalidate();
        requestLayout();
    }

    public void updateUserPosition(final MapPoint userPosition) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RoomView.this.userPosition = userPosition;

                if (isFirstTime) {
                    newPersonX = maxWidth * userPosition.getX() - DISTANCE_X_FROM_QR;
                    newPersonY = maxHeight * userPosition.getY() - DISTANCE_Y_FROM_QR;
                    personX = newPersonX;
                    personY = newPersonY;
                    isFirstTime = false;
                } else {
                    newPersonX = maxWidth * userPosition.getX() - DISTANCE_X_FROM_QR;
                    newPersonY = maxHeight * userPosition.getY() - DISTANCE_Y_FROM_QR;
                }

                invalidate();
                requestLayout();
            }
        }, 1000);
    }

    public void setWidthToHeightRatio(float ratio) {
        this.ratio = ratio;
        maxHeight = (int) (maxWidth / ratio);
        invalidate();
        requestLayout();
    }

    @Override
    public void run() {
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        while (canDraw) {

            if (!surface.getSurface().isValid()) {
                continue;
            }
            drawMap(width, height);

        }
    }

    private void drawMap(int width, int height) {
        canvas = surface.lockCanvas();

        roomPaint.setColor(Color.WHITE);
        Rect rec = new Rect(RECT_LEFT_POINT, RECT_TOP_POINT, width, height);
        canvas.drawRect(rec, roomPaint);

        roomPaint.setColor(Color.LTGRAY);

        Path path = new Path();
        path.reset();

        drawFloor(path, patternBMPshader);
        drawDoors();
        drawQrs();
        if (userPosition != null) {

            motionPerson(USER_SPEED);
            canvas.drawBitmap(personPoint, personX, personY, null);
        }
        surface.unlockCanvasAndPost(canvas);
    }

    private void drawQrs() {
        if (qrs != null && qrs.size() > 0) {

            for (QR qr :
                    qrs) {
                canvas.drawBitmap(bmpQr, maxWidth * qr.getX() - QR_SIZE,
                        maxHeight * qr.getY() - QR_SIZE, null);
            }
        }
    }

    private void drawDoors() {
        if (doors != null && doors.size() > 0) {
            for (Door door :
                    doors) {
                canvas.drawBitmap(bmpDoor, maxWidth * door.getX() - DOOR_SIZE,
                        maxHeight * door.getY() - DOOR_SIZE, null);
            }
        }
    }

    private void drawFloor(Path path, BitmapShader patternBMPshader) {
        if (shape != null) {
            for (MapPoint p : shape) {
                path.lineTo(maxWidth * p.getX(), maxHeight * p.getY());
            }
            if (shape.size() > 0) {
                path.lineTo(maxWidth * shape.get(0).getX(), maxHeight * shape.get(0).getY());
            }

            roomPaint.setShader(patternBMPshader);
            path.close();
            canvas.drawPath(path, roomPaint);
            roomPaint.setShader(null);

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

    /**
     * Resize view by using SurfaceHolder.setFixedSize(...). See {@link android.view.SurfaceHolder#setFixedSize}
     * @param width
     * @param height
     */
    public void setFixedViewSize(int width, int height)
    {
        getHolder().setFixedSize(width, height);
    }

    public void resume(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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

        Log.i(TAG, "onMeasure width:" + maxWidth);
        Log.i(TAG, "onMeasure height:" + maxHeight);
        setMeasuredDimension(maxWidth, maxHeight);
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
