package com.example.myvietnampost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myvietnampost.model.User;
import com.example.myvietnampost.model.apiModel.District;
import com.example.myvietnampost.model.apiModel.Division;
import com.example.myvietnampost.model.apiModel.Ward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UpdateUserViewModel extends ViewModel {
    private MutableLiveData<List<Division>> divisionsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<District>> districtsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ward>> wardsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> update = new MutableLiveData<>();

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
        return update;
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
    public boolean check(User user) {
        user = userLiveData.getValue();
        if (user == null) {
            return false;
        }
        boolean isPhoneNumberValid = user.getPhoneNumber().matches("^[0-9]{10,11}$");
        boolean isNameValid = user.getName() != null;
        boolean isTaxNumberValid = user.getTaxNumber() != null;
        boolean isDateOfBirthValid = user.getDateOfBirth() != null;
        boolean isEmailValid = user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$");
        boolean isAddressValid = user.getAddress() != null;
        return isPhoneNumberValid && isNameValid && isTaxNumberValid && isDateOfBirthValid && isEmailValid && isAddressValid;
    }
    public void updateUserInformation(User updatedUser) {
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.set(updatedUser)
                    .addOnSuccessListener(aVoid -> {
                        update.setValue(true);
                    })
                    .addOnFailureListener(e -> {
                        update.setValue(false);
                    });
        }
    }



}
