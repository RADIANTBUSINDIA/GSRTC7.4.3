package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;

public class Track_bus extends BaseNavigationDrawerActivity {

    ImageView track_one,track_two;
    TextView title;
    private String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.track_bus);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        track_one =(ImageView) findViewById(R.id.track_one);
        track_two =(ImageView) findViewById(R.id.track_two);
        title =(TextView)findViewById(R.id.track_title);

        if(textlang.equalsIgnoreCase("gujarati")){
//            track_one.setText(R.string.track_trackbus);
//            track_two.setText(R.string.track_vehicle_route);
            title.setText(R.string.track_title);
        }

        track_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Track_bus.this, TrackMyBusSearchActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                intent.putExtras(data);
                startActivity(intent);

            }
        });

        track_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Track_bus.this, TrackonRoutes.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // ActivityUtil.openNewActivityAsOnlyActivity(Track_bus.this, HomeActivity.class, null);
        Intent i = new Intent(Track_bus.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
