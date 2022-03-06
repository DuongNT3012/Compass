package com.digital.compass.maps.digitalcompass.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.digital.compass.maps.digitalcompass.R;


public class CustomDialogCheckGPS extends Dialog {

    public Activity c;
    public TextView tvOk;
    public IonClickOkDialogCheckGPS ionClickOkDialogCheckGPS;

    public CustomDialogCheckGPS(Activity a, IonClickOkDialogCheckGPS ionClickOkDialogCheckGPS) {
        super(a);
        this.c = a;
        this.ionClickOkDialogCheckGPS = ionClickOkDialogCheckGPS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_check_gps);
        tvOk = (TextView) findViewById(R.id.ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickOkDialogCheckGPS.onClickOkDialogCheckGPS();
            }
        });
    }
}

