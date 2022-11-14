package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Fault;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.CancellationSearchParams;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetFullCancellationDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.net.TicketCancelNetworkUtil;

public class TicketCancellationActivity extends BaseNavigationDrawerActivity {

    ViewHolder holder;
    BookingCancellation booking;
    ProgressDialog progressDialog;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_ticket_cancellation);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        setup();
    }

    public void setup(){
        holder = new ViewHolder(this);
        booking = new BookingCancellation();
        booking.cancellationSearchParams = new CancellationSearchParams();
        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitBtn();
            }
        });
    }

    public void onSubmitBtn(){

        if(TextUtils.isEmpty(holder.pnr.getText().toString())){
            ActivityUtil.showToast(this, "PNR can not be empty");
            return;
        }
        else if(!ValidationUtil.isValidEmail(holder.email.getText().toString())){
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }
        else if(!ValidationUtil.isValidPhone(holder.phone.getText().toString())){
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        else if (holder.phone.getText().length()<10) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        else if(holder.phone.getText().toString().trim().length()==13)
        {
            String mobnum=holder.phone.getText().toString();

            if (mobnum.startsWith("+"))
            {
                if (mobnum.length() == 13)
                {
                    String str_getMOBILE = mobnum.substring(3);
                    holder.phone.setText(str_getMOBILE);
                }
            }
            else
            {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }
        else if(holder.phone.getText().toString().trim().length()>10)
        {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        else if(holder.phone.getText().toString().trim().length()==10) {

            String mobnum = holder.phone.getText().toString();

            if (mobnum.startsWith("+")) {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }
        else if(TextUtils.isEmpty(holder.text_password.getText().toString())){
            ActivityUtil.showToast(this, "Text password can not be empty");
            return;
        }
//        else if(TextUtils.isEmpty(holder.wsRefNo.getText().toString())){
//            ActivityUtil.showToast(this, "Reference number can not be empty");
//            return;
//        }
        booking.cancellationSearchParams.email = holder.email.getText().toString();
        booking.cancellationSearchParams.pnr = holder.pnr.getText().toString();
        booking.cancellationSearchParams.mobile = holder.phone.getText().toString();
        booking.cancellationSearchParams.txtPassword = holder.text_password.getText().toString();
        booking.cancellationSearchParams.WSRefNo = holder.wsRefNo.getText().toString();
        goNext();
    }

    public void goNext(){
        if(booking.getAllPassengerDetailsByPnrNoResponse == null){
            getAllPassengers();
            return;
        }
        else if(booking.getFullCancellationDetailsResponse == null){
            getCancellationDetails();
            return;
        }
        else{
            hideProgressDialog();
        }
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION, booking);
        ActivityUtil.openNewActivity(this, CancellationPassengerSelectionActivity.class, bundle);
    }

    public void getAllPassengers(){
        showProgressDialog();
        TicketCancelNetworkUtil.GetAllPassengerDetailsByPnrNo(booking, new TicketCancelNetworkUtil.GetAllPassengerDetailsByPnrNoCallback() {
            @Override
            public void onSuccess(GetAllPassengerDetailsByPnrNoResponse[] response) {
                if(response!=null){
                    booking.getAllPassengerDetailsByPnrNoResponse = response;
                    goNext();
                }
                else{
                    hideProgressDialog();
                }
            }

            @Override
            public void onSuccess(Fault[] response) {
                if(response!=null){
                    hideProgressDialog();
                    //ActivityUtil.showErrorToast(TicketCancellationActivity.this,e, "Invalid Parameters");
                    ActivityUtil.showToast(TicketCancellationActivity.this, response[0].getFaultstring());
                }
                else{
                    hideProgressDialog();
                }
            }

            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                ActivityUtil.showErrorToast(TicketCancellationActivity.this,e, "Invalid Parameters");
            }
        });
    }

    public void getCancellationDetails(){
        showProgressDialog();
        TicketCancelNetworkUtil.GetFullCancellationDetails(booking, new TicketCancelNetworkUtil.GetFullCancellationDetailsCallback() {
            @Override
            public void onSuccess(GetFullCancellationDetailsResponse response) {
                if(response!=null){
                    booking.getPartialCancellationDetailsResponse = response.toPartialCancellationDetails();
                    booking.getFullCancellationDetailsResponse = response;
                    goNext();
                }else{
                    hideProgressDialog();
                }
            }
            @Override
            public void onSuccess(Fault[] response) {
                if(response!=null){
                    hideProgressDialog();
                    //ActivityUtil.showErrorToast(TicketCancellationActivity.this,e, "Invalid Parameters");
                    ActivityUtil.showToast(TicketCancellationActivity.this, response[0].getFaultstring());
                }
                else{
                    hideProgressDialog();
                }
            }
            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                ActivityUtil.showErrorToast(TicketCancellationActivity.this,e,"Invalid Parameters");
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        booking.getPartialCancellationDetailsResponse = null;
        booking.getFullCancellationDetailsResponse = null;
        booking.getAllPassengerDetailsByPnrNoResponse = null;
    }

    public void showProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing() ){
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }
    public void hideProgressDialog(){
        if(progressDialog == null || !progressDialog.isShowing() ){
            return;
        }
        progressDialog.dismiss();
    }

    public class ViewHolder{
        EditText pnr, email, phone, text_password, wsRefNo;
        TextView head,pnrtxt,emailtxt,mobiletxt,passtxt;
        Button submitBtn;
        ViewHolder(Activity activity){
            pnr = (EditText) activity.findViewById(R.id.pnr);
            email = (EditText) activity.findViewById(R.id.email);
            phone = (EditText) activity.findViewById(R.id.phone);
            text_password = (EditText) activity.findViewById(R.id.text_password);
            wsRefNo = (EditText) activity.findViewById(R.id.wsRefNo);
            submitBtn = (Button) activity.findViewById(R.id.submitBtn);

            head = (TextView) activity.findViewById(R.id.header_cancel);
            pnrtxt = (TextView) activity.findViewById(R.id.txtpnr);
            emailtxt = (TextView) activity.findViewById(R.id.txtemail);
            mobiletxt = (TextView) activity.findViewById(R.id.txtmobile);
            passtxt = (TextView) activity.findViewById(R.id.txtpass);
            submitBtn = (Button) activity.findViewById(R.id.submitBtn);

            if(textlang.equalsIgnoreCase("gujarati")){

                pnr.setHint(R.string.can_pnrno);
                email.setHint(R.string.can_email);
                phone.setHint(R.string.can_mobile);
                text_password.setHint(R.string.can_password);
                // wsRefNo.setHint(R.string.can_pnrno);
                head.setText(R.string.can_can_tkt);
                pnrtxt.setText(R.string.can_pnrno);
                emailtxt.setText(R.string.can_email);
                mobiletxt.setText(R.string.can_mobile);
                passtxt.setText(R.string.can_password);
                submitBtn.setText(R.string.can_submit);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(TicketCancellationActivity.this, HomeActivity.class, null);
        Intent i = new Intent(TicketCancellationActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
