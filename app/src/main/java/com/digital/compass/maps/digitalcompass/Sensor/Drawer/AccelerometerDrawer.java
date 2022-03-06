package com.digital.compass.maps.digitalcompass.Sensor.Drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import androidx.core.content.ContextCompat;

import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.Sensor.model.SensorValue;

public class AccelerometerDrawer {
    private static final String TAG = "AccelerometerCompassHel";
    private int mAccentColor;
    private int mBackgroundColor;
    private Point mCenter;
    private Context mContext;
    private int mForegroundColor;
    private boolean mIsPaintCreated = false;
    private final Path mPath = new Path();
    private final Paint mPathPaint = new Paint(1);
    private float mPixelScale;
    private int mPrimaryTextColor;
    private int mSecondaryTextColor;
    private final SensorValue mSensorValue = new SensorValue();
    private float mUnitPadding;

    public AccelerometerDrawer(Context context) {
        this.mContext = context;
    }

    public void draw(Canvas canvas) {
        this.mPixelScale = ((float) Math.min(canvas.getWidth(), canvas.getHeight())) / 1000.0f;
        this.mCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        this.mUnitPadding = realPx(5.0f);
        initPaint();
        drawPitchRoll(canvas);
    }

    private float realPx(float f) {
        return f * this.mPixelScale;
    }

    private void initPaint() {
        if (!this.mIsPaintCreated) {
            this.mForegroundColor = ContextCompat.getColor(this.mContext, R.color.compass_foreground_color);
            this.mBackgroundColor = ContextCompat.getColor(this.mContext, R.color.compass_background_color);
            this.mPrimaryTextColor = ContextCompat.getColor(this.mContext, R.color.compass_text_primary_color);
            this.mSecondaryTextColor = ContextCompat.getColor(this.mContext, R.color.compass_text_secondary_color);
            this.mAccentColor = ContextCompat.getColor(this.mContext, R.color.compass_accent_color);
            this.mPathPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mIsPaintCreated = true;
        }
    }

    public SensorValue getSensorValue() {
        return this.mSensorValue;
    }

    private void drawPitchRoll(Canvas canvas) {
        float realPx = realPx((float) 470);
        this.mPathPaint.setColor(Color.parseColor("#232222"));
        this.mPathPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float) this.mCenter.x, (float) this.mCenter.y, 0.6666667f * realPx, this.mPathPaint);
        this.mPathPaint.setColor(this.mPrimaryTextColor);
        this.mPathPaint.setStyle(Paint.Style.FILL);
        float roll = this.mSensorValue.getRoll();
        float realPx2 = realPx(370.0f) * ((float) Math.cos(Math.toRadians((double) (90.0f - this.mSensorValue.getPitch()))));
        float realPx3 = realPx(370.0f) * ((float) Math.cos(Math.toRadians((double) (90.0f - roll))));
        float f = realPx / 5.0f;
        this.mPath.reset();
        this.mPath.moveTo((((float) this.mCenter.x) - realPx2) - f, ((float) this.mCenter.y) + realPx3);
        this.mPath.lineTo((((float) this.mCenter.x) - realPx2) + f, ((float) this.mCenter.y) + realPx3);
        this.mPath.moveTo(((float) this.mCenter.x) - realPx2, (((float) this.mCenter.y) + realPx3) - f);
        this.mPath.lineTo(((float) this.mCenter.x) - realPx2, ((float) this.mCenter.y) + realPx3 + f);
        this.mPathPaint.setShadowLayer(realPx(3.0f), 0.0f, 0.0f, -16777216);
        this.mPathPaint.setColor(-1);
        this.mPathPaint.setStrokeWidth(realPx(5.0f));
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(this.mPath, this.mPathPaint);
        this.mPath.reset();
        this.mPath.moveTo(((float) this.mCenter.x) - realPx, (float) this.mCenter.y);
        this.mPath.lineTo(((float) this.mCenter.x) + realPx, (float) this.mCenter.y);
        this.mPath.moveTo((float) this.mCenter.x, ((float) this.mCenter.y) - realPx);
        this.mPath.lineTo((float) this.mCenter.x, ((float) this.mCenter.y) + realPx);
        this.mPathPaint.setShadowLayer(realPx(3.0f), 0.0f, 0.0f, -16777216);
        this.mPathPaint.setColor(this.mSecondaryTextColor);
        this.mPathPaint.setStrokeWidth(realPx(5.0f));
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(this.mPath, this.mPathPaint);
    }

    public float[] getXY() {
        float roll = this.mSensorValue.getRoll();
        return new float[]{realPx(370.0f) * ((float) Math.cos(Math.toRadians((double) (90.0f - this.mSensorValue.getPitch())))), realPx(370.0f) * ((float) Math.cos(Math.toRadians((double) (90.0f - roll))))};
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mIsPaintCreated = false;
    }
}
