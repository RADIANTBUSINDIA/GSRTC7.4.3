package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import java.util.ArrayList;
import java.util.List;


public class CurrentBookingHomeActivity extends BaseNavigationDrawerActivity{

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
    ViewHolder holder;
    SearchParamsCurrent searchParams;
    CalendarDatePickerDialogFragment calendarDialog;
    String textlang;

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
        setContentViewDrawer(R.layout.current_booking_home);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        setupViews();
    }

    public void setupViews(){
        holder = new ViewHolder(this);
        searchParams = new SearchParamsCurrent();
        searchParams.setCrntlanguage(textlang);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationListNames);
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

        holder.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromvalue=holder.from_location.getText().toString();
                String tovalue=holder.to_location.getText().toString();
                if ((fromvalue.matches("")) || tovalue.matches("")){

                    Toast.makeText(CurrentBookingHomeActivity.this, "Please Enter valid place", Toast.LENGTH_SHORT).show();
                    return;
                }

                holder.from_location.setText(tovalue);
                holder.to_location.setText(fromvalue);
                holder.to_location.clearFocus();
                holder.from_location.clearFocus();

            }
        });

        holder.search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResultActivity();
            }
        });

        fetchServerData();
    }

    public void goToResultActivity(){
        if(TextUtils.isEmpty(holder.adultCount.getText().toString()) && TextUtils.isEmpty(holder.childrenCount.getText().toString())){
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be zero");
            return;
        }

        if(TextUtils.isEmpty(holder.adultCount.getText().toString())){
            ActivityUtil.showSnackBar(holder.container, "No. of Adult passengers can not be zero");
            return;
        }

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

        if(!TextUtils.isEmpty(holder.adultCount.getText().toString())){
            searchParams.setAdultCount(Integer.parseInt(holder.adultCount.getText().toString()));
        }
        if(!TextUtils.isEmpty(holder.childrenCount.getText().toString())){
            searchParams.setChildrenCount(Integer.parseInt(holder.childrenCount.getText().toString()));
            Log.e("Child", holder.childrenCount.getText().toString());
        }else {
            searchParams.setChildrenCount(0);
            Log.e("Child", "0");
        }

        searchParams.setFromLocation(startPlace);
        searchParams.setToLocation(endPlace);
        searchParams.setNumOfPassengers(searchParams.getAdultCount()+searchParams.getChildrenCount());

        if(searchParams.getNumOfPassengers() == 0){
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be zero");
            return;
        }
        if(searchParams.getNumOfPassengers() > 6){
            ActivityUtil.showSnackBar(holder.container, "No. of passengers can not be greater than 6");
            return;
        }

        if(searchParams.getAdultCount() < 1){
            ActivityUtil.showSnackBar(holder.container, "No. of Adult passengers can not be zero");
            return;
        }

        Bundle extra = new Bundle();
        extra.putString("textlang", textlang);
        extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
        ActivityUtil.openNewActivityForResult(CurrentBookingHomeActivity.this, CurrentBookingSearchResultActivity.class, extra, REQUEST_NO_SERVICE_AVAILABLE);

    }

    public void fetchServerData(){
        showLoading(true);
        CustomisedProgressDialog.SHOW_CUST_DIA(CurrentBookingHomeActivity.this, "Loading Please wait....");

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
                ActivityUtil.showNoNetworkToast(CurrentBookingHomeActivity.this);
            }
        });

        holder.no_internet_text.setVisibility(View.GONE);
        SoapClientUtil.fetchAddressListAsync(new SoapClientUtil.AddressListCallback() {
            @Override
            public void onSuccess(List<Place> placeList) {
                locationList = placeList;
                locationListNames = new ArrayList<String>();
                for(Place location : locationList){
                    locationListNames.add(location.getPlaceName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentBookingHomeActivity.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
                holder.from_location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CurrentBookingHomeActivity.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
                holder.to_location.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                addressListFetched = true;
                if(maxDateFetched && addressListFetched){
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
                holder.copypolicy_one.setVisibility(View.VISIBLE);
                holder.copypolicy_title.setVisibility(View.VISIBLE);
                holder.copypolicy_two.setVisibility(View.VISIBLE);
                //holder.copyright.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                showLoadingView(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(CurrentBookingHomeActivity.this);
            }
        });
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

    public class ViewHolder{
        View container;
        AutoCompleteTextView from_location, to_location;
        Button search_btn;
        EditText adultCount, childrenCount;
        Spinner num_of_passengers;

        TextView no_internet_text;
        View loading_layout, search_layout;
        ProgressBar loading_progressbar;
        View loadingLayout;
        ImageView swap;
        TextView copypolicy_two,copyright,copypolicy_one,title,copypolicy_title;

        ViewHolder(Activity rootView){
            container = rootView.findViewById(R.id.container);
            from_location = (AutoCompleteTextView) rootView.findViewById(R.id.from_location);
            from_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            to_location = (AutoCompleteTextView) rootView.findViewById(R.id.to_location);
            to_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            title = (TextView)rootView.findViewById(R.id.title_current);
            copypolicy_two = (TextView)rootView.findViewById(R.id.copypolicy_two);
            copypolicy_one = (TextView)rootView.findViewById(R.id.copypolicy);
            copypolicy_title=(TextView)rootView.findViewById(R.id.copypolicytitle);
            swap= (ImageView) rootView.findViewById(R.id.swap);
            search_btn = (Button) rootView.findViewById(R.id.search_btn);

            adultCount = (EditText) rootView.findViewById(R.id.adultCount);
            childrenCount = (EditText) rootView.findViewById(R.id.childrenCount);
            adultCount.setInputType(InputType.TYPE_CLASS_NUMBER);
            childrenCount.setInputType(InputType.TYPE_CLASS_NUMBER);

            no_internet_text = (TextView) rootView.findViewById(R.id.no_internet_text);
            loading_layout = (View) rootView.findViewById(R.id.loading_layout);
            search_layout = (View) rootView.findViewById(R.id.search_layout);
            loadingLayout =  rootView.findViewById(R.id.loading_layout);

            if(textlang.equalsIgnoreCase("gujarati")) {
                title.setText(R.string.crnt_current_tkt);
                from_location.setHint(R.string.crnt_from);
                to_location.setHint(R.string.crnt_to);
                adultCount.setHint(R.string.crnt_adults);
                childrenCount.setHint(R.string.crnt_child);
                search_btn.setText(R.string.crnt_searc_btn);
                copypolicy_title.setText(R.string.gcrnt_policy_title);
                copypolicy_one.setText(R.string.gcrnt_policy_one);
                copypolicy_two.setText(R.string.gcrnt_policy_two);
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(CurrentBookingHomeActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);

    }
}
