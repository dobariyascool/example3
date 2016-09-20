package com.arraybit.abposa;


import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends Fragment {

    LineChart lineChart;
    LinearLayout errorLayout;
    String chartName, titleBar;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yValsCurrent = new ArrayList<>();
    ArrayList<Entry> yValsPrevious = new ArrayList<>();


    public LineChartFragment() {
        // Required empty public constructor
    }

    public LineChartFragment(ArrayList<String> xVals, ArrayList<Entry> yValsCurrent, ArrayList<Entry> yValsPrevious, String chartName, String titleBar) {
        this.chartName = chartName;
        this.titleBar = titleBar;
        this.xVals = xVals;
        this.yValsCurrent = yValsCurrent;
        this.yValsPrevious = yValsPrevious;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

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

        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        errorLayout= (LinearLayout) view.findViewById(R.id.errorLayout);

        if(xVals!=null && xVals.size()!=0 && yValsCurrent!=null && yValsCurrent.size()!=0 )
        {
            errorLayout.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            SetLineChart();
        }
        else
        {
            errorLayout.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_line_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetLineChart()
    {
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(true);
// no description text
        lineChart.setDescription("");
// if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        Legend l = lineChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.LINE);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(315);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(0);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChart.getAxisRight().setEnabled(false);
// add data
        setData(60);
        lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

//        lineChart.invalidate();
    }

    private void setData(float range) {

        LineDataSet set1, set2;

        set1 = new LineDataSet(yValsCurrent, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.GREEN);
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.BLACK);
        set1.setCircleRadius(3f);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.WHITE);
        set1.setHighLightColor(Color.GREEN);
        set1.setDrawCircleHole(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets

// create a dataset and give it a type
        if (yValsPrevious != null && yValsPrevious.size() != 0) {
            set2 = new LineDataSet(yValsPrevious, String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1));
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.CYAN);
            set2.setLineWidth(2f);
            set2.setCircleColor(Color.BLUE);
            set2.setCircleRadius(3f);
            set2.setDrawFilled(true);
            set2.setFillColor(Color.WHITE);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.CYAN);

        dataSets.add(set2);

    }
// create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

// set data
        lineChart.setData(data);

    }


}
