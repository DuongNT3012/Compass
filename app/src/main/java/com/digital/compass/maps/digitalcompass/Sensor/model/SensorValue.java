package com.digital.compass.maps.digitalcompass.Sensor.model;

public class SensorValue {
    private float azimuth;
    private float magneticField = 0.0f;
    private float pitch;
    private float roll;

    public float getMagneticField() {
        return this.magneticField;
    }

    public void setMagneticField(float f) {
        this.magneticField = f;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(float f) {
        this.azimuth = f;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float f) {
        this.roll = f;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float f) {
        this.pitch = f;
    }

    public float getRawPitch() {
        return this.pitch;
    }

    public void setRotation(float f, float f2, float f3) {
        this.azimuth = f;
        this.roll = f2;
        this.pitch = f3;
    }
}
