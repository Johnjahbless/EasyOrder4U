package com.app.android.june.easyorder4u.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.june.easyorder4u.InstantCheckoutActivity;
import com.app.android.june.easyorder4u.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private View rootView;
    private Button checkout;
    TextView etUsername, etAddress, etFoodTags, etFoodName, etShopName, etLikes, etReviews, etPrice, etPieces, etTotal;

    String foodPrice, foodPhoto, foodName, foodId, userId, shopName, shopCity, shopState, shopAddress,
            foodDesc, foodTime, foodTags, date, userID, shopID, comments, userName, userPhoto, userEmail, userHome, userPhone, userGender;
    FirebaseFirestore db;
    DocumentReference users;
    FirebaseUser user;
    DecimalFormat df = new DecimalFormat("####0");
    ImageView imageView;
    Double totalPrice = 0.00, pieces, likes;
    RelativeLayout layout;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        checkout = rootView.findViewById(R.id.checkout);
        imageView = rootView.findViewById(R.id.image);
        etFoodTags = rootView.findViewById(R.id.foodTags);
        etFoodName = rootView.findViewById(R.id.foodName);
        etShopName = rootView.findViewById(R.id.shopName);
        etLikes = rootView.findViewById(R.id.num);
        etReviews = rootView.findViewById(R.id.reviews);
        etPieces = rootView.findViewById(R.id.pieces);
        etTotal = rootView.findViewById(R.id.total);
        layout = rootView.findViewById(R.id.layout);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        getCartDetails();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InstantCheckoutActivity.class);
                intent.putExtra("foodName", foodName);
                intent.putExtra("foodPrice", totalPrice);
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
                intent.putExtra("foodPieces", pieces);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getCartDetails() {
        users =  db.collection("Customers").document("users").collection("users").document(userId).collection("cart").document("cart");
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          foodName = doc.getString("foodName");
                                                          totalPrice = doc.getDouble("foodPrice");
                                                          foodPhoto = doc.getString("foodPhoto");
                                                          likes = doc.getDouble("foodLikes");
                                                          foodId = doc.getString("foodId");
                                                          foodTags = doc.getString("foodTags");
                                                          foodDesc = doc.getString("foodDesc");
                                                          shopName = doc.getString("shopName");
                                                          shopCity = doc.getString("shopCity");
                                                          shopState = doc.getString("shopState");
                                                          shopAddress = doc.getString("shopAddress");
                                                          userID = doc.getString("userID");
                                                          shopID = doc.getString("shopID");
                                                          pieces = doc.getDouble("foodPieces");
                                                          layout.setVisibility(View.VISIBLE);
                                                          etTotal.setText("₦" + totalPrice);
                                                          etPieces.setText(pieces + " Pieces");
                                                          etReviews.setText(likes + " reviews");
                                                          etLikes.setText(df.format(likes));
                                                          etShopName.setText(shopName);
                                                          etFoodName.setText(foodName);
                                                          etFoodTags.setText(foodTags);
                                                          Picasso.with(getContext())
                                                                  .load(foodPhoto)
                                                                  .placeholder(R.drawable.food)
                                                                  .into(imageView);

                                                          checkout.setVisibility(View.VISIBLE);

                                                      }else {
                                                          Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        Toast.makeText(getContext(), "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

}
