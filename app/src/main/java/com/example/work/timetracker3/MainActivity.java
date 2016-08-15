package com.example.work.timetracker3;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import 	android.os.SystemClock;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button goingToBed;
    Button working;
    Button relaxing;
    Button eating;
    Button napping;
    Button other;
    long workingTime = 0;
    long relaxingTime = 0;
    long eatingTime = 0;
    long nappingTime = 0;
    long otherTime = 0;
    String category = "";

    //to go to the next window after going to bed
    ViewFlipper viewFlipper;
    RelativeLayout relativeLayout;
    LinearLayout switchLayout;
    Display display;
    long startTime;

    //for the pie chart. we will add data into this later
    private PieChart mChart;
    ArrayList<Entry> yValues = new ArrayList<Entry>();
    ArrayList<String> xValues = new ArrayList<String>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goingToBed = (Button) findViewById(R.id.goingToBed);
        working = (Button) findViewById(R.id.working);
        relaxing = (Button) findViewById(R.id.relaxing);
        eating = (Button) findViewById(R.id.eating);
        napping = (Button) findViewById(R.id.napping);
        other = (Button) findViewById(R.id.other);
        goingToBed.setOnClickListener(this);
        working.setOnClickListener(this);
        relaxing.setOnClickListener(this);
        eating.setOnClickListener(this);
        napping.setOnClickListener(this);
        other.setOnClickListener(this);


        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        switchLayout = (LinearLayout) findViewById(R.id.switchLayout);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        display = getWindowManager().getDefaultDisplay();

        //we need to get the time of the preference
        long time = getTime();
        String isCategory=getCategory();

        //if the preference is not set, make the start time (and the prefernce, the elpased time
        if(time==0){
            startTime= SystemClock.elapsedRealtime();
            saveTime(startTime);
        }
        //if the preference is already set set the start time to that preference
        else{
            startTime = time;
        }

        if(isCategory!=""){
            category=isCategory;
        }

        //make the pie chart
        //mChart = (PieChart)findViewById(R.id.mChart);
        mChart = new PieChart(this);

        mChart.setUsePercentValues(true);
        mChart.setDescription("Shows the percentage of time doing certain activities");

        //this enables hole and configuration
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        //enable rotation of chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        //add data

    }
    //save the time
    public void saveTime(long time){

        //we want to save the startTime in case the app closes or crashes
        SharedPreferences sharedPref = getSharedPreferences("time", Context.MODE_PRIVATE);
        //need to make an editor for the preferences
        SharedPreferences.Editor editor = sharedPref.edit();
        //putting a key and value for the preferences
        editor.putLong("time",time);
        //apply
        editor.apply();

    }

    //return the time that app was started
    public long getTime(){

        SharedPreferences sharedPref = getSharedPreferences("time", Context.MODE_PRIVATE);

        long time = sharedPref.getLong("time", 0);

        return time;

    }

    //save our current cateogry
    public void saveCategory(String incomingCategory){

        //we want to save the startTime in case the app closes or crashes
        SharedPreferences sharedPref = getSharedPreferences("category", Context.MODE_PRIVATE);
        //need to make an editor for the preferences
        SharedPreferences.Editor editor = sharedPref.edit();
        //putting a key and value for the preferences
        editor.putString("category", incomingCategory);
        //apply
        editor.apply();

    }

    //give back the category we have saved
    public String getCategory(){

        SharedPreferences sharedPref = getSharedPreferences("category", Context.MODE_PRIVATE);

        String savedCategory = sharedPref.getString("category", "");

        return savedCategory;

    }



    private void addData(){


        //create the chart
        PieDataSet dataSet  =new PieDataSet(yValues,"Shares");
        dataSet.setSliceSpace(3); //space between the slices
        dataSet.setSelectionShift(5); //sets the distance the slice is shifted away from the center

        //add colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //instantiate pie chart
        PieData data = new PieData(xValues,dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        //undo all highlists for some reason
        mChart.highlightValues(null);



        //update the pie chart
        mChart.invalidate();

    }




    private void goingToBed() {
        //assing the last time
        assignTime("");

        //reset the time and category for the next time the app opens
        saveTime(0);
        saveCategory("");
        int counter = 0;

        TextView tv1 = (TextView) findViewById(R.id.textView);
        viewFlipper.showNext();

        double totalTime = (double) (otherTime + nappingTime + relaxingTime + workingTime + eatingTime);
        tv1.setText(Double.toString(totalTime));
        float calculatedTime;

        TextView viewer;
        //setContentView(R.layout.layout);
        //set the text and the pie chart, and if the time isnt equal to 0 add it to the chart
        viewer = (TextView) findViewById(R.id.eating2);
        calculatedTime = (float) (eatingTime / totalTime * 100);
        viewer.setText("Eating: " + Float.toString(calculatedTime));
        if(calculatedTime != 0) {
            xValues.add("Eating");
            yValues.add(new Entry( (calculatedTime), counter++));
        }
        viewer = (TextView) findViewById(R.id.napping2);
        calculatedTime = (float) (nappingTime / totalTime * 100);
        viewer.setText("Napping: " + Float.toString( calculatedTime));
        if(calculatedTime != 0) {
            xValues.add("Napping");
            yValues.add(new Entry((calculatedTime), counter++));
        }

        viewer = (TextView) findViewById(R.id.other2);
        calculatedTime = (float)(otherTime / totalTime * 100);
        viewer.setText("Other: " + Float.toString( calculatedTime));
        if(calculatedTime!=0) {
            xValues.add("Other");
            yValues.add(new Entry( (calculatedTime), counter++));
        }

        viewer = (TextView) findViewById(R.id.relaxing2);
        calculatedTime = (float)(relaxingTime / totalTime * 100);
        viewer.setText("Relaxing: " + Float.toString( calculatedTime));
        if(calculatedTime!=0) {
            xValues.add("Relaxing");
            yValues.add(new Entry(calculatedTime, counter++));
        }

        viewer = (TextView) findViewById(R.id.working2);
        calculatedTime = (float)(workingTime / totalTime * 100);
        viewer.setText("Working: " + Float.toString( calculatedTime));
        if(calculatedTime!=0) {
            xValues.add("Working");
            yValues.add(new Entry(calculatedTime, counter++));
        }

        viewer = (TextView) findViewById(R.id.total);
        viewer.setText("Total Time: " + Double.toString(otherTime + nappingTime + relaxingTime + workingTime + eatingTime));

        //add chart data
        addData();

        //customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        //add the chart to the layout
        switchLayout.addView(mChart);
        ViewGroup.LayoutParams params = mChart.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eating:
                assignTime("eating");
                break;
            case R.id.napping:
                assignTime("napping");
                break;
            case R.id.other:
                assignTime("other");
                break;
            case R.id.relaxing:
                assignTime("relaxing");
                break;
            case R.id.working:
                assignTime("working");
                break;
            case R.id.goingToBed:
                goingToBed();
                break;


        }

    }

    public void assignTime(String nextCat){



        switch(category){
            case "napping":
                nappingTime+=((SystemClock.elapsedRealtime() - startTime) / 1000);
                break;
            case "eating":
                eatingTime+=((SystemClock.elapsedRealtime() - startTime) / 1000);
                break;
            case "other":
                otherTime+=((SystemClock.elapsedRealtime() - startTime) / 1000);
                break;
            case "relaxing":
                relaxingTime+=((SystemClock.elapsedRealtime() - startTime) / 1000);
                break;
            case "working":
                workingTime+=((SystemClock.elapsedRealtime() - startTime) / 1000);
                break;
            default:
                break;
        }

        //set the category
        category=nextCat;
        saveCategory(nextCat);

        //set the time
        startTime = SystemClock.elapsedRealtime();
        saveTime(startTime);

    }




}