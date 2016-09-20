package com.arraybit.abposa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arraybit.global.Globals;

public class AnalysisActivity extends AppCompatActivity implements AnalysisFragment.ActivityBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new AnalysisFragment(this);
        Globals.targetFragment = fragment;
        fragmentTransaction.replace(R.id.analysisFragmentLayout, fragment, getResources().getString(R.string.title_activity_analysis));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_activity_analysis));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == 0) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.analysisFragmentLayout);
                    if (currentFragment instanceof AnalysisFragment) {
                        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commitAllowingStateLoss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", " " + e.getMessage());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void BackClicked() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, R.anim.right_exit);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_pie_chart))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_pie_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_horizontal_chart))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_horizontal_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_line_chart))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_line_chart), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_activity_analysis))) {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                }
            } else {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_activity_analysis))) {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                }
            }
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

}
