package com.digital.compass.maps.digitalcompass.fragment;

import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.callbacks.ForeverDeniedCallback;


public final  class CustomSevens implements ForeverDeniedCallback {
    public static final CustomSevens INSTANCE = new CustomSevens();

    private CustomSevens() {
    }

    @Override
    public final void onForeverDenied(PermissionResult permissionResult) {

    }
}
