package com.digital.compass.maps.digitalcompass.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.amazic.ads.util.Admod;
import com.bumptech.glide.Glide;
import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.Utils.AppConstant;
import com.digital.compass.maps.digitalcompass.fragment.CustomFours;
import com.digital.compass.maps.digitalcompass.fragment.CustomOnes;
import com.digital.compass.maps.digitalcompass.fragment.CustomThrees;
import com.digital.compass.maps.digitalcompass.fragment.CustomTwos;
import com.digital.compass.maps.digitalcompass.fragment.MapFragment;
import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.RuntimePermission;
import com.github.florent37.runtimepermission.callbacks.AcceptedCallback;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vapp.admoblibrary.ads.AdmodUtils;
import com.vapp.admoblibrary.ads.admobnative.enumclass.GoogleEBanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.OnClick;

public class LocationInfoActivity extends AppCompatActivity {

    private Dialog dialog;
    private boolean internet_avalible = false;
    TextView tvAddress;
    TextView tvAddressDetail;
    TextView tvLatitude;
    TextView tvLatitudeDetail;
    TextView tvLongitudeDetail;
    TextView tvLongtitude;
    TextView tvMagnetic;
    TextView tvMagneticDetail;
    TextView tvTitle;
    RelativeLayout viewGone;
    ImageView btnCopyAddress;
    ImageView btnCopyLon;
    ImageView btnCopyLat;
    ImageView imageHome, imageCompass, imageLocation, imageView_Setting;
    Intent intent;
    ImageView imgNoInternet;
    CardView cardView;
    ImageView iv_back;
    private LinearLayout banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        // Ads banner
        Admod.getInstance().loadBanner(this,getString(R.string.banner));

        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean screenOn = preferences.getBoolean("screenOn", false);
        if (screenOn == true) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        //
        initLayout();

        //Check Internet
        if(AppConstant.isNetworkAvailable(LocationInfoActivity.this)){
            cardView.setVisibility(View.VISIBLE);
            imgNoInternet.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.GONE);
            imgNoInternet.setVisibility(View.VISIBLE);
        }

        //
        setOnClick();

        AppConstant.showOpenAppAds = false;
        this.tvTitle.setText(R.string.loc_info);
        setInfo();

        if (!checkConnection(getBaseContext())) {
            //findViewById(R.id.frameAds).setVisibility(4);
        }
        AppConstant.trackKing = "IAP_GetlocationAd_Sc_Show";
        if (AppConstant.gotoRemoveAds) {
            AppConstant.gotoRemoveAds = false;
            //Navigation.findNavController(view).navigate(R.id.action_fragmentLocationInfo_to_removeAdFragment);
        }

        //Set map
        Fragment mapFragment = new MapFragment(Double.valueOf(preferences.getString("latDouble", "21.04088715582817")), Double.valueOf(preferences.getString("lonDouble", "105.76411645538103")));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
    }

    private void setOnClick() {
        btnCopyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvAddress.getText().toString() + tvAddressDetail.getText().toString());
                //Toast
                Toast toast = Toast.makeText(getBaseContext(), "Address copied", Toast.LENGTH_LONG);
                /*View view1 = toast.getView();
                view1.setBackgroundResource(R.drawable.bg_toast);
                TextView text = (TextView) view1.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#EEB850"));*/
                toast.show();
            }
        });
        btnCopyLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
                copyText(tvLatitude.getText().toString() + preferences.getString("lat", ""));
                //Toast
                Toast toast = Toast.makeText(getBaseContext(), "Latitude copied", Toast.LENGTH_LONG);
                /*View view2 = toast.getView();
                view2.setBackgroundResource(R.drawable.bg_toast);
                TextView text = (TextView) view2.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#0CBDF4"));*/
                toast.show();
            }
        });
        btnCopyLon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences2 = getSharedPreferences("MY_PRE", MODE_PRIVATE);
                copyText(tvLongtitude.getText().toString() + preferences2.getString("lon", ""));
                //Toast
                Toast toast = Toast.makeText(getBaseContext(), "Longitude copied", Toast.LENGTH_LONG);
                /*View view3 = toast.getView();
                view3.setBackgroundResource(R.drawable.bg_toast);
                TextView text = (TextView) view3.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#4BE981"));*/
                toast.show();
            }
        });
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //
        hideStatusAndNavigationBar();
    }
    public void hideStatusAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(Build.VERSION.SDK_INT >= 19 ? 3334 : 1798);
        getWindow().addFlags(1024);
        getWindow().addFlags(128);
    }

    private void initLayout() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationInfoActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        cardView = findViewById(R.id.card_view);
        imgNoInternet = findViewById(R.id.img_no_internet);
        imgNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationInfoActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imageHome = findViewById(R.id.imageHome);
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationInfoActivity.this, MainCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imageCompass = findViewById(R.id.imageCompass);
        imageCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationInfoActivity.this, ChangeCompassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imageLocation = findViewById(R.id.imageLocation);
        imageView_Setting = findViewById(R.id.imageView_Setting);
        imageView_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationInfoActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnCopyAddress = findViewById(R.id.button_coppy_address);
        btnCopyLon = findViewById(R.id.coppyLongitude);
        btnCopyLat = findViewById(R.id.coppyLatitude);
        tvAddress = findViewById(R.id.tv_Address);
        tvAddressDetail = findViewById(R.id.tv_address_detail);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLatitudeDetail = findViewById(R.id.tv_latitude_detail);
        tvLongitudeDetail = findViewById(R.id.tv_longitude_detail);
        tvLongtitude = findViewById(R.id.tv_longtitude);
        tvMagnetic = findViewById(R.id.tv_magnetic);
        tvMagneticDetail = findViewById(R.id.tv_magnetic_detail);
        tvTitle = findViewById(R.id.tv_title);
        //viewGone = findViewById(R.id.viewGone);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstant.showOpenAppAds = false;
        //Keep screen on
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        boolean screenOn = preferences.getBoolean("screenOn", false);
        if(screenOn == true){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    private void setInfo() {
        SharedPreferences preferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        this.tvAddressDetail.setText(preferences.getString("add", ""));
        this.tvLongitudeDetail.setText(preferences.getString("lon", ""));
        this.tvLatitudeDetail.setText(preferences.getString("lat", ""));
        this.tvMagneticDetail.setText(preferences.getString("mag", ""));
    }

    @SuppressLint({"UseRequireInsteadOfGet", "WrongConstant"})
    private void copyText(String str) {
        ((ClipboardManager) ((Context) Objects.requireNonNull(LocationInfoActivity.this)).getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(null, str));
    }

    private void checkPermissionLocation() {
        new RuntimePermission(LocationInfoActivity.this).request("android.permission.ACCESS_FINE_LOCATION").onAccepted(new AcceptedCallback() {

            @Override
            public final void onAccepted(PermissionResult permissionResult) {
                LocationInfoActivity.this.lambda$checkPermissionLocation$0$FragmentLocationInfo(permissionResult);
            }
        }).onDenied(CustomTwos.INSTANCE).onForeverDenied(CustomFours.INSTANCE).ask();
    }

    public void lambda$checkPermissionLocation$0$FragmentLocationInfo(PermissionResult permissionResult) {
        runToCheckInternet();
    }

    @SuppressLint("MissingPermission")
    private void setLocation() {
        LocationServices.getFusedLocationProviderClient(LocationInfoActivity.this).getLastLocation().addOnSuccessListener(LocationInfoActivity.this, new OnSuccessListener() {

            @Override
            public final void onSuccess(Object obj) {
                LocationInfoActivity.this.lambda$setLocation$3$FragmentLocationInfo((Location) obj);
            }
        });
    }

    public void lambda$setLocation$3$FragmentLocationInfo(Location location) {
        String str;
        String str2;
        String str3;
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            try {
                List<Address> fromLocation = new Geocoder(LocationInfoActivity.this, Locale.getDefault()).getFromLocation(latitude, longitude, 1);
                fromLocation.get(0).getCountryName();
                double round = (double) ((Math.round(latitude * 1000.0d) / 1000) % 1);
                double round2 = (double) ((Math.round(1000.0d * longitude) / 1000) % 1);
                TextView textView = this.tvLatitudeDetail;
                StringBuilder sb = new StringBuilder();
                int i = (int) latitude;
                sb.append(i);
                sb.append("°");
                sb.append(round);
                sb.append("'");
                textView.setText(sb.toString());
                TextView textView2 = this.tvLongitudeDetail;
                StringBuilder sb2 = new StringBuilder();
                int i2 = (int) longitude;
                sb2.append(i2);
                sb2.append("°");
                sb2.append(round2);
                sb2.append("'");
                textView2.setText(sb2.toString());
                Address address = fromLocation.get(0);
                String str4 = "";
                if (address.getThoroughfare() != null) {
                    str = address.getThoroughfare() + ",";
                } else {
                    str = str4;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                if (address.getSubAdminArea() != null) {
                    str2 = address.getSubAdminArea() + ",";
                } else {
                    str2 = str4;
                }
                sb3.append(str2);
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb4);
                if (address.getAdminArea() != null) {
                    str3 = address.getAdminArea() + ",";
                } else {
                    str3 = str4;
                }
                sb5.append(str3);
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append(sb6);
                if (address.getCountryName() != null) {
                    str4 = address.getCountryName() + ",";
                }
                sb7.append(str4);
                String sb8 = sb7.toString();
                this.tvAddressDetail.setText(sb8);
                SharedPreferences.Editor edit = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit();
                edit.putString("add", sb8);
                edit.putString("lat", i + "°" + round + "'");
                edit.putString("lon", i2 + "°" + round2 + "'");
                edit.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runToCheckInternet() {
        HandlerThread handlerThread = new HandlerThread("check_network");
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() {

            public final void run() {
                LocationInfoActivity.this.lambda$runToCheckInternet$4$FragmentLocationInfo();
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void lambda$runToCheckInternet$4$FragmentLocationInfo() {
        boolean isOnline2 = isOnline2();
        this.internet_avalible = isOnline2;
        if (!isOnline2) {
            Toast.makeText(getBaseContext(), getString(R.string.turn_on_network), 0).show();
        }
    }

    public boolean checkConnection(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return false;
        }
        if (activeNetworkInfo.getType() == 1 || activeNetworkInfo.getType() == 0) {
            return true;
        }
        return false;
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

    @OnClick({R.id.btn_get_detail})
    public void onViewClicked() {
        if (SystemClock.elapsedRealtime() - MainCompassActivity.lastClickTime >= 1000) {
            MainCompassActivity.lastClickTime = SystemClock.elapsedRealtime();
            showDialogWaitAD();
        }
    }

    private void showDialogWaitAD() {
        RuntimePermission.askPermission(this, new String[0]).request("android.permission.ACCESS_FINE_LOCATION").onAccepted(new AcceptedCallback() {

            @Override
            public final void onAccepted(PermissionResult permissionResult) {
                LocationInfoActivity.this.lambda$showDialogWaitAD$5$FragmentLocationInfo(permissionResult);
            }
        }).onDenied(CustomThrees.INSTANCE).onForeverDenied(CustomOnes.INSTANCE).ask();
    }

    public void lambda$showDialogWaitAD$5$FragmentLocationInfo(PermissionResult permissionResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        View inflate = getLayoutInflater().inflate(R.layout.dialog_wait_ad, (ViewGroup) null);
        builder.setView(inflate).setCancelable(false);
        FadingCircle fadingCircle = new FadingCircle();
        fadingCircle.setColor(Color.parseColor("#E6007E"));
        Glide.with(inflate).load((Drawable) fadingCircle).into((ImageView) inflate.findViewById(R.id.image_loading));
        AlertDialog create = builder.create();
        this.dialog = create;
        ((Window) Objects.requireNonNull(create.getWindow())).setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.show();
        setLocation();
        showAds();
    }

    private void showAds() {

        LocationInfoActivity.this.dialog.dismiss();
        LocationInfoActivity.this.checkPermissionLocation();
    }
}