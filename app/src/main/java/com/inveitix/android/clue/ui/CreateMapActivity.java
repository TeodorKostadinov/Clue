package com.inveitix.android.clue.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    public void openRoom() {
        Intent intent = new Intent(CreateMapActivity.this, CreateRoomActivity.class);
        startActivity(intent);

    }
}
