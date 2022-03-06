package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.ForeverDeniedCallback;

public final  class CustomFours implements ForeverDeniedCallback {
    public static final CustomFours INSTANCE = new CustomFours();

    private CustomFours() {
    }

    @Override
    public final void onForeverDenied(PermissionResult permissionResult) {

    }
}
