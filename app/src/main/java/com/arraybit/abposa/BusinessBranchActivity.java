package com.arraybit.abposa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.arraybit.adapter.BusinessBranchAdapter;
import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.SpinnerItem;
import com.arraybit.parser.BusinessJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class BusinessBranchActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener, BusinessBranchAdapter.BranchSelectorListener, View.OnClickListener {

    AppCompatSpinner spOrderCity;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isCityFilter, isLogin;
    ArrayList<BusinessMaster> alBusinessMaster;
    LinearLayout businessBranchLayout,errorLayout;
    RecyclerView rvBusinessBranch;
    short businessMasterId;
    TextView txtBranchHeader;
    SharePreferenceManage objSharePreferenceManager = new SharePreferenceManage();
    Intent getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_branch);
        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
//            setSupportActionBar(app_bar);
            if (app_bar != null) {
                setSupportActionBar(app_bar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
            }

//        businessBranchLayout = (LinearLayout) findViewById(R.id.businessBranchLayout);
//        Globals.SetScaleImageBackground(BusinessBranchActivity.this, businessBranchLayout, null, null);

            getData = getIntent();
            isLogin = getData.getBooleanExtra("isLogin", false);

            if(isLogin)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            spOrderCity = (AppCompatSpinner) findViewById(R.id.spOrderCity);
            txtBranchHeader = (TextView) findViewById(R.id.txtBranchHeader);
            rvBusinessBranch = (RecyclerView) findViewById(R.id.rvBusinessBranch);

            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

            if (Service.CheckNet(BusinessBranchActivity.this)) {
                if (Globals.linktoBusinessGroupMasterId > 0) {
                    RequestBusinessMaster(Globals.linktoBusinessGroupMasterId, null);
                }
            } else {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null,R.drawable.wifi_off);
                Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
            }

            spOrderCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView spinnerText = (TextView) view.findViewById(R.id.txtSpinnerItem);
                    if (spinnerText != null) {
                        spinnerText.setTextColor(ContextCompat.getColor(BusinessBranchActivity.this, R.color.accent));
                    }
                    if (Service.CheckNet(BusinessBranchActivity.this)) {
                        isCityFilter = true;
                        RequestBusinessMaster(alBusinessMaster.get(0).getLinktoBusinessGroupMasterId(), (String) parent.getAdapter().getItem(position));
                    } else {
                        Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(!isLogin) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == android.R.id.home) {

        }
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        this.alBusinessMaster = alBusinessMaster;
        if (isCityFilter) {
            SetRecyclerView();
        } else {
            FillCity();
        }

    }

    @Override
    public void BranchSelectorOnClickListener(BusinessMaster objBusinessMaster) {
        Globals.linktoBusinessMasterId = objBusinessMaster.getBusinessMasterId();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessMasterId", String.valueOf(objBusinessMaster.getBusinessMasterId()), BusinessBranchActivity.this);
        objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessName", objBusinessMaster.getBusinessName(), BusinessBranchActivity.this);
        objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objBusinessMaster.getBusinessMasterId()), BusinessBranchActivity.this);
//        if (objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", BusinessBranchActivity.this) != null) {
//            businessMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", BusinessBranchActivity.this));
//            if (businessMasterId != Globals.linktoBusinessMasterId) {
//                ClearGoogleAccountAndFacebook();
//                Globals.ClearUserPreference(BusinessBranchActivity.this, BusinessBranchActivity.this);
//            }
//        }
        if (isLogin) {
            HomeActivity.isStart = true;
            Intent intent = new Intent(BusinessBranchActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isLogin) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    private void RequestBusinessMaster(short linktoBusinessGroupMasterId, String city) {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(BusinessBranchActivity.this, String.valueOf(linktoBusinessGroupMasterId), city);
    }

    private void FillCity() {
        ArrayList<SpinnerItem> alCity = new ArrayList<>();
        short cnt = 0;
        boolean isDuplicate = false;
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            for (BusinessMaster objBusiness : alBusinessMaster) {
                SpinnerItem objSpinnerItem = new SpinnerItem();
                if (alCity.size() == 0) {
                    objSpinnerItem.setText(objBusiness.getCity());
                    objSpinnerItem.setValue(cnt);
                    alCity.add(objSpinnerItem);
                } else {
                    for (SpinnerItem objSpinner : alCity) {
                        if (objSpinner.getText().equals(objBusiness.getCity())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        objSpinnerItem.setText(objBusiness.getCity());
                        objSpinnerItem.setValue(cnt);
                        alCity.add(objSpinnerItem);
                    }
                }
                cnt++;
            }
            SpinnerAdapter cityAdapter = new SpinnerAdapter(BusinessBranchActivity.this, alCity, true);
            spOrderCity.setAdapter(cityAdapter);
        }
    }

    private void SetRecyclerView() {
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            BusinessBranchAdapter adapter = new BusinessBranchAdapter(BusinessBranchActivity.this, alBusinessMaster, this);
            rvBusinessBranch.setVisibility(View.VISIBLE);
            txtBranchHeader.setVisibility(View.VISIBLE);
            rvBusinessBranch.setAdapter(adapter);
            rvBusinessBranch.setLayoutManager(new LinearLayoutManager(BusinessBranchActivity.this));
        } else {
            rvBusinessBranch.setVisibility(View.GONE);
            txtBranchHeader.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), null,R.drawable.alert);
        }
    }
}
