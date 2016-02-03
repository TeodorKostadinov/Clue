package com.inveitix.android.clue.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.ui.views.DrawingView;

import butterknife.Bind;

public class CreateRoomActivity extends AppCompatActivity {
    @Bind(R.id.drawing_view)
    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

    }
}
