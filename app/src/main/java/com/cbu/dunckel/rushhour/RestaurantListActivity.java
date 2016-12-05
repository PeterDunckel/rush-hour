package com.cbu.dunckel.rushhour;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RestaurantListActivity extends AppCompatActivity {

    private static final String TAG = "MYSERVICE";

    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longitude;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Restaurant> listDataRestaurant = new ArrayList<Restaurant>();
    HashMap<String, List<Restaurant>> listDataChild;
    private Button menuButton;
    FragmentManager fm = getSupportFragmentManager();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onStart() {
        super.onStart();

        getLocation();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // preparing list data
        //prepareListData();

        mRootRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap data = (HashMap) dataSnapshot.child("Restaurants").getValue();

                Iterator it = data.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();

                    String name = (String) pair.getKey();
                    String hours = "Not Available";
                    long waitTime = 0;

                    HashMap values = (HashMap) pair.getValue();
                    Iterator itTwo = values.entrySet().iterator();
                    while (itTwo.hasNext()) {
                        HashMap.Entry set = (HashMap.Entry) itTwo.next();

                        switch ((String) set.getKey()) {
                            case "hours":
                                hours = (String) set.getValue();
                                break;
                            case "wait time":
                                waitTime = (long) set.getValue();
                                break;
                            default:
                                break;
                        }
                        itTwo.remove();
                    }

                    listDataRestaurant.add(new Restaurant(name, (int) waitTime, hours));

                    it.remove();
                }

                System.out.println("-----------------------");
                System.out.println(dataSnapshot.child("Restaurants").getValue());
                System.out.println(listDataRestaurant.size());
                System.out.println("-----------------------");

                //set random graph pts for restaurant
                for (Restaurant r : listDataRestaurant) {
                    int count = 1;
                    List<Point> pts = new ArrayList<>();
                    Random rand = new Random();
                    long epochTime = System.currentTimeMillis();
                    for (int i = 0; i < 10; i++) {

                        pts.add(new Point(epochTime, i * (rand.nextInt(50) + 1) * count));
                        epochTime = System.currentTimeMillis() + (i * 100000);
                    }
                    r.setAnalytics(pts);
                    count++;
                }

                setContentView(R.layout.activity_restaurant_list);

                // get the listview
                expListView = (ExpandableListView) findViewById(R.id.expandableRestaurantList);

                listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataRestaurant);

                // setting list adapter
                expListView.setAdapter(listAdapter);

                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup) {
                            expListView.collapseGroup(previousGroup);
                        }
                        previousGroup = groupPosition;
                    }
                });

                listAdapter.setFM(fm);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void prepareListData() {
        listDataRestaurant = new ArrayList<Restaurant>();
        listDataChild = new HashMap<String, List<Restaurant>>();

        RestaurantSingleton singleton = RestaurantSingleton.getSharedInstance();
        System.out.println("Size = " + singleton.restaurantArray.size());
        List<Restaurant> restaurants = singleton.getAllRestaurants();

        Log.d("Size", Integer.toString(restaurants.size()));
        for (Restaurant restaurant : restaurants) {
            listDataRestaurant.add(restaurant);
        }

    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RestaurantList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void uploadLocation(Location location) {
        Log.i(TAG, "Location to show: " + location);
        Log.i(TAG, "location.getAccuracy(): " + location.getAccuracy());

        if (location.getAccuracy() < 20) {
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
            locationManager.removeUpdates(locationListener);
        }
    }

    private void getLocation() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.i(TAG, "last known location: " + location);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i(TAG, "location updated: " + location);
                uploadLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }
}
