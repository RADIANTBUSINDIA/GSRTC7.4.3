package com.gsrtc.mobileweb.ui.activities.time_table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Timetable_Search extends BaseNavigationDrawerActivity implements
        AdapterView.OnItemSelectedListener {

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
    ViewHolder holder;
    SearchParams searchParams;
    SearchParamsCurrent searchParamsCurrent;
    CalendarDatePickerDialogFragment calendarDialog;
    private String textlang,serv_class,trip ="Advance";
    Spinner service_time,service_type;
    RadioGroup radio;
    RadioButton local,advance;
    TextView local_btn,adv_btn;
    View local_view,adv_view;

    String[] Service_class = { "All","Express", "Gurjarnagri", "Sleeper", "Ac Sleeper", "Volvo","Volvo Sleeper","Metro Link Service"};
    String[] Time = { "Any"};

    List<Place> locationList = new ArrayList<Place>();
    List<String> locationListNames = new ArrayList<String>();

    MonthAdapter.CalendarDay maxDateForAdvResvBooking = null;
    MonthAdapter.CalendarDay startDate;
    MonthAdapter.CalendarDay endDate;

    boolean maxDateFetched = false;
    boolean addressListFetched = false;

    private SwipeRefreshLayout swipeContainer;
    public static int REQUEST_NO_SERVICE_AVAILABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_timetable__search);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        service_type = (Spinner) findViewById(R.id.serv_cls);
        radio = (RadioGroup) findViewById(R.id.radio);
        local = (RadioButton) findViewById(R.id.local_trip);
        advance = (RadioButton) findViewById(R.id.advance_trip);
        local_btn = (TextView) findViewById(R.id.local_txt);
        adv_btn = (TextView) findViewById(R.id.advance_txt);

        setupViews();

        ArrayAdapter serivice= new ArrayAdapter(this,android.R.layout.simple_spinner_item,Service_class);
        serivice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_type.setAdapter(serivice);

    }

    public void setupViews(){
        holder = new ViewHolder(this);

        adv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trip ="Advance";

                holder.journey_date.setVisibility(View.VISIBLE);

                adv_btn.setBackgroundResource(R.drawable.btn_shape);
                adv_btn.setTextColor(Color.parseColor("#000000"));

                local_btn.setBackground(null);
                local_btn.setTextColor(Color.parseColor("#ffffff"));

            }
        });

        local_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trip ="Local";

                holder.journey_date.setVisibility(View.GONE);

                local_btn.setBackgroundResource(R.drawable.btn_shape);
                local_btn.setTextColor(Color.parseColor("#000000"));

                adv_btn.setBackground(null);
                adv_btn.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        searchParams = new SearchParams();
        searchParamsCurrent = new SearchParamsCurrent();

        searchParams.setTextlanguage(textlang);
        searchParamsCurrent.setCrntlanguage(textlang);

        holder.to_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.to_location.getText().toString();
                searchParams.setToLocation(locationList.get(i));
                searchParamsCurrent.setToLocation(locationList.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationListNames);
        holder.from_location.setAdapter(adapter);
        holder.from_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.from_location.getText().toString();
                searchParams.setFromLocation(locationList.get(i));
                searchParamsCurrent.setFromLocation(locationList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**getting currennt date and Time **/

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd",Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));
        holder.journey_date.setText(cur_day+"/"+cur_month+"/"+cur_year);
        searchParams.setOnWardDate(cur_day+"/"+cur_month+"/"+cur_year);
        searchParamsCurrent.setOnWardDate(cur_day+"/"+cur_month+"/"+cur_year);

        holder.journey_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });

        holder.search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.from_location.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.to_location.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(holder.journey_date.getWindowToken(), 0);

                goToResultActivity();
            }
        });
        holder.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromvalue=holder.from_location.getText().toString();
                String tovalue=holder.to_location.getText().toString();
                if ((fromvalue.matches("")) || tovalue.matches("")){

                    Toast.makeText(Timetable_Search.this, "Please Enter valid place", Toast.LENGTH_SHORT).show();
                    return;
                }

                holder.from_location.setText(tovalue);
                holder.to_location.setText(fromvalue);
                holder.to_location.clearFocus();
                holder.from_location.clearFocus();

            }
        });

//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeContainer.setRefreshing(false);
//                fetchServerData();
//                maxDateFetched = false;
//                addressListFetched = false;
//            }
//        });

        fetchServerData();
    }


    public void goToResultActivity(){

        Place startPlace = null;
        Place endPlace = null;
        if(TextUtils.isEmpty(holder.from_location.getText().toString()) || TextUtils.isEmpty(holder.to_location.getText().toString())){
            ActivityUtil.showSnackBar(holder.container, "Please select a location");
            return;
        }
        else{
            for(Place place: locationList){
                if(place.getPlaceName().equals(holder.from_location.getText().toString())){
                    startPlace = place;
                }
                if(place.getPlaceName().equals(holder.to_location.getText().toString())){
                    endPlace = place;
                }
            }
        }
        if(startPlace == null){
            ActivityUtil.showSnackBar(holder.container, "Please select a valid start place");
            return;
        }
        if(endPlace == null){
            ActivityUtil.showSnackBar(holder.container, "Please select a valid end place");
            return;
        }
        if(TextUtils.isEmpty(holder.journey_date.getText().toString())){
            ActivityUtil.showSnackBar(holder.container, "Please select valid date");
            return;
        }

        searchParams.setFromLocation(startPlace);
        searchParams.setToLocation(endPlace);

        searchParamsCurrent.setFromLocation(startPlace);
        searchParamsCurrent.setToLocation(endPlace);

        if(trip.equalsIgnoreCase("Local")){

            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM_ONROUTE, searchParamsCurrent);
            ActivityUtil.openNewActivityForResult(this, Local_Trip_time_table.class, extra, REQUEST_NO_SERVICE_AVAILABLE);
        }else{

            Bundle extra = new Bundle();
            extra.putString("textlang", textlang);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM_ONROUTE, searchParamsCurrent);
            ActivityUtil.openNewActivityForResult(this, TimeTableServices.class, extra, REQUEST_NO_SERVICE_AVAILABLE);

        }
    }
    /** datre pickerDialog **/

    @SuppressLint("WrongConstant")
    public void openDatePickerDialog(final View view){
        if(calendarDialog!=null && calendarDialog.isVisible()){
            return;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        MonthAdapter.CalendarDay currentCalendarDay = new MonthAdapter.CalendarDay();
        MonthAdapter.CalendarDay startDay = ((view.getId() == R.id.onward_date)?(currentCalendarDay):((startDate == null)?currentCalendarDay:startDate));
        MonthAdapter.CalendarDay endDay = ((view.getId() == R.id.onward_date)?((endDate == null)?maxDateForAdvResvBooking:endDate):(maxDateForAdvResvBooking));
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
                .setDateRange(startDay,endDay)
                .setDoneText("Select")
                .setCancelText("Cancel")
                .setThemeDark();
        calendarDialog.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    public void onDatePicked(View view, int year, int month, int day){
        Calendar setDate = Calendar.getInstance();
        setDate.set(year, month, day);
        String dayString = ((day<10)?"0":"")+day;
        String monthString = (((month+1)<10)?"0":"")+(month+1);
        switch (view.getId()){
            case R.id.journey_date:

                holder.journey_date.setText(dayString+"/"+monthString+"/"+year);
                searchParams.setOnWardDate(dayString+"/"+monthString+"/"+year);
                startDate = new MonthAdapter.CalendarDay();
                startDate.setDay(year, month, day);
                break;
        }
    }

    public void fetchServerData(){
        showLoading(true);
        CustomisedProgressDialog.SHOW_CUST_DIA(this, "Loading Please wait...");

        SoapClientUtil.getMaxValidDateForAdvResvBooking(new SoapClientUtil.GetMaxValidDateForAdvResvBookingCallback() {
            @Override
            public void onSuccess(int year, int month, int date) {
                maxDateFetched = true;

                maxDateForAdvResvBooking = new MonthAdapter.CalendarDay();
                maxDateForAdvResvBooking.setDay(year, month, date);
                if(maxDateFetched && addressListFetched){
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            @Override
            public void onError(Exception e) {
                showLoadingView(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(Timetable_Search.this);
            }
        });

        /** fetch addressplace list **/

        holder.no_internet_text.setVisibility(View.GONE);
        SoapClientUtil.fetchAddressListAsync(new SoapClientUtil.AddressListCallback() {
            @Override
            public void onSuccess(List<Place> placeList) {
                locationList = placeList;
                locationListNames = new ArrayList<String>();
                for(Place location : locationList){
                    locationListNames.add(location.getPlaceName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Timetable_Search.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
                holder.from_location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Timetable_Search.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
                holder.to_location.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                addressListFetched = true;
                if(maxDateFetched && addressListFetched){
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }

            }
            @Override
            public void onError(Exception e) {

                showLoadingView(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(Timetable_Search.this);
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

    public void showLoadingView(boolean show){
        if(show){
            holder.loadingLayout.setVisibility(View.VISIBLE);
        }
        else{
            holder.loadingLayout.setVisibility(View.GONE);
        }
    }

    public void showLoading(boolean show){
        if(show){

            holder.loadingLayout.setVisibility(View.VISIBLE);
            if(addressListFetched && maxDateFetched){

            }else
                holder.search_layout.setVisibility(View.GONE);
        }
        else{

            holder.loadingLayout.setVisibility(View.GONE);
            holder.search_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ViewHolder{
        View container;

        AutoCompleteTextView from_location, to_location;
        Button one_way_btn, round_trip_btn, search_btn;
        EditText journey_date, childrenCount;
        Spinner num_of_passengers;

        TextView no_internet_text,timetable_title;
        View loading_layout, search_layout;
        ProgressBar loading_progressbar;
        View loadingLayout;
        ImageView swap;

        ViewHolder(Activity rootView){
            container = rootView.findViewById(R.id.container);
            from_location = (AutoCompleteTextView) rootView.findViewById(R.id.from_location);
            from_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            to_location = (AutoCompleteTextView) rootView.findViewById(R.id.to_location);
            to_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            swap= (ImageView) rootView.findViewById(R.id.swap);
            search_btn = (Button) rootView.findViewById(R.id.search_btn);
            journey_date = (EditText) rootView.findViewById(R.id.journey_date);
            no_internet_text = (TextView) rootView.findViewById(R.id.no_internet_text);
            timetable_title = (TextView) rootView.findViewById(R.id.time_table_title);
            loading_layout = (View) rootView.findViewById(R.id.loading_layout);
            search_layout = (View) rootView.findViewById(R.id.search_layout);
            loadingLayout =  rootView.findViewById(R.id.loading_layout);
            loadingLayout =  rootView.findViewById(R.id.loading_layout);

            if(textlang.equalsIgnoreCase("gujarati")) {

                from_location.setHint(R.string.bk_from_place);
                to_location.setHint(R.string.bk_to_place);
                search_btn.setText(R.string.bk_search);
                timetable_title.setText(R.string.gTime_Table);
            }

        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Timetable_Search.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
