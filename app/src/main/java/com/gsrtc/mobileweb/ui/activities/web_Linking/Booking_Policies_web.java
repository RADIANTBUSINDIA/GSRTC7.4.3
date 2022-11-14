package com.gsrtc.mobileweb.ui.activities.web_Linking;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;

public class Booking_Policies_web extends BaseNavigationDrawerActivity {

    private WebView myWebView;
    String textlang;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_division__depot_info);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new MyBrowser());

        if(textlang.equalsIgnoreCase("english")){
            myWebView.loadUrl("https://gsrtc.in/site/downloads/innerPages/termsConditions.html");
        }else {
            myWebView.loadUrl("https://gsrtc.in/site/gujarati/downloads/innerPages/termsConditions.html");
        }

    }

    private static class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(Booking_Policies_web.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}

