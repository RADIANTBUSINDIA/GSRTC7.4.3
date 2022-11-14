package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
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
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentActivity;

import androidx.appcompat.app.AppCompatActivity;

public class PaytmPaymentGatewayCurrent extends AppCompatActivity {

    ProgressBar progressBar;

    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String txnsuccessFlag = null, txnmessageType = null, txnmerchantID = null, txnserviceId = null, txnorderId = null, txncustomerId = null, txngatewayAmount = null,
            txncurrencyName = null, txnpaymentMode = null, txntransactionDate = null, txntxnId = null, txnbankTransactionNo = null, txntransactionStatus = null, txnadditionalInfo1 = null,
            txnadditionalInfo2 = null, txnadditionalInfo3 = null, txnadditionalInfo4 = null, txnadditionalInfo5 = null, txnerrorStatus = null, txnerrorDescription = null, txncheckSum = null,
            txnFailureFlag = null, txnerrorCode = null, txnerrorMessage = null, txnPendingFlag = null,txnDeclineFlag=null;

    private String gatewayMoreInfo, gatewayRemarks, gatewayStatus, bookingTransactionID;
    private String isCard, getTxntkn,pnrNo,journyDate;
    String textlang;

    Booking onwardBooking, returnBooking;

    Integer ActivityRequestCode = 2;
    SearchParamsCurrent searchParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment_gateway);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang = searchParams.getCrntlanguage().trim();
        }

        totalFare = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
        rtcRefNo = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
        pnrNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
        journyDate =searchParams.getJourneyDate_GetCurrentTotalFareDetailsOfTicket();

        isCard = searchParams.getGatewayLookupId();
        Log.e("Paymode", isCard);

        //totalFare ="1.00";
        getTransToken();

    }

    private void getTransToken() {

        if(!PaytmPaymentGatewayCurrent.this.isFinishing())
        {
            CustomisedProgressDialog.SHOW_CUST_DIA(PaytmPaymentGatewayCurrent.this, "processing...");
        }

        String URL_STATUS = PaytmConstants.PG_TXNTOKEN_URL+rtcRefNo+","+totalFare;

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value = response.toString();
                Log.i("VOLLEY", response);

                if (response != null) {
                    CustomisedProgressDialog.STOP_CUST_DIA();

                    getTxntkn = response;
                    if(getTxntkn.equalsIgnoreCase("")){
                        Toast.makeText(PaytmPaymentGatewayCurrent.this, "Invalid Txn Token", Toast.LENGTH_SHORT).show();

                    }else {
                        PaytmGatewayCall();
                    }

                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getTxntkn="";
                PaytmGatewayCall();
                Log.d("ERRORTXTTKN", String.valueOf(error));
                CustomisedProgressDialog.STOP_CUST_DIA();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaytmPaymentGatewayCurrent.this,R.style.CustomDialogTheme);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(PaytmPaymentGatewayCurrent.this, HomeActivity.class);
                        Bundle data = new Bundle();
                        data.putString("textlang", textlang);
                        i.putExtras(data);
                        startActivity(i);
                    }
                });

                // alertDialog.setNegativeButton("", null);
                alertDialog.setMessage("Error Occured! Please Try again(Txn Token not Generated).");
                alertDialog.setTitle("GSRTC");
                alertDialog.show();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(stringRequest);

    }

    public void PaytmGatewayCall(){
        progressBar.setVisibility(View.VISIBLE);

        String errors = "";

        if(PaytmConstants.PG_MERCHANT_ID.equalsIgnoreCase("")){
            errors +="Invalid MID here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if(rtcRefNo.equalsIgnoreCase("")){
            errors +="Invalid Order ID here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if(getTxntkn.equalsIgnoreCase("")){
            errors +="Invalid Txn Token here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if(totalFare.equalsIgnoreCase("")){
            errors +="Invalid Txn Amount here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }


        progressBar.setVisibility(View.GONE);
        if(errors.equalsIgnoreCase("")){
            String orderDetails = "MID: " + PaytmConstants.PG_MERCHANT_ID + ", OrderId: " + rtcRefNo + ", TxnToken: " + getTxntkn + ", Amount: " + totalFare;
            Log.d("orderDetails", orderDetails);

            String callBackUrl = PaytmConstants.PG_TRANSACTION_URL + "theia/paytmCallback?ORDER_ID="+rtcRefNo;
            PaytmOrder paytmOrder = new PaytmOrder(rtcRefNo, PaytmConstants.PG_MERCHANT_ID, getTxntkn, totalFare, callBackUrl);
            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){

                /**      {"ORDERID":"GS20144804", "MID":"GUJARA80844935267658", "TXNID":"20220429111212800110168695342421154",
                 "TXNAMOUNT":"1.00", "PAYMENTMODE":"UPI", "CURRENCY":"INR", "TXNDATE":"2022-04-29 17:55:23.0",
                 "STATUS":"TXN_SUCCESS", "RESPCODE":"01", "RESPMSG":"Txn Success", "GATEWAYNAME":"PPBLC",
                 "BANKTXNID":"211946184219", "CHECKSUMHASH":"vaRjk51jxnOjRjvEBEhQBLUkR4wsmhfR5fype5jMv+82AG/4bWAeAhQ3H3VBhmCC3ux4wcPbq6c+63qU0QOY8eqbfSQPnMaKW5xG4T0dio8="}  **/

                @Override
                public void onTransactionResponse(Bundle bundle) {

                    if (bundle != null) {

                        if (bundle.getString("STATUS") != null) {
                            Log.d("RESPONSE STATUS", String.valueOf(bundle));

                            txntransactionStatus =bundle.getString("STATUS");
                            txnerrorDescription =bundle.getString("RESPMSG");
                            Log.d("TRANS STATUS", txntransactionStatus);

                            if (txntransactionStatus.equalsIgnoreCase("TXN_SUCCESS")) {

                                txnorderId =bundle.getString("ORDERID");
                                txnmerchantID =bundle.getString("MID");
                                txntxnId =bundle.getString("TXNID");
                                txngatewayAmount =bundle.getString("TXNAMOUNT");
                                txnpaymentMode =bundle.getString("PAYMENTMODE");
                                txncurrencyName =bundle.getString("CURRENCY");
                                txntransactionDate =bundle.getString("TXNDATE");
                                txnsuccessFlag =bundle.getString("RESPCODE");
                                txnerrorDescription =bundle.getString("RESPMSG");
                                txnmessageType =bundle.getString("RESPMSG");
                                txnbankTransactionNo =bundle.getString("BANKTXNID");
                                String txnhash=bundle.getString("CHECKSUMHASH");

                                Log.d("Response PAYMENTSUCCES", txntransactionStatus+"||"+txnorderId+"||"+txnmerchantID+"||"+txntxnId
                                        +"||"+txngatewayAmount+"||"+txnpaymentMode+"||"+txncurrencyName+"||"+txntransactionDate+"||"+txnerrorDescription+"||"+txnbankTransactionNo);

                                gatewayMoreInfo = bundle.toString();
                                gatewayStatus = txntransactionStatus;
                                bookingTransactionID = txntxnId;

                                if (txnorderId.equalsIgnoreCase(rtcRefNo)) {

                                    searchParams.setBookingTransactionID(bookingTransactionID);

                                    confirmTicket();

                                } else {

                                    failedTicket();
                                    Toast.makeText(PaytmPaymentGatewayCurrent.this, txnerrorDescription, Toast.LENGTH_SHORT).show();
                                }

                            }else{

                                failedTicket();
                                Toast.makeText(PaytmPaymentGatewayCurrent.this, txnerrorDescription, Toast.LENGTH_SHORT).show();

                            }
                        }else{

                            failedTicket();
                            Toast.makeText(PaytmPaymentGatewayCurrent.this, txnerrorDescription, Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        txnerrorDescription="Payment Not Completed";
                        failedTicket();
                        Toast.makeText(PaytmPaymentGatewayCurrent.this, txnerrorDescription, Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void networkNotAvailable() {

                }

                @Override
                public void onErrorProceed(String s) {

                }

                @Override
                public void clientAuthenticationFailed(String s) {

                }

                @Override
                public void someUIErrorOccurred(String s) {

                }

                @Override
                public void onErrorLoadingWebPage(int i, String s, String s1) {

                }

                @Override
                public void onBackPressedCancelTransaction() {

                }

                @Override
                public void onTransactionCancel(String s, Bundle bundle) {

                }
            });

            transactionManager.setAppInvokeEnabled(false);
            transactionManager.setShowPaymentUrl(PaytmConstants.PG_TRANSACTION_URL + "theia/api/v1/showPaymentPage");
            //transactionManager.startTransaction(this, ActivityRequestCode);
            transactionManager.startTransactionAfterCheckingLoginStatus(this, "GSRTC", ActivityRequestCode);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
            Log.d("status", String.valueOf(data.getStringExtra("response")));
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
                    ActivityUtil.openNewActivity(PaytmPaymentGatewayCurrent.this, CurrentBookingSuccessActivity.class, extra);
                    finish();

                } else {
                    Log.d("5", "5");
                    takeBackWithError();
                }
            }
            @Override
            public void onError(Exception e) {

                ActivityUtil.showErrorToast(PaytmPaymentGatewayCurrent.this, e);
            }
        });
    }

    /***calling CONFIRM BOOKING RESPONSE PASSING TO REFNO ACTIVITY ****/

    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public  void failedTicket() {

        Intent intent = new Intent(PaytmPaymentGatewayCurrent.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription+" |OB.Ref:"+rtcRefNo);
        startActivity(intent);
        finish();
    }



    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PaytmPaymentGatewayCurrent.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(PaytmPaymentGatewayCurrent.this, HomeActivity.class);
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