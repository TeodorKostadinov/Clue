package com.inveitix.android.clue.cmn;

import java.util.List;


public class Museum {
    public String name;
    List<Room> rooms;
    public String description;

    public Museum(String name, List<Room> rooms, String description) {
        this.name = name;
        this.rooms = rooms;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
