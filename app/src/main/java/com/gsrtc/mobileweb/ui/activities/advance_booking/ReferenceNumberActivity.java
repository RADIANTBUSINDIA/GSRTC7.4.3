package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.GetTotalFareDetailsOfTicketResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.AggreepayGateway;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.NSDL_PaymentGateway;
import com.gsrtc.mobileweb.ui.activities.payment_gateway.PaytmPaymentGateway;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

public class ReferenceNumberActivity extends BaseNavigationDrawerActivity {

    ProgressDialog seatBlockingProgressDialog;
    Booking onwardBooking, returnBooking, divyangBooking;
    public static final int PAYMENT_CODE = 26;
    private String totalFare, paymentMode;
    private String isCard;
    String textlang;

    TextView title, text_up, text_down;
    Button nextbtn,nextbtnDiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_reference_number);


        title = (TextView) findViewById(R.id.header_text);
        text_up = (TextView) findViewById(R.id.text_up);
        text_down = (TextView) findViewById(R.id.text_bottom);
        nextbtn = (Button) findViewById(R.id.submitBtn);

        nextbtnDiv = (Button) findViewById(R.id.submitBtnDivyang);

        Bundle extras = getIntent().getExtras();
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);
        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
        }
        totalFare = extras.getString("totalFare", null);
        paymentMode = extras.getString("paymentMode", null);
        isCard = extras.getString("iscard", null);

        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            insertDivyangDetails(onwardBooking);

            // enable direct InsertButton
            nextbtnDiv.setVisibility(View.VISIBLE);
            nextbtn.setVisibility(View.GONE);
        } else if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            insertDivyangDetails(onwardBooking);
            insertDivyangHelperDetails(divyangBooking);
            // enable direct InsertButton

            if(totalFare.equalsIgnoreCase("0.0")){

                nextbtnDiv.setVisibility(View.VISIBLE);
                nextbtn.setVisibility(View.GONE);
            }else{
                //PaymentGateway Button
                nextbtnDiv.setVisibility(View.GONE);
                nextbtn.setVisibility(View.VISIBLE);
            }
        }

        textlang = onwardBooking.getSearchParams().getTextlanguage().trim();

        if (textlang.equalsIgnoreCase("gujarati")) {
            title.setText(R.string.ob_make_pay);
            text_up.setText(R.string.ob_text_up);
            text_down.setText(R.string.ob_text_down);
            nextbtn.setText(R.string.ob_btn_cnfrm);
            nextbtnDiv.setText(R.string.ob_btn_cnfrm);
        }

        if (onwardBooking.getSaveBookingDetailsTemporary() != null) {
            ((TextView) findViewById(R.id.onwardJourney)).setText(onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo());
        }

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
            if (returnBooking.getSaveReturnBookingDetailsTemporary() != null) {

                ((TextView) findViewById(R.id.onwardJourney)).setText(returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo());

            }
        }
        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            if (divyangBooking.getSaveReturnBookingDetailsTemporary() != null) {

                ((TextView) findViewById(R.id.onwardJourney)).setText(divyangBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo());

            }
        }

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pay();
            }
        });

        nextbtnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalFare.equalsIgnoreCase("0.0")&&(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")/*||
                        onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")*/ )){

                   // Toast.makeText(ReferenceNumberActivity.this, "April fool", Toast.LENGTH_SHORT).show();
                    confirmTicket();
                }

            }
        });
    }

    public void onBookingConfirmed() {
        if (totalFare == null)
            return;
        if (isCard.equalsIgnoreCase("724")) {
            Intent intent = new Intent(ReferenceNumberActivity.this, NSDL_PaymentGateway.class);
            intent.putExtra("textlang", textlang);
            intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
            if (returnBooking != null) {
                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
            }
            intent.putExtra("totalFare", totalFare);
            intent.putExtra("isCard", isCard);
            startActivityForResult(intent, PAYMENT_CODE);
        } else if (isCard.equalsIgnoreCase("733")) {
            Intent intent = new Intent(ReferenceNumberActivity.this, PaytmPaymentGateway.class);
            intent.putExtra("textlang", textlang);
            intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
            if (returnBooking != null) {
                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
            }
            if (divyangBooking != null) {
                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, divyangBooking);
            }
            intent.putExtra("totalFare", totalFare);
            intent.putExtra("isCard", isCard);
            startActivityForResult(intent, PAYMENT_CODE);
        } else if (isCard.equalsIgnoreCase("721")) {
            Intent intent = new Intent(ReferenceNumberActivity.this, AggreepayGateway.class);
            intent.putExtra("textlang", textlang);
            intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
            if (returnBooking != null) {
                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
            }
            intent.putExtra("totalFare", totalFare);
            intent.putExtra("isCard", isCard);
            startActivityForResult(intent, PAYMENT_CODE);

        }else if (isCard.equalsIgnoreCase("744")) {   //AXIS

            Toast.makeText(this, "Axis Bank Check", Toast.LENGTH_SHORT).show();
         /*   Intent intent = new Intent(ReferenceNumberActivity.this, AggreepayGateway.class);
            intent.putExtra("textlang", textlang);
            intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
            if (returnBooking != null) {
                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
            }
            intent.putExtra("totalFare", totalFare);
            intent.putExtra("isCard", isCard);
            startActivityForResult(intent, PAYMENT_CODE);*/
        } else {

//            Intent intent = new Intent(ReferenceNumberActivity.this, BillDeskGateway.class);
//            intent.putExtra("textlang", textlang);
//            intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
//            if (returnBooking != null) {
//                intent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
//            }
//            intent.putExtra("totalFare", totalFare);
//            intent.putExtra("isCard", isCard);
//            startActivityForResult(intent, PAYMENT_CODE);
        }
    }

    /***RETRIVE THE VALUE FROM HDFC & BILLDESK PAYMENT ACTIVITY  ***/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_CODE) {
            if (resultCode == RESULT_OK) {

                Booking onwardBooking1 = (Booking) data.getExtras().getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

                if (onwardBooking1 == null) {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    return;
                }

                if (data.getExtras().containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
                    Booking returnBooking1 = (Booking) data.getExtras().getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
                    if (onwardBooking1.getConfirmRtnSeatBookingAndroidResponse() != null && returnBooking1.getConfirmRtnSeatBookingAndroidResponse() != null) {
                        onwardBooking = onwardBooking1;
                        returnBooking = returnBooking1;

                    } else {
                        Log.d("No Luck", "No Luck");
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        return;
                    }
                } else if (data.getExtras().containsKey(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG)) {
                    Booking divvyangbooking1 = (Booking) data.getExtras().getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
                    if (onwardBooking1.getConfirmRtnSeatBookingAndroidResponse() != null && divvyangbooking1.getConfirmRtnSeatBookingAndroidResponse() != null) {
                        onwardBooking = onwardBooking1;
                        divyangBooking = divvyangbooking1;

                    } else {
                        Log.d("No Luck", "No Luck");
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        return;
                    }
                } else {
                    if (onwardBooking1.getConfirmAdvSeatBookingAndroidResponse() != null) {
                        onwardBooking = onwardBooking1;

                    } else {
                        Log.d("No Luck", "No Luck");
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        return;
                    }
                }

                CustomisedProgressDialog.STOP_CUST_DIA();
                Bundle extras = new Bundle();
                extras.putString("textlang", textlang);
                extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                if (returnBooking != null) {
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
                }
                if (divyangBooking != null) {
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, divyangBooking);
                }
                ActivityUtil.openNewActivity(ReferenceNumberActivity.this, BookingSuccessActivity.class, extras);
            } else if (resultCode == RESULT_CANCELED) {
                CustomisedProgressDialog.STOP_CUST_DIA();
            }
        }
    }

    public void pay() {
        CustomisedProgressDialog.SHOW_CUST_DIA(ReferenceNumberActivity.this, "Please wait Confirming Ticket...");

        onBookingConfirmed();
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

    /***INSERTING DIVYANG INFORMATION  ****/

    public void insertDivyangDetails(final Booking booking) {

        AdvanceBookingNetworkUtil.InsertDivyangInformations(booking, new AdvanceBookingNetworkUtil.InsertDivyangInformationsCallback() {
            @Override
            public void onSuccess(GetTotalFareDetailsOfTicketResponse response) {

            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(ReferenceNumberActivity.this, e);
            }
        });
    }

    /***INSERTING DIVYANG WITH HELPER INFORMATION  ****/

    public void insertDivyangHelperDetails(final Booking divyangbk) {

        AdvanceBookingNetworkUtil.InsertDivyangHelperInformations(divyangbk, new AdvanceBookingNetworkUtil.InsertDivyangHelperInformationsCallback() {
            @Override
            public void onSuccess(GetTotalFareDetailsOfTicketResponse response) {

            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(ReferenceNumberActivity.this, e);
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ReferenceNumberActivity.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(ReferenceNumberActivity.this, HomeActivity.class);
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


    public void confirmTicket() {

        String txntransactionStatus = "STATUS";
        String txnerrorDescription = "RESPMSG";
        Log.d("TRANS STATUS", txntransactionStatus);


        String txnorderId = "ORDERID";
        String txnmerchantID = "MID";
        String txntxnId = "TXN101420222000";
        String txngatewayAmount = "TXNAMOUNT";
        String txnpaymentMode = "PAYMENTMODE";
        String txncurrencyName = "CURRENCY";
        String txntransactionDate = "TXNDATE";
        String txnsuccessFlag = "RESPCODE";
        txnerrorDescription = "RESPMSG";
        String txnmessageType = "RESPMSG";
        String txnbankTransactionNo = "BANKTXNID";
        String txnhash = "CHECKSUMHASH";

        Log.d("Response PAYMENTSUCCES", txntransactionStatus + "||" + txnorderId + "||" + txnmerchantID + "||" + txntxnId
                + "||" + txngatewayAmount + "||" + txnpaymentMode + "||" + txncurrencyName + "||" + txntransactionDate + "||" + txnerrorDescription + "||" + txnbankTransactionNo);

        String gatewayMoreInfo = "MOREINFO";
        String gatewayStatus = txntransactionStatus;
        String bookingTransactionID = txntxnId;


        if (returnBooking != null) {
            Log.d("returnBooking", "7");
        } else {

            if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

                if (divyangBooking != null) {
                    AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroid(onwardBooking, divyangBooking,
                            txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnpaymentMode, gatewayMoreInfo,
                            txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroidCallback() {
                                @Override
                                public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                                    if (response != null) {
                                        onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                        divyangBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                        takeConfirmBack();

                                    } else {

                                        Log.d("5", "5");
                                        takeBackWithError();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.d("6", "6");
                                    takeBackWithError();
                                }
                            });
                }


            } else if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {

                AdvanceBookingNetworkUtil.ConfirmAdvDivyangSeatBookingAndroid(onwardBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnpaymentMode, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvDivyangSeatBookingAndroidCallback() {
                    @Override
                    public void onSuccess(ConfirmAdvSeatBookingAndroidResponse response) {
                        if (response != null) {
                            onwardBooking.setConfirmAdvSeatBookingAndroidResponse(response);

                            takeConfirmBack();

                        } else {

                            Log.d("7", "7");
                            takeBackWithError();

                        }
                    }

                    @Override
                    public void onError(Exception e) {

                        Log.d("8", "8");
                        takeBackWithError();
                    }
                });

            }
        }
    }
    
    
    public void takeConfirmBack(){
        

                CustomisedProgressDialog.STOP_CUST_DIA();
                Bundle extras = new Bundle();
                extras.putString("textlang", textlang);
                extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                if (returnBooking != null) {
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
                }
                if (divyangBooking != null) {
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, divyangBooking);
                }
                ActivityUtil.openNewActivity(ReferenceNumberActivity.this, BookingSuccessActivity.class, extras); CustomisedProgressDialog.STOP_CUST_DIA();
               

    }

    public void takeBackWithError() {
        Intent in =  new Intent(ReferenceNumberActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
}
