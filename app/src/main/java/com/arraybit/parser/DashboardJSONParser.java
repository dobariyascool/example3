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
import com.arraybit.modal.Dashboard;
import com.arraybit.modal.OrderMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardJSONParser {

    public String SelectDashBoardCounter = "SelectDashBoardCounter";
    public String SelectOrderMasterDailyWeeklyMonthlyOrders = "SelectOrderMasterDailyWeeklyMonthlyOrders";
    public String SelectOrderMasterLeastSellingDayOfLastWeek = "SelectOrderMasterLeastSellingDayOfLastWeek";

    Date dt = null;
    Date dt1 = null;
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    DashBoardListener objDashBoardListener;
    LastDayListener objLastDayListener;

    private Dashboard SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        Dashboard objDashboard;
        try {
            if (jsonObject != null) {
                objDashboard = new Dashboard();
                objDashboard.setLinktoBusinessMasterId(jsonObject.getInt("linktoBusinessMasterId"));
                objDashboard.setRegisteredUserBooking(jsonObject.getInt("RegisteredUserBooking"));
                objDashboard.setCanceledOrder(jsonObject.getInt("CanceledOrder"));
                objDashboard.setCanceledBooking(jsonObject.getInt("CanceledBooking"));
                objDashboard.setPreOrder(jsonObject.getInt("PreOrder"));
                return objDashboard;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private OrderMaster SetClassOrderMasterPropertiesFromJSONArray(JSONObject jsonObject) {

        OrderMaster objOrderMaster;
        try {
            objOrderMaster = new OrderMaster();
            objOrderMaster.setOrderMasterId(jsonObject.getLong("OrderMasterId"));
            objOrderMaster.setOrderNumber(jsonObject.getString("OrderNumber"));
            dt = sdfDateTimeFormat.parse(jsonObject.getString("OrderDateTime"));
            objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
            objOrderMaster.setlinktoTableMasterIds(jsonObject.getString("linktoTableMasterIds"));
            if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                objOrderMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
            }
            objOrderMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
            if (!jsonObject.getString("linktoOrderStatusMasterId").equals("null")) {
                objOrderMaster.setlinktoOrderStatusMasterId((short) jsonObject.getInt("linktoOrderStatusMasterId"));
            }
            if (!jsonObject.getString("linktoBookingMasterId").equals("null")) {
                objOrderMaster.setlinktoBookingMasterId(jsonObject.getInt("linktoBookingMasterId"));
            }
            objOrderMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
            objOrderMaster.setTotalTax(jsonObject.getDouble("TotalTax"));
            objOrderMaster.setDiscount(jsonObject.getDouble("Discount"));
            objOrderMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
            objOrderMaster.setNetAmount(jsonObject.getDouble("NetAmount"));
            objOrderMaster.setPaidAmount(jsonObject.getDouble("PaidAmount"));
            objOrderMaster.setBalanceAmount(jsonObject.getDouble("BalanceAmount"));
            objOrderMaster.setTotalItemPoint((short) jsonObject.getInt("TotalItemPoint"));
            objOrderMaster.setTotalDeductedPoint((short) jsonObject.getInt("TotalDeductedPoint"));
            objOrderMaster.setRemark(jsonObject.getString("Remark"));
            objOrderMaster.setIsPreOrder(jsonObject.getBoolean("IsPreOrder"));
            if (!jsonObject.getString("linktoCustomerAddressTranId").equals("null")) {
                objOrderMaster.setlinktoCustomerAddressTranId(jsonObject.getInt("linktoCustomerAddressTranId"));
            }
            if (!jsonObject.getString("linktoSalesMasterId").equals("null")) {
                objOrderMaster.setlinktoSalesMasterId(jsonObject.getLong("linktoSalesMasterId"));
            }
            if (!jsonObject.getString("linktoOfferMasterId").equals("null")) {
                objOrderMaster.setlinktoOfferMasterId(jsonObject.getInt("linktoOfferMasterId"));
            }
            objOrderMaster.setOfferCode(jsonObject.getString("OfferCode"));
            dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
            objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
            if (!jsonObject.getString("UpdateDateTime").equals("null")) {
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
            }
            /// Extra

            objOrderMaster.setOrderType(jsonObject.getString("OrderType"));
            objOrderMaster.setOrderStatus(jsonObject.getString("OrderStatus"));
            if (!jsonObject.getString("Year").equals("null")) {
                objOrderMaster.setYear(jsonObject.getString("Year"));
            }
            if (!jsonObject.getString("Month").equals("null")) {
                objOrderMaster.setMonth(jsonObject.getString("Month"));
            }
            if (!jsonObject.getString("LeastSellingDayName").equals("null")) {
                objOrderMaster.setLeastSellingDayName(jsonObject.getString("LeastSellingDayName"));
            }
            if (!jsonObject.getString("OrderToDateTime").equals("null")) {
                objOrderMaster.setOrderToDateTime(jsonObject.getString("OrderToDateTime"));
            }

            return objOrderMaster;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SelectDashBoardCounter(final Context context, final Fragment targetFragment, String linktoBusinessMasterId) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectDashBoardCounter + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectDashBoardCounter + "/" + 1;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectDashBoardCounter + "Result");
                            if (jsonResponse != null) {
                                objDashBoardListener = (DashBoardListener) targetFragment;
                                objDashBoardListener.DashboardResponse(null, SetClassPropertiesFromJSONObject(jsonResponse));
                            }
                        }
                    } catch (Exception e) {
                        objDashBoardListener = (DashBoardListener) targetFragment;
                        objDashBoardListener.DashboardResponse(null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objDashBoardListener = (DashBoardListener) targetFragment;
                    objDashBoardListener.DashboardResponse(null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objDashBoardListener = (DashBoardListener) targetFragment;
            objDashBoardListener.DashboardResponse(null, null);
        }

    }

    public void SelectOrderMasterDailyWeeklyMonthlyOrders(final Context context, final Fragment targetFragment, String linktoBusinessMasterId, String linktoOrderStatusMasterId) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                url = Service.Url + this.SelectOrderMasterDailyWeeklyMonthlyOrders + "/" + linktoBusinessMasterId;
            } else {
                url = Service.Url + this.SelectOrderMasterDailyWeeklyMonthlyOrders + "/" + 1;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectOrderMasterDailyWeeklyMonthlyOrders + "Result");
                            if (jsonResponse != null) {
                                objDashBoardListener = (DashBoardListener) targetFragment;
                                objDashBoardListener.DashBoardSales(String.valueOf(jsonResponse.getDouble("DailyOrder")), String.valueOf(jsonResponse.getDouble("WeeklyOrder")), String.valueOf(jsonResponse.getDouble("MonthlyOrder")));
                            }
                        }
                    } catch (Exception e) {
                        objDashBoardListener = (DashBoardListener) targetFragment;
                        objDashBoardListener.DashBoardSales("0", "0", "0");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objDashBoardListener = (DashBoardListener) targetFragment;
                    objDashBoardListener.DashBoardSales("0", "0", "0");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objDashBoardListener = (DashBoardListener) targetFragment;
            objDashBoardListener.DashBoardSales("0", "0", "0");
        }

    }

    public void SelectOrderMasterLeastSellingDayOfLastWeek(final Context context, final Fragment targetFragment, String linktoBusinessMasterId, String fromDate, String toDate, boolean isDateRange) {
        String url;
        try {

            if (isDateRange) {
                if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
                    dt = sdfControlDateFormat.parse(fromDate);
                    dt1 = sdfControlDateFormat.parse(toDate);
                }
            }
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("")) {
                if (isDateRange) {
                    if (dt != null && dt1 != null) {
                        url = Service.Url + this.SelectOrderMasterLeastSellingDayOfLastWeek + "/" + linktoBusinessMasterId + "/" + sdfDateFormat.format(dt) + "/" + sdfDateFormat.format(dt1) + "/" + String.valueOf(isDateRange);
                    } else {
                        url = Service.Url + this.SelectOrderMasterLeastSellingDayOfLastWeek + "/" + linktoBusinessMasterId + "/" + null + "/" + null + "/" + String.valueOf(false);
                    }
                } else {
                    url = Service.Url + this.SelectOrderMasterLeastSellingDayOfLastWeek + "/" + linktoBusinessMasterId + "/" + null + "/" + null + "/" + String.valueOf(isDateRange);
                }
            } else {
                url = Service.Url + this.SelectOrderMasterLeastSellingDayOfLastWeek + "/" + 1 + "/" + null + "/" + null + "/" + String.valueOf(false);
            }

            if (targetFragment != null) {
                objLastDayListener = (LastDayListener) targetFragment;
            } else {
                objLastDayListener = (LastDayListener) context;
            }

            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectOrderMasterLeastSellingDayOfLastWeek + "Result");
                            if (jsonResponse != null) {
//                                objLastDayListener = (LastDayListener) targetFragment;
                                OrderMaster orderMaster = new OrderMaster();
                                orderMaster.setLeastSellingDayName(jsonResponse.getString("LeastSellingDayName"));
                                orderMaster.setOrderToDateTime(jsonResponse.getString("OrderDateTimeString"));
                                orderMaster.setNetAmount(jsonResponse.getDouble("NetAmount"));
                                objLastDayListener.LastDayOfWeek(orderMaster);
                            }
                        }
                    } catch (Exception e) {
//                        objLastDayListener = (LastDayListener) targetFragment;
                        objLastDayListener.LastDayOfWeek(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
//                    objLastDayListener = (LastDayListener) targetFragment;
                    objLastDayListener.LastDayOfWeek(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
//            objLastDayListener = (LastDayListener) targetFragment;
            objLastDayListener.LastDayOfWeek(null);
        }

    }

    public interface DashBoardListener {
        void DashboardResponse(String errorCode, Dashboard objDashboard);

        void DashBoardSales(String dailySales, String weeklySales, String monthlySales);
    }

    public interface LastDayListener {
        void LastDayOfWeek(OrderMaster orderMaster);
    }
}
