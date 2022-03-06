package com.digital.compass.maps.digitalcompass;

import android.os.Bundle;

import androidx.recyclerview.widget.DiffUtil;

import com.digital.compass.maps.digitalcompass.model.ModelCompass;

import java.util.ArrayList;

public class MyDiffUntil extends DiffUtil.Callback {
    ArrayList<ModelCompass> newList;
    ArrayList<ModelCompass> oldList;

    public MyDiffUntil(ArrayList<ModelCompass> arrayList, ArrayList<ModelCompass> arrayList2) {
        this.newList = arrayList;
        this.oldList = arrayList2;
    }

    @Override 
    public int getOldListSize() {
        ArrayList<ModelCompass> arrayList = this.oldList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override 
    public int getNewListSize() {
        ArrayList<ModelCompass> arrayList = this.newList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override 
    public boolean areItemsTheSame(int i, int i2) {
        return this.newList.get(i2).getImageCOmpass() == this.oldList.get(i).getImageCOmpass();
    }

    @Override 
    public boolean areContentsTheSame(int i, int i2) {
        return this.newList.get(i2).isActive() == this.oldList.get(i).isActive();
    }

    @Override 
    public Object getChangePayload(int i, int i2) {
        ModelCompass modelCompass = this.newList.get(i2);
        Bundle bundle = new Bundle();
        if (modelCompass.isActive() != this.newList.get(i).isActive()) {
            bundle.putBoolean("active", modelCompass.isActive());
        }
        if (bundle.size() == 0) {
            return null;
        }
        return bundle;
    }
}
