package com.glencconnnect.isocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText edtUsername, edtFullName, edtCountry;
    private Button btnSave;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        edtUsername = findViewById(R.id.st_username);
        edtFullName = findViewById(R.id.st_fullname);
        edtCountry = findViewById(R.id.st_country);
        profileImage = findViewById(R.id.setup_image);
        btnSave = findViewById(R.id.btn_save);

    }
}