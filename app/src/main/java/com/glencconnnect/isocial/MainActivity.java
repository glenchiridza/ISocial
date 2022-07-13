package com.glencconnnect.isocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postRecycler;

    private CircleImageView navProfileImage;
    private TextView navProfileText;
    private ImageButton btnAddPost;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseAuth mAuth;

    private DatabaseReference userRef;

    private String currentUID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddPost = findViewById(R.id.add_new_post);

        postRecycler = findViewById(R.id.all_user_posts_recycler);
        postRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecycler.setLayoutManager(linearLayoutManager);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,
                R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navProfileImage = navView.findViewById(R.id.nav_profile_image);
        navProfileText = navView.findViewById(R.id.nav_user_name);

        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.hasChild("fullname")){
                        String fullname = snapshot.child("fullname").getValue().toString();
                        navProfileText.setText(fullname);

                    }
                    if(snapshot.hasChild("profileimage")){
                        String image = snapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.ic_person).into(navProfileImage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            UserMenuSelector(item);
            return false;
        });

        btnAddPost.setOnClickListener(view->{
            sendUserToPostActivity();
        });

        displayAllUserPosts();

    }

    private void displayAllUserPosts() {

    }

    private void sendUserToPostActivity() {
        Intent intent  = new Intent(this,PostActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            sendUserToLoginActivity();
        }else{
            checkUserExistence();
        }
    }

    private void checkUserExistence() {

        final String userID = mAuth.getCurrentUser().getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(userID)){
                    sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendUserToSetupActivity() {
        Intent intent = new Intent(this,SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
                sendUserToPostActivity();
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
                mAuth.signOut();
                sendUserToLoginActivity();
            }

    }
}