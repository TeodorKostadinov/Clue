package com.inveitix.android.clue.refactoring.ui.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.refactoring.data.local.AppDataDao;
import com.inveitix.android.clue.refactoring.data.local.HelperTextDao;
import com.inveitix.android.clue.refactoring.domain.about.AboutDomain;
import com.inveitix.android.clue.refactoring.domain.about.AboutDomainInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity implements AboutDomain.ViewListener {

    @Bind(R.id.txt_version) TextView txtVersion;
    @Bind(R.id.txt_clue_info) TextView txtAboutInfo;

    private AboutDomainInterface domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        this.domain = new AboutDomain(new HelperTextDao(), new AppDataDao());
        domain.setViewListener(this);
        domain.onUiReady();
    }

    @Override
    public void showAboutText(String aboutText, String appVersion) {
        txtAboutInfo.setText(aboutText);
        txtVersion.setText("Version " + appVersion);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
