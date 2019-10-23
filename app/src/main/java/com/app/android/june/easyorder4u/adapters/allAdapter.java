package com.app.android.june.easyorder4u.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.ViewFoodActivity;
import com.app.android.june.easyorder4u.helpers.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class allAdapter extends
        RecyclerView.Adapter<allAdapter.ViewHolder> {
    private List<Food> eventsList;
    private Context context;
    DecimalFormat df = new DecimalFormat("####0.00");
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference users;

    public allAdapter(List<Food> list, Context ctx) {
        eventsList = list;
        context = ctx;
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_layout, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int itemPos = position;
        // RequestManager requestManager = Glide.with(context);
        Food event = eventsList.get(position);
        holder.foodTag.setText(event.getFoodTags());
        holder.name.setText(event.getProductName());
        holder.shopname.setText(event.getShopName());
        Double likes = event.getProductLikes();
        holder.num.setText(df.format(likes / 10));
        holder.reviews.setText("   (" + likes + " reviews)");
        String food_price = event.getProductPrice();
        Double myPrice = Double.parseDouble(food_price);
        holder.foodprice.setText("₦" + df.format(myPrice));
        // holder.price.setText("₦" + event.getProductPrice());
        //String image = event.getPhoto();
       /* RequestBuilder requestBuilder = requestManager.load(event.getFoodPic());
        requestBuilder.into(holder.imageView); */
        Picasso.with(context)
                .load(event.getProductPic())
                .placeholder(R.drawable.food)
                .into(holder.imageView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, num, reviews, foodTag, shopname, foodprice, addToCart;
        String foodPrice, foodPhoto, foodName, foodId, userId, shopName, shopCity, shopState, shopAddress,
                foodDesc, foodTime, foodTags, userID, shopID;
        Double Likes;
        ImageView imageView, delete;
        RatingBar ratingBar;

        public ViewHolder(final View view) {
            super(view);

            foodTag = view.findViewById(R.id.foodTags);
            shopname = view.findViewById(R.id.shopName);

            name = view.findViewById(R.id.foodName);
            num = view.findViewById(R.id.num);
            reviews = view.findViewById(R.id.reviews);
            foodprice = view.findViewById(R.id.foodPrice);
            //date = (TextView) view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.image);
            ratingBar = view.findViewById(R.id.rating);
            delete = view.findViewById(R.id.delete);
            addToCart = view.findViewById(R.id.addToCart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        foodName = eventsList.get(pos).getProductName();
                        foodPrice = eventsList.get(pos).getProductPrice();
                        foodPhoto = eventsList.get(pos).getProductPic();
                        foodId = eventsList.get(pos).getId();
                        Likes = eventsList.get(pos).getProductLikes();
                        foodTags = eventsList.get(pos).getFoodTags();
                        String desc = eventsList.get(pos).getProductDesc();

                        shopName = eventsList.get(pos).getShopName();
                        shopCity = eventsList.get(pos).getProductCity();
                        shopState = eventsList.get(pos).getProductState();
                        shopAddress = eventsList.get(pos).getShopAddress();
                        userID = eventsList.get(pos).getUser_id();
                        shopID = eventsList.get(pos).getShopId();
                        // Toast.makeText(context, "this: " + desc, Toast.LENGTH_LONG).show();
                        //Date = eventsList.get(pos).getDate();
                        Intent i = new Intent(view.getContext(), ViewFoodActivity.class);
                        i.putExtra("foodname", foodName);
                        i.putExtra("foodprice", foodPrice);
                        i.putExtra("foodphoto", foodPhoto);
                        i.putExtra("foodlikes", Likes);
                        i.putExtra("foodid", foodId);
                        i.putExtra("foodTags", foodTags);
                        i.putExtra("fooddesc", desc);
                        i.putExtra("shopName", shopName);
                        i.putExtra("shopCity", shopCity);
                        i.putExtra("shopState", shopState);
                        i.putExtra("shopAddress", shopAddress);
                        i.putExtra("userID", userID);
                        i.putExtra("shopID", shopID);
                        view.getContext().startActivity(i);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        db = FirebaseFirestore.getInstance();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userId = user.getUid();
                        foodId = eventsList.get(pos).getId();

                        db.collection("Customers").document("Favourites").collection(userId).document(foodId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // pDialog.dismiss();

                                        Toast.makeText(view.getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Toast.makeText(view.getContext(), "ERROR" + e.toString(),
                                                                      Toast.LENGTH_SHORT).show();
                                                              //pDialog.dismiss();
                                                          }
                                                      }
                                );

                    }
                }
            });

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        db = FirebaseFirestore.getInstance();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userId = user.getUid();
                        foodId = eventsList.get(pos).getId();
                        shopName = eventsList.get(pos).getShopName();
                        shopCity = eventsList.get(pos).getProductCity();
                        shopState = eventsList.get(pos).getProductState();
                        shopAddress = eventsList.get(pos).getShopAddress();
                        foodName = eventsList.get(pos).getProductName();
                       Integer foodPrice = Integer.parseInt(eventsList.get(pos).getProductPrice());
                        foodPhoto = eventsList.get(pos).getProductPic();
                        foodDesc = eventsList.get(pos).getProductDesc();
                        foodTags = eventsList.get(pos).getFoodTags();
                        foodTime = eventsList.get(pos).getProductTime();
                        userID = eventsList.get(pos).getUser_id();
                        shopID = eventsList.get(pos).getShopId();
                        Likes = eventsList.get(pos).getProductLikes();
                        Map<String, Object> newContact = new HashMap<>();
                        newContact.put("foodName", foodName);
                        newContact.put("foodPrice", foodPrice);
                        newContact.put("foodPhoto", foodPhoto);
                        newContact.put("foodLikes", Likes);
                        newContact.put("foodId", foodId);
                        newContact.put("foodTags", foodTags);
                        newContact.put("foodDesc", foodDesc);
                        newContact.put("shopName", shopName);
                        newContact.put("shopCity", shopCity);
                        newContact.put("shopState", shopState);
                        newContact.put("shopAddress", shopAddress);
                        newContact.put("userID", userID);
                        newContact.put("shopID", shopID);
                        newContact.put("foodPieces", 1);

                        db.collection("Customers").document("users").collection("users").document(userId).collection("cart").document("cart").set(newContact)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // pDialog.dismiss();

                                        Toast.makeText(view.getContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Toast.makeText(view.getContext(), "ERROR" + e.toString(),
                                                                      Toast.LENGTH_SHORT).show();

                                                          }
                                                      }
                                );
                    }


                }


            });
        }
    }
}