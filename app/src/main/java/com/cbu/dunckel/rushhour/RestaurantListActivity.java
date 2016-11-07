package com.cbu.dunckel.rushhour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                System.out.print(groupPosition);
//                Toast toast = Toast.makeText(getApplicationContext(),  Integer.toString(groupPosition), Toast.LENGTH_LONG);
//
//                return false;
//            }
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
}
