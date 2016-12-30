package com.arraybit.abposa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.arraybit.adapter.MyAccountAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity implements MyAccountAdapter.OptionClickListener {

    ArrayList<String> alString;
    ArrayList<Boolean> alStringIsSwitch;
    RecyclerView rvOptions;
    //    FloatingActionButton fabEdit;
    TextView txtLoginChar, txtFullName, txtEmail;
    FrameLayout myAccountLayout;
    ImageView ivProfile;
    SharePreferenceManage sharePreferenceManage;
    MyAccountAdapter accountAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharePreferenceManage = new SharePreferenceManage();

        myAccountLayout = (FrameLayout) findViewById(R.id.myAccountLayout);
        txtLoginChar = (TextView) findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFullName = (TextView) findViewById(R.id.txtFullName);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        SetUserName();
        GetData();

        rvOptions = (RecyclerView) findViewById(R.id.rvOptions);
        accountAdapter = new MyAccountAdapter(alString, alStringIsSwitch, MyAccountActivity.this, MyAccountActivity.this, this);
        rvOptions.setAdapter(accountAdapter);
        rvOptions.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {

            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_change_password))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    private void SetUserName() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this));
            txtLoginChar.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this).substring(0, 1).toUpperCase());
        } else {
            txtFullName.setVisibility(View.GONE);
        }
    }

    private void GetData() {
        alString = new ArrayList<>();
        alStringIsSwitch = new ArrayList<>();

        for (int i = 0; i < getResources().getStringArray(R.array.Option).length; i++) {
            alString.add(getResources().getStringArray(R.array.Option)[i]);
        }

        for (int i = 0; i < getResources().getStringArray(R.array.isOptionSwitch).length; i++) {
            alStringIsSwitch.add(Boolean.valueOf(getResources().getStringArray(R.array.isOptionSwitch)[i]));
        }
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = MyAccountActivity.this.getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.myAccountLayout, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    @Override
    public void OptionClick(int id) {
        if (id == 0 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            ReplaceFragment(new YourAddressFragment(), getResources().getString(R.string.title_fragment_your_address));
        } else if (id == 1 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                ReplaceFragment(new ChangePasswordFragment(), getResources().getString(R.string.title_fragment_change_password));
        } else if (id == 2 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Globals.ClearPreference(MyAccountActivity.this);
            Intent intent = new Intent(MyAccountActivity.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void NotificationOnOff(int position, Switch aSwitch) {
        if (aSwitch.isChecked()) {
            sharePreferenceManage.RemovePreference("NotificationSettingPreference", "Push", MyAccountActivity.this);
            sharePreferenceManage.CreatePreference("NotificationSettingPreference", "Push", "true", MyAccountActivity.this);
            Globals.EnableBroadCastReceiver(MyAccountActivity.this);
        } else {
            ConfirmNotificationSettings(aSwitch);
        }
    }

    private void ConfirmNotificationSettings(final Switch aSwitch) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccountActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.MsgConfirmSettings))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sharePreferenceManage.RemovePreference("NotificationSettingPreference", "Push", MyAccountActivity.this);
                                sharePreferenceManage.CreatePreference("NotificationSettingPreference", "Push", "false", MyAccountActivity.this);
                                Globals.DisableBroadCastReceiver(MyAccountActivity.this);
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                             //  accountAdapter.SetSwitch();
                                aSwitch.setChecked(true);
                                Globals.EnableBroadCastReceiver(MyAccountActivity.this);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
