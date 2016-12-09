package com.cbu.dunckel.rushhour;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RestaurantListActivity extends AppCompatActivity {

    public static List<Restaurant> listDataRestaurant = new ArrayList<Restaurant>();
    FragmentManager fm = getSupportFragmentManager();

    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mAdapter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private int previousPos = -1;
    private List<CustomLayout> screenLayoutList = new ArrayList<>();

    static int currentPosition;

    Intent serviceIntent;

    LinearLayoutManager mLayoutManager;

    boolean alreadyExecuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceIntent = new Intent(getApplicationContext(), LocationService.class);

        setContentView((R.layout.restaurant_list));
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRestaurantRecyclerView = (RecyclerView) findViewById(R.id.crime_recycler_view);
        mRestaurantRecyclerView.setLayoutManager(mLayoutManager);

        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomLayout screenLayout;
        private TextView lblListHeader;
        private TextView lblWaitTime;
        private TextView minLbl;
        private Restaurant mRestaurant;
        private boolean firstClick = true;
        private int viewHeight;


        public RestaurantHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            lblListHeader = (TextView)
                    itemView.findViewById(R.id.lblListHeader);
            lblWaitTime = (TextView)
                    itemView.findViewById(R.id.lblWaitTime);
            minLbl = (TextView)
                    itemView.findViewById(R.id.minLbl);
            screenLayout = (CustomLayout)
                    itemView.findViewById(R.id.screenLayout);
            screenLayoutList.add(screenLayout);


        }

        public void bindRestaurant(Restaurant restaurant) {
            mRestaurant = restaurant;
            uploadScreenLayout(mRestaurant.getSlimBackgroundImg(), listDataRestaurant.indexOf(restaurant));
            lblListHeader.setText(mRestaurant.getName());
            lblWaitTime.setText(Integer.toString(mRestaurant.getWaitTime()));
            //Set colors for wait time label
            if (mRestaurant.getWaitTime() <= 5) {
                lblWaitTime.setTextColor(Color.parseColor("#49C4A4"));
            } else if (mRestaurant.getWaitTime() > 5 && mRestaurant.getWaitTime() <= 10) {
                lblWaitTime.setTextColor(Color.parseColor("#E5AA15"));
            } else if (mRestaurant.getWaitTime() > 10 && mRestaurant.getWaitTime() <= 15) {
                lblWaitTime.setTextColor(Color.parseColor("#D36011"));
            } else {
                lblWaitTime.setTextColor(Color.parseColor("#CC1A2B"));
            }
        }

        @Override
        public void onClick(View v) {
            if (firstClick) {
                viewHeight = v.getHeight();
                firstClick = false;
            }

            RelativeLayout mainLayout;
            TextView lblHours;
            ImageButton menuBtn;
            TextView prevWaitLbl;
            TextView prevMinLbl;
            ImageView arrow;
            Drawable upArrow = getResources().getDrawable(R.drawable.up_arrow);
            Drawable downArrow = getResources().getDrawable(R.drawable.down_arrow);

            int animDownTime = 190;
            int animUpTime = 190;
            int initialViewHeight = viewHeight;

            //Get dimension of screen
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;

            int expandedViewHeight = viewHeight + (screenHeight * 3 / 8);

            //Modify item height and add widgets of item, hide widgets of all other items
            currentPosition = mRestaurantRecyclerView.getChildAdapterPosition(v);

            if (previousPos != currentPosition) {

                // First - collapse previous position (set height to initial height)
                if (previousPos != -1) {
                    Animation ani = new ShowAnim(mLayoutManager.findViewByPosition(previousPos), initialViewHeight, expandedViewHeight);
                    ani.setDuration(animUpTime);
                    mLayoutManager.findViewByPosition(previousPos).startAnimation(ani);
                    mainLayout = (RelativeLayout)
                            mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.content);
                    mainLayout.setVisibility(View.GONE);
                    lblHours = (TextView)
                            mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.lblHours);
                    lblHours.setVisibility(View.GONE);
                    menuBtn = (ImageButton)
                            mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.menuBtn);
                    menuBtn.setVisibility(View.GONE);

                    prevWaitLbl = (TextView) mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.lblWaitTime);
                    prevWaitLbl.setTextSize(24);
                    prevMinLbl = (TextView) mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.minLbl);
                    prevMinLbl.setTextSize(24);

                    //Set arrow to down
                    arrow = (ImageView) mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.arrowView);
                    arrow.setImageDrawable(downArrow);

                    // Change background image to slim
                    uploadScreenLayout(listDataRestaurant.get(previousPos).getSlimBackgroundImg(), previousPos);
                }

                // Second - expand current position (set height to 1/3 of screen height)
                Animation anim = new ShowAnim(v, expandedViewHeight, initialViewHeight);
                anim.setDuration(animDownTime);
                v.startAnimation(anim);

                mainLayout = (RelativeLayout) v.findViewById(R.id.content);
                mainLayout.setVisibility(View.VISIBLE);
                formChart(v);
                formMenuBtn(v, screenHeight, screenWidth);

                menuBtn = (ImageButton)
                        v.findViewById(R.id.menuBtn);
                menuBtn.setVisibility(View.VISIBLE);

                //Set hour label
                lblHours = (TextView)
                        v.findViewById(R.id.lblHours);
                lblHours.setVisibility(View.VISIBLE);
                lblHours.setText(mRestaurant.getHours());

                lblWaitTime.setTextSize(30);
                minLbl.setTextSize(30);

                // Change background image to large
                uploadScreenLayout(mRestaurant.getBackgroundImg(), currentPosition);

                //Set arrow to down
                arrow = (ImageView) v.findViewById(R.id.arrowView);
                arrow.setImageDrawable(upArrow);

                previousPos = currentPosition;
            } else if (previousPos == currentPosition) {

                // Collapse current position (set height to initial height)
                Animation ani = new ShowAnim(mLayoutManager.findViewByPosition(currentPosition), initialViewHeight, expandedViewHeight);
                ani.setDuration(animUpTime);
                mLayoutManager.findViewByPosition(currentPosition).startAnimation(ani);
                mainLayout = (RelativeLayout)
                        mLayoutManager.findViewByPosition(currentPosition).findViewById(R.id.content);
                mainLayout.setVisibility(View.GONE);
                lblHours = (TextView)
                        mLayoutManager.findViewByPosition(currentPosition).findViewById(R.id.lblHours);
                lblHours.setVisibility(View.GONE);
                menuBtn = (ImageButton)
                        mLayoutManager.findViewByPosition(currentPosition).findViewById(R.id.menuBtn);
                menuBtn.setVisibility(View.GONE);

                lblWaitTime.setTextSize(24);
                minLbl.setTextSize(24);
                // Change background image to slim
                uploadScreenLayout(mRestaurant.getSlimBackgroundImg(), currentPosition);

                //Set arrow to down
                arrow = (ImageView) mLayoutManager.findViewByPosition(previousPos).findViewById(R.id.arrowView);
                arrow.setImageDrawable(downArrow);

                previousPos = -1;
            }

        }

        //uploadScreenLayout loads the url image into the designated list view
        //url is the link to the image
        //pos is the list view item position
        private void uploadScreenLayout(String url, final int pos) {
            Picasso.with(getApplicationContext()).load(url).into(screenLayoutList.get(pos));
        }

        private void formChart(View v) {
            //To set dates we need to create date formatter that extends IAxisValueFormatter
            //https://github.com/PhilJay/MPAndroidChart/blob/2d18d0695b5d6d849b249e609f66192664e118e5/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java
            //https://github.com/PhilJay/MPAndroidChart/wiki/The-AxisValueFormatter-interface

            BarChart chart = (BarChart) v.findViewById(R.id.chart);
            List<BarEntry> entries = new ArrayList<>();
//            for (GraphPoint entry : mRestaurant.getAnalytics()) {
//                BarEntry barEntry = new BarEntry(entry.getX(), entry.getY());
//                entries.add(barEntry);
//            }
//            long epochTime = System.currentTimeMillis();
            int epochTime = 1;
            entries.add(new BarEntry(epochTime, 30f));
            entries.add(new BarEntry(epochTime +1, 80f));
            entries.add(new BarEntry(epochTime +2, 60f));
            entries.add(new BarEntry(epochTime +3, 50f));
            entries.add(new BarEntry(epochTime +4, 70f));
            entries.add(new BarEntry(epochTime +5, 60f));
            entries.add(new BarEntry(epochTime +6, 20f));

            IAxisValueFormatter xAxisFormatter = new DateAxisValueFormatter(chart);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            BarDataSet set = new BarDataSet(entries, mRestaurant.getName() + "'s Wait Times");

            int[] colors = new int[entries.size()];

            for(int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getY() < 30) {
                    colors[i] = R.color.lightGreen;
                } else if (entries.get(i).getY() < 70) {
                    colors[i] = R.color.lightOrange;
                } else {
                    colors[i] = R.color.darkOrange;
                }
            }

            set.setColors(colors, getApplicationContext());


            BarData data = new BarData(set);

            data.setBarWidth(.85f); // set custom bar width

            chart.setData(data);
            chart.setFitBars(false); // make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
        }

        private void formMenuBtn(View v, final int screenHeight, final int screenWidth) {
            //Set menu button
            ImageButton menuButton = (ImageButton) v.findViewById(R.id.menuBtn);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mRestaurant.getMenuURL();

                    //Upload image from url and display it in menuImgDialogFragment
                    MenuImgDialogFragment dialogFragment = new MenuImgDialogFragment();
                    //Pass Arguments
                    Bundle args = new Bundle();
                    args.putString("url", url); //url containing image information
                    args.putInt("screenHeight", screenHeight);
                    args.putInt("screenWidth", screenWidth);
                    dialogFragment.setArguments(args);
                    // Show DialogFragment
                    dialogFragment.show(fm, "Dialog Fragment");

                    //Download pdf given url and view it through new activity
//                    String uniqueID = UUID.randomUUID().toString();
//                    System.out.println(url);
//                    download(url, uniqueID+".pdf");
//                    view(uniqueID + ".pdf");

                }
            });
        }

        public void download(String url, String fileName) {
            new DownloadFile().execute(url, fileName);
        }

        public void view(String url) {
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/RushHourPdfs/" + url);  // -> filename = maven.pdf
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }

        private class DownloadFile extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                String fileUrl = strings[0];   // -> pdf url
                String fileName = strings[1];  // -> pdf name
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "RushHourPdfs");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileDownloader.downloadFile(fileUrl, pdfFile);
                return null;
            }
        }
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {
        private List<Restaurant> mRestaurants;

        public RestaurantAdapter(List<Restaurant> restaurants) {
            mRestaurants = restaurants;
        }

        @Override
        public RestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater
                    .inflate(R.layout.list_group, parent, false);
//            mViews.add(view);
            return new RestaurantHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantHolder holder, int position) {
            Restaurant restaurant = mRestaurants.get(position);
            holder.bindRestaurant(restaurant);
        }

        @Override
        public int getItemCount() {
            return mRestaurants.size();
        }

        public void setRestaurants(List<Restaurant> restaurants) {
            mRestaurants = restaurants;
        }
    }

    public void updateUI() {

        mRootRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!listDataRestaurant.isEmpty()) {
                    listDataRestaurant.removeAll(listDataRestaurant);
                }

                HashMap data = (HashMap) dataSnapshot.child("Restaurants").getValue();

                Iterator it = data.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();

                    String name = (String) pair.getKey();
                    String hours = "Not Available";
                    String menuURL = "";
                    String backgroundImage = null;
                    String slimBackgroundImg = null;
                    double latitude = 0;
                    double longitude = 0;
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
                            case "menu":
                                menuURL = (String) set.getValue();
                                break;
                            case "background image":
                                backgroundImage = (String) set.getValue();
                                break;
                            case "slim background image":
                                slimBackgroundImg = (String) set.getValue();
                                break;
                            case "latitude":
                                latitude = (Double) set.getValue();
                                break;
                            case "longitude":
                                longitude = (Double) set.getValue();
                                break;
                            default:
                                break;
                        }
                        itTwo.remove();
                    }

                    //Set Restaurant Location
                    Location loc = new Location("");
                    loc.setLatitude(latitude);
                    loc.setLongitude(longitude);

                    listDataRestaurant.add(new Restaurant(name, (int) waitTime, hours, menuURL, backgroundImage, slimBackgroundImg, loc));
                    it.remove();
                }

                //Sort restaurant list by wait time (smallest to greatest wait time)
                if (!listDataRestaurant.isEmpty()) {
                    Collections.sort(listDataRestaurant);
                }

                System.out.println("-----------------------");
                System.out.println(dataSnapshot.child("Restaurants").getValue());
                System.out.println(listDataRestaurant.size());
                System.out.println("-----------------------");

                //set random graph pts for restaurant
                for (Restaurant r : listDataRestaurant) {
                    int count = 1;
                    List<GraphPoint> pts = new ArrayList<>();
                    Random rand = new Random();
                    long epochTime = System.currentTimeMillis();
                    for (int i = 0; i < 10; i++) {
                        pts.add(new GraphPoint(epochTime, i * (rand.nextInt(50) + 1) * count));
                    }
                    r.setAnalytics(pts);
                    count++;
                }

                if (mAdapter == null) {
                    mAdapter = new RestaurantAdapter(listDataRestaurant);
                    mRestaurantRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.setRestaurants(listDataRestaurant);
                    mAdapter.notifyDataSetChanged();
                }

                if(!alreadyExecuted) {
                    alreadyExecuted = true;
                    //Start service after updateUI to retrieve restaurant data
                    ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
                    for (Restaurant restaurant : listDataRestaurant) {
                        restaurantList.add(restaurant);
                    }
                    serviceIntent.putParcelableArrayListExtra("restaurantList", restaurantList);
                    startService(serviceIntent);
                }

                //Decay timer
                //On data change start timer to wait five minutes,
                //if after five minutes data does not change
                //subtract wait time of restaurant by one minute

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
