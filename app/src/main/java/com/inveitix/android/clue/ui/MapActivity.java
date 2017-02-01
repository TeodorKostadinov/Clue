package com.inveitix.android.clue.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.database.MapsInstance;
import com.inveitix.android.clue.scanner.IntentIntegrator;
import com.inveitix.android.clue.scanner.IntentResult;
import com.inveitix.android.clue.ui.views.DrawingView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.inveitix.android.clue.constants.FireBaseConstants.FIREBASE_STORAGE_ROOT_FOLDER;
import static com.inveitix.android.clue.constants.FireBaseConstants.ONE_MEGABYTE;

public class MapActivity extends AppCompatActivity {

    public static final String EXTRA_MUSEUM_ID = "museumId";
    public static final int NO_EXTRA = -1;
    private static final String TAG = "MapActivity";
    private static final String EXTRA_ROOM_ID = "roomId";
    private static final String EXTRA_PREVIOUS_ROOM_ID = "previousRoomId";
    private StorageReference mStorageRef;
    int museumId = 0;
    @Bind(R.id.room)
    DrawingView roomView;
    private Room room;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        museumId = getIntent().getIntExtra(EXTRA_MUSEUM_ID, NO_EXTRA);
        MuseumMap map = MapsInstance.getInstance().getMapByMuseumId(museumId);
        String roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);

        if (roomId == null && map != null) {
            roomId = map.getEntranceRoomId();
        }
        room = map.getRoomById(roomId);
        if (room != null) {
            initRoomView();
            initRoomViewListener();
            setInitialUserPosition();
        }
    }

    private void initRoomViewListener() {
        roomView.setOnViewClickedListener(new DrawingView.OnViewClickedListener() {
            @Override
            public void onViewClicked(float proportionX, float proportionY) {

            }

            @Override
            public void onDoorClicked(Door door) {
                Intent intent = new Intent(MapActivity.this, MapActivity.class);
                intent.putExtra(EXTRA_MUSEUM_ID, museumId);
                intent.putExtra(EXTRA_ROOM_ID, door.getConnectedTo());
                intent.putExtra(EXTRA_PREVIOUS_ROOM_ID, room.getId());
                startActivity(intent);
            }

            @Override
            public void onQrClicked(QR qr) {
                final String imageName = qr.getMapId() + "_" + qr.getRoomId() + "_" + qr.getId() + "_00.jpg";
                LayoutInflater factory = LayoutInflater.from(MapActivity.this);

                final View view = factory.inflate(R.layout.image_view_layout, null);
                TextView txtItemDescription = (TextView) view.findViewById(R.id.txt_item_description);
                txtItemDescription.setText(qr.getInfo());
                final ImageView imgQr00 = (ImageView) view.findViewById(R.id.img_qr_00);

                StorageReference rootRef = mStorageRef.child(FIREBASE_STORAGE_ROOT_FOLDER).child(imageName);
                rootRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgQr00.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        Log.d(TAG, "Error with downloading imageName. Image name: " + imageName);
                    }
                });

                AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                alertDialog.setTitle(getString(R.string.txt_info));
                alertDialog.setView(view);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void initRoomView() {
        roomView.setIsFloorFinished(true);
        roomView.setShape(room.getShape());
        roomView.setQrs(room.getQrs());
        roomView.setDoors(room.getDoors());
        roomView.setWidthToHeightRatio(0.89f);
    }

    private void setInitialUserPosition() {
        Door door = getEntranceDoor();
        if (door != null) {
            roomView.setUserPosition(new MapPoint(door.getX(), door.getY()));
        } else {
            roomView.setUserPosition(getDefaultDoor());
        }
    }

    private MapPoint getDefaultDoor() {
        for (Door door : room.getDoors()) {
            if (door != null) {
                Log.e(TAG, "No entrance door, setting user to first available door");
                return new MapPoint(door.getX(), door.getY());
            }
        }
        Log.e(TAG, "No doors in room, setting user in corner");
        return new MapPoint(0, 0);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @OnClick(R.id.btn_scale_up)
    public void scaleUpView() {
        roomView.scaleUp();
    }

    @OnClick(R.id.btn_scale_down)
    public void scaleDownView() {
        roomView.scaleDown();
    }
}
