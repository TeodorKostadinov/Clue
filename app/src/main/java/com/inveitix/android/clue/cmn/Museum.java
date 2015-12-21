package com.inveitix.android.clue.cmn;

import java.util.List;

/**
 * Created by Tito on 21.12.2015 г..
 */
public class Museum {
    public String name;
    List<Room> rooms;

    public Museum(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }
}
