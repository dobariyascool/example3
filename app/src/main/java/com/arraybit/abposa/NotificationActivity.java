package com.arraybit.abposa;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NotificationActivity extends AppCompatActivity implements NotificationAddFragment.BroadCastListener, ConfirmDialog.ConfirmationResponseListener {

    public NotificationAddFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llNotificationFragment, new NotificationListFragment(), getResources().getString(R.string.notification_list_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.notification_list_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_notificationlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.notification_list_fragment))) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.notification_add_fragment))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.notification_add_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    public void ReplaceAddFragment() {
        fragment = new NotificationAddFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llNotificationFragment, fragment, getResources().getString(R.string.notification_add_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.notification_add_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void ConfirmResponse() {
        fragment.AddNotification();
    }


    @Override
    public void BroadCastOnclick() {
        ConfirmDialog confirmDialog = new ConfirmDialog(true, "Are you sure for broadcast?");
        confirmDialog.show(getSupportFragmentManager(), "");
    }
}
