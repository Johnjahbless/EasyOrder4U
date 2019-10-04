package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.android.june.easyorder4u.adapters.shopAdapter;
import com.app.android.june.easyorder4u.helpers.Shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    RecyclerView friendList;

    shopAdapter recyclerViewAdapter;
    List<Shop> eventList;
    ProgressBar progressBar;
    FirebaseFirestore db;
    DocumentReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getSupportActionBar().hide();
        progressBar = findViewById(R.id.progrss);
        friendList = findViewById(R.id.shops);
        db = FirebaseFirestore.getInstance();
        //user = FirebaseAuth.getInstance().getCurrentUser();
        //getDetails();
        eventList = new ArrayList<>();
       /* LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this); */
        friendList.setLayoutManager(new GridLayoutManager(this, 2));
        getShops();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
    public void cancel(View view) {
        onBackPressed();
    }
    private void getShops() {
        db.collection("Vendors").document("shops").collection("shops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Shop e = doc.toObject(Shop.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    shopAdapter(eventList, ShopActivity.this);
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ShopActivity.this, "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("shops").collection("shops")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //List<viewItems> eventList = new ArrayList<>();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            doc.getDocument().toObject(Shop.class);
                            // eventList.add(ee);
                            // recyclerViewAdapter.notifyDataSetChanged();
                            //do something...
                        }
                    }
                });
    }
}
