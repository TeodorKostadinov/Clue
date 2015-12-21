package com.inveitix.android.clue.cmn;

import java.util.List;

/**
 * Created by Tito on 21.12.2015 г..
 */
public class Room {

    int floorNumber;
    List<Exhibit> exhibits;

    public Room(int floorNumber, List<Exhibit> exhibits) {
        this.floorNumber = floorNumber;
        this.exhibits = exhibits;
    }
}
