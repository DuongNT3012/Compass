package com.digital.compass.maps.digitalcompass.fragment;

import android.view.View;

public final  class CustomSixs implements View.OnClickListener {
    public static final CustomSixs INSTANCE = new CustomSixs();

    private CustomSixs() {
    }

    public final void onClick(View view) {
        System.exit(0);
    }
}
