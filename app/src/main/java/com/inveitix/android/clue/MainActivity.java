package com.inveitix.android.clue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inveitix.android.clue.cmn.Exhibit;
import com.inveitix.android.clue.cmn.Museum;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Museum> museums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        museums = new ArrayList<>();
    }
}
