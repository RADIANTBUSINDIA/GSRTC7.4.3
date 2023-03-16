package com.gsrtc.mobileweb.ui.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.web_Linking.BusPassLink;

public class Weblink extends AppCompatActivity {

    private WebView myWebView;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weblink);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new MyBrowser());
        // Load the initial URL
        myWebView.loadUrl("https://gsrtc.in/OPRSOnline/prePonePostPoneTicket.do");
    }

    private class MyBrowser extends WebViewClient {
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            Intent i = new Intent(Weblink.this, HomeActivity.class);
            Bundle data = new Bundle();
            data.putString("textlang", textlang);
            i.putExtras(data);
            startActivity(i);

        }
    }

//    @Override
//    public void onBackPressed() {
//        //ActivityUtil.openNewActivityAsOnlyActivity(Weblink.this, HomeActivity.class, null);
//        Intent i = new Intent(Weblink.this, HomeActivity.class);
//        Bundle data = new Bundle();
//        data.putString("textlang", textlang);
//        i.putExtras(data);
//        startActivity(i);
//    }
}
