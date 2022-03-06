package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.DeniedCallback;


public final  class CustomNights implements DeniedCallback {
    public static final CustomNights INSTANCE = new CustomNights();

    private CustomNights() {
    }

    @Override
    public final void onDenied(PermissionResult permissionResult) {

    }
}
