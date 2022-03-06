package com.digital.compass.maps.digitalcompass.Activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amazic.ads.callback.InterCallback;
import com.amazic.ads.util.Admod;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.digital.compass.maps.digitalcompass.MyApplication;
import com.digital.compass.maps.digitalcompass.MyViewModel;
import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.Sensor.SensorView.SensorListener;
import com.digital.compass.maps.digitalcompass.Sensor.SensorView.SensorUtil;
import com.digital.compass.maps.digitalcompass.Utils.AppConstant;
import com.digital.compass.maps.digitalcompass.Utils.CustomDialogCheckGPS;
import com.digital.compass.maps.digitalcompass.Utils.IonClickOkDialogCheckGPS;
import com.digital.compass.maps.digitalcompass.Utils.NetworkChangeReceiver;
import com.digital.compass.maps.digitalcompass.View.AccelerometerView;
import com.digital.compass.maps.digitalcompass.View.CompassView;
import com.digital.compass.maps.digitalcompass.model.ModelCompass;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.orhanobut.hawk.Hawk;
import com.vapp.admoblibrary.ads.AppOpenManager;
import com.vapp.admoblibrary.rate.MaybeLaterCallback;
import com.vapp.admoblibrary.rate.RatingDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainCompassActivity extends AppCompatActivity implements SensorListener.OnValueChangedListener {

    private static final int TIME_INTERVAL = 2000;
    public static long lastClickTime = 0;
    static boolean show = true;
    AccelerometerView accelerometerView;
    private Bundle bundleInfo;
    private boolean clickBanner = false;
    CompassView compassDrawer;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Handler handler;
    ImageView imageAzi;
    ImageView imageCompass;
    ImageView imageCompassNew;
    ImageView imageLocation;
    ImageView imageMap;
    ImageView imageView9;
    ImageView imageViewInfo;
    ImageView imageViewSetting;
    ImageView imageSleepDirection;
    private boolean internet_avalible = false;
    private long mBackPressed;
    TextView magneticField;
    MyViewModel myViewModel;
    private SensorListener sensorListener;
    TextView textViewBigNumber;
    TextView textViewBigSW;
    public static TextView textViewLocation;
    TextView textViewNumber;
    TextView textViewSW;
    TextView tvTrueHeading;
    TextView tvUt;
    TextView tvX;
    TextView tvY;
    View view;
    private AdView adView;
    ArrayList<ModelCompass> list;
    //
    CustomDialogCheckGPS customDialogCheckGPS;
    androidx.appcompat.app.AlertDialog alertDialog;
    private SensorManager sensorManager;
    //
    ImageView imageInfo;
    //
    NetworkChangeReceiver networkChangeReceiver;
    private LinearLayout banner;
    //
    private int checkAdsResume = 0;

    //
    private LinearLayout llCompassStyle, llMap, llSleepDirection, llSettings, llAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hawk.init(this).build();
        setContentView(R.layout.activity_main_compass);

        // Ads banner
        Admod.getInstance().loadBanner(this, getString(R.string.banner));

        // Ads Inter theme
        if (AppConstant.mInterstitialAd == null) {
            Admod.getInstance().loadInterAds(this, getString(R.string.interstitial_home), new InterCallback() {
                @Override
                public void onInterstitialLoad(InterstitialAd interstitialAd) {
                    super.onInterstitialLoad(interstitialAd);
                    AppConstant.mInterstitialAd = interstitialAd;
                }
            });
        }
        // Ads Inter Sleep Direction
        if (AppConstant.mInterSleepDirection == null) {
            Admod.getInstance().loadInterAds(this, getString(R.string.interstitial_sleep_direction), new InterCallback() {
                @Override
                public void onInterstitialLoad(InterstitialAd interstitialAd) {
                    super.onInterstitialLoad(interstitialAd);
                    AppConstant.mInterSleepDirection = interstitialAd;
                }
            });
        }

        networkChangeReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
        //networkChangeReceiver.onReceive(MainCompassActivity.this, new Intent(MainCompassActivity.this, MainCompassActivity.class));

        //Permission
        if (ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainCompassActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1111);
        }
        alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle("Grant Permission");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Please grant all permissions to access additional functionality.");
        alertDialog.setButton(-1, (CharSequence) "Go to setting", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialogInterface, int i) {
                checkAdsResume = 1;
                com.amazic.ads.util.AppOpenManager.getInstance().disableAppResume();
                alertDialog.dismiss();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1112);
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        //
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Keep screen on
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean screenOn = preferences.getBoolean("screenOn", false);
        if (screenOn == true) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        //
        this.list = new ArrayList<>();
        this.list.clear();
        this.list.add(new ModelCompass("Golden Compass", R.drawable.ic_compass1, R.drawable.ic_azi_theme1, false, 1));
        /*if (AppConstant.isNetworkAvailable(MainCompassActivity.this)) {
            this.list.add(new ModelCompass("", 0, 0, false, 2));
        }*/
        this.list.add(new ModelCompass("Sea Compass", R.drawable.ic_compass6, R.drawable.ic_azi_theme2, false, 1));
        this.list.add(new ModelCompass("Valentine 1 ", R.drawable.ic_compass_vlt_01, R.drawable.ic_azi_theme3, false, 1));
        /*if (AppConstant.isNetworkAvailable(MainCompassActivity.this)) {
            this.list.add(new ModelCompass("", 0, 0, false, 2));
        }*/
        this.list.add(new ModelCompass("Valentine 5", R.drawable.ic_compass_vlt_05, R.drawable.ic_azi_theme4, false, 1));
        this.list.add(new ModelCompass("Vintage Compass", R.drawable.ic_compass4, R.drawable.ic_azi_theme5, false, 1));
        this.list.add(new ModelCompass("Technology", R.drawable.ic_compass_technology, R.drawable.v2_azi_2, false, 1));
        this.list.add(new ModelCompass("Natural", R.drawable.ic_compass_natural, R.drawable.v2_azi_6, false, 1));
        this.list.add(new ModelCompass("Jack Sparrow", R.drawable.ic_compass_jackparrow, R.drawable.v2_azi_1, false, 1));
        this.list.add(new ModelCompass("Christmas", R.drawable.ic_compass_chrismas, R.drawable.v2_azi_5, false, 1));
        this.list.add(new ModelCompass("Happy New Year", R.drawable.ic_compass_new_year, R.drawable.v2_azi_5, false, 1));

        /*this.list.add(new ModelCompass("Default", R.drawable.compass3, 0, true));
        this.list.add(new ModelCompass("Valentine 2", R.drawable.compass_vlt_02, R.drawable.kim_vlt2, false));
        this.list.add(new ModelCompass("Valentine 3", R.drawable.compass_vlt_03, R.drawable.kim_vlt3, false));
        this.list.add(new ModelCompass("Valentine 4", R.drawable.compass_vlt_04, R.drawable.kim_vlt3, false));
        this.list.add(new ModelCompass("Army", R.drawable.compass_army, R.drawable.v2_azi_3, false));
        this.list.add(new ModelCompass("American Compass", R.drawable.compass5, R.drawable.azi2, true));
        this.list.add(new ModelCompass("Wooden Compass", R.drawable.compass_v2, R.drawable.azi2, true));*/
        //
        initLayout();
        //
        setOnClick();

        hideStatusAndNavigationBar();

        if (AppConstant.gotoRemoveAds) {
            AppConstant.gotoRemoveAds = false;
        }

        new Handler().postDelayed(new Runnable() {

            public final void run() {
                MainCompassActivity.this.customTwo();
            }
        }, 500);
        if (hasSensor()) {
            //Edit
            SharedPreferences sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
            int position = sharedPreferences.getInt("position", 0);
            this.compassDrawer.setVisibility(View.INVISIBLE);
            if (position < list.size()) {
                Glide.with(this).load(Integer.valueOf(list.get(position).getImageCOmpass())).into(this.imageCompassNew);
                Glide.with(this).load(Integer.valueOf(list.get(position).getImageAzimuth())).into(this.imageAzi);
            }
            SensorListener sensorListener2 = new SensorListener(getBaseContext());
            this.sensorListener = sensorListener2;
            sensorListener2.setOnValueChangedListener(this);
            return;
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                    networkChangeReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            );
        }
    }

    private void setOnClick() {
        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                startActivity(new Intent(MainCompassActivity.this, InfoActivity.class));
            }
        });

        imageCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                Admod.getInstance().showInterAds(MainCompassActivity.this, AppConstant.mInterstitialAd, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(MainCompassActivity.this, ChangeCompassActivity.class));
                        finish();
                        if (AppConstant.mInterstitialAd != null) {
                            AppConstant.mInterstitialAd = null;
                        }
                        // Ads Inter theme
                        Admod.getInstance().loadInterAds(MainCompassActivity.this, getString(R.string.interstitial_home), new InterCallback() {
                            @Override
                            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                                super.onInterstitialLoad(interstitialAd);
                                AppConstant.mInterstitialAd = interstitialAd;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        onAdClosed();
                    }
                });
            }
        });

        imageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCompassActivity.this.customSix();
            }
        });

        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCompassActivity.this.customFive();
            }
        });

        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCompassActivity.this.customFive();
            }
        });

        imageViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                startActivity(new Intent(MainCompassActivity.this, SettingActivity.class));
            }
        });
        //Init dialog check GPS
        customDialogCheckGPS = new CustomDialogCheckGPS(MainCompassActivity.this, new IonClickOkDialogCheckGPS() {
            @Override
            public void onClickOkDialogCheckGPS() {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        imageSleepDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admod.getInstance().showInterAds(MainCompassActivity.this, AppConstant.mInterSleepDirection, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(MainCompassActivity.this, SleepDirectionActivity.class));
                        if (AppConstant.mInterSleepDirection != null) {
                            AppConstant.mInterSleepDirection = null;
                        }
                        // Ads Inter theme
                        Admod.getInstance().loadInterAds(MainCompassActivity.this, getString(R.string.interstitial_sleep_direction), new InterCallback() {
                            @Override
                            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                                super.onInterstitialLoad(interstitialAd);
                                AppConstant.mInterSleepDirection = interstitialAd;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        onAdClosed();
                    }
                });
            }
        });

        //
        llSleepDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admod.getInstance().showInterAds(MainCompassActivity.this, AppConstant.mInterSleepDirection, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(MainCompassActivity.this, SleepDirectionActivity.class));
                        if (AppConstant.mInterSleepDirection != null) {
                            AppConstant.mInterSleepDirection = null;
                        }
                        // Ads Inter theme
                        Admod.getInstance().loadInterAds(MainCompassActivity.this, getString(R.string.interstitial_sleep_direction), new InterCallback() {
                            @Override
                            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                                super.onInterstitialLoad(interstitialAd);
                                AppConstant.mInterSleepDirection = interstitialAd;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        onAdClosed();
                    }
                });
            }
        });
        llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCompassActivity.this.customSix();
            }
        });
        llCompassStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                Admod.getInstance().showInterAds(MainCompassActivity.this, AppConstant.mInterstitialAd, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(MainCompassActivity.this, ChangeCompassActivity.class));
                        finish();
                        if (AppConstant.mInterstitialAd != null) {
                            AppConstant.mInterstitialAd = null;
                        }
                        // Ads Inter theme
                        Admod.getInstance().loadInterAds(MainCompassActivity.this, getString(R.string.interstitial_home), new InterCallback() {
                            @Override
                            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                                super.onInterstitialLoad(interstitialAd);
                                AppConstant.mInterstitialAd = interstitialAd;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        onAdClosed();
                    }
                });
            }
        });
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                startActivity(new Intent(MainCompassActivity.this, SettingActivity.class));
            }
        });
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                startActivity(new Intent(MainCompassActivity.this, InfoActivity.class));
            }
        });
    }

    private void initLayout() {
        imageInfo = findViewById(R.id.imageInfo);
        MainCompassActivity.this.llCompassStyle = this.findViewById(R.id.ll_compass_style);
        MainCompassActivity.this.llMap = this.findViewById(R.id.ll_map);
        MainCompassActivity.this.llSleepDirection = this.findViewById(R.id.ll_sleep_direction);
        MainCompassActivity.this.llSettings = this.findViewById(R.id.ll_settings);
        MainCompassActivity.this.llAbout = this.findViewById(R.id.ll_about);
        MainCompassActivity.this.imageSleepDirection = this.findViewById(R.id.imageSleepDirection);
        MainCompassActivity.this.accelerometerView = this.findViewById(R.id.accelerometer_view);
        MainCompassActivity.this.compassDrawer = this.findViewById(R.id.compass_drawer);
        MainCompassActivity.this.imageAzi = this.findViewById(R.id.image_azi);
        MainCompassActivity.this.imageCompass = this.findViewById(R.id.imageCompass);
        MainCompassActivity.this.imageCompassNew = this.findViewById(R.id.image_compass_new);
        MainCompassActivity.this.imageLocation = this.findViewById(R.id.imageLocation);
        MainCompassActivity.this.imageMap = this.findViewById(R.id.remove_ad);
        MainCompassActivity.this.imageView9 = this.findViewById(R.id.imageView9);
        MainCompassActivity.this.imageViewInfo = this.findViewById(R.id.imageView_info);
        MainCompassActivity.this.imageViewSetting = this.findViewById(R.id.imageView_Setting);
        MainCompassActivity.this.magneticField = this.findViewById(R.id.magnetic_field);
        MainCompassActivity.this.textViewBigNumber = this.findViewById(R.id.textView_Big_number);
        MainCompassActivity.this.textViewBigSW = this.findViewById(R.id.textView_Big_SW);
        MainCompassActivity.this.textViewLocation = this.findViewById(R.id.textView_Location);
        MainCompassActivity.this.textViewNumber = this.findViewById(R.id.textView_number);
        MainCompassActivity.this.textViewSW = this.findViewById(R.id.textView_SW);
        MainCompassActivity.this.tvTrueHeading = findViewById(R.id.tv_true_heading);
        MainCompassActivity.this.tvUt = this.findViewById(R.id.tv_ut);
        MainCompassActivity.this.tvX = this.findViewById(R.id.tvX);
        MainCompassActivity.this.tvY = this.findViewById(R.id.tvY);
    }

    public void hideStatusAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(Build.VERSION.SDK_INT >= 19 ? 3334 : 1798);
        getWindow().addFlags(1024);
        getWindow().addFlags(128);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void customTwo() {
        vibrateButton(this.imageMap);
    }

    private void vibrateButton(View view2) {
        YoYo.with(Techniques.Pulse).duration(500).repeat(-1).playOn(view2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkAdsResume == 1) {
            com.amazic.ads.util.AppOpenManager.getInstance().enableAppResume();
        }
        AppConstant.showOpenAppAds = true;
        Animation loadAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_tv);
        if (this.textViewLocation.getText().toString().equalsIgnoreCase(getString(R.string.Unknown))) {
            this.textViewLocation.setTextColor(Color.parseColor("#99FFFFFF"));
            this.textViewLocation.startAnimation(loadAnimation);
        } else {
            this.textViewLocation.setTextColor(Color.parseColor("#99FFFFFF"));
            this.textViewLocation.clearAnimation();
        }
        setDisplay();
        MyViewModel myViewModel2 = (MyViewModel) ViewModelProviders.of(MainCompassActivity.this).get(MyViewModel.class);
        this.myViewModel = myViewModel2;
        myViewModel2.getSelectedItem().observe(this, new Observer() {

            @Override
            public final void onChanged(Object obj) {
                MainCompassActivity.this.customEight((ModelCompass) obj);
            }
        });
        try {
            setLocation();
        } catch (Exception unused) {
        }

        //Keep screen on
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean screenOn = preferences.getBoolean("screenOn", false);
        if (screenOn == true) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        //
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            customDialogCheckGPS.dismiss();
        }

        if (ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check if device has sensor
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainCompassActivity.this);
                builder.setTitle("No MAGNETIC FIELD sensor supports");
                builder.setMessage("Your phone does not have a magnetic sensor. Without the magnetic sensor, the app won't work");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {

                });
                builder.show();
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void customEight(ModelCompass modelCompass) {
        if (modelCompass.getImageCOmpass() == R.drawable.ic_compass3) {
            this.compassDrawer.setVisibility(0);
            this.imageCompassNew.setVisibility(4);
            this.imageAzi.setVisibility(4);
            return;
        }
        this.compassDrawer.setVisibility(4);
        Glide.with(this).load(Integer.valueOf(modelCompass.getImageCOmpass())).into(this.imageCompassNew);
        Glide.with(this).load(Integer.valueOf(modelCompass.getImageAzimuth())).into(this.imageAzi);
        Hawk.put("compass_choose", modelCompass);
    }

    @SuppressLint("WrongConstant")
    private void setDisplay() {
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean z = preferences.getBoolean("mag_field", true);
        boolean z2 = preferences.getBoolean("location", true);
        if (z) {
            this.magneticField.setVisibility(0);
            this.tvUt.setVisibility(0);
        } else {
            this.magneticField.setVisibility(8);
            this.tvUt.setVisibility(8);
        }
        if (z2) {
            //this.tvTrueHeading.setVisibility(0);
            //this.textViewNumber.setVisibility(0);
            //this.textViewSW.setVisibility(0);
            this.textViewBigSW.setVisibility(0);
            this.textViewBigNumber.setVisibility(0);
            this.textViewBigSW.setVisibility(0);
            this.textViewLocation.setVisibility(0);
            //this.imageViewInfo.setVisibility(0);
            return;
        }
        //this.tvTrueHeading.setVisibility(4);
        //this.textViewNumber.setVisibility(4);
        //this.textViewSW.setVisibility(8);
        this.textViewBigSW.setVisibility(8);
        this.textViewBigNumber.setVisibility(8);
        this.textViewBigSW.setVisibility(8);
        this.textViewLocation.setVisibility(8);
        //this.imageViewInfo.setVisibility(8);
    }

    @SuppressLint("MissingPermission")
    private void setLocation() {
        /*FusedLocationProviderClient fusedLocationProviderClient2 = LocationServices.getFusedLocationProviderClient(MainCompassActivity.this);
        this.fusedLocationProviderClient = fusedLocationProviderClient2;
        fusedLocationProviderClient2.getLastLocation().addOnSuccessListener(MainCompassActivity.this, new OnSuccessListener() {

            @Override
            public final void onSuccess(Object obj) {
                customSeven((Location) obj);
            }
        });*/
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainCompassActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                this.fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(MainCompassActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses.size() > 0) {
                                    textViewLocation.setText(addresses.get(0).getAddressLine(0));
                                    detailInfo(addresses.get(0).getLatitude(), addresses.get(0).getLongitude(), tvUt.getText().toString(), addresses.get(0));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();
                                    try {
                                        Geocoder geocoder = new Geocoder(MainCompassActivity.this, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                        if (addresses.size() > 0) {
                                            textViewLocation.setText(addresses.get(0).getAddressLine(0));
                                            detailInfo(addresses.get(0).getLatitude(), addresses.get(0).getLongitude(), tvUt.getText().toString(), addresses.get(0));
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            } else {
                customDialogCheckGPS.show();
            }
        }
    }

    private void detailInfo(double d, double d2, String str, Address address) {
        String str2;
        String str3;
        String str4;
        Bundle bundle = new Bundle();
        this.bundleInfo = bundle;
        bundle.putDouble("lat", d);
        this.bundleInfo.putDouble("lon", d2);
        this.bundleInfo.putString("mag", str);
        //Add
        SharedPreferences.Editor editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit();
        editor.putString("mag", str);
        editor.apply();

        String str5 = "";
        if (address.getThoroughfare() != null) {
            str2 = address.getThoroughfare() + ",";
        } else {
            str2 = str5;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        if (address.getAdminArea() != null) {
            str3 = address.getAdminArea() + ",";
        } else {
            str3 = str5;
        }
        sb.append(str3);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        if (address.getSubAdminArea() != null) {
            str4 = address.getSubAdminArea() + ",";
        } else {
            str4 = str5;
        }
        sb3.append(str4);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        if (address.getCountryName() != null) {
            str5 = address.getCountryName() + ",";
        }
        sb5.append(str5);
        this.bundleInfo.putString("add", sb5.toString());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainCompassActivity.this.setLocation2();
            }
        }, 400);
    }

    @SuppressLint("MissingPermission")
    private void setLocation2() {
        LocationServices.getFusedLocationProviderClient(MainCompassActivity.this).getLastLocation().addOnSuccessListener(MainCompassActivity.this, new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                MainCompassActivity.this.customThree((Location) obj);
            }
        });
    }

    public void customThree(Location location) {
        String str;
        String str2;
        String str3;
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            try {
                List<Address> fromLocation = new Geocoder(MainCompassActivity.this, Locale.getDefault()).getFromLocation(latitude, longitude, 1);
                fromLocation.get(0).getCountryName();
                double round = (double) ((Math.round(latitude * 1000.0d) / 1000) % 1);
                double round2 = (double) ((Math.round(1000.0d * longitude) / 1000) % 1);
                Address address = fromLocation.get(0);
                String str4 = "";
                if (address.getThoroughfare() != null) {
                    str = address.getThoroughfare() + ",";
                } else {
                    str = str4;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                if (address.getSubAdminArea() != null) {
                    str2 = address.getSubAdminArea() + ",";
                } else {
                    str2 = str4;
                }
                sb.append(str2);
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                if (address.getAdminArea() != null) {
                    str3 = address.getAdminArea() + ",";
                } else {
                    str3 = str4;
                }
                sb3.append(str3);
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb4);
                if (address.getCountryName() != null) {
                    str4 = address.getCountryName() + ",";
                }
                sb5.append(str4);
                String sb6 = sb5.toString();
                SharedPreferences.Editor edit = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit();
                edit.putString("add", sb6);
                edit.putString("lat", ((int) latitude) + "°" + round + "'");
                edit.putString("lon", ((int) longitude) + "°" + round2 + "'");
                edit.putString("latDouble", String.valueOf(latitude));
                edit.putString("lonDouble", String.valueOf(longitude));
                edit.apply();
            } catch (IOException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SensorListener sensorListener2 = this.sensorListener;
        if (sensorListener2 != null) {
            sensorListener2.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            this.handler.removeCallbacksAndMessages(null);
            if (this.clickBanner) {
                AppConstant.showOpenAppAds = false;
            }
        } catch (Exception unused) {
        }
        SensorListener sensorListener2 = this.sensorListener;
        if (sensorListener2 != null) {
            sensorListener2.stop();
        }
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
    public void onBackPressed() {
        //super.onBackPressed();
        int count = MyApplication.getCountOpenApp(this);
        MyApplication.increaseCountOpenApp(MainCompassActivity.this);
        if (count == 1 || count == 2 || count == 3 || count == 4 || count == 6 || count == 8 || count == 10) {
            showDialogRate();
        } else {
            finish();
        }
    }

    @Override
    public void onRotationChanged(float f, float f2, float f3) {
        this.compassDrawer.getSensorValue().setRotation(f, f2, f3);
        this.accelerometerView.getSensorValue().setRotation(f, f2, f3);
        this.textViewNumber.setText(this.compassDrawer.getSW_Value()[0]);
        this.textViewBigNumber.setText(this.compassDrawer.getSW_Value()[0]);
        this.textViewSW.setText(this.compassDrawer.getSW_Value()[1]);
        this.textViewBigSW.setText(this.compassDrawer.getSW_Value()[1]);
        this.tvUt.setText(this.compassDrawer.getMagneticValue());
        TextView textView = this.tvX;
        textView.setText("X-" + this.accelerometerView.xy_Value()[0] + "°");
        TextView textView2 = this.tvY;
        textView2.setText("Y-" + this.accelerometerView.xy_Value()[1] + "°");
        imageRotate(-f);
    }

    private void imageRotate(float f) {
        this.imageCompassNew.animate().rotation(f).setDuration(0).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }
        }).start();
    }

    @Override
    public void onMagneticFieldChanged(float f) {
        this.compassDrawer.getSensorValue().setMagneticField(f);
        if (f >= 100.0f && f < 200.0f) {
            this.tvUt.setTextColor(InputDeviceCompat.SOURCE_ANY);
        } else if (f >= 200.0f) {
            this.tvUt.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.tvUt.setTextColor(-1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        try {
            if ((i == 1111 || i == 1112) && iArr[0] == -1 && iArr[1] == -1) {
                Toast.makeText(getBaseContext(), "Permission is denied", Toast.LENGTH_SHORT).show();
                alertDialog.show();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline2() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
            socket.close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    private boolean hasSensor() {
        return SensorUtil.hasAccelerometer(getBaseContext()) && SensorUtil.hasMagnetometer(getBaseContext());
    }

    public void customFive() {
        setLocation();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            startActivity(new Intent(MainCompassActivity.this, LocationInfoActivity.class));
    }

    public void customSix() {
        setLocation();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            startActivity(new Intent(MainCompassActivity.this, LocationInfoActivity.class));
    }
}