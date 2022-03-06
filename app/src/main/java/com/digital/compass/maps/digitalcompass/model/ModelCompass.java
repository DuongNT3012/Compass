package com.digital.compass.maps.digitalcompass.model;

public class ModelCompass {
    private boolean active;
    private int imageAzimuth;
    private int imageCOmpass;
    private String nameCompass;
    private int type;

    public ModelCompass(String str, int i, int i2, boolean z, int type) {
        this.nameCompass = str;
        this.imageCOmpass = i;
        this.imageAzimuth = i2;
        this.active = z;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImageAzimuth() {
        return this.imageAzimuth;
    }

    public void setImageAzimuth(int i) {
        this.imageAzimuth = i;
    }

    public String getNameCompass() {
        return this.nameCompass;
    }

    public void setNameCompass(String str) {
        this.nameCompass = str;
    }

    public int getImageCOmpass() {
        return this.imageCOmpass;
    }

    public void setImageCOmpass(int i) {
        this.imageCOmpass = i;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean z) {
        this.active = z;
    }
}
