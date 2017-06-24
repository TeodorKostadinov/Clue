package com.inveitix.android.clue.refactoring.data.local;

import android.content.Context;

import com.inveitix.android.clue.refactoring.domain.about.AppDataRepository;
import com.inveitix.android.clue.refactoring.domain.repositories.HelperTextRepository;
import com.inveitix.android.clue.refactoring.domain.repositories.models.HelperTextRecord;

public class HelperTextDao implements HelperTextRepository {


    @Override
    public void insert(HelperTextRecord data) {
        //TODO implement
    }

    @Override
    public HelperTextRecord findByType(HelperTextType type) {
        switch (type) {
            case ABOUT: return new HelperTextRecord("\\nClue is an app for navigation inside buildings and closed spaces.\n" +
                    "    It uses barcode- and symbol-scanning to allow the person to navigate.\n" +
                    "    The main advantage of the app is that it`s easily and cheaply implemented for any type of building.\n" +
                    "    Navigation is done by a map with points of interest or by augmented reality directions.");
        }
        return null;
    }
}
