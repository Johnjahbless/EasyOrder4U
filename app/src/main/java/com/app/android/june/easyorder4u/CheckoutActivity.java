package com.app.android.june.easyorder4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().hide();
    }

    public void cancel(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void editAddress(View view) {
    }


}
