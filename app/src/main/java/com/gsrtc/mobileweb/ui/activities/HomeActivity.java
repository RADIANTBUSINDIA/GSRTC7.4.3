package com.gsrtc.mobileweb.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.database.Gsrtc_local_db;
import com.gsrtc.mobileweb.ui.activities.advance_booking.AdvanceBookingSearchActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.booking_history.BookingHistoryActivity;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingHomeActivity;
import com.gsrtc.mobileweb.ui.activities.ticket_cancellation.TicketCancellationActivity;
import com.gsrtc.mobileweb.ui.activities.time_table.Timetable_Search;
import com.gsrtc.mobileweb.ui.activities.track_mybus_native.Neraby_location;
import com.gsrtc.mobileweb.ui.activities.track_mybus_native.TrackMyBusSearchActivity;
import com.gsrtc.mobileweb.ui.activities.view_booking.TrackTicketHistory;
import com.gsrtc.mobileweb.ui.activities.web_Linking.BusPassLink;
import com.gsrtc.mobileweb.ui.activities.web_Linking.HireBusLink;
import com.gsrtc.mobileweb.ui.activities.web_Linking.RefundStatus;
import com.gsrtc.mobileweb.ui.adapters.ViewPagerAdapter;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.CustomVolleyRequest;
import com.gsrtc.mobileweb.utils.SliderUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseNavigationDrawerActivity {

    ViewHolder vHolder;
    RelativeLayout mLinLayHome;
    RequestQueue rq;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private String textlang;

    private String bus_pass_opt[] = {"New Commuter Bus Pass", "Renewal Commuter Bus Pass", "Application Status", "FAQs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        setContentViewDrawer(R.layout.home_new);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        Gsrtc_local_db mDb = Gsrtc_local_db.GET_DB_Instance(HomeActivity.this);

        mDb.deleteDivyangInfo();

        mLinLayHome = (RelativeLayout) findViewById(R.id.xLinLayHomescrn);

        List<SliderUtils> listofimg = new ArrayList<SliderUtils>();

        listofimg.add(new SliderUtils("https://gsrtc.in/site/GSRTC_AndroidApp/images/GSRTC_bnr01.png"));
//            listofimg.add(new SliderUtils("https://gsrtc.in/site/GSRTC_AndroidApp/images/GSRTC_bnr01.png"));
//            listofimg.add(new SliderUtils("https://gsrtc.in/site/GSRTC_AndroidApp/images/GSRTC_bnr01.png"));
//            listofimg.add(new SliderUtils("https://gsrtc.in/site/GSRTC_AndroidApp/images/GSRTC_bnr01.png"));

        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(listofimg,HomeActivity.this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        boolean isInserted = mDb.insertDivyangMaster();

        setupViews();
    }

    public void setupViews(){

//        int mTheame = ChangeTheme.GET_BG_DRAWABLE(getWindow());
//        mLinLayHome.setBackgroundResource(mTheame);

        vHolder = new ViewHolder(contentFrame);
        vHolder.mLinearLayoutAdvanceBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this, AdvanceBookingSearchActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutCancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, TicketCancellationActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutCurrentBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, CurrentBookingHomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });

        vHolder.mLinearLayoutTrackMyBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, TrackMyBusSearchActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutIteneries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, BookingHistoryActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Timetable_Search.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutResheduleTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Weblink.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutRefundStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, RefundStatus.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutLinkBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Neraby_location.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutTrackTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, TrackTicketHistory.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        vHolder.mLinearLayoutBusPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View popupView = getLayoutInflater().inflate(R.layout.buspass_popup, null);

                // Get phone list view.
                ListView phoneListView = (ListView) popupView.findViewById(R.id.popupWindowPhoneList);

                // Set header text in list view.
                TextView headerTextView = new TextView(HomeActivity.this);
                headerTextView.setText("Buss Pass");
                headerTextView.setTextColor(Color.WHITE);
                headerTextView.setTextSize(15);
                headerTextView.setBackgroundColor(Color.BLUE);
                phoneListView.addHeaderView(headerTextView);

                // Create phone data adapter.
                ArrayAdapter<String> phoneCmdAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, bus_pass_opt);

                phoneListView.setAdapter(phoneCmdAdapter);
                phoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

                 if(itemIndex !=0) {

                        Intent i = new Intent(HomeActivity.this, BusPassLink.class);
                        Bundle data = new Bundle();
                        data.putString("textlang", textlang);
                        data.putString("pass_opt", bus_pass_opt[itemIndex - 1]);
                        i.putExtras(data);
                        startActivity(i);
                    }else{

                     }
                    }
                });

                // Create popup window.
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // Set popup window animation style.
                popupWindow.setAnimationStyle(R.style.popup_window_animation_pass);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.update();
                // Show popup window offset 1,1 to phoneBtton at the screen center.
                popupWindow.showAtLocation(vHolder.mLinearLayoutBusPass, Gravity.CENTER, 0, 0);
            }
        });

        vHolder.mLinearLayoutHireBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, HireBusLink.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });

    }

    private class ViewHolder{

        private LinearLayout mLinearLayoutAdvanceBooking, mLinearLayoutCancelTicket;
        private LinearLayout mLinearLayoutTrackMyBus, mLinearLayoutCurrentBooking;
        private LinearLayout mLinearLayoutIteneries, mLinearLayoutTimeTable;
        private LinearLayout mLinearLayoutResheduleTicket, mLinearLayoutRefundStatus,mLinearLayoutTrackTicket, mLinearLayoutLinkBooking,
                mLinearLayoutBusPass, mLinearLayoutHireBus;

        private TextView ad_booking,cr_booking,can_ticket,trackbus,track_ticket,rescdule_tkt,bus_pass,near_location,refund_status,hire_bus,show_more,show_less,time_table,iteneries;

        ViewHolder(View rootView){
            mLinearLayoutAdvanceBooking = (LinearLayout)findViewById(R.id.linearLayoutAdvanceBooking);
            mLinearLayoutCancelTicket = (LinearLayout)findViewById(R.id.linearLayoutCancelTicket);
            mLinearLayoutTrackMyBus = (LinearLayout)findViewById(R.id.linearLayoutTrackMyBus);
            mLinearLayoutCurrentBooking = (LinearLayout)findViewById(R.id.linearLayoutCurrentBooking);

            mLinearLayoutIteneries = (LinearLayout)findViewById(R.id.linearLayoutItineries);
            mLinearLayoutTimeTable = (LinearLayout)findViewById(R.id.linearLayoutTimeTable);
            mLinearLayoutResheduleTicket = (LinearLayout)findViewById(R.id.linearLayoutResheduleTicket);
            mLinearLayoutRefundStatus = (LinearLayout)findViewById(R.id.linearLayoutRefundStatus);

            mLinearLayoutTrackTicket = (LinearLayout)findViewById(R.id.linearLayoutTrackTicket);
            mLinearLayoutLinkBooking = (LinearLayout)findViewById(R.id.linearLayoutLinkBooking);
            mLinearLayoutBusPass = (LinearLayout)findViewById(R.id.linearLayoutBusPass);
            mLinearLayoutHireBus = (LinearLayout)findViewById(R.id.linearLayoutHireBus);

            ad_booking = (TextView)findViewById(R.id.textViewBusIcon);
            cr_booking = (TextView)findViewById(R.id.textViewCurrentBooking);
            trackbus = (TextView)findViewById(R.id.textViewTrackMyBus);
            can_ticket = (TextView)findViewById(R.id.textViewCancelTicket);

            rescdule_tkt = (TextView)findViewById(R.id.textViewResheduleTicket);
            bus_pass = (TextView)findViewById(R.id.textViewBusPass);
            near_location = (TextView)findViewById(R.id.textViewLinkBooking);
            refund_status = (TextView)findViewById(R.id.textViewRefundStatus);
            hire_bus = (TextView)findViewById(R.id.textViewHireBus);
//            show_more = (TextView)findViewById(R.id.textshow);
            time_table = (TextView)findViewById(R.id.textViewTimeTable);
            iteneries = (TextView)findViewById(R.id.textViewItineries);
            track_ticket = (TextView)findViewById(R.id.textViewTrack_ticket);

            if(textlang.equalsIgnoreCase("gujarati"))
            {
                ad_booking.setText(R.string.hm_advance_booking);
                cr_booking.setText(R.string.hm_current_ticket);
                can_ticket.setText(R.string.hm_cancel_ticket);
                trackbus.setText(R.string.hm_track_my_bus);
                rescdule_tkt.setText(R.string.gReschedule_Ticket);
                bus_pass.setText(R.string.gBus_Pass);
                near_location.setText(R.string.gNear_By_Location);
                refund_status.setText(R.string.gRefund_status);
                hire_bus.setText(R.string.gHire_A_Bus);
               // show_more.setText(R.string.gShow_More);
                time_table.setText(R.string.gTime_Table);
                iteneries.setText(R.string.gIteneries);
                track_ticket.setText(R.string.gtrack_ticket);
            }
        }
    }

    public void onBackPressed() {
        ActivityUtil.openNewActivityAsOnlyActivity(HomeActivity.this, Language_Select.class, null);
    }
}
