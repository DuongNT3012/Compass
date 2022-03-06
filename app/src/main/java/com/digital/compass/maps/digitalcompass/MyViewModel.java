package com.digital.compass.maps.digitalcompass;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digital.compass.maps.digitalcompass.model.ModelCompass;

public class MyViewModel extends ViewModel {
    private MutableLiveData<ModelCompass> data = new MutableLiveData<>();

    public MutableLiveData<ModelCompass> getSelectedItem() {
        return this.data;
    }

    public void setSelectedItem(ModelCompass modelCompass) {
        this.data.setValue(modelCompass);
    }
}
