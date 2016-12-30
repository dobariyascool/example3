package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposa.AnalysisFragment;
import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.AnalysisTypes;
import com.arraybit.parser.ChartDataJSONParser;
import com.arraybit.parser.DashboardJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class AnalysisSalesAdapter extends RecyclerView.Adapter<AnalysisSalesAdapter.AnalysisViewHolder> {

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

        if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.dailySales.getDesc())) {
            SetVisible(holder, objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.dailySales));
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.weeklySales.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.weeklySales));
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.monthlySales.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());
            holder.txtSalesDetail.setText(context.getResources().getString(R.string.dfRupee) + " " + String.valueOf(AnalysisFragment.monthlySales));
//        } else if (objAnalysisTypes.getDesc().equals("By Date Range Sales")) {
//            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.yearlySales.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.orderTypewise.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.categoryWise.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.leastSellingItems.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.mostSellingItems.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.leastSellingDay.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

//        } else if (objAnalysisTypes.getDesc().equals("Least Selling Hours")) {
//            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.lowStock.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.profitloss.getDesc())) {
            SetVisible(holder,  objAnalysisTypes.isGraph());

        }
    }

    @Override
    public int getItemCount() {
        return lstAnalysisTypes.size();
    }

    private void SetVisible(AnalysisViewHolder holder, boolean isGraphShow) {
        if (isGraphShow) {
            holder.ivVisible.setVisibility(View.GONE);
            holder.txtSalesDetail.setVisibility(View.VISIBLE);
        } else {
            holder.ivVisible.setVisibility(View.GONE);
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
        if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.dailySales.getDesc())) {
            Log.e("daily sales","clicked");
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.weeklySales.getDesc())) {
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.monthlySales.getDesc())) {
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
//        }  else if (objAnalysisTypes.getDesc().equals("By Date Range Sales")) {
//            objCardListener = (CardListener) targetFragment;
//            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.yearlySales.getDesc())) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderMasterYearlyRevenueChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment);
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.orderTypewise.getDesc())) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderMasterOrderTypeWiseChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment);
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.categoryWise.getDesc())) {

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.leastSellingItems.getDesc())) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderItemTranLeastOrderedItemsChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment, false);
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.mostSellingItems.getDesc())) {
            ChartDataJSONParser objChartDataJSONParser = new ChartDataJSONParser();
            objChartDataJSONParser.SelectAllOrderItemTranMostOrderedItemsChart(context, String.valueOf(Globals.linktoBusinessMasterId), targetFragment, true);
        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.leastSellingDay.getDesc())) {
            objCardListener = (CardListener) targetFragment;
            objCardListener.CardOnClick(objAnalysisTypes.getDesc());
//        } else if (objAnalysisTypes.getDesc().equals("Least Selling Hours")) {

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.lowStock.getDesc())) {

        } else if (objAnalysisTypes.getDesc().equals(Globals.SalesAnalysis.profitloss.getDesc())) {

        }
    }

    class AnalysisViewHolder extends RecyclerView.ViewHolder {

        TextView txtAdminSales, txtSalesDetail;
        ImageView ivVisible;
        LinearLayout card_analysis;

        public AnalysisViewHolder(View itemView) {
            super(itemView);

            txtAdminSales = (TextView) itemView.findViewById(R.id.txtAdminSales);
            txtSalesDetail = (TextView) itemView.findViewById(R.id.txtSalesDetail);
            ivVisible = (ImageView) itemView.findViewById(R.id.ivVisible);
            card_analysis = (LinearLayout) itemView.findViewById(R.id.card_analysis);

            ivVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardOnClick(getAdapterPosition());
                }
            });

            card_analysis.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            card_analysis.setBackgroundColor(ContextCompat.getColor(context,R.color.card_option_ripple_color));
                            break;
                        case MotionEvent.ACTION_UP:
                            //set color back to default
                            card_analysis.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
                            break;
                    }
                    return true;

                }
            });

            card_analysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardOnClick(getAdapterPosition());
                }
            });

            txtAdminSales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardOnClick(getAdapterPosition());
                }
            });

            txtSalesDetail.setOnClickListener(new View.OnClickListener() {
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
        objDashboardJSONParser.SelectOrderMasterDailyWeeklyMonthlyOrders(context, targetFragment, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(0));
    }
}
