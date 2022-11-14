package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;

/**
 * Created by bala on 01-01-2018.
 */

public class CurrentBookingSuccessActivity extends BaseNavigationDrawerActivity {

    SearchParamsCurrent searchParams;
    TextView textViewTotal_fare;
    TextView textViewRoute;
    TextView textViewPNRNumber,mobilenum;
    TextView textViewJourneyDate;
    TextView textViewDepartureTime;
    TextView txtheader,txttotfare;
    Button btnConfirmSummary;

    String textlang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.current_booking_success);

        if (getIntent() != null && getIntent().getExtras() != null) {
            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang = searchParams.getCrntlanguage().trim();
        }
        txtheader = (TextView)findViewById(R.id.title_header);
        txttotfare = (TextView)findViewById(R.id.tot_fare);

        textViewTotal_fare = (TextView)findViewById(R.id.total_fare);
        textViewRoute = (TextView)findViewById(R.id.route1);
        textViewPNRNumber = (TextView)findViewById(R.id.pnr1);
        textViewJourneyDate = (TextView)findViewById(R.id.journey_date1);
        mobilenum = (TextView)findViewById(R.id.conductor_mobilenum);

        textViewDepartureTime = (TextView)findViewById(R.id.departure_time1);
        btnConfirmSummary = (Button)findViewById(R.id.btnConfirmSummary);

        if(textlang.equalsIgnoreCase("")){

            txtheader.setText(R.string.adcnfm_header);
            txttotfare.setText(R.string.adcnfm_total_fare);
            btnConfirmSummary.setText(R.string.adcnfm_btn);
        }

        textViewTotal_fare.setText("\u20B9 "+searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket());
        textViewRoute.setText(searchParams.getServiceRouteName());
        textViewPNRNumber.setText("PNR: "+searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse());
        if(textlang.equalsIgnoreCase("")) {
            textViewPNRNumber.setText("પી.એન.આર : " + searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse());
        }else{
            textViewPNRNumber.setText("PNR: " + searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse());
        }
        textViewJourneyDate.setText(searchParams.getJourneyDate());
        textViewDepartureTime.setText(searchParams.getDepartureTime());
        mobilenum.setText(searchParams.getConductorMobileNoGetCurrentServiceDetails());


        btnConfirmSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActivityUtil.openNewActivity(CurrentBookingSuccessActivity.this, HomeActivity.class);
                Intent i = new Intent(CurrentBookingSuccessActivity.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivity(CurrentBookingSuccessActivity.this, HomeActivity.class);
        Intent i = new Intent(CurrentBookingSuccessActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
        finish();
    }
}
