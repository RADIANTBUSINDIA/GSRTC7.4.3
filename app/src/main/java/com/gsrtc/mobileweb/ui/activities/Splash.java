package com.gsrtc.mobileweb.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.gsrtc.mobileweb.R;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private long mDelay=4*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot())
        {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                    Intent i = new Intent(Splash.this, Language_Select.class);
                    startActivity(i);
                    finish();

            }
        }, mDelay);
    }
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }
    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();

    }
}
