package com.glencconnnect.isocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postRecycler;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,
                R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(item -> {
            UserMenuSelector(item);
            return false;
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
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