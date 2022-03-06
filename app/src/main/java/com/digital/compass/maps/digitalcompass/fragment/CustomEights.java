package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.DeniedCallback;


public final  class CustomEights implements DeniedCallback {
    public static final CustomEights INSTANCE = new CustomEights();

    private CustomEights() {
    }

    @Override
    public final void onDenied(PermissionResult permissionResult) {

    }
}
