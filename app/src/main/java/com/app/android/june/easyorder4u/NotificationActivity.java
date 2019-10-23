package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.android.june.easyorder4u.adapters.orderAdapter;
import com.app.android.june.easyorder4u.helpers.Orders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView friendList;
    orderAdapter recyclerViewAdapter;
    List<Orders> eventList;
    ProgressBar progressBar;
    FirebaseUser user;
    String userId;
    CountDownTimer countDownTimer;
    private int counter = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();

        friendList = findViewById(R.id.order_recycler);
        progressBar = findViewById(R.id.progrss);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        userId = user.getUid();
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        friendList.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(friendList.getContext(),
                        recyclerLayoutManager.getOrientation());
        friendList.addItemDecoration(dividerItemDecoration);
        getFriendList();

    }
    private void getFriendList() {
        db.collection("Customers").document("users").collection("users").document(userId).collection("orders").orderBy("Date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            //List<viewItems> eventList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Orders e = doc.toObject(Orders.class);
                                if (e != null){

                                }
                                e.setId(doc.getId());
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    orderAdapter(eventList,
                                    NotificationActivity.this);
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(NotificationActivity.this, "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Customers").document("users").collection("users").document(userId).collection("orders").orderBy("Date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //List<viewItems> eventList = new ArrayList<>();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            doc.getDocument().toObject(Orders.class);
                            // eventList.add(ee);
                            // recyclerViewAdapter.notifyDataSetChanged();
                            //do something...
                        }
                    }
                });
    }
    public void startTimer() {

        countDownTimer = new CountDownTimer(100000, 1000) {
            public void onTick(long millisUntilFinished) {
                counter--;
            }
            public void onFinish() {
                counter = 100;
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                //getFriendList();
                startTimer();

            }

        }.start();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        startTimer();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        countDownTimer.cancel();
        super.onPause();
    }

    @Override
    public void onStop() {
        countDownTimer.cancel();
        super.onStop();
    }
    public void cancel(View view) {
        onBackPressed();
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
}
