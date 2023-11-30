package com.example.myvietnampost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myvietnampost.model.Order;
import com.example.myvietnampost.model.User;
import com.example.myvietnampost.model.apiModel.District;
import com.example.myvietnampost.model.apiModel.Division;
import com.example.myvietnampost.model.apiModel.Ward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CreateOderViewModel extends ViewModel {
    private MutableLiveData<List<Division>> divisionsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<District>> districtsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ward>> wardsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> addFlag = new MutableLiveData<>();

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> getUpdate = new MutableLiveData<>();
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
    //   USER
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
    public MutableLiveData<Boolean> getUpdateStatus(){
        return addFlag;
    }
    public void fetchDataFromFirebase() {
        user = mauth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                userLiveData.setValue(user);
            });
        }
    }
    public boolean check(Order order) {
        boolean isIdValid = order.getId() != null && !order.getId().isEmpty();
        boolean isPhoneNumberValid = order.getPhone().matches("^[0-9]{10,11}$");
        boolean isNameValid = order.getName() != null && !order.getName().isEmpty();
        boolean isImageUrlValid = order.getImageUrl() != null;
        boolean isAddressValid = order.getAddress() != null && !order.getAddress().isEmpty();
        boolean isWeightValid = order.getWeight() >= 0;

        return isPhoneNumberValid && isNameValid && isImageUrlValid && isIdValid && isAddressValid && isWeightValid;
    }
    public void addOrder(Order order) {
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("order").document(order.getId());
            docRef.set(order)
                    .addOnSuccessListener(aVoid -> {
                        addFlag.setValue(true);
                    })
                    .addOnFailureListener(e -> {
                        addFlag.setValue(false);
                    });
        }
    }


}
