package com.inveitix.android.clue.cmn;

import java.util.List;


public class Museum {

    public static final int DOWNLOADING_STATUS = 1;
    public static final int DOWNLOADED_STATUS = 2;
    public static final int NOT_DOWNLOADED = 0;

    public String name;
    public String description;
    List<Room> rooms;
    int museumID;
    int mapStatus;

    public Museum(String name, List<Room> rooms, String description, int museumID) {
        this.museumID = museumID;
        this.name = name;
        this.rooms = rooms;
        this.description = description;
        setMapStatus(NOT_DOWNLOADED);

    }

    public int getMapStatus() {
        return mapStatus;
    }

    public void setMapStatus(int mapStatus) {
        this.mapStatus = mapStatus;
    }

    public int getMuseumID() {
        return museumID;
    }

    public void setMuseumID(int museumID) {
        this.museumID = museumID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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
