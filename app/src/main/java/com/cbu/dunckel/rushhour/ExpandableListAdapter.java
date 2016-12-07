package com.cbu.dunckel.rushhour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final int NUM_CHILDVIEWS = 1;
    private Context _context;
    private List<Restaurant> _listDataRestaurant; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Restaurant>> _listDataChild;
    FragmentManager fm;

    public ExpandableListAdapter(Context context, List<Restaurant> listOfRestaurants) {
        this._context = context;
        this._listDataRestaurant = listOfRestaurants;
        System.out.println("ExpandableListAdapter constructor called");
//        this._listDataChild = listChildData;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataRestaurant.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Restaurant restaurantData = (Restaurant) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroup_item, null);
        }




        //Set menu button
        Button menuButton = (Button) convertView.findViewById(R.id.menuBtn);
        menuButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 System.out.println("clicked");
                 String url = "http://www.orimi.com/pdf-test.pdf";

//                 MenuImgDialogFragment dialogFragment = new MenuImgDialogFragment();
//                 //Pass Arguments
//                 Bundle args = new Bundle();
//                 args.putInt("num", num);
//                 dialogFragment.setArguments(args);
//                 // Show DialogFragment
//                 dialogFragment.show(fm, "Dialog Fragment");

                 String uniqueID = UUID.randomUUID().toString();
                 String[] fileName = url.split("/");
//            download(url, fileName[fileName.length-1]);
//                 download(url, uniqueID+".pdf");
                 System.out.println(restaurantData.getMenuURL());
                 download(restaurantData.getMenuURL(), uniqueID+".pdf");
//            view(fileName[fileName.length-1]);
                 view(uniqueID+".pdf");



//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _context.startActivity(browserIntent);

             }
           }
        );


        //Set hour label
        TextView hourLbl = (TextView) convertView
                .findViewById(R.id.lblHours);

        //hourLbl.setText(restaurantData.getHours());
        hourLbl.setText("Test");

        //To set dates we need to create date formatter that extends IAxisValueFormatter
        //https://github.com/PhilJay/MPAndroidChart/blob/2d18d0695b5d6d849b249e609f66192664e118e5/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java
        //https://github.com/PhilJay/MPAndroidChart/wiki/The-AxisValueFormatter-interface

        BarChart chart = (BarChart) convertView.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        for(GraphPoint entry : restaurantData.getAnalytics()){
            entries.add(new BarEntry(entry.getX(), entry.getY()));
        }


        IAxisValueFormatter xAxisFormatter = new DateAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        BarDataSet set = new BarDataSet(entries, restaurantData.getName()+"'s Wait Times");

        BarData data = new BarData(set);

        data.setBarWidth(100000); // set custom bar width

        chart.setData(data);
        chart.setFitBars(false); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return NUM_CHILDVIEWS;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataRestaurant.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataRestaurant.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Restaurant headerTitle = (Restaurant) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

//        if (isExpanded)
//            convertView.setPadding(0, 0, 0, 0);
//        else
//            convertView.setPadding(0, 0, 0, 200);



        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getName());

        TextView lblWaitTime = (TextView) convertView
                .findViewById(R.id.lblWaitTime);
        lblWaitTime.setText(Integer.toString(headerTitle.getWaitTime()));

        if(headerTitle.getWaitTime() < 6){
            lblWaitTime.setTextColor(Color.parseColor("#036814"));
        } else if(headerTitle.getWaitTime() > 6 && headerTitle.getWaitTime() < 16){
            lblWaitTime.setTextColor(Color.parseColor("#FF8000"));
        } else{
            lblWaitTime.setTextColor(Color.RED);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setFM(FragmentManager fm){
        this.fm = fm;
    }

    public void download(String url, String fileName)
    {
        new DownloadFile().execute(url, fileName);
    }

    public void view(String url)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/RushHourPdfs/" + url);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        System.out.println("Uri Path: "+ path);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            _context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(_context.getApplicationContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}