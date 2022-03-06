package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.ForeverDeniedCallback;

public final  class CustomOnes implements ForeverDeniedCallback {
    public static final CustomOnes INSTANCE = new CustomOnes();

    private CustomOnes() {
    }

    @Override
    public final void onForeverDenied(PermissionResult permissionResult) {

    }
}
