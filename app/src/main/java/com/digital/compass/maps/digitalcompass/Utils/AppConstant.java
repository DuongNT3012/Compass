package com.digital.compass.maps.digitalcompass.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class AppConstant {
    public static final String FOLDER_PHOTO_LAYOUT = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/PhotoLayout/");
    public static String IS_FEEDBACK = "feedback";
    public static String IS_RATE = "israte";
    public static final String PRODUCT_ID = "removeads";
    public static int checkFirstGetIap = 0;
    public static boolean clickBanner = false;
    public static boolean gotoRemoveAds = true;
    public static boolean isShowRate = false;
    public static boolean isShowtoday = false;
    public static boolean removeAds = true;
    public static boolean shouldShowDialogRate = true;
    public static boolean showOpenAppAds = false;
    public static boolean showOpenAppAdsInfo = false;
    public static boolean showOpenAppPermission = false;
    public static String trackKing = "IAP_Splash_Sc_Show";
    public static InterstitialAd mInterstitialAd = null;
    public static InterstitialAd mInterSleepDirection = null;

    public static boolean isNetworkAvailable(Activity activity) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
