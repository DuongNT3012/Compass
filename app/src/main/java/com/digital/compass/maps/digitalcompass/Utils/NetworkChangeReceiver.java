package com.digital.compass.maps.digitalcompass.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver
{
    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            if (isNetworkAvailable(context)) {
                //Toast.makeText(context.getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();
                Log.e("keshav", "Online");
            } else {
                //Toast.makeText(context.getApplicationContext(), "Offline", Toast.LENGTH_SHORT).show();
                Log.e("keshav", "Offline");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
