package com.example.myvietnampost;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myvietnampost.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
//   Tro ve dang nhap
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//   Dang ky
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  binding.emailIP.getText().toString().trim();
                String password =  binding.passwordIP.getText().toString().trim();
                String confirmpassword =  binding.confirmPasswordIP.getText().toString().trim();
                if (checkBlank(email, password,confirmpassword)){
                    Toast.makeText(Register.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                if(!checkEmail(email)){
                    Toast.makeText(Register.this, "Email sai", Toast.LENGTH_SHORT).show();
                }
                if(!checkPass(password,confirmpassword)){
                    Toast.makeText(Register.this, "Nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                }
                if(!checkBlank(email, password,confirmpassword) && checkEmail(email) && checkPass(password,confirmpassword)){
                    regester(email,password);

                }



            }
        });
    }
//    Xu ly dang ky tren FireBase
    void regester(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Intent intentLogin = new Intent(Register.this, Login.class);
                    startActivity(intentLogin);
                }else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Đăng ký không thành công",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }
//    Xu ly loi
    boolean checkEmail(String email){
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            if(matcher.matches()){
                return true;
            }else {
                return false;
            }
    }
    boolean checkBlank(String email, String password, String confirmPassword){
        if(email.equals("") || password.equals("") || confirmPassword.equals("")){
            return true;
        }else{
            return false;
        }
    }
    boolean checkPass(String password, String confirmPassword){
        if(password.equals(confirmPassword) && password.length()>=6){
            return true;
        }else return false;
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

}