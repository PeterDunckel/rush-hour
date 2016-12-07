package com.cbu.dunckel.rushhour;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Dunckel on 11/4/2016.
 */

public class RestaurantSingleton {

    // create a static variable with only one instance in memory
    static RestaurantSingleton sharedInstance;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    static ArrayList<Restaurant> restaurantArray = new ArrayList<Restaurant>();

    public void getData(){

        mRootRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap data = (HashMap) dataSnapshot.child("Restaurants").getValue();

                Iterator it = data.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry)it.next();

                    String name = (String) pair.getKey();
                    String hours = "Not Available";
                    long waitTime = 0;
                    String menuURL = "";
                    String backgroundImg = null;
                    String slimBackgroundImg = null;

                    HashMap values = (HashMap) pair.getValue();
                    Iterator itTwo = values.entrySet().iterator();
                    while (itTwo.hasNext()){
                        HashMap.Entry set = (HashMap.Entry)itTwo.next();

                        switch((String)set.getKey()){
                            case "hours":
                                hours = (String) set.getValue();
                                break;
                            case "wait time":
                                waitTime = (long) set.getValue();
                                break;
                            case "menu":
                                menuURL = (String) set.getValue();
                                break;
                            case "background image":
                                backgroundImg = (String) set.getValue();
                                break;
                            case "slim background image":
                                slimBackgroundImg = (String) set.getValue();
                                break;
                            default:
                                break;
                        }
                        itTwo.remove();
                    }

                    restaurantArray.add(new Restaurant(name, (int)waitTime, hours, menuURL, backgroundImg, slimBackgroundImg));

                    it.remove();
                }

                System.out.println("-----------------------");
                System.out.println(dataSnapshot.child("Restaurants").getValue());
                System.out.println(restaurantArray.size());
                System.out.println("-----------------------");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    RestaurantSingleton() {
        getData();
    }

    public static RestaurantSingleton getSharedInstance(){
        if(sharedInstance == null){
            if(sharedInstance == null){
                return new RestaurantSingleton();
            }
        }
        return sharedInstance;
    }

    public ArrayList<Restaurant> getAllRestaurants(){
        return restaurantArray;
    }

    public void setAllRestaurants(){

//        restaurantArray.add(new Restaurant("ADC", 1, "7:00am - 8:00pm"));
//        restaurantArray.add(new Restaurant("Wandas", 3, "7:00am - 8:30pm"));
//        restaurantArray.add(new Restaurant("Briscos", 5, "7:00am - 11:00pm"));
//        restaurantArray.add(new Restaurant("Chick-fil-a", 13, "10:30am - 7:00pm"));
//        restaurantArray.add(new Restaurant("El Monte", 25, "10:30am - 7:00pm"));

        //set random graph pts for restaurant
        for(Restaurant r: restaurantArray){
            int count = 1;
            List<GraphPoint> pts = new ArrayList<>();
            Random rand= new Random();
            long epochTime = System.currentTimeMillis();
            for(int i = 0; i <10; i++){

                    pts.add(new GraphPoint(epochTime,i*(rand.nextInt(50)+1)*count));
                epochTime = System.currentTimeMillis() + (i*100000);
            }
            r.setAnalytics(pts);
            count++;
        }

    }
}
