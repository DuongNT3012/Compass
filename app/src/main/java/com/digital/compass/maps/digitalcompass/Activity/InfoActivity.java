package com.digital.compass.maps.digitalcompass.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amazic.ads.util.Admod;
import com.digital.compass.maps.digitalcompass.BuildConfig;
import com.digital.compass.maps.digitalcompass.R;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.vapp.admoblibrary.rate.MaybeLaterCallback;
import com.vapp.admoblibrary.rate.RatingDialog;

public class InfoActivity extends AppCompatActivity {

    LinearLayout tvRate, tvPrivacyPolicy;
    ImageView iv_back;
    private FrameLayout frNative;
    private boolean isOpenResume = true;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        hideStatusAndNavigationBar();
        iv_back = findViewById(R.id.iv_back);
        tvRate = findViewById(R.id.tv_rate_us);
        tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(InfoActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRate();
            }
        });

        tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("Compass: Version " + BuildConfig.VERSION_NAME);

        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/compass-7b801.appspot.com/o/Privacy-Policy.html?alt=media&token=bdfab51e-14f8-48f1-865e-ddb0301459c6")));
                isOpenResume = false;
                com.amazic.ads.util.AppOpenManager.getInstance().disableAppResume();
            }
        });

        // load ads native home
        frNative = findViewById(R.id.fr_native);
        NativeAdView adView = (NativeAdView) LayoutInflater.from(InfoActivity.this).inflate(R.layout.ads_native_result, null);
        frNative.addView(adView);

        if (SplashActivity.nativeInfo != null) {
            Admod.getInstance().pushAdsToViewCustom(SplashActivity.nativeInfo, adView);
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

    private void showDialogRate() {
        RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(1)
                .date(1)
                .setNameApp(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setEmail("anhnt.vtd@gmail.com")
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate(new MaybeLaterCallback() {
                    @Override
                    public void onClick() {
                        //Utils.getInstance().showMessenger(MainActivity.this,"clicked Maybe Later");
                        finish();
                    }
                })
                .ratingButtonColor(R.color.blue_btn_bg_color)
                .build();

        //Cancel On Touch Outside
        ratingDialog.setCanceledOnTouchOutside(false);
        //show
        ratingDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isOpenResume) {
            com.amazic.ads.util.AppOpenManager.getInstance().enableAppResume();
            isOpenResume = true;
        }
    }
}