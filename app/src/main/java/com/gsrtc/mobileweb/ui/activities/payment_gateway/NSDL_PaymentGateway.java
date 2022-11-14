package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.billdesk.pgidsk.PGIUtil;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskGateway;
import com.gsrtc.mobileweb.ui.activities.advance_booking.HdfcGatewayActivity;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.ndml.surepay.SurePay;
import com.ndml.surepay.SurePayCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.ndml.surepay.SurePay.activity;

@SuppressLint("JavascriptInterface") public class NSDL_PaymentGateway extends AppCompatActivity {


    private static long checksumval;
    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String txnsuccessFlag = null, txnmessageType = null, txnmerchantID = null, txnserviceId = null, txnorderId = null, txncustomerId = null, txngatewayAmount = null,
            txncurrencyName = null, txnpaymentMode = null, txntransactionDate = null, txntxnId = null, txnbankTransactionNo = null, txntransactionStatus = null, txnadditionalInfo1 = null,
            txnadditionalInfo2 = null, txnadditionalInfo3 = null, txnadditionalInfo4 = null, txnadditionalInfo5 = null, txnerrorStatus = null, txnerrorDescription = null, txncheckSum = null,
            txnFailureFlag = null, txnerrorCode = null, txnerrorMessage = null, txnPendingFlag = null,txnDeclineFlag=null;

    private String gatewayMoreInfo, gatewayRemarks, gatewayStatus, bookingTransactionID;
    private String isCard, getTxntkn;
    String textlang;
    private String checksumMsg = "";
    private Request request;

    Booking onwardBooking, returnBooking;

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
    private String requestDateTime;
    private WebView mWviewPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_s_d_l__payment_gateway);

        Bundle extras = getIntent().getExtras();
        totalFare = extras.getString("totalFare", "");
        isCard = extras.getString("isCard");
        textlang = extras.getString("textlang");
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        }

        totalFare = getIntent().getExtras().getString("totalFare", "");

        if (returnBooking != null) {
            rtcRefNo = returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            returnPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber();
            onwardPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber();

        } else {
            rtcRefNo = onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo();
            returnPNR = "";
            onwardPNR = onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber();
        }
        Log.d("Ticket Parameters", totalFare + "," + onwardPNR + "," + returnPNR);

        customerId = onwardBooking.getPassengerInfo().getPhone();

        pnr = onwardPNR;
        if (!TextUtils.isEmpty(returnPNR))
            pnr += "" + returnPNR;
        Map fields = new HashMap();

        Log.d("pnr", pnr);

        //amount ="2.00";

//  String message =messageType|merchantId|serviceId|orderId|customerId|transactionAmount|currencyCode|requestDateTime|
//  successUrl|failUrl|additionalFeild1| additionalFeild2| additionalFeild3|additionalFeild4| additionalFeild5

        requestDateTime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        checksumMsg = messageType + "|" + merchantId + "|" + serviceId + "|" + rtcRefNo + "|" + customerId + "|" + totalFare + "|" + currency + "|" + requestDateTime + "|" + successUrl + "|" + failUrl + "|||||";

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

        if(!NSDL_PaymentGateway.this.isFinishing())
        {
            CustomisedProgressDialog.SHOW_CUST_DIA(NSDL_PaymentGateway.this, "processing...");
        }
//        try {
//            HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(getApplicationContext()));
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
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
            jsonBody.put("customerId", onwardBooking.getPassengerInfo().getPhone());
            jsonBody.put("transactionAmount", totalFare);
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

//        try {
//            HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(getApplicationContext()));
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
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

                Log.i("PAYGOV REPONSE : ", s);
                getResp();
                CustomisedProgressDialog.STOP_CUST_DIA();

            }
        });

        builder.start(NSDL_PaymentGateway.this);
    }

    public void getResp() {
        if(!NSDL_PaymentGateway.this.isFinishing())
        {
            CustomisedProgressDialog.SHOW_CUST_DIA(NSDL_PaymentGateway.this, "processing...");
        }
//        try {
//            HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(getApplicationContext()));
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
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

                    if (pgResponse == SUCCESS) {
                        if (txnorderId.equalsIgnoreCase(rtcRefNo)) {
                            confirmTicket();
                            CustomisedProgressDialog.STOP_CUST_DIA();
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
                    CustomisedProgressDialog.STOP_CUST_DIA();
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
            CustomisedProgressDialog.STOP_CUST_DIA();
        }
    }

    /***calling CONFIRM TICKET BOOKING METHOD  ****/

    public void confirmTicket() {
        CustomisedProgressDialog.STOP_CUST_DIA();
        if (returnBooking != null) {
            //if(returnBooking.getConfirmRtnSeatBookingAndroidResponse() == null){
            AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(onwardBooking, returnBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnserviceId, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
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

            AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(onwardBooking, txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnserviceId, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
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

    /***calling CONFIRM BOOKING RESPONSE PASSING TO REFNO ACTIVITY ****/

    public void takeBack() {
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
        if (returnBooking != null) {
            resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

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
        CustomisedProgressDialog.STOP_CUST_DIA();
        Intent intent = new Intent(NSDL_PaymentGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription+" |OB.Ref:"+rtcRefNo);
        startActivity(intent);
        finish();
    }

//SSL CERTIFICATE ADDING
//    public SSLSocketFactory getSocketFactory(Context context)
//            throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//
//        // Load CAs from an InputStream (could be from a resource or ByteArrayInputStream or ...)
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//        InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.surpay_ssl_certificate));
//        // I paste my myFile.crt in raw folder under res.
//        Certificate ca;
//
//        //noinspection TryFinallyCanBeTryWithResources
//        try {
//            ca = cf.generateCertificate(caInput);
//            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//        } finally {
//            caInput.close();
//        }
//
//        // Create a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//        // Create a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//        // Create an SSLContext that uses our TrustManager
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, tmf.getTrustManagers(), null);
//
//        return sslContext.getSocketFactory();
//    }

    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NSDL_PaymentGateway.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(NSDL_PaymentGateway.this, HomeActivity.class);
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