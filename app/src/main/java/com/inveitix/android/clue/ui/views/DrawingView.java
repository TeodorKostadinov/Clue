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
import com.inveitix.android.clue.cmn.Room;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends SurfaceView {

    private static final float DOOR_SIZE = 30;
    private static final float QR_SIZE = 30;
    private static final float POINT_RADIUS = 10;
    public static final String POINT_PREFIX = "point";
    public static final String DOOR_PREFIX = "door";
    public static final String QR_PREFIX = "qr ";
    public String museumId;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private List<MapPoint> shape;
    private List<Door> doors;
    private boolean isFloorFinished;
    private boolean isDoorSelected;
    private DrawDoorListener drawDoorListener;
    private DrawQrListener drawQrListener;
    private boolean isQrSelected;
    private List<QR> qrs;
    private QR qr;
    private String roomId;
    private String qrInfo;
    private Context context;
    private ShapeCreatedListened shapeListener;
    private onRoomCreatedListener roomListener;

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

    public List<MapPoint> getShape() {
        return shape;
    }

    public Room getRoom() {
        Room room = new Room();
        room.setId(roomId);
        room.setDoors(doors);
        room.setMapId(getMuseumId());
        room.setQrs(qrs);
        room.setShape(shape);
        roomListener.onRoomCreated(room);
        return room;
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
        qrs = new ArrayList<>();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isFloorFinished) {
            if (surfaceHolder.getSurface().isValid()) {
                long timeStamp = System.currentTimeMillis();
                MapPoint point = new MapPoint();
                point.setMapId(getMuseumId());
                point.setId(POINT_PREFIX + timeStamp);
                point.setRoomId(this.roomId);
                point.setX(event.getX());
                point.setY(event.getY());
                shape.add(point);
                shapeListener.onShapeCreated(point);
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (MapPoint mapPoint : shape) {
                    canvas.drawCircle(mapPoint.getX(), mapPoint.getY(), POINT_RADIUS, paint);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && isDoorSelected) {
            if (surfaceHolder.getSurface().isValid()) {
                long timeStamp = System.currentTimeMillis();
                Door door = new Door();
                door.setId(DOOR_PREFIX + timeStamp);
                door.setConnectedTo("door1"); //this is for test
                door.setX(event.getX());
                door.setY(event.getY());
                door.setRoomId(roomId);
                door.setMapId(getMuseumId());
                doors.add(door);
                drawFloor();
                drawDoorListener.onDoorDrawn(door);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && isQrSelected) {
            if (surfaceHolder.getSurface().isValid()) {
                showQrInfoDialog();
                long timeStamp = System.currentTimeMillis();
                qr = new QR();
                qr.setX(event.getX());
                qr.setY(event.getY());
                qr.setId(QR_PREFIX + timeStamp);
                qr.setMapId(getMuseumId());
                qr.setRoomId(this.roomId);
                qrs.add(qr);
                drawFloor();
            }
        }
        return false;
    }

    public void showQrInfoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.edt_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_qr_dialog);

        dialogBuilder.setTitle(context.getString(R.string.qr_information));
        dialogBuilder.setMessage(context.getString(R.string.enter_qr_info));
        dialogBuilder.setPositiveButton(context.getString(R.string.txt_done),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setQrInfo(edt.getText().toString());
                        qr.setInfo(qrInfo);
                        drawQrListener.onQrDrawn(qr);
                    }
                });
        AlertDialog b = dialogBuilder.create();
        b.show();
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

        if (qrs.size() > 0) {
            drawQrs();
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

    public void drawQrs() {
        Bitmap bmpDoor = BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_white_36dp);
        paint.setFilterBitmap(true);

        synchronized (surfaceHolder) {
            if (qrs != null && qrs.size() > 0) {
                for (QR qr : qrs) {
                    canvas.drawBitmap(bmpDoor, qr.getX() - QR_SIZE, qr.getY() - QR_SIZE, paint);
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

    public void setQrSelected(boolean qrSelected) {
        this.isQrSelected = qrSelected;
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

    public void setDrawQrListener(DrawQrListener drawQrListener) {
        this.drawQrListener = drawQrListener;
    }

    public void setOnRoomCreatedListener(onRoomCreatedListener roomListener) {
        this.roomListener = roomListener;
    }

    public void setShapeCreatedListener(ShapeCreatedListened shapeListener) {
        this.shapeListener = shapeListener;
    }

    public String getMuseumId() {
        return museumId;
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }

    public void setRoomId(String roomId) {
        if (this.roomId == null || this.roomId.equals("")) {
            this.roomId = roomId;
        }
    }

    public void setQrInfo(String qrInfo) {
        this.qrInfo = qrInfo;
    }

    public interface DrawDoorListener {
        void onDoorDrawn(Door door);
    }

    public interface DrawQrListener {
        void onQrDrawn(QR qr);
    }

    public interface ShapeCreatedListened {
        void onShapeCreated(MapPoint point);
    }

    public interface onRoomCreatedListener {
        void onRoomCreated(Room room);
    }
}

