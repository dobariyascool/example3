package com.arraybit.abposa;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.rey.material.widget.CompoundButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BusinessJSONParser.BusinessRequestListener {

    public static boolean isActive = false;
    public static boolean isStart = false;
    public static String mode;
    NavigationView navigationView;
    CompoundButton cbLogout;
    TextView txtFullName, txtLetter;
    LinearLayout homeLayout, internetLayout, nameLayout;
    ImageView imageView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.central_air);
        }

        //navigation view
        View headerView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.nav_header_home, null);
        cbLogout = (CompoundButton) headerView.findViewById(R.id.cbLogout);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        nameLayout = (LinearLayout) headerView.findViewById(R.id.nameLayout);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);

        cbLogout.setOnClickListener(this);
        nameLayout.setOnClickListener(this);

        navigationView = (NavigationView) findViewById(R.id.naviViewHome);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.addHeaderView(headerView);
        SetUserName();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, app_bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // end navigation view


        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this).equals("")) {
            navigationView.getMenu().findItem(R.id.nav_business).setTitle(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this));
        }

        // check user type
        if (objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this) != null && !objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this).equals("")) {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this).equals("User")) {
                mode = objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this) ;
            } else if(objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this).equals("Admin") ) {
                mode = objSharePreferenceManage.GetPreference("LoginPreference", "Role", HomeActivity.this) ;
            }
        }
        AddFragmentInLayout(new HomeOptionFragment(), mode);

        if (Globals.linktoBusinessMasterId == 0) {
            if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this).equals("")) {
                Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this));
            }
            if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessGroupMasterId", HomeActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessGroupMasterId", HomeActivity.this).equals("")) {
                Globals.linktoBusinessGroupMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessGroupMasterId", HomeActivity.this));
            }
        }
        if (Globals.totalCounter > 0) {
            if (Globals.totalCounter > 1) {
                navigationView.getMenu().findItem(R.id.nav_business).setEnabled(true);
            } else {
                navigationView.getMenu().findItem(R.id.nav_business).setEnabled(false);
            }
        } else {
            if (Service.CheckNet(HomeActivity.this)) {
                if (Globals.linktoBusinessGroupMasterId > 0) {
                    RequestAllBusiness();
                } else {
                    navigationView.getMenu().findItem(R.id.nav_business).setEnabled(false);
                }
            } else {
                Globals.ShowSnackBar(drawer, getResources().getString(R.string.MsgCheckConnection), HomeActivity.this, 1000);
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragmentLayout);
                if (currentFragment instanceof HomeOptionFragment) {
                    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commitAllowingStateLoss();
                }

                if (requestCode == 0) {
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                    if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this) != null && !objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this).equals("")) {
                        navigationView.getMenu().findItem(R.id.nav_business).setTitle(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessName", HomeActivity.this));
                    }
                } else if (requestCode == 1) {

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", " " + e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_account) {
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            Globals.ChangeActivity(HomeActivity.this, MyAccountActivity.class, false);
        } else if (id == R.id.nav_analyis) {
            Intent intent = new Intent(HomeActivity.this, AnalysisActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            Globals.ChangeActivity(HomeActivity.this, AnalysisActivity.class, false);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(HomeActivity.this, EventActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            Globals.ChangeActivity(HomeActivity.this, EventActivity.class, false);
        } else if (id == R.id.nav_business) {
            Intent intent = new Intent(HomeActivity.this, BusinessBranchActivity.class);
            intent.putExtra("isLogin", false);
            startActivityForResult(intent, 0);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (id == R.id.nav_offers) {
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            Globals.ChangeActivity(HomeActivity.this, OfferActivity.class, false);
        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_rate_us) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (id == R.id.nav_exit) {
            System.exit(0);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cbLogout) {
            Globals.ClearPreference(HomeActivity.this);
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else if (v.getId() == R.id.nameLayout) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            if (alBusinessMaster.size() > 1) {
                Globals.totalCounter = alBusinessMaster.size();
                navigationView.getMenu().findItem(R.id.nav_business).setEnabled(true);
            } else {
                Globals.totalCounter = 1;
                navigationView.getMenu().findItem(R.id.nav_business).setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        isActive = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    @Override
    protected void onRestart() {
        isStart = false;
        super.onRestart();
    }

    @Override
    protected void onStop() {
        isStart = false;
        super.onStop();
    }

    //region Private Methods & interfaces
    private void SetUserName() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this).substring(0, 1).toUpperCase());
        } else {
            txtFullName.setVisibility(View.GONE);
        }
    }

    private void AddFragmentInLayout(Fragment fragment, String mode) {
//        Bundle bundle = new Bundle();
//        bundle.putString("HomeMode", mode);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Globals.targetFragment = fragment;
        fragment.setTargetFragment(fragment, 0);
        fragmentTransaction.add(R.id.homeFragmentLayout, fragment, getResources().getString(R.string.title_fragment_home_options));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_home_options));
//        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void RequestAllBusiness() {
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(HomeActivity.this, String.valueOf(Globals.linktoBusinessGroupMasterId), null);
    }
    //endregion
}


