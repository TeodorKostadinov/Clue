package com.inveitix.android.clue.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.inveitix.android.clue.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateMapActivity extends AppCompatActivity {
    @Bind(R.id.edt_museum_name)
    EditText edtMuseumName;
    @Bind(R.id.edt_description)
    EditText edtDescription;
    private boolean gotLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_location)
    public void getLocation() {
        gotLocation = true;
    }

    @OnClick(R.id.btn_create_room)
    public void openRoom() {
        String museumName = edtMuseumName.getText().toString();
        String museumInfo = edtDescription.getText().toString();
        if (museumName.equalsIgnoreCase("") || museumName.length() < 3) {
            edtMuseumName.setError(getString(R.string.err_txt_museum_name));
        } else if (museumInfo.equalsIgnoreCase("") || museumInfo.length() < 10){
            edtDescription.setError(getString(R.string.err_txt_info));
        } else if(!gotLocation) {
            Toast.makeText(CreateMapActivity.this, R.string.err_txt_location, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CreateMapActivity.this, CreateRoomActivity.class);
            startActivity(intent);
        }
    }
}
