package com.arraybit.abposa;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {

    ArrayList<Entry> alPercentage = new ArrayList<>();
    ArrayList<String> alStrings = new ArrayList<>();
    PieChart pieChart;
    LinearLayout errorLayout;
    String chartName,titleBar;

    public PieChartFragment() {
        // Required empty public constructor
    }

    public PieChartFragment(ArrayList<Entry> alPercentage, ArrayList<String> alStrings,String chartName,String titleBar) {
        this.alPercentage=alPercentage;
        this.alStrings=alStrings;
        this.chartName=chartName;
        this.titleBar=titleBar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(titleBar);
        }

        setHasOptionsMenu(true);

        pieChart= (PieChart) view.findViewById(R.id.pieChart);
        errorLayout= (LinearLayout) view.findViewById(R.id.errorLayout);

        if(alStrings!=null && alStrings.size()!=0 && alPercentage!=null && alPercentage.size()!=0 )
        {
            errorLayout.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            SetPieChart();
        }
        else
        {
            errorLayout.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_pie_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetPieChart()
    {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(50);

        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        setPieChartData(100);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setPieChartData( float range) {

//        float mult = range;

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        PieDataSet dataSet = new PieDataSet(alPercentage, chartName);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(alStrings, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }


}
