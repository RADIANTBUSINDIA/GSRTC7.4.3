package com.gsrtc.mobileweb.ui.activities.view_booking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.print_sms.GetPrintSmsDetailslResponse;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsData;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsParam;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.BookingHistoryNetworkUtils;

public class ViewandSMSTicket extends BaseNavigationDrawerActivity {

    private PrintSmsData ticketsms;
    private ViewHolder holder;
    private String email, mobile;
    private String textlang;
    private EditText etOtp;

    private static final int REQ_USER_CONSENT = 201;

    private String serverOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_view_smsticket);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            textlang = bundle.getString("textlang");
        }

        setup();
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(
                com.google.android.gms.auth.api.phone.SmsRetriever.SMS_RETRIEVED_ACTION
        );

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsReceiver, filter, android.content.Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(smsReceiver, filter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            unregisterReceiver(smsReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setup() {
        ticketsms = new PrintSmsData();
        ticketsms.printSmsParam = new PrintSmsParam();
        holder = new ViewHolder(this);

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitBtn();

                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.email.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.mobile.getWindowToken(), 0);
            }
        });
    }

    private void onSubmitBtn() {

        email = holder.email.getText().toString().trim();
        mobile = holder.mobile.getText().toString().trim();

        ticketsms.printSmsParam.emailID = email;
        ticketsms.printSmsParam.mobileNo = mobile;

        if (email.isEmpty() && mobile.isEmpty()) {
            ActivityUtil.showToast(this,
                    "Please enter Email ID or Mobile Number");
            return;
        }

        if (!email.isEmpty() && !mobile.isEmpty()) {
            ActivityUtil.showToast(this,
                    "Please enter only Email OR Mobile Number");
            return;
        }

        /* ================= EMAIL FLOW (NO OTP) ================= */
        if (!email.isEmpty()) {

            if (!ValidationUtil.isValidEmail(holder.email.getText())) {
                ActivityUtil.showToast(this,
                        "Please enter valid Email");
                return;
            }

            goEmail();
            return;
        }

        /* ================= MOBILE FLOW ================= */
        if (!mobile.isEmpty()) {

            if (!ValidationUtil.isValidPhone(holder.mobile.getText())) {
                ActivityUtil.showToast(this,
                        "Please enter valid Mobile Number");
                return;
            }

            if (mobile.length() < 10 || mobile.length() > 13) {
                ActivityUtil.showToast(this,
                        "Please enter valid Mobile Number");
                return;
            }

            // 🔥 FIRST CHECK BOOKINGS
            checkMobileHasBookings();
        }
    }

    /* ================= CHECK BOOKINGS FIRST ================= */

    private void checkMobileHasBookings() {

        CustomisedProgressDialog.SHOW_CUST_DIA(
                ViewandSMSTicket.this,
                "Checking bookings..."
        );

        BookingHistoryNetworkUtils.GetPrintTicketMobile(
                ticketsms,
                new BookingHistoryNetworkUtils.GetPrintTicketMobileCallback() {

                    @Override
                    public void onSuccess(GetPrintSmsDetailslResponse[] response) {

                        CustomisedProgressDialog.STOP_CUST_DIA();

                        // ❌ NO BOOKINGS → STOP (NO OTP)
                        if (response == null || response.length == 0) {
                            ActivityUtil.showToast(
                                    ViewandSMSTicket.this,
                                    "No Transaction Found!"
                            );
                            return;
                        }


                        ticketsms.getPrintSmsDetailslResponses = response;
                        sendOtpToMobile();
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showErrorToast(
                                ViewandSMSTicket.this, e);
                    }
                });
    }

    /* ================= SEND OTP ================= */

    private void sendOtpToMobile() {

        CustomisedProgressDialog.SHOW_CUST_DIA(
                ViewandSMSTicket.this,
                "Sending OTP..."
        );

        BookingHistoryNetworkUtils.GetCommonOTP(
                mobile,
                new BookingHistoryNetworkUtils.GetCommonOTPCallback() {

                    @Override
                    public void onSuccess(String otp) {

                        CustomisedProgressDialog.STOP_CUST_DIA();

                        if (otp != null && otp.length() == 6) {
                            serverOtp = otp;
                            showOtpDialog();
                        } else {
                            ActivityUtil.showToast(
                                    ViewandSMSTicket.this,
                                    "Failed to send OTP");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showErrorToast(
                                ViewandSMSTicket.this, e);
                    }
                });
    }

    /* ================= OTP DIALOG ================= */

    private void showOtpDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_otp);
        dialog.setCancelable(false);

        etOtp = dialog.findViewById(R.id.etOtp);
        Button btnVerify = dialog.findViewById(R.id.btnVerifyOtp);
        Button btnCancel = dialog.findViewById(R.id.btnCancelOtp);
        TextView txtOtpError = dialog.findViewById(R.id.txtOtpError);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredOtp = etOtp.getText().toString().trim();
                txtOtpError.setVisibility(View.GONE);

                if (enteredOtp.isEmpty()) {
                    txtOtpError.setText("Please enter OTP");
                    txtOtpError.setVisibility(View.VISIBLE);
                    return;
                }

                if (enteredOtp.length() != 6) {
                    txtOtpError.setText("OTP must be 6 digits");
                    txtOtpError.setVisibility(View.VISIBLE);
                    return;
                }

                if (!enteredOtp.equals(serverOtp)) {
                    txtOtpError.setText("Wrong OTP entered");
                    txtOtpError.setVisibility(View.VISIBLE);
                    return;
                }


                dialog.dismiss();
                goMobile();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        startSmsUserConsent();
        dialog.show();

        dialog.getWindow().setSoftInputMode(
                android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);

        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5)
                + (int) (140 * getResources().getDisplayMetrics().density);

        dialog.getWindow().setLayout(width, height);


    }

    /* ================= NAVIGATION ================= */

    public void goMobile() {

        if (ticketsms.getPrintSmsDetailslResponses == null ||
                ticketsms.getPrintSmsDetailslResponses.length == 0) {

            ActivityUtil.showToast(this, "No Transaction Found!");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("textlang", textlang);
        bundle.putSerializable(
                AppConstants.INTENT_EXTRA.PRINT_SMS_TICKET, ticketsms);

        ActivityUtil.openNewActivity(this, TicketData.class, bundle);
    }

    private void startSmsUserConsent() {
        com.google.android.gms.auth.api.phone.SmsRetrieverClient client =
                com.google.android.gms.auth.api.phone.SmsRetriever.getClient(this);

        client.startSmsUserConsent(null)
                .addOnSuccessListener(aVoid -> {
                    android.util.Log.d("OTP_DEBUG", "Consent STARTED");
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("OTP_DEBUG", "Consent FAILED");
                });
    }



    private android.content.BroadcastReceiver smsReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {

            if (com.google.android.gms.auth.api.phone.SmsRetriever.SMS_RETRIEVED_ACTION
                    .equals(intent.getAction())) {

                android.os.Bundle extras = intent.getExtras();
                if (extras == null) return;

                com.google.android.gms.common.api.Status status =
                        (com.google.android.gms.common.api.Status)
                                extras.get(com.google.android.gms.auth.api.phone.SmsRetriever.EXTRA_STATUS);

                if (status == null) return;

                if (status.getStatusCode() ==
                        com.google.android.gms.common.api.CommonStatusCodes.SUCCESS) {

                    android.content.Intent consentIntent =
                            extras.getParcelable(
                                    com.google.android.gms.auth.api.phone.SmsRetriever.EXTRA_CONSENT_INTENT
                            );

                    try {
                        startActivityForResult(consentIntent, REQ_USER_CONSENT);
                    } catch (android.content.ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {

                String message = data.getStringExtra(
                        com.google.android.gms.auth.api.phone.SmsRetriever.EXTRA_SMS_MESSAGE
                );

                android.util.Log.d("OTP_DEBUG", "SMS: " + message);

                if (message != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{6}");
                    java.util.regex.Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {
                        String otp = matcher.group(0);
                        android.util.Log.d("OTP_DEBUG", "OTP: " + otp);
                        if (etOtp != null) {
                            etOtp.setText(otp);
                        }
                    }
                }
            }

            else if (resultCode == RESULT_CANCELED) {
                android.util.Log.d("OTP_DEBUG", "User denied SMS consent");
            }
        }
    }



    public void goEmail() {

        if (ticketsms.getPrintSmsDetailslResponses == null) {
            getTicketEmail();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("textlang", textlang);
        bundle.putSerializable(
                AppConstants.INTENT_EXTRA.PRINT_SMS_TICKET, ticketsms);

        ActivityUtil.openNewActivity(this, TicketData.class, bundle);
    }

    private void getTicketEmail() {

        CustomisedProgressDialog.SHOW_CUST_DIA(this,
                "Please wait loading...");

        BookingHistoryNetworkUtils.GetPrintTicketEmail(
                ticketsms,
                new BookingHistoryNetworkUtils.GetPrintTicketEmailCallback() {

                    @Override
                    public void onSuccess(
                            GetPrintSmsDetailslResponse[] response) {

                        if (response != null && response.length > 0) {
                            ticketsms.getPrintSmsDetailslResponses = response;
                            goEmail();
                        } else {
                            CustomisedProgressDialog.STOP_CUST_DIA();
                            ActivityUtil.showToast(
                                    ViewandSMSTicket.this,
                                    "No Transaction Found!");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showErrorToast(
                                ViewandSMSTicket.this, e);
                    }
                });
    }

    /* ================= VIEW HOLDER ================= */

    public class ViewHolder {

        EditText email, mobile;
        TextView title, emailtxt, mobiletxt, or;
        Button submitBtn;

        ViewHolder(Activity activity) {

            submitBtn = activity.findViewById(R.id.submitBtn);
            email = activity.findViewById(R.id.email);
            mobile = activity.findViewById(R.id.mobile);

            title = activity.findViewById(R.id.title_sms);
            emailtxt = activity.findViewById(R.id.emailtxt);
            mobiletxt = activity.findViewById(R.id.mobiletxt);
            or = activity.findViewById(R.id.sms_or);

            if ("gujarati".equalsIgnoreCase(textlang)) {

                email.setHint(R.string.vwsms_enter_email);
                mobile.setHint(R.string.vwsms_enter_mobile);

                title.setText(R.string.vwsms_title);
                emailtxt.setText(R.string.vwsms_email);
                mobiletxt.setText(R.string.vwsms_mobile);
                or.setText(R.string.vwsms_or);
                submitBtn.setText(R.string.vwsms_submit);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("textlang", textlang);
        startActivity(i);
    }
}
