package com.cbu.dunckel.rushhour;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationService extends IntentService {
    public static final String BROADCAST_ACTION = "Hello World";

    public MyLocationListener listener;

    Intent intent;

    LocationManager locationManager;

    ArrayList<Restaurant> restaurants = new ArrayList<>();

    Stopwatch timer = new Stopwatch();

    private boolean inRestaurant = false;

    private int restaurantPos;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LocationService() {
        super("LocationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        restaurants = intent.getParcelableArrayListExtra("restaurantList");

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location location) {

            Log.i("************", "Location changed");

            Log.i("tag", "location updated: " + location);

            if (inRestaurant){

                float distanceInMeters = location.distanceTo(RestaurantListActivity.listDataRestaurant.get(restaurantPos).getLocation());
                System.out.println("Distance " + RestaurantListActivity.listDataRestaurant.get(restaurantPos).getName() + ": " + distanceInMeters);
                if (distanceInMeters < 15) {
                    System.out.println("we are still in " + RestaurantListActivity.listDataRestaurant.get(restaurantPos).getName());
                    return;
                } else {
                    System.out.println("we left " + RestaurantListActivity.listDataRestaurant.get(restaurantPos).getName());
                    //Are we leaving Restaurant?
                    timer.stop();
                    updateWaitTime(timer.getElapsedTimeSecs()/60,
                            RestaurantListActivity.listDataRestaurant.get(restaurantPos).getWaitTime(),
                            RestaurantListActivity.listDataRestaurant.get(restaurantPos).getName());
                    inRestaurant = false;
                    return;
                }
            } else{
                for (Restaurant restaurant : RestaurantListActivity.listDataRestaurant) {
                    float distanceInMeters = location.distanceTo(restaurant.getLocation());
                    System.out.println("Distance from " + restaurant.getName() + ": " + distanceInMeters);
                    if (distanceInMeters < 15) {
                        System.out.println("we are in " + restaurant.getName());
                        //Start wait timer
                        timer.start();
                        inRestaurant = true;
                        restaurantPos = RestaurantListActivity.listDataRestaurant.indexOf(restaurant);
                        return;
                    } else {
                        System.out.println("we are NOT in " + restaurant.getName());
                    }
                }
            }
//                intent.putExtra("Latitude", location.getLatitude());
//                intent.putExtra("Longitude", location.getLongitude());
//                intent.putExtra("Provider", location.getProvider());
//                sendBroadcast(intent);
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    public void updateWaitTime(double newWaitTime, int waitTime, String restaurantName) {
        //Do not send averaged wait time if data is larger than expected
        //Wait time can be flawed because of individuals eating at the location

        if(newWaitTime <= waitTime*1.5){

            int avgWaitTime = (int) (newWaitTime + waitTime) / 2;

            System.out.println("SENDING TO FIREBASE WAIT TIME: " + avgWaitTime);

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            database.getReference("Restaurants").child(restaurantName).child("wait time").setValue(avgWaitTime);
        }
    }
}
