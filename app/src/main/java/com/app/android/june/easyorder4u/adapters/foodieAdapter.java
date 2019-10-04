package com.app.android.june.easyorder4u.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.ViewFoodActivity;
import com.app.android.june.easyorder4u.helpers.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class foodieAdapter extends
        RecyclerView.Adapter<foodieAdapter.ViewHolder> {
    private List<Food> eventsList;
    private Context context;

    public foodieAdapter(List<Food> list, Context ctx) {
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
                .inflate(R.layout.foods_layout, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;
        // RequestManager requestManager = Glide.with(context);
        final Food event = eventsList.get(position);
        holder.name.setText(event.getProductName());
        holder.price.setText("₦" + event.getProductPrice());
        //String image = event.getPhoto();
       /* RequestBuilder requestBuilder = requestManager.load(event.getFoodPic());
        requestBuilder.into(holder.imageView); */
        Picasso.with(context)
                .load(event.getProductPic())
                .placeholder(R.drawable.food)
                .into(holder.imageView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        String foodPrice, foodPhoto, foodName, foodId, userId, shopName, shopCity, shopState, shopAddress,
                foodDesc, foodTime, foodTags, date, userID, shopID;
        Double Likes;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(final View view) {
            super(view);

            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            //date = (TextView) view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.image);
            cardView = view.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
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


        }
    }


}