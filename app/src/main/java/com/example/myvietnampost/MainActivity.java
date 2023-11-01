package com.example.myvietnampost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myvietnampost.databinding.ActivityMainBinding;
import com.example.myvietnampost.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//      FireBase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
//      Tao thu 1 user
//       User user1 = new User(user.getUid(), "Ngô Duy Khánh", "0789262169", "ngoduykhanha5@gmail.com", "Tổ 8 phường Quán Triều", "029856421-688", "019201004299", "2001-12-10", "Thái Nguyên", "TP. Thái Nguyên", "P. Quan Triều");

        DocumentReference docRef = db.collection("user").document("info");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user1 = documentSnapshot.toObject(User.class);
                binding.setUser(user1);
            }
        });
    }
}
