package com.digital.compass.maps.digitalcompass.View;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.digital.compass.maps.digitalcompass.Sensor.Drawer.CompassDrawAtMap;
import com.digital.compass.maps.digitalcompass.Sensor.model.SensorValue;

public class CompassMapView extends View {
    private CompassDrawAtMap compassDrawAtMap;
    private final Handler handler = new Handler();
    private boolean mIsPortrait;
    private final Runnable runnable = new Runnable() {
       
        public void run() {
            CompassMapView.this.invalidate();
            CompassMapView.this.handler.removeCallbacks(this);
            CompassMapView.this.handler.postDelayed(this, 16);
        }
    };

    public CompassMapView(Context context) {
        super(context);
        init();
    }

    public CompassMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CompassMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mIsPortrait = ((float) displayMetrics.heightPixels) / ((float) displayMetrics.widthPixels) > 1.4f;
        this.compassDrawAtMap = new CompassDrawAtMap(getContext());
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        float f = (float) size;
        int i3 = (int) ((this.mIsPortrait ? 1.0f : 0.8f) * f);
        int i4 = (int) (f * 0.86f);
        if (mode != 1073741824) {
            size = mode == Integer.MIN_VALUE ? Math.min(i3, size) : i3;
        }
        if (mode2 != 1073741824) {
            size2 = mode2 == Integer.MIN_VALUE ? Math.min(i4, size2) : i4;
        }
        setMeasuredDimension(size, size2);
    }

    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.handler.post(this.runnable);
    }

    
    public void onDetachedFromWindow() {
        this.handler.removeCallbacks(this.runnable);
        super.onDetachedFromWindow();
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.compassDrawAtMap.onSizeChanged(i, i2, i3, i4);
    }

    
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.compassDrawAtMap.draw(canvas);
    }

    public SensorValue getSensorValue() {
        return this.compassDrawAtMap.getSensorValue();
    }

    public String[] getSW_Value() {
        return new String[]{this.compassDrawAtMap.getNumberSW(), this.compassDrawAtMap.getTextSW()};
    }
}
