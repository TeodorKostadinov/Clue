package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class MapPoint {
    private String id;
    private String mapId;
    private float x;
    private float y;


    public MapPoint() {
    }
    public MapPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public float getX() {
        return (float) x;
    }

    public float getY() {
        return (float) y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
