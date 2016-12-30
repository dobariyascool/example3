package com.arraybit.abposa;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.AnalysisSalesAdapter;
import com.arraybit.global.Globals;
import com.arraybit.modal.AnalysisTypes;
import com.arraybit.modal.Dashboard;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.ChartDataJSONParser;
import com.arraybit.parser.DashboardJSONParser;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment implements ChartDataJSONParser.ChartDataListener, DashboardJSONParser.DashBoardListener, DashboardJSONParser.LastDayListener, AnalysisSalesAdapter.CardListener {
//

    public static double dailySales;
    public static double monthlySales;
    public static double weeklySales;
    public static OrderMaster orderMaster;
    RecyclerView rvSalesAnalysis;
    AnalysisSalesAdapter adapter;
    ArrayList<String> salesType = new ArrayList<>();
    ArrayList<Boolean> isGraphArrayList = new ArrayList<>();
    ActivityBack objActivityBack;
//    Context context;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        try {
            Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                app_bar.setTitle(R.string.salesAnalysis);
            }

            setHasOptionsMenu(true);

            DataRequest();

            rvSalesAnalysis = (RecyclerView) view.findViewById(R.id.rvSalesAnalysis);

            SetRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_activity_analysis))) {
                objActivityBack = (ActivityBack) getActivity();
                objActivityBack.BackClicked();
//                getActivity().setResult(Activity.RESULT_OK);
//                getActivity().finish();
//                getActivity().overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //order type wise pie chart
    @Override
    public void OrderTypeWise(ArrayList<OrderMaster> lstOrderMaster) {
        //pie chart
        ArrayList<Entry> alPercentage = new ArrayList<>();
        ArrayList<String> alStrings = new ArrayList<>();
        if (lstOrderMaster != null && lstOrderMaster.size() > 0) {
            for (OrderMaster objOrderMaster : lstOrderMaster) {
                alPercentage.add(new Entry((float) objOrderMaster.getTotalAmount(), 0));
                alStrings.add(objOrderMaster.getOrderType());
            }
        }
        Calendar now = Calendar.getInstance();
        String chartName = now.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "," + now.get(Calendar.YEAR);
        Globals.ReplaceFragmentFull(new PieChartFragment(alPercentage, alStrings, chartName, "Order Typewise"), getActivity().getSupportFragmentManager(), getActivity().getString(R.string.title_pie_chart));
    }

    @Override
    public void MostSellingItem(ArrayList<OrderItemTran> lstOrderItemTran, boolean isMost) {
        //XAxis====
        ArrayList<String> alItem = new ArrayList<>();
        //YAxis====
        ArrayList<BarEntry> alQty = new ArrayList<>();
        if (lstOrderItemTran != null && lstOrderItemTran.size() > 0) {
            for (int i = 0; i < lstOrderItemTran.size(); i++) {
                alItem.add(lstOrderItemTran.get(i).getItem());
                alQty.add(new BarEntry(lstOrderItemTran.get(i).getTotalItemQuantity(), i));
            }
        }
        if (isMost) {
            Globals.ReplaceFragmentFull(new HorizontalBarFragment(alQty, alItem, "Horizontal Bar Chart", "Most Selling Item"), getActivity().getSupportFragmentManager(), getActivity().getString(R.string.title_horizontal_chart));
        } else {
            Globals.ReplaceFragmentFull(new HorizontalBarFragment(alQty, alItem, "Horizontal Bar Chart", "Least Selling Item"), getActivity().getSupportFragmentManager(), getActivity().getString(R.string.title_horizontal_chart));
        }
    }

    @Override
    public void YearlySales(ArrayList<OrderMaster> lstOrderMaster) {
        ArrayList<Entry> yValsPrevious = new ArrayList<>();
        ArrayList<Entry> yValsCurrent = new ArrayList<>();
        ArrayList<String> xMonths = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        String currentyear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String previousyear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
        for (int i = 1; i <= Globals.Months.values().length; i++) {
            xMonths.add(Globals.Months.getMonth(i));
            xVals.add(Globals.Months.getShortMonth(i));
        }
        if (lstOrderMaster != null && lstOrderMaster.size() > 0) {

            for (int i = 0; i < xMonths.size(); i++) {
                for (int j = 0; j < lstOrderMaster.size(); j++) {

                    if (xMonths.get(i).equals(lstOrderMaster.get(j).getMonth()) && currentyear.equals(lstOrderMaster.get(j).getYear())) {
                        yValsCurrent.add(new Entry((float) lstOrderMaster.get(j).getNetAmount(), i));
                    } else if (xMonths.get(i).equals(lstOrderMaster.get(j).getMonth()) && previousyear.equals(lstOrderMaster.get(j).getYear())) {
                        yValsPrevious.add(new Entry((float) lstOrderMaster.get(j).getNetAmount(), i));
                    }
                }
            }
        }
        Log.e("yCurrent", " " + yValsCurrent.size());
        Log.e("yPrevious", " " + yValsPrevious.size());
        Globals.ReplaceFragmentFull(new LineChartFragment(xVals, yValsCurrent, yValsPrevious, "Line Chart", "Yearly Sales"), getActivity().getSupportFragmentManager(), getActivity().getString(R.string.title_line_chart));
    }

    @Override
    public void DashboardResponse(String errorCode, Dashboard objDashboard) {

    }

    @Override
    public void DashBoardSales(String dailySales, String weeklySales, String monthlySales) {
        this.dailySales = Double.valueOf(dailySales);
        this.weeklySales = Double.valueOf(weeklySales);
        this.monthlySales = Double.valueOf(monthlySales);
        adapter.DataReceived();
    }

    @Override
    public void LastDayOfWeek(OrderMaster orderMaster) {
        this.orderMaster = orderMaster;
    }

    @Override
    public void CardOnClick(String salesType) {
        Intent intent = new Intent(getActivity(), DailySalesActivity.class);
        intent.putExtra("SalesReportType", salesType);
        getActivity().startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    //region Private Methods and interfaces
    private void DataRequest() {
        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
//        if (Globals.isAdmin) {
        objDashboardJSONParser.SelectOrderMasterDailyWeeklyMonthlyOrders(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(0));
//        } else {
//            objDashboardJSONParser.SelectDashBoardCounter(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId));
//        }
    }

    private void SetRecyclerView() {
        ArrayList<AnalysisTypes> lstAnalysisTypes = new ArrayList<>();
        AnalysisTypes objAnalysisType;
        for (int i = 0; i < Globals.SalesAnalysis.values().length; i++) {
            objAnalysisType = new AnalysisTypes();
            objAnalysisType.setId(i);
            objAnalysisType.setDesc(Globals.SalesAnalysis.getValue(i));
            objAnalysisType.setGraph(Globals.SalesAnalysis.getGraphValue(i));

            lstAnalysisTypes.add(objAnalysisType);
        }
        rvSalesAnalysis.setVisibility(View.VISIBLE);
        adapter = new AnalysisSalesAdapter(getActivity(), lstAnalysisTypes, this);
        rvSalesAnalysis.setAdapter(adapter);
        rvSalesAnalysis.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public interface ActivityBack {
        void BackClicked();
    }
    //endregion

}
