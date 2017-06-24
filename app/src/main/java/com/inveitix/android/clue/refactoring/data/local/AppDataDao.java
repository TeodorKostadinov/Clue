package com.inveitix.android.clue.refactoring.data.local;

import android.content.Context;
import android.content.pm.PackageManager;

import com.inveitix.android.clue.refactoring.domain.about.AppDataRepository;



public class AppDataDao implements AppDataRepository {

    Context context;

    @Override
    public String getVersion() {
        String appVersion = "1.0";
//        try {
//            appVersion = context.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        return appVersion;
    }
}
