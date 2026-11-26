package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.easebuzz.payment.kit.PWECheckoutActivity;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.Passenger;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.advance_booking.BillDeskFailtrans;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSuccessActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Unified Easebuzz / Union Bank Payment Gateway Activity
 * Handles Advance Booking, Package Tour Booking, and Current Booking.
 */
public class EasebuzzPaymentGateway extends AppCompatActivity {
    private static final String TAG = "EasebuzzPG";

    private ProgressBar progressBar;
    private TextView tvStatus;
    private WebView webView;

    private String totalFare = "0.0";
    private String rtcRefNo = "";
    private String onwardPNR = "";
    private String isCard = EasebuzzConstants.GATEWAY_CODE;
    private String textlang = "english";

    // Booking models (either Current or Advance/Package)
    private SearchParamsCurrent searchParams;
    private Booking onwardBooking, returnBooking, divyangBooking;

    private ActivityResultLauncher<Intent> pweActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easebuzz_payment_gateway);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        webView = findViewById(R.id.webView);

        pweActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        progressBar.setVisibility(View.GONE);
                        Intent data = result.getData();
                        if (data != null) {
                            String payment_result = data.getStringExtra("result");
                            String payment_response = data.getStringExtra("payment_response");
                            Log.d(TAG, "Easebuzz result: " + payment_result + ", response: " + payment_response);

                            if ("payment_successfull".equalsIgnoreCase(payment_result)
                                    || "payment_successful".equalsIgnoreCase(payment_result)
                                    || (payment_result != null && payment_result.toLowerCase().contains("success"))) {
                                String easepayid = rtcRefNo;
                                String bankRef = "";
                                try {
                                    if (payment_response != null) {
                                        JSONObject json = new JSONObject(payment_response);
                                        easepayid = json.optString("easepayid", rtcRefNo);
                                        bankRef = json.optString("bank_ref_num", rtcRefNo);
                                    }
                                } catch (Exception ignored) {
                                }
                                handlePaymentSuccess(bankRef, bankRef);
                            } else {
                                String failureReason = !TextUtils.isEmpty(payment_result) ? payment_result : "Payment failed / cancelled";
                                handlePaymentFailure(failureReason);
                            }
                        } else {
                            handlePaymentFailure("Transaction cancelled by user");
                        }
                    }
                }
        );

        extractIntentData();
        initiatePayment();
    }

    private void extractIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        totalFare = extras.getString("totalFare", "");
        isCard = extras.getString("isCard", EasebuzzConstants.GATEWAY_CODE);
        textlang = extras.getString("textlang", "english");

        // 1. Current Booking params
        searchParams = (SearchParamsCurrent) extras.getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
        if (searchParams != null) {
            rtcRefNo = searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse();
            if (TextUtils.isEmpty(rtcRefNo)) {
                rtcRefNo = searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse();
            }
            if (TextUtils.isEmpty(totalFare)) {
                totalFare = searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket();
            }
            if (searchParams.getCrntlanguage() != null) {
                textlang = searchParams.getCrntlanguage().trim();
            }
        }

        // 2. Advance / Package Booking params
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);
        returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);

        if (returnBooking != null && returnBooking.getSaveReturnBookingDetailsTemporary() != null) {
            rtcRefNo = returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            onwardPNR = returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber();
        } else if (divyangBooking != null && divyangBooking.getSaveReturnBookingDetailsTemporary() != null) {
            rtcRefNo = divyangBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
            onwardPNR = onwardBooking != null && onwardBooking.getSaveBookingDetailsTemporary() != null
                    ? onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber() : "";
        } else if (onwardBooking != null && onwardBooking.getSaveBookingDetailsTemporary() != null) {
            rtcRefNo = onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo();
            onwardPNR = onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber();
        }

        if (TextUtils.isEmpty(rtcRefNo)) {
            rtcRefNo = !TextUtils.isEmpty(onwardPNR) ? onwardPNR : ("GSRTC" + System.currentTimeMillis());
        }
    }

    /**
     * Step 1: Initiate Easebuzz Payment by generating SHA-512 Hash & calling Initiate Link API
     */
    private void initiatePayment() {
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Connecting to payment gateway...");

        float amountFloat = 0;
        try {
            amountFloat = Float.parseFloat(totalFare.trim());
        } catch (Exception e) {

        }
        final String formattedAmount = String.format(Locale.US, "%.2f", amountFloat);
        final String txnId = rtcRefNo;
        final String firstName = extractPassengerName();
        final String email = extractPassengerEmail();
        final String phone = extractPassengerPhone();

        Log.d(TAG, "Initiating payment: txn_id=" + txnId + ", amount=" + formattedAmount + ", name=" + firstName + ", email=" + email + ", phone=" + phone);

        // Generate Exact Bank Format Hash
        final String hash = EasebuzzConstants.generateSha512Hash(txnId, formattedAmount, firstName, email);

        RequestBody formBody = new FormBody.Builder()
                .add("key", EasebuzzConstants.MERCHANT_KEY)
                .add("txnid", txnId)
                .add("amount", formattedAmount)
                .add("productinfo", EasebuzzConstants.PRODUCT_INFO)
                .add("firstname", firstName)
                .add("email", email)
                .add("phone", phone)
                .add("surl", EasebuzzConstants.CALLBACK_URL)
                .add("furl", EasebuzzConstants.CALLBACK_URL)
                .add("hash", hash != null ? hash : "")
                .add("udf1", "")
                .add("udf2", "")
                .add("udf3", "")
                .add("udf4", "")
                .add("udf5", "")
                .add("udf6", "")
                .add("udf7", "")
                .add("udf8", "")
                .add("udf9", "")
                .add("udf10", "")
                .build();

        Request request = new Request.Builder()
                .url(EasebuzzConstants.INITIATE_URL)
                .post(formBody)
                .addHeader("Accept", "application/json")
                .build();

        new OkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> handlePaymentFailure("Connection error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        if (json.optInt("status", 0) == 1) {
                            final String accessKey = json.getString("data");
                            runOnUiThread(() -> launchPayment(accessKey));
                        } else {
                            final String msg = json.optString("data", "Error initiating transaction");
                            runOnUiThread(() -> handlePaymentFailure(msg));
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> handlePaymentFailure("Invalid response: " + e.getMessage()));
                    }
                } else {
                    runOnUiThread(() -> handlePaymentFailure("Server error: HTTP " + response.code()));
                }
            }
        });
    }

    /**
     * Step 2: Launch Easebuzz Native SDK or Web Checkout
     */
    private void launchPayment(String accessKey) {
        tvStatus.setText("Opening payment screen...");



            Intent intentProceed = new Intent(this, PWECheckoutActivity.class);
            intentProceed.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intentProceed.putExtra("access_key", accessKey);
            intentProceed.putExtra("pay_mode", EasebuzzConstants.PAY_MODE);
            pweActivityResultLauncher.launch(intentProceed);

    }


    /**
     * Step 3: Handle SDK Result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EasebuzzConstants.EPG_REQUEST_CODE) {
            progressBar.setVisibility(View.GONE);
            if (data != null) {
                String result = data.getStringExtra("result");
                String paymentResponse = data.getStringExtra("payment_response");
                Log.d(TAG, "Easebuzz result: " + result + ", response: " + paymentResponse);

                if ("payment_successful".equalsIgnoreCase(result)
                        || "payment_successfull".equalsIgnoreCase(result)
                        || (result != null && result.toLowerCase().contains("success"))) {
                    String easepayid = rtcRefNo;
                    String bankRef ="";
                    try {
                        if (paymentResponse != null) {
                            JSONObject json = new JSONObject(paymentResponse);
                            easepayid = json.optString("easepayid", rtcRefNo);
                            bankRef = json.optString("bank_ref_num", rtcRefNo);
                        }
                    } catch (Exception ignored) {
                    }
                    handlePaymentSuccess(bankRef, bankRef);
                } else {
                    String failureReason = !TextUtils.isEmpty(result) ? result : "Payment failed / cancelled";
                    handlePaymentFailure(failureReason);
                }
            } else {
                handlePaymentFailure("Transaction cancelled by user");
            }
        }
    }

    /**
     * Step 4: Handle Payment Success & Confirm Booking with Backend
     */
    private void handlePaymentSuccess(String txnId, String moreInfo) {
        runOnUiThread(() -> {
            if (tvStatus != null) {
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("Payment successful! Confirming ticket...");
            }
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            if (webView != null) webView.setVisibility(View.GONE);
        });

        final String status = EasebuzzConstants.STATUS_SUCCESS;
        final String successFlag = "01";
        final String txnNumber = !TextUtils.isEmpty(txnId) ? txnId : rtcRefNo;
        final String txnDate = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.S",
                Locale.US
        ).format(new Date(System.currentTimeMillis()));
        final String paymentMode = EasebuzzConstants.PG_LOOKUP_VALUE;

        // Current Booking Confirmation
        if (searchParams != null) {
            searchParams.setBookingTransactionID(txnNumber);
            SoapClientUtil.conformCurrentSeatBooking(searchParams, new SoapClientUtil.ConfirmCurrentSeatBookingAndroidCallback() {
                @Override
                public void onSuccess(ConfirmCurrentSeatBookingAndroidResponse response) {
                    if (response != null) {
                        searchParams.setConfirmStatus(response.getConfirmStatus());
                        Bundle extra = new Bundle();
                        extra.putString("textlang", textlang);
                        extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                        ActivityUtil.openNewActivity(EasebuzzPaymentGateway.this, CurrentBookingSuccessActivity.class, extra);
                        finish();
                    } else {
                        handlePaymentFailure("Ticket confirmation failed");
                    }
                }

                @Override
                public void onError(Exception e) {
                    handlePaymentFailure("Confirmation error: " + e.getMessage());
                }
            });
            return;
        }

        // Advance Booking Confirmation
        if (onwardBooking != null && onwardBooking.getSearchParams() != null &&
                onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper") &&
                divyangBooking != null) {

            AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroid(
                    onwardBooking, divyangBooking, status, successFlag, txnNumber, txnNumber,
                    txnNumber, totalFare, paymentMode, moreInfo, txnDate, isCard,
                    new AdvanceBookingNetworkUtil.ConfirmDivyangRtnSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                divyangBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                finishBookingSuccess();
                            } else {
                                handlePaymentFailure("Divyang ticket confirmation failed");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            handlePaymentFailure("Confirmation error: " + e.getMessage());
                        }
                    }
            );
        } else if (returnBooking != null) {
            AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroid(
                    onwardBooking, returnBooking, status, successFlag, txnNumber, txnNumber,
                    txnNumber, totalFare, paymentMode, moreInfo, txnDate, isCard,
                    new AdvanceBookingNetworkUtil.ConfirmRtnSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmRtnSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                returnBooking.setConfirmRtnSeatBookingAndroidResponse(response);
                                finishBookingSuccess();
                            } else {
                                handlePaymentFailure("Return ticket confirmation failed");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            handlePaymentFailure("Confirmation error: " + e.getMessage());
                        }
                    }
            );
        } else {
            AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroid(
                    onwardBooking, status, successFlag, txnNumber, txnNumber,
                    txnNumber, totalFare, paymentMode, moreInfo, txnDate, isCard,
                    new AdvanceBookingNetworkUtil.ConfirmAdvSeatBookingAndroidCallback() {
                        @Override
                        public void onSuccess(ConfirmAdvSeatBookingAndroidResponse response) {
                            if (response != null) {
                                onwardBooking.setConfirmAdvSeatBookingAndroidResponse(response);
                                finishBookingSuccess();
                            } else {
                                handlePaymentFailure("Ticket confirmation failed");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            handlePaymentFailure("Confirmation error: " + e.getMessage());
                        }
                    }
            );
        }
    }

    private void finishBookingSuccess() {
        Intent resultIntent = new Intent();
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

    private void handlePaymentFailure(String reason) {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(EasebuzzPaymentGateway.this, BillDeskFailtrans.class);
        intent.putExtra("textlang", textlang);
        intent.putExtra("errorDescription", (reason != null ? reason : "Payment failed") + " | Ref: " + rtcRefNo);
        startActivity(intent);
        finish();
    }

    private String extractPassengerName() {
        String name = getIntent().getStringExtra("passengerName");
        if (TextUtils.isEmpty(name)) name = getIntent().getStringExtra("name");

        if (TextUtils.isEmpty(name) && searchParams != null) {
            name = searchParams.getPassengerName();
        }
        if (TextUtils.isEmpty(name) && onwardBooking != null && onwardBooking.getPassengers() != null) {
            for (Passenger p : onwardBooking.getPassengers()) {
                if (p != null) {
                    name = !TextUtils.isEmpty(p.getPassengerNameA()) ? p.getPassengerNameA() : p.getPassengerName();
                    if (!TextUtils.isEmpty(name)) break;
                }
            }
        }
        if (TextUtils.isEmpty(name)) return EasebuzzConstants.DEFAULT_FIRST_NAME;
        String cleaned = name.replaceAll("[^a-zA-Z0-9 .\\-_]", "").trim().replaceAll("\\s+", " ");
        if (cleaned.length() > 50) cleaned = cleaned.substring(0, 50).trim();
        return TextUtils.isEmpty(cleaned) ? EasebuzzConstants.DEFAULT_FIRST_NAME : cleaned;
    }

    private String extractPassengerEmail() {
        String email = getIntent().getStringExtra("passengerEmail");
        if (TextUtils.isEmpty(email)) email = getIntent().getStringExtra("mail");

        if (TextUtils.isEmpty(email) && searchParams != null) {
            email = searchParams.getPassengerEmail();
        }
        if (TextUtils.isEmpty(email) && onwardBooking != null && onwardBooking.getPassengerInfo() != null) {
            email = onwardBooking.getPassengerInfo().getEmail();
        }
        return isValidEmail(email) ? email.trim() : "customer@gsrtc.in";
    }

    private String extractPassengerPhone() {
        String phone = getIntent().getStringExtra("passengerPhone");
        if (TextUtils.isEmpty(phone)) phone = getIntent().getStringExtra("number");

        if (TextUtils.isEmpty(phone) && searchParams != null) {
            phone = searchParams.getPassengerMobile();
        }
        if (TextUtils.isEmpty(phone) && onwardBooking != null && onwardBooking.getPassengerInfo() != null) {
            phone = onwardBooking.getPassengerInfo().getPhone();
        }
        if (TextUtils.isEmpty(phone)) return "9876543210";
        String digits = phone.replaceAll("[^0-9]", "");
        return digits.length() >= 10 ? digits.substring(digits.length() - 10) : "9876543210";
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && email.contains("@") && email.contains(".");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setTitle("GSRTC Payment")
                .setMessage("Going back will discard your booking. Do you want to continue?")
                .setPositiveButton("YES", (d, w) -> finish())
                .setNegativeButton("NO", null)
                .show();
    }
}
