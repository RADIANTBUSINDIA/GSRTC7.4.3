package com.gsrtc.mobileweb.ui.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;

public class Terms_condsActivity extends BaseNavigationDrawerActivity {

    private WebView myWebView;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_terms_conds);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        WebView webView = (WebView) findViewById(R.id.webviewterm);
        String htmlData="<font color='black'>" + getString(R.string.booking_policy) + "</font>";
        webView .loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "");
        webView .setBackgroundColor(0);
        WebSettings webSettings = webView .getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultFontSize((int) (getResources().getDimension(R.dimen.text_size_for_content)/getResources().getDisplayMetrics().density));
    }
    @Override
    public void onBackPressed() {
//        ActivityUtil.openNewActivityAsOnlyActivity(Terms_condsActivity.this, HomeActivity.class, null);
        Intent i = new Intent(Terms_condsActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }

}
