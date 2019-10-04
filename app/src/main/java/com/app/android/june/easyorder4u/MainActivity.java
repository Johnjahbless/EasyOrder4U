package com.app.android.june.easyorder4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.android.june.easyorder4u.adapters.TabsAdapter;
import com.app.android.june.easyorder4u.fragments.CartFragment;
import com.app.android.june.easyorder4u.fragments.FavouriteFragment;
import com.app.android.june.easyorder4u.fragments.HomeFragment;
import com.app.android.june.easyorder4u.fragments.ProfileFragment;
import com.app.android.june.easyorder4u.fragments.SearchFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private String mLastUpdateTime, userId, myLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    FirebaseUser user;
    AppLocationService appLocationService;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private static final String SUBSCRIBE_TO = "userABC";

    FirebaseFirestore db;
    DocumentReference users;
    Fragment fragment;
    Integer value = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        appLocationService = new AppLocationService(
                MainActivity.this);
        //setupViewPager(mViewPager);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            FirebaseMessaging.getInstance().subscribeToTopic(userId);
            Show();
            Show2();
            init();
            //startLocationUpdates();
            startLocationButtonClick();

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        loadFragment(new HomeFragment());

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_favourite:
                        fragment = new FavouriteFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_search:
                        fragment = new SearchFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_cart:
                        fragment = new CartFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });

    }
     /*   mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                    navigation.setSelectedItemId(R.id.navigation_home);
                else if(position == 1)
                    navigation.setSelectedItemId(R.id.navigation_favourite);
                else if (position == 2)
                    navigation.setSelectedItemId(R.id.navigation_search);
                else if (position == 3)
                    navigation.setSelectedItemId(R.id.navigation_cart);
                else if (position == 4)
                    navigation.setSelectedItemId(R.id.navigation_profile);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }); */



    private void Show2() {
        db = FirebaseFirestore.getInstance();
        users = db.collection("Updates").document("customerApp");
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      // pDialog.dismiss();
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc.exists()) {
                                                          //pDialog.dismiss();
                                                          Double id = doc.getDouble("id");
                                                          if (id > 1.0){
                                                              Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                                                              startActivity(intent);
                                                          }
                                                      }else {
                                                          Map<String, Object> newContact = new HashMap<>();
                                                          newContact.put("id", 1.0);
                                                          db.collection("Updates").document("customerApp").set(newContact)
                                                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                      @Override
                                                                      public void onSuccess(Void aVoid) {

                                                                      }
                                                                  })
                                                                  .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                // Toast.makeText(AticlesActivity.this, "ERROR" + e.toString(),
                                                                                                //  Toast.LENGTH_SHORT).show();
                                                                                                //pDialog.dismiss();
                                                                                            }
                                                                                        }
                                                                  );
                                                      }
                                                  }
                                              }
                                          }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void Show() {
        SharedPreferences sharedPreferences = getSharedPreferences("notify", MODE_PRIVATE);
        Integer value = sharedPreferences.getInt("notifykey", 1);
        if (value == 1){
            //Toast.makeText(this, "Notification is on", Toast.LENGTH_SHORT).show();
            FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);

        }else {
            Toast.makeText(this, "Notification is off", Toast.LENGTH_LONG).show();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new FavouriteFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new CartFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
       // transaction.addToBackStack(false);
        transaction.commit();
    }
    public void notification(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("id", "userID");
        startActivity(intent);
    }

    public void menu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("id", "userID");
        startActivity(intent);
    }

    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        //Log.e(TAG, "User agreed to make required location settings changes.");
                       // Toast.makeText(this, "User agreed to make required location settings changes.", Toast.LENGTH_SHORT).show();
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        //Log.e(TAG, "User chose not to make required location settings changes.");
                        //Toast.makeText(this, "User chose not to make required location settings changes.", Toast.LENGTH_SHORT).show();
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null ) {
                showAddress();
            String latitude = String.valueOf(mCurrentLocation.getLatitude());
            String longitude = String.valueOf(mCurrentLocation.getLongitude());
            // Toast.makeText(this, "Lat: " + mCurrentLocation.getLatitude() + "Lon: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
            Map<String, Object> newContact = new HashMap<>();
            newContact.put("Date", mLastUpdateTime);
            newContact.put("Latitude", latitude);
            newContact.put("Longitude", longitude);
            db.collection("Customers").document("users").collection("users").document(userId).collection("location").document("location").set(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // pDialog.dismiss();

                            //Toast.makeText(MainActivity.this, "Posted Succesfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(MainActivity.this, "ERROR" + e.toString(),
                                                          Toast.LENGTH_SHORT).show();

                                              }
                                          }
                    );

        }else {
            // Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        // Log.i(TAG, "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                //       "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    // Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                // Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }


    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onStart(){
        super.onStart();
        checkPermissions();

    }

    @Override
    public void onResume(){
        super.onResume();
        //startLocationUpdates();
    }
    @Override
    public void onStop() {
        //mDemoSlider.stopAutoCycle();
        super.onStop();
        //stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Do you want to exit?");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();


                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showAddress() {

            LocationAddress.getAddressFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
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
            //editText.setText(locationAddress);
            //assert locationAddress != null;
            if (locationAddress != null) {
                myLocation = locationAddress.toLowerCase();
                String[] parts = myLocation.trim().split(",");
                String a = parts[0];
                String b = parts[3];
                String c = parts[1];
                String d = parts[2];
                Map<String, Object> newContact = new HashMap<>();
                newContact.put("State", a);
                newContact.put("Lga", b);
                newContact.put("Street name", c);
                newContact.put("Capital", d);
                db.collection("Customers").document("users").collection("users").document(userId).update(newContact)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                                  @Override
                                                  public void onFailure(@NonNull Exception e) {

                                                  }
                                              }
                        );

            }
        }
    }

}
