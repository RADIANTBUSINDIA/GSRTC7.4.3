package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.AggreepayGateway;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.AggreepayGatewayCurrent;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.CCAvenueCurrentBooking;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.NSDL_PaymentCurrentBooking;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.PaytmPaymentGatewayCurrent;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

/**
 * Created by rad on 31-12-2017.
 */

public class CurrentReferenceNumberActivity extends BaseNavigationDrawerActivity{


    SearchParamsCurrent searchParams;
    ActivityViewHolder vHolder;
    boolean dataFetched = false;
    ProgressDialog seatBlockingProgressDialog;
    private String totalFare, paymentMode;
    public static final int PAYMENT_CODE = 26;
    private String isCard;
    String textlang;
    String mail,name,Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_reference_number);

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            totalFare = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
            isCard = searchParams.getGatewayLookupId();
            Log.e("Paymode", isCard);
            mail= searchParams.getPassengerEmail()  ;
            name=searchParams.getPassengerName() ;
            Number=searchParams.getPassengerMobile()   ;
            textlang = searchParams.getCrntlanguage().trim();

        }




        setupViews();


    }

    public void setupViews() {
        vHolder = new ActivityViewHolder(this);

        vHolder.tvRefNumber.setText(searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse());
        vHolder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBookingConfirmed();

            }
        });
    }

    public void pay() {
        //showProgressDialog();
        CustomisedProgressDialog.SHOW_CUST_DIA(CurrentReferenceNumberActivity.this, "Please wait Confirming Ticket...");
        onBookingConfirmed();
    }

    public void onBookingConfirmed() {
        if (totalFare == null)
            return;
        if (isCard.equals("733")) {
            Intent intent = new Intent(CurrentReferenceNumberActivity.this, PaytmPaymentGatewayCurrent.class);
            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            intent.putExtras(extra);
            CustomisedProgressDialog.STOP_CUST_DIA();
            startActivityForResult(intent, PAYMENT_CODE);

        } else if(isCard.equals("724")) {

            Intent intent = new Intent(CurrentReferenceNumberActivity.this, NSDL_PaymentCurrentBooking.class);
            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            intent.putExtras(extra);
            CustomisedProgressDialog.STOP_CUST_DIA();
            startActivityForResult(intent, PAYMENT_CODE);
        } else if (isCard.equals("721")) {
            Intent intent = new Intent(CurrentReferenceNumberActivity.this, AggreepayGatewayCurrent.class);
            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putString("mail", mail);
            extra.putString("name", name);
            extra.putString("number", Number);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            intent.putExtras(extra);
            CustomisedProgressDialog.STOP_CUST_DIA();
            startActivityForResult(intent, PAYMENT_CODE);
        } else if (isCard.equals("758") || isCard.equals("744")) {
            Intent intent = new Intent(CurrentReferenceNumberActivity.this, com.gsrtc.mobileweb.ui.activities.payment_gateway.SapphirePaymentGatewayCurrent.class);
            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putString("mail", mail);
            extra.putString("name", name);
            extra.putString("number", Number);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            intent.putExtras(extra);
            CustomisedProgressDialog.STOP_CUST_DIA();
            startActivityForResult(intent, PAYMENT_CODE);
        } else {
//            Intent intent = new Intent(CurrentReferenceNumberActivity.this, CCAvenueCurrentBooking.class);
//            Bundle extra = new Bundle();
//            extra.putString("textlang", textlang);
//            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
//            intent.putExtras(extra);
//            CustomisedProgressDialog.STOP_CUST_DIA();
//            startActivityForResult(intent, PAYMENT_CODE);
        }
    }

    public void showProgressDialog() {
        if (seatBlockingProgressDialog != null && seatBlockingProgressDialog.isShowing()) {
            return;
        }
        seatBlockingProgressDialog = new ProgressDialog(this);
        seatBlockingProgressDialog.setCancelable(false);
        seatBlockingProgressDialog.setTitle("Confirming Ticket...");
        seatBlockingProgressDialog.setMessage("Please wait...");
        seatBlockingProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (seatBlockingProgressDialog == null || !seatBlockingProgressDialog.isShowing()) {
            return;
        }
        seatBlockingProgressDialog.dismiss();
    }

    public class ActivityViewHolder{
        View rootView, loadingLayout;
        Button btnContinue;
        TextView tvRefNumber,header,text_up,text_down;

        ActivityViewHolder(Activity activity){
            rootView = activity.findViewById(R.id.rootView);
            tvRefNumber = (TextView) activity.findViewById(R.id.onwardJourney);
            header = (TextView) activity.findViewById(R.id.header_text);
            text_up = (TextView) activity.findViewById(R.id.text_up);
            text_down = (TextView) activity.findViewById(R.id.text_bottom);

            btnContinue = (Button) activity.findViewById(R.id.submitBtn);

            if(textlang.equalsIgnoreCase("gujarati")){
                header.setText(R.string.crtob_make_pay);
                text_up.setText(R.string.crtob_text_up);
                text_down.setText(R.string.crtob_text_down);
                btnContinue.setText(R.string.crtob_btn_cnfrm);
            }
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                CurrentReferenceNumberActivity.this,R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ActivityUtil.openNewActivityAsOnlyActivity(CurrentReferenceNumberActivity.this, HomeActivity.class, null);
                Intent i = new Intent(CurrentReferenceNumberActivity.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });

        alertDialog.setNegativeButton("NO", null);

        alertDialog.setMessage("Going back will discard your booking. Do you want to continue?");
        alertDialog.setTitle("GSRTC");
        alertDialog.show();
    }
}
