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
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSuccessActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.payphi.customersdk.views.Application;
import com.payphi.customersdk.views.PayPhiSdk;
import com.payphi.customersdk.views.PaymentOptionsActivity;

import java.util.Locale;
import java.util.Map;

public class SapphirePaymentGatewayCurrent extends AppCompatActivity {
    private static final String TAG = "SapphirePGCurrent";

    private ProgressBar progressBar;
    private TextView tvStatus;

    private String totalFare, rtcRefNo, onwardPNR, returnPNR = "";
    private String pnrNo, isCard, textlang;
    private String mail, name, number;

    private String txnsuccessFlag = null;
    private String txnmerchantID = null;
    private String txnorderId = null;
    private String txngatewayAmount = null;
    private String txnpaymentMode = null;
    private String txntransactionDate = null;
    private String txntxnId = null;
    private String txnbankTransactionNo = null;
    private String txntransactionStatus = null;
    private String txnerrorDescription = null;

    private String bookingTransactionID;
    private SearchParamsCurrent searchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sapphire_payment_gateway);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        if (getIntent() != null && getIntent().getExtras() != null) {
            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            if (searchParams != null) {
                textlang = searchParams.getCrntlanguage() != null ? searchParams.getCrntlanguage().trim() : "english";
                mail = searchParams.getPassengerEmail();
                name = searchParams.getPassengerName();
                number = searchParams.getPassengerMobile();
                totalFare = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
                rtcRefNo = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
                pnrNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
                isCard = searchParams.getGatewayLookupId();
            }
        }

        if (TextUtils.isEmpty(rtcRefNo)) {
            rtcRefNo = "" + System.currentTimeMillis();
        }

        Log.d(TAG, "Current Booking Parameters - TotalFare: " + totalFare + ", RefNo: " + rtcRefNo + ", PNR: " + pnrNo);

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
                                Toast.makeText(SapphirePaymentGatewayCurrent.this, msg, Toast.LENGTH_LONG).show();
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
        String email = !TextUtils.isEmpty(mail) ? mail : "customer@gsrtc.in";

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
            txntransactionDate = !TextUtils.isEmpty(paymentDateTime) ? paymentDateTime : "";
            bookingTransactionID = txntxnId;

            if (searchParams != null) {
                searchParams.setBookingTransactionID(bookingTransactionID);
            }

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
        if (searchParams == null) {
            takeBackWithError();
            return;
        }

        SoapClientUtil.conformCurrentSeatBooking(searchParams, new SoapClientUtil.ConfirmCurrentSeatBookingAndroidCallback() {
            @Override
            public void onSuccess(ConfirmCurrentSeatBookingAndroidResponse confirmResponse) {
                if (confirmResponse != null) {
                    String confirmStatus = confirmResponse.getConfirmStatus();
                    searchParams.setConfirmStatus(confirmStatus);

                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    ActivityUtil.openNewActivity(SapphirePaymentGatewayCurrent.this, CurrentBookingSuccessActivity.class, extra);
                    finish();
                } else {
                    takeBackWithError();
                }
            }

            @Override
            public void onError(Exception e) {
                ActivityUtil.showErrorToast(SapphirePaymentGatewayCurrent.this, e);
            }
        });
    }

    public void takeBackWithError() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void failedTicket() {
        Intent intent = new Intent(SapphirePaymentGatewayCurrent.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", (txnerrorDescription != null ? txnerrorDescription : "Transaction Failed") + " |OB.Ref:" + rtcRefNo);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SapphirePaymentGatewayCurrent.this, R.style.CustomDialogTheme);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(SapphirePaymentGatewayCurrent.this, HomeActivity.class);
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
