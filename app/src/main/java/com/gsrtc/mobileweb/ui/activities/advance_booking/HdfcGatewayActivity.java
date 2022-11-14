package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.security.MessageDigest;
import java.math.*;
import java.util.*;

public class HdfcGatewayActivity extends AppCompatActivity {

    String SECURE_SECRET = "a74d72ff59dbac6079efdf3a0a78b1b8"; // your secret key;
    String md5HashData = SECURE_SECRET;

    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String gatewayAmount, gatewayRemarks, gatewayMoreInfo, transactionDate;

    private String authStatus, bankId, bankMerchantId, bankRefNo, bookingTransactionID, hdfcBankRespURL;

    private String isCard;
    String textlang;
    Booking onwardBooking, returnBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_bus_web_view);

        String encValue = "";
        hdfcBankRespURL = "https://gsrtc.in/gmobile/androidHDFCPayment.do";

        Bundle extras = getIntent().getExtras();
        totalFare = extras.getString("totalFare", "");
        textlang = extras.getString("textlang");
//        totalFare = "2";
        isCard = extras.getString("isCard");
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        }

        //STAGING ENVIRONMENT

        //totalFare  = getIntent().getExtras().getString("totalFare","");

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

        fields.put("channel", "10");
        fields.put("account_id", "15265");
        fields.put("reference_no", rtcRefNo);
        fields.put("mode", "LIVE");
        fields.put("currency", "INR");
        fields.put("description", pnr);
        fields.put("return_url", hdfcBankRespURL);
        fields.put("name", "GSRTC");
        fields.put("address", "Naroda");
        fields.put("city", "Ahmedabad");
        fields.put("state", "Gujarat");
        fields.put("country", "IND");
        fields.put("postal_code", "380001");
        fields.put("phone", "1234567890");
        fields.put("email", "feedback@gsrtc.in");
        fields.put("ship_name", "GSRTC");
        fields.put("ship_address", "Naroda");
        fields.put("ship_city", "Ahmedabad");
        fields.put("ship_state", "Gujarat");
        fields.put("ship_country", "IND");
        fields.put("ship_postal_code", "380001");
        fields.put("ship_phone", "1234567890");
        fields.put("amount", totalFare);
        //fields.put("amount", "2.00");

        Map requestFields = new TreeMap(fields);

        for (Iterator i = requestFields.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) requestFields.get(key);
            md5HashData += "|" + value;
        }
        String hvalue = "MD5";
        String hashedvalue = "";

        if (hvalue.equals("MD5")) {
            try {
                hashedvalue = md5(md5HashData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();

        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webSettings.setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        String strHTML = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title></title></head><body>\"" +
                "<form id=\"order\" action=\"https://secure.ebs.in/pg/ma/payment/request\" method=\"post\">\"" +
                "<input type=\"hidden\" name=\"channel\" value=\"" + fields.get("channel") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"account_id\" value=\"" + fields.get("account_id") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"reference_no\" value=\"" + fields.get("reference_no") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"mode\" value=\"" + fields.get("mode") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"currency\" value=\"" + fields.get("currency") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"description\" value=\"" + fields.get("description") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"return_url\" value=\"" + fields.get("return_url") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"name\" value=\"" + fields.get("name") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"address\" value=\"" + fields.get("address") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"city\" value=\"" + fields.get("city") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"state\" value=\"" + fields.get("state") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"country\" value=\"" + fields.get("country") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"postal_code\" value=\"" + fields.get("postal_code") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"phone\" value=\"" + fields.get("phone") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"email\" value=\"" + fields.get("email") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_name\" value=\"" + fields.get("ship_name") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_address\" value=\"" + fields.get("ship_address") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_city\" value=\"" + fields.get("ship_city") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_state\" value=\"" + fields.get("ship_state") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_country\" value=\"" + fields.get("ship_country") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_postal_code\" value=\"" + fields.get("ship_postal_code") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"ship_phone\" value=\"" + fields.get("ship_phone") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"amount\" value=\"" + fields.get("amount") + "\"><br>\"" +
                "<input type=\"hidden\" name=\"secure_hash\" value=\"" + hashedvalue + "\"><br>\"" +
                "</form><script>document.getElementById(\"order\").submit();</script></body></html>";

        myWebView.loadData(strHTML, "text/html", null);

    }


    public class MyJavaScriptInterface {

        @JavascriptInterface
        public void showHTML(String html) {
            //Log.e("HTML", " ======>  HTML Data : "+  html);
            try {
                if (html.contains("<form name=\"myform\"")) {
                    Log.d("My page", " ======>  My page : " + html);

                    Document doc = Jsoup.parse(html);

                    // Response Code
                    Element msg1 = doc.select("input[name*=msg").first();
                    Log.d("msg1", msg1.toString());
                    String ss1 = msg1.toString(); // between(msg.toString(), "value=", ">");
                    String responseCode = ss1.substring(ss1.indexOf("value=") + 7, ss1.indexOf(">"));

                    String[] strData = responseCode.split(",");

                    if (strData.length == 5) {
                        bankRefNo = strData[0];
                        authStatus = strData[1];
                        gatewayAmount = strData[2];
                        bankId = strData[3];
                        bookingTransactionID = strData[4];
                        bookingTransactionID = bookingTransactionID.substring(0, bookingTransactionID.length() - 1);
                        pay();
                    } else {
                        Log.d("response code", "showHTML: failed" + responseCode);
                        Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
                        takeBackWithError();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("4", "4");
                //takeBackWithError();
                Toast.makeText(getApplicationContext(), "Transaction Failure", Toast.LENGTH_SHORT).show();
                takeBackWithError();
            }

        }

        /***calling CONFIRM TICKET BOOKING METHOD  ****/

        public void pay() {

            if (returnBooking != null) {
                //if(returnBooking.getConfirmRtnSeatBookingAndroidResponse() == null){
                AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(onwardBooking, returnBooking, authStatus, bankId, bankMerchantId, bankRefNo, bookingTransactionID, gatewayAmount, gatewayRemarks, gatewayMoreInfo, transactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
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

                AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(onwardBooking, authStatus, bankId, bankMerchantId, bankRefNo, bookingTransactionID, gatewayAmount, gatewayRemarks, gatewayMoreInfo, transactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
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

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            view.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("request.toString()", request.toString());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.d("URL", url);
            super.onLoadResource(view, url);
        }
    }

    private String md5(String str) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = str.getBytes();
        m.update(data, 0, data.length);
        BigInteger i = new BigInteger(1, m.digest());
        String hash = String.format("%1$032X", i);
        return hash;
    }

    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                HdfcGatewayActivity.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ActivityUtil.openNewActivityAsOnlyActivity(HdfcGatewayActivity.this, HomeActivity.class, null);
                Intent i = new Intent(HdfcGatewayActivity.this, HomeActivity.class);
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
