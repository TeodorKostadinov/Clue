package com.inveitix.android.clue.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.ui.views.DrawingView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;
    @Bind(R.id.btn_place_qr)
    ToggleButton btnPlaceQr;
    @Bind(R.id.btn_place_door)
    ToggleButton btnPlaceDoor;
    @Bind(R.id.spn_rooms_list)
    Spinner spnRoomsList;
    private List<String> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        ButterKnife.bind(this);
        openDialog();
        drawingView.setWidthToHeightRatio(0.89f);
        rooms = new ArrayList<>();
        Room room = new Room();
        room.setId("room1");
        rooms.add(room.getId());
        drawingView.setDrawDoorListener(new DrawingView.DrawDoorListener() {
            @Override
            public void onDoorDrawn(Door door) {
                openRoomsDialog();
            }
        });
    }

    private void openRoomsDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select room which is connected this door");
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.spinner, null);
        dialog.setView(view);
        dialog.setPositiveButton(String.valueOf(R.string.txt_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoomsList.setAdapter(adapter);
        spnRoomsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateRoomActivity.this, rooms.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     }


    @OnCheckedChanged (R.id.btn_place_door)
    void onDoorChecked(boolean checked) {
        if (checked) {
            btnPlaceQr.setChecked(false);
            drawingView.setIsDoorSelected(true);
        }
    }

    @OnCheckedChanged (R.id.btn_place_qr)
    void onQrChecked(boolean checked) {
        if (checked) {
            btnPlaceDoor.setChecked(false);
            drawingView.setIsDoorSelected(false);
        }
    }


    @OnClick(R.id.btn_done)
    public void draw() {
        drawingView.drawFloor();
        btnPlaceQr.setVisibility(View.VISIBLE);
        btnPlaceDoor.setVisibility(View.VISIBLE);
        drawingView.setIsFloorFinished(true);
    }


    public void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.txt_create_room));
        alertDialog.setMessage(getString(R.string.txt_create_instructions));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.txt_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
