package com.arraybit.abposa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.UserMaster;
import com.arraybit.modal.UserRightsTran;
import com.arraybit.parser.BusinessJSONParser;
import com.arraybit.parser.UserJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, UserJSONParser.UserRequestListener, BusinessJSONParser.BusinessRequestListener {

    EditText etUserName, etPassword;
    ImageView ibClear;
    ToggleButton tbPasswordShow;
    View view;
    ProgressDialog progressDialog;
    UserMaster objUserMaster;
    SharePreferenceManage objSharePreferenceManage;
    UserJSONParser objUserJSONParser;
    LinearLayout internetLayout,signInLayout;
    //    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        try {
            if (Service.CheckNet(this)) {
                FCMTokenGenerate();
            }
            etUserName = (EditText) findViewById(R.id.etUserName);
            etPassword = (EditText) findViewById(R.id.etPassword);
            signInLayout = (LinearLayout) findViewById(R.id.signInLayout);

            internetLayout = (LinearLayout) findViewById(R.id.internetLayout);
            Button btnRetry = (Button) internetLayout.findViewById(R.id.btnRetry);

            Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
            ibClear = (ImageView) findViewById(R.id.ibClear);
            tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);

            btnSignIn.setOnClickListener(this);
            btnRetry.setOnClickListener(this);

            ibClear.setOnClickListener(this);
            tbPasswordShow.setOnClickListener(this);
            Globals.HideKeyBoard(SignInActivity.this,signInLayout);

            etUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    etUserName.clearError();
                    ibClear.setVisibility(View.VISIBLE);
                    if (etUserName.getText().toString().equals("")) {
                        ibClear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    etPassword.clearError();
                    tbPasswordShow.setVisibility(View.VISIBLE);
                    if (etPassword.getText().toString().equals("")) {
                        tbPasswordShow.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (Service.CheckNet(this)) {
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                internetLayout.setVisibility(View.VISIBLE);
                Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                signInLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnSignIn) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), SignInActivity.this, 1000);
            } else {
                if (Service.CheckNet(this)) {
                    LoginRequest();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
            Globals.HideKeyBoard(this, view);
        } else if (v.getId() == R.id.ibClear) {
            etUserName.setText("");
            ibClear.setVisibility(View.GONE);
        } else if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }else if (v.getId() == R.id.btnRetry) {
            if (Service.CheckNet(SignInActivity.this)) {
//                CheckUserNamePassword();
                FCMTokenGenerate();
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }
    }

    @Override
    public void UserResponse(String errorCode, UserMaster objuserMaster) {
        progressDialog.dismiss();
//        if (isIntegrationLogin) {
        this.objUserMaster = objuserMaster;
        if (objUserMaster != null) {
            if (!objUserMaster.isEnabled()) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginDisable), SignInActivity.this, 1000);
            } else if (objUserMaster.getLoginFailCount() >= 7) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginBlocked), SignInActivity.this, 1000);
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserMasterId", String.valueOf(objUserMaster.getUserMasterId()), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objUserMaster.getUsername(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objUserMaster.getPassword(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objUserMaster.getLinktoBusinessMasterId()), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "Role", objuserMaster.getRole(), this);
                objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessGroupMasterId", String.valueOf(objUserMaster.getLinktoBusinessGroupMasterId()), this);
                objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessMasterId", String.valueOf(objUserMaster.getLinktoBusinessMasterId()), this);
                Globals.linktoBusinessGroupMasterId = objUserMaster.getLinktoBusinessGroupMasterId();

                if (Globals.linktoBusinessGroupMasterId > 0) {
                    RequestAllBusiness();
                } else {
                    Globals.linktoBusinessMasterId = objUserMaster.getLinktoBusinessMasterId();
                    HomeActivity.isStart = true;
                    Globals.ChangeActivity(this, HomeActivity.class, true);
                }
            }
        } else {
            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), SignInActivity.this, 1000);
        }
    }

    @Override
    public void UserRightsRespons(String errorcode, ArrayList<UserRightsTran> lstUserRightsTran) {

    }

    @Override
    public void BusinessResponse(String errorCode, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();

        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            if (alBusinessMaster.size() > 1) {
                Globals.totalCounter = alBusinessMaster.size();
//                Globals.ReplaceFragment(new CounterFragment(objUserMaster.getLinktoUserTypeMasterId()), getSupportFragmentManager(), null);
                Intent intent = new Intent(SignInActivity.this, BusinessBranchActivity.class);
                intent.putExtra("isLogin", true);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else {

                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessMasterId", String.valueOf(alBusinessMaster.get(0).getBusinessMasterId()), SignInActivity.this);
                objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessName", alBusinessMaster.get(0).getBusinessName(), SignInActivity.this);
                Globals.linktoBusinessMasterId = alBusinessMaster.get(0).getBusinessMasterId();
                Globals.totalCounter = 1;
                HomeActivity.isStart = true;
                Globals.ChangeActivity(this, HomeActivity.class, true);

//                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //region Private Methods
    private void LoginRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        objUserJSONParser = new UserJSONParser();
        objUserJSONParser.SelectUserMaster(SignInActivity.this, etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), token);
    }

    private void RoleRequest() {
//        progressDialog = new ProgressDialog();
//        progressDialog.show(getSupportFragmentManager(), "");
        objUserJSONParser = new UserJSONParser();
        objUserJSONParser.SelectAllUserRightsTran(SignInActivity.this, String.valueOf(objUserMaster.getLinktoRoleMasterId()));
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (etUserName.getText().toString().equals("") && etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
        } else if (etUserName.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("") && !etUserName.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            etUserName.clearError();
            IsValid = false;
        } else {
            etUserName.clearError();
            etPassword.clearError();
        }

        return IsValid;
    }

//    private void GCMTokenRegistration() {
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //If the broadcast has received with success
//                //that means device is registered successfully
//                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
//                    //Getting the registration token from the intent
//                    token = intent.getStringExtra("token");
//                    //Displaying the token as toast
////                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
//
//                    Log.e("login token", " " + token);
//                    //if the intent is not with success then displaying error messages
//
//                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
//                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        //Checking play service is available or not
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        //if play service is not available
//        if (ConnectionResult.SUCCESS != resultCode) {
//            //If play service is supported but not installed
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
//                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
//
//                //If play service is not supported
//                //Displaying an error message
//            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
//            }
//
//            //If play service is available
//        } else {
//            //Starting intent to register device
//            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
//            startService(itent);
//        }
//
//    }

    private void FCMTokenGenerate() {

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {

            FirebaseMessaging.getInstance().subscribeToTopic("news");
            // [END subscribe_topics]

            // Log and toast
//            String msg = "Subscribed";
//            Log.e("MainActivity"," "+ msg);
//            Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
            token = FirebaseInstanceId.getInstance().getToken();

            // Log and toast
//            String msg = getString(R.string.msg_token_fmt, token);
//            Log.e("mainActivity", "token:   "+token);
//            Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void RequestAllBusiness() {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
//        if (linktoBusinessGroupMasterId != 0) {
        objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(SignInActivity.this, String.valueOf(Globals.linktoBusinessGroupMasterId), null);
//        }

    }
    //endregion
}
