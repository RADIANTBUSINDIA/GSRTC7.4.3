package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.database.Gsrtc_local_db;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

public class AdvanceBookingSearchActivity extends BaseNavigationDrawerActivity {

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
    ViewHolder holder;
    SearchParams searchParams;
    CalendarDatePickerDialogFragment calendarDialog;
    private String textlang, crntDate,selectType="Onward";
    List<Place> locationList = new ArrayList<Place>();
    List<Place> locationPrevSearch = new ArrayList<Place>();
    List<String> locationListNames = new ArrayList<String>();
    MonthAdapter.CalendarDay maxDateForAdvResvBooking = null;
    MonthAdapter.CalendarDay startDate;
    MonthAdapter.CalendarDay endDate;
    boolean maxDateFetched = false;
    boolean addressListFetched = false;

    private SwipeRefreshLayout swipeContainer;
    public static int REQUEST_NO_SERVICE_AVAILABLE = 1;
    private CheckBox checkBox;
    private TextView single, round;

    //
    private ArrayList<String> gSId, gSCode, gSPlace, gDId, gDCode, gDPlace, mDate, mReturnFlag;
    private ArrayList<String> mReturnDate;
    private String prevSource, prevDest;
    private RecyclerView horizontal_recycler_view;
    private RecyclerView.Adapter horizontalAdapter;

    RadioGroup radio;
    RadioButton sinle_lady;
    RadioButton divyang;
    RadioButton div_helper;
    private String select_option = "General";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_advance_booking);

        Bundle mBun = getIntent().getExtras();
        textlang = mBun.getString("textlang");

        radio = (RadioGroup) findViewById(R.id.radio);
        sinle_lady = (RadioButton) findViewById(R.id.single_lady);
        divyang = (RadioButton) findViewById(R.id.divyang);
        div_helper = (RadioButton) findViewById(R.id.div_helper);
        single = (TextView) findViewById(R.id.single_trip);
        round = (TextView) findViewById(R.id.round_trip);

        setupViews();
        gSId = new ArrayList<String>();
        gSCode = new ArrayList<String>();
        gSPlace = new ArrayList<String>();
        gDId = new ArrayList<String>();
        gDCode = new ArrayList<String>();
        gDPlace = new ArrayList<String>();
        mDate = new ArrayList<String>();
        mReturnFlag = new ArrayList<String>();
        mReturnDate = new ArrayList<String>();
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.last_Search);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(AdvanceBookingSearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

        getLastSearch();

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.return_date.setVisibility(View.GONE);
                single.setVisibility(View.VISIBLE);
                single.setBackgroundResource(R.drawable.btn_shape);
                round.setTextColor(Color.parseColor("#ffffff"));
                single.setTextColor(Color.parseColor("#000000"));
                round.setBackground(null);
                selectType="Onward";
                radio.getChildAt(1).setEnabled(true);
                radio.getChildAt(2).setEnabled(true);


            }
        });

        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.return_date.setVisibility(View.VISIBLE);
                round.setBackgroundResource(R.drawable.btn_shape);
                single.setTextColor(Color.parseColor("#ffffff"));
                round.setTextColor(Color.parseColor("#000000"));
                single.setBackground(null);
                selectType="Return";
                radio.getChildAt(1).setEnabled(false);
                radio.getChildAt(2).setEnabled(false);
                
            }
        });
    }


    public void setupViews() {
        holder = new ViewHolder(this);
        searchParams = new SearchParams();
        //showLoading(true);
        searchParams.setTextlanguage(textlang);
        holder.to_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.to_location.getText().toString();
                searchParams.setToLocation(locationList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_place_list, R.id.place_text, locationListNames);
        holder.from_location.setAdapter(adapter);
        holder.from_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.from_location.getText().toString();
                searchParams.setFromLocation(locationList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**getting currennt date and Time **/

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd", Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));
        crntDate = cur_day + "/" + cur_month + "/" + cur_year;
        holder.onward_date.setText(cur_day + "/" + cur_month + "/" + cur_year);
        searchParams.setOnWardDate(cur_day + "/" + cur_month + "/" + cur_year);

        holder.onward_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });

        holder.return_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getText().toString().equalsIgnoreCase("Single Lady")) {

                    select_option = "Single Lady";
                    holder.adultCount.setText("1");
                    holder.adultCount.setFocusable(false);
                    holder.adultCount.setFocusableInTouchMode(false);
                    holder.adultCount.setClickable(false);
                    holder.childrenCount.setFocusable(false);
                    holder.childrenCount.setFocusableInTouchMode(false);
                    holder.childrenCount.setClickable(false);
                    round.setClickable(true);
                    round.setFocusable(true);

                } else if (button.getText().toString().equalsIgnoreCase("Divyang")) {


                        select_option = "Divyang";
                        holder.adultCount.setText("1");
                        round.setClickable(false);
                        round.setFocusable(false);
                        holder.adultCount.setFocusable(false);
                        holder.adultCount.setFocusableInTouchMode(false);
                        holder.adultCount.setClickable(false);
                        holder.childrenCount.setFocusable(false);
                        holder.childrenCount.setFocusableInTouchMode(false);
                        holder.childrenCount.setClickable(false);


                } else if (button.getText().toString().equalsIgnoreCase("Divyang with Helper")) {

                    select_option = "Divyang with Helper";
                    holder.adultCount.setText("2");
                    holder.adultCount.setFocusable(false);
                    holder.adultCount.setFocusableInTouchMode(false);
                    holder.adultCount.setClickable(false);
                    round.setClickable(false);
                    round.setFocusable(false);
                    holder.childrenCount.setFocusable(false);
                    holder.childrenCount.setFocusableInTouchMode(false);
                    holder.childrenCount.setClickable(false);
                }
            }
        });

        holder.search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.from_location.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.to_location.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.adultCount.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.childrenCount.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.return_date.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.onward_date.getWindowToken(), 0);

                goToResultActivity();
            }
        });
        holder.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromvalue = holder.from_location.getText().toString();
                String tovalue = holder.to_location.getText().toString();
                if ((fromvalue.matches("")) || tovalue.matches("")) {

                    Toast.makeText(AdvanceBookingSearchActivity.this, "Please Enter valid place", Toast.LENGTH_SHORT).show();
                    return;
                }

                holder.from_location.setText(tovalue);
                holder.to_location.setText(fromvalue);
                holder.to_location.clearFocus();
                holder.from_location.clearFocus();

            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                fetchServerData();
                maxDateFetched = false;
                addressListFetched = false;
            }
        });

        fetchServerData();
    }


    public void goToResultActivity() {
        if (TextUtils.isEmpty(holder.adultCount.getText().toString()) && TextUtils.isEmpty(holder.childrenCount.getText().toString())) {
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be zero");
            return;
        }

        if (TextUtils.isEmpty(holder.adultCount.getText().toString())) {
            ActivityUtil.showSnackBar(holder.container, "No. of Adult passengers can not be zero");
            return;
        }

        Place startPlace = null;
        Place endPlace = null;
        if (TextUtils.isEmpty(holder.from_location.getText().toString()) || TextUtils.isEmpty(holder.to_location.getText().toString())) {
            ActivityUtil.showSnackBar(holder.container, "Please select a location");
            return;
        } else {
            for (Place place : locationList) {
                if (place.getPlaceName().equals(holder.from_location.getText().toString())) {
                    startPlace = place;
                }
                if (place.getPlaceName().equals(holder.to_location.getText().toString())) {
                    endPlace = place;
                }
            }
        }
        if (startPlace == null) {
            ActivityUtil.showSnackBar(holder.container, "Please select a valid start place");
            return;
        }
        if (endPlace == null) {
            ActivityUtil.showSnackBar(holder.container, "Please select a valid end place");
            return;
        }
        if (TextUtils.isEmpty(holder.onward_date.getText().toString())) {
            ActivityUtil.showSnackBar(holder.container, "Please select valid start date");
            return;
        }

        if (!TextUtils.isEmpty(holder.adultCount.getText().toString())) {
            searchParams.setAdultCount(Integer.parseInt(holder.adultCount.getText().toString()));
        }
        if (!TextUtils.isEmpty(holder.childrenCount.getText().toString())) {
            searchParams.setChildrenCount(Integer.parseInt(holder.childrenCount.getText().toString()));
        }

        searchParams.setFromLocation(startPlace);
        searchParams.setToLocation(endPlace);

        if (select_option.equalsIgnoreCase("Single Lady")) {
            holder.adultCount.setText("0");
            holder.childrenCount.setText("0");
            searchParams.setAdultFemaleCount(Integer.parseInt("1"));
            searchParams.setChildrenCount(Integer.parseInt("0"));
            searchParams.setAdultCount(Integer.parseInt("0"));
        } else {
            searchParams.setAdultFemaleCount(Integer.parseInt("0"));
        }

        searchParams.setDivyangBooking("General");
        searchParams.setNumOfPassengers(searchParams.getAdultCount() + searchParams.getChildrenCount() + searchParams.getAdultFemaleCount());

        if (select_option.equalsIgnoreCase("Divyang")) {
            if (searchParams.getNumOfPassengers() > 1 || searchParams.getNumOfPassengers() < 1) {
                Toast.makeText(AdvanceBookingSearchActivity.this, "No.of Divyang passengers can not be greater than 1 ", Toast.LENGTH_SHORT).show();
                return;
            } else {
                searchParams.setDivyangBooking("Divyang");
            }
        }

        if (select_option.equalsIgnoreCase("Divyang with Helper")) {

            if (searchParams.getNumOfPassengers() > 2 || searchParams.getNumOfPassengers() < 2) {
                Toast.makeText(AdvanceBookingSearchActivity.this, "No.of Divyang with Helper passengers can not be greater/ Lesser than 2 ", Toast.LENGTH_SHORT).show();
                return;
            } else {
                searchParams.setDivyangBooking("Divyang with Helper");
            }
        }

        if (searchParams.getNumOfPassengers() == 0) {
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be zero");
            return;
        }
        if (searchParams.getNumOfPassengers() > 6) {
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be greater than 6");
            return;
        }

        if (searchParams.getAdultCount() < 1 && select_option.equalsIgnoreCase("General")) {
            ActivityUtil.showSnackBar(holder.container, "No. of Adult passengers can not be zero");
            return;
        }

        saveLastSearch();

        Bundle extra = new Bundle();
        extra.putString("textlang", textlang);
        extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
        ActivityUtil.openNewActivityForResult(AdvanceBookingSearchActivity.this, AdvanceBookingSearchResultActivity.class, extra, REQUEST_NO_SERVICE_AVAILABLE);
    }

    /**
     * @param view - View which opens up the dialog on click
     */


    /**
     * datre pickerDialog
     **/

    @SuppressLint("WrongConstant")
    public void openDatePickerDialog(final View view) {
        if (calendarDialog != null && calendarDialog.isVisible()) {
            return;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        MonthAdapter.CalendarDay currentCalendarDay = new MonthAdapter.CalendarDay();
        MonthAdapter.CalendarDay startDay = ((view.getId() == R.id.onward_date) ? (currentCalendarDay) : ((startDate == null) ? currentCalendarDay : startDate));
        MonthAdapter.CalendarDay endDay = ((view.getId() == R.id.onward_date) ? ((endDate == null) ? maxDateForAdvResvBooking : endDate) : (maxDateForAdvResvBooking));
        Calendar preselectedDate = Calendar.getInstance();
        preselectedDate.setTimeInMillis(startDay.getDateInMillis());
        calendarDialog = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        onDatePicked(view, year, monthOfYear, dayOfMonth);
                    }
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(preselectedDate.get(Calendar.YEAR), preselectedDate.get(Calendar.MONTH), preselectedDate.get(Calendar.DAY_OF_MONTH))
                .setDateRange(startDay, endDay)
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
            case R.id.onward_date:

                holder.onward_date.setText(dayString + "/" + monthString + "/" + year);
                searchParams.setOnWardDate(dayString + "/" + monthString + "/" + year);
                startDate = new MonthAdapter.CalendarDay();
                startDate.setDay(year, month, day);
                break;
            case R.id.return_date:
                holder.return_date.setText(dayString + "/" + monthString + "/" + year);
                searchParams.setReturnDate(dayString + "/" + monthString + "/" + year);
                endDate = new MonthAdapter.CalendarDay();
                endDate.setDay(year, month, day);
                break;
        }
    }

    public void fetchServerData() {
        showLoading(true);
        CustomisedProgressDialog.SHOW_CUST_DIA(AdvanceBookingSearchActivity.this, "Loading Please wait...");

        SoapClientUtil.getMaxValidDateForAdvResvBooking(new SoapClientUtil.GetMaxValidDateForAdvResvBookingCallback() {
            @Override
            public void onSuccess(int year, int month, int date) {
                maxDateFetched = true;

                maxDateForAdvResvBooking = new MonthAdapter.CalendarDay();
                maxDateForAdvResvBooking.setDay(year, month, date);
                if (maxDateFetched && addressListFetched) {
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            @Override
            public void onError(Exception e) {
                showLoadingView(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(AdvanceBookingSearchActivity.this);
            }
        });

        /** fetch addressplace list **/

        holder.no_internet_text.setVisibility(View.GONE);
        SoapClientUtil.fetchAddressListAsync(new SoapClientUtil.AddressListCallback() {
            @Override
            public void onSuccess(List<Place> placeList) {
                locationList = placeList;
                locationListNames = new ArrayList<String>();
                for (Place location : locationList) {
                    locationListNames.add(location.getPlaceName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdvanceBookingSearchActivity.this, R.layout.custom_place_list, R.id.place_text, locationListNames);
                holder.from_location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AdvanceBookingSearchActivity.this, R.layout.custom_place_list, R.id.place_text, locationListNames);
                holder.to_location.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                addressListFetched = true;
                if (maxDateFetched && addressListFetched) {
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
                holder.copytitle.setVisibility(View.GONE);
                holder.copyone.setVisibility(View.VISIBLE);
                holder.copytwo.setVisibility(View.GONE);
                holder.copythree.setVisibility(View.GONE);
                holder.copy.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

                showLoadingView(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(AdvanceBookingSearchActivity.this);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NO_SERVICE_AVAILABLE) {
            if (resultCode == RESULT_OK) {
                ActivityUtil.showToast(this, "Sorry, no bus service is available for your selection");
            }
        }
    }

    public void showLoadingView(boolean show) {
        if (show) {
            holder.loadingLayout.setVisibility(View.VISIBLE);
        } else {
            holder.loadingLayout.setVisibility(View.GONE);
        }
    }

    public void showLoading(boolean show) {
        if (show) {

            holder.loadingLayout.setVisibility(View.VISIBLE);
            if (addressListFetched && maxDateFetched) {

            } else
                holder.search_layout.setVisibility(View.GONE);
        } else {

            holder.loadingLayout.setVisibility(View.GONE);
            holder.search_layout.setVisibility(View.VISIBLE);
        }
    }

    private class ViewHolder {
        View container;

        AutoCompleteTextView from_location, to_location;
        Button one_way_btn, round_trip_btn, search_btn;
        EditText return_date, onward_date, adultCount, childrenCount;
        Spinner num_of_passengers;

        TextView no_internet_text;
        View loading_layout, search_layout;
        ProgressBar loading_progressbar;
        View loadingLayout;
        ImageView swap;
        TextView copy, copyone, copytwo, copythree, copytitle, adbook_title, recent_search, singleLady;

        ViewHolder(Activity rootView) {
            container = rootView.findViewById(R.id.container);
            from_location = (AutoCompleteTextView) rootView.findViewById(R.id.from_location);
            from_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            to_location = (AutoCompleteTextView) rootView.findViewById(R.id.to_location);
            to_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            copyone = (TextView) rootView.findViewById(R.id.policyone);
            copytwo = (TextView) rootView.findViewById(R.id.policytwo);
            copythree = (TextView) rootView.findViewById(R.id.policythree);
            copytitle = (TextView) rootView.findViewById(R.id.copytitle);
            copy = (TextView) rootView.findViewById(R.id.copyright);
            recent_search = (TextView) rootView.findViewById(R.id.recent_search);
            singleLady = (TextView) rootView.findViewById(R.id.single_lady);

            adbook_title = (TextView) rootView.findViewById(R.id.title);
            swap = (ImageView) rootView.findViewById(R.id.swap);
            search_btn = (Button) rootView.findViewById(R.id.search_btn);
            onward_date = (EditText) rootView.findViewById(R.id.onward_date);
            return_date = (EditText) rootView.findViewById(R.id.return_date);
            adultCount = (EditText) rootView.findViewById(R.id.adultCount);
            childrenCount = (EditText) rootView.findViewById(R.id.childrenCount);
            adultCount.setInputType(InputType.TYPE_CLASS_NUMBER);
            childrenCount.setInputType(InputType.TYPE_CLASS_NUMBER);
            no_internet_text = (TextView) rootView.findViewById(R.id.no_internet_text);
            loading_layout = (View) rootView.findViewById(R.id.loading_layout);
            search_layout = (View) rootView.findViewById(R.id.search_layout);
            loadingLayout = rootView.findViewById(R.id.loading_layout);

            adultCount.setFocusable(true);
            adultCount.setFocusableInTouchMode(true);
            adultCount.setClickable(true);
            childrenCount.setFocusable(true);
            childrenCount.setFocusableInTouchMode(true);
            childrenCount.setClickable(true);

            if (textlang.equalsIgnoreCase("gujarati")) {
                adbook_title.setText(R.string.bk_advance_booking);
                from_location.setHint(R.string.bk_from_place);
                to_location.setHint(R.string.bk_to_place);
                onward_date.setHint(R.string.bk_onward_date);
                return_date.setHint(R.string.bk_return_date);
                adultCount.setHint(R.string.bk_adults);
                childrenCount.setHint(R.string.bk_child);
                search_btn.setText(R.string.bk_search);
                copytitle.setText(R.string.bk_policy);
                copyone.setText(R.string.bk_policyone);
                recent_search.setText(R.string.bk_recent_search);
                singleLady.setText(R.string.bk_single_lady);

                copytwo.setText(R.string.bk_policytwo);
                copythree.setText(R.string.bk_policythree);
            }

            copyone.setSelected(true);

        }
    }

    @SuppressLint("Range")
    public void getLastSearch() {
        gSId.removeAll(gSId);
        gSCode.removeAll(gSCode);
        gSPlace.removeAll(gSPlace);
        gDId.removeAll(gDId);
        gDCode.removeAll(gDCode);
        gDPlace.removeAll(gDPlace);
        mDate.removeAll(mDate);
        mReturnDate.removeAll(mReturnDate);
        mReturnFlag.removeAll(mReturnFlag);
        Gsrtc_local_db mDb = Gsrtc_local_db.GET_DB_Instance(AdvanceBookingSearchActivity.this);
        Cursor c = mDb.mGetLastAllSearch(crntDate);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                gSId.add(c.getString(c.getColumnIndex("_source_city_id")));
                gSCode.add(c.getString(c.getColumnIndex("_source_city_code")));
                gSPlace.add(c.getString(c.getColumnIndex("_source_city_name")));
                gDId.add(c.getString(c.getColumnIndex("_dest_city_id")));
                gDCode.add(c.getString(c.getColumnIndex("_dest_city_code")));
                gDPlace.add(c.getString(c.getColumnIndex("_dest_city_name")));
                mDate.add(c.getString(c.getColumnIndex("_journey_date")));
                mReturnDate.add(c.getString(c.getColumnIndex("_return_date")));
                mReturnFlag.add(c.getString(c.getColumnIndex("_adult")));
                Log.e("date", "" + c.getString(c.getColumnIndex("_datefordb")));
            } while (c.moveToNext());
        }
        c.close();
        mDb.close();

        horizontalAdapter = new CustomRecyclerAdapter(this, gSId);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        if (gSId.size() > 0) {
            holder.recent_search.setVisibility(View.VISIBLE);
            horizontal_recycler_view.setVisibility(View.VISIBLE);

        } else {
            horizontal_recycler_view.setVisibility(View.GONE);
        }
    }

    public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

        private Context context;
        private List<String> get_last_search;

        public CustomRecyclerAdapter(Context context, List get_search) {
            this.context = context;
            this.get_last_search = get_search;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_search_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        //        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.itemView.setTag(get_last_search.get(position));

            holder.mTvSource.setText(gSPlace.get(position) + " - " + gDPlace.get(position));

            holder.mTvBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Place source = new Place();

                    source.setPlaceCode(gSCode.get(position));
                    source.setPlaceID(gSId.get(position));
                    source.setPlaceName(gSPlace.get(position));
                    prevSource = gSPlace.get(position);
                    locationPrevSearch.add(source);
                    searchParams.setFromLocation(source);

                    Place dest = new Place();
                    dest.setPlaceCode(gDCode.get(position));
                    dest.setPlaceID(gDId.get(position));
                    dest.setPlaceName(gDPlace.get(position));

                    prevDest = gDPlace.get(position);
                    locationPrevSearch.add(dest);
                    searchParams.setToLocation(dest);

                    setPrevVal();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Place source = new Place();
                    source.setPlaceCode(gSCode.get(position));
                    source.setPlaceID(gSId.get(position));
                    source.setPlaceName(gSPlace.get(position));
                    prevSource = gSPlace.get(position);
                    locationPrevSearch.add(source);
                    searchParams.setFromLocation(source);

                    Place dest = new Place();
                    dest.setPlaceCode(gDCode.get(position));
                    dest.setPlaceID(gDId.get(position));
                    dest.setPlaceName(gDPlace.get(position));

                    prevDest = gDPlace.get(position);
                    locationPrevSearch.add(dest);
                    searchParams.setToLocation(dest);

                    setPrevVal();
                }
            });
        }

        public void setPrevVal() {
            holder.from_location.setText(prevSource);
            holder.to_location.setText(prevDest);
        }

        @Override
        public int getItemCount() {
            return get_last_search.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTvSource, mTvDest, mTvBook;

            public ViewHolder(View itemView) {
                super(itemView);

                mTvSource = (TextView) itemView.findViewById(R.id.xTvSource);
                mTvDest = (TextView) itemView.findViewById(R.id.xTvDest);
                mTvBook = (TextView) itemView.findViewById(R.id.xTvBook);
            }
        }
    }

    public void saveLastSearch() {
        Gsrtc_local_db mDb = Gsrtc_local_db.GET_DB_Instance(AdvanceBookingSearchActivity.this);
        Cursor c = mDb.mGetLastSearch();
        if (c.getCount() < 10) {

        } else {
            c.moveToFirst();
            @SuppressLint("Range") String id = c.getString(c.getColumnIndex("_id"));

            Log.e("id", "" + id);
            mDb.deleteLastSearchMoreThenFive(id);

        }
        c.close();
        mDb.close();

        mDb.sInsert_LastSearch(searchParams.getFromLocation().getPlaceID(), searchParams.getFromLocation().getPlaceCode(), searchParams.getFromLocation().getPlaceName(),
                searchParams.getToLocation().getPlaceID(), searchParams.getToLocation().getPlaceCode(), searchParams.getToLocation().getPlaceName(),
                searchParams.getOnWardDate(), searchParams.getReturnDate(), searchParams.getAdultCount(), searchParams.getOnWardDate());
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(AdvanceBookingSearchActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
