package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.modal.OrderMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChartDataJSONParser {

    public String SelectAllOrderMasterOrderTypeWiseChart = "SelectAllOrderMasterOrderTypeWiseChart";
    public String SelectAllOrderItemTranMostOrderedItemsChart = "SelectAllOrderItemTranMostOrderedItemsChart";
    public String SelectAllOrderItemTranLeastOrderedItemsChart = "SelectAllOrderItemTranLeastOrderedItemsChart";
    public String SelectAllOrderMasterYearlyRevenueChart = "SelectAllOrderMasterYearlyRevenueChart";
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    ChartDataListener objChartDataListener;

    //region Class Methods

    private ArrayList<OrderMaster> SetListOrderMasterPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderMaster> lstOrderMaster = new ArrayList<>();
        OrderMaster objOrderMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderMaster = new OrderMaster();
                objOrderMaster.setOrderMasterId(jsonArray.getJSONObject(i).getLong("OrderMasterId"));
                objOrderMaster.setOrderNumber(jsonArray.getJSONObject(i).getString("OrderNumber"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("OrderDateTime"));
                objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoTableMasterIds(jsonArray.getJSONObject(i).getString("linktoTableMasterIds"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoBookingMasterId").equals("null")) {
                    objOrderMaster.setlinktoBookingMasterId(jsonArray.getJSONObject(i).getInt("linktoBookingMasterId"));
                }
                objOrderMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOrderMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objOrderMaster.setNetAmount(jsonArray.getJSONObject(i).getDouble("NetAmount"));
                objOrderMaster.setPaidAmount(jsonArray.getJSONObject(i).getDouble("PaidAmount"));
                objOrderMaster.setBalanceAmount(jsonArray.getJSONObject(i).getDouble("BalanceAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                objOrderMaster.setIsPreOrder(jsonArray.getJSONObject(i).getBoolean("IsPreOrder"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerAddressTranId").equals("null")) {
                    objOrderMaster.setlinktoCustomerAddressTranId(jsonArray.getJSONObject(i).getInt("linktoCustomerAddressTranId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonArray.getJSONObject(i).getLong("linktoSalesMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoOfferMasterId").equals("null")) {
                    objOrderMaster.setlinktoOfferMasterId(jsonArray.getJSONObject(i).getInt("linktoOfferMasterId"));
                }
                objOrderMaster.setOfferCode(jsonArray.getJSONObject(i).getString("OfferCode"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                /// Extra

                objOrderMaster.setOrderType(jsonArray.getJSONObject(i).getString("OrderType"));
                objOrderMaster.setOrderStatus(jsonArray.getJSONObject(i).getString("OrderStatus"));
                if (!jsonArray.getJSONObject(i).getString("Year").equals("null")) {
                    objOrderMaster.setYear(jsonArray.getJSONObject(i).getString("Year"));
                }
                if (!jsonArray.getJSONObject(i).getString("Month").equals("null")) {
                    objOrderMaster.setMonth(jsonArray.getJSONObject(i).getString("Month"));
                }
 if (!jsonArray.getJSONObject(i).getString("LeastSellingDayName").equals("null")) {
                    objOrderMaster.setMonth(jsonArray.getJSONObject(i).getString("LeastSellingDayName"));
                }

                lstOrderMaster.add(objOrderMaster);
            }
            return lstOrderMaster;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

   private ArrayList<OrderItemTran> SetListOrderItemTranPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderItemTran> lstOrderItemTran = new ArrayList<>();
        OrderItemTran objOrderItemTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderItemTran = new OrderItemTran();
                objOrderItemTran.setOrderItemTranId(jsonArray.getJSONObject(i).getLong("OrderItemTranId"));
                objOrderItemTran.setlinktoOrderMasterId(jsonArray.getJSONObject(i).getLong("linktoOrderMasterId"));
                objOrderItemTran.setlinktoItemMasterId(jsonArray.getJSONObject(i).getInt("linktoItemMasterId"));
                objOrderItemTran.setQuantity((short) jsonArray.getJSONObject(i).getInt("Quantity"));
                objOrderItemTran.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                objOrderItemTran.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objOrderItemTran.setDeductedPoint((short) jsonArray.getJSONObject(i).getInt("DeductedPoint"));
                objOrderItemTran.setItemRemark(jsonArray.getJSONObject(i).getString("ItemRemark"));
                if(!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")){
                    objOrderItemTran.setlinktoOrderStatusMasterId(Short.valueOf(jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId")));
                }
                if(!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objOrderItemTran.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                /// Extra
                objOrderItemTran.setItem(jsonArray.getJSONObject(i).getString("Item"));
                objOrderItemTran.setTotalItemQuantity(jsonArray.getJSONObject(i).getInt("TotalItemQuantity"));
                lstOrderItemTran.add(objOrderItemTran);
            }
            return lstOrderItemTran;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //endregion

    // order type wise
    public void SelectAllOrderMasterOrderTypeWiseChart(final Context context, String linktoBusinessMasterId,final Fragment targetFragment) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectAllOrderMasterOrderTypeWiseChart + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectAllOrderMasterOrderTypeWiseChart + "/" + 1 ;
            }
            Log.e("url"," "+url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonArray= jsonObject.getJSONArray(SelectAllOrderMasterOrderTypeWiseChart + "Result");
                            if (jsonArray != null) {
                                objChartDataListener = (ChartDataListener) targetFragment;
                                objChartDataListener.OrderTypeWise(SetListOrderMasterPropertiesFromJSONArray(jsonArray));
                            }
                            else
                            {
                                objChartDataListener =(ChartDataListener) targetFragment;
                                objChartDataListener.OrderTypeWise(null);
                            }
                        }
                    } catch (Exception e) {
                        objChartDataListener =(ChartDataListener) targetFragment;
                        objChartDataListener.OrderTypeWise(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objChartDataListener = (ChartDataListener) targetFragment;
                    objChartDataListener.OrderTypeWise(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objChartDataListener =(ChartDataListener) targetFragment;
            objChartDataListener.OrderTypeWise(null);
        }

    }

    //yearly sales
    public void SelectAllOrderMasterYearlyRevenueChart(final Context context, String linktoBusinessMasterId,final Fragment targetFragment) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectAllOrderMasterYearlyRevenueChart + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectAllOrderMasterYearlyRevenueChart + "/" + 1 ;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonArray= jsonObject.getJSONArray(SelectAllOrderMasterYearlyRevenueChart + "Result");
                            if (jsonArray != null) {
                                objChartDataListener = (ChartDataListener) targetFragment;
                                objChartDataListener.YearlySales(SetListOrderMasterPropertiesFromJSONArray(jsonArray));
                            }
                            else
                            {
                                objChartDataListener =(ChartDataListener) targetFragment;
                                objChartDataListener.YearlySales(null);
                            }
                        }
                    } catch (Exception e) {
                        objChartDataListener =(ChartDataListener) targetFragment;
                        objChartDataListener.YearlySales(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objChartDataListener = (ChartDataListener) targetFragment;
                    objChartDataListener.YearlySales(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objChartDataListener =(ChartDataListener) targetFragment;
            objChartDataListener.YearlySales(null);
        }

    }

    // most selling item
    public void SelectAllOrderItemTranMostOrderedItemsChart(final Context context, String linktoBusinessMasterId,final Fragment targetFragment, final boolean isMost) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectAllOrderItemTranMostOrderedItemsChart + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectAllOrderItemTranMostOrderedItemsChart + "/" + 1 ;
            }
            Log.e("url"," "+url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonArray= jsonObject.getJSONArray(SelectAllOrderItemTranMostOrderedItemsChart + "Result");
                            if (jsonArray != null) {
                                objChartDataListener = (ChartDataListener) targetFragment;
                                objChartDataListener.MostSellingItem(SetListOrderItemTranPropertiesFromJSONArray(jsonArray),isMost);
                            }
                            else
                            {
                                objChartDataListener =(ChartDataListener) targetFragment;
                                objChartDataListener.MostSellingItem(null,isMost);
                            }
                        }
                    } catch (Exception e) {
                        objChartDataListener =(ChartDataListener) targetFragment;
                        objChartDataListener.MostSellingItem(null,isMost);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objChartDataListener = (ChartDataListener) targetFragment;
                    objChartDataListener.MostSellingItem(null,isMost);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objChartDataListener =(ChartDataListener) targetFragment;
            objChartDataListener.MostSellingItem(null,isMost);
        }

    }

  // least selling item
    public void SelectAllOrderItemTranLeastOrderedItemsChart(final Context context, String linktoBusinessMasterId,final Fragment targetFragment, final boolean isMost) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectAllOrderItemTranLeastOrderedItemsChart + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectAllOrderItemTranLeastOrderedItemsChart + "/" + 1 ;
            }
            Log.e("url"," "+url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonArray= jsonObject.getJSONArray(SelectAllOrderItemTranLeastOrderedItemsChart + "Result");
                            if (jsonArray != null) {
                                objChartDataListener = (ChartDataListener) targetFragment;
                                objChartDataListener.MostSellingItem(SetListOrderItemTranPropertiesFromJSONArray(jsonArray),isMost);
                            }
                            else
                            {
                                objChartDataListener =(ChartDataListener) targetFragment;
                                objChartDataListener.MostSellingItem(null,isMost);
                            }
                        }
                    } catch (Exception e) {
                        objChartDataListener =(ChartDataListener) targetFragment;
                        objChartDataListener.MostSellingItem(null,isMost);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objChartDataListener = (ChartDataListener) targetFragment;
                    objChartDataListener.MostSellingItem(null,isMost);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objChartDataListener =(ChartDataListener) targetFragment;
            objChartDataListener.MostSellingItem(null,isMost);
        }

    }

    public interface ChartDataListener {
        void OrderTypeWise(ArrayList<OrderMaster> lstOrderMaster);
        void MostSellingItem(ArrayList<OrderItemTran> lstOrderItemTran, boolean isMost);
        void YearlySales(ArrayList<OrderMaster> lstOrderMaster);
    }
}
