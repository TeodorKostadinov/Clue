package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class MapPoint {
    public float x;
    public float y;

    public MapPoint() {
    }
    public MapPoint(float x, float y) {
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
