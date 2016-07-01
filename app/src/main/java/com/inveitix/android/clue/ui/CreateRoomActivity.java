package com.inveitix.android.clue.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.database.DBUtils;
import com.inveitix.android.clue.interfaces.DrawDoorListener;
import com.inveitix.android.clue.interfaces.DrawQrListener;
import com.inveitix.android.clue.interfaces.ShapeCreatedListened;
import com.inveitix.android.clue.interfaces.onRoomCreatedListener;
import com.inveitix.android.clue.ui.views.DrawingView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;
    @Bind(R.id.fab_door)
    ImageButton fabDoor;
    @Bind(R.id.fab_qr)
    ImageButton fabQr;

    private Animation fab_open;
    private Boolean isFabOpen;
    private ArrayList<String> roomsName;
    private ArrayList<Room> rooms;
    private ArrayList<MapPoint> shape;
    private ArrayList<Door> doors;
    private ArrayList<QR> qrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        ButterKnife.bind(this);
        openDialog();
        init();
    }

    private void init() {
        long timeStamp = System.currentTimeMillis();
        roomsName = new ArrayList<>();
        doors = new ArrayList<>();
        shape = new ArrayList<>();
        qrs = new ArrayList<>();
        rooms = new ArrayList<>();
        testRooms();
        initDrawingView(timeStamp);
        isFabOpen = false;
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
    }

    private void initDrawingView(long timeStamp) {
        drawingView.setMuseumId(getIntent().getStringExtra(getString(R.string.museum_id)));
        drawingView.setRoomId("room " + timeStamp);

        drawingView.setOnRoomCreatedListener(new onRoomCreatedListener() {
            @Override
            public void onRoomCreated(Room room) {
                rooms.add(room);
            }
        });
        drawingView.setDrawDoorListener(new DrawDoorListener() {
            @Override
            public void onDoorDrawn(Door door) {
                openRoomsDialog();
                doors.add(door);
            }
        });
        drawingView.setShapeCreatedListener(new ShapeCreatedListened() {
            @Override
            public void onShapeCreated(MapPoint point) {
                shape.add(point);
            }
        });
        drawingView.setDrawQrListener(new DrawQrListener() {
            @Override
            public void onQrDrawn(QR qr) {
                qrs.add(qr);
            }
        });
    }

    private void testRooms() {
        Room room = new Room();
        room.setId("Soon...");
        roomsName.add(room.getId());
    }

    public void animateFAB() {
        if (isFabOpen) {
            saveMap();
            finish();
        } else {
            fabDoor.startAnimation(fab_open);
            fabQr.startAnimation(fab_open);
            fabDoor.setClickable(true);
            fabQr.setClickable(true);
            isFabOpen = true;
        }
    }

    private void saveMap() {
        drawingView.initRoom();
        for (Room room : rooms) {
            DBUtils.getInstance(this).writeRoomRecord(room);
        }
        for (MapPoint point : shape) {
            DBUtils.getInstance(this).writeShapeRecord(point);
        }
        for (Door door : doors) {
            DBUtils.getInstance(this).writeDoorRecord(door);
        }
        for (QR qr : qrs) {
            DBUtils.getInstance(this).writeQrRecord(qr);
        }
        String mapId = getIntent().getStringExtra(getString(R.string.museum_id));
        createMap(mapId, mapId, rooms, rooms.get(0).getId());
    }

    private void createMap(String id, String museumId, List<Room> rooms, String entranceRoomId) {
        MuseumMap map = new MuseumMap();
        map.setId(id);
        map.setRooms(rooms);
        map.setEntranceRoomId(entranceRoomId);
        map.setMuseumId(Integer.parseInt(museumId));
        DBUtils.getInstance(this).writeMapRecord(map);
    }

    private void openRoomsDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.spinner, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.setTitle(getString(R.string.txt_select_room));
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomsName);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        final Spinner mSpinner = (Spinner) promptsView
                .findViewById(R.id.spn_rooms_list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.fab_door)
    void onDoorClicked() {
        drawingView.setIsDoorSelected(true);
        drawingView.setQrSelected(false);
    }

    @OnClick(R.id.fab_qr)
    void onQrClicked() {
        drawingView.setIsDoorSelected(false);
        drawingView.setQrSelected(true);
    }

    @OnClick(R.id.fab_done)
    public void draw() {
        drawingView.drawFloor();
        drawingView.setIsFloorFinished(true);
        animateFAB();
    }

    public void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.txt_create_room));
        alertDialog.setMessage(getString(R.string.txt_create_instructions));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
