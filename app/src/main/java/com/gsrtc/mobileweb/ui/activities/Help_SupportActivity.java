package com.gsrtc.mobileweb.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.track_mybus_native.MapsActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;

public class Help_SupportActivity extends BaseNavigationDrawerActivity {

    TextView Helpno, Helplink;
    TextView Helpnotxt, Helplinktxt,title;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_help__support);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        Helpno = (TextView) findViewById(R.id.helplineno);
        Helplink = (TextView) findViewById(R.id.helplineemail);
        Helpnotxt = (TextView) findViewById(R.id.helpnumtxt);
        Helplinktxt = (TextView) findViewById(R.id.txtrefund);
        title = (TextView) findViewById(R.id.title_help);

        if(textlang.equalsIgnoreCase("gujarati")){
            Helpnotxt.setText(R.string.hlp_sprt_hlpnum);
            Helplinktxt.setText(R.string.hlp_sprt_rfnd_cmplnt);
            title.setText(R.string.hlp_sprt_title);
        }


        gonext();
    }
    private void gonext() {
//        Helplink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Help_SupportActivity.this, Refundcomplaint.class);
//                Bundle data = new Bundle();
//                data.putString("textlang", textlang);
//                intent.putExtras(data);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(Help_SupportActivity.this, HomeActivity.class, null);
        Intent i = new Intent(Help_SupportActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }

}
