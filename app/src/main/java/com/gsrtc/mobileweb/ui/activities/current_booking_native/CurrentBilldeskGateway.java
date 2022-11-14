package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.billdesk.pgidsk.PGIUtil;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;

import org.jsoup.Jsoup;

import java.util.StringTokenizer;

@SuppressLint("JavascriptInterface") public class CurrentBilldeskGateway extends AppCompatActivity {


    private String merchantID, customerID, txnReferenceNo,bankRefNo, txnAmount, bankId, bankMerchantId,
            txnType, currencyName, itemCode, securityType, securityID, securityPassword, txnDate, authStatus,
            settlementType, additionalInfo1, additionalInfo2, additionalInfo3, additionalInfo4, additionalInfo5,
            additionalInfo6, additionalInfo7, errorStatus, errorDescription, checkSum,gatewayAmount, transactionDate,getGatewayLookupId;

    private String gatewayMoreInfo,gatewayRemarks,gatewayStatus,bookingTransactionID;

    private WebView mWviewPay;
    private boolean mGotHash = false;
    private String mProductInfo;
    String textlang;

    //Payment Parameters
    String strHash = "";
    String billDeskMessage="";

    /**testing  **/

    //String returnURL = "https://gsrtc.in/gmobile/androidPayment.do";

    /** production  **/

    String returnURL = "https://gsrtc.in/gmobile/androidPayment.do";

    String dataSeparator = "|";
    String merchantId = "GSRTC";
    String customerId = "ARP10234"; //ob number
    String notAvailable = "NA";
    String amount = "2.00"; // ticket amount
    String currencyType = "INR";
    String typeField1 = "R";
    String securityId = "gsrtc";
    String typeField2 = "F";
    String pnrNo = "T8007496";
    String transactionType = "mbooking";
    String mobileUserId = "0";
    String journyDate = "";
    String paymentURL = "https://www.billdesk.com/pgidsk/PGIMerchantPayment";
    String finalbillDeskMessage = "";
    String gatewayLookupId = "175";

    /******Production credentials*****/

    String gatewayFullInfo = "merchantID="+merchantID+"&customerID="+customerID+"&txnReferenceNo="+txnReferenceNo+"&bankReferenceNo="+bankRefNo+"&txnAmount="+gatewayAmount+"&bankID="+bankId+"&bankMerchantID="+bankMerchantId+
            "&txnType="+txnType+"&currencyName="+currencyName+"&itemCode="+itemCode+"&securityType="+securityType+"&securityID="+securityID+
            "&securityPassword="+securityPassword+"&txnDate="+transactionDate+"&authStatus="+authStatus+"&settlementType="+settlementType+"&additionalInfo1="+additionalInfo1+
            "&additionalInfo2="+additionalInfo2+"&additionalInfo3="+additionalInfo3+"&additionalInfo4="+additionalInfo4+"&additionalInfo5="+additionalInfo5+
            "&additionalInfo6="+additionalInfo6+"&additionalInfo7="+additionalInfo7+"&errorStatus="+errorStatus+"&errorDescription="+errorDescription+"&checkSum="+checkSum;


    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr,paymentMode;

    private String isCard;
    SearchParamsCurrent searchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_billdesk_gateway);

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang = searchParams.getCrntlanguage().trim();
        }

        amount = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
        customerId = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
        pnrNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
        journyDate =searchParams.getJourneyDate_GetCurrentTotalFareDetailsOfTicket();
        isCard = searchParams.getGatewayLookupId();
        Log.e("Paymode", isCard);

//        if (paymentMode.equals("HDFC"))
//            isCard = false;
//        else
//            isCard = true;



        mWviewPay = (WebView) findViewById(R.id.xWviewPay);

        mWviewPay.getSettings().setJavaScriptEnabled(true);
        mWviewPay.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        mWviewPay.setWebChromeClient(new WebChromeClient());

        mWviewPay.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWviewPay.loadUrl("javascript:window.HTMLOUT.mInterCept" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            }

            public void onPageStarted(WebView paramAnonymousWebView, final String paramAnonymousString, Bitmap paramAnonymousBitmap) {
                super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);

            } // END onPageStarted()

        });

        mWviewPay.loadDataWithBaseURL(null, mDefineValues(), "text/html", "UTF-8", null);

    }

    @SuppressLint("LongLogTag")
    private String mDefineValues()
    {
        billDeskMessage = merchantId+"|"+customerId+"|NA|"+amount+"|NA|NA|NA|"+currencyType+"|NA|"+typeField1+"|"+securityId+"|NA|NA|"+typeField2+"|"+pnrNo+"|"+transactionType+"|NA|"+mobileUserId+"|"+journyDate+"|NA|NA|"+returnURL;
        strHash = PGIUtil.doDigest(billDeskMessage, "kLQ24kvIUhVM");

        Log.e("Do Digest Message : ", strHash);

        finalbillDeskMessage = billDeskMessage + "|" +strHash;

        Log.e("Final Message Define Values : ", finalbillDeskMessage);


        String str1 = "<form id = 'billdesk_submit_form' action = "+"'"+paymentURL+"'"+ "method='POST'>";
        str1 = str1 + "<input type='hidden' name='msg' value='" + finalbillDeskMessage + "'/>";

        String str2 = str1 + "</form>";
        String str3= "<html><head></head><body onload=\"document.getElementById('billdesk_submit_form').submit();\"><h1>Loading ... </h1>" + str2 + "</body>" + "</html>";

        return str3;
    } // mDefineValues()

    class MyJavaScriptInterface {

        @JavascriptInterface
        public void mInterCept(final String html) {


            if(html.contains("<form name=\"MobileBookingHistoryForm\" method=\"post\">")){

                System.out.println("here" + html);

                runOnUiThread(new Runnable() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        org.jsoup.nodes.Document doc = Jsoup.parse(html);

                        org.jsoup.nodes.Element msg = doc.select("input[name*=" + "msg").first();

                        String ss = between(msg.toString(), "value=", ">");

                        String decryptableData = ss.substring(1, ss.length()-1);

                        //**decryptableData = PGIUtil.doDigest(arg0, arg1)

                        Log.e("Decryptable Data Jaffa --- : ", decryptableData);

                        String hashData = decryptableData.substring(decryptableData.lastIndexOf("|") + 1).trim();

                        Log.e("Hash Value Jaffa --- : ", hashData);


                        StringTokenizer stringTokenizer = new StringTokenizer(decryptableData, "|");
                        while (stringTokenizer.hasMoreTokens()) {

                            merchantID = stringTokenizer.nextToken();
                            customerId = stringTokenizer.nextToken();
                            txnReferenceNo = stringTokenizer.nextToken();
                            bankRefNo = stringTokenizer.nextToken();
                            gatewayAmount = stringTokenizer.nextToken();
                            bankId = stringTokenizer.nextToken();
                            bankMerchantId = stringTokenizer.nextToken();
                            txnType = stringTokenizer.nextToken();
                            currencyName = stringTokenizer.nextToken();
                            itemCode = stringTokenizer.nextToken();
                            securityType = stringTokenizer.nextToken();
                            securityID = stringTokenizer.nextToken();
                            securityPassword = stringTokenizer.nextToken();
                            transactionDate = stringTokenizer.nextToken();
                            authStatus = stringTokenizer.nextToken();
                            settlementType = stringTokenizer.nextToken();
                            additionalInfo1 = stringTokenizer.nextToken();
                            additionalInfo2 = stringTokenizer.nextToken();
                            additionalInfo3 = stringTokenizer.nextToken();
                            additionalInfo4 = stringTokenizer.nextToken();
                            additionalInfo5 = stringTokenizer.nextToken();
                            additionalInfo6 = stringTokenizer.nextToken();
                            additionalInfo7 = stringTokenizer.nextToken();
                            errorStatus = stringTokenizer.nextToken();
                            errorDescription = stringTokenizer.nextToken();
                            checkSum = stringTokenizer.nextToken();

                            gatewayMoreInfo = bankMerchantId;
                            gatewayRemarks = errorDescription;
                            gatewayStatus = authStatus;
                            bookingTransactionID = txnReferenceNo;
                            searchParams.setBookingTransactionID(bookingTransactionID);

                            if (!authStatus.equals("0300") ) {

                                Cancel();

                            } else {

                                confirm();

                            }
                        }
                    }
                });
            }
        }
    }

    public  void confirm() {

        pay();
    }

    public  void Cancel() {

        //PaymentFailure();
        Intent intent = new Intent(CurrentBilldeskGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", errorDescription);
        startActivity(intent);
        finish();
    }

    public void pay() {

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
                    ActivityUtil.openNewActivity(CurrentBilldeskGateway.this, CurrentBookingSuccessActivity.class, extra);
                    finish();

                } else {
                    Log.d("5", "5");
                    takeBackWithError();
                }
            }
            @Override
            public void onError(Exception e) {

                ActivityUtil.showErrorToast(CurrentBilldeskGateway.this, e);
            }
        });
    }


    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                CurrentBilldeskGateway.this,R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ActivityUtil.openNewActivityAsOnlyActivity(CurrentBilldeskGateway.this, HomeActivity.class, null);
                Intent i = new Intent(CurrentBilldeskGateway.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", null);

        alertDialog.setMessage("Going back will discard your booking. Do you want to continue?");
        alertDialog.setTitle("GSRTC Payment");
        alertDialog.show();
    }


    public static String between(String value, String a, String b){
        //Return a substring between the two strings
        int posA = value.indexOf(a);
        if(posA == -1){
            return "";
        }
        int posB = value.lastIndexOf(b);
        if(posB == -1){
            return "";
        }
        int adjustedPosA = posA + a.length();
        if(adjustedPosA >= posB){
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }
}
