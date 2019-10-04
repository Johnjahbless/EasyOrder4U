package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    private EditText edtEmail;
    private ProgressDialog pDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().hide();
        edtEmail = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        displayLoader();
        String email = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            pDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotActivity.this);
                            alertDialogBuilder.setTitle("Reset Password Instructions sent Successfully");
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setMessage("Please check your mail to reset your password")
                                    .setIcon(R.drawable.ic_done)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                    dialog.cancel();


                                                }
                                            });


                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            // Toast.makeText(ResetPasswordActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            pDialog.dismiss();
                            //Toast.makeText(ResetPasswordActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotActivity.this);
                            alertDialogBuilder.setTitle("Invalid Email Address");
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setMessage("Sorry, This email address is not registered")
                                    .setIcon(R.drawable.ic_close)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();


                                                }
                                            });


                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    }
                });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(ForgotActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}