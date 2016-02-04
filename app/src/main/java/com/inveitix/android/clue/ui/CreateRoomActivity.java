package com.inveitix.android.clue.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.ui.views.DrawingView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        ButterKnife.bind(this);
        openDialog();
        drawingView.setWidthToHeightRatio(0.89f);
    }

    @OnClick(R.id.btn_done)
    public void draw(){
        drawingView.drawFloor();
    }

    public void openDialog(){
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
