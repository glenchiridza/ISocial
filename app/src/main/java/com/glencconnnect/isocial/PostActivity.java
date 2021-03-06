package com.glencconnnect.isocial;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageButton postImageSelector;
    private EditText postText;
    private Button btnPublishPost;

    private Uri imageUri = null;
    private String postDescription;
    private StorageReference postImageRef;
    private DatabaseReference userRef, postRef;
    private FirebaseAuth mAuth;
    private String currentUID;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl;

    private ProgressDialog loadingBar;

    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        postImageRef = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        toolbar = findViewById(R.id.post_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        postImageSelector = findViewById(R.id.post_image);
        postText = findViewById(R.id.post_text);
        btnPublishPost = findViewById(R.id.post_publish);

        postImageSelector.setOnClickListener(view->{
            openGallery();
        });

        btnPublishPost.setOnClickListener(view->{
            coordinatorLayout = view;
            validatePost();

        });
    }

    private void validatePost() {
        postDescription = postText.getText().toString();
        if(imageUri == null){
            Toast.makeText(this, "Please Select Post Image", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(postDescription)){
            postText.setError("Post Description Required");
            postText.requestFocus();
        }else{
            storeImageInFirebaseStorage();
        }
    }

    private void storeImageInFirebaseStorage() {

        loadingBar.setTitle("Publish Post");
        loadingBar.setMessage("Please wait, we're publishing your post...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();


        Calendar callDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        saveCurrentDate = currentDate.format(callDate.getTime());

        Calendar callTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        saveCurrentTime = currentTime.format(callTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        String postImageName = imageUri.getLastPathSegment() + postRandomName + ".jpg";
        StorageReference filePath = postImageRef.child("PostImages").child(postImageName);
        filePath.putFile(imageUri)
                .continueWithTask( task -> {

                    if(!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }

                    return filePath.getDownloadUrl();

                })
                .addOnCompleteListener(task->{

                    if(task.isSuccessful()){
                        downloadUrl = task.getResult().toString();
                        
                        Toast.makeText(PostActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();

                        savePostInformationToDB();

                        loadingBar.dismiss();
                    }else{
                        loadingBar.dismiss();
                        String message = task.getException().getMessage();
                        Toast.makeText(PostActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void savePostInformationToDB() {

        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userFullName  = snapshot.child("fullname").getValue().toString();
                    String userProfileImage = snapshot.child("profileimage").getValue().toString();

                    //adding post information
                    HashMap<String,Object> postMap = new HashMap<>();
                    postMap.put("uid",currentUID);
                    postMap.put("date",saveCurrentDate);
                    postMap.put("time",saveCurrentDate);
                    postMap.put("description",postDescription);
                    postMap.put("postimage",downloadUrl);
                    postMap.put("profileimage",userProfileImage);
                    postMap.put("fullname",userFullName);


                    postRef.child(currentUID+postRandomName).updateChildren(postMap)
                            .addOnCompleteListener(task->{
                               if(task.isSuccessful()){
                                   sendUserToMainActivity();

                                   //remove the values inserted by user
                                   imageUri = null;
                                   postText.setText("");

                                   Snackbar.make(coordinatorLayout,"Post published successfully",Snackbar.LENGTH_SHORT).show();
                               }else{

                                   Snackbar.make(coordinatorLayout,"Error while publishing",Snackbar.LENGTH_SHORT).show();
                               }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.putExtra("crop","true");
//        intent.putExtra("aspectY","1");
//        intent.putExtra("aspectX","1");
        launchActivityIntent.launch(intent);

    }

    ActivityResultLauncher<Intent> launchActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();

                    if(data != null && data.getData() != null){

                        imageUri = data.getData();
                        postImageSelector.setImageURI(imageUri);

                    }

                }


            }
    );

    private void sendUserToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            sendUserToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }


}