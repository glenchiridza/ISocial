package com.glencconnnect.isocial;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageButton postImageSelector;
    private EditText postText;
    private Button btnPublishPost;

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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

        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("aspectY","1");
        intent.putExtra("aspectX","1");
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