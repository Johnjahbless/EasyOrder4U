package com.app.android.june.easyorder4u.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.june.easyorder4u.EditProfileActivity;
import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.adapters.historyAdapter;
import com.app.android.june.easyorder4u.helpers.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
private View rootview;
private TextView about, history, nameText, emailText, nameText2, phoneText, locationText, genderText;
    private String email, state, lga, name, work, phone, home, userId, status, gender, photo_url, ID;
private ImageView edit_profile;
private LinearLayout about_layout;
private RecyclerView history_layout;
private View about_line, history_line;
private ImageView imageView, editProfile;
    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference users;
    private historyAdapter recyclerViewAdapter;
    private List<Food> eventList;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        about = rootview.findViewById(R.id.about_text);
        history = rootview.findViewById(R.id.history_text);
        about_layout = rootview.findViewById(R.id.layout_about);
        history_layout = rootview.findViewById(R.id.history_recycler);
        about_line = rootview.findViewById(R.id.about_line);
        history_line = rootview.findViewById(R.id.history_line);
        edit_profile = rootview.findViewById(R.id.edit_profile);
        imageView = rootview.findViewById(R.id.imgSender);
        nameText = rootview.findViewById(R.id.name);
        emailText = rootview.findViewById(R.id.email);
        edit_profile = rootview.findViewById(R.id.edit_profile);
        nameText2 = rootview.findViewById(R.id.name2);
        phoneText = rootview.findViewById(R.id.phone);
        locationText = rootview.findViewById(R.id.location);
        genderText = rootview.findViewById(R.id.gender);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        getUsers();
        eventList = new ArrayList<>();
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getContext());
        history_layout.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(history_layout.getContext(),
                        recyclerLayoutManager.getOrientation());
        history_layout.addItemDecoration(dividerItemDecoration);
        getFriendList();
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("Name", name);
                intent.putExtra("Email", email);
                intent.putExtra("Gender", gender);
                intent.putExtra("Photo", photo_url);
                intent.putExtra("Phone", phone);
                intent.putExtra("home", home);
                intent.putExtra("userID", userId);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_layout.setVisibility(View.GONE);
                about_layout.setVisibility(View.VISIBLE);
                history_line.setVisibility(View.GONE);
                about_line.setVisibility(View.VISIBLE);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_layout.setVisibility(View.VISIBLE);
                about_layout.setVisibility(View.GONE);
                history_line.setVisibility(View.VISIBLE);
                about_line.setVisibility(View.GONE);
            }
        });
        return rootview;
    }

    private void getUsers() {
        users =  db.collection("Customers").document("users").collection("users").document(userId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          name = doc.getString("Name");
                                                          email = doc.getString("Email");
                                                          photo_url = doc.getString("Profile_pic");
                                                          phone = doc.getString("Phone");
                                                          gender = doc.getString("Gender");
                                                          home = doc.getString("HomeAddress");
                                                          nameText.setText(name);
                                                          nameText2.setText(name);
                                                          emailText.setText(email);
                                                          locationText.setText(home);
                                                          phoneText.setText(phone);
                                                          genderText.setText(gender);
                                                          Picasso.with(getContext())
                                                                  .load(photo_url)
                                                                  .placeholder(R.drawable.pic)
                                                                  .into(imageView);
                                                      }else {
                                                          Toast.makeText(getContext(), "user does not exit", Toast.LENGTH_SHORT).show();
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
    private void getFriendList() {

        db.collection("Customers").document("users").collection("users").document(userId).collection("history").orderBy("Date", Query.Direction.DESCENDING)
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
                                    historyAdapter(eventList,
                                    getActivity());
                            history_layout.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        db.collection("Customers").document("users").collection("users").document(userId).collection("history").orderBy("Date", Query.Direction.DESCENDING)
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
