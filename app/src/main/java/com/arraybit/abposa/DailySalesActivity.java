package com.arraybit.abposa;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.OrderPaymentTran;
import com.arraybit.modal.PaymentTypeMaster;
import com.arraybit.parser.DailySalesJSONParser;
import com.arraybit.parser.DashboardJSONParser;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DailySalesActivity extends AppCompatActivity implements DailySalesJSONParser.SalesReportListener, DashboardJSONParser.LastDayListener {

    EditText etdsFromDate, etdsToDate;
    TextView txtFromDate, txtToDate, txtLeastDay, txtWeekInformation;
    LinearLayout llDailySales, llTableLayout, errorLayout, llLeastDay;
    ScrollView svTableLayout;
    ArrayList<PaymentTypeMaster> lstPaymentTypeMasters = new ArrayList<>();
    String salesReportType;
    Spinner spinnerMonths, spinnerYears;
    SimpleDateFormat sdfControl = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterMonths;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isProgressDailog = true, isLeastDate = false;
    RelativeLayout rlSpinnerYears, rlSpinnerMonths;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sales);

        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(app_bar);
            if (app_bar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
            }

            Intent intent = getIntent();
            salesReportType = intent.getStringExtra("SalesReportType");
            getSupportActionBar().setTitle(salesReportType + " Report");

            llDailySales = (LinearLayout) findViewById(R.id.llDailySales);
            llTableLayout = (LinearLayout) findViewById(R.id.llTableLayout);
            llLeastDay = (LinearLayout) findViewById(R.id.llLeastDay);
            svTableLayout = (ScrollView) findViewById(R.id.svTableLayout);

            etdsFromDate = (EditText) findViewById(R.id.etdsFromDate);
            etdsToDate = (EditText) findViewById(R.id.etdsToDate);
            txtFromDate = (TextView) findViewById(R.id.txtFromDate);
            txtToDate = (TextView) findViewById(R.id.txtToDate);
            etdsToDate.setCursorVisible(false);
            etdsFromDate.setCursorVisible(false);

            txtLeastDay = (TextView) findViewById(R.id.txtLeastDay);
            txtWeekInformation = (TextView) findViewById(R.id.txtWeekInformation);

            spinnerMonths = (Spinner) findViewById(R.id.spinnerMonths);
            spinnerYears = (Spinner) findViewById(R.id.spinnerYears);
            rlSpinnerYears = (RelativeLayout) findViewById(R.id.rlSpinnerYears);
            rlSpinnerMonths = (RelativeLayout) findViewById(R.id.rlSpinnerMonths);

            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

            if (salesReportType.equals(Globals.SalesAnalysis.dailySales.getDesc())) {
                txtFromDate.setText("From Date :");
                txtToDate.setText("To Date :");
                etdsToDate.setVisibility(View.VISIBLE);
                txtToDate.setVisibility(View.VISIBLE);
                rlSpinnerYears.setVisibility(View.GONE);
                rlSpinnerMonths.setVisibility(View.GONE);

                etdsFromDate.setText(sdfControl.format(new Date()));
                etdsToDate.setText(sdfControl.format(new Date()));
                String strFromDate = etdsFromDate.getText().toString();
                String strToDate = etdsToDate.getText().toString();
                if (!strFromDate.equals("") && !strToDate.equals("")) {
                    if (Service.CheckNet(DailySalesActivity.this)) {
                        RequestDailySaleDateWise(strFromDate, strToDate);
                    } else {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                    }
                }
            } else if (salesReportType.equals(Globals.SalesAnalysis.leastSellingDay.getDesc())) {

                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                c.add(Calendar.DATE, -i - 7);
                Date start = c.getTime();
                c.add(Calendar.DATE, 6);
                Date end = c.getTime();

                txtFromDate.setText("From Date :");
                txtToDate.setText("To Date :");
                etdsToDate.setVisibility(View.VISIBLE);
                txtToDate.setVisibility(View.VISIBLE);
                rlSpinnerYears.setVisibility(View.GONE);
                rlSpinnerMonths.setVisibility(View.GONE);

                etdsFromDate.setText(sdfControl.format(start));
                etdsToDate.setText(sdfControl.format(end));
                String strFromDate = etdsFromDate.getText().toString();
                String strToDate = etdsToDate.getText().toString();
                if (!strFromDate.equals("") && !strToDate.equals("")) {
                    if (Service.CheckNet(DailySalesActivity.this)) {
                        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
                        objDashboardJSONParser.SelectOrderMasterLeastSellingDayOfLastWeek(DailySalesActivity.this, null, String.valueOf(Globals.linktoBusinessMasterId), strFromDate, strToDate, true);
//                        RequestDailySaleDateWise(strFromDate, strToDate);
                    } else {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                    }
                }
            } else if (salesReportType.equals(Globals.SalesAnalysis.weeklySales.getDesc())) {

                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

                Date weekStart = c.getTime();
// we do not need the same day a week after, that's why use 6, not 7
                c.setTime(date);
                Date weekEnd = c.getTime();

                txtFromDate.setText("Select Week:");
//                txtToDate.setText("To Date :");
                txtWeekInformation.setVisibility(View.VISIBLE);
                etdsToDate.setVisibility(View.GONE);
                txtToDate.setVisibility(View.GONE);
                rlSpinnerYears.setVisibility(View.GONE);
                rlSpinnerMonths.setVisibility(View.GONE);

                txtFromDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
                etdsFromDate.setLayoutParams(new LinearLayout.LayoutParams(400, 60));

                etdsFromDate.setText(sdfControl.format(weekStart) + " to " + sdfControl.format(weekEnd));
//                etdsToDate.setText(sdfControl.format(weekEnd));
                String strFromDate = sdfControl.format(weekStart);
                String strToDate = sdfControl.format(weekEnd);
                if (!strFromDate.equals("") && !strToDate.equals("")) {
                    if (Service.CheckNet(DailySalesActivity.this)) {
                        RequestDailySaleDateWise(strFromDate, strToDate);
                    } else {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                    }
                }
            } else if (salesReportType.equals(Globals.SalesAnalysis.monthlySales.getDesc())) {
                txtFromDate.setText("Month :");
                txtToDate.setText("Year :");
                etdsToDate.setVisibility(View.GONE);
                etdsFromDate.setVisibility(View.GONE);
                rlSpinnerYears.setVisibility(View.VISIBLE);
                rlSpinnerMonths.setVisibility(View.VISIBLE);

                ArrayList<String> years = new ArrayList<>();
                ArrayList<String> Months = new ArrayList<>();
                int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
                int leastYear = 2011;
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this).equals("")) {
                    Date leastDate = sdfControl.parse(objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this));
                    Calendar c = Calendar.getInstance();
                    c.setTime(leastDate);
                    leastYear = c.get(Calendar.YEAR);
                    if (thisYear == leastYear) {
                        int leastMonth = c.get(Calendar.MONTH);
                        for (int i = (leastMonth + 1); i <= (thisMonth + 1); i++) {
                            Months.add(Globals.Months.getMonth(i));
                        }
                        isLeastDate = true;
                    }
                }
                for (int i = leastYear; i <= thisYear; i++) {
                    years.add(Integer.toString(i));
                }
                adapter = new ArrayAdapter<String>(this, R.layout.row_month_year, years);
                if (!isLeastDate) {
                    for (int i = 1; i <= (thisMonth + 1); i++) {
                        Months.add(Globals.Months.getMonth(i));
                    }
                }
                spinnerYears.setAdapter(adapter);
                // Set months
                adapterMonths = new ArrayAdapter<String>(this, R.layout.row_month_year, Months);
                spinnerMonths.setAdapter(adapterMonths);

                String thisMonth1 = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                String thisYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                int spinnerMonthPosition = adapterMonths.getPosition(thisMonth1);
                int spinnerYearPosition = adapter.getPosition(thisYear1);
                spinnerMonths.setSelection(spinnerMonthPosition);
                spinnerYears.setSelection(spinnerYearPosition);

                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Integer.parseInt(spinnerYears.getSelectedItem().toString()), spinnerMonths.getSelectedItemPosition() + 1, 0);
                Date today = cal.getTime();
//                Date today = new Date();
                cal.setTime(today);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                Date start = cal.getTime();

                cal.setTime(today);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date end = cal.getTime();

                etdsFromDate.setText(sdfControl.format(start));
                etdsToDate.setText(sdfControl.format(end));
                String strFromDate = etdsFromDate.getText().toString();
                String strToDate = etdsToDate.getText().toString();
                if (!strFromDate.equals("") && !strToDate.equals("")) {
                    if (Service.CheckNet(DailySalesActivity.this)) {
                        RequestDailySaleDateWise(strFromDate, strToDate);
                    } else {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                    }
                }
            }

            etdsFromDate.addTextChangedListener(new TextWatcher() {
                String strFromDate = "";
                String strToDate = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (!etdsFromDate.getText().toString().equals("") && !(strFromDate.equals(etdsFromDate.getText().toString()))) {
                            if (salesReportType.equals(Globals.SalesAnalysis.dailySales.getDesc())) {
                                strFromDate = etdsFromDate.getText().toString();
                                strToDate = etdsToDate.getText().toString();
                                if (!strFromDate.equals("") && !strToDate.equals("")) {
                                    if (Service.CheckNet(DailySalesActivity.this)) {
                                        RequestDailySaleDateWise(strFromDate, strToDate);
                                    } else {
                                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                    }
                                }
                            } else if (salesReportType.equals(Globals.SalesAnalysis.weeklySales.getDesc())) {
                                strFromDate = etdsFromDate.getText().toString();
//                            strToDate = etdsToDate.getText().toString();

                                Date date = new Date();
                                Date weekStart = null;
                                Date weekEnd = null;
                                Calendar c = Calendar.getInstance();
                                c.setTime(sdfControl.parse(strFromDate));
                                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                                c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
                                objSharePreferenceManage = new SharePreferenceManage();
                                if (objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this).equals("")) {
                                    Date leastDate = sdfControl.parse(objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this));
                                    if (leastDate.after(c.getTime())) {
                                        weekStart = leastDate;
                                        isLeastDate = true;
                                    } else {
                                        weekStart = c.getTime();
                                    }
                                    c.add(Calendar.DAY_OF_MONTH, 6);
                                    weekEnd = c.getTime();
                                }

                                if (!isLeastDate) {
                                    Log.e("date", " " + c.getTime());
                                    weekStart = c.getTime();
// we do not need the same day a week after, that's why use 6, not 7
                                    c.add(Calendar.DAY_OF_MONTH, 6);
                                    weekEnd = c.getTime();
                                    if (date.after(weekStart) && date.before(weekEnd)) {
                                        c.setTime(date);
                                        weekEnd = c.getTime();
                                    }
                                }

                                etdsFromDate.setLayoutParams(new LinearLayout.LayoutParams(400, 60));

                                etdsFromDate.setText(sdfControl.format(weekStart) + " to " + sdfControl.format(weekEnd));
                                if (!strFromDate.equals("")) {
                                    if (Service.CheckNet(DailySalesActivity.this)) {
                                        RequestDailySaleDateWise(sdfControl.format(weekStart), sdfControl.format(weekEnd));
                                    } else {
                                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                    }
                                }
                            } else if (salesReportType.equals(Globals.SalesAnalysis.leastSellingDay.getDesc())) {
                                strFromDate = etdsFromDate.getText().toString();
                                strToDate = etdsToDate.getText().toString();
                                if (!strFromDate.equals("") && !strToDate.equals("")) {
                                    if (Service.CheckNet(DailySalesActivity.this)) {
                                        DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
                                        objDashboardJSONParser.SelectOrderMasterLeastSellingDayOfLastWeek(DailySalesActivity.this, null, String.valueOf(Globals.linktoBusinessMasterId), strFromDate, strToDate, true);
//                                    RequestDailySaleDateWise(strFromDate, strToDate);
                                    } else {
                                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                    }
                                }
                            } else {
                                strFromDate = etdsFromDate.getText().toString();
                                strToDate = etdsToDate.getText().toString();
                                if (!strFromDate.equals("") && !strToDate.equals("")) {
                                    if (Service.CheckNet(DailySalesActivity.this)) {
                                        RequestDailySaleDateWise(strFromDate, strToDate);
                                    } else {
                                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                    }
                                }
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            etdsToDate.addTextChangedListener(new TextWatcher() {
                String strFromDate = "";
                String strToDate = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!etdsToDate.getText().toString().equals("") && !(strToDate.equals(etdsFromDate.getText().toString()))) {
                        if (salesReportType.equals(Globals.SalesAnalysis.dailySales.getDesc())) {
                            strFromDate = etdsFromDate.getText().toString();
                            strToDate = etdsToDate.getText().toString();
                            if (!strFromDate.equals("") && !strToDate.equals("")) {
                                if (Service.CheckNet(DailySalesActivity.this)) {
                                    RequestDailySaleDateWise(strFromDate, strToDate);
                                } else {
                                    Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                    Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                }
                            }
                        } else if (salesReportType.equals(Globals.SalesAnalysis.leastSellingDay.getDesc())) {
                            strFromDate = etdsFromDate.getText().toString();
                            strToDate = etdsToDate.getText().toString();
                            if (!strFromDate.equals("") && !strToDate.equals("")) {
                                if (Service.CheckNet(DailySalesActivity.this)) {
                                    DashboardJSONParser objDashboardJSONParser = new DashboardJSONParser();
                                    objDashboardJSONParser.SelectOrderMasterLeastSellingDayOfLastWeek(DailySalesActivity.this, null, String.valueOf(Globals.linktoBusinessMasterId), strFromDate, strToDate, true);
//                                    RequestDailySaleDateWise(strFromDate, strToDate);
                                } else {
                                    Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                    Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                                }
                            }
                        }
                    }
                }
            });

            spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Calendar cal = Calendar.getInstance();
                    cal.clear();
                    cal.set(Integer.parseInt(spinnerYears.getSelectedItem().toString()), spinnerMonths.getSelectedItemPosition() + 1, 0);
                    Date today = cal.getTime();
                    cal.setTime(today);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    Date start = cal.getTime();

                    cal.setTime(today);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date end = cal.getTime();

                    etdsFromDate.setText(sdfControl.format(start));
                    etdsToDate.setText(sdfControl.format(end));
                    String strFromDate = etdsFromDate.getText().toString();
                    String strToDate = etdsToDate.getText().toString();
                    if (!strFromDate.equals("") && !strToDate.equals("")) {
                        if (Service.CheckNet(DailySalesActivity.this)) {
                            RequestDailySaleDateWise(strFromDate, strToDate);
                        } else {
                            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                            Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    try {
                        isLeastDate = false;
                        String thisMonth1 = spinnerMonths.getSelectedItem().toString();
                        if (String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).equals(spinnerYears.getSelectedItem().toString())) {
                            int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
                            String thisYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                            ArrayList<String> Months = new ArrayList<>();
                            objSharePreferenceManage = new SharePreferenceManage();
                            if (objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this).equals("")) {
                                Date leastDate = sdfControl.parse(objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this));
                                Calendar c = Calendar.getInstance();
                                c.setTime(leastDate);
                                int leastYear = c.get(Calendar.YEAR);
                                if (Integer.parseInt(thisYear1) == leastYear) {
                                    int leastMonth = c.get(Calendar.MONTH);
                                    for (int i = (leastMonth + 1); i <= (thisMonth + 1); i++) {
                                        Months.add(Globals.Months.getMonth(i));
                                    }
                                    isLeastDate = true;
                                }
                            }

                            if (!isLeastDate) {
                                for (int i = 1; i <= (thisMonth + 1); i++) {
                                    Months.add(Globals.Months.getMonth(i));
                                }
                            }
                            adapterMonths = new ArrayAdapter<String>(DailySalesActivity.this, R.layout.row_month_year, Months);
                            spinnerMonths.setAdapter(adapterMonths);

                        } else {
                            ArrayList<String> Months = new ArrayList<>();
                            String thisYear1 = spinnerYears.getSelectedItem().toString();
                            objSharePreferenceManage = new SharePreferenceManage();
                            if (objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this).equals("")) {
                                Date leastDate = sdfControl.parse(objSharePreferenceManage.GetPreference("BusinessPreference", "CreateDateTime", DailySalesActivity.this));
                                Calendar c = Calendar.getInstance();
                                c.setTime(leastDate);
                                int leastYear = c.get(Calendar.YEAR);
                                if (Integer.parseInt(thisYear1) == leastYear) {
                                    int leastMonth = c.get(Calendar.MONTH);
                                    for (int i = (leastMonth + 1); i <= Globals.Months.values().length; i++) {
                                        Months.add(Globals.Months.getMonth(i));
                                    }
                                    isLeastDate = true;
                                }
                            }

                            if (!isLeastDate) {
                                for (int i = 1; i <= Globals.Months.values().length; i++) {
                                    Months.add(Globals.Months.getMonth(i));
                                }
                            }
                            adapterMonths = new ArrayAdapter<String>(DailySalesActivity.this, R.layout.row_month_year, Months);
                            spinnerMonths.setAdapter(adapterMonths);
                        }
                        int spinnerMonthPosition = adapterMonths.getPosition(thisMonth1);
                        spinnerMonths.setSelection(spinnerMonthPosition);

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Integer.parseInt(spinnerYears.getSelectedItem().toString()), spinnerMonths.getSelectedItemPosition() + 1, 0);
                        Date today = cal.getTime();
                        cal.setTime(today);

                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        Date start = cal.getTime();

                        cal.setTime(today);
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        Date end = cal.getTime();

                        etdsFromDate.setText(sdfControl.format(start));
                        etdsToDate.setText(sdfControl.format(end));
                        String strFromDate = etdsFromDate.getText().toString();
                        String strToDate = etdsToDate.getText().toString();
                        if (!strFromDate.equals("") && !strToDate.equals("")) {
                            if (Service.CheckNet(DailySalesActivity.this)) {
                                RequestDailySaleDateWise(strFromDate, strToDate);
                            } else {
                                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                                Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("errormonth", " " + e.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, R.anim.right_exit);
    }

    public void FromDateOnClick(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Globals.ShowDatePickerDialog(etdsFromDate, DailySalesActivity.this, true);
        }
    }

    public void ToDateOnClick(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Globals.ShowDatePickerDialog(etdsToDate, DailySalesActivity.this, true);
        }
    }

    @Override
    public void SetDailySalesReport(ArrayList<OrderPaymentTran> lstOrderPaymentTran) {
        if (progressDialog.isVisible()) {
            progressDialog.dismiss();
            isProgressDailog = true;
        }
        ClearData();
        if (lstOrderPaymentTran == null) {
            svTableLayout.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null, R.drawable.alert);
//            Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgServerNotResponding), DailySalesActivity.this, 1000);
        } else if (lstOrderPaymentTran.size() == 0) {
            svTableLayout.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null, R.drawable.alert);
//            Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgServerNotResponding), DailySalesActivity.this, 1000);
        } else {
            svTableLayout.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, false, null, null, 0);
            SetSalesReport(lstOrderPaymentTran);
        }
    }

    @Override
    public void LastDayOfWeek(OrderMaster orderMaster) {
        if (orderMaster != null) {
            Globals.SetErrorLayout(errorLayout, false, null, null, 0);
            llLeastDay.setVisibility(View.VISIBLE);
            txtLeastDay.setText(orderMaster.getLeastSellingDayName() + " - " + orderMaster.getOrderToDateTime());
            String dateformat = orderMaster.getOrderToDateTime().replace('-', '/');
            if (Service.CheckNet(DailySalesActivity.this)) {
                RequestDailySaleDateWise(dateformat, dateformat);
            } else {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
//                Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
            }
        } else {
            llLeastDay.setVisibility(View.GONE);
            svTableLayout.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null, R.drawable.alert);
//                Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
        }
    }

    private void RequestDailySaleDateWise(String fromDate, String toDate) {
        if (isProgressDailog) {
            isProgressDailog = false;
            progressDialog.show(getSupportFragmentManager(), "");
        }
        DailySalesJSONParser objDailySalesJSONParser = new DailySalesJSONParser();
        objDailySalesJSONParser.SelectDailySaleDateWise(this, fromDate, toDate, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetSalesReport(ArrayList<OrderPaymentTran> lstOrderPaymentTran) {
        double totalSale = 0;
        ArrayList<String> rv = new ArrayList<>();
        ArrayList<String> cv = new ArrayList<>();

        for (OrderPaymentTran objOrderPaymentTran : lstOrderPaymentTran) {
            rv.add(objOrderPaymentTran.getPaymentType());
            cv.add(String.valueOf(objOrderPaymentTran.getTotalAmount()));
            totalSale += objOrderPaymentTran.getTotalAmount();
        }
        rv.add(getResources().getString(R.string.total_sales));
        cv.add(String.valueOf(totalSale));
        TableLayout tableLayout = createTableLayout(rv, cv, lstOrderPaymentTran.size() + 1, 2);

        llTableLayout.addView(tableLayout);
    }

    private void ClearData() {
        llTableLayout.removeAllViewsInLayout();
    }

    private TableLayout createTableLayout(ArrayList<String> rv, ArrayList<String> cv, int rowCount, int columnCount) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        TableLayout tableLayout = new TableLayout(this);
//        tableLayout.setBackgroundColor(Color.BLACK);

        TableRow.LayoutParams layoutParamsCategory = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 80, 4);
        TableRow.LayoutParams layoutParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 80, 1);

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(layoutParamsCategory);
        tv0.setText(getResources().getString(R.string.category));
        tv0.setTextColor(ContextCompat.getColor(this, R.color.primary));
        tv0.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv0.setPadding((int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall));
        tv0.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);


        tv0.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(layoutParamsTotal);
        tv1.setText(getResources().getString(R.string.total));
        tv1.setPadding((int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall));
        tv1.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);
        tv1.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
        tv1.setTextColor(ContextCompat.getColor(this, R.color.primary));
        tbrow0.addView(tv1);

        tableLayout.addView(tbrow0);
        for (int i = 0; i < rowCount; i++) {
            TableRow tbrow = new TableRow(this);
            tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columnCount; j++) {
                TextView t1v = new TextView(this);
                if (j == 0) {
                    t1v.setText(rv.get(i));
                    t1v.setLayoutParams(layoutParamsCategory);
                    t1v.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    t1v.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
                } else if (j == 1) {
                    t1v.setText(cv.get(i));
                    t1v.setLayoutParams(layoutParamsTotal);
                    t1v.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                    t1v.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
                }
                if (i == rowCount - 1) {
                    t1v.setTextColor(ContextCompat.getColor(this, R.color.accent));
                }
                t1v.setPadding((int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall));
                tbrow.addView(t1v);
            }
            tableLayout.addView(tbrow);
        }
        return tableLayout;
    }
}
