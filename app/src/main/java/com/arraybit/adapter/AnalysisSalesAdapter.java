package com.arraybit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.abposa.AnalysisActivity;
import com.arraybit.abposa.AnalysisFragment;
import com.arraybit.abposa.DailySalesActivity;
import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.AnalysisTypes;
import com.arraybit.modal.Dashboard;
import com.arraybit.parser.ChartDataJSONParser;
import com.arraybit.parser.DashboardJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class AnalysisSalesAdapter extends RecyclerView.Adapter<AnalysisSalesAdapter.AnalysisViewHolder> {

    public static boolean isHole = false;
    ArrayList<AnalysisTypes> lstAnalysisTypes= new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    View view;
    AnalysisTypes objAnalysisTypes = null;
    Fragment targetFragment;
    CardListener objCardListener;

    public AnalysisSalesAdapter(Context context, ArrayList<AnalysisTypes> lstAnalysisTypes,Fragment targetFragment) {
        this.context = context;
        this.lstAnalysisTypes= lstAnalysisTypes;
        this.layoutInflater = LayoutInflater.from(context);
        this.targetFragment = targetFragment;
    }

    @Override
    public AnalysisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_analysis_graphs, parent, false);
        return new AnalysisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnalysisViewHolder holder, int position) {

        objAnalysisTypes = lstAnalysisTypes.get(position);
        holder.txtAdminSales.setText(objAnalysisTypes.getDesc());

        if (objAnalysisTypes.getDesc().equals("Daily Sales")) {
            SetVisible(holder, objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.dailySales));
        } else if (objAnalysisTypes.getDesc().equals("Weekly Sales")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.weeklySales));
        } else if (objAnalysisTypes.getDesc().equals("Monthly Sales")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.monthlySales));
        } else if (objAnalysisTypes.getDesc().equals("By Date Range Sales")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Yearly Sales")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Order Type Wise")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Category Wise")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Least Selling Items")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Most Selling Items")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Least Selling Day")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Least Selling Hours")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Low Stock")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals("Profit/Loss")) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        }
    }

    @Override
    public int getItemCount() {
        return lstAnalysisTypes.size();
    }

    private void SetVisible(AnalysisViewHolder holder, boolean isGraphShow) {
        if (isGraphShow) {
            holder.ivVisible.setVisibility(View.VISIBLE);
            holder.txtSalesDetail.setVisibility(View.VISIBLE);
        } else {
            holder.ivVisible.setVisibility(View.VISIBLE);
            holder.txtSalesDetail.setVisibility(View.VISIBLE);
//            holder.card_analysis.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_white));
            holder.card_analysis.setClickable(false);
        }
    }

    public void DataReceived()
    {
        notifyDataSetChanged();
    }

    private void CardOnClick(int position) {
        objAnalysisTypes = lstAnalysisTypes.get(position);
        if (objAnalysisTypes.getDesc().equals("Daily Sales")) {
            Log.e("daily sales","clicked");
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        } else if (objAnalysisTypes.getDesc().equals("Weekly Sales")) {

        } else if (objAnalysisTypes.getDesc().equals("Monthly Sales")) {
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        }  else if (objAnalysisTypes.getDesc().equals("By Date Range Sales")) {
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        } else if (objAnalysisTypes.getDesc().equals("Yearly Sales")) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderMasterYearlyRevenueChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment);
        } else if (objAnalysisTypes.getDesc().equals("Order Type Wise")) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderMasterOrderTypeWiseChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment);
        } else if (objAnalysisTypes.getDesc().equals("Category Wise")) {

        } else if (objAnalysisTypes.getDesc().equals("Least Selling Items")) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderItemTranLeastOrderedItemsChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment, false);
        } else if (objAnalysisTypes.getDesc().equals("Most Selling Items")) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderItemTranMostOrderedItemsChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment, true);
        } else if (objAnalysisTypes.getDesc().equals("Least Selling Day")) {

        } else if (objAnalysisTypes.getDesc().equals("Least Selling Hours")) {

        } else if (objAnalysisTypes.getDesc().equals("Low Stock")) {

        } else if (objAnalysisTypes.getDesc().equals("Profit/Loss")) {

        }
    }

//    public interface AnalysisListener {
//        void CardViewPieChart();
//
//        void CardViewBarChart();
//    }

    class AnalysisViewHolder extends RecyclerView.ViewHolder {

        TextView txtAdminSales, txtSalesDetail;
        ImageView ivVisible;
        CardView card_analysis;

        public AnalysisViewHolder(View itemView) {
            super(itemView);

            txtAdminSales = (TextView) itemView.findViewById(R.id.txtAdminSales);
            txtSalesDetail = (TextView) itemView.findViewById(R.id.txtSalesDetail);
            ivVisible = (ImageView) itemView.findViewById(R.id.ivVisible);
            card_analysis = (CardView) itemView.findViewById(R.id.card_analysis);

//            ivVisible.setVisibility(View.GONE);

            ivVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardOnClick(getAdapterPosition());
                }
            });

            card_analysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardOnClick(getAdapterPosition());
                }
            });

        }
    }

    public interface CardListener
    {
        void CardOnClick(String salesType);
    }

    private void DataRequest() {
        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
//        if (Globals.isAdmin) {
        objDashboardJSONParser.SelectOrderMasterDailyWeeklyMonthlyOrders(context, targetFragment, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(0));
//        } else {
//            objDashboardJSONParser.SelectDashBoardCounter(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId));
//        }
    }
}
