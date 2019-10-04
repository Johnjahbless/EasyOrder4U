package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HfSubmitActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ImageView imageView;
    private EditText etEmail, etWork, etState, etLga, full_Name, etPhone, etHome, etCountry, etShopName, etShopAddress;
    private String email, state, lga, name, work, phone, home, userId, country, gender, photo_url, ID, formattedDate, shopName, shopAddress, userID;
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
    DocumentReference users;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" +
            "AAAAndDVwl4:APA91bGzZGz2Le7rcdqDhufmQkQNqtTpXmmbbDaS1ExaUTKkV3l5VL-bAA54NQ_jfqp7lRjD8-ndzJcof8AHhMBLyYI7tE8-L_VDYMqbZhqbPlkQnmB79V6xBunWLxRSKBW56-N7P1qi";
    final private String contentType = "application/json";
    private static final String SUBSCRIBE_TO = "userABC";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf_submit);
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
        etShopAddress = findViewById(R.id.shopaddress);
        etShopName = findViewById(R.id.shopname);

        relativeLayout = findViewById(R.id.rootLayout);
        Button buttoText = findViewById(R.id.buttonText);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        date = new Date().getTime();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dddf = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = dddf.format(c.getTime());

        if (getIntent().hasExtra("name")) {
            shopName = getIntent().getExtras().getString("name");
            shopAddress = getIntent().getExtras().getString("location");
            userID = getIntent().getExtras().getString("userId");
            ID = getIntent().getExtras().getString("id");
            etShopName.setText(shopName);
            etShopAddress.setText(shopAddress);
            getUsers();
        }
    }


    private void getUsers() {
        users =  db.collection("Customers").document("users").collection("users").document(userId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          name = doc.getString("Name");
                                                          email = doc.getString("Email");
                                                          photo_url = doc.getString("Profile_pic");
                                                          phone = doc.getString("Phone");
                                                          gender = doc.getString("Gender");
                                                          home = doc.getString("HomeAddress");
                                                         lga = doc.getString("Lga");
                                                         state = doc.getString("State");
                                                         etLga.setText(lga);
                                                         etState.setText(state);
                                                          full_Name.setText(name);
                                                          etEmail.setText(email);
                                                          etHome.setText(home);
                                                          etPhone.setText(phone);
                                                         // genderText.setText(gender);
                                                          Picasso.with(HfSubmitActivity.this)
                                                                  .load(photo_url)
                                                                  .placeholder(R.drawable.pic)
                                                                  .into(imageView);
                                                      }else {
                                                          Toast.makeText(HfSubmitActivity.this, "user does not exit", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(HfSubmitActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
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
        country = etCountry.getText().toString().toLowerCase();
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

        if (KEY_EMPTY.equals(country)) {
            etCountry.setError("Invalid input");
            etCountry.requestFocus();
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
        pDialog = new ProgressDialog(HfSubmitActivity.this);
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
                        Toast.makeText(HfSubmitActivity.this, "Starting registration, please wait...", Toast.LENGTH_LONG).show();

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
        newContact.put("Country", country);
        newContact.put("Hf_delivery", "false");
        newContact.put("HomeAddress", home);
        newContact.put("Created_date", formattedDate);
        newContact.put("Date", date);
        newContact.put("Status", "Not Approved");
        db.collection("Vendors").document("users").collection("users").document(userID).collection("Hf").add(newContact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HfSubmitActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        onBackPressed();
                        sendNotification();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(HfSubmitActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();
                                          }
                                      }
                );
    }



    private void sendNotification() {
        TOPIC = "/topics/" + userID; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "New User Request";
        NOTIFICATION_MESSAGE = "You have received a new request from " + name;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
            //Toast.makeText(ChatActivity.this, "err  " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        // Toast.makeText(ChatActivity.this, "res  " + response.toString(), Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}


