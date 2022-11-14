package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBilldeskGateway;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSuccessActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.ndml.surepay.SurePay;
import com.ndml.surepay.SurePayCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@SuppressLint("JavascriptInterface") public class NSDL_PaymentCurrentBooking extends AppCompatActivity {


    private static long checksumval;
    private String txnsuccessFlag = null, txnmessageType = null, txnmerchantID = null, txnserviceId = null, txnorderId = null, txncustomerId = null, txngatewayAmount = null,
            txncurrencyName = null, txnpaymentMode = null, txntransactionDate = null, txntxnId = null, txnbankTransactionNo = null, txntransactionStatus = null, txnadditionalInfo1 = null,
            txnadditionalInfo2 = null, txnadditionalInfo3 = null, txnadditionalInfo4 = null, txnadditionalInfo5 = null, txnerrorStatus = null, txnerrorDescription = null, txncheckSum = null,
            txnFailureFlag = null, txnerrorCode = null, txnerrorMessage = null, txnPendingFlag = null,txnDeclineFlag=null;

    private String gatewayMoreInfo, gatewayRemarks, gatewayStatus, bookingTransactionID;
    private String isCard, getTxntkn,amount,journyDate,pnrNo;
    String textlang;
    private String checksumMsg = "";
    private Request request;

    Booking onwardBooking, returnBooking;
    SearchParamsCurrent searchParams;

    //STAGING

//    private String merchantId="UATGSRTCSG0000001145";
//    private String messageType="0100";
//    private String currency="INR";
//    private String serviceId="MTICKET";
//    private String customerId ="8080656545";
//    private String secretKey ="c2e9f13f6d0807d218ce4e9953270738d96053f2189b7748ee42a3278d3d967e";
//    private String successUrl="http://182.74.17.187:8080/GSRTCOnline/confirmPayment.do";
//    private String failUrl="http://182.74.17.187:8080/GSRTCOnline/confirmPayment.do";

    //PRODUCTION

    private String merchantId = "GSRTCSG0000000109";
    private String messageType = "0100";
    private String currency = "INR";
    private String serviceId = "MTICKET";
    private String customerId = "8080656545";
    private String secretKey = "583b94a14e03bcb860544637c22ba2160cd87f96b191a4092b1018fd2d2437cb";
    private String successUrl = "https://gsrtc.in/GSRTCOnline/androidPayGovPayment.do";
    private String failUrl = "https://gsrtc.in/GSRTCOnline/androidPayGovPayment.do";
    private String requestDateTime,rtcRefNo;
    private WebView mWviewPay;
    private String pnr,paymentMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_s_d_l__payment_current_booking);

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang = searchParams.getCrntlanguage().trim();
        }

        amount = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
        rtcRefNo = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
        pnrNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
        journyDate =searchParams.getJourneyDate_GetCurrentTotalFareDetailsOfTicket();
        customerId= searchParams.getPassengerMobile();
        isCard = searchParams.getGatewayLookupId();
        Log.e("Paymode", isCard);

//  String message =messageType|merchantId|serviceId|orderId|customerId|transactionAmount|currencyCode|requestDateTime|
//  successUrl|failUrl|additionalFeild1| additionalFeild2| additionalFeild3|additionalFeild4| additionalFeild5

        requestDateTime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        checksumMsg = messageType + "|" + merchantId + "|" + serviceId + "|" + rtcRefNo + "|" + customerId + "|" + amount + "|" + currency + "|" + requestDateTime + "|" + successUrl + "|" + failUrl + "|||||";

        generateCRC32Checksum(checksumMsg, secretKey);

        getTransToken();


    }

    /**********************checksum Logic*********************/

    public static String generateCRC32Checksum(String message, String secretKey) {
        String msg = message + "|" + secretKey;
        byte bytes[] = msg.getBytes();
        Checksum checksum = new CRC32();
// update the current checksum with the specified array of bytes
        checksum.update(bytes, 0, bytes.length);
// get the current checksum value
        long checksumValue = checksum.getValue();
        checksumval = checksum.getValue();

// logger.info("CRC32 checksum for input string is: " +checksumValue);
        return String.valueOf(checksumValue);
    }

    /************************gettxnTokenn******/

    public void getTransToken() {

        CustomisedProgressDialog.SHOW_CUST_DIA(NSDL_PaymentCurrentBooking.this, "processing...");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //STAGING URL
        // String URL_TKN="https://pilot.surepay.ndml.in/SurePayPayment/generateTransactionId";
        //PRODUCTION URL
        String URL_TKN = "https://surepay.ndml.in/SurePayPayment/generateTransactionId";

        JSONObject jsonBody = new JSONObject();
        try {

            jsonBody.put("merchantId", merchantId);
            jsonBody.put("messageType", messageType);
            jsonBody.put("serviceId", serviceId);
            jsonBody.put("orderId", rtcRefNo);
            jsonBody.put("customerId", searchParams.getPassengerMobile());
            jsonBody.put("transactionAmount", amount);
            jsonBody.put("currencyCode", currency);
            jsonBody.put("requestDateTime", requestDateTime);
            jsonBody.put("successUrl", successUrl);
            jsonBody.put("failUrl", failUrl);
            jsonBody.put("additionalField1", "");
            jsonBody.put("additionalField2", "");
            jsonBody.put("additionalField3", "");
            jsonBody.put("additionalField4", "");
            jsonBody.put("additionalField5", "");
            jsonBody.put("checksum", checksumval);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_TKN, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    if (response != null) {
                        try {
                            getTxntkn = response.getString("token");
                            payamount();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void payamount() {

        SurePay.ActivityBuilder builder = SurePay.activity();
        builder.setToken(getTxntkn);
        //builder.setSurePayUrl("https://pilot.surepay.ndml.in/surepay-webapp/?token="); // stagingurl
        builder.setSurePayUrl("https://surepay.ndml.in/surepay-webapp/?token="); // productionurl
        builder.setSuccessUrl(successUrl); // same as when generate transaction id
        builder.setFailureUrl(failUrl); // same as when generate transaction id
        builder.printLogs(false); // true = print, false = not print
        builder.printStackTrace(false); // true = print, false = not print
        CustomisedProgressDialog.STOP_CUST_DIA();
        builder.setCallback(new SurePayCallback() {
            @Override
            public void callback(String s) {

                getResp();
                CustomisedProgressDialog.STOP_CUST_DIA();

            }
        });

        builder.start(NSDL_PaymentCurrentBooking.this);
    }

    public void getResp() {
        //String URL_STATUS = "https://surepay.ndml.in/SurePayPayment/v1/queryPaymentStatus?requestMsg=|GSRTCSG0000000109|GS14018824";

        String URL_STATUS = "https://surepay.ndml.in/SurePayPayment/v1/queryPaymentStatus?requestMsg=|"+merchantId+"|"+rtcRefNo;

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("merchantId", merchantId);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STATUS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String value = response.toString();
                    Log.i("VOLLEY", response);

                    char first = value.charAt(0);
                    char pgResponse = first;
                    char SUCCESS = 'S';
                    char FAILURE_FLAG = 'F';
                    char DECLINE_FLAG = 'D';

                    String msgPayGov[] = value.split("\\|");
                    System.out.println("PAY GOV[5]pgResponse:" + pgResponse + ":SUCCESS:" + SUCCESS);
                    if (pgResponse == SUCCESS) {
                        System.out.println("PAY GOV SUCESS FLOW");
                        txnsuccessFlag = msgPayGov[0];//S
                        txnmessageType = msgPayGov[1];//0100
                        txnmerchantID = msgPayGov[2];//UATGSRTCSG0000001145
                        txnserviceId = msgPayGov[3];//ETICKET
                        txnorderId = msgPayGov[4];//GS7610752
                        txncustomerId = msgPayGov[5];//G25427127
                        txngatewayAmount = msgPayGov[6];//128.90
                        txncurrencyName = msgPayGov[7];//INR
                        txnpaymentMode = msgPayGov[8];//Debit-Card
                        txntransactionDate = msgPayGov[9];//26-03-2021 1:10
                        txntxnId = msgPayGov[10];//29155
                        txnbankTransactionNo = msgPayGov[11];//pay_GrHSfetIrzgyA8
                        txntransactionStatus = msgPayGov[12];//A
                        txnadditionalInfo1 = msgPayGov[13];
                        txnadditionalInfo2 = msgPayGov[14];
                        txnadditionalInfo3 = msgPayGov[15];
                        txnadditionalInfo4 = msgPayGov[16];
                        txnadditionalInfo5 = msgPayGov[17];
                        txnerrorStatus = msgPayGov[18];
                        txnerrorDescription = msgPayGov[19];
                        txncheckSum = msgPayGov[20];
                    } else if (pgResponse == FAILURE_FLAG) {
                        System.out.println("PAY GOV[7]FAILURE_FLAG");
                        txnFailureFlag = msgPayGov[0];//S
                        txnmerchantID = msgPayGov[1];//0100
                        txnorderId = msgPayGov[2];//UATGSRTCSG0000001145
                        txnserviceId = msgPayGov[3];//ETICKET
                        txnpaymentMode = msgPayGov[4];//GS7610752
                        txnbankTransactionNo = msgPayGov[5];//pay_GrHSfetIrzgyA8
                        txnerrorCode = msgPayGov[6];
                        txnerrorMessage = msgPayGov[7];
                        txnerrorDescription = msgPayGov[8];
                        txntransactionDate = msgPayGov[9];//26-03-2021 1:10
                        txncheckSum = msgPayGov[10];
                    } else if (pgResponse == DECLINE_FLAG)  {
                        System.out.println("PAY GOV DECLINE_FLAG");
                        txnDeclineFlag = msgPayGov[0];//P
                        txnmerchantID = msgPayGov[1];//0100
                        txnorderId = msgPayGov[2];//UATGSRTCSG0000001145
                        txnserviceId = msgPayGov[3];//ETICKET
                        txnpaymentMode = msgPayGov[4];//GS7610752
                        txnerrorDescription = msgPayGov[5];
                        txntransactionDate = msgPayGov[6];//26-03-2021 1:10
                        txncheckSum = msgPayGov[7];
                    }else{
                        System.out.println("PAY GOV[8]PENDING_FLAG");
                        txnPendingFlag = msgPayGov[0];//P
                        txnmerchantID = msgPayGov[1];//0100
                        txnorderId = msgPayGov[2];//UATGSRTCSG0000001145
                        txnserviceId = msgPayGov[3];//ETICKET
                        txnpaymentMode = msgPayGov[4];//GS7610752
                        txnerrorDescription = msgPayGov[5];
                        txntransactionDate = msgPayGov[6];//26-03-2021 1:10
                        txncheckSum = msgPayGov[7];
                    }

                    System.out.println("PAY GOV[9]:PAYMENT GATEWAY ONLINE RESPONSE:"
                            + ":SuccessFlag:" + txnsuccessFlag + ":MessageType:" + txnmessageType
                            + ":SurePayMerchantId:" + txnmerchantID + ":ServiceId:" + txnserviceId
                            + ":OrderId:" + txnorderId + ":CustomerId:" + txncustomerId
                            + ":TransactionAmount:" + txngatewayAmount + ":CurrencyCode:" + txncurrencyName
                            + ":PaymentMode:" + txnpaymentMode + ":ResponseDateTime:" + txntransactionDate
                            + ":BankTransactionNo:" + txnbankTransactionNo + ":TransactionStatus:" + txntransactionStatus);

                    gatewayMoreInfo = value;
                    // gatewayRemarks = txnerrorDescription;
                    gatewayStatus = txntransactionStatus;
                    bookingTransactionID = txnbankTransactionNo;
                    searchParams.setBookingTransactionID(bookingTransactionID);
                    if (pgResponse == SUCCESS) {
                        if (txnorderId.equalsIgnoreCase(rtcRefNo)) {
                            confirmTicket();
                        } else {

                            failedTicket();

                            Toast.makeText(getApplicationContext(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (pgResponse == FAILURE_FLAG) {

                        failedTicket();

                    } else if(pgResponse == DECLINE_FLAG) {

                        failedTicket();
                    }else{
                        failedTicket();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Basic R1NSVENTRzAwMDAwMDAxMDk6dGFrcnZ5dHZneXpudm1ta2Vzbnk=");
                    params.put("content-type", "application/json");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
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
                    ActivityUtil.openNewActivity(NSDL_PaymentCurrentBooking.this, CurrentBookingSuccessActivity.class, extra);
                    finish();

                } else {
                    Log.d("5", "5");
                    takeBackWithError();
                }
            }
            @Override
            public void onError(Exception e) {

                ActivityUtil.showErrorToast(NSDL_PaymentCurrentBooking.this, e);
            }
        });
    }

    /***calling CONFIRM BOOKING RESPONSE PASSING TO REFNO ACTIVITY ****/



    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public static String between(String value, String a, String b) {
        //Return a substring between the two strings
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(b);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }

    public void failedTicket() {

        Intent intent = new Intent(NSDL_PaymentCurrentBooking.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription+" |OB.Ref:"+rtcRefNo);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NSDL_PaymentCurrentBooking.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(NSDL_PaymentCurrentBooking.this, HomeActivity.class);
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