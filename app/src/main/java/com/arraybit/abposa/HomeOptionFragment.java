package com.arraybit.abposa;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.Dashboard;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.DashboardJSONParser;
import com.rey.material.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeOptionFragment extends Fragment implements DashboardJSONParser.DashBoardListener, View.OnClickListener, DashboardJSONParser.LastDayListener {

    LinearLayout userModeLayout, adminModeLayout;
    TextView dailySales, monthlySales, weeklySales, leastDaySale;
    TextView newOrder, newBooking, cancelOrder, cancelBooking;
    CardView cvUserModeOffer, cvUserModeReedem, cvUserCancelledOrder, cvUserNewOrder, cvUserNewBooking, cvUserCancelledBooking;
    CardView cvAdminDailySales, cvAdminMonthlySales;

    LinearLayout llEventFragment;

    public HomeOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home_option, container, false);
//        Bundle bundle = this.getArguments();
//        String mode = bundle.getString("HomeMode", "User");
        try {
            userModeLayout = (LinearLayout) rootView.findViewById(R.id.userModeLayout);
            adminModeLayout = (LinearLayout) rootView.findViewById(R.id.adminModeLayout);
            llEventFragment = (LinearLayout) rootView.findViewById(R.id.llEventFragment);

            newOrder = (TextView) rootView.findViewById(R.id.txtUserNewOrderCount);
            cancelOrder = (TextView) rootView.findViewById(R.id.txtUserCancelledOrderCount);
            newBooking = (TextView) rootView.findViewById(R.id.txtUserNewBookingCounts);
            cancelBooking = (TextView) rootView.findViewById(R.id.txtUserCancelledBookingCounts);

            dailySales = (TextView) rootView.findViewById(R.id.txtAdminDailySalesAmount);
            monthlySales = (TextView) rootView.findViewById(R.id.txtAdminMonthlySalesAmount);
            weeklySales = (TextView) rootView.findViewById(R.id.txtAdminWeeklySalesAmount);
            leastDaySale = (TextView) rootView.findViewById(R.id.txtAdminLeastDayAmount);

            cvUserNewOrder = (CardView) rootView.findViewById(R.id.cvUserNewOrder);
            cvUserCancelledOrder = (CardView) rootView.findViewById(R.id.cvUserCancelledOrder);
            cvUserNewBooking = (CardView) rootView.findViewById(R.id.cvUserNewBooking);
            cvUserCancelledBooking = (CardView) rootView.findViewById(R.id.cvUserCancelledBooking);
            cvUserModeOffer = (CardView) rootView.findViewById(R.id.cvUserModeOffer);
            cvUserModeReedem = (CardView) rootView.findViewById(R.id.cvUserModeReedem);

            cvAdminDailySales = (CardView) rootView.findViewById(R.id.cvAdminDailySales);
            cvAdminMonthlySales = (CardView) rootView.findViewById(R.id.cvAdminMonthlySales);

            cvUserNewOrder.setOnClickListener(this);
            cvUserCancelledOrder.setOnClickListener(this);
            cvUserNewBooking.setOnClickListener(this);
            cvUserCancelledBooking.setOnClickListener(this);
            cvUserModeOffer.setOnClickListener(this);
            cvUserModeReedem.setOnClickListener(this);

            cvAdminDailySales.setOnClickListener(this);
            cvAdminMonthlySales.setOnClickListener(this);

            llEventFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.layout_separator));

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.llEventFragment, new EventFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (HomeActivity.mode.equals("User")) {
                Globals.isAdmin = false;
                userModeLayout.setVisibility(View.VISIBLE);
                adminModeLayout.setVisibility(View.GONE);
                if (Service.CheckNet(getActivity())) {
                    DataRequestUser();
                } else {
                    Globals.ShowSnackBar(rootView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            } else if (HomeActivity.mode.equals("Admin")) {
                Globals.isAdmin = true;
                userModeLayout.setVisibility(View.GONE);
                adminModeLayout.setVisibility(View.VISIBLE);
                if (Service.CheckNet(getActivity())) {
                    DataRequestAdmin();
                } else {
                    Globals.ShowSnackBar(rootView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        DataRequest();
        OrderBookingData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvUserModeOffer) {
            Globals.ChangeActivity(getActivity(), OfferActivity.class, false);

        } else if (v.getId() == R.id.cvUserNewOrder) {
            Intent intent = new Intent(getActivity(), OrderBookingActivity.class);
            intent.putExtra("IsCancelled", false);
            intent.putExtra("activityName", getResources().getString(R.string.golfNewOrders));
            getActivity().startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (v.getId() == R.id.cvUserCancelledOrder) {
            Intent intent = new Intent(getActivity(), OrderBookingActivity.class);
            intent.putExtra("IsCancelled", true);
            intent.putExtra("activityName", getResources().getString(R.string.golfCancelledOrders));
            getActivity().startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (v.getId() == R.id.cvUserNewBooking) {
            Intent intent = new Intent(getActivity(), OrderBookingActivity.class);
            intent.putExtra("IsCancelled", false);
            intent.putExtra("activityName", getResources().getString(R.string.golfNewBookings));
            getActivity().startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (v.getId() == R.id.cvUserCancelledBooking) {
            Intent intent = new Intent(getActivity(), OrderBookingActivity.class);
            intent.putExtra("IsCancelled", true);
            intent.putExtra("activityName", getResources().getString(R.string.golfCancelledBookings));
            getActivity().startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (v.getId() == R.id.cvAdminDailySales) {
            Intent intent = new Intent(getActivity(), DailySalesActivity.class);
            intent.putExtra("SalesReportType", "Daily Sales");
            getActivity().startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.cvAdminMonthlySales) {
            Intent intent = new Intent(getActivity(), DailySalesActivity.class);
            intent.putExtra("SalesReportType", "Monthly Sales");
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void DashboardResponse(String errorCode, Dashboard objDashboard) {
        if (objDashboard != null) {
            Globals.newOrder = String.valueOf(objDashboard.getPreOrder());
            Globals.newBooking = String.valueOf(objDashboard.getRegisteredUserBooking());
            Globals.cancleOrder = String.valueOf(objDashboard.getCanceledOrder());
            Globals.cancleBooking = String.valueOf(objDashboard.getCanceledBooking());
        }
//        newOrder.setText(String.valueOf(objDashboard.getPreOrder()));
//        newBooking.setText(String.valueOf(objDashboard.getRegisteredUserBooking()));
//        cancelOrder.setText(String.valueOf(objDashboard.getCanceledOrder()));
//        cancelBooking.setText(String.valueOf(objDashboard.getCanceledBooking()));

        newOrder.setText(Globals.newOrder);
        newBooking.setText(Globals.newBooking);
        cancelOrder.setText(Globals.cancleOrder);
        cancelBooking.setText(Globals.cancleBooking);
    }

    @Override
    public void DashBoardSales(String dailySales, String weeklySales, String monthlySales) {
        this.dailySales.setText(dailySales);
        this.weeklySales.setText(weeklySales);
        this.monthlySales.setText(monthlySales);

//        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
//        objDashboardJSONParser.SelectOrderMasterLeastSellingDayOfLastWeek(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    @Override
    public void LastDayOfWeek(OrderMaster orderMaster) {
//       leastDaySale.setText(orderMaster.getLeastSellingDayName()+", "+orderMaster.);
    }


    public void OrderBookingData() {
        newOrder.setText(Globals.newOrder);
        newBooking.setText(Globals.newBooking);
        cancelOrder.setText(Globals.cancleOrder);
        cancelBooking.setText(Globals.cancleBooking);
    }

    private void DataRequestAdmin() {
//        progressDialog = new ProgressDialog();
//        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
        objDashboardJSONParser.SelectOrderMasterDailyWeeklyMonthlyOrders(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(0));
    }

    private void DataRequestUser() {
        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
//        if (HomeActivity.isStart) {
            objDashboardJSONParser.SelectDashBoardCounter(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId));
//        } else {
//            OrderBookingData();
//        }
    }


}
