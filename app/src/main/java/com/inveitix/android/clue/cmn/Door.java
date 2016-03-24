package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class Door {

    private String connectedTo;
    private String id;
    private String mapId;
    private float x;
    private float y;

    public Door() {
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(String connectedTo) {
        this.connectedTo = connectedTo;
    }

    public float getX() {
        return (float) x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return (float) y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
