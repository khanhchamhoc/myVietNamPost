package com.example.myvietnampost;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myvietnampost.databinding.ActivityMenuViewBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MenuView extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuViewBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMenuView.toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(MenuView.this, Login.class);
            startActivity(intent);
        }
        binding.appBarMenuView.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MenuView.this, CreateOder.class);
                startActivity(intent1);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_userInfo, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_userInfo) {
                Intent intent = new Intent(MenuView.this, UserActivity.class);
                startActivity(intent);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            return false;
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_view);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        Load thong tin
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        String imageUrl = String.valueOf(user.getPhotoUrl());
        Picasso.get().load(imageUrl).into(imageView);
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_view);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}