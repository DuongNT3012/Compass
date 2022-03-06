package com.digital.compass.maps.digitalcompass.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.exifinterface.media.ExifInterface;

import java.util.Locale;

public class Utility {
    private static final String TAG = "Utility";

    public static String getDirectionText(float f) {
        return ((f < 0.0f || f >= 22.5f) && f <= 337.5f) ? (f < 22.5f || f >= 67.5f) ? (f < 67.5f || f >= 112.5f) ? (f < 112.5f || f >= 157.5f) ? (f < 157.5f || f >= 202.5f) ? (f < 202.5f || f >= 247.5f) ? (f < 247.5f || f >= 292.5f) ? (f < 292.5f || f >= 337.5f) ? "" : "NW" : ExifInterface.LONGITUDE_WEST : "SW" : ExifInterface.LATITUDE_SOUTH : "SE" : ExifInterface.LONGITUDE_EAST : "NE" : "N";
    }

    public static String formatDms(float f) {
        long j = (long) f;
        float f2 = f - ((float) j);
        long j2 = (long) (60.0f * f2);
        return String.format(Locale.US, "%d°%d'%.2f\"", Long.valueOf(j), Long.valueOf(j2), Float.valueOf((f2 - ((float) (j2 / 60))) * 3600.0f));
    }

    public static String formatTemperature(Context context, float f) {
        return String.format(Locale.US, "%.0f°F", Float.valueOf(f));
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
