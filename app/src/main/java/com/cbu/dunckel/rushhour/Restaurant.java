package com.cbu.dunckel.rushhour;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jacobtorres on 11/4/16.
 */

public class Restaurant implements Parcelable, Comparable<Restaurant> {

    private String mName;
    private Location mLocation;
    private int mWaitTime;
    private String mHours;
    private String mMenuURL;
    private List<GraphPoint> mAnalytics; // possible 672 data points (updating every 30 minutes)
    private String backgroundImg;
    private String slimBackgroundImg;

    protected Restaurant(Parcel in) {
        mName = in.readString();
//        mLocation = in.readParcelable(Location.class.getClassLoader());
        mLocation=Location.CREATOR.createFromParcel(in);
        mWaitTime = in.readInt();
        mHours = in.readString();
        mMenuURL = in.readString();
        backgroundImg = in.readString();
        slimBackgroundImg = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public List<GraphPoint> getAnalytics() {
        return mAnalytics;
    }

    Restaurant(String name, int waitTime, String hours, String menuURL, String backgroundImg, String slimBackgroundImg, Location location){
        this.mName = name;
        this.mWaitTime = waitTime;
        this.mHours = hours;
        this.mMenuURL = menuURL;
        this.backgroundImg = backgroundImg;
        this.slimBackgroundImg = slimBackgroundImg;
        this.mLocation = location;
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

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        getLocation().writeToParcel(dest, flags);
        dest.writeInt(mWaitTime);
        dest.writeString(mHours);
        dest.writeString(mMenuURL);
        dest.writeString(backgroundImg);
        dest.writeString(slimBackgroundImg);
    }
}
