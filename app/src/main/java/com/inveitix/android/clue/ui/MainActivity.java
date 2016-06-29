package com.inveitix.android.clue.ui;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.inveitix.android.clue.database.DBLoader;
import com.inveitix.android.clue.interfaces.DownloadListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DownloadListener {

    @Bind(R.id.rec_view)
    RecyclerView recView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    RecListAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        loadingListProgress();
        DBLoader.getInstance(this).loadContent(this);
    }

    private void loadingListProgress() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecListAdapter(this);
        recView.setAdapter(adapter);
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

    @OnClick(R.id.btn_add_museum)
    public void addMuseum() {
        Intent createMapIntent = new Intent(MainActivity.this, CreateMapActivity.class);
        startActivity(createMapIntent);
    }

    @Override
    public void onMuseumListDownloaded(List<Museum> museums) {
        adapter.addItems(museums);
        dialog.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshMuseumList();
    }
}
