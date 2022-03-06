package com.digital.compass.maps.digitalcompass.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String AD_UNIT_ID_AU = "ca-app-pub-8541569573502186/3014062371";
    private static final String AD_UNIT_ID_FAKE = "ca-app-pub-3940256099942544/3419835294";
    private static final String LOG_TAG = "AppOpenManager";
    private static boolean isShowingAd = false;

    private Activity currentActivity;

    private final Application myApplication;
    private boolean show = false;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public AppOpenManager(Application application) {
        this.myApplication = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void fetchAd() {
//        if (!isAdAvailable()) {
//            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
//                /* class AppOpenManager.AnonymousClass1 */
//
//                @Override // com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
//                public void onAppOpenAdLoaded(AppOpenAd appOpenAd) {
//                    AppOpenManager.this.appOpenAd = appOpenAd;
//                }
//
//                @Override // com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
//                public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
//                    Log.d(AppOpenManager.LOG_TAG, "error in loading");
//                }
//            };
//            if (!AppConstant.removeAds) {
//                AdRequest adRequest = getAdRequest();
//                if (Constant.isDebugMode) {
//                    AppOpenAd.load(this.myApplication, AD_UNIT_ID_FAKE, adRequest, 1, this.loadCallback);
//                } else {
//                    AppOpenAd.load(this.myApplication, AD_UNIT_ID_AU, adRequest, 1, this.loadCallback);
//                }
//            }
//        }
    }

    public void showAdIfAvailable() {
//        if (isShowingAd || !isAdAvailable() || !AppConstant.showOpenAppAds || AppConstant.removeAds) {
//            Log.d(LOG_TAG, "Can not show ad.");
//            fetchAd();
//            return;
//        }
        Log.d(LOG_TAG, "Will show ad.");
//        this.appOpenAd.show(this.currentActivity, new FullScreenContentCallback() {
//            /* class AppOpenManager.AnonymousClass2 */
//
//            @Override // com.google.android.gms.ads.FullScreenContentCallback
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//            }
//
//            @Override // com.google.android.gms.ads.FullScreenContentCallback
//            public void onAdDismissedFullScreenContent() {
//                AppOpenManager.this.appOpenAd = null;
//                boolean unused = AppOpenManager.isShowingAd = false;
//                AppOpenManager.this.fetchAd();
//                AppConstant.showOpenAppAds = false;
//            }
//
//            @Override // com.google.android.gms.ads.FullScreenContentCallback
//            public void onAdShowedFullScreenContent() {
//                boolean unused = AppOpenManager.isShowingAd = true;
//            }
//        });
    }

//    private AdRequest getAdRequest() {
//        return new AdRequest.Builder().build();
//    }
//
//    public boolean isAdAvailable() {
//        return this.appOpenAd != null;
//    }

    public void onActivityStarted(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityDestroyed(Activity activity) {
        this.currentActivity = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        //showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }
}
