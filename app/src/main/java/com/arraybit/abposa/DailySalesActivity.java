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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OrderPaymentTran;
import com.arraybit.modal.PaymentTypeMaster;
import com.arraybit.parser.DailySalesJSONParser;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DailySalesActivity extends AppCompatActivity implements DailySalesJSONParser.SalesReportListener {

    EditText etdsFromDate, etdsToDate;
    TextView txtFromDate, txtToDate;
    LinearLayout llDailySales, llTableLayout, errorLayout;
    ScrollView svTableLayout;
    ArrayList<PaymentTypeMaster> lstPaymentTypeMasters = new ArrayList<>();
    String salesReportType;
    Spinner spinnerMonths, spinnerYears;
    SimpleDateFormat sdfControl = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterMonths;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isProgressDailog = true;

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
            svTableLayout = (ScrollView) findViewById(R.id.svTableLayout);

            etdsFromDate = (EditText) findViewById(R.id.etdsFromDate);
            etdsToDate = (EditText) findViewById(R.id.etdsToDate);
            txtFromDate = (TextView) findViewById(R.id.txtFromDate);
            txtToDate = (TextView) findViewById(R.id.txtToDate);
            etdsToDate.setCursorVisible(false);
            etdsFromDate.setCursorVisible(false);

            spinnerMonths = (Spinner) findViewById(R.id.spinnerMonths);
            spinnerYears = (Spinner) findViewById(R.id.spinnerYears);

            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

            if (salesReportType.equals("Daily Sales")) {
                txtFromDate.setText(getResources().getString(R.string.sales_date));
                etdsToDate.setVisibility(View.GONE);
                txtToDate.setVisibility(View.GONE);
                spinnerYears.setVisibility(View.GONE);
                spinnerMonths.setVisibility(View.GONE);

                etdsFromDate.setText(sdfControl.format(new Date()));
                String strFromDate = etdsFromDate.getText().toString();
                String strToDate = etdsFromDate.getText().toString();
                if (!strFromDate.equals("") && !strToDate.equals("")) {
                    if (Service.CheckNet(DailySalesActivity.this)) {
                        RequestDailySaleDateWise(strFromDate, strToDate);
                    } else {
                        Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
                    }
                }
            } else if (salesReportType.equals("By Date Range Sales")) {
                txtFromDate.setText(getResources().getString(R.string.sales_from_date));
                txtToDate.setText(getResources().getString(R.string.sales_to_date));
                etdsToDate.setVisibility(View.VISIBLE);
                txtToDate.setVisibility(View.VISIBLE);
                spinnerYears.setVisibility(View.GONE);
                spinnerMonths.setVisibility(View.GONE);

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
            } else if (salesReportType.equals("Monthly Sales")) {
                txtFromDate.setText(getResources().getString(R.string.sales_month));
                txtToDate.setText(getResources().getString(R.string.sales_year));
                etdsToDate.setVisibility(View.GONE);
                etdsFromDate.setVisibility(View.GONE);
                spinnerYears.setVisibility(View.VISIBLE);
                spinnerMonths.setVisibility(View.VISIBLE);

                ArrayList<String> years = new ArrayList<>();
                ArrayList<String> Months = new ArrayList<>();
                int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                for (int i = 2011; i <= thisYear; i++) {
                    years.add(Integer.toString(i));
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
                for (int i = 1; i <= Globals.Months.values().length; i++) {
                    Months.add(Globals.Months.getMonth(i));
                }
                spinnerYears.setAdapter(adapter);
                // Set months
                adapterMonths = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Months);
//                adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerMonths.setAdapter(adapterMonths);
                String thisMonth = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                String thisYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                int spinnerMonthPosition = adapterMonths.getPosition(thisMonth);
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
//                    if (!etdsFromDate.getText().toString().equals("") && !(strFromDate.equals(etdsFromDate.getText().toString()))
//                            && !etdsToDate.getText().toString().equals("") && !(strToDate.equals(etdsFromDate.getText().toString()))) {
////                        etdsFromDate.setSelection(etdsFromDate.getText().length());
//                        strFromDate = etdsFromDate.getText().toString();
//                        strToDate = etdsToDate.getText().toString();
//                        if (!strFromDate.equals("") && !strToDate.equals("")) {
//                            if (Service.CheckNet(DailySalesActivity.this)) {
//                                RequestDailySaleDateWise(strFromDate, strToDate);
//                            } else {
//                                Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
//                            }
//                        }
//                    }
                    if (!etdsFromDate.getText().toString().equals("") && !(strFromDate.equals(etdsFromDate.getText().toString()))) {
                        if (salesReportType.equals("Daily Sales")) {
//                        txtFromDate.setText("Date ");
//                        etdsToDate.setVisibility(View.GONE);
//                        txtToDate.setVisibility(View.GONE);
//                        etdsFromDate.setText(sdfControl.format(new Date()));
                            strFromDate = etdsFromDate.getText().toString();
                            strToDate = etdsFromDate.getText().toString();
                            if (!strFromDate.equals("") && !strToDate.equals("")) {
                                if (Service.CheckNet(DailySalesActivity.this)) {
                                    RequestDailySaleDateWise(strFromDate, strToDate);
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
//                       else if (salesReportType.equals("Monthly Sales")) {
//                            try {
////                                txtFromDate.setText("From Date :");
////                                txtToDate.setText("To Date :");
////                                etdsToDate.setVisibility(View.VISIBLE);
////                                txtToDate.setVisibility(View.VISIBLE);
//                                Date today = sdfControl.parse(String.valueOf(etdsFromDate.getText()));
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime(today);
//                                cal.set(Calendar.DAY_OF_MONTH, 1);
//                                Date start = cal.getTime();
//
//                                cal.setTime(today);
//                                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//                                Date end = cal.getTime();
//
//                                etdsFromDate.setText(sdfControl.format(start));
//                                etdsToDate.setText(sdfControl.format(end));
//                                String strFromDate = etdsFromDate.getText().toString();
//                                String strToDate = etdsToDate.getText().toString();
//                                if (!strFromDate.equals("") && !strToDate.equals("")) {
//                                    if (Service.CheckNet(DailySalesActivity.this)) {
//                                        RequestDailySaleDateWise(strFromDate, strToDate);
//                                    } else {
//                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }

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
                    if (!etdsFromDate.getText().toString().equals("") && !(strFromDate.equals(etdsFromDate.getText().toString()))
                            && !etdsToDate.getText().toString().equals("") && !(strToDate.equals(etdsFromDate.getText().toString()))) {
//                        etdsToDate.setSelection(etdsToDate.getText().length());
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
//                    if (!etdsToDate.getText().toString().equals("") && !(strToDate.equals(etdsToDate.getText().toString()))) {
//                        if (salesReportType.equals("Monthly Sales")) {
//                            try {
////                                txtFromDate.setText("From Date :");
////                                txtToDate.setText("To Date :");
////                                etdsToDate.setVisibility(View.VISIBLE);
////                                txtToDate.setVisibility(View.VISIBLE);
//
//                                Date today = sdfControl.parse(String.valueOf(etdsToDate.getText()));
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime(today);
//                                cal.set(Calendar.DAY_OF_MONTH, 1);
//                                Date start = cal.getTime();
//
//                                cal.setTime(today);
//                                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//                                Date end = cal.getTime();
//
//                                etdsFromDate.setText(sdfControl.format(start));
//                                etdsToDate.setText(sdfControl.format(end));
//                                strFromDate = etdsFromDate.getText().toString();
//                                strToDate = etdsToDate.getText().toString();
//                                if (!strFromDate.equals("") && !strToDate.equals("")) {
//                                    if (Service.CheckNet(DailySalesActivity.this)) {
//                                        RequestDailySaleDateWise(strFromDate, strToDate);
//                                    } else {
//                                        Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgCheckConnection), DailySalesActivity.this, 1000);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
                }
            });

            spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                    String thisMonth = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
//                    String thisYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//                    int spinnerMonthPosition = adapterMonths.getPosition(thisMonth);
//                    int spinnerYearPosition = adapter.getPosition(thisYear1);
//                    spinnerMonths.setSelection(spinnerMonthPosition);
//                    spinnerYears.setSelection(spinnerYearPosition);

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

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                    String thisMonth = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
//                    String thisYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//                    int spinnerMonthPosition = adapterMonths.getPosition(thisMonth);
//                    int spinnerYearPosition = adapter.getPosition(thisYear1);
//                    spinnerMonths.setSelection(spinnerMonthPosition);
//                    spinnerYears.setSelection(spinnerYearPosition);

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
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null, R.drawable.alert);
//            Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgServerNotResponding), DailySalesActivity.this, 1000);
        } else if (lstOrderPaymentTran.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null, R.drawable.alert);
//            Globals.ShowSnackBar(llDailySales, getResources().getString(R.string.MsgServerNotResponding), DailySalesActivity.this, 1000);
        } else {
            svTableLayout.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, false, null, null, 0);
            SetSalesReport(lstOrderPaymentTran);
        }
    }

    private void RequestDailySaleDateWise(String fromDate, String toDate) {
        if (isProgressDailog) {
            isProgressDailog = false;
            progressDialog.show(getSupportFragmentManager(), "");
        }
        DailySalesJSONParser objDailySalesJSONParser = new DailySalesJSONParser();
        objDailySalesJSONParser.SelectDailySaleDateWise(this, null, fromDate, toDate, String.valueOf(Globals.linktoBusinessMasterId));

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

        TableRow.LayoutParams layoutParamsCategory = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 8);
        TableRow.LayoutParams layoutParamsTotal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(layoutParamsCategory);
        tv0.setText(getResources().getString(R.string.category));
        tv0.setTextColor(ContextCompat.getColor(this, R.color.primary));
        tv0.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv0.setPadding((int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall));
        tv0.setGravity(Gravity.START);
        tv0.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(layoutParamsTotal);
        tv1.setText(getResources().getString(R.string.total));
        tv1.setPadding((int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall), (int) getResources().getDimension(R.dimen.control_SpecingSmall));
        tv1.setGravity(Gravity.END);
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
                    t1v.setGravity(Gravity.START);
                    t1v.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
                } else if (j == 1) {
                    t1v.setText(cv.get(i));
                    t1v.setLayoutParams(layoutParamsTotal);
                    t1v.setGravity(Gravity.END);
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
