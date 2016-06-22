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
import android.widget.Toast;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.ui.views.DrawingView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;
    @Bind(R.id.fab_done)
    ImageButton fabDone;
    @Bind(R.id.fab_door)
    ImageButton fabDoor;
    @Bind(R.id.fab_qr)
    ImageButton fabQr;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen;
    private List<String> rooms;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        ButterKnife.bind(this);
        openDialog();
        init();
    }

    private void init() {
        timeStamp = System.currentTimeMillis()/1000;
        rooms = new ArrayList<>();
        testRooms();
        drawingView.setDrawDoorListener(new DrawingView.DrawDoorListener() {
            @Override
            public void onDoorDrawn(Door door) {
                openRoomsDialog();
            }
        });
        drawingView.setDrawQrListener(new DrawingView.DrawQrListener() {
            @Override
            public void onQrDrawn(QR qr) {
                Toast.makeText(CreateRoomActivity.this, String.valueOf(qr.getId()), Toast.LENGTH_SHORT).show();
            }
        });
        isFabOpen = false;
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
    }

    private void testRooms() {
        Room room = new Room();
        room.setId("room1");
        Room room2 = new Room();
        room2.setId("room2");
        rooms.add(room.getId());
        rooms.add(room2.getId());
        rooms.add(room2.getId() + "2");
        rooms.add(room2.getId() + "1");
    }

    public void animateFAB() {

        if (isFabOpen) {
            fabDoor.startAnimation(fab_close);
            fabQr.startAnimation(fab_close);
            fabDoor.setClickable(false);
            fabQr.setClickable(false);
            isFabOpen = false;
        } else {
            fabDoor.startAnimation(fab_open);
            fabQr.startAnimation(fab_open);
            fabDoor.setClickable(true);
            fabQr.setClickable(true);
            isFabOpen = true;
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        final Spinner mSpinner = (Spinner) promptsView
                .findViewById(R.id.spn_rooms_list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Clicked : " +
                        parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
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
        drawingView.setMuseumId(getIntent().getStringExtra(getString(R.string.museum_id)));
        drawingView.setRoomId("room " + timeStamp);
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
