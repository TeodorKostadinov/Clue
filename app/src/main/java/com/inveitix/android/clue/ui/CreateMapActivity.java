package com.inveitix.android.clue.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inveitix.android.clue.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_create_room)
    public void openRoomDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(CreateMapActivity.this).create();
        alertDialog.setTitle("Create first room");
        alertDialog.setMessage("Touch the screen and mark the corners of the room. When you finish press done button.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateMapActivity.this, CreateRoomActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
