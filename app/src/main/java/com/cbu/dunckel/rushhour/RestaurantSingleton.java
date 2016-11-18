package com.cbu.dunckel.rushhour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dunckel on 11/4/2016.
 */

public class RestaurantSingleton {

    // create a static variable with only one instance in memory
    static RestaurantSingleton sharedInstance;

    ArrayList<Restaurant> restaurantArray = new ArrayList<Restaurant>();

    RestaurantSingleton() {
        setAllRestaurants();
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

        restaurantArray.add(new Restaurant("ADC", 1, "7:00am - 8:00pm"));
        restaurantArray.add(new Restaurant("Wandas", 3, "7:00am - 8:30pm"));
        restaurantArray.add(new Restaurant("Briscos", 5, "7:00am - 11:00pm"));
        restaurantArray.add(new Restaurant("Chick-fil-a", 13, "10:30am - 7:00pm"));
        restaurantArray.add(new Restaurant("El Monte", 25, "10:30am - 7:00pm"));

        //set random graph pts for restaurant
        for(Restaurant r: restaurantArray){
            int count = 1;
            List<Point> pts = new ArrayList<>();
            Random rand= new Random();
            long epochTime = System.currentTimeMillis();
            for(int i = 0; i <2; i++){
                System.out.println((int) epochTime + " " + (float) epochTime + " " + epochTime );
                    pts.add(new Point(epochTime,i*(rand.nextInt(50)+1)*count));
                epochTime = System.currentTimeMillis() + (i*100000);
            }
            r.setAnalytics(pts);
            count++;
        }

    }
}
