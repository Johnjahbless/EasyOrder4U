package com.app.android.june.easyorder4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        getSupportActionBar().hide();
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
    public void cancel(View view) {
        onBackPressed();
    }

    public void letStart(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }
}
