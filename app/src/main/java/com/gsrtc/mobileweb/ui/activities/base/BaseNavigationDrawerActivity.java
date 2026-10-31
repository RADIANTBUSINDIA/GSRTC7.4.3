package com.gsrtc.mobileweb.ui.activities.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.FeedbackActivity;
import com.gsrtc.mobileweb.ui.activities.Help_SupportActivity;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.Terms_condsActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.AdvanceBookingSearchActivity;
import com.gsrtc.mobileweb.ui.activities.booking_history.BookingHistoryActivity;

import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingHomeActivity;
import com.gsrtc.mobileweb.ui.activities.ticket_cancellation.Forgot_Txn_Pass;
import com.gsrtc.mobileweb.ui.activities.ticket_cancellation.TicketCancellationActivity;

import com.gsrtc.mobileweb.ui.activities.track_mybus_native.TrackMyBusSearchActivity;
import com.gsrtc.mobileweb.ui.activities.view_booking.ViewandSMSTicket;
import com.gsrtc.mobileweb.ui.activities.web_Linking.Booking_Policies_web;
import com.gsrtc.mobileweb.ui.activities.web_Linking.BusPassLink;
import com.gsrtc.mobileweb.ui.activities.web_Linking.Division_Depot_info;
import com.gsrtc.mobileweb.utils.AppConstants;

public class BaseNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected FrameLayout contentFrame;
    ToolbarViewHolder toolbarView;
    String textlang;
    DrawerLayout drawer;
    String getapp="https://t.ly/OA1RD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSecurity(this);
        try {
            Bundle mBun = getIntent().getExtras();

            textlang =mBun.getString("textlang",null);
            if(textlang==null){
                textlang= "english";
            }
        }catch(Exception ex){
            textlang= "english";
        }


        if(textlang.equalsIgnoreCase("gujarati")){
            setContentView(R.layout.activity_base_navigation_drawer2);
        }else {
            setContentView(R.layout.activity_base_navigation_drawer);
        }


        contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) drawer.getLayoutParams();
            // Convert 50dp to pixels
            int topMarginInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    45,
                    getResources().getDisplayMetrics()
            );
            params.topMargin = topMarginInPx;
            drawer.setLayoutParams(params);
        }



        if(textlang.equalsIgnoreCase("gujarati")){

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
            navigationView.setNavigationItemSelectedListener(this);
        }else {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        setupToolbar();
    }


    /**
     * Use this instead of setContentView(layoutResId) in child activity classes
     * @param layoutResID
     */
    protected void setContentViewDrawer(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, contentFrame);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkSecurity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSecurity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this) && item != null && item.getTitle() != null) {
            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak(item.getTitle().toString().trim());
        }

        Class targetActivity = null;
        switch (id){
            case R.id.advance_booking_btn:

                Intent i = new Intent(BaseNavigationDrawerActivity.this, AdvanceBookingSearchActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
                break;
            case R.id.nav_home:

                Intent i1 = new Intent(BaseNavigationDrawerActivity.this, HomeActivity.class);
                Bundle data1 = new Bundle();
                data1.putString("textlang", textlang);
                i1.putExtras(data1);
                startActivity(i1);

                break;

            case R.id.nav_ticket_cancellation:

                Intent i2 = new Intent(BaseNavigationDrawerActivity.this,TicketCancellationActivity.class);
                Bundle data2 = new Bundle();
                data2.putString("textlang", textlang);
                i2.putExtras(data2);
                startActivity(i2);

                break;
            case R.id.nav_current_booking:

                Intent i3 = new Intent(BaseNavigationDrawerActivity.this,CurrentBookingHomeActivity.class);
                Bundle data3 = new Bundle();
                data3.putString("textlang", textlang);
                i3.putExtras(data3);
                startActivity(i3);

                break;
            case R.id.nav_view_booking:

                Intent i4 = new Intent(BaseNavigationDrawerActivity.this,BookingHistoryActivity.class);
                Bundle data4 = new Bundle();
                data4.putString("textlang", textlang);
                i4.putExtras(data4);
                startActivity(i4);

                break;
            case R.id.nav_forgot_transpass:

                Intent i5 = new Intent(BaseNavigationDrawerActivity.this,Forgot_Txn_Pass.class);
                Bundle data5 = new Bundle();
                data5.putString("textlang", textlang);
                i5.putExtras(data5);
                startActivity(i5);
                break;
            case R.id.nav_track_my_bus:

                Intent i6 = new Intent(BaseNavigationDrawerActivity.this,TrackMyBusSearchActivity.class);
                Bundle data6 = new Bundle();
                data6.putString("textlang", textlang);
                i6.putExtras(data6);
                startActivity(i6);
                break;
            case R.id.nav_feedback:

                Intent i7 = new Intent(BaseNavigationDrawerActivity.this,FeedbackActivity.class);
                Bundle data7 = new Bundle();
                data7.putString("textlang", textlang);
                i7.putExtras(data7);
                startActivity(i7);
                break;
            case R.id.nav_help_and_support:

                Intent i8 = new Intent(BaseNavigationDrawerActivity.this,Help_SupportActivity.class);
                Bundle data8 = new Bundle();
                data8.putString("textlang", textlang);
                i8.putExtras(data8);
                startActivity(i8);
                break;
            case R.id.nav_terms_and_conditions:

                Intent i9 = new Intent(BaseNavigationDrawerActivity.this, Booking_Policies_web.class);
                Bundle data9 = new Bundle();
                data9.putString("textlang", textlang);
                i9.putExtras(data9);
                startActivity(i9);

                break;
            case R.id.nav_weblink:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, AppConstants.SHARE_TEXT_SUB);
                if(textlang.equalsIgnoreCase("gujarati")){
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, AppConstants.SHARE_TEXT_GUJARATI);
                }else {
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, AppConstants.SHARE_TEXT);
                }
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;
            case R.id.nav_printsms:

                Intent i11 = new Intent(BaseNavigationDrawerActivity.this,ViewandSMSTicket.class);
                Bundle data11 = new Bundle();
                data11.putString("textlang", textlang);
                i11.putExtras(data11);
                startActivity(i11);

                break;


            case R.id.nav_refund_complaint:

                Intent i13 = new Intent(BaseNavigationDrawerActivity.this, BusPassLink.class);
                Bundle data13 = new Bundle();
                data13.putString("textlang", textlang);
                data13.putString("pass_opt", "Complaint");
                i13.putExtras(data13);
                startActivity(i13);

                break;

            case R.id.nav_div_depot_info:

                Intent i12 = new Intent(BaseNavigationDrawerActivity.this,Division_Depot_info.class);
                Bundle data12 = new Bundle();
                data12.putString("textlang", textlang);
                i12.putExtras(data12);
                startActivity(i12);

                break;
        }

//        if(targetActivity != null)
//            ActivityUtil.openNewActivityAsOnlyActivity(this, targetActivity, null);

//        if(textlang.equalsIgnoreCase("gujarati")){
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
//        }else {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
//        }
    }

    protected void setupToolbar(){
//        if(textlang.equalsIgnoreCase("gujarati")){
//            toolbarView = new ToolbarViewHolder(this);
//            toolbarView.ham.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (drawer != null)
//                        drawer.openDrawer(Gravity.LEFT);
//                }
//            });
//        }else {


        toolbarView = new ToolbarViewHolder(this);
        toolbarView.ham.setContentDescription("Open navigation menu");
        toolbarView.ham.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(BaseNavigationDrawerActivity.this).speak("Navigation menu opened");
                if (drawer != null)
                    drawer.openDrawer(Gravity.LEFT);
            }
        });
        toolbarView.share.setContentDescription("Share application");
        toolbarView.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(BaseNavigationDrawerActivity.this).speak("Share application");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, AppConstants.SHARE_TEXT_SUB);
                if(textlang.equalsIgnoreCase("gujarati")){
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, AppConstants.SHARE_TEXT_GUJARATI);
                }else {
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, AppConstants.SHARE_TEXT);
                 }
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
//        }
    }

    protected static class ToolbarViewHolder{
        //Toolbar View
        ImageView ham,share;
        ToolbarViewHolder(Activity rootView){
            ham = (ImageView) rootView.findViewById(R.id.ham);
            share = (ImageView) rootView.findViewById(R.id.shareapp);

        }
    }


    public static void checkSecurity(final Context context) {
        StringBuilder securityWarnings = new StringBuilder();

        if (isDeveloperModeEnabled(context)) {
            Log.e("Security", "Developer Mode is enabled!");
            securityWarnings.append("⚠ Developer Mode is enabled!\n");
        }

        if (isEmulator()) {
            Log.e("Security", "Running on an Emulator!");
            securityWarnings.append("⚠ App is running on an Emulator!\n");
        }

        if (isUSBConnected(context)) {
            Log.e("Security", "USB Debugging is enabled!");
            securityWarnings.append("⚠ USB Debugging is enabled!\n");
        }

       //to run in debugmode
      securityWarnings = new StringBuilder();

        if (securityWarnings.length() > 0) {
            showSecurityDialog(context, securityWarnings.toString());
        }
    }



    private static boolean isDeveloperModeEnabled(Context context) {
        return Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1;
    }

    private static boolean isEmulator() {
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String hardware = Build.HARDWARE;
        String fingerprint = Build.FINGERPRINT;
        String product = Build.PRODUCT;
        String device = Build.DEVICE;

        return (product.contains("sdk") ||
                model.contains("Emulator") ||
                model.contains("Genymotion") ||
                manufacturer.contains("Genymotion") ||
                brand.contains("generic") ||
                hardware.contains("goldfish") ||
                hardware.contains("ranchu") ||
                fingerprint.contains("generic") ||
                device.contains("generic") ||
                product.contains("vbox86p"));
    }

    private static boolean isUSBConnected(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) == 1;
    }

    private static void showSecurityDialog(final Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Security Warning ⚠")
                .setMessage(message )
                .setCancelable(false)
                /* .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         // Continue using the app
                         dialog.dismiss();
                     }
                 })*/
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app

                        System.exit(0);
                    }
                })
                .show();
    }


}
