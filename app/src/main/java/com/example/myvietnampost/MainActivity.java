package com.example.myvietnampost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myvietnampost.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

//        Kiểm tra đã có tài khoản nào đăng nhập vào hệ thống chưa
        if (user != null) {
            binding.tvName.setText(user.getEmail());
            Picasso.get().load(user.getPhotoUrl()).into(binding.ivImage);
        }else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

//       Nút đăng xuất user
        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if(mAuth.getCurrentUser() != null){
                    Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
            }
        });



    }
}
