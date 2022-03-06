package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.DeniedCallback;

public final  class CustomTwos implements DeniedCallback {
    public static final CustomTwos INSTANCE = new CustomTwos();

    private CustomTwos() {
    }

    @Override
    public final void onDenied(PermissionResult permissionResult) {

    }
}
