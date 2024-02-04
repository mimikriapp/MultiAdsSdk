package com.mimikridev.ad.sdkdemo.activity;

import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mimikridev.ad.sdk.format.AdNetwork;
import com.mimikridev.ad.sdkdemo.BuildConfig;
import com.mimikridev.ad.sdkdemo.R;
import com.mimikridev.ad.sdkdemo.application.MyApplication;
import com.mimikridev.ad.sdkdemo.callback.CallbackConfig;
import com.mimikridev.ad.sdkdemo.data.Constant;
import com.mimikridev.ad.sdkdemo.database.SharedPref;
import com.mimikridev.ad.sdkdemo.rest.RestAdapter;

import java.util.Arrays;
import java.util.List;



public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";

    public static int DELAY_PROGRESS = 1500;
    AdNetwork.Initialize adNetwork;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = new SharedPref(this);
        initAds();

        startMainActivity();

    }



    private void initAds() {
        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setStartappAppId(Constant.STARTAPP_APP_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setIronSourceAppKey(Constant.IRONSOURCE_APP_KEY)
                .setDebug(BuildConfig.DEBUG)
                .build();
    }



    public void startMainActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_PROGRESS);
    }

}
