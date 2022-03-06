package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.DeniedCallback;


public final  class CustomThrees implements DeniedCallback {
    public static final CustomThrees INSTANCE = new CustomThrees();

    private CustomThrees() {
    }

    @Override
    public final void onDenied(PermissionResult permissionResult) {

    }
}
