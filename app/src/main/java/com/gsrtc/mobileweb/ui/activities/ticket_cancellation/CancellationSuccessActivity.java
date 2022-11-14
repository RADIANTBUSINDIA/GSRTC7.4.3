package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;

public class CancellationSuccessActivity extends BaseNavigationDrawerActivity {

    BookingCancellation booking;
    private String textlang;
    TextView textmsg,title;
    Button mainmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_cancellation_success);
        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        textmsg =(TextView)findViewById(R.id.cancelmsg);
        title =(TextView)findViewById(R.id.header_text);
        mainmenu =(Button) findViewById(R.id.submitBtn);

        if(textlang.equalsIgnoreCase("gujarati")){
            textmsg.setText(R.string.cnfmfnl_msg);
            title.setText(R.string.cnfmfnl_title);
            mainmenu.setText(R.string.cnfmfnl_mainmenu);
        }

        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ActivityUtil.openNewActivityAsOnlyActivity(CancellationSuccessActivity.this, HomeActivity.class, null);
                Intent i = new Intent(CancellationSuccessActivity.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(CancellationSuccessActivity.this, HomeActivity.class, null);
        Intent i = new Intent(CancellationSuccessActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
