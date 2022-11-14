package com.gsrtc.mobileweb.ui.activities.advance_booking;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;

import java.text.DecimalFormat;

public class BillDeskFailtrans extends BaseNavigationDrawerActivity implements View.OnClickListener {
    private Button btn1;
    private TextView textView1;
    private LinearLayout mLinLayHome;
    private RelativeLayout mRellayTop;
    private TextView mtvCancelledMessage;

    private DecimalFormat mDecimalFormat;

    private Bundle mBun;
    private Intent mIntent;
    private String mcancel,textlang;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_bill_desk_failtrans);

        mIntent = getIntent();
        mBun = mIntent.getExtras();

        mcancel = mBun.getString("errorDescription");
        textlang = mBun.getString("textlang");

        textView1 = (TextView) findViewById(R.id.text_view_one);
        textView1.setText(mcancel +"!");
        btn1 = (Button) findViewById(R.id.butto);
        btn1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(BillDeskFailtrans.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BillDeskFailtrans.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
        finish();
    }

}