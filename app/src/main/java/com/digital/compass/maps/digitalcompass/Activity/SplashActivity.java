package com.digital.compass.maps.digitalcompass.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.amazic.ads.callback.InterCallback;
import com.amazic.ads.callback.NativeCallback;
import com.amazic.ads.util.Admod;
import com.digital.compass.maps.digitalcompass.MyApplication;
import com.digital.compass.maps.digitalcompass.R;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;

public class SplashActivity extends AppCompatActivity {
    public static NativeAd nativeSetting;
    public static NativeAd nativeInfo;
    public static NativeAd nativeTheme;
    boolean guided = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (checkPermission(getPermission())) {
            Admod.getInstance().setOpenActivityAfterShowInterAds(true);
        } else {
            Admod.getInstance().setOpenActivityAfterShowInterAds(false);
        }

        if (SplashActivity.nativeSetting == null) {
            loadNativeSetting();
        }
        if (SplashActivity.nativeInfo == null) {
            loadNativeInfo();
        }
        if (SplashActivity.nativeTheme == null) {
            loadNativeTheme();
        }

        SharedPreferences prefsGuide = getSharedPreferences("MY_PREFS_GUIDE", MODE_PRIVATE);
        guided = prefsGuide.getBoolean("guided", false);
        if (!guided) {
            Admod.getInstance().loadSplashInterAds(SplashActivity.this, getString(R.string.interstitial_splash), 25000, 5000, new InterCallback() {
                @Override
                public void onAdClosed() {
                    startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                    finish();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError i) {
                    super.onAdFailedToLoad(i);
                    startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                    finish();
                }
            });
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS_THEME", MODE_PRIVATE);
            boolean theme = sharedPreferences.getBoolean("theme", true);
            if (theme == true) {
                Admod.getInstance().loadSplashInterAds(SplashActivity.this, getString(R.string.interstitial_splash), 25000, 5000, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(SplashActivity.this, MainCompassActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        startActivity(new Intent(SplashActivity.this, MainCompassActivity.class));
                        finish();
                    }
                });
            } else {
                Admod.getInstance().loadSplashInterAds(SplashActivity.this, getString(R.string.interstitial_splash), 25000, 5000, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_THEME", MODE_PRIVATE).edit();
                        editor.putBoolean("theme", true);
                        editor.apply();
                        startActivity(new Intent(SplashActivity.this, ChangeCompassActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_THEME", MODE_PRIVATE).edit();
                        editor.putBoolean("theme", true);
                        editor.apply();
                        startActivity(new Intent(SplashActivity.this, ChangeCompassActivity.class));
                        finish();
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String[] per) {
        for (String s : per) {
            if (checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public String[] getPermission() {
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    private void loadNativeSetting() {
        Admod.getInstance().loadNativeAd(this, getString(R.string.native_setting), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                SplashActivity.nativeSetting = nativeAd;
            }
        });
    }

    private void loadNativeInfo() {
        Admod.getInstance().loadNativeAd(this, getString(R.string.native_info), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                SplashActivity.nativeInfo = nativeAd;
            }
        });
    }

    private void loadNativeTheme() {
        Admod.getInstance().loadNativeAd(this, getString(R.string.native_item_theme), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                SplashActivity.nativeTheme = nativeAd;
            }
        });
    }

}