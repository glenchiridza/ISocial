package com.glencconnnect.isocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.log_edt_email);
        edtPassword = findViewById(R.id.log_edt_password);
        btnLogin = findViewById(R.id.btn_login);

        register = findViewById(R.id.go_register);

    }
}