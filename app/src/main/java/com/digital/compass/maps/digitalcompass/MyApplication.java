package com.digital.compass.maps.digitalcompass;

import android.content.Context;
import android.content.SharedPreferences;

import com.amazic.ads.util.AdsApplication;
import com.amazic.ads.util.AppOpenManager;
import com.digital.compass.maps.digitalcompass.Activity.InfoActivity;
import com.digital.compass.maps.digitalcompass.Activity.SplashActivity;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.List;

public class MyApplication extends AdsApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
    }

    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public List<String> getListTestDeviceId() {
        return null;
    }

    @Override
    public String getResumeAdId() {
        return getResources().getString(R.string.app_open_resume);
    }

    public static int getCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts", 1);
    }
    public static void increaseCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts", pre.getInt("counts", 1) + 1);
        editor.commit();
    }
}
