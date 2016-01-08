package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class MapPoint {
    public double x;
    public double y;

    public MapPoint() {
    }
    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return (float) x;
    }

    public float getY() {
        return (float) y;
    }
}
