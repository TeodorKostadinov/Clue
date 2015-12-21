package com.inveitix.android.clue.cmn;

import java.util.List;

/**
 * Created by Tito on 21.12.2015 г..
 */
public class Museum {
    String name;
    List<Floor> floors;
    String description;

    public Museum(String name, List<Floor> floors, String description) {
        this.name = name;
        this.floors = floors;
        this.description = description;
    }
}
