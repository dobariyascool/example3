package com.arraybit.parser;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OrderPaymentTran;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DailySalesJSONParser {

    public String SelectAllDailySaleDateWise = "SelectAllDailySaleDateWise";
    public SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    public Date dt = null;
    public Date dt1 = null;
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SalesReportListener objSalesReportListener;

    private ArrayList<OrderPaymentTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderPaymentTran> lstobjOrderPaymentTran = new ArrayList<>();
        OrderPaymentTran objOrderPaymentTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderPaymentTran = new OrderPaymentTran();

                if (jsonArray.getJSONObject(i).getLong("OrderPaymentTranId") > 0) {
                    objOrderPaymentTran.setOrderPaymentTranId(jsonArray.getJSONObject(i).getLong("OrderPaymentTranId"));
                }
                if (jsonArray.getJSONObject(i).getLong("linktoOrderMasterId") > 0) {
                    objOrderPaymentTran.setLinktoOrderMasterId(jsonArray.getJSONObject(i).getLong("linktoOrderMasterId"));
                }
                if (jsonArray.getJSONObject(i).getInt("linktoPaymentTypeMasterId") > 0) {
                    objOrderPaymentTran.setLinktoPaymentTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoPaymentTypeMasterId"));
                }
                if (jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId") > 0) {
                    objOrderPaymentTran.setLinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                if (jsonArray.getJSONObject(i).getDouble("AmountPaid") > 0) {
                    objOrderPaymentTran.setAmountPaid(jsonArray.getJSONObject(i).getDouble("AmountPaid"));
                }
                if (jsonArray.getJSONObject(i).getString("PaymentDateTime") != null) {
                    objOrderPaymentTran.setPaymentDateTime(jsonArray.getJSONObject(i).getString("PaymentDateTime"));
                }
                if (jsonArray.getJSONObject(i).getString("Remark") != null) {
                    objOrderPaymentTran.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                }
                objOrderPaymentTran.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                if (jsonArray.getJSONObject(i).getString("CreateDateTime") != null) {
                    objOrderPaymentTran.setCreateDateTime(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                }
                if (jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy") > 0) {
                    objOrderPaymentTran.setLinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                }
                if (jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy") > 0) {
                    objOrderPaymentTran.setLinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                if (jsonArray.getJSONObject(i).getString("UpdateDateTime") != null) {
                    objOrderPaymentTran.setUpdateDateTime(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                }
                if (jsonArray.getJSONObject(i).getString("PaymentType") != null) {
                    objOrderPaymentTran.setPaymentType(jsonArray.getJSONObject(i).getString("PaymentType"));
                }
                if (jsonArray.getJSONObject(i).getDouble("totalAmount") > 0) {
                    objOrderPaymentTran.setTotalAmount(jsonArray.getJSONObject(i).getDouble("totalAmount"));
                }
                lstobjOrderPaymentTran.add(objOrderPaymentTran);
            }
            return lstobjOrderPaymentTran;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SelectDailySaleDateWise(final Context context, String fromDate,String toDate, String linktoBusinessMasterId) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("") && fromDate != null && !fromDate.equals("")&& toDate != null && !toDate.equals("")) {
                dt = sdfControlDateFormat.parse(fromDate);
                dt1=sdfControlDateFormat.parse(toDate);
                url = Service.Url + this.SelectAllDailySaleDateWise + "/" + linktoBusinessMasterId + "/" + sdfDateFormat.format(dt)+ "/" + sdfDateFormat.format(dt1);
            } else {
                url = Service.Url + this.SelectAllDailySaleDateWise + "/" + 1 + "/" + null+"/"+null;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonResponse = jsonObject.getJSONArray(SelectAllDailySaleDateWise + "Result");
                            if (jsonResponse != null) {
                                objSalesReportListener = (SalesReportListener) context;
                                objSalesReportListener.SetDailySalesReport(SetListPropertiesFromJSONArray(jsonResponse));
                            }
                        }
                    } catch (Exception e) {
                        objSalesReportListener = (SalesReportListener) context;
                        objSalesReportListener.SetDailySalesReport(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objSalesReportListener = (SalesReportListener) context;
                    objSalesReportListener.SetDailySalesReport(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objSalesReportListener = (SalesReportListener) context;
            objSalesReportListener.SetDailySalesReport(null);
            e.printStackTrace();
        }
    }

    public interface SalesReportListener{
        void SetDailySalesReport(ArrayList<OrderPaymentTran> lstOrderPaymentTran);
    }
}
