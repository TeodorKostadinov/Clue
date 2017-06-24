package com.inveitix.android.clue.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.database.DBLoader;
import com.inveitix.android.clue.interfaces.DownloadListener;
import com.inveitix.android.clue.refactoring.ui.activities.AboutActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements DownloadListener, SearchView.OnQueryTextListener {

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

    private void initSearchView(Menu menu) {
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
            changeSearchViewTextColor(searchView);
        }
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
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
        initSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_about:
                openAboutActivity();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        recView.setAdapter(adapter);
        adapter.searchFilter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recView.setAdapter(adapter);
        adapter.searchFilter(newText);
        return false;
    }

    public void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        intent.putExtra("Key_About_Activity", value);
        startActivity(intent);
    }
}
