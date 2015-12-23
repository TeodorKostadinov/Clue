package com.inveitix.android.clue;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.interfaces.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecListAdapter.OnDownloadClickedListener {

    @Bind(R.id.rec_view)
    RecyclerView recView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    RecListAdapter adapter;
    private List<Museum> museums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        museums = new ArrayList<>();
        museums.add(new Museum("Vratsa", null, "Vratsa museum 1", 1));
        museums.add(new Museum("Vratsa2", null, "Vratsa museum 2", 2));
        museums.add(new Museum("Vratsa3", null, "Vratsa museum 3", 3));
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecListAdapter(this, museums, this);
        recView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showInfoDialog(position, museums);
            }
        });
    }

    public void showInfoDialog(int position, List<Museum> museums) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(museums.get(position).getName());
        alertDialog.setMessage(museums.get(position).getDescription());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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

    public void onDownloadFinished(int museumID) {
        adapter.updateItem(museumID, Museum.STATUS_DOWNLOADED);
    }

    @Override
    public void onDownloadClicked(final int museumID) {
        adapter.updateItem(museumID, Museum.STATUS_DOWNLOADING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onDownloadFinished(museumID);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
