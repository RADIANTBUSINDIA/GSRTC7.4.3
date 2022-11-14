package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.advance_booking.FareDetailsActivity;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaytmPaymentGateway extends AppCompatActivity {

    ProgressBar progressBar;
    String midString;
    String txnAmountString;
    String orderIdString;
    String txnTokenString;

    private static long checksumval;
    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String txnsuccessFlag = null, txnmessageType = null, txnmerchantID = null, txnserviceId = null, txnorderId = null, txncustomerId = null, txngatewayAmount = null,
            txncurrencyName = null, txnpaymentMode = null, txntransactionDate = null, txntxnId = null, txnbankTransactionNo = null, txntransactionStatus = null, txnadditionalInfo1 = null,
            txnadditionalInfo2 = null, txnadditionalInfo3 = null, txnadditionalInfo4 = null, txnadditionalInfo5 = null, txnerrorStatus = null, txnerrorDescription = null, txncheckSum = null,
            txnFailureFlag = null, txnerrorCode = null, txnerrorMessage = null, txnPendingFlag = null, txnDeclineFlag = null, txnhash = null;

    private String gatewayMoreInfo, gatewayRemarks, gatewayStatus, bookingTransactionID;
    private String isCard, getTxntkn;
    String textlang;
    private String checksumMsg = "";
    private Request request;

    Booking onwardBooking, returnBooking, divyangBooking;

    Integer ActivityRequestCode = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment_gateway);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        totalFare = extras.getString("totalFare", "");
        isCard = extras.getString("isCard");
        textlang = extras.getString("textlang");
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);
        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG)) {
            divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
        }

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        }

        totalFare = getIntent().getExtras().getString("totalFare", "");

        if (returnBooking != null) {
            rtcRefNo = returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            returnPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber();
            onwardPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber();

        } else {
            if (divyangBooking != null && divyangBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
                rtcRefNo = divyangBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
                returnPNR = divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber();
                onwardPNR = onwardBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber();
            } else {
                rtcRefNo = onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo();
                returnPNR = "";
                onwardPNR = onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber();
            }
        }

        Log.d("Ticket Parameters", totalFare + "," + onwardPNR + "," + returnPNR);

        pnr = onwardPNR;
        if (!TextUtils.isEmpty(returnPNR))
            pnr += "" + returnPNR;

        Log.d("pnr", pnr);


        getTransToken();

    }

    private void getTransToken() {
        if (!PaytmPaymentGateway.this.isFinishing()) {
            CustomisedProgressDialog.SHOW_CUST_DIA(PaytmPaymentGateway.this, "processing...");
        }

        String URL_STATUS = PaytmConstants.PG_TXNTOKEN_URL + rtcRefNo + "," + totalFare;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value = response.toString();
                Log.d("VOLLEY", response);

                if (response != null) {
                    CustomisedProgressDialog.STOP_CUST_DIA();

                    getTxntkn = response;
                    if (getTxntkn.equalsIgnoreCase("")) {
                        Toast.makeText(PaytmPaymentGateway.this, "Invalid Txn Token", Toast.LENGTH_SHORT).show();

                    } else {
                        PaytmGatewayCall();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getTxntkn = "";
                PaytmGatewayCall();
                Log.d("ERRORTXTTKN", String.valueOf(error));
                CustomisedProgressDialog.STOP_CUST_DIA();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaytmPaymentGateway.this, R.style.CustomDialogTheme);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(PaytmPaymentGateway.this, HomeActivity.class);
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

    public void PaytmGatewayCall() {
        progressBar.setVisibility(View.VISIBLE);

        String errors = "";

        if (PaytmConstants.PG_MERCHANT_ID.equalsIgnoreCase("")) {
            errors += "Invalid MID here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if (rtcRefNo.equalsIgnoreCase("")) {
            errors += "Invalid Order ID here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if (getTxntkn.equalsIgnoreCase("")) {
            errors += "Invalid Txn Token here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }
        if (totalFare.equalsIgnoreCase("")) {
            errors += "Invalid Txn Amount here";
            Toast.makeText(this, errors, Toast.LENGTH_SHORT).show();
        }


        progressBar.setVisibility(View.GONE);
        if (errors.equalsIgnoreCase("")) {
            String orderDetails = "MID: " + PaytmConstants.PG_MERCHANT_ID + ", OrderId: " + rtcRefNo + ", TxnToken: " + getTxntkn + ", Amount: " + totalFare;
            Log.d("orderDetails", orderDetails);

            String callBackUrl = PaytmConstants.PG_TRANSACTION_URL + "theia/paytmCallback?ORDER_ID=" + rtcRefNo;
            PaytmOrder paytmOrder = new PaytmOrder(rtcRefNo, PaytmConstants.PG_MERCHANT_ID, getTxntkn, totalFare, callBackUrl);
            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

                /**      {"ORDERID":"GS20144804", "MID":"GUJARA80844935267658", "TXNID":"20220429111212800110168695342421154",
                 "TXNAMOUNT":"1.00", "PAYMENTMODE":"UPI", "CURRENCY":"INR", "TXNDATE":"2022-04-29 17:55:23.0",
                 "STATUS":"TXN_SUCCESS", "RESPCODE":"01", "RESPMSG":"Txn Success", "GATEWAYNAME":"PPBLC",
                 "BANKTXNID":"211946184219", "CHECKSUMHASH":"vaRjk51jxnOjRjvEBEhQBLUkR4wsmhfR5fype5jMv+82AG/4bWAeAhQ3H3VBhmCC3ux4wcPbq6c+63qU0QOY8eqbfSQPnMaKW5xG4T0dio8="}  **/

                @Override
                public void onTransactionResponse(Bundle bundle) {

                    if (bundle != null) {

                        if (bundle.getString("STATUS") != null) {
                            Log.d("RESPONSE STATUS", String.valueOf(bundle));

                            txntransactionStatus = bundle.getString("STATUS");
                            txnerrorDescription = bundle.getString("RESPMSG");
                            Log.d("TRANS STATUS", txntransactionStatus);

                            if (txntransactionStatus.equalsIgnoreCase("TXN_SUCCESS")) {
                                txnorderId = bundle.getString("ORDERID");
                                txnmerchantID = bundle.getString("MID");
                                txntxnId = bundle.getString("TXNID");
                                txngatewayAmount = bundle.getString("TXNAMOUNT");
                                txnpaymentMode = bundle.getString("PAYMENTMODE");
                                txncurrencyName = bundle.getString("CURRENCY");
                                txntransactionDate = bundle.getString("TXNDATE");
                                txnsuccessFlag = bundle.getString("RESPCODE");
                                txnerrorDescription = bundle.getString("RESPMSG");
                                txnmessageType = bundle.getString("RESPMSG");
                                txnbankTransactionNo = bundle.getString("BANKTXNID");
                                txnhash = bundle.getString("CHECKSUMHASH");

                                Log.d("Response PAYMENTSUCCES", txntransactionStatus + "||" + txnorderId + "||" + txnmerchantID + "||" + txntxnId
                                        + "||" + txngatewayAmount + "||" + txnpaymentMode + "||" + txncurrencyName + "||" + txntransactionDate + "||" + txnerrorDescription + "||" + txnbankTransactionNo);

                                gatewayMoreInfo = bundle.toString();
                                gatewayStatus = txntransactionStatus;
                                bookingTransactionID = txntxnId;

                                if (txnorderId.equalsIgnoreCase(rtcRefNo)) {

                                    confirmTicket();

                                } else {

                                    failedTicket();
                                    Toast.makeText(PaytmPaymentGateway.this, txnerrorDescription, Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                failedTicket();
                                Toast.makeText(PaytmPaymentGateway.this, txnerrorDescription, Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            failedTicket();
                            Toast.makeText(PaytmPaymentGateway.this, txnerrorDescription, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        txnerrorDescription = "Payment Not Completed";
                        Toast.makeText(PaytmPaymentGateway.this, txnerrorDescription, Toast.LENGTH_SHORT).show();
                        failedTicket();

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

            Log.d("status", String.valueOf(data.getStringExtra("response")));
        }
    }

    /***calling CONFIRM TICKET BOOKING METHOD  ****/

    public void confirmTicket() {

        if (returnBooking != null) {

            AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(onwardBooking, returnBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnpaymentMode, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
                @Override
                public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                    if (response != null) {
                        onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                        returnBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                        takeBack();

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


        } else {

            if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

                if (divyangBooking != null) {
                    AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroid(onwardBooking, divyangBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnpaymentMode, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                divyangBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                takeBack();

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

                            takeBack();

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

            } else {
                AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(onwardBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnpaymentMode, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
                    @Override
                    public void onSuccess(ConfirmAdvSeatBookingAndroidResponse response) {
                        if (response != null) {
                            onwardBooking.setConfirmAdvSeatBookingAndroidResponse(response);

                            takeBack();

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

    /***calling CONFIRM BOOKING RESPONSE PASSING TO REFNO ACTIVITY ****/

    public void takeBack() {
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
        if (returnBooking != null) {
            resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
        }
        if (divyangBooking != null) {
            resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, divyangBooking);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public void failedTicket() {

        Intent intent = new Intent(PaytmPaymentGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription + " |OB.Ref:" + rtcRefNo);
        startActivity(intent);
        finish();
    }


    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PaytmPaymentGateway.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(PaytmPaymentGateway.this, HomeActivity.class);
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