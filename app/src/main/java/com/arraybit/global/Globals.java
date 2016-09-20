package com.arraybit.global;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.abposa.R;
import com.rey.material.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Globals {

    public static short linktoBusinessMasterId = 0;
    public static short linktoBusinessGroupMasterId = 0;
    public static int totalCounter = 0;
    public static Fragment targetFragment;
    public static boolean isAdmin;
    public static String DateFormat = "dd/MM/yyyy";
    public static String TimeFormat = "hh:mm";
    public static String DisplayTimeFormat = "hh:mm a";
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    public static String newOrder;
    public static String cancleOrder;
    public static String newBooking;
    public static String cancleBooking;
    static int y, M, d, H, m;

    public static void HideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void ShowSnackBar(View view, String message, Context context, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        TextView txt = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey));
        snackbar.show();
    }

    public static void ClearPreference(Activity activity) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserName", activity);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserMasterId", activity);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserPassword", activity);
        objSharePreferenceManage.RemovePreference("LoginPreference", "BusinessMasterId", activity);
        objSharePreferenceManage.RemovePreference("BusinessPreference", "BusinessMasterId", activity);
        objSharePreferenceManage.RemovePreference("BusinessPreference", "BusinessGroupMasterId", activity);

        objSharePreferenceManage.ClearPreference("LoginPreference", activity);
        objSharePreferenceManage.ClearPreference("BusinessPreference", activity);
    }

    public static void ChangeActivity(Activity fromActivity, Class toActivity, boolean finish) {
        Intent intent = new Intent(fromActivity, toActivity);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        if (finish) {
            fromActivity.finish();
        }
    }

    public static void ReplaceFragmentFull(Fragment fragment, FragmentManager fragmentManager, String fragmentName) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    public static void ReplaceFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName, int layoutId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(layoutId, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg, RecyclerView recyclerView, int errorIcon) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        }
        if (isShow) {
            layout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            layout.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void SetItemAnimator(RecyclerView.ViewHolder holder) {
        //slide from bottom
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 200, 0);
        animatorTranslateY.setDuration(500);
        animatorTranslateY.start();
    }

    public static void ShowDatePickerDialog(final EditText txtView, Context context, final boolean IsPreventNextDateRequest) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
            }
        }

        y = c.get(Calendar.YEAR);
        M = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();

                        Date date = new Date(view.getMinDate());
                        calendar.setTime(date);

                        y = year;
                        M = monthOfYear;
                        d = dayOfMonth;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
                        if (IsPreventNextDateRequest) {
                            if (d >= calendar.get(Calendar.DAY_OF_MONTH) || M >= calendar.get(Calendar.MONTH) || y >= calendar.get(Calendar.YEAR)) {
                                txtView.setText(sdfControl.format(cal.getTime()));
                            } else {
                                txtView.setText(sdfControl.format(new Date()));
                            }
                        } else {
                            txtView.setText(sdfControl.format(cal.getTime()));
                        }
                    }

                }, y, M, d);
        if (IsPreventNextDateRequest) {
            dp.getDatePicker().setMaxDate(System.currentTimeMillis() + 10000);
        }
        dp.hide();
        dp.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String GetCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h/m");
        return sdf.format(calendar.getTime());
    }

    //region Enum
    public enum SalesAnalysis {

        dailySales(0, "Daily Sales", true),
        weeklySales(1, "Weekly Sales", false),
        monthlySales(2, "Monthly Sales", true),
        leastSellingDay(3, "Least Selling Day", false),
        leastSellingHours(4, "Least Selling Hours", false),
        byDateRange(5, "By Date Range Sales", true),
        lowStock(6, "Low Stock", false),
        profitloss(7, "Profit/Loss", false),
        yearlySales(8, "Yearly Sales", true),
        orderTypewise(9, "Order Type Wise", true),
        categoryWise(10, "Category Wise", true),
        leastSellingItems(11, "Least Selling Items", true),
        mostSellingItems(12, "Most Selling Items", true);

        private int id;
        private String desc;
        private boolean isGraph;

        SalesAnalysis(int id, String desc, boolean isGraph) {
            this.id = id;
            this.desc = desc;
            this.isGraph = isGraph;
        }

        public static String getValue(int id) {

            for (SalesAnalysis sale : SalesAnalysis.values()) {
                if (sale.getId() == id) {
                    return sale.getDesc();
                }
            }
            return null;
        }

        public static boolean getGraphValue(int id) {

            for (SalesAnalysis sale : SalesAnalysis.values()) {
                if (sale.getId() == id) {
                    return sale.getGraph();
                }
            }
            return false;
        }

        public int getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        public boolean getGraph() {
            return isGraph;
        }
    }

    public enum Months {

        January(1, "January", "JAN"),
        February(2, "February", "FEB"),
        March(3, "March", "MAR"),
        April(4, "April", "APR"),
        May(5, "May", "MAY"),
        June(6, "June", "JUN"),
        July(7, "July", "JUL"),
        August(8, "August", "AUG"),
        September(9, "September", "SEP"),
        October(10, "October", "OCT"),
        November(11, "November", "NOV"),
        December(12, "December", "DEC");

        private int id;
        private String month;
        private String sMonth;

        Months(int id, String month, String sMonth) {
            this.id = id;
            this.month = month;
            this.sMonth = sMonth;
        }

        public static String getMonth(int id) {
            for (Months sale : Months.values()) {
                if (sale.getId() == id) {
                    return sale.getMonthName();
                }
            }
            return null;
        }

        public static String getShortMonth(int id) {
            for (Months sale : Months.values()) {
                if (sale.getId() == id) {
                    return sale.getMonthShortName();
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public String getMonthName() {
            return month;
        }

        public String getMonthShortName() {
            return sMonth;
        }

    }

    public enum OrderType {
        DineIn(1),
        TakeAway(2),
        HomeDelivery(3);

        private int intValue;

        OrderType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum OrderStatus {
        Cooking(1),
        Ready(2),
        Served(3),
        Cancelled(4),
        Left(5),
        Delivered(6);

        private int intValue;

        OrderStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum BookingStatus {
        New(1),
        Modified(2),
        Confirmed(3),
        Cancelled(4);

        private int intValue;

        BookingStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    //endregion
}
