package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {
    EditText editText;
    ProgressDialog pDialog;
    FirebaseUser user;
    FirebaseFirestore db;
    String name, email, userId, formattedDate, home, phone, date, gender;
    DocumentReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();
        Button startBtn = (Button) findViewById(R.id.sendEmail);
        editText = (EditText)findViewById(R.id.edit);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        SimpleDateFormat dd = new SimpleDateFormat("yyyy/MM/dd");
        formattedDate = ddf.format(c.getTime());
        date = dd.format(c.getTime());
        if (user == null) {
            Toast.makeText(this, "You're not login", Toast.LENGTH_SHORT).show();
        } else {
            userId = user.getUid();
            getUser();
        }


        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });

    }

    private void getUser() {
        users = db.collection("Customers").document("users").collection("users").document(userId);
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
                                                          phone = doc.getString("Phone");
                                                          gender = doc.getString("Gender");
                                                          home = doc.getString("HomeAddress");
                                                      }else {
                                                          Toast.makeText(ContactActivity.this, "You're not  login", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(ContactActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    protected void sendEmail() {
        String mesaage = editText.getText().toString();
        if (TextUtils.isEmpty(mesaage)) {
            editText.setError("Empty field!");
            editText.requestFocus();
        }else {
            displayLoader();
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("Name", name);
            newContact.put("Email", email);
            newContact.put("Message", mesaage);
            newContact.put("homeAddress", home);
            newContact.put("Phone", phone);
            newContact.put("Gender", gender);
            newContact.put("User_id", userId);
            newContact.put("Created_date", formattedDate);
            db.collection("messages").document("customers").collection(date).add(newContact)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            pDialog.dismiss();

                            Toast.makeText(ContactActivity.this, "Sent Succesfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(ContactActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();
                                                  pDialog.dismiss();
                                              }
                                          }
                    );

        }
       /* Log.i("Send email", "");
        String message = editText.getText().toString();
        String[] TO = {"juneapps12@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

*/
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(ContactActivity.this);
        pDialog.setMessage("Sending message.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void cancel(View view) {
        onBackPressed();
    }
}