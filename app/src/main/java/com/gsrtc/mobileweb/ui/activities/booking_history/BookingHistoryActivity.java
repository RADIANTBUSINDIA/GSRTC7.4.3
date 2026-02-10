package com.gsrtc.mobileweb.ui.activities.booking_history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.booking_history.BookingHistory;
import com.gsrtc.mobileweb.models.booking_history.BookingHistorySearchParam;
import com.gsrtc.mobileweb.models.booking_history.GetViewBookingHistoryDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.BookingHistoryNetworkUtils;

import java.util.Calendar;

public class BookingHistoryActivity extends BaseNavigationDrawerActivity {

    private BookingHistory booking;
    private ViewHolder holder;
    private ProgressDialog progressDialog;
    private CalendarDatePickerDialogFragment calendarDialog;

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";

    private String sts = "S"; // S = Success, F = Failure
    private String textlang;

    // OTP
    private String serverOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_booking_histroy);

        Bundle b = getIntent().getExtras();
        if (b != null) textlang = b.getString("textlang");

        setup();
    }

    private void setup() {

        booking = new BookingHistory();
        booking.bookingHistorySearchParam = new BookingHistorySearchParam();

        holder = new ViewHolder(this);

        holder.submitBtn.setOnClickListener(v -> onSubmitBtn());

        holder.from_date.setOnClickListener(this::openDatePickerDialog);
        holder.to_date.setOnClickListener(this::openDatePickerDialog);

        holder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sts = (position == 0) ? "S" : "F";
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /* ================= DATE PICKER ================= */

    @SuppressLint("WrongConstant")
    private void openDatePickerDialog(final View view) {

        if (calendarDialog != null && calendarDialog.isVisible()) return;

        Calendar now = Calendar.getInstance();
        MonthAdapter.CalendarDay today = new MonthAdapter.CalendarDay();

        calendarDialog = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener((dialog, year, month, day) ->
                        onDatePicked(view, year, month, day))
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH))
                .setDateRange(null, today)
                .setThemeDark();

        calendarDialog.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    private void onDatePicked(View view, int year, int month, int day) {

        String d = (day < 10 ? "0" : "") + day;
        String m = ((month + 1) < 10 ? "0" : "") + (month + 1);
        String date = d + "/" + m + "/" + year;

        if (view.getId() == R.id.from_date) {
            holder.from_date.setText(date);
        } else {
            holder.to_date.setText(date);
        }
    }

    /* ================= SUBMIT ================= */

    private void onSubmitBtn() {

        String email = holder.email.getText().toString().trim();
        String mobile = holder.mobile.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(mobile)) {
            ActivityUtil.showToast(this,
                    "Email ID and Mobile Number are mandatory");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            ActivityUtil.showToast(this,
                    "Please enter valid Email ID");
            return;
        }

        if (!TextUtils.isDigitsOnly(mobile) || mobile.length() != 10) {
            ActivityUtil.showToast(this,
                    "Please enter valid Mobile Number");
            return;
        }

        if (TextUtils.isEmpty(holder.from_date.getText())
                || TextUtils.isEmpty(holder.to_date.getText())) {
            ActivityUtil.showToast(this,
                    "Please select From Date and To Date");
            return;
        }

        booking.bookingHistorySearchParam.email = email;
        booking.bookingHistorySearchParam.mobileNo = mobile;
        booking.bookingHistorySearchParam.fromDate =
                holder.from_date.getText().toString();
        booking.bookingHistorySearchParam.toDate =
                holder.to_date.getText().toString();
        booking.bookingHistorySearchParam.status = sts;

        getAllTickets();
    }

    /* ================= API ================= */

    private void getAllTickets() {

        showProgressDialog();

        BookingHistoryNetworkUtils.GetViewBookingHistoryDetails(
                booking,
                new BookingHistoryNetworkUtils.GetViewBookingHistoryDetailsCallback() {

                    @Override
                    public void onSuccess(GetViewBookingHistoryDetailsResponse[] response) {

                        hideProgressDialog();

                        // 🔥 NO BOOKINGS → NO OTP
                        if (response == null || response.length == 0) {
                            ActivityUtil.showToast(
                                    BookingHistoryActivity.this,
                                    "No Bookings Found");
                            return;
                        }

                        booking.getViewBookingHistoryDetailsResponse = response;

                        // 🔐 OTP ONLY FOR SUCCESS
                        if ("S".equalsIgnoreCase(sts)) {
                            sendOtp();
                        } else {
                            goNext();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        hideProgressDialog();
                        ActivityUtil.showErrorToast(
                                BookingHistoryActivity.this, e);
                    }
                });
    }

    /* ================= OTP ================= */

    private void sendOtp() {

        CustomisedProgressDialog.SHOW_CUST_DIA(this, "Sending OTP...");

        BookingHistoryNetworkUtils.GetCommonOTP(
                booking.bookingHistorySearchParam.mobileNo,
                new BookingHistoryNetworkUtils.GetCommonOTPCallback() {

                    @Override
                    public void onSuccess(String otp) {
                        CustomisedProgressDialog.STOP_CUST_DIA();

                        if (!TextUtils.isEmpty(otp) && otp.length() == 6) {
                            serverOtp = otp;
                            showOtpDialog();
                        } else {
                            ActivityUtil.showToast(
                                    BookingHistoryActivity.this,
                                    "Failed to send OTP");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showErrorToast(
                                BookingHistoryActivity.this, e);
                    }
                });
    }

    private void showOtpDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_otp);
        dialog.setCancelable(false);

        // 🔥 CRUCIAL FOR CURVED DESIGN
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
        }

        EditText etOtp = dialog.findViewById(R.id.etOtp);
        TextView txtErr = dialog.findViewById(R.id.txtOtpError);
        Button verify = dialog.findViewById(R.id.btnVerifyOtp);
        Button cancel = dialog.findViewById(R.id.btnCancelOtp);

        verify.setOnClickListener(v -> {

            String entered = etOtp.getText().toString().trim();
            txtErr.setVisibility(View.GONE);

            if (!entered.equals(serverOtp)) {
                txtErr.setText("Wrong OTP entered");
                txtErr.setVisibility(View.VISIBLE);
                return;
            }

            dialog.dismiss();
            goNext();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        int w = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int h = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(w, h);
    }

    /* ================= NAVIGATION ================= */

    private void goNext() {

        Bundle bundle = new Bundle();
        bundle.putString("textlang", textlang);
        bundle.putSerializable(
                AppConstants.INTENT_EXTRA.BOOKING_HISTORY, booking);

        ActivityUtil.openNewActivity(
                this,
                BookingHistorySearchResultActivity.class,
                bundle);
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
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /* ================= VIEW HOLDER ================= */

    class ViewHolder {

        EditText email, mobile, from_date, to_date;
        Spinner status;
        Button submitBtn;
        TextView emailtxt, mobiletxt, datetxt, statustxt, title;

        ViewHolder(Activity a) {
            submitBtn = a.findViewById(R.id.submitBtn);
            email = a.findViewById(R.id.email);
            mobile = a.findViewById(R.id.mobile);
            from_date = a.findViewById(R.id.from_date);
            to_date = a.findViewById(R.id.to_date);
            status = a.findViewById(R.id.status);

            emailtxt = a.findViewById(R.id.email_txt);
            mobiletxt = a.findViewById(R.id.mobile_txt);
            datetxt = a.findViewById(R.id.date_txt);
            statustxt = a.findViewById(R.id.status_txt);
            title = a.findViewById(R.id.title_book_history);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("textlang", textlang);
        startActivity(i);
    }
}
