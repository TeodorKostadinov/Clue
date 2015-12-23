package com.inveitix.android.clue.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Point;
import com.inveitix.android.clue.ui.views.RoomView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    @Bind(R.id.grp_map_container)
    ViewGroup grpMapContainer;
    @Bind(R.id.room)
    RoomView roomView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        List<Point> roomPoints = new ArrayList<>();
        generateFakeRoom(roomPoints);
        roomView.setShape(roomPoints);
        roomView.setWidthToHeightRatio(0.89f);
    }

    private void generateFakeRoom(List<Point> roomPoints) {
        roomPoints.add(new Point(0, 0));
        roomPoints.add(new Point(0.25, 0));
        roomPoints.add(new Point(0.25, 0.25));
        roomPoints.add(new Point(0.5, 0.25));
        roomPoints.add(new Point(0.5, 0.5));
        roomPoints.add(new Point(0.25, 0.5));
        roomPoints.add(new Point(0.25, 0.62));
        roomPoints.add(new Point(0.62, 0.62));
        roomPoints.add(new Point(0.62, 0.12));
        roomPoints.add(new Point(0.75, 0.12));
        roomPoints.add(new Point(0.75, 0.75));
        roomPoints.add(new Point(1, 0.75));
        roomPoints.add(new Point(1, 1));
        roomPoints.add(new Point(0.5, 1));
        roomPoints.add(new Point(0.5, 0.87));
        roomPoints.add(new Point(0.25, 0.87));
        roomPoints.add(new Point(0.25, 1));
        roomPoints.add(new Point(0, 1));
    }
}
