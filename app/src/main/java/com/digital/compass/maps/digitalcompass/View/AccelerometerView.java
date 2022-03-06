package com.digital.compass.maps.digitalcompass.View;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.digital.compass.maps.digitalcompass.Sensor.Drawer.AccelerometerDrawer;
import com.digital.compass.maps.digitalcompass.Sensor.model.SensorValue;

public class AccelerometerView extends View {
    private final Runnable mDraw = new Runnable() {
      
        public void run() {
            AccelerometerView.this.invalidate();
            AccelerometerView.this.mHandler.removeCallbacks(this);
            AccelerometerView.this.mHandler.postDelayed(this, 33);
        }
    };
    private final Handler mHandler = new Handler();
    private AccelerometerDrawer mHelper;
    private boolean mIsPortrait;

    public AccelerometerView(Context context) {
        super(context);
        init(context);
    }

    public AccelerometerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AccelerometerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mIsPortrait = ((float) displayMetrics.heightPixels) / ((float) displayMetrics.widthPixels) > 1.4f;
        this.mHelper = new AccelerometerDrawer(context);
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
        this.mHandler.post(this.mDraw);
    }

    
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacks(this.mDraw);
        super.onDetachedFromWindow();
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mHelper.onSizeChanged(i, i2, i3, i4);
    }

    
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mHelper.draw(canvas);
    }

    public SensorValue getSensorValue() {
        return this.mHelper.getSensorValue();
    }

    public int[] xy_Value() {
        return new int[]{(int) this.mHelper.getXY()[0], (int) this.mHelper.getXY()[1]};
    }
}
