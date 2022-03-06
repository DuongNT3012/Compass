package com.digital.compass.maps.digitalcompass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.digital.compass.maps.digitalcompass.Sensor.SensorView.SensorListener;
import com.digital.compass.maps.digitalcompass.View.CompassMapView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorListener.OnValueChangedListener {
    @BindView(R.id.compass_map_view)
    CompassMapView compassMapView;
    private FusedLocationProviderClient fusedLocation;
    @BindView(R.id.image_map_info)
    ImageView imageMapInfo;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    private GoogleMap mMap;
    SensorListener sensorListener;
    @BindView(R.id.tv_map_mag)
    TextView tvMapMag;
    @BindView(R.id.tvX2)
    TextView tvX2;
    @BindView(R.id.tvY2)
    TextView tvY2;

   
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
   
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        hideStatusAndNavigationBar();
        setAutoComplete();
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        SensorListener sensorListener2 = new SensorListener(this);
        this.sensorListener = sensorListener2;
        sensorListener2.setOnValueChangedListener(this);
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
        SensorListener sensorListener2 = this.sensorListener;
        if (sensorListener2 != null) {
            sensorListener2.stop();
        }
    }

    private void setAutoComplete() {
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        Places.createClient(this);
        if (!Places.isInitialized()) {
            Places.initialize(this, getResources().getString(R.string.google_maps_key));
        }
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
           

            @Override 
            public void onError(Status status) {
            }

            @Override 
            public void onPlaceSelected(Place place) {
                MapsActivity.this.mMap.clear();
                MapsActivity.this.mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            }
        });
    }

    @Override 
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        updateLocationUI();
        getLocationDevice();
    }

    @SuppressLint("MissingPermission")
    private void getLocationDevice() {
        this.mMap.clear();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        this.fusedLocation = fusedLocationProviderClient;
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener() {
           

            @Override 
            public final void onComplete(Task task) {
                MapsActivity.this.customOne(task);
            }
        });
    }

    public  void customOne(Task task) {
        if (task.isSuccessful()) {
            LatLng latLng = new LatLng(((Location) task.getResult()).getLatitude(), ((Location) task.getResult()).getLongitude());
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
            this.mMap.addMarker(new MarkerOptions().title("You are here").position(latLng));
            this.mMap.setMapType(4);
            return;
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        GoogleMap googleMap = this.mMap;
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
            this.mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    public void hideStatusAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(Build.VERSION.SDK_INT >= 19 ? 3334 : 1798);
        getWindow().addFlags(1024);
        getWindow().addFlags(128);
    }

    @Override 
    public void onRotationChanged(float f, float f2, float f3) {
        this.compassMapView.getSensorValue().setRotation(f, f2, f3);
        TextView textView = this.tvMapMag;
        textView.setText(this.compassMapView.getSW_Value()[0] + " " + this.compassMapView.getSW_Value()[1]);
    }

    @Override
    public void onMagneticFieldChanged(float f) {
        this.compassMapView.getSensorValue().setMagneticField(f);
    }

    @OnClick({R.id.image_map_info, R.id.imageView2})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.imageView2) {
            getLocationDevice();
        }
    }
}
