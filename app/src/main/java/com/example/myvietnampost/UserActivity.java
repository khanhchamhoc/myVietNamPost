package com.example.myvietnampost;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myvietnampost.databinding.ActivityMainBinding;
import com.example.myvietnampost.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back lần nữa để thoát", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//      FireBase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(UserActivity.this, Login.class);
            startActivity(intent);
        }
        db = FirebaseFirestore.getInstance();
//      Khởi tạo dữ liệu mới khi người dùng lần đầu tiên đăng nhập vào ứng dụng
        DocumentReference docRef = db.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user1 = document.toObject(User.class);
                        binding.setUser(user1);
                    }
                } else {
                    Log.d(TAG, "Lỗi ", task.getException());
                }
            }
        });
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        User user1 = new User(user.getUid(),user.getDisplayName(),"",user.getEmail(),"","","","","","","");
                        db.collection("user").document(user.getUid()).set(user1);
                    }
                } else {
                    Log.d(TAG, "Lỗi ", task.getException());
                }
            }
        });
//        Lấy dữ liệu trả về nơi hiển thị


//       Nút chỉnh sửa
        binding.updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UpdateUser.class);
                startActivity(intent);
            }
        });
    }
}
