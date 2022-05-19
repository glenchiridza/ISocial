package com.glencconnnect.isocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextView goLogin;
    private Button btnRegister;
    private EditText edtEmail,edtPassword,edtConfirmPassword;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        goLogin = findViewById(R.id.go_login);
        btnRegister = findViewById(R.id.btn_register);
        edtEmail = findViewById(R.id.reg_edt_email);
        edtPassword = findViewById(R.id.reg_edt_password);
        edtConfirmPassword = findViewById(R.id.reg_edt_confirm_password);

        loadingBar = new ProgressDialog(this);

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

        if(TextUtils.isEmpty(email)){

            edtPassword.requestFocus();
            edtPassword.setError("Email required");
        }
        else if(TextUtils.isEmpty(password)){
            edtPassword.requestFocus();
            edtPassword.setError("Password required");
        }
        else if(TextUtils.isEmpty(confirmPassword)){

            edtPassword.requestFocus();
            edtPassword.setError("Confirm Password required");
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "password do not match", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Account creation in progress...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "auth success", Toast.LENGTH_SHORT).show();
                        }else{
                            loadingBar.dismiss();
                            String message = task.getException().getMessage();

                            Toast.makeText(RegisterActivity.this, "Error Occured:"+message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void takeUserToLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}