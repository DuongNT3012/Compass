package com.digital.compass.maps.digitalcompass.Sensor.SensorView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorListener implements SensorEventListener {
    private static final String TAG = "CompassListener";
    private float[] I = new float[9];
    private float[] R = new float[9];
    private Sensor mAccelerometerSensor;
    private float mCurrentAzimuth = 0.0f;
    private float[] mGeomagnetic = new float[3];
    private float[] mGravity = new float[3];
    private int mIntervalTime = 0;
    private long mLastTime = 0;
    private Sensor mMagneticSensor;
    private OnValueChangedListener mOnValueChangedListener;
    private final float[] mOrientation = new float[3];
    private Sensor mOrientationSensor;
    private SensorManager mSensorManager;

    public interface OnValueChangedListener {
        void onMagneticFieldChanged(float f);

        void onRotationChanged(float f, float f2, float f3);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public SensorListener(Context context) {
        @SuppressLint("WrongConstant") SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mAccelerometerSensor = sensorManager.getDefaultSensor(1);
        this.mMagneticSensor = this.mSensorManager.getDefaultSensor(2);
    }

    public void start() {
        this.mSensorManager.registerListener(this, this.mAccelerometerSensor, 0);
        this.mSensorManager.registerListener(this, this.mMagneticSensor, 0);
    }

    public void stop() {
        this.mSensorManager.unregisterListener(this);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.mOnValueChangedListener = onValueChangedListener;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mOnValueChangedListener != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastTime > ((long) this.mIntervalTime)) {
                synchronized (this) {
                    if (sensorEvent.sensor.getType() == 1) {
                        this.mGravity[0] = (this.mGravity[0] * 0.97f) + (sensorEvent.values[0] * 0.029999971f);
                        this.mGravity[1] = (this.mGravity[1] * 0.97f) + (sensorEvent.values[1] * 0.029999971f);
                        this.mGravity[2] = (this.mGravity[2] * 0.97f) + (sensorEvent.values[2] * 0.029999971f);
                    }
                    if (sensorEvent.sensor.getType() == 2) {
                        this.mGeomagnetic[0] = (this.mGeomagnetic[0] * 0.97f) + (sensorEvent.values[0] * 0.029999971f);
                        this.mGeomagnetic[1] = (this.mGeomagnetic[1] * 0.97f) + (sensorEvent.values[1] * 0.029999971f);
                        this.mGeomagnetic[2] = (this.mGeomagnetic[2] * 0.97f) + (sensorEvent.values[2] * 0.029999971f);
                        this.mOnValueChangedListener.onMagneticFieldChanged((float) Math.sqrt((double) ((this.mGeomagnetic[0] * this.mGeomagnetic[0]) + (this.mGeomagnetic[1] * this.mGeomagnetic[1]) + (this.mGeomagnetic[2] * this.mGeomagnetic[2]))));
                    }
                    if (SensorManager.getRotationMatrix(this.R, this.I, this.mGravity, this.mGeomagnetic)) {
                        SensorManager.getOrientation(this.R, this.mOrientation);
                        this.mOnValueChangedListener.onRotationChanged((((float) Math.toDegrees((double) this.mOrientation[0])) + 360.0f) % 360.0f, (float) Math.toDegrees((double) this.mOrientation[1]), (float) Math.toDegrees((double) this.mOrientation[2]));
                    }
                }
                this.mLastTime = currentTimeMillis;
            }
        }
    }
}
