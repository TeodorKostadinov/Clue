package com.inveitix.android.clue;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.database.FireBaseConstants;
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
    Firebase museumFireBaseRef;
    private List<Museum> museums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        museums = new ArrayList<>();
        loadingOnlineDataBase();
        ButterKnife.bind(this);
        initViews();
    }

    private void loadingOnlineDataBase() {

        museumFireBaseRef = new Firebase(FireBaseConstants.FIREBASE_URL).child("museums");
        museumFireBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postMuseum : dataSnapshot.getChildren()) {

                    String name = (String) postMuseum.child("name").getValue();
                    String description = "Hello world!";
                    int id = Integer.parseInt(postMuseum.child("id").getValue().toString());
                    String location = (String) postMuseum.child("location").getValue();
                    int mapSizeKB = Integer.parseInt(postMuseum.child("mapSizeKB").getValue().toString());

                    museums.add(new Museum(name, description, id, location, mapSizeKB));

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
