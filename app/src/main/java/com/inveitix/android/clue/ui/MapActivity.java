package com.inveitix.android.clue.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.Point;
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

    private static final String TAG = "MapActivity";
    public static final String EXTRA_MUSEUM_ID = "museumId";
    private static final String EXTRA_ROOM_ID = "roomId";
    public static final int NO_EXTRA = -1;
    private static final int RC_BARCODE_CAPTURE = 156;

    @Bind(R.id.grp_map_container)
    ViewGroup grpMapContainer;
    @Bind(R.id.room)
    RoomView roomView;
    private Room room;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        final int museumId = getIntent().getIntExtra(EXTRA_MUSEUM_ID, NO_EXTRA);
        MuseumMap map = MapsInstance.getInstance().getMapByMuseumId(museumId);
        String roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);

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
                    startActivity(intent);
                }
            });

            roomView.setOnQrClickedListener(new RoomView.OnQrClickedListener() {
                @Override
                public void onQrClicked(QR qr) {
                    Toast.makeText(MapActivity.this, "Qr CLicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "ACtRez:" + requestCode);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            Log.e(TAG, "QRcode:" + scanResult.getContents());
            String qrId = scanResult.getContents();
            QR qr = room.getQrById(qrId);
            if (qr != null) {
                roomView.updateUserPosition(new Point(qr.getX(), qr.getY()));
            }
        }
    }
}
