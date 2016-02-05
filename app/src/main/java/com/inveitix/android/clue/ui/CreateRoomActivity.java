package com.inveitix.android.clue.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.ui.views.DrawingView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;
    @Bind(R.id.btn_place_qr)
    ToggleButton btnPlaceQr;
    @Bind(R.id.btn_place_door)
    ToggleButton btnPlaceDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        ButterKnife.bind(this);
        openDialog();
        drawingView.setWidthToHeightRatio(0.89f);
        init();
    }

    private void init() {
        btnPlaceDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnPlaceQr.setChecked(false);
                    drawingView.setIsDoorSelected(true);
                }
            }
        });

        btnPlaceQr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnPlaceDoor.setChecked(false);
                    drawingView.setIsDoorSelected(false);
                }
            }
        });
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
