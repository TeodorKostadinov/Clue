package com.inveitix.android.clue.refactoring.domain.repositories;

import com.inveitix.android.clue.refactoring.domain.repositories.models.HelperTextRecord;

public interface HelperTextRepository {

    void insert(HelperTextRecord data);
    HelperTextRecord findByType(HelperTextType type);


    enum HelperTextType {
        ABOUT, SEARCH, MAP_INFO
    }
}
