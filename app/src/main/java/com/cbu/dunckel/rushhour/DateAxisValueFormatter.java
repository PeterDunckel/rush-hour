package com.cbu.dunckel.rushhour;

/**
 * Created by Dunckel on 11/17/2016.
 */

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAxisValueFormatter implements IAxisValueFormatter
{

    private BarLineChartBase<?> chart;

    public DateAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        System.out.println(value);
        long epochTime = (long) value;
//        System.out.println(epochTime);

        //SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        //System.out.println("Time:" + sdf.format(new Date(epochTime)));

        return sdf.format(new Date(epochTime));
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }

}