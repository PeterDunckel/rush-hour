package com.cbu.dunckel.rushhour;

import android.location.Location;

import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jacobtorres on 11/4/16.
 */

public class Restaurant implements Comparable<Restaurant>{

    private String mName;
    private Location mLocation;
    private int mWaitTime;
    private String mHours;
    private String mMenuURL;
    private List<GraphPoint> mAnalytics; // possible 672 data points (updating every 30 minutes)
    private String backgroundImg;
    private String slimBackgroundImg;

    public List<GraphPoint> getAnalytics() {
        return mAnalytics;
    }

    Restaurant(String name, int waitTime, String hours, String menuURL, String backgroundImg, String slimBackgroundImg){
        this.mName = name;
        this.mWaitTime = waitTime;
        this.mHours = hours;
        this.mMenuURL = menuURL;
        this.backgroundImg = backgroundImg;
        this.slimBackgroundImg = slimBackgroundImg;
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

    public void setAnalytics(List<GraphPoint> analytics) {
        mAnalytics = analytics;
    }

    public int getWaitTime() {
        return mWaitTime;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getSlimBackgroundImg() {
        return slimBackgroundImg;
    }

    public void setSlimBackgroundImg(String slimBackgroundImg) {
        this.slimBackgroundImg = slimBackgroundImg;
    }

    @Override
    public int compareTo(Restaurant o) {
        if(this.mWaitTime < o.getWaitTime()){
            return -1;
        } else if ( this.mWaitTime > o.getWaitTime()){
            return 1;
        } else{
            return 0;
        }
    }
}
