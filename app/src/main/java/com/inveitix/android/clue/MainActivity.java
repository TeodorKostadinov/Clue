package com.inveitix.android.clue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Museum> museums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        museums = new ArrayList<>();
        RecyclerView recView = (RecyclerView) findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        RecListAdapter adapter = new RecListAdapter(this, museums);
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

                return true;
            case R.id.action_settings:

                return true;
            default:
                super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
