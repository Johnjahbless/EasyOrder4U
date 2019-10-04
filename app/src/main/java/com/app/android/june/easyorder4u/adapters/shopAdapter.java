package com.app.android.june.easyorder4u.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android.june.easyorder4u.HfSubmitActivity;
import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.helpers.Shop;
import com.squareup.picasso.Picasso;

import java.util.List;

public class shopAdapter extends
        RecyclerView.Adapter<shopAdapter.ViewHolder> {
    private List<Shop> eventsList;
    private Context context;
    public shopAdapter(List<Shop> list, Context ctx ) {
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
                .inflate(R.layout.shops_layout, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;
       // RequestManager requestManager = Glide.with(context);
        final Shop event = eventsList.get(position);
        holder.name.setText(event.getShopName());
        holder.location.setText(event.getShopAddress());
        //String image = event.getPhoto();
       /* RequestBuilder requestBuilder = requestManager.load(event.getShopPic());
        requestBuilder.into(holder.imageView); */
        Picasso.with(context)
                .load(event.getShopPic())
                .placeholder(R.drawable.food)
                .into(holder.imageView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, date;
        String  Location, Photo, Name, Email, Date, Time, Comments,Id, Profile;
        Double Likes;
        ImageView imageView;

        public ViewHolder(final View view) {
            super(view);

            name = view.findViewById(R.id.name);
            location = view.findViewById(R.id.location);
            //date = (TextView) view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Name = eventsList.get(pos).getShopName();
                        Location = eventsList.get(pos).getShopAddress();
                        //Photo = eventsList.get(pos).getShopPic();
                        Id = eventsList.get(pos).getId();
                       // Likes = eventsList.get(pos).getShopLikes();
                       // String city = eventsList.get(pos).getShopCity();
                       // String state = eventsList.get(pos).getShopState();
                        String userID = eventsList.get(pos).getUser_id();
                        //Date = eventsList.get(pos).getDate();
                        Intent intent = new Intent(view.getContext(), HfSubmitActivity.class);
                        intent.putExtra("name", Name);
                        intent.putExtra("location", Location);
                        intent.putExtra("userId", userID);
                        intent.putExtra("id", Id);
                        view.getContext().startActivity(intent);
                    }
                }});
        }
    }


}