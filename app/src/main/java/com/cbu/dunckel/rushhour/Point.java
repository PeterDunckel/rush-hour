package com.cbu.dunckel.rushhour;

/**
 * Created by Dunckel on 11/17/2016.
 */

public class Point {

    private long x;
    private int y;

    public Point(long x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getX() {

        return x;
    }

    public void setX(long x) {
        this.x = x;
    }
}
