package com.app.android.june.easyorder4u;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private String email, password;
    ProgressDialog pDialog;
    private static final String KEY_EMPTY = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

    }



    public void signUp(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void login(View view) {
        email = etEmail.getText().toString().toLowerCase().trim();
        password = etPassword.getText().toString().trim();

        if (validateInputs()) {
            LoginUser();
        }


    }

    public void forgot(View view) {
        Intent i = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(i);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void LoginUser(){
        displayLoader();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "login successful",
                                    Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           startActivity(intent);
                           finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password",
                                    Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                });
    }

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(email)){
            etEmail.setError("Invalid input");
            etEmail.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Invalid input");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
