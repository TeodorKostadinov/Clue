package com.inveitix.android.clue.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.JustifyTextView;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.constants.MuseumConstants;
import com.inveitix.android.clue.database.DBLoader;
import com.inveitix.android.clue.database.DBUtils;
import com.inveitix.android.clue.database.FireBaseLoader;
import com.inveitix.android.clue.database.MapsInstance;
import com.inveitix.android.clue.interfaces.MapDownloadListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MuseumDetailsActivity extends AppCompatActivity implements MapDownloadListener {
    @Bind(R.id.view_details_back)
    ImageView imgBackground;
    @Bind(R.id.txt_museum_title)
    TextView txtMuseumName;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.txt_museum_info)
    JustifyTextView txtMuseumInfo;
    @Bind(R.id.btn_download2)
    FloatingActionButton btnDownload;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private Museum museum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_details);
        ButterKnife.bind(this);
        animateButton();
        loadMuseum();
        DBLoader.getInstance(this).loadDownloadedMap(this);
        txtMuseumName.setText(museum.getName());
        txtMuseumInfo.setText(museum.getDescription());
        loadImage();
        loadDownloadBtn(museum);
    }

    /**
     * Animate Floating action button to appear and disappear when you starts and close the activity
     */
    private void animateButton() {
        Fade mFade;
        Transition sharedElementEnterTransition;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mFade = new Fade(Fade.IN);
            TransitionManager.beginDelayedTransition(scrollView, mFade);
            btnDownload.hide();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
            sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    btnDownload.show();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    private void loadMuseum() {
        List<Museum> museumList = DBLoader.getInstance(this).getMuseums();
        for (Museum museum : museumList) {
            if (museum.getId() == getIntent().getIntExtra(MuseumConstants.MUSEUM_ID, -1)) {
                this.museum = museum;
                break;
            }
        }
    }

    private void loadImage() {
        if (isConnected()) {
            Picasso.with(this)
                    .load(museum.getImageURL())
                    .resize(300, 200)
                    .centerCrop()
                    .into(imgBackground);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                    || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    private void startMapActivity(int museumId) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_MUSEUM_ID, museumId);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    /**
     * Change the icon of the FAB according to the map status
     * @param museum
     */
    private void loadDownloadBtn(Museum museum) {
        if (museum.getMapStatus() == Museum.STATUS_DOWNLOADED) {
            btnDownload.setImageResource(R.drawable.ic_navigation_white_36dp);
        } else {
            btnDownload.setImageResource(R.drawable.ic_file_download_white_36dp);
        }
    }

    private void updateItem(int museumID, int museumStatus) {

        if (museum.getId() == museumID) {
            if (museumStatus == Museum.STATUS_DOWNLOADED) {
                museum.setMapStatus(Museum.STATUS_DOWNLOADED);
                DBUtils.getInstance(this).updateMapStatus(museumID, Museum.STATUS_DOWNLOADED);
            }
        }
    }

    @OnClick(R.id.btn_download2)
    public void onClick() {
        if (museum.getMapStatus() == Museum.STATUS_DOWNLOADED) {
            startMapActivity(museum.getId());
        } else {
            onDownloadClicked(museum.getId());
        }
    }


    private void onDownloadClicked(int museumID) {
        updateItem(museumID, Museum.STATUS_DOWNLOADING);
        FireBaseLoader.getInstance(this).downloadMap(museumID, this);
        progressBar.setVisibility(View.VISIBLE);
        btnDownload.setClickable(false);
    }

    @Override
    public void onMapDownloaded(MuseumMap museum) {
        MapsInstance.getInstance().addMap(museum);
        updateItem(museum.getMuseumId(), Museum.STATUS_DOWNLOADED);
        btnDownload.setImageResource(R.drawable.ic_navigation_white_36dp);
        btnDownload.setClickable(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        btnDownload.hide();
        super.onBackPressed();
    }
}
