package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewOrderActivity extends AppCompatActivity {
String userID, orderID, ID, userId;
    FirebaseFirestore db;
    DocumentReference users;
    private ProgressDialog pDialog;
    FirebaseUser user;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" +
            "AAAAqsA5p9A:APA91bGjR9RTuFz-IdQcMuWJ4ClTNwYCUOteV_yhb3fpqtTxjeGP61_zLv1KTY_yWWIqMvVVIRa9wXY1RxzGvOAKKoA0Cjs_G8MNzH0Y06iq8wbaqqGO0Q1YhcU2cd0gNSoDu4JPoPZq";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        getSupportActionBar().hide();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        if (getIntent().hasExtra("UserID")) {
            userID = getIntent().getExtras().getString("UserID");
            orderID = getIntent().getExtras().getString("OrderID");
            ID = getIntent().getExtras().getString("ID");


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Cancel order");
            alertDialogBuilder
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_warning)
                    .setMessage("Are you sure you want to cancel this order!")
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            })
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //displayLoader();
                                    cancelOrder();
                                }
                            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if (getIntent().hasExtra("value3")){
            orderID = getIntent().getExtras().getString("OrderID");
            ID = getIntent().getExtras().getString("ID");
            deleteOrder();
        }
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void deleteOrder() {
        db.collection("Customers").document("users").collection("users").document(userId).collection("orders").document(ID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Toast.makeText(ViewOrderActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewOrderActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              onBackPressed();
                                          }
                                      }
                );
    }
    private void cancelOrder() {
        displayLoader();
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("Status", 2);
        db.collection("Vendors").document("users").collection("users").document(userID).collection("orders").document(orderID).update(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendNotification();
                        cancelOrder2();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewOrderActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();
                                              onBackPressed();
                                          }
                                      }
                );
    }

    private void cancelOrder2() {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("Status", 2);
        db.collection("Customers").document("users").collection("users").document(userId).collection("orders").document(ID).update(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        sendNotification2();
                        Toast.makeText(ViewOrderActivity.this, "Cancel Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(ViewOrderActivity.this, "ERROR" + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              pDialog.dismiss();
                                              onBackPressed();
                                          }
                                      }
                );
    }

    private void sendNotification() {
        TOPIC = "/topics/" + userID; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "An order has been canceled";
        NOTIFICATION_MESSAGE = "A user has canceled an order ";

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
    private void sendNotification2() {
        TOPIC = "/topics/" + userId; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "Your order has been canceled";
        NOTIFICATION_MESSAGE = "You have canceled your order ";

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
        sendNotification2(notification);
    }

    private void sendNotification2(JSONObject notification) {
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
}
