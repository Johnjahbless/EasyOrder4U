package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CusLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseFirestore db;
    DocumentReference users;
    String userId, UserID;
    Double lati, logi;
    MapFragment mapFragment;
    EditText editText;

    CountDownTimer countDownTimer;
    private int counter = 40;
    AppLocationService appLocationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_location);
        getSupportActionBar().hide();
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        editText = findViewById(R.id.myLocation);
        db = FirebaseFirestore.getInstance();

        appLocationService = new AppLocationService(
                CusLocationActivity.this);
        if (getIntent().hasExtra("UserID")) {
            UserID = getIntent().getStringExtra("UserID");
            getUserLocation();
        }
    }

    private void getUserLocation() {
        users =  db.collection("Vendors").document("users").collection("users").document(UserID).collection("location").document("location");
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                       String latitude = doc.getString("Latitude");
                                                         String longitude = doc.getString("Longitude");
                                                           lati = Double.parseDouble(latitude);
                                                          logi = Double.parseDouble(longitude);
                                                          //Toast.makeText(CusLocationActivity.this, "vendor location" + "lati: " + lati +"log: " + logi, Toast.LENGTH_LONG).show();
                                                          mapFragment.getMapAsync(CusLocationActivity.this);
                                                          showAddress();
                                                          startTimer();
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pDialog.dismiss();
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lati, logi))
                .title("Vendor location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, logi), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lati, logi))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void startTimer() {

        countDownTimer = new CountDownTimer(40000, 1000) {
            public void onTick(long millisUntilFinished) {
                counter--;
            }
            public void onFinish() {
                counter = 40;
                getUserLocation();

            }

        }.start();
    }
    private void showAddress() {


        LocationAddress locationAddress = new LocationAddress();
        LocationAddress.getAddressFromLocation(lati, logi,
                getApplicationContext(), new GeocoderHandler());
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            editText.setText(locationAddress);
           // myLocation = locationAddress;
        }
    }
}