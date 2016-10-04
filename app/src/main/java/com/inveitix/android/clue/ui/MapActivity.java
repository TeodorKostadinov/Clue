package com.inveitix.android.clue.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.MyScaleGestureListener;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.database.MapsInstance;
import com.inveitix.android.clue.scanner.IntentIntegrator;
import com.inveitix.android.clue.scanner.IntentResult;
import com.inveitix.android.clue.ui.views.RoomView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends AppCompatActivity {

    public static final String EXTRA_MUSEUM_ID = "museumId";
    public static final int NO_EXTRA = -1;
    private static final String TAG = "MapActivity";
    private static final String EXTRA_ROOM_ID = "roomId";
    private static final String EXTRA_PREVIOUS_ROOM_ID = "previousRoomId";
    int museumId = 0;
    @Bind(R.id.room)
    RoomView roomView;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private FrameLayout.LayoutParams mRootParam;
    private Room room;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mRootParam = (FrameLayout.LayoutParams) (findViewById(R.id.grp_map)).getLayoutParams();
        museumId = getIntent().getIntExtra(EXTRA_MUSEUM_ID, NO_EXTRA);
        MuseumMap map = MapsInstance.getInstance().getMapByMuseumId(museumId);
        String roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        mScaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureListener(mRootParam, roomView));
        mGestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());
        roomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        if (roomId == null) {
            roomId = map.getEntranceRoomId();
        }
        room = map.getRoomById(roomId);
        if (room != null) {
            roomView.setShape(room.getShape());
            roomView.setQrs(room.getQrs());
            roomView.setDoors(room.getDoors());
            roomView.setWidthToHeightRatio(0.89f);

            roomView.setOnDoorClickedListener(new RoomView.OnDoorClickedListener() {
                @Override
                public void onDoorClicked(Door door) {
                    Intent intent = new Intent(MapActivity.this, MapActivity.class);
                    intent.putExtra(EXTRA_MUSEUM_ID, museumId);
                    intent.putExtra(EXTRA_ROOM_ID, door.getConnectedTo());
                    intent.putExtra(EXTRA_PREVIOUS_ROOM_ID, room.getId());
                    startActivity(intent);
                }
            });

            roomView.setOnQrClickedListener(new RoomView.OnQrClickedListener() {
                @Override
                public void onQrClicked(QR qr) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                    alertDialog.setTitle(getString(R.string.txt_info));
                    alertDialog.setMessage(qr.getInfo());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            setInitialUserPosition();
        }
    }

    private void setInitialUserPosition() {
        Door door = getEntranceDoor();
        if (door != null) {
            roomView.updateUserPosition(new MapPoint(door.getX(), door.getY()));
        }
    }

    private Door getEntranceDoor() {
        String prevRoomId = getIntent().getStringExtra(EXTRA_PREVIOUS_ROOM_ID);
        for (Door door : room.getDoors()) {
            if ((prevRoomId != null && door.getConnectedTo().equals(prevRoomId))) {
                return door;
            }
        }
        for (Door door : room.getDoors()) {
            if (door.getConnectedTo().equals(Room.EXIT)) {
                return door;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @OnClick(R.id.btn_scan)
    public void openQrScanner() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        roomView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomView.resume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "ACtRez:" + requestCode);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            Log.e(TAG, "QRcode:" + scanResult.getContents());
            String qrId = scanResult.getContents();
            final QR qr = room.getQrById(qrId);
            if (qr != null) {
                roomView.updateUserPosition(new MapPoint(qr.getX(), qr.getY()));
            }
        }
    }

    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (roomView == null) {
                roomView.setShape(room.getShape());
                roomView.setQrs(room.getQrs());
                roomView.setDoors(room.getDoors());
                roomView.setWidthToHeightRatio(0.89f);

                roomView.setOnDoorClickedListener(new RoomView.OnDoorClickedListener() {
                    @Override
                    public void onDoorClicked(Door door) {
                        Intent intent = new Intent(MapActivity.this, MapActivity.class);
                        intent.putExtra(EXTRA_MUSEUM_ID, museumId);
                        intent.putExtra(EXTRA_ROOM_ID, door.getConnectedTo());
                        intent.putExtra(EXTRA_PREVIOUS_ROOM_ID, room.getId());
                        startActivity(intent);
                    }
                });

                roomView.setOnQrClickedListener(new RoomView.OnQrClickedListener() {
                    @Override
                    public void onQrClicked(QR qr) {
                        AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                        alertDialog.setTitle(getString(R.string.txt_info));
                        alertDialog.setMessage(qr.getInfo());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
                setInitialUserPosition();
            }
            return true;
        }

    }
}
