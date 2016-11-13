package com.cbu.dunckel.rushhour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import static com.cbu.dunckel.rushhour.R.id.lblListHeader;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final int NUM_CHILDVIEWS = 1;
    private Context _context;
    private List<Restaurant> _listDataRestaurant; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Restaurant>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Restaurant> listOfRestaurants) {
        this._context = context;
        this._listDataRestaurant = listOfRestaurants;
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

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblHours);

        txtListChild.setText(restaurantData.getHours());



        BarChart chart = (BarChart) convertView.findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 30f));
        entries.add(new BarEntry(1*restaurantData.getWaitTime(), 80f));
        entries.add(new BarEntry(2, 60f));
        entries.add(new BarEntry(3, 50f));
        entries.add(new BarEntry(4, 70f));
        entries.add(new BarEntry(5, 60f));

        BarDataSet set = new BarDataSet(entries, restaurantData.getName()+"'s Wait Times");

        BarData data = new BarData(set);

        data.setBarWidth(0.5f); // set custom bar width

        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
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
}