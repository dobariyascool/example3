package com.arraybit.abposa;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalBarFragment extends Fragment {

    HorizontalBarChart horizontalChart;
    LinearLayout errorLayout;
    String chartName, titleBar;
    //XAxis====
    ArrayList<String> alItem = new ArrayList<>();
    //YAxis====
    ArrayList<BarEntry> alQty = new ArrayList<>();

    public HorizontalBarFragment() {
        // Required empty public constructor
    }


    public HorizontalBarFragment(ArrayList<BarEntry> alQty, ArrayList<String> alItem, String chartName, String titleBar) {
        this.alQty = alQty;
        this.alItem = alItem;
        this.chartName = chartName;
        this.titleBar = titleBar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horizontal_bar, container, false);
        try {
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

            horizontalChart = (HorizontalBarChart) view.findViewById(R.id.horizontalChart);
            errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

            if (alItem != null && alItem.size() != 0 && alQty != null && alQty.size() != 0) {
                errorLayout.setVisibility(View.GONE);
                horizontalChart.setVisibility(View.VISIBLE);
                SetHorizontalChart();
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                horizontalChart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_horizontal_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetHorizontalChart() {
        horizontalChart.setClickable(false);
        horizontalChart.setDrawBarShadow(false);

        horizontalChart.setDrawValueAboveBar(true);

        horizontalChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        horizontalChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        horizontalChart.setPinchZoom(false);

        horizontalChart.setDrawGridBackground(false);

        XAxis xl = horizontalChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = horizontalChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = horizontalChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setHorizontalChartData();
        horizontalChart.animateY(2500);

    }

    private void setHorizontalChartData() {

        for (int i = 0; i < alItem.size(); i++) {
            String temp = alItem.get(i);
            if (temp.length() > 14) {
                temp = temp.substring(0, 12) + "...";
            }
            alItem.set(i, temp);
        }
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        BarDataSet set1 = new BarDataSet(alQty, "Items");
//        set1.setBarSpacePercent(35f);
        set1.setColors(colors);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(alItem, dataSets);
//        data.setValueTextSize(10f);

        horizontalChart.setData(data);
    }


}
