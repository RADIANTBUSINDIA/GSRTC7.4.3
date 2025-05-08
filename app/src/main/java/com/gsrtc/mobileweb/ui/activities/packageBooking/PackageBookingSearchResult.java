package com.gsrtc.mobileweb.ui.activities.packageBooking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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


import com.gsrtc.mobileweb.ui.activities.advance_booking.SeatSelectionActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

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

public class PackageBookingSearchResult extends BaseNavigationDrawerActivity {

    PackageBookingSearchResult.SearchResultAdapter searchResultAdapter;
    SearchParams searchParams;
    private Date mJourneyStartDate;
    private Date mReturnStartDate;
    private SimpleDateFormat mSmpDtFrmt;
    private SimpleDateFormat mSmpDtFrmtCall;
    Booking onwardBooking;
    Booking returnBooking;
    PackageBookingSearchResult.ActivityViewHolder vHolder;
    boolean dataFetched = false;
    private int mIndex;
    ProgressDialog progressDialog;
    CalendarDatePickerDialogFragment calendarDialog;
    private String textlang, discfare, divyang_disable_type, divyang_auth, div_id_valid;
    private String status, divName, address1, address2, address3, address4, div_id, gender, dob, place, type, percentage, expired, mobile, count;
    private String disabilityId, issueDistrictId;
    PopupWindow popupWindow;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    private EditText div_id_text, div_type_text, div_birth_date, div_expiry_date, div_percentage;
    private Spinner div_disability_type, div_authority;
    private Button div_cancel, div_confirm;
    String crntDate ="",crntTime="";

    public enum SORT_METHOD {
        DEP_TIME, CLASS, PRICE;
    }

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
        setContentView(R.layout.activity_package_booking_search_result);


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

        vHolder = new PackageBookingSearchResult.ActivityViewHolder(this);

        vHolder.from.setText(searchParams.getFromLocation().getPlaceName());
        vHolder.fromId.setText(searchParams.getFromLocation().getPlaceCode());
        vHolder.date.setText(searchParams.getOnWardDate());
        vHolder.to.setText(searchParams.getToLocation().getPlaceName());
        vHolder.toId.setText(searchParams.getToLocation().getPlaceCode());

        setupListView(null);
        vHolder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CustomisedProgressDialog.SHOW_CUST_DIA(PackageBookingSearchResult.this, "Searching Buses...");

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

        searchResultAdapter = new PackageBookingSearchResult.SearchResultAdapter(this, serviceList) {

            @Override
            public void onItemClick(BusService busService) {

                if (searchParams.getDivyangBooking().equalsIgnoreCase("Divyang") || searchParams.getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

                    LayoutInflater layoutInflater = (LayoutInflater) PackageBookingSearchResult.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = layoutInflater.inflate(R.layout.divyang_info, null);

                    div_id_text = (EditText) customView.findViewById(R.id.div_id);
                    div_type_text = (EditText) customView.findViewById(R.id.div_type);
                    div_birth_date = (EditText) customView.findViewById(R.id.div_dob);
                    div_expiry_date = (EditText) customView.findViewById(R.id.div_expired);
                    div_percentage = (EditText) customView.findViewById(R.id.div_percent);
                    div_disability_type = (Spinner) customView.findViewById(R.id.div_disablity);
                    div_authority = (Spinner) customView.findViewById(R.id.div_authority);
                    div_cancel = (Button) customView.findViewById(R.id.div_cancel);
                    div_confirm = (Button) customView.findViewById(R.id.div_submit);

                    div_disability_type.setAdapter(adapter);
                    div_authority.setAdapter(adapter1);

                    div_disability_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            divyang_disable_type = String.valueOf(adapterView.getItemAtPosition(i));
                            if (!divyang_disable_type.equalsIgnoreCase("--SELECT TYPE--")) {
                                int val = 100+i;
                                disabilityId = String.valueOf(val);
                                getValidDivyangId(div_id_valid);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    div_authority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            divyang_auth = String.valueOf(adapterView.getItemAtPosition(i));
                            if (!divyang_auth.equalsIgnoreCase("--SELECT--")) {
                                issueDistrictId = String.valueOf(i);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    div_type_text.setText(searchParams.getDivyangBooking());
                    div_birth_date.setText(dob);
                    div_expiry_date.setText(expired);
                    div_percentage.setText(percentage);

                    //instantiate popup window
                    popupWindow = new PopupWindow(customView, 950, ViewGroup.LayoutParams.WRAP_CONTENT);

                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(true);
                    popupWindow.update();

                    //display the popup window
                    popupWindow.showAtLocation(vHolder.recyclerView, Gravity.CENTER, 0, 0);

                    div_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (divyang_disable_type.equalsIgnoreCase("--SELECT TYPE--") || divyang_auth.equalsIgnoreCase("--SELECT--")) {

                                Toast.makeText(PackageBookingSearchResult.this, "Please select valid Disability Type / Authority Details", Toast.LENGTH_SHORT).show();

                            } else {
                                if (div_birth_date.getText().toString().equalsIgnoreCase("") || div_expiry_date.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(PackageBookingSearchResult.this, "Invalid divyang Id!", Toast.LENGTH_SHORT).show();

                                } else {
                                    searchParams.setDivyangIssueDistrictId(issueDistrictId);
                                    searchParams.setDivyangIssueDistrict(divyang_auth);
                                    Bundle extra = new Bundle();
                                    extra.putString("textlang", textlang);
                                    extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                                    Intent intent = new Intent(PackageBookingSearchResult.this, SeatSelectionActivity.class);
                                    intent.putExtras(extra);
                                    startActivityForResult(intent, SEAT_SELECTION_REQUEST);
                                }
                            }
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
                        }
                    });

                } else {
                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    Intent intent = new Intent(PackageBookingSearchResult.this, SeatSelectionActivity.class);
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
                sortList(busServiceList, PackageBookingSearchResult.SORT_METHOD.DEP_TIME);
                setupListView(busServiceList);
                break;
            case R.id.class_btn:
                sortList(busServiceList, PackageBookingSearchResult.SORT_METHOD.CLASS);
                setupListView(busServiceList);
                break;
            case R.id.price_btn:
                sortList(busServiceList, PackageBookingSearchResult.SORT_METHOD.PRICE);
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


    public void sortList(List<BusService> unsortedList, PackageBookingSearchResult.SORT_METHOD sort_method) {
        if (unsortedList == null || unsortedList.isEmpty()) {
            return;
        }
        if (sort_method.equals(PackageBookingSearchResult.SORT_METHOD.DEP_TIME)) {
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
        } else if (sort_method.equals(PackageBookingSearchResult.SORT_METHOD.CLASS)) {
            Collections.sort(unsortedList, new Comparator<BusService>() {
                @Override
                public int compare(BusService a1, BusService a2) {
                    return -a1.getServiceClass().compareTo(a2.getServiceClass());
                }
            });
        } else if (sort_method.equals(PackageBookingSearchResult.SORT_METHOD.PRICE)) {
            Collections.sort(unsortedList, new Comparator<BusService>() {
                @Override
                public int compare(BusService a1, BusService a2) {
                    return ((Double) Math.ceil(Double.parseDouble(a1.getAdultFare()) - Double.parseDouble(a2.getAdultFare()))).intValue();
                }
            });
        }
    }


    public void fetchAvailableServices() {

        CustomisedProgressDialog.SHOW_CUST_DIA(PackageBookingSearchResult.this, "Searching Buses...");

        SoapClientUtil.getPackageAvailableServiceDetails(searchParams, (onwardBooking != null), new SoapClientUtil.GetAvailableServiceDetailsCallback() {
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
            }

            @Override
            public void onError(Exception e) {
                vHolder.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(PackageBookingSearchResult.this, e);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        ActivityUtil.openNewActivity(this, PackagePassengerInfo.class, extras);
                    }
                    if (searchParams.getReturnDate() == null) {   //No return date has to be selected
                        Bundle extras = new Bundle();
                        extras.putString("textlang", textlang);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                        ActivityUtil.openNewActivity(this, PackagePassengerInfo.class, extras);
                    } else {           //return date has to be selected
                        loadViewForReturnJourney();
                    }
                }
            }
        }
    }


    static abstract class SearchResultAdapter extends RecyclerView.Adapter<PackageBookingSearchResult.SearchResultAdapter.ViewHolder> {

        private List<BusService> busList;
        Context context;


        public SearchResultAdapter(Context context, List<BusService> busList) {
            if (busList == null)
                busList = new ArrayList<BusService>();
            this.busList = busList;
            this.context = context;
        }

        @Override
        public PackageBookingSearchResult.SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.advance_booking_list_item, parent, false);

            // Return a new holder instance
            return new PackageBookingSearchResult.SearchResultAdapter.ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(PackageBookingSearchResult.SearchResultAdapter.ViewHolder holder, int position) {
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
                    PackageBookingSearchResult.this);

            alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle extras = new Bundle();
                    extras.putString("textlang", textlang);
                    onwardBooking.setSearchParams(searchParams);
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                    ActivityUtil.openNewActivity(PackageBookingSearchResult.this, PackagePassengerInfo.class, extras);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PackageBookingSearchResult.this.finish();
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
                    PackageBookingSearchResult.this, R.style.CustomDialogTheme);

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
        PackageBookingSearchResult.Months mMon = PackageBookingSearchResult.Months.valueOf(paramDate.trim().split(" ")[1]);
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

//        String divId = "130021600461";
        String divId = div_id_text.getText().toString();

        if (!PackageBookingSearchResult.this.isFinishing()) {
            CustomisedProgressDialog.SHOW_CUST_DIA(PackageBookingSearchResult.this, "processing...");
        }

        String URL_DIVYANG = "https://www.gsrtc.in/GSRTCPhonepe/manageDivyangApi.do?DivyangId=" + divId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DIVYANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value = response.toString();
                CustomisedProgressDialog.STOP_CUST_DIA();
                Log.i("VOLLEY", response);

                System.out.println("DivResponse:" + response);

                String getDivyang[] = value.split("\\$");
                char first = value.charAt(0);
                char FOUND = 'F';
                char NOT_FOUND = 'N';
                if (first == FOUND) {

                    status = getDivyang[0];
                    divName = getDivyang[1];
                    address1 = getDivyang[2];
                    div_id = getDivyang[3];
                    gender = getDivyang[4];
                    dob = getDivyang[5];
                    place = getDivyang[7];
                    type = getDivyang[8];
                    percentage = getDivyang[9];
                    expired = getDivyang[10];
                    mobile = getDivyang[13];
                    count = getDivyang[14];


                    System.out.println("DivResponse:" + status + "|" + divName + "|" + address1 + "|" + address2 + "|" + address3 + "|" + address4 + "|" + div_id + "|" + gender + "|" + dob + "|" + place + "|" + type + "|" + percentage + "|" + expired + "|" + mobile + "|" + count);

                    div_birth_date.setText(dob);
                    div_expiry_date.setText(expired);
                    div_percentage.setText(percentage);
                    searchParams.setDivyangId(divId);
                    searchParams.setDivyangIssueDistrict(divyang_auth);
                    searchParams.setDivyangIssueDistrictId(issueDistrictId);
                    searchParams.setDivyangDisablityType(divyang_disable_type);
                    searchParams.setDivyangDisablityID(disabilityId);
                    searchParams.setDivyangPercentage(percentage);
                    getDivyangConcess(divyang_disable_type.trim());

                } else {
                    Toast.makeText(PackageBookingSearchResult.this, "Invalid divyang Id!", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                System.out.println("DivResponse:" + error.toString());
                CustomisedProgressDialog.STOP_CUST_DIA();
            }
        });

        requestQueue.add(stringRequest);

    }

    @SuppressLint("Range")
    public void getDivyangConcess(String div_concess) {

        Gsrtc_local_db mDb = Gsrtc_local_db.GET_DB_Instance(PackageBookingSearchResult.this);
        Cursor c = mDb.getDivyangIds(div_concess);

        if (c.getCount() == 0){
            Toast.makeText(PackageBookingSearchResult.this, "Data Not Found!", Toast.LENGTH_SHORT).show();

        }
        else{

            while (c.moveToNext()){

                disabilityId= String.valueOf(c.getInt(c.getColumnIndex("_DCD_ID")));
                int da_id= Integer.parseInt(c.getString(c.getColumnIndex("_DCD_DA_ID")));
                percentage = c.getString(c.getColumnIndex("_DCD_DA_CONCESS"));
                div_percentage.setText(percentage);
                searchParams.setDivyangPercentage(percentage);
                searchParams.setDivyangDisablityID(disabilityId);
                searchParams.setDivyangWithHelperIds(String.valueOf(da_id));
            }
        }

    }

}
