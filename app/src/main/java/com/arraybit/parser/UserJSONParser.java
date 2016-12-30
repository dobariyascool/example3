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
import com.arraybit.modal.UserMaster;
import com.arraybit.modal.UserRightsTran;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserJSONParser {

    public String SelectUserMaster = "SelectUserMaster";
    public String UpdateUserMasterPassword = "UpdateUserMasterPassword";
    public String SelectAllUserRightsTran = "SelectAllUserRightsTran";
    Date dt = null;
    UserRequestListener objUserRequestListener;
    UserPasswordListener objUserPasswordListener;

    //region class methods
    private UserMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        UserMaster objUserMaster ;
        try {
            if (jsonObject != null) {
                objUserMaster = new UserMaster();
                objUserMaster.setUserMasterId(jsonObject.getInt("UserMasterId"));
                objUserMaster.setUsername(jsonObject.getString("Username"));
                objUserMaster.setPassword(jsonObject.getString("Password"));
                objUserMaster.setLinktoRoleMasterId((short) jsonObject.getInt("linktoRoleMasterId"));
                objUserMaster.setCreateDateTime(jsonObject.getString("CreateDateTime"));
                objUserMaster.setLinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("UpdateDateTime").equals("null") && !jsonObject.getString("UpdateDateTime").equals("")) {
                    objUserMaster.setUpdateDateTime(jsonObject.getString("UpdateDateTime"));
                }
//                if (jsonObject.getString("linktoUserMasterIdUpdateBy").equals("null") && !jsonObject.getString("LastLoginDateTime").equals("")) {
                objUserMaster.setLinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
//                }
                if (!jsonObject.getString("LastLoginDateTime").equals("null") && !jsonObject.getString("LastLoginDateTime").equals("")) {
                    objUserMaster.setLastLoginDateTime(jsonObject.getString("LastLoginDateTime"));
                }
                objUserMaster.setLoginFailCount((short) jsonObject.getInt("LoginFailCount"));
                if (!jsonObject.getString("LastLockoutDateTime").equals("null") && !jsonObject.getString("LastLockoutDateTime").equals("")) {
                    objUserMaster.setLastLockoutDateTime(jsonObject.getString("LastLockoutDateTime"));
                }
                if (!jsonObject.getString("LastPasswordChangedDateTime").equals("null") && !jsonObject.getString("LastPasswordChangedDateTime").equals("")) {
                    objUserMaster.setLastPasswordChangedDateTime(jsonObject.getString("LastPasswordChangedDateTime"));
                }
                objUserMaster.setComment(jsonObject.getString("Comment"));
                objUserMaster.setLinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objUserMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objUserMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objUserMaster.setLinktoBusinessTypeMasterId((short) jsonObject.getInt("linktoBusinessTypeMasterId"));
                if (jsonObject.getString("linktoBusinessGroupMasterId")!=null && !jsonObject.getString("linktoBusinessGroupMasterId").equals("null") ) {
                    objUserMaster.setLinktoBusinessGroupMasterId((short) jsonObject.getInt("linktoBusinessGroupMasterId"));
                }
                objUserMaster.setRole(jsonObject.getString("Role"));
                return objUserMaster;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<UserRightsTran> SetClassListPropertiesFromJSONObject(JSONArray jsonArray) {
        ArrayList<UserRightsTran> lstUserRightsTran = new ArrayList<>();
        UserRightsTran objUserRightsTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objUserRightsTran = new UserRightsTran();
                objUserRightsTran.setLinktoRoleMasterId((short) jsonArray.getJSONObject(i).getInt("LinktoRoleMasterId"));
                objUserRightsTran.setLinktoUserRightsMasterId((short) jsonArray.getJSONObject(i).getInt("LinktoUserRightsMasterId"));
                objUserRightsTran.setUserRightsTranId(jsonArray.getJSONObject(i).getInt("UserRightsTranId"));
                lstUserRightsTran.add(objUserRightsTran);
            }
            return lstUserRightsTran;
        } catch (Exception e) {
            return null;
        }
    }
    //endregion

    //region update
    public void UpdateUserMasterPassword(String userMasterId, String userPassword, final Context context, final Fragment targetFragment) {
        String url;
        try {
            if (userMasterId != null) {
                url = Service.Url + this.UpdateUserMasterPassword + "/" + userMasterId + "/" + userPassword;
            } else {
                url = Service.Url + this.UpdateUserMasterPassword + "/" + null;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());

                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(UpdateUserMasterPassword + "Result");
                            if (jsonResponse != null) {

                                if (jsonResponse.getInt("ErrorCode") == -1) {
                                    objUserPasswordListener = (UserPasswordListener) targetFragment;
                                    objUserPasswordListener.UpdatePassword("-1");
                                } else {
                                    objUserPasswordListener = (UserPasswordListener) targetFragment;
                                    objUserPasswordListener.UpdatePassword(String.valueOf(jsonResponse.getInt("ErrorCode")));
                                }

                            }
                        }
                    } catch (Exception e) {
                        objUserPasswordListener = (UserPasswordListener) targetFragment;
                        objUserPasswordListener.UpdatePassword("-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objUserPasswordListener = (UserPasswordListener) targetFragment;
                    objUserPasswordListener.UpdatePassword("-1");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objUserPasswordListener = (UserPasswordListener) targetFragment;
            objUserPasswordListener.UpdatePassword("-1");
        }

    }
    //endregion

    //region Select
    public void SelectUserMaster(final Context context, String userName, String password, String token) {
        String url;
        try {
            if (userName != null && password != null ) {
                String token1 = null;
                if(token!=null) {
                  token1 = token.replace(":", "2E2").replace("-", "3E3").replace("_", "4E4");
                }
                url = Service.Url + this.SelectUserMaster + "/" + URLEncoder.encode(userName, "utf-8") + "/" + URLEncoder.encode(password, "utf-8") + "/" + token1;
            } else {
                url = Service.Url + this.SelectUserMaster + "/" + null + "/" + null + "/" + null;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectUserMaster + "Result");
                            if (jsonResponse != null) {

                                objUserRequestListener = (UserRequestListener) context;
                                objUserRequestListener.UserResponse(null, SetClassPropertiesFromJSONObject(jsonResponse));

                            }
                        }
                    } catch (Exception e) {
                        objUserRequestListener = (UserRequestListener) context;
                        objUserRequestListener.UserResponse(null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objUserRequestListener = (UserRequestListener) context;
                    objUserRequestListener.UserResponse(null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objUserRequestListener = (UserRequestListener) context;
            objUserRequestListener.UserResponse(null, null);
        }

    }
    //endregion

    //region SelectAll
    public void SelectAllUserRightsTran(final Context context, String linktoRoleMasterId) {
        String url;
        try {
            if (linktoRoleMasterId != null) {
                url = Service.Url + this.SelectAllUserRightsTran + "/" + linktoRoleMasterId;
            } else {
                url = Service.Url + this.SelectAllUserRightsTran + "/" + null;
            }
            Log.e("url", " " + url);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("json", " " + jsonObject.toString());
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(SelectAllUserRightsTran + "Result");
                            if (jsonArray != null) {

                                objUserRequestListener = (UserRequestListener) context;
                                objUserRequestListener.UserRightsRespons(null, SetClassListPropertiesFromJSONObject(jsonArray));

                            }
                        }
                    } catch (Exception e) {
                        objUserRequestListener = (UserRequestListener) context;
                        objUserRequestListener.UserRightsRespons(null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objUserRequestListener = (UserRequestListener) context;
                    objUserRequestListener.UserRightsRespons(null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objUserRequestListener = (UserRequestListener) context;
            objUserRequestListener.UserRightsRespons(null, null);
        }

    }
    //endregion

    public interface UserRequestListener {
        void UserResponse(String errorCode, UserMaster objUserMaster);

        void UserRightsRespons(String errorcode, ArrayList<UserRightsTran> lstUserRightsTran);

    }

    public interface UserPasswordListener {
        void UpdatePassword(String errorcode);
    }
}
