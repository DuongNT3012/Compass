package com.digital.compass.maps.digitalcompass.Sensor.SensorView;

import android.content.Context;
import android.hardware.SensorManager;

public class SensorUtil {
    public static boolean hasGravitySensor(Context context) {
        return !((SensorManager) context.getSystemService("sensor")).getSensorList(9).isEmpty();
    }

    public static boolean hasMagnetometer(Context context) {
        return !((SensorManager) context.getSystemService("sensor")).getSensorList(2).isEmpty();
    }

    public static boolean hasAccelerometer(Context context) {
        return !((SensorManager) context.getSystemService("sensor")).getSensorList(1).isEmpty();
    }

    public static boolean hasRotationVector(Context context) {
        return !((SensorManager) context.getSystemService("sensor")).getSensorList(11).isEmpty();
    }
}
