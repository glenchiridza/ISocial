package com.glencconnnect.isocial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    private TextView goRegister;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        edtEmail = findViewById(R.id.log_edt_email);
        edtPassword = findViewById(R.id.log_edt_password);
        btnLogin = findViewById(R.id.btn_login);

        loadingBar = new ProgressDialog(this);

        goRegister = findViewById(R.id.go_register);

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegister();
            }
        });

        btnLogin.setOnClickListener(view->{
            loginUser();
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendUserToMainActivity();
        }
    }

    private void loginUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            edtEmail.setError("Email required");
            edtEmail.requestFocus();

        }else if(TextUtils.isEmpty(password)){

            edtEmail.setError("Password required");
            edtEmail.requestFocus();
        }else{
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           loadingBar.dismiss();
                           sendUserToMainActivity();
                           Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                       }else{
                           loadingBar.dismiss();
                           String message = task.getException().getMessage();
                           Toast.makeText(LoginActivity.this, "Error Occured: "+ message, Toast.LENGTH_SHORT).show();
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

    private void sendUserToRegister() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

}
