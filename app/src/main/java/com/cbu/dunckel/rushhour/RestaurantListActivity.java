package com.cbu.dunckel.rushhour;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

public class RestaurantListActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Restaurant> listDataRestaurant;
    HashMap<String, List<Restaurant>> listDataChild;
    Button menuButton;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

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