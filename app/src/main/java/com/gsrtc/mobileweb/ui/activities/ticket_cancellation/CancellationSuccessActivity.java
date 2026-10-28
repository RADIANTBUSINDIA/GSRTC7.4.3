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
        if (mBun != null) {
            textlang = mBun.getString("textlang");
            booking = (BookingCancellation) mBun.getSerializable(com.gsrtc.mobileweb.utils.AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION);
        }

        textmsg =(TextView)findViewById(R.id.cancelmsg);
        title =(TextView)findViewById(R.id.header_text);
        mainmenu =(Button) findViewById(R.id.submitBtn);

        if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this)) {
            if (booking != null && booking.isDivyangWithHelper) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Cancellation confirmed successfully for Divyang and Helper tickets. Your refund will be processed as per GSRTC policy.");
            } else {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Cancellation confirmed successfully. Your refund will be processed as per GSRTC policy.");
            }
        }

        if(textlang != null && textlang.equalsIgnoreCase("gujarati")){
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
