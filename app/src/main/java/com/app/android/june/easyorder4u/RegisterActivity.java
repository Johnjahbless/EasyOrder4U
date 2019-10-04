package com.app.android.june.easyorder4u;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.android.june.easyorder4u.helpers.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ImageView imageView;
    private EditText etEmail, etPassword, etConfirmPassword, full_Name, etPhone, etHome, etLga, etState;
    private String email, password, name, Cpassword, phone, home, userId, gender, photo_url,
            image, formattedDate, thumb_image, status, state, lga;
    private ProgressDialog pDialog;
    private static final String TAG = "SignUpActivity";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    Long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        imageView = findViewById(R.id.circleImageViewUserImage);
        full_Name = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etHome = findViewById(R.id.home);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirmpassword);
        etLga = findViewById(R.id.lga);
        etState = findViewById(R.id.state);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dddf = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = dddf.format(c.getTime());
        date = new Date().getTime();

    }

    public void login(View view) {
        onBackPressed();
    }
    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void register(View view) {
        name = full_Name.getText().toString();
        email = etEmail.getText().toString().toLowerCase().trim();
        phone = etPhone.getText().toString();
        home = etHome.getText().toString();
        password = etPassword.getText().toString().trim();
        Cpassword = etConfirmPassword.getText().toString().trim();
        lga = etLga.getText().toString().toLowerCase();
        state = etState.getText().toString().toLowerCase();
        if (validateInputs()) {
            RegisterUser();

        }
    }

    private void writeNewUser() {
        displayLoader();
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("Name", name);
        newContact.put("Gender", gender);
        newContact.put("Email", email);
        newContact.put("Profile_pic", photo_url);
        newContact.put("Password", password);
        newContact.put("UserID", userId);
        newContact.put("Phone", phone);
        newContact.put("State", state);
        newContact.put("Lga", lga);
        newContact.put("Country", "null");
        newContact.put("HomeAddress", home);
        newContact.put("Created_date", formattedDate);
        newContact.put("Date", date);
        newContact.put("Approved", "Yes");
        db.collection("Customers").document("users").collection("users").document(userId).set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendEmailVerification();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(RegisterActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();
                                          }
                                      }
                );


    }
    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            pDialog.dismiss();
                            displayDialog();
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                });
    }
    private void displayDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        alertDialogBuilder.setTitle("Please verify your email address");
        alertDialogBuilder
                .setCancelable(false)
                .setIcon(R.drawable.ic_done)
                .setMessage("Your account has been created successfully, however you will need to login into your email account and click on the verification link to verify your email, your account will be suspended if you don't verify within 24hrs")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();




    }
    public void RegisterUser() {
        displayLoader();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            if (task.isSuccessful()) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                userId = user.getUid();
                                pDialog.dismiss();
                                upload_imageTo_storage();

                                //sendPost(fullName, email, gender, phoneNo, password, photo,userId);
                                //finish();
                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Couldn't register, try again",
                                        Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    public void privacy(View view) {
    }

    public void terms(View view) {
    }

    public void Select_gender(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    gender = "Male";
                break;
            case R.id.female:
                if (checked)
                    gender = "Female";
                break;
        }
    }

    private boolean validateInputs() {
        String KEY_EMPTY = "";
        if (KEY_EMPTY.equals(name)) {
            full_Name.setError("Invalid input");
            full_Name.requestFocus();
            return false;

        }

        if (KEY_EMPTY.equals(email)) {
            etEmail.setError("Invalid input");
            etEmail.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Invalid input");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6){
            etPassword.setError("Password too short");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(Cpassword)) {
            etConfirmPassword.setError("Invalid input");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(phone)) {
            etPhone.setError("Invalid input");
            etPhone.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(home)) {
            etHome.setError("Invalid input");
            etHome.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(lga)) {
            etLga.setError("Invalid input");
            etLga.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(state)) {
            etState.setError("Invalid input");
            etState.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(gender)) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(Cpassword)) {
            etConfirmPassword.setError("Password and Confirm Password does not match");
            etConfirmPassword.requestFocus();
            return false;
        }
       /* if (filePath == null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }*/


        return true;
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void upload_imageTo_storage() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference ref = storageReference.child("profile/" + userId);
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        final Uri downloadUri = task.getResult();
                        photo_url = downloadUri.toString();
                        thumb_image = downloadUri.toString();
                        image = thumb_image;
                        status = "Approved";
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Starting registration, please wait...", Toast.LENGTH_LONG).show();
                        registerUser(name, image, status, thumb_image);
                        writeNewUser();

                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            //RegisterFirestore();
                        }
                    });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    //progressDialog.dismiss();
                }
            });
        }else{
            registerUser(name, image, status, thumb_image);
            writeNewUser();
        }
    }

    private void registerUser(String name, String image, String status, String thumb_image ) {
        Users details = new Users(name, image, status, thumb_image);
        mDatabase.child(userId).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });



    }
}
