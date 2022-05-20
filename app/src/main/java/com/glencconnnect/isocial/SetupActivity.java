package com.glencconnnect.isocial;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    public static final int GALLERY_PICK= 1;

    private EditText edtUsername, edtFullName, edtCountry;
    private Button btnSave;
    private CircleImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String currentUID;

    private ProgressDialog loadingBar;

    private StorageReference userProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");


        loadingBar = new ProgressDialog(this);

        edtUsername = findViewById(R.id.st_username);
        edtFullName = findViewById(R.id.st_fullname);
        edtCountry = findViewById(R.id.st_country);
        profileImage = findViewById(R.id.setup_image);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(view->{
            AccountSetup();
        });

        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            activityResultLauncher.launch(intent);


        });

    }

    private void AccountSetup() {
        String username=  edtUsername.getText().toString();
        String fullname=  edtFullName.getText().toString();
        String country=  edtCountry.getText().toString();

        if(TextUtils.isEmpty(username)){
            edtUsername.setError("Field required");
            edtUsername.requestFocus();
        }
        else if(TextUtils.isEmpty(fullname)){
            edtFullName.setError("Field required");
            edtFullName.requestFocus();
        }
        else if(TextUtils.isEmpty(country)){
            edtCountry.setError("Field required");
            edtCountry.requestFocus();
        }
        else{
            loadingBar.setTitle("Profile Setup");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap<String,Object> usermap = new HashMap<>();
            usermap.put("username",username);
            usermap.put("fullname",fullname);
            usermap.put("country",country);
            usermap.put("status","Hey am using ISocial");
            usermap.put("gender","none");
            usermap.put("dob","none");
            usermap.put("relationship_status","none");

            userRef.updateChildren(usermap).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    sendUserToMainActivity();
                    Toast.makeText(SetupActivity.this, "profile updated", Toast.LENGTH_LONG).show();
                }else{
                    loadingBar.dismiss();
                    String message = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();



                        if(data != null && data.getData() !=null) {

                            Uri imageUri = data.getData();

                            CropImage.activity()
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setAspectRatio(1, 1)
                                    .start(SetupActivity.this);


                                CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);


                            if(result.getResultCode() == RESULT_OK && cropResult != null){


                                loadingBar.setTitle("Profile Image");
                                loadingBar.setMessage("Please wait...");
                                loadingBar.show();
                                loadingBar.setCanceledOnTouchOutside(true);

                                Uri resultUri = cropResult.getUri();
                                StorageReference filePath = userProfileImageRef.child(currentUID+".jpg");
                                filePath.putFile(resultUri)
                                        .continueWithTask(task -> {
                                            if(!task.isSuccessful()){
                                                throw task.getException();
                                            }
                                            return filePath.getDownloadUrl();
                                        })
                                        .addOnCompleteListener(task -> {
                                            if(task.isSuccessful()){
                                                Uri downloadUri = task.getResult();

                                                userRef.child("profileimage").setValue(downloadUri)
                                                        .addOnCompleteListener(tasc->{
                                                            if(tasc.isSuccessful()){
                                                                loadingBar.dismiss();
                                                                Toast.makeText(SetupActivity.this, "profile image stored to firebase db", Toast.LENGTH_SHORT).show();

                                                            }else{
                                                                loadingBar.dismiss();
                                                                String message = tasc.getException().getMessage();
                                                                Toast.makeText(SetupActivity.this, "Failure: "+message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                Toast.makeText(SetupActivity.this, "profile image saved", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }else{

                            Toast.makeText(SetupActivity.this, "Error: Image not compatible", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });
}