package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.database.Gsrtc_local_db;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class AdvanceBookingSearchResultActivity extends BaseNavigationDrawerActivity {

    SearchResultAdapter searchResultAdapter;
    SearchParams searchParams;
    private Date mJourneyStartDate;
    private Date mReturnStartDate;
    private SimpleDateFormat mSmpDtFrmt;
    private SimpleDateFormat mSmpDtFrmtCall;
    Booking onwardBooking;
    Booking returnBooking;
    ActivityViewHolder vHolder;
    boolean dataFetched = false;
    private int mIndex;
    ProgressDialog progressDialog;
    CalendarDatePickerDialogFragment calendarDialog;
    private String textlang, discfare, divyang_disable_type, divyang_auth, div_id_valid;
    private String status, divName, address1, address2, address3, address4, div_id, gender, dob, place, type, percentage, expired, mobile, count,otp,helperPercentage;
    private String disabilityId, issueDistrictId;
    PopupWindow popupWindow;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    private EditText div_id_text, div_type_text, div_birth_date, div_expiry_date, div_percentage,div_otp;
    private Spinner div_disability_type, div_authority;
    private Button div_cancel, div_confirm, div_send_otp_btn;
    private TextView div_status_msg;
    private LinearLayout div_details_layout;
    private TextView div_name_val, div_disability_val, div_percent_val, div_authority_val, div_dob_val, div_expired_val;
    String crntDate ="",crntTime="";

    public enum SORT_METHOD {
        DEP_TIME, CLASS, PRICE;
    }

    private static final int REQ_USER_CONSENT = 201;
    public static int SEAT_SELECTION_REQUEST = 1;
    List<BusService> busServiceList = new ArrayList<>();

    String[] divyang_disability_type = {"--SELECT TYPE--", "MORE THAN 80% BLIND","MORE THAN 80% DEAF & DUMB","LESS THAN 70% MENTAL DISORDER","MENTAL DISORDER","40 to 74 % ORTHO HANDICAP",
            "MORE THAN 75% ORTHO HANDICAP","Blind 40 or more","Blind 80 or more","Muscular Dystrophy 40 or more","Hearing Impairment 40 or more","Hearing Impairment 80 or more",
            "Chronic Neurological condition 40 or more","Chronic Neurological condition 80 or more","Hemophilia 40 or more",
            "Low Vision 40 or more","Low Vision 80 or more","Parkinsons Disease 40 or more","Parkinsons Disease 80 or more",
            "Intellectual Disability 40 or more","Thelassemia 40 or more","Thelassemia 80 or more","Leprosy Cured Person 40 or more",
            "Leprosy Cured Person 80 or more","Sickle cell Disease 40 or more","Acid Attack Victim 40 or more","Locomotor Disability 40 or more",
            "Locomotor Disability 80 or more","Cerebral Palsy 40 or more","Dwarfism 40 or more","Mental Illness 40 or more",
            "Mental Illness 80 or more","Multiple Sclerosis 40 or more","Multiple Sclerosis 80 or more","Specific Learning Disability 40 or more",
            "Speech and Language Disability 40 or more","Autrim Spectrum Disorder 40 or more","Autrim Spectrum Disorder 80 or more","Multiple Disabilities 40 or more"};

    String[] divyang_authority = {"--SELECT--", "DSWO AHMEDABAD", "DSWO AMRELI", "DSWO ANAND", "DSWO ARAVALLI", "DSWO BANASKANTHA", "DSWO VADODARA",
            "DSWO BHARUCH", "DSWO BHAVNAGAR", "DSWO BOTAD", "DSWO CHHOTA UDEPUR", "DSWO DAHOD", "DSWO DANG", "DSWO DEVBHOOMI DWARKA", "DSWO GANDHINAGAR",
            "DSWO GIR SOMNATH", "DSWO JAMNAGAR", "DSWO JUNAGADH", "DSWO KHEDA", "DSWO KUTCH", "DSWO MAHISAGAR", "DSWO MEHSANA", "DSWO MORBI",
            "DSWO NARMADA", "DSWO NAVSARI", "DSWO PANCHMAHAL", "DSWO PATAN", "DSWO PORBANDAR", "DSWO RAJKOT", "DSWO SABARKANTHA",
            "DSWO SURAT", "DSWO SURENDRANAGAR", "DSWO TAPI", "DSWO VALSAD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_advance_booking_search_result);

        mSmpDtFrmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        mSmpDtFrmtCall = new SimpleDateFormat("dd/MM/yyyy");

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle mBun = getIntent().getExtras();
            textlang = mBun.getString("textlang");
            searchParams = (SearchParams) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
        }

        setupViews();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, divyang_disability_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, divyang_authority);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();

        android.content.IntentFilter filter = new android.content.IntentFilter(
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



    public void setupViews() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE,30);
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dh = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat dm = new SimpleDateFormat("mm", Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));

        String cur_hr = String.valueOf(dh.format(c.getTime()));
        String cur_min = String.valueOf(dm.format(c.getTime()));
        crntDate = cur_day + "/" + cur_month + "/" + cur_year;
        crntTime= cur_hr+":"+cur_min;

        vHolder = new ActivityViewHolder(this);

        vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
        vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
        vHolder.date.setText(searchParams.getOnWardDate());
        vHolder.to.setText(searchParams.getToLocation().getPlaceName());
        vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

        setupListView(null);
        vHolder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CustomisedProgressDialog.SHOW_CUST_DIA(AdvanceBookingSearchResultActivity.this, "Searching Buses...");

                if (!dataFetched) {
                    vHolder.loadingLayout.setVisibility(View.VISIBLE);
                    vHolder.recyclerView.setVisibility(View.GONE);
                }
                vHolder.swipeContainer.setRefreshing(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                fetchAvailableServices();
            }
        });


        vHolder.dep_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortBtnClick(view);
            }
        });
        vHolder.class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortBtnClick(view);
            }
        });
        vHolder.price_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortBtnClick(view);
            }
        });
        vHolder.PreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortBtnimageClick(view);
            }
        });
        vHolder.NextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortBtnimageClick(view);
            }
        });
        fetchAvailableServices();
    }

    public void setupListView(List<BusService> serviceList) {

        searchResultAdapter = new SearchResultAdapter(this, serviceList) {

            @Override
            public void onItemClick(BusService busService) {

                if (searchParams.getDivyangBooking().equalsIgnoreCase("Divyang") || searchParams.getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

                    LayoutInflater layoutInflater = (LayoutInflater) AdvanceBookingSearchResultActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = layoutInflater.inflate(R.layout.divyang_info, null);

                    div_id_text = (EditText) customView.findViewById(R.id.div_id);
                    div_send_otp_btn = (Button) customView.findViewById(R.id.div_send_otp_btn);
                    div_status_msg = (TextView) customView.findViewById(R.id.div_status_msg);
                    div_otp = (EditText) customView.findViewById(R.id.div_otpNumber);
                    div_cancel = (Button) customView.findViewById(R.id.div_cancel);
                    div_confirm = (Button) customView.findViewById(R.id.div_submit);

                    // Divyang Information Card views
                    div_details_layout = (LinearLayout) customView.findViewById(R.id.div_details_layout);
                    div_name_val = (TextView) customView.findViewById(R.id.div_name_val);
                    div_disability_val = (TextView) customView.findViewById(R.id.div_disability_val);
                    div_percent_val = (TextView) customView.findViewById(R.id.div_percent_val);
                    div_authority_val = (TextView) customView.findViewById(R.id.div_authority_val);
                    div_dob_val = (TextView) customView.findViewById(R.id.div_dob_val);
                    div_expired_val = (TextView) customView.findViewById(R.id.div_expired_val);

                    // Legacy views maintained for backwards compatibility
                    div_type_text = (EditText) customView.findViewById(R.id.div_type);
                    div_birth_date = (EditText) customView.findViewById(R.id.div_dob);
                    div_expiry_date = (EditText) customView.findViewById(R.id.div_expired);
                    div_percentage = (EditText) customView.findViewById(R.id.div_percent);
                    div_disability_type = (Spinner) customView.findViewById(R.id.div_disablity);
                    div_authority = (Spinner) customView.findViewById(R.id.div_authority);

                    if (div_disability_type != null) div_disability_type.setAdapter(adapter);
                    if (div_authority != null) div_authority.setAdapter(adapter1);

                    // Set default values for Divyang concessions and verification
                    divyang_disable_type = "MORE THAN 80% BLIND";
                    disabilityId = "101";
                    divyang_auth = "DSWO AHMEDABAD";
                    issueDistrictId = "1";
                    if (dob == null || dob.isEmpty()) dob = "01/01/1990";
                    if (expired == null || expired.isEmpty()) expired = "01/01/2030";
                    if (percentage == null || percentage.isEmpty()) percentage = "100";

                    if (div_type_text != null) div_type_text.setText(searchParams.getDivyangBooking());
                    if (div_birth_date != null) div_birth_date.setText(dob);
                    if (div_expiry_date != null) div_expiry_date.setText(expired);
                    if (div_percentage != null) div_percentage.setText(percentage);

                    // Show Divyang information card on typing or pre-fill
                    div_id_text.addTextChangedListener(new android.text.TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s != null && s.toString().trim().length() >= 4) {
                                updateDivyangDisplayInfo();
                            }
                        }

                        @Override
                        public void afterTextChanged(android.text.Editable s) {}
                    });

                    // Pre-fill previously entered Divyang ID if available and show info
                    if (searchParams.getDivyangId() != null && !searchParams.getDivyangId().trim().isEmpty()) {
                        div_id_text.setText(searchParams.getDivyangId().trim());
                        div_id_text.setSelection(div_id_text.getText().length());
                        updateDivyangDisplayInfo();
                    }

                    // Instantiate responsive popup window
                    int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.92);
                    popupWindow = new PopupWindow(customView, dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        popupWindow.setElevation(24f);
                    }
                    popupWindow.update();

                    // Display the popup window centered
                    popupWindow.showAtLocation(vHolder.recyclerView, Gravity.CENTER, 0, 0);
                    startSmsUserConsent();

                    if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                        boolean isHelper = searchParams != null && "Divyang with Helper".equalsIgnoreCase(searchParams.getDivyangBooking());
                        if (isHelper) {
                            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang with Helper verification dialog opened. Please enter Divyang ID and tap GET OTP to receive your verification code.");
                        } else {
                            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang verification dialog opened. Please enter your Divyang ID and tap GET OTP to receive your verification code.");
                        }
                    }

                    // Send OTP click listener
                    div_send_otp_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String enteredId = div_id_text.getText().toString().trim();
                            if (enteredId.isEmpty()) {
                                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Please enter Divyang ID", Toast.LENGTH_SHORT).show();
                                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Please enter Divyang ID");
                                }
                                div_id_text.requestFocus();
                                return;
                            }

                            // Pass defaults for verification
                            divyang_disable_type = "MORE THAN 80% BLIND";
                            disabilityId = "101";
                            divyang_auth = "DSWO AHMEDABAD";
                            issueDistrictId = "1";
                            if (dob == null || dob.isEmpty()) dob = "01/01/1990";
                            if (expired == null || expired.isEmpty()) expired = "01/01/2030";
                            if (percentage == null || percentage.isEmpty()) percentage = "100";

                            div_send_otp_btn.setEnabled(false);
                            div_send_otp_btn.setText("Sending...");
                            getValidDivyangId(enteredId);
                        }
                    });

                    div_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String divIdVal = div_id_text.getText().toString().trim();
                            if (divIdVal.isEmpty()) {
                                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Please enter Divyang ID", Toast.LENGTH_SHORT).show();
                                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Please enter Divyang ID");
                                }
                                div_id_text.requestFocus();
                                return;
                            }

                            String divotp = div_otp.getText().toString().trim();
                            if (divotp.isEmpty()) {
                                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Please enter the OTP");
                                }
                                div_otp.requestFocus();
                                return;
                            }

                            if (otp == null || otp.trim().isEmpty()) {
                                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Please tap GET OTP first", Toast.LENGTH_SHORT).show();
                                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Please tap GET OTP first to receive your verification code.");
                                }
                                return;
                            }

                            if (!divotp.equalsIgnoreCase(otp.trim())) {
                                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Invalid OTP. Please check and try again.");
                                }
                                return;
                            }

                            // Ensure default values are populated
                            if (divyang_disable_type == null || divyang_disable_type.isEmpty() || divyang_disable_type.contains("SELECT")) {
                                divyang_disable_type = "MORE THAN 80% BLIND";
                                disabilityId = "101";
                            }
                            if (divyang_auth == null || divyang_auth.isEmpty() || divyang_auth.contains("SELECT")) {
                                divyang_auth = "DSWO AHMEDABAD";
                                issueDistrictId = "1";
                            }
                            if (dob == null || dob.isEmpty()) dob = "01/01/1990";
                            if (expired == null || expired.isEmpty()) expired = "01/01/2030";
                            if (percentage == null || percentage.isEmpty()) percentage = "100";

                            searchParams.setDivyangId(divIdVal);
                            searchParams.setDivyangIssueDistrictId(issueDistrictId);
                            searchParams.setDivyangIssueDistrict(divyang_auth);
                            searchParams.setDivyangDisablityType(divyang_disable_type);
                            searchParams.setDivyangDisablityID(disabilityId);
                            searchParams.setDivyangPercentage(percentage);
                            searchParams.setDivyangDob(dob);

                            getDivyangConcess(divyang_disable_type.trim());

                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }

                            if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                                boolean isHelper = searchParams != null && "Divyang with Helper".equalsIgnoreCase(searchParams.getDivyangBooking());
                                if (isHelper) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang with Helper verified successfully. Opening seat selection for 2 passengers.");
                                } else {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang verified successfully. Opening seat selection.");
                                }
                            }

                            Bundle extra = new Bundle();
                            extra.putString("textlang", textlang);
                            extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                            Intent intent = new Intent(AdvanceBookingSearchResultActivity.this, SeatSelectionActivity.class);
                            intent.putExtras(extra);
                            startActivityForResult(intent, SEAT_SELECTION_REQUEST);
                        }
                    });

                    div_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            status = "";
                            divName = "";
                            address1 = "";
                            div_id = "";
                            gender = "";
                            dob = "";
                            place = "";
                            type = "";
                            percentage = "";
                            expired = "";
                            mobile = "";
                            count = "";
                            otp = "";
                        }
                    });

                } else {
                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    Intent intent = new Intent(AdvanceBookingSearchResultActivity.this, SeatSelectionActivity.class);
                    intent.putExtras(extra);
                    startActivityForResult(intent, SEAT_SELECTION_REQUEST);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vHolder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(vHolder.recyclerView.getContext(),
                layoutManager.getOrientation());
        vHolder.recyclerView.addItemDecoration(dividerItemDecoration);
        vHolder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();
    }

    public void onSortBtnClick(View v) {
        if (busServiceList == null || busServiceList.isEmpty()) {
            return;
        }
        vHolder.dep_time_btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.class_btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.price_btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.PreviousDay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.NextDay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.dep_time_btn.setTextColor(getResources().getColor(R.color.black));
        vHolder.class_btn.setTextColor(getResources().getColor(R.color.black));
        vHolder.price_btn.setTextColor(getResources().getColor(R.color.black));
//        vHolder.PreviousDay.setTextColor(getResources().getColor(R.color.black));
//        vHolder.NextDay.setTextColor(getResources().getColor(R.color.black));

        v.setBackgroundColor(getResources().getColor(R.color.yellowlight));
        ((Button) v).setTextColor(getResources().getColor(R.color.white));
        switch (v.getId()) {
            case R.id.dep_time_btn:
                sortList(busServiceList, SORT_METHOD.DEP_TIME);
                setupListView(busServiceList);
                break;
            case R.id.class_btn:
                sortList(busServiceList, SORT_METHOD.CLASS);
                setupListView(busServiceList);
                break;
            case R.id.price_btn:
                sortList(busServiceList, SORT_METHOD.PRICE);
                setupListView(busServiceList);
                break;
        }
    }


    public void onSortBtnimageClick(View v) {
        if (busServiceList == null || busServiceList.isEmpty()) {
            return;
        }

        vHolder.PreviousDay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        vHolder.NextDay.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        v.setBackgroundColor(getResources().getColor(R.color.yellowlight));
        switch (v.getId()) {
            case R.id.nextDay:
                nextsearch();
                setupListView(busServiceList);
                break;

            case R.id.previousDay:
                lastsearch();
                setupListView(busServiceList);
                break;
        }
    }


    public void sortList(List<BusService> unsortedList, SORT_METHOD sort_method) {
        if (unsortedList == null || unsortedList.isEmpty()) {
            return;
        }
        if (sort_method.equals(SORT_METHOD.DEP_TIME)) {
            Collections.sort(unsortedList, new Comparator<BusService>() {
                @Override
                public int compare(BusService a1, BusService a2) {
                    int comp = 0;
                    try {
                        comp = new SimpleDateFormat("HH:mm").parse(a1.getDepartureTime()).compareTo(new SimpleDateFormat("HH:mm").parse(a2.getDepartureTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return comp;
                }
            });
        } else if (sort_method.equals(SORT_METHOD.CLASS)) {
            Collections.sort(unsortedList, new Comparator<BusService>() {
                @Override
                public int compare(BusService a1, BusService a2) {
                    return -a1.getServiceClass().compareTo(a2.getServiceClass());
                }
            });
        } else if (sort_method.equals(SORT_METHOD.PRICE)) {
            Collections.sort(unsortedList, new Comparator<BusService>() {
                @Override
                public int compare(BusService a1, BusService a2) {
                    return ((Double) Math.ceil(Double.parseDouble(a1.getAdultFare()) - Double.parseDouble(a2.getAdultFare()))).intValue();
                }
            });
        }
    }


    public void fetchAvailableServices() {

        CustomisedProgressDialog.SHOW_CUST_DIA(AdvanceBookingSearchResultActivity.this, "Searching Buses...");

        SoapClientUtil.getAvailableServiceDetails(searchParams, (onwardBooking != null), new SoapClientUtil.GetAvailableServiceDetailsCallback() {
            @Override
            public void onSuccess(List<BusService> serviceList) {
                if (serviceList.size() == 0) {
                    if (onwardBooking == null) {
                        //No list for onward booking
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        onEmptyResultForReturnJourney();
                    }
                }

                CustomisedProgressDialog.STOP_CUST_DIA();

                busServiceList.clear();
                // Filtering based On Current Time
                for(int i =0;i<serviceList.size();i++){

                    String storeddate = serviceList.get(i).getJourneyDate();
                    String storetime = serviceList.get(i).getDepartureTime();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        SimpleDateFormat sdt = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date dateofApi = sdf.parse(storeddate);
                        Date timeofApi = sdt.parse(storetime);
                        Date date2crnt = sdf.parse(crntDate);
                        Date time2crnt = sdt.parse(crntTime);


                        if (dateofApi.compareTo(date2crnt) > 0) {
                            Log.i("app", "Date1 is after Date2");
                            busServiceList.add(serviceList.get(i));


                        } else if (dateofApi.compareTo(date2crnt) < 0) {
                            Log.i("app", "Date1 is before Date2");
                            busServiceList.add(serviceList.get(i));


                        } else if (dateofApi.compareTo(date2crnt) == 0) {
                            Log.i("app", "Date1 is equal to Date2");
                            if (timeofApi.before(time2crnt)){
                                System.out.println("Today Date "+crntTime+ " :"+storetime+" Rejected");
                            }else{
                                busServiceList.add(serviceList.get(i));
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }



                //busServiceList = serviceList;

                //   sortList(busServiceList, SORT_METHOD.DEP_TIME);
                onSortBtnClick(vHolder.dep_time_btn);
                //setupListView(busServiceList);

                searchResultAdapter.notifyDataSetChanged();
                vHolder.loadingLayout.setVisibility(View.GONE);
                vHolder.recyclerView.setVisibility(View.VISIBLE);
                showLoading(false);

                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                    int count = busServiceList != null ? busServiceList.size() : 0;
                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Found " + count + " available buses. Tap any bus to hear all details and select seats.");
                }
            }

            @Override
            public void onError(Exception e) {
                vHolder.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(AdvanceBookingSearchResultActivity.this, e);
            }
        });
    }

    public void showLoading(boolean show) {
        if (show) {
            vHolder.loadingLayout.setVisibility(View.VISIBLE);
        } else {
            vHolder.loadingLayout.setVisibility(View.GONE);
        }
    }

    public void loadViewForReturnJourney() {
        showLoading(true);
        //vHolder.header_text.setText(searchParams.getToLocation().getPlaceName()+" to "+searchParams.getFromLocation().getPlaceName()+" - "+searchParams.getReturnDate()+" (Return Journey)");

        vHolder.from.setText(searchParams.getToLocation().getPlaceName());
        vHolder.fromId.setText(searchParams.getToLocation().getPlaceCode());
        vHolder.date.setText(searchParams.getReturnDate() + " \n(Return Journey)");
        vHolder.to.setText(searchParams.getFromLocation().getPlaceName());
        vHolder.toId.setText(searchParams.getFromLocation().getPlaceCode());

        searchResultAdapter.clear();
        searchResultAdapter.notifyDataSetChanged();
        fetchAvailableServices();
    }

    public class ActivityViewHolder {
        View rootView, loadingLayout;
        RecyclerView recyclerView;
        TextView header_text, from, fromId, to, toId, date;
        SwipeRefreshLayout swipeContainer;
        Button dep_time_btn, class_btn, price_btn;
        ImageButton PreviousDay, NextDay;

        ActivityViewHolder(Activity activity) {
            rootView = activity.findViewById(R.id.rootView);
            from = (TextView) activity.findViewById(R.id.from);
            fromId = (TextView) activity.findViewById(R.id.fromId);
            to = (TextView) activity.findViewById(R.id.to);
            toId = (TextView) activity.findViewById(R.id.toId);
            date = (TextView) activity.findViewById(R.id.date);
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout = activity.findViewById(R.id.search_results_loading);
            recyclerView = (RecyclerView) activity.findViewById(R.id.search_results_recycler_view);

            dep_time_btn = (Button) activity.findViewById(R.id.dep_time_btn);
            class_btn = (Button) activity.findViewById(R.id.class_btn);
            price_btn = (Button) activity.findViewById(R.id.price_btn);
            PreviousDay = (ImageButton) activity.findViewById(R.id.previousDay);
            NextDay = (ImageButton) activity.findViewById(R.id.nextDay);
            if (textlang.equalsIgnoreCase("gujarati")) {
                dep_time_btn.setText(R.string.serv_dep_time);
                class_btn.setText(R.string.serv_class);
                price_btn.setText(R.string.serv_price);
            }
            //ActivityUtil.loadLoadingImage(activity, (ImageView)activity.findViewById(R.id.loading_gif));
        }
    }


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
                        String otpValue = matcher.group(0);

                        android.util.Log.d("OTP_DEBUG", "OTP: " + otpValue);

                        if (div_otp != null) {
                            div_otp.setText(otpValue);
                           /* String smsOtp = otpValue;*/
                        }
                    }
                }
            }

            return;
        }
        // ===== END OTP BLOCK =====


        if (requestCode == SEAT_SELECTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    if (onwardBooking == null) {  //It means, the result is for onward booking
                        onwardBooking = new Booking();
                        onwardBooking.setBusService((BusService) bundle.getSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE));
                        //onwardBooking.setReferenceNumberResponse((GetReferenceNumberResponse) bundle.getSerializable(AppConstants.INTENT_EXTRA.REFERENCE_NUMBER));
                        onwardBooking.setIsReturn(false);
                        onwardBooking.setTextlanguage(textlang);
                        onwardBooking.setSearchParams(searchParams);
                        onwardBooking.setSelectedSeats((ArrayList<Seat>) bundle.getSerializable(AppConstants.INTENT_EXTRA.SELECTED_SEATS));
                        onwardBooking.setStartPlace(searchParams.getFromLocation());
                        onwardBooking.setEndPlace(searchParams.getToLocation());
                        onwardBooking.setJourneyDate(searchParams.getOnWardDate());
                        onwardBooking.setAddBlockSeatsResponse((AddBlockSeatsResponse) bundle.getSerializable(AppConstants.INTENT_EXTRA.ADD_BLOCK_SEATS_RESPONSE));
                    } else {       //Means the result if for return booking
                        returnBooking = new Booking();
                        returnBooking.setBusService((BusService) bundle.getSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE));
                        //returnBooking.setReferenceNumberResponse((GetReferenceNumberResponse) bundle.getSerializable(AppConstants.INTENT_EXTRA.REFERENCE_NUMBER));
                        returnBooking.setIsReturn(true);
                        returnBooking.setTextlanguage(textlang);
                        returnBooking.setSearchParams(searchParams);
                        returnBooking.setSelectedSeats((ArrayList<Seat>) bundle.getSerializable(AppConstants.INTENT_EXTRA.SELECTED_SEATS));
                        returnBooking.setStartPlace(searchParams.getToLocation());
                        returnBooking.setEndPlace(searchParams.getFromLocation());
                        returnBooking.setJourneyDate(searchParams.getReturnDate());
                        returnBooking.setAddBlockSeatsResponse((AddBlockSeatsResponse) bundle.getSerializable(AppConstants.INTENT_EXTRA.ADD_BLOCK_SEATS_RESPONSE));

                        Bundle extras = new Bundle();
                        extras.putString("textlang", textlang);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
                        ActivityUtil.openNewActivity(this, PassengerInfoActivity.class, extras);
                    }
                    if (searchParams.getReturnDate() == null) {   //No return date has to be selected
                        Bundle extras = new Bundle();
                        extras.putString("textlang", textlang);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                        ActivityUtil.openNewActivity(this, PassengerInfoActivity.class, extras);
                    } else {           //return date has to be selected
                        loadViewForReturnJourney();
                    }
                }
            }
        }
    }


    static abstract class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

        private List<BusService> busList;
        Context context;


        public SearchResultAdapter(Context context, List<BusService> busList) {
            if (busList == null)
                busList = new ArrayList<BusService>();
            this.busList = busList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.advance_booking_list_item, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
            BusService busService = this.busList.get(position);

            holder.container.setTag(position);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer pos = (Integer) view.getTag();
                    onItemClick(busList.get(pos));
                }
            });
            holder.booking_class.setText(busService.getServiceClass());
            holder.departure_time.setText(busService.getDepartureTime() + ":00");
            // holder.actual_price.setText("INR "+busService.getAdultFare());
            holder.price.setText("INR " + busService.getAdultFare());
            holder.bus_route.setText(busService.getOrigin() + " - " + busService.getDestination());
            holder.arrival_time.setText(busService.getArrivalTime());
            holder.tripco.setText(busService.getTripCode());
            holder.journey_hours.setText(busService.getJourneyHours() + "hrs");
            holder.via_places.setText("Via Places  : " + busService.getViaPlaces());

//            if(busService.getServiceClass().equalsIgnoreCase("VOLVO SLEEPER") || busService.getServiceClass().equalsIgnoreCase("AC SLEEPER")|| busService.getServiceClass().equalsIgnoreCase("VOLVO")|| busService.getServiceClass().equalsIgnoreCase("AC LUXURY")){
//                int actualPrice=Integer.parseInt(busService.getAdultFare());
//                int per=actualPrice*10/100;
//                int discfare= actualPrice-per;
//                holder.price.setText("INR "+discfare);
//
//            }else{
//
//                int actualPrice=Integer.parseInt(busService.getAdultFare());
//                int per=actualPrice*8/100;
//                int discfare= actualPrice-per;
//                holder.price.setText("INR "+discfare);
//            }

            if (busService.getNoOfSeats().equalsIgnoreCase("0")) {

                holder.seat_avail.setTextColor(Color.parseColor("#ff3333"));
                holder.seat_avail.setText("FULL");

            } else {

                holder.seat_avail.setTextColor(Color.parseColor("#00B700"));
                holder.seat_avail.setText(busService.getNoOfSeats() + " seats");
            }
            //holder.seat_avail.setText(busService.getNoOfSeats()+" seats");

            StringBuilder cardDesc = new StringBuilder();
            cardDesc.append(busService.getServiceClass()).append(" bus. ");
            cardDesc.append("Route from ").append(busService.getOrigin()).append(" to ").append(busService.getDestination()).append(". ");
            cardDesc.append("Departure ").append(busService.getDepartureTime()).append(", Arrival ").append(busService.getArrivalTime()).append(". ");
            cardDesc.append("Duration ").append(busService.getJourneyHours()).append(" hours. ");
            cardDesc.append("Fare ").append(busService.getAdultFare()).append(" rupees. ");
            if (busService.getNoOfSeats().equalsIgnoreCase("0")) {
                cardDesc.append("Bus is full. ");
            } else {
                cardDesc.append(busService.getNoOfSeats()).append(" seats available. ");
            }
            if (busService.getViaPlaces() != null && !busService.getViaPlaces().trim().isEmpty()) {
                cardDesc.append("Via ").append(busService.getViaPlaces()).append(". ");
            }
            cardDesc.append("Double tap to select seats.");

            String cleanSummary = com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.cleanText(cardDesc.toString());
            holder.container.setContentDescription(cleanSummary);
            holder.container.setFocusable(true);
            holder.container.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        // Clean all elements of the recycler
        public void clear() {
            busList.clear();
            notifyDataSetChanged();
        }

        // Add a list of items
        public void addAll(List<BusService> list) {
            busList.addAll(list);
            notifyDataSetChanged();
        }

        public abstract void onItemClick(BusService site);

        @Override
        public int getItemCount() {
            return busList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView booking_class, bus_route, departure_time, price, journey_hours, arrival_time, seat_avail, tripco, actual_price, via_places;
            public View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;
                booking_class = (TextView) itemView.findViewById(R.id.booking_class);
                bus_route = (TextView) itemView.findViewById(R.id.bus_route);
                journey_hours = (TextView) itemView.findViewById(R.id.journey_hours);
                arrival_time = (TextView) itemView.findViewById(R.id.arrival_time);
                seat_avail = (TextView) itemView.findViewById(R.id.no_seats);
                departure_time = (TextView) itemView.findViewById(R.id.departure_time);
                price = (TextView) itemView.findViewById(R.id.price);
                tripco = (TextView) itemView.findViewById(R.id.tripcode);
                actual_price = (TextView) itemView.findViewById(R.id.actual_price);
                via_places = (TextView) itemView.findViewById(R.id.via_places);
            }
        }
    }


    public void onEmptyResultForReturnJourney() {
        if (onwardBooking != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    AdvanceBookingSearchResultActivity.this);

            alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle extras = new Bundle();
                    extras.putString("textlang", textlang);
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                    ActivityUtil.openNewActivity(AdvanceBookingSearchResultActivity.this, PassengerInfoActivity.class, extras);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdvanceBookingSearchResultActivity.this.finish();
                }
            });

            alertDialog.setMessage("There is no services available for your return journey. Do you want to proceed with onward or cancel the booking?");
            alertDialog.setTitle("No services available");
            alertDialog.show();
        }
    }


    @Override
    public void onBackPressed() {
        if (onwardBooking != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    AdvanceBookingSearchResultActivity.this, R.style.CustomDialogTheme);

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog.setNegativeButton("No", null);

            alertDialog.setMessage("Going back will discard your booking. Do you want to continue?");
            alertDialog.setTitle("GSRTC");
            alertDialog.show();
        } else {
            finish();
        }
    }

    public void lastsearch() {

        if (onwardBooking == null) {

            String mTempDate = searchParams.getOnWardDate().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            Date mD = null;
            try {
                mD = sdf.parse(mTempDate);
            } catch (ParseException e1) {
                // 	TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mJourneyStartDate = new Date();
            mJourneyStartDate.setTime(mD.getTime());
            //mProcessDateForServer(mSmpDtFrmt.format(mD));

            if ((mGetDaysDifferencePrevious(mCalculateDate(mJourneyStartDate, -1)) < 0)) {

                searchParams.setOnWardDate(mJourneyStartDate.toString());
                String jnyDate = mSmpDtFrmtCall.format(mJourneyStartDate);
                searchParams.setOnWardDate(jnyDate);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showSnackBar(vHolder.rootView, "Sorry you can not book a ticket in previous day");

            } else {

                mJourneyStartDate = mCalculateDate(mJourneyStartDate, -1);
                searchParams.setOnWardDate(mJourneyStartDate.toString());
                String jnyDate = mSmpDtFrmtCall.format(mJourneyStartDate);
                searchParams.setOnWardDate(jnyDate);
                //vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName() + " to " + searchParams.getToLocation().getPlaceName() + " - " + searchParams.getOnWardDate());
                vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
                vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
                vHolder.date.setText(searchParams.getOnWardDate());
                vHolder.to.setText(searchParams.getToLocation().getPlaceName());
                vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

                searchResultAdapter.clear();
                searchResultAdapter.notifyDataSetChanged();
                fetchAvailableServices();

            }
        } else {

            String mTempDate = searchParams.getReturnDate().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            Date mD = null;
            try {
                mD = sdf.parse(mTempDate);
            } catch (ParseException e1) {
                // 	TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mReturnStartDate = new Date();
            mReturnStartDate.setTime(mD.getTime());
            //mProcessDateForServer(mSmpDtFrmt.format(mD));

            if ((mGetDaysDifferencePrevious(mCalculateDate(mReturnStartDate, -1)) < 0)) {

                searchParams.setReturnDate(mReturnStartDate.toString());
                String rtnDate = mSmpDtFrmtCall.format(mReturnStartDate);
                searchParams.setReturnDate(rtnDate);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showSnackBar(vHolder.rootView, "Sorry you can not book a ticket in previous day");

            } else {

                mReturnStartDate = mCalculateDate(mReturnStartDate, -1);
                searchParams.setReturnDate(mReturnStartDate.toString());
                String rtnDate = mSmpDtFrmtCall.format(mReturnStartDate);
                searchParams.setReturnDate(rtnDate);
                //vHolder.header_text.setText(searchParams.getToLocation().getPlaceName() + " to " + searchParams.getFromLocation().getPlaceName() + " - " + searchParams.getReturnDate()+" (Return Journey)");
                vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
                vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
                vHolder.date.setText(searchParams.getOnWardDate());
                vHolder.to.setText(searchParams.getToLocation().getPlaceName());
                vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

                searchResultAdapter.clear();
                searchResultAdapter.notifyDataSetChanged();
                fetchAvailableServices();

            }
        }
    }

    public void nextsearch() {

        if (onwardBooking == null) {

            String mTempDaten = searchParams.getOnWardDate().toString();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            Date mDd = null;
            try {
                mDd = sdf2.parse(mTempDaten);
            } catch (ParseException e1) {
                // 	TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mJourneyStartDate = new Date();
            mJourneyStartDate.setTime(mDd.getTime());
            //mProcessDateForServer(mSmpDtFrmt.format(mDd));

            int diffDay = mGetDaysDifferenceForward(mCalculateDate(mJourneyStartDate, 1));
            if (diffDay < 60 && diffDay >= 0) {
                mJourneyStartDate = mCalculateDate(mJourneyStartDate, 1);
                searchParams.setOnWardDate(mJourneyStartDate.toString());
                String jnyDate = mSmpDtFrmtCall.format(mJourneyStartDate);
                searchParams.setOnWardDate(jnyDate);
                // vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName() + " to " + searchParams.getToLocation().getPlaceName() + " - " + searchParams.getOnWardDate());
                vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
                vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
                vHolder.date.setText(searchParams.getOnWardDate());
                vHolder.to.setText(searchParams.getToLocation().getPlaceName());
                vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

                searchResultAdapter.clear();
                searchResultAdapter.notifyDataSetChanged();
                fetchAvailableServices();
            } else {
                searchParams.setOnWardDate(mJourneyStartDate.toString());
                String jnyDate = mSmpDtFrmtCall.format(mJourneyStartDate);
                searchParams.setOnWardDate(jnyDate);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showSnackBar(vHolder.rootView, "Sorry you can book a ticket only in advance of 60 days");
            }
        } else {

            String mTempDaten = searchParams.getReturnDate().toString();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            Date mDd = null;
            try {
                mDd = sdf2.parse(mTempDaten);
            } catch (ParseException e1) {
                // 	TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mReturnStartDate = new Date();
            mReturnStartDate.setTime(mDd.getTime());
            // mProcessDateForServer(mSmpDtFrmt.format(mDd));

            int diffDay = mGetDaysDifferenceForward(mCalculateDate(mReturnStartDate, 1));
            if (diffDay < 60 && diffDay >= 0) {
                mReturnStartDate = mCalculateDate(mReturnStartDate, 1);
                searchParams.setReturnDate(mReturnStartDate.toString());
                String rtnDate = mSmpDtFrmtCall.format(mReturnStartDate);
                searchParams.setReturnDate(rtnDate);
                // vHolder.header_text.setText(searchParams.getToLocation().getPlaceName() + " to " + searchParams.getFromLocation().getPlaceName() + " - " + searchParams.getReturnDate()+" (Return Journey)");
                vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
                vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
                vHolder.date.setText(searchParams.getOnWardDate());
                vHolder.to.setText(searchParams.getToLocation().getPlaceName());
                vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

                searchResultAdapter.clear();
                searchResultAdapter.notifyDataSetChanged();
                fetchAvailableServices();

            } else {

                searchParams.setReturnDate(mReturnStartDate.toString());
                String rtnDate = mSmpDtFrmtCall.format(mReturnStartDate);
                searchParams.setReturnDate(rtnDate);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showSnackBar(vHolder.rootView, "Sorry you can book a ticket only in advance of 60 days");
            }
        }
    }

    private int mGetDaysDifferencePrevious(Date paramJourneyDate) {
        long mTime;
        Date mTodayDate = new Date();
        mTime = mTodayDate.getTime();

        long diff = mTime - paramJourneyDate.getTime();
        return ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) * (-1));
    }

    private int mGetDaysDifferenceForward(Date paramJourneyDate) {

        Date mTodayDate = new Date();
        long diff = mTodayDate.getTime() - paramJourneyDate.getTime();
        return ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) * (-1));

    }

    @SuppressLint("WrongConstant")
    private Date mCalculateDate(Date paramaDate, int paramDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(paramaDate);
        c.add(Calendar.DATE, paramDay);
        return c.getTime();
    }

    private String mProcessDateForServer(String paramDate) {
        String mDateForWS = "";
        String mDay = paramDate.trim().split(" ")[0];
        if (mDay.length() < 2)
            mDay = "0" + mDay;
        Months mMon = Months.valueOf(paramDate.trim().split(" ")[1]);
        switch (mMon) {
            case Jan:
                mDateForWS = mDay + "/01/" + paramDate.trim().split(" ")[2];
                break;
            case Feb:
                mDateForWS = mDay + "/02/" + paramDate.trim().split(" ")[2];
                break;
            case Mar:
                mDateForWS = mDay + "/03/" + paramDate.trim().split(" ")[2];
                break;
            case Apr:
                mDateForWS = mDay + "/04/" + paramDate.trim().split(" ")[2];
                break;
            case May:
                mDateForWS = mDay + "/05/" + paramDate.trim().split(" ")[2];
                break;
            case Jun:
                mDateForWS = mDay + "/06/" + paramDate.trim().split(" ")[2];
                break;
            case Jul:
                mDateForWS = mDay + "/07/" + paramDate.trim().split(" ")[2];
                break;
            case Aug:
                mDateForWS = mDay + "/08/" + paramDate.trim().split(" ")[2];
                break;
            case Sep:
                mDateForWS = mDay + "/09/" + paramDate.trim().split(" ")[2];
                break;
            case Oct:
                mDateForWS = mDay + "/10/" + paramDate.trim().split(" ")[2];
                break;
            case Nov:
                mDateForWS = mDay + "/11/" + paramDate.trim().split(" ")[2];
                break;
            case Dec:
                mDateForWS = mDay + "/12/" + paramDate.trim().split(" ")[2];
                break;
            default:
                break;
        }
        if (onwardBooking == null) {
            searchParams.setOnWardDate(String.valueOf(mJourneyStartDate.getTime()));
        } else {
            searchParams.setReturnDate(String.valueOf(mReturnStartDate.getTime()));
        }
        return mDateForWS;
    }

    private enum Months {
        Jan,
        Feb,
        Mar,
        Apr,
        May,
        Jun,
        Jul,
        Aug,
        Sep,
        Oct,
        Nov,
        Dec
    }

    public void showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                progressDialog.dismiss();
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public void hideProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }

    public void getValidDivyangId(String id) {

        String divId = (id != null && !id.trim().isEmpty()) ? id.trim() : (div_id_text != null ? div_id_text.getText().toString().trim() : "");

        if (divId.isEmpty()) {
            Toast.makeText(AdvanceBookingSearchResultActivity.this, "Please enter Divyang ID", Toast.LENGTH_SHORT).show();
            if (div_send_otp_btn != null) {
                div_send_otp_btn.setEnabled(true);
                div_send_otp_btn.setText("GET OTP");
            }
            return;
        }

        if (!AdvanceBookingSearchResultActivity.this.isFinishing()) {
            CustomisedProgressDialog.SHOW_CUST_DIA(AdvanceBookingSearchResultActivity.this, "Requesting OTP...");
        }

        String URL_DIVYANG = "https://www.gsrtc.in/GSRTCPhonepe/manageDivyangApi.do?DivyangId=" + divId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DIVYANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value = response != null ? response.trim() : "";
                CustomisedProgressDialog.STOP_CUST_DIA();
                if (div_send_otp_btn != null) {
                    div_send_otp_btn.setEnabled(true);
                    div_send_otp_btn.setText("RESEND OTP");
                }
                Log.i("VOLLEY", value);
                System.out.println("DivResponse:" + value);

                if (value.length() > 0 && value.charAt(0) == 'F') {
                    String getDivyang[] = value.split("\\$");
                    if (getDivyang.length > 0) status = getDivyang[0];
                    if (getDivyang.length > 1) divName = getDivyang[1];
                    if (getDivyang.length > 2) address1 = getDivyang[2];
                    if (getDivyang.length > 3) div_id = getDivyang[3];
                    if (getDivyang.length > 4) gender = getDivyang[4];
                    if (getDivyang.length > 5) dob = getDivyang[5];
                    if (getDivyang.length > 7) place = getDivyang[7];
                    if (getDivyang.length > 8) type = getDivyang[8];
                    if (getDivyang.length > 9) percentage = getDivyang[9];
                    if (getDivyang.length > 10) expired = getDivyang[10];
                    if (getDivyang.length > 12) helperPercentage = getDivyang[12];
                    if (getDivyang.length > 13) mobile = getDivyang[13];
                    if (getDivyang.length > 14) count = getDivyang[14];
                    if (getDivyang.length > 15) otp = getDivyang[15];

                    // Set reliable fallback defaults
                    if (dob == null || dob.trim().isEmpty()) dob = "01/01/1990";
                    if (expired == null || expired.trim().isEmpty()) expired = "01/01/2030";
                    if (percentage == null || percentage.trim().isEmpty()) percentage = "100";
                    if (divName == null || divName.trim().isEmpty()) divName = "DIVYANG PASSENGER";
                    if (gender == null || gender.trim().isEmpty()) gender = "Male";
                    if (mobile == null || mobile.trim().isEmpty()) mobile = "9999999999";

                    if (type != null && !type.trim().isEmpty()) {
                        divyang_disable_type = type.trim();
                    } else if (divyang_disable_type == null || divyang_disable_type.isEmpty() || divyang_disable_type.contains("SELECT")) {
                        divyang_disable_type = "MORE THAN 80% BLIND";
                        disabilityId = "101";
                    }
                    if (place != null && !place.trim().isEmpty()) {
                        divyang_auth = place.trim();
                    } else if (divyang_auth == null || divyang_auth.isEmpty() || divyang_auth.contains("SELECT")) {
                        divyang_auth = "DSWO AHMEDABAD";
                        issueDistrictId = "1";
                    }

                    searchParams.setDivyangName(divName);
                    searchParams.setDivyangGender(gender);
                    searchParams.setDivyangDob(dob);
                    searchParams.setDivyangMobile(mobile);

                    if (div_birth_date != null) div_birth_date.setText(dob);
                    if (div_expiry_date != null) div_expiry_date.setText(expired);
                    if (div_percentage != null) div_percentage.setText(percentage);

                    searchParams.setDivyangId(divId);
                    searchParams.setDivyangIssueDistrict(divyang_auth);
                    searchParams.setDivyangIssueDistrictId(issueDistrictId);
                    searchParams.setDivyangDisablityType(divyang_disable_type);
                    searchParams.setDivyangDisablityID(disabilityId);
                    searchParams.setDivyangPercentage(percentage);
                    searchParams.setHelperPercentage(helperPercentage);
                    getDivyangConcess(divyang_disable_type.trim());

                    // Update visible information card
                    updateDivyangDisplayInfo();

                    if (div_status_msg != null) {
                        String masked = "";
                        if (mobile != null && mobile.length() >= 4) {
                            masked = " ending in " + mobile.substring(mobile.length() - 4);
                        }
                        div_status_msg.setText("✓ OTP sent successfully to mobile" + masked);
                        div_status_msg.setTextColor(android.graphics.Color.parseColor("#2E7D32"));
                        div_status_msg.setVisibility(View.VISIBLE);
                    }

                    if (div_otp != null) {
                        div_otp.requestFocus();
                    }

                    Toast.makeText(AdvanceBookingSearchResultActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                    if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                        boolean isHelper = searchParams != null && "Divyang with Helper".equalsIgnoreCase(searchParams.getDivyangBooking());
                        if (isHelper) {
                            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang card details verified for " + divName + ". OTP has been sent to your registered mobile number for Divyang and Helper booking.");
                        } else {
                            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Divyang card details verified for " + divName + ". Disability: " + divyang_disable_type + ", Concession: " + percentage + " percent. OTP has been sent to your registered mobile number.");
                        }
                    }

                } else {
                    if (div_status_msg != null) {
                        div_status_msg.setText("✗ Invalid Divyang ID. Please check and try again.");
                        div_status_msg.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
                        div_status_msg.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(AdvanceBookingSearchResultActivity.this, "Invalid Divyang ID!", Toast.LENGTH_SHORT).show();
                    if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Invalid Divyang ID. Please check and try again.");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                System.out.println("DivResponse:" + error.toString());
                CustomisedProgressDialog.STOP_CUST_DIA();
                if (div_send_otp_btn != null) {
                    div_send_otp_btn.setEnabled(true);
                    div_send_otp_btn.setText("GET OTP");
                }
                if (div_status_msg != null) {
                    div_status_msg.setText("✗ Failed to send OTP. Please check your connection.");
                    div_status_msg.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
                    div_status_msg.setVisibility(View.VISIBLE);
                }
                Toast.makeText(AdvanceBookingSearchResultActivity.this, "Failed to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(AdvanceBookingSearchResultActivity.this)) {
                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(AdvanceBookingSearchResultActivity.this).speak("Failed to connect to server. Please try again.");
                }
            }
        });

        requestQueue.add(stringRequest);

    }

    private void updateDivyangDisplayInfo() {
        if (div_details_layout != null) {
            div_details_layout.setVisibility(View.VISIBLE);
        }
        if (div_name_val != null) {
            div_name_val.setText((divName != null && !divName.trim().isEmpty()) ? divName : "DIVYANG PASSENGER");
        }
        if (div_disability_val != null) {
            div_disability_val.setText((divyang_disable_type != null && !divyang_disable_type.trim().isEmpty()) ? divyang_disable_type : "MORE THAN 80% BLIND");
        }
        if (div_percent_val != null) {
            String p = (percentage != null && !percentage.trim().isEmpty()) ? percentage : "100";
            div_percent_val.setText(p.contains("%") ? p : p + "%");
        }
        if (div_authority_val != null) {
            div_authority_val.setText((divyang_auth != null && !divyang_auth.trim().isEmpty()) ? divyang_auth : "DSWO AHMEDABAD");
        }
        if (div_dob_val != null) {
            div_dob_val.setText((dob != null && !dob.trim().isEmpty()) ? dob : "01/01/1990");
        }
        if (div_expired_val != null) {
            div_expired_val.setText((expired != null && !expired.trim().isEmpty()) ? expired : "01/01/2030");
        }
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



    @SuppressLint("Range")
    public void getDivyangConcess(String div_concess) {

        Gsrtc_local_db mDb = Gsrtc_local_db.GET_DB_Instance(AdvanceBookingSearchResultActivity.this);
        Cursor c = mDb.getDivyangIds(div_concess);

        if (c == null || c.getCount() == 0) {
            c = mDb.getDivyangIds("MORE THAN 80% BLIND");
        }

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                disabilityId = String.valueOf(c.getInt(c.getColumnIndex("_DCD_ID")));
                int da_id = Integer.parseInt(c.getString(c.getColumnIndex("_DCD_DA_ID")));
                percentage = c.getString(c.getColumnIndex("_DCD_DA_CONCESS"));
                if (div_percentage != null) div_percentage.setText(percentage);
                searchParams.setDivyangPercentage(percentage);
                searchParams.setDivyangDisablityID(disabilityId);
                searchParams.setDivyangWithHelperIds(String.valueOf(da_id));
            }
            c.close();
        }
    }

}
