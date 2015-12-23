package com.inveitix.android.clue.cmn;

import java.util.List;

public class Museum {

    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_DOWNLOADED = 2;
    public static final int STATUS_NOT_DOWNLOADED = 0;

    private String name;
    private String description;
    private List<Room> rooms;
    private int museumID;
    private int mapStatus;

    public Museum(String name, List<Room> rooms, String description, int museumID) {
        this.museumID = museumID;
        this.name = name;
        this.rooms = rooms;
        this.description = description;
        setMapStatus(STATUS_NOT_DOWNLOADED);

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
