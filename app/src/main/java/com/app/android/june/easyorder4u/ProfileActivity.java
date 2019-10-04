package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference users;
    private String email, state, lga, name, work, phone, home, userId, status, gender, photo_url, ID;
    private ImageView imageView;
    TextView name1, name2, emailText, phoneText, locationText, genderText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        transparentStatusAndNavigation();
        imageView = findViewById(R.id.circleImageViewUserImage);

        name1 = findViewById(R.id.name);
        name2 = findViewById(R.id.name2);
        emailText = findViewById(R.id.email);
        phoneText = findViewById(R.id.phone);
        locationText = findViewById(R.id.location);
        genderText = findViewById(R.id.gender);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        getUsers();

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
                                                          name1.setText(name);
                                                          name2.setText(name);
                                                          emailText.setText(email);
                                                          locationText.setText(home);
                                                          phoneText.setText(phone);
                                                          genderText.setText(gender);
                                                          Picasso.with(ProfileActivity.this)
                                                                  .load(photo_url)
                                                                  .placeholder(R.drawable.pic)
                                                                  .into(imageView);
                                                      }else {
                                                          Toast.makeText(ProfileActivity.this, "user does not exit", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View view) {
        onBackPressed();
    }

    public void chat(View view) {
        finish();
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("Name", name);
        intent.putExtra("Email", email);
        intent.putExtra("Gender", gender);
        intent.putExtra("Photo", photo_url);
        intent.putExtra("Phone", phone);
        intent.putExtra("home", home);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }


    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
