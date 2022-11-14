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

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.utils.AppConstants;
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

public class AggreepayGateway extends AppCompatActivity {


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
    TextView transactionIdView;
    TextView transactionStatusView;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggreepay_gateway);
        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        totalFare = extras.getString("totalFare", "");
        isCard = extras.getString("isCard");
        textlang = extras.getString("textlang");
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


        showpaymentpage();

    }

    private void showpaymentpage() {

        //amount ="2.00";

            pb.setVisibility(View.VISIBLE);

            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            Constants.PG_ORDER_ID="Test"+Integer.toString(n);

            PaymentParams pgPaymentParams = new PaymentParams();
            pgPaymentParams.setAPiKey(Constants.PG_API_KEY);
            pgPaymentParams.setAmount(totalFare);
            pgPaymentParams.setEmail(onwardBooking.getPassengerInfo().getEmail());
            pgPaymentParams.setName(onwardBooking.getPassengers().get(0).getPassengerNameA());
            pgPaymentParams.setPhone(onwardBooking.getPassengerInfo().getPhone());
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
            //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");

            PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, AggreepayGateway.this);
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

            AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(onwardBooking,txntransactionStatus, txnsuccessFlag, txntxnId, txnbankTransactionNo, bookingTransactionID, txngatewayAmount, txnserviceId, gatewayMoreInfo, txntransactionDate, isCard, new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
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


    public  void failedTicket() {

        Intent intent = new Intent(AggreepayGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", txnerrorDescription+" |OB.Ref:"+rtcRefNo);
        startActivity(intent);
        finish();
    }


    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AggreepayGateway.this, R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ActivityUtil.openNewActivityAsOnlyActivity(HdfcGatewayActivity.this, HomeActivity.class, null);
                Intent i = new Intent(AggreepayGateway.this, HomeActivity.class);
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