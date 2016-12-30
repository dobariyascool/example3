package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventJSONParser {

    public String SelectAllEventsPageWise = "SelectAllEventsPageWise";
    public SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    public Date dt = null;
    EventsListener objEventsListener;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private ArrayList<Events> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<Events> lstEvents = new ArrayList<>();
        Events objEvents;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objEvents = new Events();
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("EventDate"));
                objEvents.setEventDate(sdfControlDateFormat.format(dt));
                objEvents.setUserName(jsonArray.getJSONObject(i).getString("UserName"));
                objEvents.setType((short) jsonArray.getJSONObject(i).getInt("type"));
                if(jsonArray.getJSONObject(i).getString("PersonMobile")!=null && !jsonArray.getJSONObject(i).getString("PersonMobile").equals(""))
                {
                    objEvents.setPersonMobile(jsonArray.getJSONObject(i).getString("PersonMobile"));
                }
                lstEvents.add(objEvents);
            }
            return lstEvents;
        } catch (Exception e) {
            return null;
        }
    }

    public void SelectEventsPageWise(final Context context, final Fragment targetFragment, String currentpage, String linktoBusinessMasterId, final String eventType) {
        String url;
        try {
            if (linktoBusinessMasterId != null && !linktoBusinessMasterId.equals("") && currentpage != null && !currentpage.equals("0") && eventType != null && !eventType.equals("")) {
                url = Service.Url + this.SelectAllEventsPageWise + "/" + linktoBusinessMasterId + "/" + currentpage + "/" + eventType;
            } else {
                url = Service.Url + this.SelectAllEventsPageWise + "/" + 1 + "/" + 0 + "/" + null;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonResponse = jsonObject.getJSONArray(SelectAllEventsPageWise + "Result");
                            if (jsonResponse != null) {
                                objEventsListener = (EventsListener) targetFragment;
                                objEventsListener.SetEvents(SetListPropertiesFromJSONArray(jsonResponse), eventType);
                            }
                        }
                    } catch (Exception e) {
                        objEventsListener = (EventsListener) targetFragment;
                        objEventsListener.SetEvents(null, eventType);
                        Log.e("json", " " + e.getMessage());

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objEventsListener = (EventsListener) targetFragment;
                    objEventsListener.SetEvents(null, eventType);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objEventsListener = (EventsListener) targetFragment;
            objEventsListener.SetEvents(null, eventType);
        }

    }

    public interface EventsListener {
        void SetEvents(ArrayList<Events> lstEvents, String eventType);
    }
}
