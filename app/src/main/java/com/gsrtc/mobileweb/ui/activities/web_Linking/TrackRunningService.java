package com.gsrtc.mobileweb.ui.activities.web_Linking;

import androidx.appcompat.app.AppCompatActivity;

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

public class TrackRunningService extends AppCompatActivity {

    private WebView myWebView;
    String textlang;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_running_service);
        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new TrackRunningService.MyBrowser());

        if(textlang.equalsIgnoreCase("english")){
            myWebView.loadUrl("https://www.gsrtc.in/GSRTCOnline/preTripSheetTakenDetails.do");
        }else {
            myWebView.loadUrl("https://www.gsrtc.in/GSRTCOnline/preTripSheetTakenDetails.do");
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

        Intent i = new Intent(TrackRunningService.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}

