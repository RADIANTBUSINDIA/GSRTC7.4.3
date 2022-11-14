package com.gsrtc.mobileweb.ui.activities.view_booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.booking_history.GetTrackTicketDetails;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsData;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsParam;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.BookingHistoryNetworkUtils;

public class TrackTicketHistory extends BaseNavigationDrawerActivity {
    PrintSmsData ticketsms;
    ViewHolder holder;
    private String pnrno,mobile;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_track_ticket_history);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        setup();
    }

    private void setup() {

        ticketsms = new PrintSmsData();
        ticketsms.printSmsParam = new PrintSmsParam();
        holder = new ViewHolder(this);
        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitBtn();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.pnrno.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.mobile.getWindowToken(), 0);
            }
        });
    }

    private void onSubmitBtn() {

        ticketsms.printSmsParam.pnrNo = holder.pnrno.getText().toString();
        ticketsms.printSmsParam.mobileNo = holder.mobile.getText().toString();

        pnrno=ticketsms.printSmsParam.pnrNo;
        mobile=ticketsms.printSmsParam.mobileNo;

        if(pnrno.equalsIgnoreCase("") && mobile.equalsIgnoreCase("")){
            ActivityUtil.showToast(this, "Please Enter valid details");
            return;
        }
        if(holder.pnrno.getText().toString().isEmpty()) {
            ActivityUtil.showToast(this, "Please enter valid PNR No");
            return;
        }
        if(holder.pnrno.getText().toString().isEmpty() && holder.mobile.getText().toString().isEmpty()){
            ActivityUtil.showToast(this, " Please Enter valid details ");
            return;
        }

        if(!holder.mobile.getText().toString().isEmpty() && !ValidationUtil.isValidPhone(holder.mobile.getText())) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }


            if (!ValidationUtil.isValidPhone(holder.mobile.getText().toString())) {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
            else if (holder.mobile.getText().length()<10) {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
            else if(holder.mobile.getText().toString().trim().length()==13)
            {
                String mobnum=holder.mobile.getText().toString();

                if (mobnum.startsWith("+"))
                {
                    if (mobnum.length() == 13)
                    {
                        String str_getMOBILE = mobnum.substring(3);
                        holder.mobile.setText(str_getMOBILE);
                    }
                }
                else
                {
                    ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                    return;
                }
            }
            else if(holder.mobile.getText().toString().trim().length()>10)
            {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
            else if(holder.mobile.getText().toString().trim().length()==10) {

                String mobnum = holder.mobile.getText().toString();
               // goMobile();

                if (mobnum.startsWith("+")) {
                    ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                    return;
                }
            }
               goMobile();

        }

    public void goMobile() {
        if (ticketsms.getTrackTicketDetails == null) {
            getTicketMobile();
            return;
        }
        else {
            CustomisedProgressDialog.STOP_CUST_DIA();
        }
        Bundle bundle = new Bundle();
        bundle.putString("textlang", textlang);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.PRINT_SMS_TICKET, ticketsms);
        ActivityUtil.openNewActivity(this, TrackTicket_details.class, bundle);
    }


    /**calling print ticket PNR **/

    private void getTicketMobile() {

        CustomisedProgressDialog.SHOW_CUST_DIA(TrackTicketHistory.this, "Please wait loading...");

        BookingHistoryNetworkUtils.GetTrackTicketHistory(ticketsms, new BookingHistoryNetworkUtils.GetTrackTicketHistoryCallback() {
            @Override
            public void onSuccess(GetTrackTicketDetails[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(TrackTicketHistory.this, "No Transaction Found!");
                    }
                    ticketsms.getTrackTicketDetails = response;
                    goMobile();
                } else {
                    ActivityUtil.showToast(TrackTicketHistory.this, "No Transaction Found!");
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(TrackTicketHistory.this, e);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(ticketsms!=null){
            ticketsms.getTrackTicketDetails = null;
        }
    }


    public class ViewHolder {
        EditText pnrno, mobile;
        TextView title,pnrnotxt,mobiletxt,or;
        Button submitBtn;

        ViewHolder(Activity activity) {
            submitBtn = (Button) activity.findViewById(R.id.submitBtn);
            pnrno = (EditText) activity.findViewById(R.id.pnrNo);
            mobile = (EditText) activity.findViewById(R.id.mobile);

            title = (TextView) activity.findViewById(R.id.title_track_details);
            pnrnotxt = (TextView) activity.findViewById(R.id.pnrtxt);
            mobiletxt = (TextView) activity.findViewById(R.id.mobiletxt);

            if(textlang.equalsIgnoreCase("gujarati")){

                pnrno.setHint(R.string.genter_pnr);
                mobile.setHint(R.string.vwsms_enter_mobile);
                title.setText(R.string.gTrack_Ticket_details);
                pnrnotxt.setText(R.string.gpnr_no);
                mobiletxt.setText(R.string.vwsms_mobile);
                submitBtn.setText(R.string.vwsms_submit);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TrackTicketHistory.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}