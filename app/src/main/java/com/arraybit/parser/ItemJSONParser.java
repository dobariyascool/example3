package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;

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

public class ItemJSONParser {

    public String SelectAllItemMaster = "SelectAllItemMasterIdByCategoryMasterId";
    public String SelectAllOrderMasterOrderItemByStatus = "SelectAllOrderMasterWithOrderItemByStatus";
    public String UpdateOrderMasterStatus = "UpdateOrderMasterStatus";
    Date fromDate = null;
    Date toDate = null;
    Date dt = null;
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    ItemMasterRequestListener objItemMasterRequestListener;
    OrderMasterRequestListener objOrderMasterRequestListener;

    //region Class Methods
    private ItemMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ItemMaster objItemMaster = null;
        try {
            if (jsonObject != null) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonObject.getInt("ItemMasterId"));
                objItemMaster.setShortName(jsonObject.getString("ShortName"));
                objItemMaster.setItemName(jsonObject.getString("ItemName"));
                objItemMaster.setItemCode(jsonObject.getString("ItemCode"));
                objItemMaster.setBarCode(jsonObject.getString("BarCode"));
                objItemMaster.setShortDescription(jsonObject.getString("ShortDescription"));
                if (!jsonObject.getString("linktoUnitMasterId").equals("null")) {
                    objItemMaster.setlinktoUnitMasterId((short) jsonObject.getInt("linktoUnitMasterId"));
                }
                objItemMaster.setlinktoCategoryMasterId((short) jsonObject.getInt("linktoCategoryMasterId"));
                if (!jsonObject.getString("IsFavourite").equals("null")) {
                    objItemMaster.setIsFavourite(jsonObject.getBoolean("IsFavourite"));
                }
                if (!jsonObject.getString("ImagePhysicalName").equals("null")) {
                    objItemMaster.setXs_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                    objItemMaster.setSm_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                    objItemMaster.setLg_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                    objItemMaster.setXl_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                    objItemMaster.setMd_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                }
                objItemMaster.setItemPoint((short) jsonObject.getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonObject.getInt("PriceByPoint"));
                objItemMaster.setSearchWords(jsonObject.getString("SearchWords"));
                objItemMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                objItemMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objItemMaster.setIsDineInOnly(jsonObject.getBoolean("IsDineInOnly"));
                objItemMaster.setItemType((short) jsonObject.getInt("ItemType"));
                fromDate = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(fromDate));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setRate(jsonObject.getDouble("Rate"));
                objItemMaster.setMRP(jsonObject.getDouble("MRP"));
                objItemMaster.setTaxRate(jsonObject.getDouble("TaxRate"));
                /// Extra
                objItemMaster.setUnit(jsonObject.getString("Unit"));
                objItemMaster.setCategory(jsonObject.getString("Category"));
                objItemMaster.setBusiness(jsonObject.getString("Business"));
                objItemMaster.setUserCreatedBy(jsonObject.getString("UserCreatedBy"));
                objItemMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
                objItemMaster.setLinktoItemMasterIdModifiers(jsonObject.getString("linktoItemMasterIdModifiers"));
                objItemMaster.setLinktoOptionMasterIds(jsonObject.getString("linktoOptionMasterIds"));
                objItemMaster.setIsChecked((short) -1);
                objItemMaster.setIsDeleted(false);
            }
            return objItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<ItemMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemMaster> lstItemMaster = new ArrayList<>();
        ItemMaster objItemMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("ItemMasterId"));
                objItemMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("ItemName"));
                objItemMaster.setItemCode(jsonArray.getJSONObject(i).getString("ItemCode"));
                objItemMaster.setBarCode(jsonArray.getJSONObject(i).getString("BarCode"));
                objItemMaster.setShortDescription(jsonArray.getJSONObject(i).getString("ShortDescription"));
                if (!jsonArray.getJSONObject(i).getString("linktoUnitMasterId").equals("null")) {
                    objItemMaster.setlinktoUnitMasterId((short) jsonArray.getJSONObject(i).getInt("linktoUnitMasterId"));
                }
                objItemMaster.setlinktoCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCategoryMasterId"));
                if (!jsonArray.getJSONObject(i).getString("IsFavourite").equals("null")) {
                    objItemMaster.setIsFavourite(jsonArray.getJSONObject(i).getBoolean("IsFavourite"));
                }
                if (!jsonArray.getJSONObject(i).getString("ImagePhysicalName").equals("null")) {
                    objItemMaster.setXs_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                    objItemMaster.setSm_ImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                    objItemMaster.setLg_ImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                    objItemMaster.setXl_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
                    objItemMaster.setMd_ImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                }
                objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonArray.getJSONObject(i).getInt("PriceByPoint"));
                objItemMaster.setSearchWords(jsonArray.getJSONObject(i).getString("SearchWords"));
                objItemMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objItemMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objItemMaster.setIsDineInOnly(jsonArray.getJSONObject(i).getBoolean("IsDineInOnly"));
                objItemMaster.setItemType((short) jsonArray.getJSONObject(i).getInt("ItemType"));
//                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
//                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                objItemMaster.setMRP(jsonArray.getJSONObject(i).getDouble("MRP"));
                objItemMaster.setTax(jsonArray.getJSONObject(i).getString("Tax"));
                objItemMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));

                /// Extra
                objItemMaster.setUnit(jsonArray.getJSONObject(i).getString("Unit"));
                objItemMaster.setCategory(jsonArray.getJSONObject(i).getString("Category"));
                objItemMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                objItemMaster.setUserCreatedBy(jsonArray.getJSONObject(i).getString("UserCreatedBy"));
                objItemMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
                objItemMaster.setLinktoItemMasterIdModifiers(jsonArray.getJSONObject(i).getString("linktoItemMasterIdModifiers"));
                objItemMaster.setLinktoOptionMasterIds(jsonArray.getJSONObject(i).getString("linktoOptionMasterIds"));
                objItemMaster.setIsChecked((short) -1);
                objItemMaster.setIsDeleted(false);
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //endregion

    public void UpdateOrderMasterStatus(String orderMasterId, final Context context) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderMasterId").value(orderMasterId);
            stringer.key("linktoOrderStatusMasterId").value(Globals.OrderStatus.Cancelled.getValue());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateOrderMasterStatus;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateOrderMasterStatus + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorNumber"));
                            objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                            objOrderMasterRequestListener.OrderMasterResponse(errorCode, null);
                        } else {
                            objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                        objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                    objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);

        } catch (Exception ex) {
            objOrderMasterRequestListener = (OrderMasterRequestListener) context;
            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
        }
    }

    public void SelectAllOrderMasterOrderItem(final Context context, String currentPage, String linktoBusinessMasterId, boolean IsCancelled) {
        try {
            Calendar cal = Calendar.getInstance();
            fromDate = cal.getTime();
            // this does work, increment 10 days
            cal.add(Calendar.DATE, 7);
            toDate = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = Service.Url + this.SelectAllOrderMasterOrderItemByStatus + "/" + currentPage + "/" + linktoBusinessMasterId + "/" + String.valueOf(IsCancelled) + "/" + sdfDateFormat.format(fromDate) + "/" + sdfDateFormat.format(toDate);
        Log.e("url", " " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Date dt;
                try {
                    Log.e("jsonObject", " " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllOrderMasterOrderItemByStatus + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ItemMaster objItemMaster = new ItemMaster();
                            objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("linktoItemMasterId"));
                            objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("Item"));
                            objItemMaster.setItemCode(jsonArray.getJSONObject(i).getString("ItemCode"));
                            objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                            objItemMaster.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                            objItemMaster.setSellPrice(jsonArray.getJSONObject(i).getDouble("TotalRate"));
                            objItemMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                            objItemMaster.setQuantity(jsonArray.getJSONObject(i).getInt("Quantity"));
                            objItemMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                            objItemMaster.setRemark(jsonArray.getJSONObject(i).getString("ItemRemark"));
                            objItemMaster.setLinktoItemMasterIdModifiers(jsonArray.getJSONObject(i).getString("linktoItemMasterModifierId"));
                            objItemMaster.setLinktoOrderMasterId(jsonArray.getJSONObject(i).getInt("linktoOrderMasterId"));
                            objItemMaster.setLinktoOrderItemTranId(jsonArray.getJSONObject(i).getInt("OrderItemTranId"));
                            objItemMaster.setOrderNumber(jsonArray.getJSONObject(i).getString("OrderNumber"));
                            objItemMaster.setPaymentStatus(jsonArray.getJSONObject(i).getBoolean("PaymentStatus"));
                            dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("OrderDateTime"));
                            objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt) + "T" + sdfControlTimeFormat.format(dt));
                            if (!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")) {
                                objItemMaster.setLinktoOrderStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                            }
                            alItemMaster.add(objItemMaster);
                        }
                        objItemMasterRequestListener = (ItemMasterRequestListener) context;
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) context;
                    objItemMasterRequestListener.ItemMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) context;
                objItemMasterRequestListener.ItemMasterResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public void SelectAllItemMaster(final Fragment targetFragment, final Context context, String categoryMasterId, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllItemMaster + "/" + categoryMasterId + "/" + linktoBusinessMasterId ;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllItemMaster + "Result");
                    if (jsonArray != null) {
                        objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                        objItemMasterRequestListener.ItemMasterResponse(SetListPropertiesFromJSONArray(jsonArray));
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                objItemMasterRequestListener.ItemMasterResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public interface OrderMasterRequestListener {
        void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster);
    }

    public interface ItemMasterRequestListener {
        void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster);
    }
}