package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Fault;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.CancellationSearchParams;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetFullCancellationDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TicketCancelNetworkUtil;

public class TicketCancellationActivity extends BaseNavigationDrawerActivity {

    ViewHolder holder;
    BookingCancellation booking;
    ProgressDialog progressDialog;
    String textlang;

    // 🔐 OTP
    private String serverOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_ticket_cancellation);
        Bundle mBun = getIntent().getExtras();
        if (mBun != null) {
            textlang = mBun.getString("textlang");
        }
        setup();
    }

    private void setup() {
        holder = new ViewHolder(this);
        booking = new BookingCancellation();
        booking.cancellationSearchParams = new CancellationSearchParams();
        holder.submitBtn.setOnClickListener(v -> onSubmitBtn());
    }

    /* ================= SUBMIT ================= */
    private void onSubmitBtn() {
        String pnr = holder.pnr.getText().toString().trim();
        String email = holder.email.getText().toString().trim();
        String mobile = holder.phone.getText().toString().trim();

        if (TextUtils.isEmpty(pnr)) {
            ActivityUtil.showToast(this, "PNR can not be empty");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }

        if (!ValidationUtil.isValidPhone(mobile)) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }

        if (mobile.length() < 10 || mobile.length() > 13) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }

        // +91 handling
        if (mobile.startsWith("+") && mobile.length() == 13) {
            mobile = mobile.substring(3);
            holder.phone.setText(mobile);
        }

        if (mobile.length() != 10) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }

        booking.cancellationSearchParams.pnr = pnr;
        booking.cancellationSearchParams.email = email;
        booking.cancellationSearchParams.mobile = mobile;
        booking.cancellationSearchParams.WSRefNo = holder.wsRefNo.getText().toString().trim();

        goNext();
    }

    /* ================= FLOW ================= */
    private void goNext() {
        if (booking.getAllPassengerDetailsByPnrNoResponse == null) {
            getAllPassengers();
            return;
        }
        // Trigger OTP only after passengers fetched successfully
        sendOtp();
    }

    /* ================= PASSENGERS ================= */
    private void getAllPassengers() {
        showProgressDialog();
        TicketCancelNetworkUtil.GetAllPassengerDetailsByPnrNo(
                booking,
                new TicketCancelNetworkUtil.GetAllPassengerDetailsByPnrNoCallback() {
                    @Override
                    public void onSuccess(GetAllPassengerDetailsByPnrNoResponse[] response) {
                        hideProgressDialog();
                        if (response == null || response.length == 0) {
                            ActivityUtil.showToast(
                                    TicketCancellationActivity.this,
                                    "Invalid PNR or details"
                            );
                            return;
                        }
                        booking.getAllPassengerDetailsByPnrNoResponse = response;
                        goNext();
                    }

                    @Override
                    public void onSuccess(Fault[] response) {
                        hideProgressDialog();
                        ActivityUtil.showToast(
                                TicketCancellationActivity.this,
                                "Invalid parameters or data"
                        );
                    }

                    @Override
                    public void onError(Exception e) {
                        hideProgressDialog();
                        ActivityUtil.showToast(
                                TicketCancellationActivity.this,
                                "Invalid parameters or data"
                        );
                    }
                });
    }

    /* ================= CANCELLATION DETAILS ================= */
    private void getCancellationDetails() {
        showProgressDialog();
        TicketCancelNetworkUtil.GetFullCancellationDetails(
                booking,
                new TicketCancelNetworkUtil.GetFullCancellationDetailsCallback() {
                    @Override
                    public void onSuccess(GetFullCancellationDetailsResponse response) {
                        hideProgressDialog();
                        if (response == null) {
                            ActivityUtil.showToast(
                                    TicketCancellationActivity.this,
                                    "Invalid PNR or details"
                            );
                            return;
                        }
                        booking.getPartialCancellationDetailsResponse = response.toPartialCancellationDetails();
                        booking.getFullCancellationDetailsResponse = response;
                        navigateNext();
                    }

                    @Override
                    public void onSuccess(Fault[] response) {
                        hideProgressDialog();
                        ActivityUtil.showToast(
                                TicketCancellationActivity.this,
                                "Invalid parameters or data"
                        );
                    }

                    @Override
                    public void onError(Exception e) {
                        hideProgressDialog();
                        ActivityUtil.showToast(
                                TicketCancellationActivity.this,
                                "Invalid parameters or data"
                        );
                    }
                });
    }

    /* ================= OTP ================= */
    private void sendOtp() {
        CustomisedProgressDialog.SHOW_CUST_DIA(this, "Sending OTP...");
        TicketCancelNetworkUtil.GetCommonOTP(
                booking.cancellationSearchParams.mobile,
                new TicketCancelNetworkUtil.GetCommonOTPCallback() {
                    @Override
                    public void onSuccess(String otp) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        if (!TextUtils.isEmpty(otp) && otp.length() == 6) {
                            serverOtp = otp;
                            showOtpDialog();
                        } else {
                            ActivityUtil.showToast(
                                    TicketCancellationActivity.this,
                                    "Failed to send OTP"
                            );
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(
                                TicketCancellationActivity.this,
                                "Failed to send OTP"
                        );
                    }
                });
    }

    private void showOtpDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_otp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        EditText etOtp = dialog.findViewById(R.id.etOtp);
        TextView txtErr = dialog.findViewById(R.id.txtOtpError);
        Button verify = dialog.findViewById(R.id.btnVerifyOtp);
        Button cancel = dialog.findViewById(R.id.btnCancelOtp);

        verify.setOnClickListener(v -> {
            String enteredOtp = etOtp.getText().toString().trim();
            txtErr.setVisibility(View.GONE);
            if (!enteredOtp.equals(serverOtp)) {
                txtErr.setText("Wrong OTP entered");
                txtErr.setVisibility(View.VISIBLE);
                return;
            }
            dialog.dismiss();
            // Fetch cancellation details after OTP verification
            getCancellationDetails();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

        int w = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int h = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
        dialog.getWindow().setLayout(w, h);
    }

    /* ================= FINAL NAV ================= */
    private void navigateNext() {
        Bundle bundle = new Bundle();
        bundle.putString("textlang", textlang);
        bundle.putSerializable(
                AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION,
                booking
        );
        ActivityUtil.openNewActivity(
                this,
                CancellationPassengerSelectionActivity.class,
                bundle
        );
    }

    /* ================= PROGRESS ================= */
    private void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) return;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /* ================= VIEW HOLDER ================= */
    class ViewHolder {
        EditText pnr, email, phone, wsRefNo;
        TextView head, pnrtxt, emailtxt, mobiletxt;
        Button submitBtn;

        ViewHolder(Activity a) {
            pnr = a.findViewById(R.id.pnr);
            email = a.findViewById(R.id.email);
            phone = a.findViewById(R.id.phone);
            wsRefNo = a.findViewById(R.id.wsRefNo);
            submitBtn = a.findViewById(R.id.submitBtn);
            head = a.findViewById(R.id.header_cancel);
            pnrtxt = a.findViewById(R.id.txtpnr);
            emailtxt = a.findViewById(R.id.txtemail);
            mobiletxt = a.findViewById(R.id.txtmobile);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("textlang", textlang);
        startActivity(i);
    }
}