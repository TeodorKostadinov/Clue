package com.inveitix.android.clue.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.database.FireBaseLoader;
import com.inveitix.android.clue.database.MapsInstance;
import com.inveitix.android.clue.interfaces.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecListAdapter.OnDownloadClickedListener, FireBaseLoader.DownloadListener {

    @Bind(R.id.rec_view)
    RecyclerView recView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    RecListAdapter adapter;
    private List<Museum> museums;
    private List<MuseumMap> maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        museums = new ArrayList<>();
        maps = new ArrayList<>();
        ButterKnife.bind(this);
        initViews();
        FireBaseLoader.getInstance(this).downloadMuseumsList(this);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecListAdapter(this, this);
        recView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {

            @Override
            public void onItemClick(Museum museumClicked) {
                showInfoDialog(museumClicked);
            }
        });
    }

    public void showInfoDialog(final Museum museum) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(museum.getName());
        alertDialog.setMessage(museum.getDescription());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ENTER MAP",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startMapActivity(museum.getId());
                    }
                });
        alertDialog.show();
    }

    private void startMapActivity(int museumId) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_MUSEUM_ID, museumId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "Settings pressed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadClicked(final int museumID) {
        adapter.updateItem(museumID, Museum.STATUS_DOWNLOADING);
        FireBaseLoader.getInstance(this).downloadMap(museumID, this);
    }

    @Override
    public void onMuseumListDownloaded(List<Museum> museums) {
        adapter.addItems(museums);
    }

    @Override
    public void onMuseumDownloaded(MuseumMap museum) {
        MapsInstance.getInstance().addMap(museum);
        adapter.updateItem(museum.getMuseumId(), Museum.STATUS_DOWNLOADED);
    }
}
