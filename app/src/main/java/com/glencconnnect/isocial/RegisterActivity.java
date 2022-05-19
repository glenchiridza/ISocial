package com.glencconnnect.isocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private TextView goLogin;
    private Button btnRegister;
    private EditText edtEmail,edtPassword,edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        goLogin = findViewById(R.id.go_login);
        btnRegister = findViewById(R.id.btn_register);
        edtEmail = findViewById(R.id.reg_edt_email);
        edtPassword = findViewById(R.id.reg_edt_password);
        edtConfirmPassword = findViewById(R.id.reg_edt_confirm_password);

        goLogin.setOnClickListener(view->{
            takeUserToLogin();
        });

        btnRegister.setOnClickListener(view->{
            createAccount();
        });
    }

    private void createAccount() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

    }

    private void takeUserToLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}