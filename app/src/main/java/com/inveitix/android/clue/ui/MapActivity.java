package com.inveitix.android.clue.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.inveitix.android.clue.R;

import butterknife.Bind;

public class MapActivity extends AppCompatActivity {

    @Bind(R.id.grp_map_container)
    ViewGroup grpMapContainer;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_map);


    }
}
