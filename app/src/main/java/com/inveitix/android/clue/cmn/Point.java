package com.inveitix.android.clue.cmn;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class Point {
    public double x;
    public double y;

    public Point() {
    }
    public Point(double x, double y) {
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
