package com.digital.compass.maps.digitalcompass.fragment;

import com.digital.compass.maps.digitalcompass.Utils.AppConstant;


public final class CustomElevens implements Runnable {
    public static final CustomElevens INSTANCE = new CustomElevens();

    private CustomElevens() {
    }

    public final void run() {
        AppConstant.showOpenAppAds = true;
    }
}
