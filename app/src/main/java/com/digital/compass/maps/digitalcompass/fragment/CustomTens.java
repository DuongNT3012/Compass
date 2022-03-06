package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.ForeverDeniedCallback;


public final  class CustomTens implements ForeverDeniedCallback {
    public static final CustomTens INSTANCE = new CustomTens();

    private CustomTens() {
    }

    @Override
    public final void onForeverDenied(PermissionResult permissionResult) {

    }
}
