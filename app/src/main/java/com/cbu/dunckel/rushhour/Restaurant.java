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
    private String mMenuURL;
    private List<Point> mAnalytics; // possible 672 data points (updating every 30 minutes)

    public List<Point> getAnalytics() {
        return mAnalytics;
    }

    Restaurant(String name, int waitTime, String hours, String menuURL){
        this.mName = name;
        this.mWaitTime = waitTime;
        this.mHours = hours;
        this.mMenuURL = menuURL;
    }

    public String getHours() {
        return mHours;
    }

    public void setHours(String hours) {
        mHours = hours;
    }

    public String getMenuURL() {
        return mMenuURL;
    }

    public void setMenuURL(String menuURL) {
        mMenuURL = menuURL;
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
