package com.arraybit.abposa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;

public class SplashScreenActivity extends AppCompatActivity {

    RelativeLayout splashScreenLayout;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenLayout = (RelativeLayout) findViewById(R.id.splashScreenLayout);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.air_splash);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);
        splashScreenLayout.setBackground(new BitmapDrawable(getResources(),resizeBitmap));

        SetBusinessMasterID();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this) != null) {
                    String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this);
                    String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this);
                    String businessMasterId = objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", SplashScreenActivity.this);
                    if ((!userName.isEmpty() && !userPassword.isEmpty()) && (businessMasterId != null && businessMasterId.equals(String.valueOf(Globals.linktoBusinessMasterId)))) {
                       HomeActivity.isStart= true;
                        Globals.ChangeActivity(SplashScreenActivity.this, HomeActivity.class, true);
                    } else {
                        Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
                    }
                } else {
                    Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
                }
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {

    }

    private void SetBusinessMasterID() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", SplashScreenActivity.this) != null) {
            Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", SplashScreenActivity.this));
        } else {
            Globals.linktoBusinessMasterId = 1;
        }

        if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessGroupMasterId", SplashScreenActivity.this) != null) {
            Globals.linktoBusinessGroupMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessGroupMasterId", SplashScreenActivity.this));
        } else {
            Globals.linktoBusinessGroupMasterId = 0;
        }

    }
}
