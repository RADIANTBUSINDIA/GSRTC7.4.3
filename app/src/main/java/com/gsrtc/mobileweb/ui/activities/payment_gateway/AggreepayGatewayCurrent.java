package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSuccessActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class AggreepayGatewayCurrent extends AppCompatActivity {


    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String txnsuccessFlag=null, txnmessageType=null, txnmerchantID=null, txnserviceId=null, txnorderId=null, txncustomerId=null, txngatewayAmount=null,
            txncurrencyName=null, txnpaymentMode=null, txntransactionDate=null, txntxnId=null, txnbankTransactionNo=null, txntransactionStatus=null, txnadditionalInfo1=null,
            txnadditionalInfo2=null, txnadditionalInfo3=null, txnadditionalInfo4=null, txnadditionalInfo5=null, txnerrorStatus=null, txnerrorDescription=null, txnhash=null,
            txnFailureFlag=null,txnerrorCode=null,txnerrorMessage=null,txnPendingFlag=null;

    private String gatewayMoreInfo,gatewayRemarks,gatewayStatus,bookingTransactionID;
    private String isCard,getTxntkn,textlang;
    Booking onwardBooking, returnBooking;

    ProgressBar pb;

    private String amount,journyDate,pnrNo;

    private String checksumMsg = "";
    private Request request;

    SearchParamsCurrent searchParams;
    String mail,name,Number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggreepay_gateway);
        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang = searchParams.getCrntlanguage().trim();
            mail= searchParams.getPassengerEmail() ;
            name=searchParams.getPassengerName();
            Number=searchParams.getPassengerMobile() ;
        }

        totalFare = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
        rtcRefNo = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
        pnrNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
        journyDate =searchParams.getJourneyDate_GetCurrentTotalFareDetailsOfTicket();





        isCard = searchParams.getGatewayLookupId();
        Log.e("Paymode", isCard);

        showpaymentpage();

    }

    private void showpaymentpage() {


            pb.setVisibility(View.VISIBLE);

            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            Constants.PG_ORDER_ID="Test"+Integer.toString(n);

            PaymentParams pgPaymentParams = new PaymentParams();
            pgPaymentParams.setAPiKey(Constants.PG_API_KEY);
            pgPaymentParams.setAmount(totalFare);
            pgPaymentParams.setEmail(mail);
            pgPaymentParams.setName(name);
            pgPaymentParams.setPhone(Number);
            pgPaymentParams.setOrderId(rtcRefNo);
            pgPaymentParams.setCurrency(Constants.PG_CURRENCY);
            pgPaymentParams.setDescription(Constants.PG_DESCRIPTION);
            pgPaymentParams.setCity(Constants.PG_CITY);
            pgPaymentParams.setState(Constants.PG_STATE);
            pgPaymentParams.setAddressLine1(Constants.PG_ADD_1);
            pgPaymentParams.setAddressLine2(Constants.PG_ADD_2);
            pgPaymentParams.setZipCode(Constants.PG_ZIPCODE);
            pgPaymentParams.setCountry(Constants.PG_COUNTRY);
            pgPaymentParams.setReturnUrl(Constants.PG_RETURN_URL);
            pgPaymentParams.setMode(Constants.PG_MODE);
            pgPaymentParams.setUdf1(rtcRefNo);
            pgPaymentParams.setUdf2(onwardPNR);
            pgPaymentParams.setUdf3(returnPNR);
            pgPaymentParams.setUdf4(Constants.PG_UDF4);
            pgPaymentParams.setUdf5(Constants.PG_UDF5);
            pgPaymentParams.setEnableAutoRefund("n");
            pgPaymentParams.setOfferCode("");

            PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, AggreepayGatewayCurrent.this);
            pgPaymentInitialzer.initiatePaymentProcess();


    }

    @Override
    public void onStop() {
        super.onStop();
        pb.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                pb.setVisibility(View.GONE);
                try{
                    String paymentResponse=data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: "+paymentResponse);
                    if(paymentResponse.equals("null")){
                        System.out.println("Transaction Error!");
                        txnerrorDescription ="Transaction Error!";
                        failedTicket();
                        Toast.makeText(getApplicationContext(), "Transaction Error!", Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject response = new JSONObject(paymentResponse);

                        txntransactionStatus = response.getString("response_message");
                        txnerrorDescription = response.getString("error_desc");

                        if (txntransactionStatus.equalsIgnoreCase("Transaction successful")) {
                            txnorderId = response.getString("order_id");
                            txngatewayAmount = response.getString("amount");
                            txncurrencyName = response.getString("currency");
                            txnbankTransactionNo = response.getString("transaction_id");
                            txnpaymentMode = response.getString("payment_mode");
                            txntransactionDate = response.getString("payment_datetime");
                            txnmessageType = response.getString("response_code");
                            txnsuccessFlag = response.getString("response_message");
                            txnmerchantID = response.getString("merchant_id");
                            txnhash = response.getString("hash");

                            gatewayMoreInfo = paymentResponse.toString();
                            gatewayStatus = txntransactionStatus;
                            bookingTransactionID = txnbankTransactionNo;

                            if (txntransactionStatus.equalsIgnoreCase("Transaction successful")) {
                                if (txnmessageType.equalsIgnoreCase("0")) {
                                    if (txnorderId.equalsIgnoreCase(rtcRefNo)) {
                                        searchParams.setBookingTransactionID(bookingTransactionID);
                                        confirmTicket();
                                    } else {
                                        failedTicket();
                                        Toast.makeText(getApplicationContext(), txnerrorDescription, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    failedTicket();
                                    Toast.makeText(getApplicationContext(), txnerrorDescription, Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                failedTicket();
                                Toast.makeText(getApplicationContext(), txnerrorDescription, Toast.LENGTH_SHORT).show();

                            }
                        }else{

                            failedTicket();
                            Toast.makeText(getApplicationContext(), txnerrorDescription, Toast.LENGTH_SHORT).show();

                        }
                    }

                }catch (JSONException e){
                    pb.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                pb.setVisibility(View.GONE);
                //Write your code if there's no result
                txnerrorDescription ="Transaction Error!";
                failedTicket();
                Toast.makeText(getApplicationContext(), "Transaction Error!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /***calling CONFIRM TICKET BOOKING METHOD  ****/

    public void confirmTicket() {

        SoapClientUtil.conformCurrentSeatBooking(searchParams, new SoapClientUtil.ConfirmCurrentSeatBookingAndroidCallback() {

            @Override
            public void onSuccess(ConfirmCurrentSeatBookingAndroidResponse confirmCurrentSeatBookingAndroidResponse) {
                if (confirmCurrentSeatBookingAndroidResponse != null) {
                    ConfirmCurrentSeatBookingAndroidResponse confirmCurrentSeatBookingAndroidResponse1 = confirmCurrentSeatBookingAndroidResponse;

                    String confirmStatus = confirmCurrentSeatBookingAndroidResponse1.getConfirmStatus();
                    searchParams.setConfirmStatus(confirmStatus);

                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    ActivityUtil.openNewActivity(AggreepayGatewayCurrent.this, CurrentBookingSuccessActivity.class, extra);
                    finish();

                } else {
                    Log.d("5", "5");
                    takeBackWithError();
                }
            }
            @Override
            public void onError(Exception e) {

                ActivityUtil.showErrorToast(AggreepayGatewayCurrent.this, e);
            }
        });
    }

    /***calling CONFIRM BOOKING RESPONSE PASSING TO REFNO ACTIVITY ****/

    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public  void failedTicket() {

        Intent intent = new Intent(AggreepayGatewayCurrent.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription+" |OB.Ref:"+rtcRefNo);
        startActivity(intent);
        finish();
    }


    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AggreepayGatewayCurrent.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(AggreepayGatewayCurrent.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
        alertDialog.setNegativeButton("NO", null);
        alertDialog.setMessage("Going back will discard your booking. Do you want to continue?");
        alertDialog.setTitle("GSRTC Payment");
        alertDialog.show();
    }
}