package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class InstantCheckoutActivity extends AppCompatActivity {
TextView etUsername, etAddress, etFoodTags, etFoodName, etShopName, etLikes, etReviews, etPrice, etPieces, etTotal;
String username, userAddress, paymentMethod = "pay on delivery", foodName, shopName,  review, foodPrice,foodPhoto, foodId, foodTags, foodDesc,
    shopCity, shopState, shopAddress, shopID, userID, userId, useremail, userPhoto, orderId, orderId2, userPhone, userGender, userHome, newAddress,
    mon, tue, wed, thu, fri, sat, sun, shopOrder, order = "YES";
Integer value = 2, valueTime;
Double likes, totalPrice, pieces;
ImageView imageView;
    FirebaseFirestore db;
    DocumentReference users;
    DecimalFormat df = new DecimalFormat("####0");
    AlertDialog dialog;
    FirebaseUser user;
    private ProgressDialog pDialog;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" +
            "AAAAqsA5p9A:APA91bGjR9RTuFz-IdQcMuWJ4ClTNwYCUOteV_yhb3fpqtTxjeGP61_zLv1KTY_yWWIqMvVVIRa9wXY1RxzGvOAKKoA0Cjs_G8MNzH0Y06iq8wbaqqGO0Q1YhcU2cd0gNSoDu4JPoPZq";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private static final String SUBSCRIBE_TO = "userABC";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE, today;
    String TOPIC;
    long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_checkout);
        getSupportActionBar().hide();
        etUsername = findViewById(R.id.username);
        etAddress = findViewById(R.id.useraddress);
        imageView = findViewById(R.id.image);
        etFoodTags = findViewById(R.id.foodTags);
        etFoodName = findViewById(R.id.foodName);
        etShopName = findViewById(R.id.shopName);
        etLikes = findViewById(R.id.num);
        etReviews = findViewById(R.id.reviews);
        etPrice = findViewById(R.id.price);
        etPieces = findViewById(R.id.pieces);
        etTotal = findViewById(R.id.total);

        date   = new Date().getTime();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        getUser();
        //getConstants();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
       today= (new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
        //Toast.makeText(this, ", " + today, Toast.LENGTH_SHORT).show();
        getTimeOfday();
        if (getIntent().hasExtra("foodName")) {
            foodName = Objects.requireNonNull(getIntent().getExtras()).getString("foodName");
            totalPrice = getIntent().getExtras().getDouble("foodPrice");
            foodPrice = getIntent().getExtras().getString("foodprice");
            foodPhoto = getIntent().getExtras().getString("foodPhoto");
            likes = getIntent().getExtras().getDouble("foodLikes");
            foodId = getIntent().getExtras().getString("foodId");
            foodDesc = getIntent().getExtras().getString("foodDesc");
            pieces = getIntent().getExtras().getDouble("foodPieces");

            foodTags = getIntent().getExtras().getString("foodTags");
            shopName = getIntent().getExtras().getString("shopName");
            shopCity = getIntent().getExtras().getString("shopCity");
            shopState = getIntent().getExtras().getString("shopState");
            shopAddress = getIntent().getExtras().getString("shopAddress");
            userID = getIntent().getExtras().getString("userID");
            shopID = getIntent().getExtras().getString("shopID");
            etTotal.setText("₦" + totalPrice);
            etPieces.setText(pieces + " Pieces");
            etPrice.setText("₦" + totalPrice);
            etReviews.setText(df.format(likes) + " reviews");
            //etLikes.setText(df.format(likes));
            etShopName.setText(shopName);
            etFoodName.setText(foodName);
            etFoodTags.setText(foodTags);
            Picasso.with(this)
                    .load(foodPhoto)
                    .placeholder(R.drawable.food)
                    .into(imageView);

            getDays();




        }
    }

    private void getTimeOfday() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 9){
            valueTime = 0;
        }else if(timeOfDay >= 9 && timeOfDay < 16){
            valueTime = 1;
        }else if(timeOfDay >= 16 && timeOfDay < 17){
            valueTime = 1;
        }else if(timeOfDay >= 17 && timeOfDay < 24){
            valueTime = 0;
        }
    }

    public void cancel(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void editAddress(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstantCheckoutActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_layout, null);
        builder.setView(dialogView);

         dialog= builder.create();
        dialog.setCancelable(false);
        dialog.show();

        ImageView btnClose = dialog.findViewById(R.id.close);
        Button btn = dialog.findViewById(R.id.copyAddress);
        final EditText address = dialog.findViewById(R.id.address);
        address.setText(userHome);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Address = address.getText().toString();
                if (TextUtils.isEmpty(Address)) {
                    Toast.makeText(InstantCheckoutActivity.this, "Please enter your correct address", Toast.LENGTH_SHORT).show();
                } else {
                    newAddress = Address;
                    updateDatabase();

                }
            }
        });
    }

    private void updateDatabase() {
        if (value == 4){
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("HomeAddress", newAddress);
            db.collection("Vendors").document("users").collection("users").document(userId).update(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etAddress.setText(newAddress);
                            Toast.makeText(InstantCheckoutActivity.this, "Address updated", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();

                                              }
                                          }
                    );

        }else if (value == 5){
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("HomeAddress", newAddress);
            db.collection("Customers").document("users").collection("users").document(userId).update(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etAddress.setText(newAddress);
                            Toast.makeText(InstantCheckoutActivity.this, "Address updated", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();

                                              }
                                          }
                    );
        }

    }

    public void checkout(View view) {
        if (validates()){
            displayLoader();
            SendToVendor();
        }

    }

    private boolean validates() {
        if (!order.equals("YES")){
            Toast.makeText(this, "Sorry, you have been banned from making orders due to policy violation", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!shopOrder.equals("YES")) {
            Toast.makeText(this, "Sorry, this shop does not accept orders", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (valueTime == 0) {
            Toast.makeText(this, "Sorry, You can not make an order at this time", Toast.LENGTH_LONG).show();
            return false;
        }
            if (username == null) {
                Toast.makeText(this, "Network connection failed", Toast.LENGTH_LONG).show();
                return false;
            }
            if (value == 2){
                Toast.makeText(this, "Card payment is currently unavailable", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (today.equals("Mon")){
                if (mon.equals("0")){
                    Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                    return false;
                }else {
                    return true;
                }
        }if (today.equals("Tue")) {
            if (tue.equals("0")) {
                Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
            if (today.equals("Wed")) {
                if (wed.equals("0")) {
                    Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            }

        if (today.equals("Thu")) {
            if (thu.equals("0")) {
                Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
        if (today.equals("Fri")) {
            if (fri.equals("0")) {
                Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
        if (today.equals("Sat")) {
            if (sat.equals("0")) {
                Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
        if (today.equals("Sun")) {
            if (sun.equals("0")) {
                Toast.makeText(this, "Sorry, this vendor does not deliver today", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }

            return true;
        }


    private void displayLoader() {
        pDialog = new ProgressDialog(InstantCheckoutActivity.this);
        pDialog.setMessage("Processing.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void SendToVendor() {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("UserName", username);
        newContact.put("FoodName", foodName);
        newContact.put("Date", date);
        newContact.put("UserEmail", useremail);
        newContact.put("UserGender", userGender);
        newContact.put("UserPhoto", userPhoto);
        newContact.put("UserPhone", userPhone);
        newContact.put("Likes", likes);
        newContact.put("FoodPhoto", foodPhoto);
        newContact.put("FoodDesc", foodDesc);
        newContact.put("FoodId", foodId);
        newContact.put("TotalPrice", totalPrice);
        newContact.put("FoodPieces", pieces);
        newContact.put("FoodTags", foodTags);
        newContact.put("ShopId", shopID);
        newContact.put("PaymentMethod", paymentMethod);
        newContact.put("Status", 0);
        newContact.put("Time", "time");
        newContact.put("UserId", userId);
        newContact.put("HomeAddress", newAddress);

        db.collection("Vendors").document("users").collection("users").document(userID).collection("orders").add(newContact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
orderId2 = documentReference.getId();
                       // Toast.makeText(InstantCheckoutActivity.this, "Address updated", Toast.LENGTH_SHORT).show();
                        sendNotification();
                        updateUser();
                        //UpdateHistory();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();

                                          }
                                      }
                );
    }

    private void updateUser() {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("FoodName", foodName);
        newContact.put("UserName", username);
        newContact.put("Date", date);
        newContact.put("ShopName", shopName);
        newContact.put("Likes", likes);
        newContact.put("FoodPhoto", foodPhoto);
        newContact.put("FoodDesc", foodDesc);
        newContact.put("FoodId", foodId);
        newContact.put("TotalPrice", totalPrice);
        newContact.put("FoodPieces", pieces);
        newContact.put("FoodTags", foodTags);
        newContact.put("ShopId", shopID);
        newContact.put("PaymentMethod", paymentMethod);
        newContact.put("Status", 0);
        newContact.put("OrderId", orderId2);
        newContact.put("Time", "time");
        newContact.put("UserId", userID);
        newContact.put("HomeAddress", newAddress);
        newContact.put("UserEmail", useremail);
        newContact.put("UserGender", userGender);
        newContact.put("UserPhoto", userPhoto);
        newContact.put("UserPhone", userPhone);
        db.collection("Customers").document("users").collection("users").document(userId).collection("orders").add(newContact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        orderId = documentReference.getId();
                        UpdateHistory();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();

                                          }
                                      }
                );
    }

    private void sendNotification() {
        TOPIC = "/topics/" + userID; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "New Order Received";
        NOTIFICATION_MESSAGE = "You have received a new order from " + username;

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

    private void UpdateHistory() {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("ProductName", foodName);
        newContact.put("UserName", username);
        newContact.put("Date", date);
        newContact.put("ShopName", shopName);
        newContact.put("ProductLikes", likes);
        newContact.put("ProductPic", foodPhoto);
        newContact.put("ProductPrice", foodPrice);
        newContact.put("ProductDesc", foodDesc);
        newContact.put("FoodId", foodId);
        newContact.put("TotalPrice", totalPrice);
        newContact.put("FoodPieces", pieces);
        newContact.put("FoodTags", foodTags);
        newContact.put("ShopId", shopID);
        newContact.put("PaymentMethod", paymentMethod);
        newContact.put("Status", 0);
        newContact.put("OrderId", orderId2);
        newContact.put("Time", "time");
        newContact.put("UserId", userID);
        newContact.put("HomeAddress", newAddress);
        newContact.put("UserEmail", useremail);
        newContact.put("UserGender", userGender);
        newContact.put("UserPhoto", userPhoto);
        newContact.put("UserPhone", userPhone);
        db.collection("Customers").document("users").collection("users").document(userId).collection("history").add(newContact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        updateVendor();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();

                                          }
                                      }
                );
    }

    private void updateVendor() {
        Map<String, Object> newContact = new HashMap<>();

        newContact.put("OrderId", orderId);
        db.collection("Vendors").document("users").collection("users").document(userID).collection("orders").document(orderId2).update(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Toast.makeText(InstantCheckoutActivity.this, "Your order has been sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InstantCheckoutActivity.this, OrdersActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(InstantCheckoutActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();

                                          }
                                      }
                );
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
                                                          value = 4;
                                                          username = doc.getString("Name");
                                                          useremail = doc.getString("Email");
                                                          userPhoto = doc.getString("Profile_pic");
                                                          userPhone = doc.getString("Phone");
                                                          userGender = doc.getString("Gender");
                                                          userHome = doc.getString("HomeAddress");
                                                          newAddress = doc.getString("HomeAddress");
                                                          etAddress.setText(userHome);
                                                          etUsername.setText(username);

                                                      }else {
                                                          value = 5;
                                                          users =  db.collection("Customers").document("users").collection("users").document(userId);
                                                          users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        // pDialog.dismiss();
                                                                                                        DocumentSnapshot doc = task.getResult();
                                                                                                        if (doc.exists()) {
                                                                                                            //pDialog.dismiss();
                                                                                                            username = doc.getString("Name");
                                                                                                            useremail = doc.getString("Email");
                                                                                                            userPhoto = doc.getString("Profile_pic");
                                                                                                            userPhone = doc.getString("Phone");
                                                                                                            userGender = doc.getString("Gender");
                                                                                                            userHome = doc.getString("HomeAddress");
                                                                                                            newAddress = doc.getString("HomeAddress");
                                                                                                            order = doc.getString("CanOrder");
                                                                                                            etAddress.setText(userHome);
                                                                                                            etUsername.setText(username);
                                                                                                        }else {
                                                                                                            Toast.makeText(InstantCheckoutActivity.this, "user does not exit", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                          )
                                                                  .addOnFailureListener(new OnFailureListener() {
                                                                      @Override
                                                                      public void onFailure(@NonNull Exception e) {
                                                                          // pDialog.dismiss();
                                                                          Toast.makeText(InstantCheckoutActivity.this, "error" + e, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(InstantCheckoutActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void Select_payment(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.delivery:
                if (checked)
                    value = 1;
                    paymentMethod = "pay on delivery";
                break;
            case R.id.card:
                if (checked)
                    value = 2;
                    paymentMethod = "pay with card";
                break;
        }
    }


    private void getDays() {
        users =  db.collection("Vendors").document("shops").collection("shops").document(shopID);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          mon = doc.getString("Monday");
                                                          tue = doc.getString("Tuesday");
                                                          wed = doc.getString("Wednesday");
                                                          thu = doc.getString("Thursday");
                                                          fri = doc.getString("Friday");
                                                          sat = doc.getString("Saturday");
                                                          sun = doc.getString("Sunday");
                                                          shopOrder = doc.getString("ShopOrder");

                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(InstantCheckoutActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }


}