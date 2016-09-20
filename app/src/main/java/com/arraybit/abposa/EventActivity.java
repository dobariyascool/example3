package com.arraybit.abposa;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(app_bar);
            if (app_bar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
            }

            LinearLayout llEventFragment = (LinearLayout) findViewById(R.id.llEventFragment);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.replace(R.id.llEventFragment, new EventFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, R.anim.right_exit);
    }
}
