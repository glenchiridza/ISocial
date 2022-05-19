package com.glencconnnect.isocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postRecycler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });
    }

    private void UserMenuSelector(MenuItem item) {
        int idx = item.getItemId();

            if (idx == R.id.nav_post) {
                Toast.makeText(this, "post clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_profile) {
                Toast.makeText(this, "profile clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_home) {
                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_friends) {
                Toast.makeText(this, "friends clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_find_friends) {
                Toast.makeText(this, "find friends clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_messages) {
                Toast.makeText(this, "messages clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_settings) {
                Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
            }
            else if (idx == R.id.nav_logout) {
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();
            }

    }
}