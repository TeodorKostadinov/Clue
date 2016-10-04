package com.inveitix.android.clue.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.database.DBLoader;
import com.inveitix.android.clue.database.DBUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateMapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int PERMISSIONS_REQUEST_LOCATION = 0;
    public static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Bind(R.id.edt_museum_name)
    EditText edtMuseumName;
    @Bind(R.id.edt_description)
    EditText edtDescription;
    @Bind(R.id.txt_location)
    TextView txtLocation;
    private boolean gotLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private Museum museum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        ButterKnife.bind(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @OnClick(R.id.btn_get_location)
    public void getLocation() {
        mGoogleApiClient.connect();
    }

    @OnClick(R.id.btn_create_room)
    public void openRoom() {
        String museumName = edtMuseumName.getText().toString().trim();
        String museumInfo = edtDescription.getText().toString().trim();
        if (museumName.equalsIgnoreCase("") || museumName.length() < 3) {
            edtMuseumName.setError(getString(R.string.err_txt_museum_name));
        } else if (museumInfo.equalsIgnoreCase("") || museumInfo.length() < 10) {
            edtDescription.setError(getString(R.string.err_txt_info));
        } else if (!gotLocation) {
            Toast.makeText(CreateMapActivity.this, R.string.err_txt_location, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CreateMapActivity.this, CreateRoomActivity.class);
            saveMuseum();
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
            intent.putExtra(getString(R.string.museum_id), String.valueOf(this.museum.getId()));
            startActivity(intent);
            finish();
        }
    }

    private void saveMuseum() {
        museum = new Museum();
        museum.setDescription(edtDescription.getText().toString().trim());
        museum.setLocation(currentLatitude + ", " + currentLongitude);
        museum.setName(edtMuseumName.getText().toString().trim());
        museum.setMapSizeKB(512);
        museum.setMapStatus(Museum.STATUS_DOWNLOADED);
        long timeStamp = System.currentTimeMillis() / 1000;
        museum.setId((int) timeStamp);
        DBUtils.getInstance(this).writeMuseumRecord(museum);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestFineLocationPermission();
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            txtLocation.setText(String.valueOf("Lat: " + currentLatitude + ", \nLon: " + currentLongitude));
            gotLocation = true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void requestLocationPermission() {
        ActivityCompat
                .requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
    }

    private void requestFineLocationPermission() {
        ActivityCompat
                .requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
    }
}
