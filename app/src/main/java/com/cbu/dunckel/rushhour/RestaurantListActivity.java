package com.cbu.dunckel.rushhour;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity implements LocationListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Restaurant> listDataRestaurant;
    HashMap<String, List<Restaurant>> listDataChild;
    Button menuButton;
    FragmentManager fm = getSupportFragmentManager();
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        System.out.println("Checking location permissions...");
        //if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            System.out.println("Performing location determination....");
            Location location = locationManager.getLastKnownLocation(provider);
            System.out.println("Location determination....");
            if (location != null) {
                Log.i("Location Info", "Location acheived!");
                System.out.println("Location achieved....");
            } else {
                Log.i("Location info", "No location :( ");
                System.out.println("Location not achieved....");
            }
        //}

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableRestaurantList);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataRestaurant);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup){
                    expListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Log.i("Location info: Lat", lat.toString());
        Log.i("Location info: lng", lng.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void prepareListData() {
        listDataRestaurant = new ArrayList<Restaurant>();
        listDataChild = new HashMap<String, List<Restaurant>>();

        RestaurantSingleton singleton = RestaurantSingleton.getSharedInstance();
        List<Restaurant> restaurants = singleton.getAllRestaurants();
        Log.d("err",Integer.toString(restaurants.size()));
        for (Restaurant restaurant : restaurants) {

            listDataRestaurant.add(restaurant);

        }
    }

    public void menuButtonClick(View v) {

        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dFragment dialogFragment = new dFragment();
                // Show DialogFragment
                dialogFragment.show(fm, "Dialog Fragment");
            }
        }
        );


    }




}
