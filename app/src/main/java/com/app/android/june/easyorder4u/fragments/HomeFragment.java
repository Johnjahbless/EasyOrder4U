package com.app.android.june.easyorder4u.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.june.easyorder4u.MainActivity;
import com.app.android.june.easyorder4u.R;
import com.app.android.june.easyorder4u.ViewAllActivity;
import com.app.android.june.easyorder4u.adapters.foodAdapter;
import com.app.android.june.easyorder4u.adapters.nearmeAdapter;
import com.app.android.june.easyorder4u.adapters.popularAdapter;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    private TextView dishes, popular, sponsord, recent, nearme, breakText, lunchText, dinnerText, drinksText, pizzaText, shoesText, clothsText, beautyText, othersText;
    private ImageView imageView;
    private CardView breakfast, lunch, dinner;
    private FirebaseFirestore db;
    private RecyclerView friendList, friendList2, friendList3, friendList4, friendList5;
    private popularAdapter recyclerViewAdapter2, recyclerViewAdapter3, recyclerViewAdapter4;
    private nearmeAdapter recyclerViewAdapter5;
    private foodAdapter recyclerViewAdapter;
    private List<Food> eventList;
    private List<Food> eventList2;
    private List<Food> eventList3;
    private List<Food> eventList4;
    private List<Food> eventList5;
    private ProgressBar progressBar;
    private String userId, address, state;
    private FirebaseUser user;
    private DocumentReference users;

    DecimalFormat df = new DecimalFormat("####0");

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        dishes = rootView.findViewById(R.id.textClick);
        popular = rootView.findViewById(R.id.textClick2);
        sponsord = rootView.findViewById(R.id.textClick3);
        recent = rootView.findViewById(R.id.recent);
        imageView = rootView.findViewById(R.id.image2);
        breakfast = rootView.findViewById(R.id.breakfast);
        lunch = rootView.findViewById(R.id.lunch);
        dinner = rootView.findViewById(R.id.dinner);
        nearme = rootView.findViewById(R.id.nearme);
        progressBar = rootView.findViewById(R.id.progrss);
        friendList = rootView.findViewById(R.id.dishes);
        friendList2 = rootView.findViewById(R.id.popular_recycler);
        friendList3 = rootView.findViewById(R.id.sponsord_recycler);
        friendList4 = rootView.findViewById(R.id.recent_recycler);
        friendList5 = rootView.findViewById(R.id.nearme_recycler);
        breakText = rootView.findViewById(R.id.breakText);
        lunchText = rootView.findViewById(R.id.lunchText);
        dinnerText = rootView.findViewById(R.id.dinnerText);
        pizzaText = rootView.findViewById(R.id.pizzaText);
        drinksText = rootView.findViewById(R.id.drinksText);
        shoesText = rootView.findViewById(R.id.shoes);
        clothsText = rootView.findViewById(R.id.cloths);
        beautyText = rootView.findViewById(R.id.beauty);
        othersText = rootView.findViewById(R.id.others);
        CardView othersCard = rootView.findViewById(R.id.othersCard);
        CardView beautyCard = rootView.findViewById(R.id.beautyCard);
        CardView pizzaCard = rootView.findViewById(R.id.pizza);
        CardView clothsCard = rootView.findViewById(R.id.clothsCard);
        CardView shoesCard = rootView.findViewById(R.id.shoesCard);
        CardView fashionCard = rootView.findViewById(R.id.fashion);
        CardView electronicCard = rootView.findViewById(R.id.electronics);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        eventList = new ArrayList<>();
        eventList2 = new ArrayList<>();
        eventList3 = new ArrayList<>();
        eventList4 = new ArrayList<>();
        eventList5 = new ArrayList<>();
        friendList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendList2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendList3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendList4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendList5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getUsers();
        getFoodItems2();
        getFoodItems22();
        getFoodItems4();

        getFoodItems3();
        getFoodItems33();
        getTags();
        dishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("name", "dishes");
                startActivity(intent);
            }
        });
        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("popular", "popular");
                startActivity(intent);
            }
        });
        sponsord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("sponsord", "sponsord");
                startActivity(intent);
            }
        });

        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("recent", "sponsord");
                startActivity(intent);
            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("name", "breakfast");
                startActivity(intent);
            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("name", "lunch");
                startActivity(intent);
            }
        });
        pizzaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("name", "dinner");
                startActivity(intent);
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("name", "dinner");
                startActivity(intent);
            }
        });
        othersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });
        beautyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });
        clothsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });
        shoesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });

        fashionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });
        electronicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("others", "others");
                startActivity(intent);
            }
        });
        nearme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity.class);
                intent.putExtra("nearme", "dishes");
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void getFoodItems() {
        db.collection("Vendors").document("services").collection("services").whereArrayContains("ShopAddresses", state)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList.add(e);
                            }
                            recyclerViewAdapter = new
                                    foodAdapter(eventList, getContext());
                            friendList.setAdapter(recyclerViewAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("services").whereArrayContains("ShopAddresses", state)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getFoodItems2() {
        db.collection("Vendors").document("services").collection("services").orderBy("ProductLikes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList2.add(e);
                            }
                            recyclerViewAdapter2 = new
                                    popularAdapter(eventList2, getContext());
                            friendList2.setAdapter(recyclerViewAdapter2);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("services").orderBy("ProductLikes", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    private void getFoodItems22() {
        db.collection("Vendors").document("services").collection("others").orderBy("ProductLikes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList2.add(e);
                            }
                            recyclerViewAdapter2 = new
                                    popularAdapter(eventList2, getContext());
                            friendList2.setAdapter(recyclerViewAdapter2);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("others").orderBy("ProductLikes", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getFoodItems3() {
        db.collection("Vendors").document("services").collection("services").whereEqualTo("Sponsord", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList3.add(e);
                            }
                            recyclerViewAdapter3 = new
                                    popularAdapter(eventList3, getContext());
                            friendList3.setAdapter(recyclerViewAdapter3);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("services").whereEqualTo("Sponsord", 1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getFoodItems33() {
        db.collection("Vendors").document("services").collection("others").whereEqualTo("Sponsord", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList3.add(e);
                            }
                            recyclerViewAdapter3 = new
                                    popularAdapter(eventList3, getContext());
                            friendList3.setAdapter(recyclerViewAdapter3);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("others").whereEqualTo("Sponsord", 1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    private void getFoodItems5() {
        db.collection("Vendors").document("services").collection("services").whereArrayContains("ShopAddresses", address)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList5.add(e);
                            }
                            recyclerViewAdapter5 = new
                                    nearmeAdapter(eventList5, getContext());
                            friendList5.setAdapter(recyclerViewAdapter5);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("services").whereArrayContains("ShopAddresses", address)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    private void getFoodItems55() {
        db.collection("Vendors").document("services").collection("others").whereArrayContains("ShopAddresses", address)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<viewItems> eventList = new ArrayList<>();
                            //progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Food e = doc.toObject(Food.class);

                                e.setId(doc.getId());
                                //eventList.clear();
                                eventList5.add(e);
                            }
                            recyclerViewAdapter5 = new
                                    nearmeAdapter(eventList5, getContext());
                            friendList5.setAdapter(recyclerViewAdapter5);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        db.collection("Vendors").document("services").collection("others").whereArrayContains("ShopAddresses", address)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
private void getFoodItems4() {
    db.collection("Customers").document("users").collection("users").document(userId).collection("history").orderBy("Date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        //List<viewItems> eventList = new ArrayList<>();
                        //progressBar.setVisibility(View.GONE);
                        for (DocumentSnapshot doc : task.getResult()) {
                            Food e = doc.toObject(Food.class);

                            e.setId(doc.getId());
                            //eventList.clear();
                            eventList4.add(e);
                        }
                        recyclerViewAdapter4 = new
                                popularAdapter(eventList4, getContext());
                        friendList4.setAdapter(recyclerViewAdapter4);

                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(getContext(), "error: " + task.getException(), Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                }
            });

    db.collection("Customers").document("users").collection("users").document(userId).collection("history").orderBy("Date", Query.Direction.DESCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
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


    private void getUsers() {
        users =  db.collection("Customers").document("users").collection("users").document(userId);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          address = doc.getString("Lga");
                                                          state = doc.getString("Street name");
                                                          getFoodItems5();
                                                          getFoodItems55();
                                                          getFoodItems();
                                                         // Toast.makeText(getContext(), "a: " + address + "  " + state, Toast.LENGTH_LONG).show();
                                                      }else {
                                                          getUsers2();
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
    private void getTags() {
        users =  db.collection("Vendors").document("foodTags");
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                         Double breakfast = doc.getDouble("Breakfast");
                                                          Double lunch = doc.getDouble("Lunch");
                                                          Double  dinner = doc.getDouble("Dinner");
                                                          Double drinks = doc.getDouble("Drinks");
                                                          Double pizza = doc.getDouble("Pizza");
                                                          Double  tea = doc.getDouble("Tea");
                                                          Double burger = doc.getDouble("Burger");
                                                          Double shoes = doc.getDouble("Shoes");
                                                          Double cloths = doc.getDouble("Cloths");
                                                          Double beauty = doc.getDouble("Beauty");
                                                          Double othersValue = doc.getDouble("Others");
                                                          breakText.setText(df.format(breakfast) + " Items");
                                                          lunchText.setText(df.format(lunch) + " Items");
                                                          dinnerText.setText(df.format(dinner) + " Items");
                                                          pizzaText.setText(df.format(pizza) + " Items");
                                                          drinksText.setText(df.format(drinks) + " Items");
                                                          shoesText.setText(df.format(shoes) + " Items");
                                                          clothsText.setText(df.format(cloths) + " Items");
                                                          beautyText.setText(df.format(beauty) + " Items");
                                                          othersText.setText(df.format(othersValue) + " Items");

                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                        // Toast.makeText(ProfileActivity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void getUsers2() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Account Conflicting");
        alertDialogBuilder
                .setCancelable(false)
                .setIcon(R.drawable.ic_close)
                .setMessage("Sorry, this account is already in used in another application! please kindly register into this application with a different email address")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //exit app and kill the app from running
                                FirebaseAuth.getInstance().signOut();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);


                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

