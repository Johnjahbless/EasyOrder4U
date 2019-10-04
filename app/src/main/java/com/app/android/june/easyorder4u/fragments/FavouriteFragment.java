package com.app.android.june.easyorder4u.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.adapters.favouriteAdapter;
import com.app.android.june.easyorder4u.helpers.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {
    private View rootview;

    private RecyclerView friendList;
    private favouriteAdapter recyclerViewAdapter;
    private List<Food> eventList;
   private FirebaseUser user;
   private FirebaseFirestore db;
    private DocumentReference users;
    private String userId;
    
    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_favourite, container, false);
        friendList = rootview.findViewById(R.id.favouriteRecycler);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        eventList = new ArrayList<>();
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getContext());
        friendList.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(friendList.getContext(),
                        recyclerLayoutManager.getOrientation());
        friendList.addItemDecoration(dividerItemDecoration);
        getFriendList();



        return rootview;
    }

    private void getFriendList() {

        db.collection("Customers").document("Favourites").collection(userId).orderBy("Date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);
                                if (e != null){

                                }
                                e.setId(doc.getId());
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    favouriteAdapter(eventList,
                                    getActivity());
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        db.collection("Customers").document("Favourites").collection(userId).orderBy("Date", Query.Direction.DESCENDING)
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //List<viewItems> eventList = new ArrayList<>();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            doc.getDocument().toObject(Food.class);
                            // eventList.add(ee);
                            // recyclerViewAdapter.notifyDataSetChanged();
                            //do something...
                        }
                    }
                });
    }

}
