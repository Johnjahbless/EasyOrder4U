package com.app.android.june.easyorder4u.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.ViewProfileActivity;
import com.app.android.june.easyorder4u.helpers.comments;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends
        RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<comments> eventsList;
    private Context context;
    public CommentsAdapter(List<comments> list, Context ctx ) {
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
                .inflate(R.layout.comments_layout, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;

        final comments event = eventsList.get(position);
        holder.name.setText(event.getName());
        holder.date.setText(event.getDate());

       /* String timeStamp = event.getDate();
        long myTime = Long.parseLong(timeStamp);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(myTime);
        String cal[] = calendar.getTime().toString().split(" ");
        String time_of_message = cal[1]+","+cal[2]+"  "+cal[3].substring(0,5);
        holder.date.setText(time_of_message); */
        String desc = event.getComments();
        if (desc.length() > 500){
            holder.comments.setText(desc.substring(0, 500));
        }else {
            holder.comments.setText(desc);
        }
        //String image = event.getPhoto();

        Picasso.with(context)
                .load(event.getPhoto())
                .placeholder(R.drawable.food)
                .into(holder.imageView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, comments, date;
        String Id;
        ImageView imageView;

        public ViewHolder(final View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            comments = (TextView) view.findViewById(R.id.comment);
            date = (TextView) view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.profile);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //cardView.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        //Id = eventsList.get(pos).getId();
                        String userID = eventsList.get(pos).getUserId();

                        Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                        intent.putExtra("userID", userID);
                        view.getContext().startActivity(intent);
                    }
                }});
        }
    }


}
