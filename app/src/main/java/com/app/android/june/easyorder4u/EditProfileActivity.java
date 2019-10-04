package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ImageView imageView;
    private EditText etEmail, etWork, etState, etLga, full_Name, etPhone, etHome, etCountry;
    private String email, state, lga, name, work, phone, home, userId, status, gender, photo_url, ID, formattedDate;
    private ProgressDialog pDialog;
    private static final String TAG = "SignUpActivity";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    RelativeLayout relativeLayout;
    Integer value = 0;
    TextView textView;
    Long date;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.image_profile);
        full_Name = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etHome = findViewById(R.id.home);
        etCountry = findViewById(R.id.country);
        etWork = findViewById(R.id.work);
        etLga = findViewById(R.id.lga);
        etState = findViewById(R.id.state);
        textView = findViewById(R.id.text);

        relativeLayout = findViewById(R.id.rootLayout);
        Button buttoText = findViewById(R.id.buttonText);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        if (getIntent().hasExtra("Name")) {
            value = 1;
            name = getIntent().getExtras().getString("Name");
            gender = getIntent().getExtras().getString("Gender");
            email = getIntent().getExtras().getString("Email");
            photo_url = getIntent().getExtras().getString("Photo");
            phone = getIntent().getExtras().getString("Phone");
            home = getIntent().getExtras().getString("home");
            ID = getIntent().getExtras().getString("userID");
            full_Name.setText(name);
            etEmail.setText(email);
            etPhone.setText(phone);
            etHome.setText(home);
            etWork.setText(work);
            etLga.setText(lga);
            etState.setText(state);
            Picasso.with(this)
                    .load(photo_url)
                    .placeholder(R.drawable.pic)
                    .into(imageView);

        }
    }


    public void cancel(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void selectImage(View view) {
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

    public void submit(View view) {
        name = full_Name.getText().toString();
        email = etEmail.getText().toString().toLowerCase().trim();
        phone = etPhone.getText().toString();
        home = etHome.getText().toString().toLowerCase();
        work = etWork.getText().toString().toLowerCase();
        state = etState.getText().toString().toLowerCase();
        lga = etLga.getText().toString().toLowerCase();
        if (validateInputs()) {
            upload_imageTo_storage();

        }
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
        if (KEY_EMPTY.equals(work)) {
            etWork.setError("Invalid input");
            etWork.requestFocus();
            return false;
        }
        if (phone.length() < 11){
            etPhone.setError("Invalid input");
            etPhone.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(state)) {
            etState.setError("Invalid input");
            etState.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(lga)) {
            etLga.setError("Invalid input");
            etLga.requestFocus();
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
        if (gender == null) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (filePath == null && photo_url == null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private void displayLoader() {
        pDialog = new ProgressDialog(EditProfileActivity.this);
        pDialog.setMessage("Please wait...");
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
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Starting registration, please wait...", Toast.LENGTH_LONG).show();

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
        }else if (photo_url != null) {
            writeNewUser();
        }else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewUser() {
        displayLoader();
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("Name", name);
            newContact.put("Gender", gender);
            newContact.put("Email", email);
            newContact.put("Profile_pic", photo_url);
            newContact.put("Occupation", work);
            newContact.put("UserID", userId);
            newContact.put("Phone", phone);
            newContact.put("State", state);
            newContact.put("Lga", lga);
            newContact.put("HomeAddress", home);
        db.collection("Customers").document("users").collection("users").document(userId).update(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                            onBackPressed();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(EditProfileActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();
                                                  pDialog.dismiss();
                                              }
                                          }
                    );

        }
}
