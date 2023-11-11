package com.example.myvietnampost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myvietnampost.model.apiModel.District;
import com.example.myvietnampost.model.apiModel.Division;
import com.example.myvietnampost.model.apiModel.Ward;

import java.util.List;

public class UpdateUserViewModel extends ViewModel {
    private MutableLiveData<List<Division>> divisionsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<District>> districtsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ward>> wardsLiveData = new MutableLiveData<>();

//Tinh
    public LiveData<List<Division>> getDivisionsLiveData(){
        return divisionsLiveData;
    }

    public void updateDivisions(List<Division> divisions) {
        divisionsLiveData.setValue(divisions);
    }
//    Thanh Pho
    public LiveData<List<District>> getDistrictsLiveData(){
        return districtsLiveData;
    }
    public void updateDistricts(List<District> districts){
        districtsLiveData.setValue(districts);
    }
//    Phuong/Xa
    public LiveData<List<Ward>> getWardsLiveData(){
        return wardsLiveData;
    }
    public void updateWards(List<Ward> wards){
        wardsLiveData.setValue(wards);
    }

}
