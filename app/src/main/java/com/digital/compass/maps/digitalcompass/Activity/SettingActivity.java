package com.digital.compass.maps.digitalcompass.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.amazic.ads.util.Admod;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.Utils.AppConstant;
import com.digital.compass.maps.digitalcompass.fragment.CustomElevens;
import com.google.android.gms.ads.nativead.NativeAdView;

public class SettingActivity extends AppCompatActivity {

    View btnBuy2;
    ConstraintLayout privacy;
    ConstraintLayout rate;
    SwitchCompat switch2;
    SwitchCompat switchMagnetic;
    SwitchCompat switchScreenOn;
    TextView tvTitle;
    TextView versionName;
    boolean screenOn;
    ImageView imageHome, imageCompass, imageLocation, imageView_Setting;
    Intent intent;
    ImageView iv_back;
    private FrameLayout frNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        screenOn = preferences.getBoolean("screenOn", false);
        if (screenOn == true) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        //
        initLayout();
        //
        setOnClick();

        isChecked();
        this.tvTitle.setText(R.string.set);
        checkMagneticField();
        checkLocationService();
        this.versionName.setVisibility(View.VISIBLE);
        getVersionName();
        AppConstant.trackKing = "IAP_Setting_Ba_Tap";
        View findViewById = findViewById(R.id.btnBuy2);
        this.btnBuy2 = findViewById;

        if (AppConstant.removeAds) {
            findViewById(R.id.removeAd).setVisibility(View.GONE);
        }

        //
        hideStatusAndNavigationBar();

        // load ads native home
        frNative = findViewById(R.id.fr_native);
        NativeAdView adView = (NativeAdView) LayoutInflater.from(SettingActivity.this).inflate(R.layout.ads_native_result, null);
        frNative.addView(adView);

        if (SplashActivity.nativeSetting != null) {
            Admod.getInstance().pushAdsToViewCustom(SplashActivity.nativeSetting, adView);
            frNative.setVisibility(View.VISIBLE);
        } else {
            frNative.setVisibility(View.GONE);
        }
    }

    public void hideStatusAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(Build.VERSION.SDK_INT >= 19 ? 3334 : 1798);
        getWindow().addFlags(1024);
        getWindow().addFlags(128);
    }

    private void setOnClick() {
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switchScreenOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit();
                editor.putBoolean("screenOn", b);
                editor.apply();
                if (b == true) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    private void initLayout() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SettingActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imageHome = findViewById(R.id.imageHome);
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SettingActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imageCompass = findViewById(R.id.imageCompass);
        imageLocation = findViewById(R.id.imageLocation);
        imageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SettingActivity.this, LocationInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imageView_Setting = findViewById(R.id.imageView_Setting);
        imageCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SettingActivity.this, ChangeCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        switchScreenOn = findViewById(R.id.switch_screen_on);
        if (screenOn == true) {
            switchScreenOn.setChecked(true);
        } else {
            switchScreenOn.setChecked(false);
        }
        privacy = findViewById(R.id.privacy);
        rate = findViewById(R.id.rate);
        switch2 = findViewById(R.id.switch_location);
        switchMagnetic = findViewById(R.id.switch_magnetic);
        tvTitle = findViewById(R.id.tv_title);
        versionName = findViewById(R.id.version);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(CustomElevens.INSTANCE, 1000);
        new Handler().postDelayed(new Runnable() {

            public final void run() {
                SettingActivity.this.settingOne();
            }
        }, 500);
    }

    public void settingOne() {
        YoYo.with(Techniques.Pulse).duration(500).repeat(-1).playOn(this.btnBuy2);
    }

    private void getVersionName() {
        try {
            String str = ((Context) getBaseContext()).getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName;
            TextView textView = this.versionName;
            textView.setText("Version " + str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void isChecked() {
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean z = preferences.getBoolean("mag_field", true);
        boolean z2 = preferences.getBoolean("location", true);
        this.switchMagnetic.setChecked(z);
        this.switch2.setChecked(z2);
    }

    private void checkLocationService() {
        this.switchMagnetic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingActivity.this.settingTwo(getSharedPreferences("MY_PRE", MODE_PRIVATE).edit(), compoundButton, z);
            }
        });
    }

    public void settingTwo(SharedPreferences.Editor editor, CompoundButton compoundButton, boolean z) {
        if (z) {
            editor.putBoolean("mag_field", true);
        } else {
            editor.putBoolean("mag_field", false);
        }
        editor.apply();
    }

    private void checkMagneticField() {
        this.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingActivity.this.settingThree(getSharedPreferences("MY_PRE", MODE_PRIVATE).edit(), compoundButton, z);
            }
        });
    }

    public void settingThree(SharedPreferences.Editor editor, CompoundButton compoundButton, boolean z) {
        if (z) {
            editor.putBoolean("location", true);
        } else {
            editor.putBoolean("location", false);
        }
        editor.apply();
    }
}