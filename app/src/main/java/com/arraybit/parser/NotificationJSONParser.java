package com.arraybit.parser;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.NotificationMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for NotificationMaster
/// </summary>
public class NotificationJSONParser {
    public String InsertNotificationMaster = "InsertNotificationMaster";
    public String UpdateNotificationMaster = "UpdateNotificationMaster";
    public String DeleteNotificationMaster = "DeleteNotificationMaster";
    public String SelectNotificationMaster = "SelectNotificationMaster";
    public String SelectAllNotificationMaster = "SelectAllNotificationMasterPageWise";
    public String SelectAllNotificationMasterNotificationMasterId = "SelectAllNotificationMasterNotificationMasterId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    NotificationRequestListener objNotificationRequestListener;
    NotificationAddListener objNotificationAddListener;

    private NotificationMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        NotificationMaster objNotificationMaster = null;
        try {
            if (jsonObject != null) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonObject.getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonObject.getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonObject.getString("NotificationText"));
                if (jsonObject.getString("NotificationImageName") != null && !jsonObject.getString("NotificationImageName").equals("") && !jsonObject.getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonObject.getString("NotificationImageName"));
                }
                if (!jsonObject.getString("Type").equals("null")) {
                    objNotificationMaster.setType((short)jsonObject.getInt("Type"));
                }
                if (!jsonObject.getString("ID").equals("null")) {
                    objNotificationMaster.setID(jsonObject.getInt("ID"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("NotificationDateTime"));
                objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
            }
            return objNotificationMaster;
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<NotificationMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<NotificationMaster> lstNotificationMaster = new ArrayList<>();
        NotificationMaster objNotificationMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonArray.getJSONObject(i).getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonArray.getJSONObject(i).getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonArray.getJSONObject(i).getString("NotificationText"));
                if (jsonArray.getJSONObject(i).getString("NotificationImageName") != null && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("") && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonArray.getJSONObject(i).getString("NotificationImageName"));
                }
                if (!jsonArray.getJSONObject(i).getString("Type").equals("null")) {
                    objNotificationMaster.setType((short)jsonArray.getJSONObject(i).getInt("Type"));
                }
                if (!jsonArray.getJSONObject(i).getString("ID").equals("null")) {
                    objNotificationMaster.setID(jsonArray.getJSONObject(i).getInt("ID"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("NotificationDateTime"));
                objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
                lstNotificationMaster.add(objNotificationMaster);
            }
            return lstNotificationMaster;
        } catch (Exception e) {
            return null;
        }
    }

    public void InsertNotificationMaster(NotificationMaster objNotificationMaster, Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("notificationMaster");
            stringer.object();

            stringer.key("NotificationTitle").value(objNotificationMaster.getNotificationTitle());
            stringer.key("NotificationText").value(objNotificationMaster.getNotificationText());
            stringer.key("NotificationImageNameBytes").value(objNotificationMaster.getNotificationImageNameBytes());
            stringer.key("NotificationImageName").value(objNotificationMaster.getNotificationImageName());
            stringer.key("NotificationDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objNotificationMaster.getlinktoUserMasterIdCreatedBy());
            stringer.key("linktoBusinessMasterId").value(objNotificationMaster.getLinktoBusinessMasterId());
            stringer.key("IsDeleted").value(objNotificationMaster.getIsDeleted());
            stringer.key("Type").value(objNotificationMaster.getType());
            stringer.key("ID").value(objNotificationMaster.getID());

            stringer.endObject();
            stringer.endObject();

            String url = Service.Url + this.InsertNotificationMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertNotificationMaster + "Result");
                        if (jsonResponse != null) {
                            objNotificationAddListener = (NotificationAddListener) targetFragment;
                            objNotificationAddListener.NotificationAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    } catch (JSONException e) {
                        objNotificationAddListener = (NotificationAddListener) targetFragment;
                        objNotificationAddListener.NotificationAddResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objNotificationAddListener = (NotificationAddListener) targetFragment;
                    objNotificationAddListener.NotificationAddResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objNotificationAddListener = (NotificationAddListener) targetFragment;
            objNotificationAddListener.NotificationAddResponse("-1", null);
        }
    }

    public String UpdateNotificationMaster(NotificationMaster objNotificationMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("notificationMaster");
            stringer.object();

            stringer.key("NotificationMasterId").value(objNotificationMaster.getNotificationMasterId());
            stringer.key("NotificationTitle").value(objNotificationMaster.getNotificationTitle());
            stringer.key("NotificationText").value(objNotificationMaster.getNotificationText());
            stringer.key("NotificationImageNameBytes").value(objNotificationMaster.getNotificationImageNameBytes());
            dt = sdfControlDateFormat.parse(objNotificationMaster.getNotificationDateTime());
            stringer.key("NotificationDateTime").value(sdfDateTimeFormat.format(dt));
            dt = sdfControlDateFormat.parse(objNotificationMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objNotificationMaster.getlinktoUserMasterIdUpdatedBy());
            stringer.key("IsDeleted").value(objNotificationMaster.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateNotificationMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateNotificationMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String DeleteNotificationMaster(int notificationMasterId) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("notificationMasterId").value(notificationMasterId);

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.DeleteNotificationMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.DeleteNotificationMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public NotificationMaster SelectNotificationMaster(int notificationMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectNotificationMaster + "/" + notificationMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectNotificationMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public void SelectAllNotificationMasterPageWise(String currentPage, Context context, final Fragment targetFragment) {
        String url = Service.Url + this.SelectAllNotificationMaster + "/" + currentPage + "/" + Globals.linktoBusinessMasterId+"/0";
        Log.e("url", " " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("json", " " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllNotificationMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<NotificationMaster> notificationMasters = SetListPropertiesFromJSONArray(jsonArray);
                        objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                        objNotificationRequestListener.NotificationResponse(notificationMasters, null);
                    }
                } catch (Exception e) {
                    objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                    objNotificationRequestListener.NotificationResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                objNotificationRequestListener.NotificationResponse(null, null);
            }

        });
        queue.add(jsonObjectRequest);

    }

//	public ArrayList<SpinnerItem> SelectAllNotificationMasterNotificationMasterId() {
//		ArrayList<SpinnerItem> lstSpinnerItem = null;
//		try {
//			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllNotificationMasterNotificationMasterId);
//			if (jsonResponse != null) {
//				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllNotificationMasterNotificationMasterId + "Result");
//				if (jsonArray != null) {
//					lstSpinnerItem = new ArrayList<>();
//					SpinnerItem objSpinnerItem;
//					for (int i = 0; i < jsonArray.length(); i++) {
//						objSpinnerItem = new SpinnerItem();
//						objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("NotificationMasterId"));
//						objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("NotificationMasterId"));
//						lstSpinnerItem.add(objSpinnerItem);
//					}
//				}
//			}
//			return lstSpinnerItem;
//		}
//		catch (Exception ex) {
//			return null;
//		}
//	}


    //region interface
    public interface NotificationRequestListener {
        void NotificationResponse(ArrayList<NotificationMaster> alNotificationMasters, NotificationMaster objNotificationMaster);
    }

    public interface NotificationAddListener {
        void NotificationAddResponse(String errorCode, NotificationMaster objNotificationMaster);
    }
    //endregion
}
