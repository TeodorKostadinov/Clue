package com.inveitix.android.clue.cmn;

public class Museum {

    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_DOWNLOADED = 2;
    public static final int STATUS_NOT_DOWNLOADED = 0;

    private String description;
    private int id;
    private String location;
    private int mapSizeKB;
    private int mapStatus;
    private String name;

    public Museum() {
        this.mapStatus = STATUS_NOT_DOWNLOADED;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMapSizeKB() {
        return mapSizeKB;
    }

    public void setMapSizeKB(int mapSizeKB) {
        this.mapSizeKB = mapSizeKB;
    }

    public int getMapStatus() {
        return mapStatus;
    }

    public void setMapStatus(int mapStatus) {
        this.mapStatus = mapStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
