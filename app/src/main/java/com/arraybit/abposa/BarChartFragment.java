package com.arraybit.abposa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {

  
    BarChart barChart;
    LinearLayout errorLayout;
    String chartName,titleBar;
    public BarChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bar_chart, container, false);

        barChart =(BarChart) view.findViewById(R.id.barChart);
        errorLayout= (LinearLayout) view.findViewById(R.id.errorLayout);

            errorLayout.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);

        return view;
    }

}
