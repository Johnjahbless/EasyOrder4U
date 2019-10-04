package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.june.easyorder4u.adapters.CommentsAdapter;
import com.app.android.june.easyorder4u.helpers.comments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewFoodActivity extends AppCompatActivity {
    String foodPrice, foodPhoto, foodName, foodId, userId, shopName, shopCity, shopState, shopAddress,
            foodDesc, foodTime, foodTags, date, userID, shopID, comments, userName, userPhoto, userEmail, userHome, userPhone, userGender;

    ImageView imageView;
    CheckBox favourite;
    RatingBar ratingBar;
    TextView name, num, review, price, desc, totalText, textPieces;
    DecimalFormat df = new DecimalFormat("####0");
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference users;
    Double likes = 0.00, totalPrice = 0.00, mainPrice = 0.00, total = 1.00;
    EditText etComments;
    RecyclerView friendList;
    CommentsAdapter recyclerViewAdapter;
    List<comments> eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        getSupportActionBar().hide();
        transparentStatusAndNavigation();

        if (getIntent().hasExtra("foodname")) {
            foodName = Objects.requireNonNull(getIntent().getExtras()).getString("foodname");
            foodPrice = getIntent().getExtras().getString("foodprice");
            foodPhoto = getIntent().getExtras().getString("foodphoto");
            likes = getIntent().getExtras().getDouble("foodlikes");
            foodId = getIntent().getExtras().getString("foodid");
            foodDesc = getIntent().getExtras().getString("fooddesc");

            foodTags = getIntent().getExtras().getString("foodTags");
            shopName = getIntent().getExtras().getString("shopName");
            shopCity = getIntent().getExtras().getString("shopCity");
            shopState = getIntent().getExtras().getString("shopState");
            shopAddress = getIntent().getExtras().getString("shopAddress");
            userID = getIntent().getExtras().getString("userID");
            shopID = getIntent().getExtras().getString("shopID");

        }

    imageView = findViewById(R.id.image2);
    name = findViewById(R.id.foodName);
    num = findViewById(R.id.num);
    review = findViewById(R.id.people);
    price = findViewById(R.id.price);
    desc = findViewById(R.id.description);
    favourite = findViewById(R.id.favourite);
    ratingBar = findViewById(R.id.rating);
    etComments = findViewById(R.id.comments);
    friendList = findViewById(R.id.comment_recycler);
    totalText = findViewById(R.id.total);
    textPieces = findViewById(R.id.pieces);


        name.setText(foodName);
        price.setText("₦" + foodPrice);
        String trim = foodPrice.trim();
        totalPrice = Double.parseDouble(trim);
        mainPrice = Double.parseDouble(trim);
        desc.setText(foodDesc);
        num.setText(df.format(likes/10));
        review.setText("   (" + likes + " reviews)");
        Picasso.with(this)
                .load(foodPhoto)
                .placeholder(R.drawable.food)
                .into(imageView);
        date   = DateFormat.getTimeInstance().format(new Date());
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        checkFavourite();
        getUser();
        eventList = new ArrayList<>();
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        friendList.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(friendList.getContext(),
                        recyclerLayoutManager.getOrientation());
        friendList.addItemDecoration(dividerItemDecoration);
        getFriendList();


}

    private void getUser() {
        users =  db.collection("Vendors").document("users").collection("users").document(userId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          userName = doc.getString("Name");
                                                          userEmail = doc.getString("Email");
                                                          userPhoto = doc.getString("Profile_pic");
                                                          userPhone = doc.getString("Phone");
                                                          userGender = doc.getString("Gender");
                                                          userHome = doc.getString("HomeAddress");

                                                      }else {
                                                          users =  db.collection("Customers").document("users").collection("users").document(userId);
                                                          users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        // pDialog.dismiss();
                                                                                                        DocumentSnapshot doc = task.getResult();
                                                                                                        if (doc.exists()) {
                                                                                                            //pDialog.dismiss();
                                                                                                            userName = doc.getString("Name");
                                                                                                            userEmail = doc.getString("Email");
                                                                                                            userPhoto = doc.getString("Profile_pic");
                                                                                                            userPhone = doc.getString("Phone");
                                                                                                            userGender = doc.getString("Gender");
                                                                                                            userHome = doc.getString("HomeAddress");

                                                                                                        }else {
                                                                                                            Toast.makeText(ViewFoodActivity.this, "user does not exit", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                          )
                                                                  .addOnFailureListener(new OnFailureListener() {
                                                                      @Override
                                                                      public void onFailure(@NonNull Exception e) {
                                                                          // pDialog.dismiss();
                                                                          Toast.makeText(ViewFoodActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                                                                      }
                                                                  });
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(ViewFoodActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkFavourite() {
        users =  db.collection("Customers").document("Favourites").collection(userId).document(foodId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          favourite.setChecked(true);
                                                      }else {
                                                          favourite.setChecked(false);
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

    public void onChecked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.favourite:
                if (checked){
                    addToFavourte();
                }else {
                    removeFromFavourte();
                }
                break;
        }
    }

    private void removeFromFavourte() {
        if (user!= null){
            db.collection("Customers").document("Favourites").collection(userId).document(foodId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // pDialog.dismiss();

                            Toast.makeText(ViewFoodActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                            UpdateData2();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  // Toast.makeText(AticlesActivity.this, "ERROR" + e.toString(),
                                                  //   Toast.LENGTH_SHORT).show();
                                                  //pDialog.dismiss();
                                              }
                                          }
                    );

        }

    }

    private void UpdateData2() {
        likes -= 1;
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("ProductLikes", likes);
        db.collection("Vendors").document("services").collection("services").document(foodId).update(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // pDialog.dismiss();

                        Toast.makeText(ViewFoodActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                        //UpdateData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              // Toast.makeText(AticlesActivity.this, "ERROR" + e.toString(),
                                              //  Toast.LENGTH_SHORT).show();
                                              //pDialog.dismiss();
                                          }
                                      }
                );
    }

    private void addToFavourte() {
        if (user != null) {
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("ShopName", shopName);
            newContact.put("ShopAddress", shopAddress);
            newContact.put("ProductName", foodName);
            newContact.put("ProductCity", shopCity);
            newContact.put("ProductDesc", foodDesc);
            newContact.put("ProductPic", foodPhoto);
            newContact.put("ProductPrice", foodPrice);
            newContact.put("ProductState", shopState);
            newContact.put("User_id", userID);
            newContact.put("ShopId", shopID);
            newContact.put("ProductTime", foodTime);
            newContact.put("FoodTags", foodTags);
            newContact.put("Date", date);
            newContact.put("ProductLikes", likes);
            newContact.put("Created_date", date);
            db.collection("Customers").document("Favourites").collection(userId).document(foodId).set(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // pDialog.dismiss();

                            Toast.makeText(ViewFoodActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                            UpdateData1();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(ViewFoodActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();
                                                  //pDialog.dismiss();
                                              }
                                          }
                    );

        }

    }

    private void UpdateData1() {
        likes += 1;
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("ProductLikes", likes);
        db.collection("Vendors").document("services").collection("services").document(foodId).update(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // pDialog.dismiss();

                        Toast.makeText(ViewFoodActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                        //UpdateData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewFoodActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              //pDialog.dismiss();
                                          }
                                      }
                );
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
    public void post(View view) {
        comments = etComments.getText().toString();

        if (TextUtils.isEmpty(comments)){
            Toast.makeText(this, "Please write a review to post", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Adding review...", Toast.LENGTH_LONG).show();
            postComment();
        }
    }

    private void postComment() {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("Name", userName);
        newContact.put("Date", date);
        newContact.put("Email", userEmail);
        newContact.put("Gender", userGender);
        newContact.put("Comments", comments);
        newContact.put("Photo", userPhoto);
        newContact.put("UserId", userId);
        db.collection("happiFoodie").document("comments").collection(foodId).add(newContact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ViewFoodActivity.this, "Posted Succesfully", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewFoodActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();

                                          }
                                      }
                );
    }
    private void getFriendList() {

        db.collection("happiFoodie").document("comments").collection(foodId).orderBy("Date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                comments e = doc.toObject(comments.class);
                                if (e != null){

                                }
                                e.setId(doc.getId());
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    CommentsAdapter(eventList,
                                    ViewFoodActivity.this);
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ViewFoodActivity.this, "error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        db.collection("happiFoodie").document("comments").collection(foodId).orderBy("Date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //List<viewItems> eventList = new ArrayList<>();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            doc.getDocument().toObject(comments.class);
                            // eventList.add(ee);
                            // recyclerViewAdapter.notifyDataSetChanged();
                            //do something...
                        }
                    }
                });
    }

    public void AddToCart(View view) {
        Toast.makeText(this, "Updating cart", Toast.LENGTH_SHORT).show();
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("foodName", foodName);
        newContact.put("foodPrice", totalPrice);
        newContact.put("foodPhoto", foodPhoto);
        newContact.put("foodLikes", likes);
        newContact.put("foodId", foodId);
        newContact.put("foodTags", foodTags);
        newContact.put("foodDesc", foodDesc);
        newContact.put("shopName", shopName);
        newContact.put("shopCity", shopCity);
        newContact.put("shopState", shopState);
        newContact.put("shopAddress", shopAddress);
        newContact.put("userID", userID);
        newContact.put("shopID", shopID);
        newContact.put("foodPieces", total);
        db.collection("Customers").document("users").collection("users").document(userId).collection("cart").document("cart").set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // pDialog.dismiss();

                        Toast.makeText(ViewFoodActivity.this, "Cart updated successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewFoodActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();

                                          }
                                      }
                );


    }

    public void BuyNow(View view) {
        Intent intent = new Intent(this, InstantCheckoutActivity.class);
        intent.putExtra("foodName", foodName);
        intent.putExtra("foodPrice", totalPrice);
        intent.putExtra("foodprice", foodPrice);
        intent.putExtra("foodPhoto", foodPhoto);
        intent.putExtra("foodLikes", likes);
        intent.putExtra("foodId", foodId);
        intent.putExtra("foodTags", foodTags);
        intent.putExtra("foodDesc", foodDesc);
        intent.putExtra("shopName", shopName);
        intent.putExtra("shopCity", shopCity);
        intent.putExtra("shopState", shopState);
        intent.putExtra("shopAddress", shopAddress);
        intent.putExtra("userID", userID);
        intent.putExtra("shopID", shopID);
        intent.putExtra("foodPieces", total);
        startActivity(intent);
    }

    public void Subtract(View view) {
        if (total == 1 ) {
            totalPrice = mainPrice * total;
            price.setText("₦" + totalPrice);
            totalText.setText(df.format(total));
            textPieces.setText(total+ " Pieces");
            Toast.makeText(this, "Minimum value reached", Toast.LENGTH_SHORT).show();
        }else {
            total -= 1;
            totalPrice -= mainPrice;
            price.setText("₦" + totalPrice);
            totalText.setText(df.format(total));
            textPieces.setText(total+ " Pieces");
        }
    }

    public void Add(View view) {
        if (total == 10 ) {
            totalPrice = mainPrice * total;
            price.setText("₦" + totalPrice);
            totalText.setText(df.format(total));
            textPieces.setText(total+ " Pieces");
            Toast.makeText(this, "Maximum value reached", Toast.LENGTH_SHORT).show();
        }else {
            total += 1;
            totalPrice = mainPrice * total;
            price.setText("₦" + totalPrice);
            totalText.setText(df.format(total));
            textPieces.setText(total+ " Pieces");
        }
    }

    public void callShop(View view) {
Intent intent = new Intent(this, ViewShopActivity.class);
intent.putExtra("shopID", shopID);
intent.putExtra("value", 1);
startActivity(intent);
    }

    public void viewShop(View view) {
        Intent intent = new Intent(this, ViewShopActivity.class);
        intent.putExtra("shopID", shopID);
        intent.putExtra("value", 0);
        startActivity(intent);
    }
}
