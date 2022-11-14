package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CCAvenueWebview extends AppCompatActivity {

    Intent mainIntent;
    String encVal;
    String vResponse;

    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String gatewayAmount, gatewayRemarks, gatewayMoreInfo, transactionDate;
    private String order_id,tracking_id,bank_ref_no,order_status,amount,card_name,response_code;
    private String authStatus, bankId, bankMerchantId, bankRefNo, bookingTransactionID, hdfcBankRespURL;
    private String isCard;
    String textlang;

    Booking onwardBooking, returnBooking;

    String merchantID="221012";
    String accessCode="AVIE85GF73AF41EIFA";
    String currency="INR";
    String returnURL="https://www.gsrtc.in/GSRTCPG/androidCCAvenuePayment.do";
    String cancelURL="https://www.gsrtc.in/GSRTCPG/androidCCAvenuePayment.do";
    String rsakeyURL="https://secure.ccavenue.com/transaction/getRSAKey";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ccavenue_webview);
        mainIntent = getIntent();

        Bundle extras = getIntent().getExtras();
        totalFare = extras.getString("totalFare", "");
        //totalFare = "2.00";
        isCard = extras.getString("isCard");
        textlang = extras.getString("textlang");
        //encVal = extras.getString("enc_data");
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        }

        totalFare  = getIntent().getExtras().getString("totalFare","");


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

        pnr = onwardPNR;
        if (!TextUtils.isEmpty(returnPNR))
            pnr += "" + returnPNR;
        Map fields = new HashMap();

        Log.d("pnr", pnr);

       // showwebview();

//get rsa key method
        get_RSA_key(accessCode,rtcRefNo);
    }



private class RenderView extends AsyncTask<Void, Void, Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        LoadingDialog.showLoadingDialog(CCAvenueWebview.this, "Loading...");
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        if (!ServiceUtility.chkNull(vResponse).equals("")
                && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
            StringBuffer vEncVal = new StringBuffer("");
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, totalFare));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, currency));
            encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
        }

        return null;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        LoadingDialog.cancelLoading();

        @SuppressWarnings("unused")
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processHTML(final String html) {
                // process the html source code to get final status of transaction

                Log.e("HTML", " ======>  HTML Data : "+  html);
                try {
                    if (html.contains("<form name=\"myform\" method=\"post\">")) {
                        Log.d("My page", " ======>  My page : " + html);

                        Document doc = Jsoup.parse(html);

                        // Response Code
                        Element msg1 = doc.select("input[name*=ccMsg").first();
                        Log.d("msg1", msg1.toString());
                        String ss1 = msg1.toString(); // between(msg.toString(), "value=", ">");
                        String responseCode = ss1.substring(ss1.indexOf("value=") + 7, ss1.indexOf(">"));

                        String[] strData = responseCode.split(",");

                        if (strData.length == 7) {
                            order_id = strData[0];
                            tracking_id = strData[1];
                            bank_ref_no = strData[2];
                            order_status = strData[3];
                            amount = strData[4];
                            card_name = strData[5];
                            response_code = strData[6];
                            if(order_status.equalsIgnoreCase("Success")) {
                                if(order_id.equalsIgnoreCase(rtcRefNo)) {
                                    confirmTicktet();
                                }else{
                                    String payment_status = "Transaction Cancelled!";
                                    paymentCancel(payment_status);

                                    Toast.makeText(getApplicationContext(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                String payment_status = null;

                                if (order_status.equalsIgnoreCase("Failure")) {
                                    payment_status = "Transaction Declined!";
                                } else if (order_status.equalsIgnoreCase("Aborted")) {
                                    payment_status = "Transaction Cancelled!";
                                } else {
                                    payment_status = "Status Not Known!";
                                }
                                paymentCancel(payment_status);
                                Toast.makeText(getApplicationContext(), payment_status, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String payment_status = "Transaction Cancelled!";
                            paymentCancel(payment_status);
                            Log.d("response code", "showHTML: failed" + responseCode);
                            Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
                            //takeBackWithError();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("4", "4");
                    //takeBackWithError();
                    Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
                    // takeBackWithError();
                }
            }
        }

        final WebView mCCwebview = (WebView) findViewById(R.id.webview);
        mCCwebview.clearCache(true);
        mCCwebview.getSettings().setJavaScriptEnabled(true);
        mCCwebview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        mCCwebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingDialog.cancelLoading();
               if (url.indexOf("/androidCCAvenuePayment.do") != -1) {
                mCCwebview.loadUrl("javascript:window.HTMLOUT.processHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
               }
            }

            @Override
            public void onPageStarted(WebView paramAnonymousWebView, final String url, Bitmap favicon) {
                super.onPageStarted(paramAnonymousWebView, url, favicon);
                LoadingDialog.showLoadingDialog(CCAvenueWebview.this, "Loading...");
            }
        });

        try {
            String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(accessCode, "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(merchantID, "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(rtcRefNo, "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(returnURL, "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(cancelURL, "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
            mCCwebview.postUrl(Constants.TRANS_URL, postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(CCAvenueWebview.this, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, rsakeyURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();

                        if (response != null && !response.equals("")) {
                            vResponse = response;     ///save retrived rsa key
                            if (vResponse.contains("!ERROR!")) {
                                show_alert(vResponse);
                            } else {
                                new RenderView().execute();   // Calling async task to get display content
                            }
                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                CCAvenueWebview.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }


    public  void paymentCancel(String errormsg) {

        //PaymentFailure();
        Intent intent = new Intent(CCAvenueWebview.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", errormsg);
        startActivity(intent);
        finish();
    }

    public void confirmTicktet() {

        if (returnBooking != null) {
            //if(returnBooking.getConfirmRtnSeatBookingAndroidResponse() == null){
            AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(onwardBooking, returnBooking, order_status, bankId, bankMerchantId, bank_ref_no, tracking_id, amount, gatewayRemarks, gatewayMoreInfo, transactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
                @Override
                public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                    if (response != null) {
                        onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                        returnBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                        takeBack();
//                            onBookingConfirmed();
                    } else {
//                            hideProgressDialog();
                        Log.d("5", "5");
                        takeBackWithError();
                    }
                }

                @Override
                public void onError(Exception e) {
//                        hideProgressDialog();
//                        ActivityUtil.showErrorToast(ReferenceNumberActivity.this, e);
                    Log.d("6", "6");
                    takeBackWithError();
                }
            });

        } else {

            AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(onwardBooking, order_status, bankId, bankMerchantId, bank_ref_no, tracking_id, amount, gatewayRemarks, gatewayMoreInfo, transactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
                @Override
                public void onSuccess(ConfirmAdvSeatBookingAndroidResponse response) {
                    if (response != null) {
                        onwardBooking.setConfirmAdvSeatBookingAndroidResponse(response);

                        takeBack();
                        //pay(); //To check for return booking
                    } else {
//                        hideProgressDialog();
                        Log.d("7", "7");
                        takeBackWithError();
                    }
                }

                @Override
                public void onError(Exception e) {
//                    hideProgressDialog();
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

    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                CCAvenueWebview.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ActivityUtil.openNewActivityAsOnlyActivity(HdfcGatewayActivity.this, HomeActivity.class, null);
                Intent i = new Intent(CCAvenueWebview.this, HomeActivity.class);
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

//    public void showwebview(){
//
//        LoadingDialog.cancelLoading();
//
//        @SuppressWarnings("unused")
//        class MyJavaScriptInterface {
//            @JavascriptInterface
//            public void processHTML(final String html) {
//                // process the html source code to get final status of transaction
//
//                Log.e("HTML", " ======>  HTML Data : "+  html);
//                try {
//                    if (html.contains("<form name=\"myform\" method=\"post\">")) {
//                        Log.d("My page", " ======>  My page : " + html);
//
//                        Document doc = Jsoup.parse(html);
//
//                        // Response Code
//                        Element msg1 = doc.select("input[name*=ccMsg").first();
//                        Log.d("msg1", msg1.toString());
//                        String ss1 = msg1.toString(); // between(msg.toString(), "value=", ">");
//                        String responseCode = ss1.substring(ss1.indexOf("value=") + 7, ss1.indexOf(">"));
//
//                        String[] strData = responseCode.split(",");
//
//                        if (strData.length == 7) {
//                            order_id = strData[0];
//                            tracking_id = strData[1];
//                            bank_ref_no = strData[2];
//                            order_status = strData[3];
//                            amount = strData[4];
//                            card_name = strData[5];
//                            response_code = strData[6];
//                            if(order_status.equalsIgnoreCase("Success")) {
//                                if(order_id.equalsIgnoreCase(rtcRefNo)) {
//                                    confirmTicktet();
//                                }else{
//                                    String payment_status = "Transaction Cancelled!";
//                                    paymentCancel(payment_status);
//
//                                    Toast.makeText(getApplicationContext(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                String payment_status = null;
//
//                                if (order_status.equalsIgnoreCase("Failure")) {
//                                    payment_status = "Transaction Declined!";
//                                } else if (order_status.equalsIgnoreCase("Aborted")) {
//                                    payment_status = "Transaction Cancelled!";
//                                } else {
//                                    payment_status = "Status Not Known!";
//                                }
//                                paymentCancel(payment_status);
//                                Toast.makeText(getApplicationContext(), payment_status, Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            String payment_status = "Transaction Cancelled!";
//                            paymentCancel(payment_status);
//                            Log.d("response code", "showHTML: failed" + responseCode);
//                            Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
//                            //takeBackWithError();
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("4", "4");
//                    //takeBackWithError();
//                    Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
//                    // takeBackWithError();
//                }
//            }
//        }
//
//        final WebView mCCwebview = (WebView) findViewById(R.id.webview);
//        mCCwebview.clearCache(true);
//        mCCwebview.getSettings().setJavaScriptEnabled(true);
//        mCCwebview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
//        mCCwebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                LoadingDialog.cancelLoading();
//                if (url.indexOf("/androidCCAvenuePayment.do") != -1) {
//                    mCCwebview.loadUrl("javascript:window.HTMLOUT.processHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
//                }
//            }
//
//            @Override
//            public void onPageStarted(WebView paramAnonymousWebView, final String url, Bitmap favicon) {
//                super.onPageStarted(paramAnonymousWebView, url, favicon);
//                LoadingDialog.showLoadingDialog(CCAvenueWebview.this, "Loading...");
//            }
//        });
//
//        try {
//            String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(accessCode, "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(merchantID, "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(rtcRefNo, "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(returnURL, "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(cancelURL, "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
//            mCCwebview.postUrl(Constants.TRANS_URL, postData.getBytes());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
}