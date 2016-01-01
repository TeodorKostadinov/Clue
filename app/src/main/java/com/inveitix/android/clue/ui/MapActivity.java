package com.inveitix.android.clue.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.Point;
import com.inveitix.android.clue.database.MapsInstance;
import com.inveitix.android.clue.ui.views.RoomView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    public static final String EXTRA_MUSEUM_ID = "museumId";

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
        roomView.setDoors(generateDoors());
        roomView.setWidthToHeightRatio(0.89f);

        int museumId = getIntent().getIntExtra(EXTRA_MUSEUM_ID, -1);
        MuseumMap map = MapsInstance.getInstance().getMapByMuseumId(museumId);
        

        roomView.setOnDoorClickedListener(new RoomView.OnDoorClickedListener() {
            @Override
            public void onDoorClicked(Point door) {
                Toast.makeText(MapActivity.this, "Door clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Point> generateDoors() {
        List<Point> doors = new ArrayList<>();
        doors.add(new Point(0.12, 0));
        doors.add(new Point(0.35, 0.35));
        doors.add(new Point(1, 0.87));
        doors.add(new Point(0.70, 1));
        return doors;
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
