package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.june.easyorder4u.adapters.foodieAdapter;
import com.app.android.june.easyorder4u.helpers.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewShopActivity extends AppCompatActivity {
    TextView nameText, locationText, numberText, reviewText;
    RatingBar ratingBar;
    String name, location, shopId, photo, phone;
    ImageView imageView;
    Double num;
    DecimalFormat df = new DecimalFormat("####0.0");
    FirebaseFirestore db;
    DocumentReference users;
    RecyclerView friendList;
    Integer value = 0;

    foodieAdapter recyclerViewAdapter;
    List<Food> eventList;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop);
        getSupportActionBar().hide();
        transparentStatusAndNavigation();

        shopId = getIntent().getStringExtra("shopID");
        value = getIntent().getIntExtra("value", 0);
        nameText = findViewById(R.id.name);
        locationText = findViewById(R.id.location);
        numberText = findViewById(R.id.number);
        reviewText = findViewById(R.id.reviews);
        ratingBar = findViewById(R.id.rating);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progrss);
        friendList = findViewById(R.id.food_recycler);
        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
       /* LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this); */
        friendList.setLayoutManager(new GridLayoutManager(this, 2));
getShopDetails();

        getFoodItems();
    }

    private void getShopDetails() {
        users =  db.collection("Vendors").document("shops").collection("shops").document(shopId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          name = doc.getString("ShopName");
                                                          location = doc.getString("ShopAddress");
                                                          num = doc.getDouble("ShopLikes");
                                                          photo = doc.getString("ShopPic");
                                                          phone = doc.getString("ShopNumber");
                                                          nameText.setText(name);
                                                          locationText.setText(location);
                                                          numberText.setText(df.format(num/10));
                                                          reviewText.setText("   (" +num + " reviews)");
                                                          Picasso.with(ViewShopActivity.this)
                                                                  .load(photo)
                                                                  .placeholder(R.drawable.food)
                                                                  .into(imageView);

                                                          if (value == 1){
                                                              startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                                                          }

                                                      }else {
                                                         // favourite.setChecked(false);
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getFoodItems() {
        db.collection("Vendors").document("services").collection("services").whereEqualTo("ShopId", shopId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    foodieAdapter(eventList, ViewShopActivity.this);
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ViewShopActivity.this, "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("services").whereEqualTo("ShopId", shopId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //List<viewItems> eventList = new ArrayList<>();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            doc.getDocument().toObject(Food.class);
                            // eventList.add(ee);
                            // recyclerViewAdapter.notifyDataSetChanged();
                            //do something...
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
