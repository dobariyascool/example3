package com.arraybit.parser;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BookingMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingJSONParser {
    public String UpdateBookingMaster = "UpdateBookingMasterStatus";
    public String SelectAllBookingMasterByStatus = "SelectAllBookingMasterByStatus";

    BookingRequestListener objBookingRequestListener;

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat DisplayTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);
    Date dt = null;
    Date fromDate = null;
    Date toDate = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    private BookingMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BookingMaster objBookingMaster = null;
        try {
            if (jsonObject != null) {
                objBookingMaster = new BookingMaster();
                objBookingMaster.setBookingMasterId(jsonObject.getInt("BookingMasterId"));
                dt = sdfDateFormat.parse(jsonObject.getString("FromDate"));
                objBookingMaster.setFromDate(sdfControlDateFormat.format(dt));
                dt = sdfDateFormat.parse(jsonObject.getString("ToDate"));
                objBookingMaster.setToDate(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("FromTime"));
                    objBookingMaster.setFromTime(DisplayTimeFormat.format(dt));
                }
                if (!jsonObject.getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("ToTime"));
                    objBookingMaster.setToTime(DisplayTimeFormat.format(dt));
                }
                if (!jsonObject.getString("IsHourly").equals("null")) {
                    objBookingMaster.setIsHourly(jsonObject.getBoolean("IsHourly"));
                }
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objBookingMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                objBookingMaster.setBookingPersonName(jsonObject.getString("BookingPersonName"));
                objBookingMaster.setEmail(jsonObject.getString("Email"));
                objBookingMaster.setPhone(jsonObject.getString("Phone"));
                objBookingMaster.setNoOfAdults((short) jsonObject.getInt("NoOfAdults"));
                objBookingMaster.setNoOfChildren((short) jsonObject.getInt("NoOfChildren"));
                objBookingMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objBookingMaster.setDiscountPercentage((short) jsonObject.getInt("DiscountPercentage"));
                objBookingMaster.setDiscountAmount(jsonObject.getDouble("DiscountAmount"));
                objBookingMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objBookingMaster.setNetAmount(jsonObject.getDouble("NetAmount"));
                objBookingMaster.setPaidAmount(jsonObject.getDouble("PaidAmount"));
                objBookingMaster.setBalanceAmount(jsonObject.getDouble("BalanceAmount"));
                objBookingMaster.setRemark(jsonObject.getString("Remark"));
                if (!jsonObject.getString("IsPreOrder").equals("null")) {
                    objBookingMaster.setIsPreOrder(jsonObject.getBoolean("IsPreOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objBookingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objBookingMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBookingMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objBookingMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objBookingMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                if (!jsonObject.getString("BookingStatus").equals("null")) {
                    objBookingMaster.setBookingStatus((short) jsonObject.getInt("BookingStatus"));
                }

                /// Extra
//                objBookingMaster.setUserCreatedBy(jsonObject.getString("UserCreatedBy"));
//                objBookingMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
//                objBookingMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objBookingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BookingMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BookingMaster> lstBookingMaster = new ArrayList<>();
        BookingMaster objBookingMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBookingMaster = new BookingMaster();
                objBookingMaster.setBookingMasterId(jsonArray.getJSONObject(i).getInt("BookingMasterId"));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("FromDate"));
                objBookingMaster.setFromDate(sdfControlDateFormat.format(dt));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("ToDate"));
                objBookingMaster.setToDate(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("FromTime"));
                    objBookingMaster.setFromTime(DisplayTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ToTime"));
                    objBookingMaster.setToTime(DisplayTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("IsHourly").equals("null")) {
                    objBookingMaster.setIsHourly(jsonArray.getJSONObject(i).getBoolean("IsHourly"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objBookingMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objBookingMaster.setBookingPersonName(jsonArray.getJSONObject(i).getString("BookingPersonName"));
                objBookingMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objBookingMaster.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                objBookingMaster.setNoOfAdults((short) jsonArray.getJSONObject(i).getInt("NoOfAdults"));
                objBookingMaster.setNoOfChildren((short) jsonArray.getJSONObject(i).getInt("NoOfChildren"));
                objBookingMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objBookingMaster.setDiscountPercentage((short) jsonArray.getJSONObject(i).getInt("DiscountPercentage"));
                objBookingMaster.setDiscountAmount(jsonArray.getJSONObject(i).getDouble("DiscountAmount"));
                objBookingMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objBookingMaster.setNetAmount(jsonArray.getJSONObject(i).getDouble("NetAmount"));
                objBookingMaster.setPaidAmount(jsonArray.getJSONObject(i).getDouble("PaidAmount"));
                objBookingMaster.setBalanceAmount(jsonArray.getJSONObject(i).getDouble("BalanceAmount"));
                objBookingMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                if (!jsonArray.getJSONObject(i).getString("IsPreOrder").equals("null")) {
                    objBookingMaster.setIsPreOrder(jsonArray.getJSONObject(i).getBoolean("IsPreOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objBookingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objBookingMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objBookingMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBookingMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objBookingMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objBookingMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                if (!jsonArray.getJSONObject(i).getString("BookingStatus").equals("null")) {
                    objBookingMaster.setBookingStatus((short) jsonArray.getJSONObject(i).getInt("BookingStatus"));
                }

                /// Extra
//                objBookingMaster.setUserCreatedBy(jsonArray.getJSONObject(i).getString("UserCreatedBy"));
//                objBookingMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
//                objBookingMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBookingMaster.add(objBookingMaster);
            }
            return lstBookingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //region Update
    public void UpdateBookingMaster(BookingMaster objBookingMaster, final Context context) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("bookingMaster");
            stringer.object();

            stringer.key("BookingMasterId").value(objBookingMaster.getBookingMasterId());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateBookingMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateBookingMaster + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objBookingRequestListener = (BookingRequestListener) context;
                            objBookingRequestListener.BookingResponse(errorCode, null);
                        } else {
                            objBookingRequestListener = (BookingRequestListener) context;
                            objBookingRequestListener.BookingResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objBookingRequestListener = (BookingRequestListener) context;
                        objBookingRequestListener.BookingResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objBookingRequestListener = (BookingRequestListener) context;
                    objBookingRequestListener.BookingResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);

        } catch (Exception ex) {
            objBookingRequestListener = (BookingRequestListener) context;
            objBookingRequestListener.BookingResponse("-1", null);
        }
    }
    //endregion

    //region SelectAll
    public void SelectAllBookingMaster(final Context context, String currentPage, String linktoBusinessMasterId, boolean IsCancelled) {
        try {
            Calendar cal = Calendar.getInstance();
            fromDate = cal.getTime();
            // this does work, increment 30 days
            cal.add(Calendar.DATE,30);
            toDate = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = Service.Url + this.SelectAllBookingMasterByStatus + "/" + currentPage + "/" + linktoBusinessMasterId + "/" + String.valueOf(IsCancelled) + "/" + sdfDateFormat.format(fromDate) + "/" + sdfDateFormat.format(toDate);
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.e("url", " " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("jsonObjectbooking", " " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllBookingMasterByStatus + "Result");
                    if (jsonArray != null) {
                        ArrayList<BookingMaster> alBookingMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objBookingRequestListener = (BookingRequestListener) context;
                        objBookingRequestListener.BookingResponse(null, alBookingMaster);
                    }
                } catch (Exception e) {
                    objBookingRequestListener = (BookingRequestListener) context;
                    objBookingRequestListener.BookingResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBookingRequestListener = (BookingRequestListener) context;
                objBookingRequestListener.BookingResponse(null, null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    //endregion

    public interface BookingRequestListener {
        void BookingResponse(String errorCode, ArrayList<BookingMaster> alBookingMaster);

    }
}


