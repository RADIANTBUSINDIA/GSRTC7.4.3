package com.gsrtc.mobileweb.ui.activities.booking_history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.booking_history.BookingHistory;
import com.gsrtc.mobileweb.models.booking_history.BookingHistorySearchParam;
import com.gsrtc.mobileweb.models.booking_history.GetViewBookingHistoryDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.AdvanceBookingSearchActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.net.BookingHistoryNetworkUtils;


/**
 * Created by Ayush on 23-May-17.
 */

public class BookingHistoryActivity extends BaseNavigationDrawerActivity {

    BookingHistory booking;
    ViewHolder holder;
    ProgressDialog progressDialog;
    CalendarDatePickerDialogFragment calendarDialog;
    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
    MonthAdapter.CalendarDay toDate;
    MonthAdapter.CalendarDay fromDate;
    String[] statusOption = {"Success","Failure"};
    String sts="S";
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_booking_histroy);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        setup();
    }

    private void setup() {

        booking = new BookingHistory();
        booking.bookingHistorySearchParam = new BookingHistorySearchParam();
     //   booking.getViewBookingHistoryDetailsResponse=new GetViewBookingHistoryDetailsResponse[];
        holder = new ViewHolder(this);
        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitBtn();
            }
        });

        holder.from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });

        holder.to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });



       holder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    sts="S";
                else if(position==1)
                    sts="F";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @SuppressLint("WrongConstant")
    public void openDatePickerDialog(final View view) {
        if (calendarDialog != null && calendarDialog.isVisible()) {
            return;
        }
        Date currentDate = new Date(System.currentTimeMillis());

        MonthAdapter.CalendarDay currentCalendarDay = new MonthAdapter.CalendarDay();
        Calendar preselectedDate = Calendar.getInstance();
        preselectedDate.setTimeInMillis(currentCalendarDay.getDateInMillis());
        calendarDialog = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        onDatePicked(view, year, monthOfYear, dayOfMonth);
                    }
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(preselectedDate.get(Calendar.YEAR), preselectedDate.get(Calendar.MONTH), preselectedDate.get(Calendar.DAY_OF_MONTH))
                .setDateRange(null,currentCalendarDay)
                .setDoneText("Select")
                .setCancelText("Cancel")
                .setThemeDark();
          calendarDialog.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    public void onDatePicked(View view, int year, int month, int day) {
        Calendar setDate = Calendar.getInstance();
        setDate.set(year, month, day);
        String dayString = ((day < 10) ? "0" : "") + day;
        String monthString = (((month + 1) < 10) ? "0" : "") + (month + 1);

        switch (view.getId()) {
            case R.id.from_date:
                holder.from_date.setText(dayString + "/" + monthString + "/" + year);
               //   booking.bookingHistorySearchParam.fromDate=dayString + "/" + monthString + "/" + year;
                  fromDate = new MonthAdapter.CalendarDay();
                  fromDate.setDay(year, month, day);
                break;
            case R.id.to_date:
                holder.to_date.setText(dayString + "/" + monthString + "/" + year);
               // booking.bookingHistorySearchParam.toDate=dayString + "/" + monthString + "/" +year ;
                toDate = new MonthAdapter.CalendarDay();
                toDate.setDay(year, month, day);
                break;
        }

    }


    private void onSubmitBtn() {
        if (!ValidationUtil.isValidEmail(holder.email.getText().toString())) {
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }


        booking.bookingHistorySearchParam.email = holder.email.getText().toString();
        booking.bookingHistorySearchParam.fromDate = holder.from_date.getText().toString();
        booking.bookingHistorySearchParam.toDate = holder.to_date.getText().toString();
        booking.bookingHistorySearchParam.status=sts;
        goNext();
    }

    public void goNext() {
        if (booking.getViewBookingHistoryDetailsResponse == null) {
            getAllTickets();
            return;
        }
        else {
            hideProgressDialog();
       }
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_HISTORY, booking);
        ActivityUtil.openNewActivity(this, BookingHistorySearchResultActivity.class, bundle);
    }

    private void getAllTickets() {
        showProgressDialog();
        BookingHistoryNetworkUtils.GetViewBookingHistoryDetails(booking, new BookingHistoryNetworkUtils.GetViewBookingHistoryDetailsCallback() {
            @Override
            public void onSuccess(GetViewBookingHistoryDetailsResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        hideProgressDialog();
                        ActivityUtil.showToast(BookingHistoryActivity.this, "No Bookings Found");
                    }
                    booking.getViewBookingHistoryDetailsResponse = response;
                    goNext();
                } else {
                    ActivityUtil.showToast(BookingHistoryActivity.this, "No Bookings Found");
                    hideProgressDialog();
                }
            }

            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                ActivityUtil.showErrorToast(BookingHistoryActivity.this, e);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(booking!=null){
            booking.getViewBookingHistoryDetailsResponse = null;
        }
    }

    public void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }

    public class ViewHolder {
        EditText email, from_date, to_date;
        Button submitBtn;
        Spinner status;
        TextView emailtxt,datetxt,statustxt,title;

        ViewHolder(Activity activity) {
            submitBtn = (Button) activity.findViewById(R.id.submitBtn);
            email = (EditText) activity.findViewById(R.id.email);
            status = (Spinner) activity.findViewById(R.id.status);
            from_date = (EditText) activity.findViewById(R.id.from_date);
            to_date = (EditText) activity.findViewById(R.id.to_date);

            emailtxt = (TextView) activity.findViewById(R.id.email_txt);
            datetxt = (TextView) activity.findViewById(R.id.date_txt);
            statustxt = (TextView) activity.findViewById(R.id.status_txt);
            title= (TextView) activity.findViewById(R.id.title_book_history);
            if(textlang.equalsIgnoreCase("gujarati")){
                submitBtn.setText(R.string.transhis_title);
                email.setHint(R.string.transhis_enter_email);
                from_date.setHint(R.string.transhis_fromdate);
                to_date.setHint(R.string.transhis_todate);

                emailtxt.setText(R.string.transhis_email);
                datetxt.setText(R.string.transhis_date);
                statustxt.setText(R.string.transhis_status);
                title.setText(R.string.transhis_title);
            }
        }
    }

    @Override
    public void onBackPressed() {
       // ActivityUtil.openNewActivityAsOnlyActivity(BookingHistoryActivity.this, HomeActivity.class, null);
        Intent i = new Intent(BookingHistoryActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }

}