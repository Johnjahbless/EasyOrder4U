package com.app.android.june.easyorder4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
    }

    public void cancel(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updates(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.app.android.june.easyorder4u"));
        startActivity(intent);
    }

    public void share(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.app.android.june.easyorder4u");
        startActivity(shareIntent);
    }

    public void like(View view) {
        String facebookPageID = "easyorder4u/";
        // URL
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
    }

    public void rate(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.app.android.june.easyorder4u"));
        startActivity(intent);
    }

    public void privacy(View view) {
        String url = "https://easyorder4u.com/privacypolicy.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void terms(View view) {
        String url = "https://easyorder4u.com/termsofservice.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void contact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void messages(View view) {
       // Intent intent = new Intent(this, MessagesActivity.class);
        // startActivity(intent);
    }

    public void help(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void notify(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void wallet(View view) {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    public void delivery(View view) {
        Intent intent = new Intent(this, HFActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void orders(View view) {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }

    public void location(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}
