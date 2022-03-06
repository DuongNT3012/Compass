package com.digital.compass.maps.digitalcompass.model;

public class HelpGuidModel {
    public int img;
    public String content;

    public HelpGuidModel(int img, String content) {
        this.img = img;
        this.content = content;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
