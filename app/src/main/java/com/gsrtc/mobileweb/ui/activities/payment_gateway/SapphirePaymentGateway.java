package com.gsrtc.mobileweb.ui.activities.payment_gateway;

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

import androidx.appcompat.app.AppCompatActivity;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import com.payphi.customersdk.views.Application;
import com.payphi.customersdk.views.PayPhiSdk;
import com.payphi.customersdk.views.PaymentOptionsActivity;

import java.util.Locale;
import java.util.Map;

public class SapphirePaymentGateway extends AppCompatActivity {
    private static final String TAG = "SapphirePG";

    private ProgressBar progressBar;
    private TextView tvStatus;

    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnr;
    private String isCard, textlang;

    private String txnsuccessFlag = null;
    private String txnmessageType = null;
    private String txnmerchantID = null;
    private String txnserviceId = null;
    private String txnorderId = null;
    private String txncustomerId = null;
    private String txngatewayAmount = null;
    private String txncurrencyName = null;
    private String txnpaymentMode = null;
    private String txntransactionDate = null;
    private String txntxnId = null;
    private String txnbankTransactionNo = null;
    private String txntransactionStatus = null;
    private String txnerrorDescription = null;

    private String gatewayMoreInfo;
    private String gatewayStatus;
    private String bookingTransactionID;

    private Booking onwardBooking, returnBooking, divyangBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sapphire_payment_gateway);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalFare = extras.getString("totalFare", "");
            isCard = extras.getString("isCard");
            textlang = extras.getString("textlang");
            onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
                returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
            }
            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG)) {
                divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
            }
        }

        if (returnBooking != null && returnBooking.getSaveReturnBookingDetailsTemporary() != null) {
            rtcRefNo = returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            returnPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber();
            onwardPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber();
        } else if (divyangBooking != null && divyangBooking.getSaveReturnBookingDetailsTemporary() != null) {
            rtcRefNo = divyangBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            returnPNR = divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber();
            onwardPNR = onwardBooking != null && onwardBooking.getSaveBookingDetailsTemporary() != null ?
                    onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber() : "";
        } else if (onwardBooking != null && onwardBooking.getSaveBookingDetailsTemporary() != null) {
            rtcRefNo = onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo();
            returnPNR = "";
            onwardPNR = onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber();
        } else {
            rtcRefNo = "" + System.currentTimeMillis();
            onwardPNR = "";
            returnPNR = "";
        }

        pnr = onwardPNR;
        if (!TextUtils.isEmpty(returnPNR)) {
            pnr += returnPNR;
        }

        Log.d(TAG, "Booking parameters - TotalFare: " + totalFare + ", Ref: " + rtcRefNo + ", PNR: " + pnr);

        initiateSapphireSdk();
    }

    private void initiateSapphireSdk() {
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Initializing payment gateway...");

        Application.setEnv(SapphireConstants.STARTING_ENV);

        String mIdToUse = !TextUtils.isEmpty(SapphireConstants.AGGREGATOR_ID) ?
                SapphireConstants.AGGREGATOR_ID : SapphireConstants.MERCHANT_ID;

        Application.setAppInfo(
                mIdToUse,
                SapphireConstants.APP_ID,
                getApplicationContext(),
                new Application.IAppInitializationListener() {
                    @Override
                    public void onFailure(final String errorCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                String msg = "Initialization failed (Error: " + errorCode + ")";
                                Log.e(TAG, msg);
                                Toast.makeText(SapphirePaymentGateway.this, msg, Toast.LENGTH_LONG).show();
                                txnerrorDescription = msg;
                                failedTicket();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String status) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Application.setMerchantName(SapphireConstants.MERCHANT_NAME, getApplicationContext());
                                makePayment();
                            }
                        });
                    }
                }
        );
    }

    private void makePayment() {
        if (TextUtils.isEmpty(totalFare)) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        float amountFloat;
        try {
            amountFloat = Float.parseFloat(totalFare.trim());
        } catch (NumberFormatException e) {
            amountFloat = 1.0f;
        }
        final String formattedAmt = String.format(Locale.US, "%.2f", amountFloat);
        final String orderId = !TextUtils.isEmpty(rtcRefNo) ? rtcRefNo : ("GSRTC" + System.currentTimeMillis());

        String email = "customer@gsrtc.in";
        if (onwardBooking != null && onwardBooking.getPassengerInfo() != null && !TextUtils.isEmpty(onwardBooking.getPassengerInfo().getEmail())) {
            email = onwardBooking.getPassengerInfo().getEmail();
        }

        String secureToken = SapphireConstants.generateSecureToken(
                formattedAmt,
                SapphireConstants.MERCHANT_ID,
                orderId,
                SapphireConstants.SECRET_KEY
        );

        tvStatus.setText("Opening payment options...");

        Intent intent = new Intent(this, PaymentOptionsActivity.class);
        intent.putExtra("SecureToken", secureToken);
        intent.putExtra("MerchantID", SapphireConstants.MERCHANT_ID);
        intent.putExtra("aggregatorID", SapphireConstants.AGGREGATOR_ID);
        intent.putExtra("Amount", formattedAmt);
        intent.putExtra("MerchantTxnNo", orderId);
        intent.putExtra("invoiceNo", orderId);
        intent.putExtra("CurrencyCode", SapphireConstants.CURRENCY_CODE);
        intent.putExtra("CustomerEmailID", email);
        intent.putExtra("addlParam1", rtcRefNo);
        intent.putExtra("apiVersion", SapphireConstants.API_VERSION);

        PayPhiSdk.makePayment(
                this,
                intent,
                PayPhiSdk.DIALOG,
                new PayPhiSdk.IAppPaymentResponseListenerEx() {
                    @Override
                    public void onPaymentResponse1(int resultCode, Intent data) {
                        // Handled in onPaymentResponse
                    }

                    @Override
                    public void onPaymentResponse(final int resultCode, final Intent data, final Map<String, String> additionalInfo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handlePaymentResponse(resultCode, data);
                            }
                        });
                    }
                }
        );
    }

    private void handlePaymentResponse(int resultCode, Intent data) {
        Log.d(TAG, "handlePaymentResponse resultCode: " + resultCode);

        if (resultCode == RESULT_CANCELED) {
            progressBar.setVisibility(View.GONE);
            txnerrorDescription = "Transaction Canceled";
            failedTicket();
            return;
        }

        Bundle bundle = (data != null) ? data.getExtras() : null;
        if (bundle == null) {
            progressBar.setVisibility(View.GONE);
            txnerrorDescription = "No payment response data received";
            failedTicket();
            return;
        }

        for (String key : bundle.keySet()) {
            Log.d(TAG, "PayPhi Response Key: " + key + " = " + bundle.get(key));
        }

        String responseType = bundle.getString("respType");
        String statusKeyToVerify = "checkstatus".equalsIgnoreCase(responseType) ? "txnResponseCode" : "responseCode";
        String responseCode = bundle.getString(statusKeyToVerify);
        String paymentDateTime = bundle.getString("paymentDateTime");
        String merchantTxnNo = bundle.getString("merchantTxnNo");
        String txnAuthID = bundle.getString("txnAuthID");

        if (SapphireConstants.SUCCESS_STATUS_VALUE.equals(responseCode) || "0000".equals(responseCode) || "000".equals(responseCode) || "00".equals(responseCode)) {
            txntransactionStatus = "TXN_SUCCESS";
            txnsuccessFlag = "00";
            txntxnId = !TextUtils.isEmpty(txnAuthID) ? txnAuthID : merchantTxnNo;
            txnbankTransactionNo = !TextUtils.isEmpty(txnAuthID) ? txnAuthID : merchantTxnNo;
            txngatewayAmount = totalFare;
            txnpaymentMode = SapphireConstants.GATEWAY_LOOKUP_VALUE;
            txncurrencyName = "INR";
            txntransactionDate = !TextUtils.isEmpty(paymentDateTime) ? paymentDateTime : "";
            bookingTransactionID = txntxnId;
            gatewayMoreInfo = bundle.toString();
            gatewayStatus = txntransactionStatus;

            tvStatus.setText("Payment successful! Confirming ticket...");
            confirmTicket();
        } else {
            progressBar.setVisibility(View.GONE);
            txnerrorDescription = "Payment Failed (Code: " + responseCode + ")";
            Toast.makeText(this, txnerrorDescription, Toast.LENGTH_SHORT).show();
            failedTicket();
        }
    }

    public void confirmTicket() {
        if (onwardBooking != null && onwardBooking.getSearchParams() != null &&
                onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper") &&
                divyangBooking != null) {

            AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroid(
                    onwardBooking,
                    divyangBooking,
                    txntransactionStatus,
                    txnsuccessFlag,
                    txntxnId,
                    txnbankTransactionNo,
                    bookingTransactionID,
                    txngatewayAmount,
                    txnpaymentMode,
                    gatewayMoreInfo,
                    txntransactionDate,
                    isCard,
                    new AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                divyangBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                takeBack();
                            } else {
                                takeBackWithError();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Divyang confirm error", e);
                            takeBackWithError();
                        }
                    }
            );
        } else if (returnBooking != null) {
            AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(
                    onwardBooking,
                    returnBooking,
                    txntransactionStatus,
                    txnsuccessFlag,
                    txntxnId,
                    txnbankTransactionNo,
                    bookingTransactionID,
                    txngatewayAmount,
                    txnserviceId,
                    gatewayMoreInfo,
                    txntransactionDate,
                    isCard,
                    new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                returnBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                takeBack();
                            } else {
                                takeBackWithError();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Return confirm error", e);
                            takeBackWithError();
                        }
                    }
            );
        } else if (onwardBooking != null) {
            AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(
                    onwardBooking,
                    txntransactionStatus,
                    txnsuccessFlag,
                    txntxnId,
                    txnbankTransactionNo,
                    bookingTransactionID,
                    txngatewayAmount,
                    txnserviceId,
                    gatewayMoreInfo,
                    txntransactionDate,
                    isCard,
                    new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmAdvSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmAdvSeatBookingAndroidResponse(response);
                                takeBack();
                            } else {
                                takeBackWithError();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Advance confirm error", e);
                            takeBackWithError();
                        }
                    }
            );
        } else {
            takeBack();
        }
    }

    public void takeBack() {
        Intent resultIntent = new Intent();
        if (onwardBooking != null) {
            resultIntent.putExtra(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
        }
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
        Intent intent = new Intent(SapphirePaymentGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", (txnerrorDescription != null ? txnerrorDescription : "Transaction Failed") + " |OB.Ref:" + rtcRefNo);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SapphirePaymentGateway.this, R.style.CustomDialogTheme);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(SapphirePaymentGateway.this, HomeActivity.class);
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
}
