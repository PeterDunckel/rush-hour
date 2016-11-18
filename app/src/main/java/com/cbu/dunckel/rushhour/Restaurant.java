package com.cbu.dunckel.rushhour;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;

import java.net.URL;
import java.util.List;

/**
 * Created by jacobtorres on 11/4/16.
 */

public class Restaurant {

    private String mName;
    private Location mLocation;
    private int mWaitTime;
    private String mHours;
    private URL mMenuURL;
    private List<Point> mAnalytics; // possible 672 data points (updating every 30 minutes)

    public List<Point> getAnalytics() {
        return mAnalytics;
    }

    Restaurant(String name, int waitTime, String hours){
        this.mName = name;
        this.mWaitTime = waitTime;
        this.mHours = hours;
    }

    public String getHours() {
        return mHours;
    }

    public void setHours(String hours) {
        mHours = hours;
    }

    public void getName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;

    }

    public void setAnalytics(List<Point> analytics) {
        mAnalytics = analytics;
    }

    public int getWaitTime() {
        return mWaitTime;
    }
}
