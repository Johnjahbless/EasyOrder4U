package com.app.android.june.easyorder4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class AboutActivity extends AppCompatActivity {
    WebView webview;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout layout;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();
        webview = (WebView) findViewById(R.id.wv_test);

        layout = (RelativeLayout)findViewById(R.id.layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                (android.R.color.black));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview.loadUrl("https://happifoodi.com/about");

            }
        });

        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //webSettings.setUseWideViewPort(true);
        // webSettings.setSaveFormData(true);
        //webSettings.setSavePassword(true);
        webSettings.setEnableSmoothTransition(true);
        webview.getSettings().setJavaScriptEnabled(true);


        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://www.happifoodi.com/about");
    }

    public class WebViewClient extends
            android.webkit.WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            swipeRefreshLayout.setRefreshing(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("my-apps-aecb3.firebaseapp.com/")) {
                view.loadUrl(url);

            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);

            }
            return true;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            layout.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void cancel(View view) {
        onBackPressed();
    }
}
