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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android.june.easyorder4u.CusLocationActivity;
import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.ViewOrderActivity;
import com.app.android.june.easyorder4u.helpers.Orders;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class orderAdapter extends
        RecyclerView.Adapter<orderAdapter.ViewHolder> {
    private List<Orders> eventsList;
    private Context context;
    DecimalFormat df = new DecimalFormat("####0.00");
    public orderAdapter(List<Orders> list, Context ctx ) {
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
                .inflate(R.layout.order_layout, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;

        final Orders event = eventsList.get(position);
        holder.name.setText(event.getFoodName());
        holder.shopName.setText(event.getShopName());
        holder.location.setText(event.getHomeAddress());

        Integer pieces = event.getFoodPieces();
        Integer totalPrice = event.getTotalPrice();

        String pieces2 = String.valueOf(pieces);
        //String price2 = String.valueOf(totalPrice);
        holder.pieces.setText(pieces2 + " Pieces    ");
        holder.price.setText("₦" + df.format(totalPrice));
       // holder.pieces.setText(event.getFoodPieces());
       // holder.price.setText(event.getTotalPrice());

       /* String timeStamp = event.getDate();
        long myTime = Long.parseLong(timeStamp);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(myTime);
        String cal[] = calendar.getTime().toString().split(" ");
        String time_of_message = cal[1]+","+cal[2]+"  "+cal[3].substring(0,5);
        holder.date.setText(time_of_message); */
        Integer status = event.getStatus();
        if (status == 0){
            holder.status.setText("Not approved");
        }else if (status == 2) {
            holder.status.setText("Canceled");
        }else {
            holder.status.setText("Approved");
        }
        //String image = event.getPhoto();

        Picasso.with(context)
                .load(event.getFoodPhoto())
                .placeholder(R.drawable.food)
                .into(holder.imageView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, shopName, location, pieces, price, status;
        String Id;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(final View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.foodname);
            imageView = view.findViewById(R.id.imageView);
            shopName = view.findViewById(R.id.shopname);
            location = view.findViewById(R.id.location);
            pieces = view.findViewById(R.id.pieces);
            price = view.findViewById(R.id.price);
            status = view.findViewById(R.id.status);
            cardView = view.findViewById(R.id.cardView);

           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //cardView.setVisibility(View.VISIBLE);
                    return false;
                }
            }); */

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        //Id = eventsList.get(pos).getId();
                        Integer status = eventsList.get(pos).getStatus();
                        final String userID = eventsList.get(pos).getUserId();
                        final String orderID = eventsList.get(pos).getOrderId();
                        final String ID = eventsList.get(pos).getId();

                        if (status == 0){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                            alertDialogBuilder.setTitle("Your order status");
                            alertDialogBuilder
                                    .setCancelable(true)
                                    .setMessage("Your order has been sent and waiting for approval")
                                    .setPositiveButton("Okay",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    dialog.cancel();
                                                }
                                            })

                                    .setNegativeButton("Cancel order", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                          Intent intent = new Intent(view.getContext(), ViewOrderActivity.class);
                                          intent.putExtra("UserID", userID);
                                          intent.putExtra("OrderID", orderID);
                                          intent.putExtra("ID", ID);
                                          view.getContext().startActivity(intent);
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }else if (status == 2){
                             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                            alertDialogBuilder.setTitle("Your order status");
                            alertDialogBuilder
                                    .setCancelable(true)
                                    .setMessage("Your have Canceled this order")
                                    .setPositiveButton("Okay",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(view.getContext(), ViewOrderActivity.class);
                                    intent.putExtra("OrderID", ID);
                                    intent.putExtra("ID", ID);
                                    intent.putExtra("value3", "io");
                                    view.getContext().startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                            alertDialogBuilder.setTitle("Your order status");
                            alertDialogBuilder
                                    .setCancelable(true)
                                    .setMessage("Your order has been approved and is on route")
                                    .setPositiveButton("Track item",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(view.getContext(), CusLocationActivity.class);
                                                    intent.putExtra("UserID", userID);
                                                    view.getContext().startActivity(intent);
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                }});
        }
    }


}

