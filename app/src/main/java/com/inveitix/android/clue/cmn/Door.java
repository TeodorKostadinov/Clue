package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class Door {

    private String connectedTo;
    private double x;
    private double y;

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

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return (float) y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
