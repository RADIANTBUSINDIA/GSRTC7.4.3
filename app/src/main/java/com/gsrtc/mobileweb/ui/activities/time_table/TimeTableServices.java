package com.gsrtc.mobileweb.ui.activities.time_table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.BusCurrentService;
import com.gsrtc.mobileweb.models.BusGetRouteDetails;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.CalenderList;
import com.gsrtc.mobileweb.models.RouteListAdapter;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.trackmybus.GetPnrDetailsResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.TrackmybusHistorySearchParam;
import com.gsrtc.mobileweb.ui.activities.advance_booking.PassengerInfoActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.SeatSelectionActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.GetCurrentSummaryActivity;
import com.gsrtc.mobileweb.ui.activities.track_mybus_native.CurrentTrackBus;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TrackMyBusNetworkUtil;

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

import static com.gsrtc.mobileweb.ui.activities.time_table.TimeTableServices.SORT_METHOD.DEP_TIME;


public class TimeTableServices extends BaseNavigationDrawerActivity {


    SearchResultAdapter searchResultAdapter;
    SearchResultAdapter1 calendersearchAdapter;
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
    private String textlang,slctDate,clndate;

    public enum SORT_METHOD{
        DEP_TIME, DATE, PRICE;
    }

    public static int SEAT_SELECTION_REQUEST = 1;
    List<BusService> busServiceList = new ArrayList<>();
    List<CalenderList> calenderLists = new ArrayList<>();

    Trackmybus tracking;

    TrackmybusHistorySearchParam trackmyParams;
    SearchParamsCurrent searchParamsCurrent;
    ActivityViewHolderCrnt vHolderCrnt;
    SearchResultAdapterOnRoute searchResultAdapterOnRoute;
    List<BusCurrentService> busServiceListCrnt = new ArrayList<>();
    List<BusGetRouteDetails> busRouteList = new ArrayList<>();
    private TextView advcBook,onRoutebook;

    static String StartPlaceID;
    static String EndPlaceID;
    static String JourneyDate;
    static String ClassId;
    private String crntDate;
    private LinearLayout onRouteList,advBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_time_table_services);

        mSmpDtFrmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        mSmpDtFrmtCall = new SimpleDateFormat("dd/MM/yyyy");

        onRouteList =(LinearLayout)findViewById(R.id.onroute_list);
        advBookList =(LinearLayout)findViewById(R.id.adv_layout);

        advcBook =(TextView) findViewById(R.id.advBook);
        onRoutebook =(TextView) findViewById(R.id.onRouteBook);
        tracking = new Trackmybus();
        tracking.trackmybusHistorySearchParam = new TrackmybusHistorySearchParam();

        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle mBun = getIntent().getExtras();
            textlang =mBun.getString("textlang");
            searchParamsCurrent = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM_ONROUTE);
            searchParams = (SearchParams) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            slctDate =searchParams.getOnWardDate().toString().trim();
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd",Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));

        crntDate=(cur_day+"/"+cur_month+"/"+cur_year);

        if (crntDate.compareTo(slctDate) == 0) {
            onRouteList.setVisibility(View.VISIBLE);
            getOnroutebooking();
            advcBook.setVisibility(View.VISIBLE);
            onRoutebook.setVisibility(View.VISIBLE);
        }
        calenderListView();
        setupViews();
    }

    public void calenderListView(){
        calenderview();
    }

    public void setupViews(){
        vHolder = new ActivityViewHolder(this);

        vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName()+" to "+searchParams.getToLocation().getPlaceName()+" - "+slctDate);

//        vHolder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                CustomisedProgressDialog.SHOW_CUST_DIA(TimeTableServices.this, "please wait loading...");
//
//                if(!dataFetched){
//                    vHolder.loadingLayout.setVisibility(View.VISIBLE);
//                    vHolder.recyclerView.setVisibility(View.GONE);
//                }
//                vHolder.swipeContainer.setRefreshing(false);
//                CustomisedProgressDialog.STOP_CUST_DIA();
//
//                searchParams.setAdultCount(Integer.parseInt("1"));
//                searchParams.setChildrenCount(Integer.parseInt("0"));
//                searchParams.setNumOfPassengers(Integer.parseInt("1"));
//
//                fetchAvailableServices();
//            }
//        });

        searchParams.setAdultCount(Integer.parseInt("1"));
        searchParams.setChildrenCount(Integer.parseInt("0"));
        searchParams.setNumOfPassengers(Integer.parseInt("1"));
        searchParams.setOnWardDate(slctDate);
        searchParams.setDivyangBooking("General");

        fetchAvailableServices();
    }


    public void setupListView(List<BusService> serviceList){

        searchResultAdapter = new SearchResultAdapter(this, serviceList) {

            @Override
            public void onItemClick(BusService busService) {
                Bundle extra = new Bundle();
                extra.putString("textlang", textlang);
                extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                Intent intent = new Intent(TimeTableServices.this, SeatSelectionActivity.class);
                intent.putExtras(extra);
                startActivityForResult(intent, SEAT_SELECTION_REQUEST);
            }
        };
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        vHolder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(vHolder.recyclerView.getContext(),
                layoutManager.getOrientation() );
        vHolder.recyclerView.addItemDecoration(dividerItemDecoration);
        vHolder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();
    }


    public void sortList(List<BusService> unsortedList, SORT_METHOD sort_method) {
        if (unsortedList == null || unsortedList.isEmpty()) {
            return;
        }
        if (sort_method.equals(DEP_TIME)) {
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
        }
    }


    public void fetchAvailableServices(){

        CustomisedProgressDialog.SHOW_CUST_DIA(TimeTableServices.this, "Loading Available Trips...");

        SoapClientUtil.getAvailableServiceDetails(searchParams, (onwardBooking != null), new SoapClientUtil.GetAvailableServiceDetailsCallback() {
            @Override
            public void onSuccess(List<BusService> serviceList) {
                if(serviceList.size() == 0){
                    if(onwardBooking== null){   //No list for onward booking
                        if(busServiceListCrnt.size()==0) {
                            setResult(RESULT_OK);
                            finish();
                        }else{

                            CustomisedProgressDialog.STOP_CUST_DIA();
                            advBookList.setVisibility(View.GONE);
                            ActivityUtil.showToast(TimeTableServices.this, "Sorry, no service is available for your Advance Booking");
                            vHolder.recyclerView.setVisibility(View.GONE);

                        }
                    }
                }else {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    busServiceList = serviceList;

                    sortList(busServiceList, SORT_METHOD.DEP_TIME);
                    setupListView(busServiceList);

                    searchResultAdapter.notifyDataSetChanged();
                    vHolder.loadingLayout.setVisibility(View.GONE);
                    vHolder.recyclerView.setVisibility(View.VISIBLE);
                    showLoading(false);
                }
            }

            @Override
            public void onError(Exception e) {
                vHolder.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(TimeTableServices.this, e);
            }
        });
    }


    public void showLoading(boolean show){
        if(show){
            vHolder.loadingLayout.setVisibility(View.VISIBLE);
        }
        else{
            vHolder.loadingLayout.setVisibility(View.GONE);
        }
    }

    public class ActivityViewHolder{
        View rootView, loadingLayout;
        RecyclerView recyclerView,clndRecyclerView;
        TextView header_text;
        SwipeRefreshLayout swipeContainer;

        ActivityViewHolder(Activity activity){
            rootView = activity.findViewById(R.id.rootView);
            header_text = (TextView)activity.findViewById(R.id.header_text);
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout =  activity.findViewById(R.id.search_results_loading);
            recyclerView = (RecyclerView) activity.findViewById(R.id.search_results_recycler_view);
            clndRecyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_calender);
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
                        ActivityUtil.openNewActivity(this, PassengerInfoActivity.class, extras);
                    }
                    if (searchParams.getReturnDate() == null) {   //No return date has to be selected
                        Bundle extras = new Bundle();
                        extras.putString("textlang", textlang);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                        ActivityUtil.openNewActivity(this, PassengerInfoActivity.class, extras);
                    }
                }
            }
        }
    }



    static abstract class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

        private List<BusService> busList;
        Context context;

        public SearchResultAdapter(Context context, List<BusService> busList) {
            if(busList == null)
                busList = new ArrayList<BusService>();
            this.busList = busList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.time_table_list, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BusService busService = this.busList.get(position);

            holder.container.setTag(position);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer pos = (Integer)view.getTag();
                    onItemClick(busList.get(pos));
                    notifyDataSetChanged();
                }
            });

            holder.booking_class.setText(busService.getServiceClass());
            holder.departure_time.setText(busService.getDepartureTime()+":00");
           // holder.actual_price.setText("INR "+busService.getAdultFare());
            holder.price.setText("INR "+busService.getAdultFare());
            holder.bus_route.setText(busService.getOrigin()+"-"+busService.getDestination());
            holder.arrival_time.setText(busService.getArrivalTime());
            holder.via_places.setText("Via Places  : "+busService.getViaPlaces());
            holder.tripco.setText(busService.getTripCode());
            holder.journey_hours.setText(busService.getJourneyHours()+" hrs");

//            if(busService.getServiceClass().equalsIgnoreCase("VOLVO SLEEPER") || busService.getServiceClass().equalsIgnoreCase("AC SLEEPER")|| busService.getServiceClass().equalsIgnoreCase("VOLVO")|| busService.getServiceClass().equalsIgnoreCase("AC LUXURY")){
//
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


            if(busService.getNoOfSeats().equalsIgnoreCase("0")){

                holder.seat_avail.setTextColor(Color.parseColor("#ff3333"));
                holder.seat_avail.setText("FULL");

            }else {

                holder.seat_avail.setTextColor(Color.parseColor("#00B700"));
                holder.seat_avail.setText(busService.getNoOfSeats() + " seats");
            }
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
            TextView booking_class, bus_route, departure_time, price,journey_hours,arrival_time,seat_avail,via_places,tripco,actual_price;
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
                via_places = (TextView) itemView.findViewById(R.id.via_places);
                tripco = (TextView) itemView.findViewById(R.id.tripcode);
                actual_price = (TextView) itemView.findViewById(R.id.actual_price);

            }
        }
    }



    public class SearchResultAdapter1 extends RecyclerView.Adapter<SearchResultAdapter1.ViewHolder>{

        private List<CalenderList> calenderlist;
        Context context;
        private int lastSelected =-1;

        public SearchResultAdapter1(Context context, List<CalenderList> calenderlist) {
            if(calenderlist == null)
                calenderlist = new ArrayList<CalenderList>();
            this.calenderlist = calenderlist;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.time_table_calender_list, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            CalenderList calenderlst = this.calenderlist.get(position);

            holder.container.setTag(position);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastSelected =position;
                    notifyDataSetChanged();

                    searchParams.setOnWardDate(calenderlist.get(position).getClndate());

                    slctDate =calenderlist.get(position).getClndate();
                    searchParams.setAdultCount(Integer.parseInt("1"));
                    searchParams.setChildrenCount(Integer.parseInt("0"));
                    searchParams.setNumOfPassengers(Integer.parseInt("1"));
                    slctDate = searchParams.getOnWardDate().toString().trim();
                    vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName()+" to "+searchParams.getToLocation().getPlaceName()+" - "+slctDate);

                    if (crntDate.compareTo(slctDate) == 0) {
                        onRouteList.setVisibility(View.VISIBLE);
                        getOnroutebooking();
                        advcBook.setVisibility(View.VISIBLE);
                        onRoutebook.setVisibility(View.VISIBLE);
                    }else {

                        onRouteList.setVisibility(View.GONE);
                        advcBook.setVisibility(View.GONE);
                        onRoutebook.setVisibility(View.GONE);
                    }

                    fetchAvailableServices();
                }
            });

            if(lastSelected==position){
                holder.calviewLay.setBackgroundColor(Color.parseColor("#E94054"));
            }
            else
            {
                holder.calviewLay.setBackgroundColor(Color.parseColor("#647B93"));
            }

            holder.Clnmonth.setText(calenderlst.getClnmonth());
            holder.Clndate.setText(calenderlst.getClndate());
            clndate =calenderlst.getClndate().toString();
            holder.Clnday.setText(calenderlst.getClnday());
        }


        public void onItemClick(CalenderList calenderList) {

            searchParams.setOnWardDate(calenderList.getClndate());
            searchParams.setAdultCount(Integer.parseInt("1"));
            searchParams.setChildrenCount(Integer.parseInt("0"));
            searchParams.setNumOfPassengers(Integer.parseInt("1"));

            fetchAvailableServices();
        }

        @Override
        public int getItemCount() {
            return calenderlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView Clnmonth, Clnday, Clndate;
            LinearLayout calviewLay;
            public View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;
                calviewLay = (LinearLayout) itemView.findViewById(R.id.calListlayout);
                Clnmonth = (TextView) itemView.findViewById(R.id.month);
                Clndate = (TextView) itemView.findViewById(R.id.date);
                Clnday = (TextView) itemView.findViewById(R.id.day);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(onwardBooking !=null){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    TimeTableServices.this, R.style.CustomDialogTheme);

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
        }
        else{
            finish();
        }
    }

    public void calenderview() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM",Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd",Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));
        String crntdate =cur_day+"/"+cur_month+"/"+cur_year;

        for (int i = 0; i < 60; i++) {

            CalenderList mCalndr = new CalenderList();

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            Date mDd = null;
            try {
                mDd = sdf2.parse(crntdate);
            } catch (ParseException e1) {
                // 	TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mJourneyStartDate = new Date();
            mJourneyStartDate.setTime(mDd.getTime());
            //mProcessDateForServer(mSmpDtFrmt.format(mDd));

            int diffDay = mGetDaysDifferenceForward(mCalculateDate(mJourneyStartDate, i));
            if (diffDay < 60 && diffDay >= 0) {
                mJourneyStartDate = mCalculateDate(mJourneyStartDate, i);
                String jnyDate = mSmpDtFrmtCall.format(mJourneyStartDate);
                String jnyDate1 = String.valueOf(mJourneyStartDate);

                String s1 = jnyDate1.substring(0, 3);
                String s2 = jnyDate1.substring(4,7);

                mCalndr.setClndate(jnyDate);
                mCalndr.setClnday(s1);
                mCalndr.setClnmonth(s2);
                calenderLists.add(i, mCalndr);
            }
        }

        vHolder = new ActivityViewHolder(this);
        calendersearchAdapter = new SearchResultAdapter1(this, calenderLists);

        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        vHolder.clndRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(vHolder.clndRecyclerView.getContext(),
                layoutManager.getOrientation() );
        vHolder.clndRecyclerView.addItemDecoration(dividerItemDecoration);
        vHolder.clndRecyclerView.setAdapter(calendersearchAdapter);
        calendersearchAdapter.notifyDataSetChanged();
    }

    private int mGetDaysDifferenceForward(Date paramJourneyDate)
    {
        Date mTodayDate = new Date();
        long diff = mTodayDate.getTime() - paramJourneyDate.getTime();
        return((int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)*(-1));
    }

    @SuppressLint("WrongConstant")
    private Date mCalculateDate(Date paramaDate, int paramDay)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(paramaDate);
        c.add(Calendar.DATE, paramDay);
        return c.getTime();
    }


    private String mProcessDateForServer(String paramDate)
    {
        String mDateForWS="";
        String mDay= paramDate.trim().split(" ")[0];
        if(mDay.length()<2)
            mDay="0"+mDay;
        Months mMon = Months.valueOf(paramDate.trim().split(" ")[1]);
        switch (mMon)
        {
            case Jan:
                mDateForWS = mDay+"/01/"+ paramDate.trim().split(" ")[2];
                break;
            case Feb:
                mDateForWS = mDay+"/02/"+ paramDate.trim().split(" ")[2];
                break;
            case Mar:
                mDateForWS = mDay+"/03/"+paramDate.trim().split(" ")[2];
                break;
            case Apr:
                mDateForWS = mDay+"/04/"+paramDate.trim().split(" ")[2];
                break;
            case May:
                mDateForWS = mDay+"/05/"+paramDate.trim().split(" ")[2];
                break;
            case Jun:
                mDateForWS = mDay+"/06/"+paramDate.trim().split(" ")[2];
                break;
            case Jul:
                mDateForWS = mDay+"/07/"+paramDate.trim().split(" ")[2];
                break;
            case Aug:
                mDateForWS = mDay+"/08/"+paramDate.trim().split(" ")[2];
                break;
            case Sep:
                mDateForWS = mDay+"/09/"+paramDate.trim().split(" ")[2];
                break;
            case Oct:
                mDateForWS = mDay+"/10/"+paramDate.trim().split(" ")[2];
                break;
            case Nov:
                mDateForWS = mDay+"/11/"+paramDate.trim().split(" ")[2];
                break;
            case Dec:
                mDateForWS = mDay+"/12/"+paramDate.trim().split(" ")[2];
                break;
            default:
                break;
        }

        searchParams.setOnWardDate(String.valueOf(mJourneyStartDate.getTime()));
        return mDateForWS;
    }
    private enum Months
    {
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

    private void getOnroutebooking(){

        vHolderCrnt = new ActivityViewHolderCrnt(this);

        setupOnRouteList(null);

        fetchOnRouteServices();
    }

    public void fetchOnRouteServices() {

        searchParamsCurrent.setAdultCount(Integer.parseInt("1"));
        searchParamsCurrent.setChildrenCount(Integer.parseInt("0"));
        searchParamsCurrent.setNumOfPassengers(Integer.parseInt("1"));
        searchParamsCurrent.setCrntlanguage(textlang);
        searchParamsCurrent.setOnWardDate(crntDate);

        SoapClientUtil.getAvailableCurrentServiceDetails(searchParamsCurrent, new SoapClientUtil.GetAvailableCurrentServiceDetailsCallback() {
            @Override
            public void onSuccess(List<BusCurrentService> serviceList) {

                if (serviceList.size() != 0) {
                    busServiceListCrnt = serviceList;

                    setupOnRouteList(busServiceListCrnt);
                    vHolderCrnt.loadingLayout.setVisibility(View.GONE);
                    vHolderCrnt.recyclerViewCrnt.setVisibility(View.VISIBLE);
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                } else {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    onRouteList.setVisibility(View.GONE);
                    advcBook.setVisibility(View.GONE);
                    onRoutebook.setVisibility(View.GONE);
                    ActivityUtil.showToast(TimeTableServices.this, "Sorry, no OnRoute Booking bus service is available for your selection");
//                    Intent i = new Intent(TimeTableServices.this, CurrentBookingHomeActivity.class);
//                    Bundle data = new Bundle();
//                    data.putString("textlang", textlang);
                    vHolderCrnt.loadingLayout.setVisibility(View.GONE);
                    vHolderCrnt.recyclerViewCrnt.setVisibility(View.VISIBLE);
//                    showLoading(false);
//                    i.putExtras(data);
//                    startActivity(i);
                }
            }

            @Override
            public void onError(Exception e) {
                vHolderCrnt.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(TimeTableServices.this, e);
            }
        });
    }

    public static class ActivityViewHolderCrnt {
        View rootView, loadingLayout;
        RecyclerView recyclerViewCrnt;
        TextView header_text;
        SwipeRefreshLayout swipeContainer;

        ActivityViewHolderCrnt(Activity activity) {
            rootView = activity.findViewById(R.id.rootView);
            header_text = (TextView) activity.findViewById(R.id.header_text);
//            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout = activity.findViewById(R.id.search_results_loading);
            recyclerViewCrnt = (RecyclerView) activity.findViewById(R.id.search_results_onroute_view);
        }
    }

    public void setupOnRouteList(List<BusCurrentService> serviceList) {
        searchResultAdapterOnRoute = new SearchResultAdapterOnRoute(this, serviceList) {
            @Override
            public void onItemClick(BusCurrentService busService) {

            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vHolderCrnt.recyclerViewCrnt.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(vHolderCrnt.recyclerViewCrnt.getContext(),
                layoutManager.getOrientation());
        vHolderCrnt.recyclerViewCrnt.addItemDecoration(dividerItemDecoration);
        vHolderCrnt.recyclerViewCrnt.setAdapter(searchResultAdapterOnRoute);
        searchResultAdapterOnRoute.notifyDataSetChanged();
    }


    abstract class SearchResultAdapterOnRoute extends RecyclerView.Adapter<SearchResultAdapterOnRoute.ViewHolder> {

        private List<BusCurrentService> busList;
        Context context;

        public SearchResultAdapterOnRoute(Context context, List<BusCurrentService> busList) {
            if (busList == null)
                busList = new ArrayList<BusCurrentService>();
            this.busList = busList;
            this.context = context;
        }

        @Override
        public SearchResultAdapterOnRoute.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.current_booking_list_item, parent, false);
            // Return a new holder instance
            return new SearchResultAdapterOnRoute.ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(final SearchResultAdapterOnRoute.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final BusCurrentService busService = this.busList.get(position);

            holder.container.setTag(position);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer pos = (Integer) view.getTag();
                    Log.e("Position", String.valueOf(pos));
                    onItemClick(busList.get(pos));
                }
            });

            String routeName = busService.getVehicleNo() + "-[ " + busService.getServiceClass() + " ] [DEP: " + busService.getDepartureTime() + "]";
            String lastDeparted = busService.getLastBusStation() + ", " + busService.getLastBusStationTime();
            holder.tvDeparted.setText(lastDeparted);
            holder.tvServiceRouteName.setText(busService.getServiceRouteName());
            holder.tvVehicleNo.setText(routeName);

            if (!busService.getVehicleStatus().contentEquals("N/A")) {
                holder.tvBusRunningStatus.setTextColor(Color.GREEN);
            } else {
                holder.tvBusRunningStatus.setTextColor(Color.BLACK);
            }
            holder.tvBusRunningStatus.setText(busService.getVehicleStatus());

            if (textlang.equalsIgnoreCase("gujarati")) {
                holder.btnBook.setText("નોંધ કરો \u20B9" + busService.getAdultFare());
            } else {
                holder.btnBook.setText("Book \u20B9" + busService.getAdultFare());
            }
            holder.tvSeatsAvailable.setText(busService.getSeatavailble());
            holder.mobile_conductor.setText(busService.getConductorMobileNo());
            holder.adult_fare.setText(busService.getAdultFare());
            holder.kilomtrs.setText(busService.getKms() + "kms");
            holder.tripcode.setText(busService.getTripCode());
            //holder.callopt.setText(busService.getConductorMobileNo());

            JourneyDate = busService.getJourneyDate();
            Log.e("JourneyDate", busService.getJourneyDate());
            Log.e("PickServiceId", busService.getPickServiceId());
            ClassId = busService.getClassID();
            Log.e("ClassId", busService.getClassID());

            holder.btnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
                    SimpleDateFormat df2 = new SimpleDateFormat("dd", Locale.getDefault());
                    int cur_year = Integer.valueOf(df1.format(c.getTime()));
                    String cur_month = String.valueOf(df.format(c.getTime()));
                    String cur_day = String.valueOf(df2.format(c.getTime()));
                    tracking.trackmybusHistorySearchParam.CurrentDate = (cur_year + "-" + cur_month + "-" + cur_day);
                    tracking.trackmybusHistorySearchParam.VehicleNo = busService.getVehicleNo();
                    tracking.trackmybusHistorySearchParam.PnrNo = "";
                    tracking.trackmybusHistorySearchParam.TripCode = "";
                    tracking.trackmybusHistorySearchParam.StatusCode = "V";
                    goNextpnr();
                }
            });

            holder.btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("Position", String.valueOf(position));
                    Log.e("adultFare", busList.get(position).getAdultFare());
                    Log.e("childFare", busList.get(position).getChildFare());

                    String pickServiceID = busList.get(position).getPickServiceId();
                    String departureTime = busList.get(position).getDepartureTime();

                    searchParamsCurrent.setServiceRouteName(holder.tvServiceRouteName.getText().toString());
                    searchParamsCurrent.setJourneyDate(JourneyDate);
                    searchParamsCurrent.setPickServiceId(pickServiceID);
                    searchParamsCurrent.setDepartureTime(departureTime);
                    searchParamsCurrent.setClassId(ClassId);
                    searchParamsCurrent.setRouteNameAppended(holder.tvVehicleNo.getText().toString());

                    searchParamsCurrent.setAdultFareGetCurrentServiceDetails(busList.get(position).getAdultFare());
                    searchParamsCurrent.setAdultTicketFareGetCurrentServiceDetails(busList.get(position).getAdultTicketFare());
                    searchParamsCurrent.setChildFareGetCurrentServiceDetails(busList.get(position).getChildFare());
                    searchParamsCurrent.setChildTicketFareGetCurrentServiceDetails(busList.get(position).getChildTicketFare());
                    searchParamsCurrent.setClassIDGetCurrentServiceDetails(busList.get(position).getClassID());
                    searchParamsCurrent.setConductorMobileNoGetCurrentServiceDetails(busList.get(position).getConductorMobileNo());
                    searchParamsCurrent.setDepartureTimeGetCurrentServiceDetails(busList.get(position).getDepartureTime());
                    searchParamsCurrent.setDestinationGetCurrentServiceDetails(busList.get(position).getDestination());
                    searchParamsCurrent.setJourneyDateGetCurrentServiceDetails(busList.get(position).getJourneyDate());
                    searchParamsCurrent.setJourneyHoursGetCurrentServiceDetails(busList.get(position).getJourneyDate());
                    searchParamsCurrent.setLastBusStationGetCurrentServiceDetails(busList.get(position).getLastBusStation());
                    searchParamsCurrent.setLastBusStationTimeGetCurrentServiceDetails(busList.get(position).getLastBusStationTime());
                    searchParamsCurrent.setOriginGetCurrentServiceDetails(busList.get(position).getOrigin());
                    searchParamsCurrent.setPickServiceIdGetCurrentServiceDetails(busList.get(position).getPickServiceId());
                    searchParamsCurrent.setSeatavailbleGetCurrentServiceDetails(busList.get(position).getSeatavailble());
                    searchParamsCurrent.setServiceClassGetCurrentServiceDetails(busList.get(position).getServiceClass());
                    searchParamsCurrent.setServiceIDGetCurrentServiceDetails(busList.get(position).getServiceID());
                    searchParamsCurrent.setServiceOrginTimeGetCurrentServiceDetails(busList.get(position).getServiceOrginTime());
                    searchParamsCurrent.setServiceRouteIdGetCurrentServiceDetails(busList.get(position).getServiceRouteId());
                    searchParamsCurrent.setServiceRouteNameGetCurrentServiceDetails(busList.get(position).getServiceRouteName());
                    searchParamsCurrent.setTripCodeGetCurrentServiceDetails(busList.get(position).getTripCode());
                    searchParamsCurrent.setVehicleIdGetCurrentServiceDetails(busList.get(position).getVehicleId());
                    searchParamsCurrent.setVehicleNoGetCurrentServiceDetails(busList.get(position).getVehicleNo());
                    searchParamsCurrent.setVehicleStatusGetCurrentServiceDetails(busList.get(position).getVehicleStatus());
                    searchParamsCurrent.setKms(busList.get(position).getKms());

                    searchParamsCurrent.setAdultFareServiceDetails(busList.get(position).getAdultFare());
                    searchParamsCurrent.setChildFareServiceDetails(busList.get(position).getChildFare());

                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParamsCurrent);
                    ActivityUtil.openNewActivity(v.getContext(), GetCurrentSummaryActivity.class, extra);

                }
            });

            holder.linearLayoutRouteDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //startAuthDialog();
                    String pickServiceID = busList.get(position).getPickServiceId();
                    String departureTime = busList.get(position).getDepartureTime();
                    startRouteDetailsDialog(pickServiceID);
                }
            });
        }

        private void startRouteDetailsDialog(String pickServiceID) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TimeTableServices.this);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            LayoutInflater inflater = TimeTableServices.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.route_details_dialog, null);
            alertDialog.getWindow().setContentView(dialogView);
            Button dialogButton = (Button) dialogView.findViewById(R.id.btnRouteDetailsClose);
            final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
            final ListView listView = (ListView) dialogView.findViewById(R.id.lvRouteDetails);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            SoapClientUtil.getRouteDetails(searchParamsCurrent, pickServiceID, new SoapClientUtil.GetRouteDetailsCallback() {
                @Override
                public void onSuccess(List<BusGetRouteDetails> routeDetailsList) {

                    busRouteList = routeDetailsList;
                    RouteListAdapter routeListAdapter = new RouteListAdapter(TimeTableServices.this, R.layout.route_details_listview_item, busRouteList);
                    listView.setAdapter(routeListAdapter);
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        private void startAuthDialog() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TimeTableServices.this);
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setLayout(800, 1400);
            LayoutInflater inflater = TimeTableServices.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.route_details_dialog, null);
            alertDialog.getWindow().setContentView(dialogView);
        }

        // Clean all elements of the recycler
        public void clear() {
            busList.clear();
            notifyDataSetChanged();
        }

        // Add a list of items
        public void addAll(List<BusCurrentService> list) {
            busList.addAll(list);
            notifyDataSetChanged();
        }

        public abstract void onItemClick(BusCurrentService site);

        @Override
        public int getItemCount() {
            return busList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvServiceRouteName, tvVehicleNo, tvDeparted;
            TextView tvBusRunningStatus;
            TextView tvSeatsAvailable, mobile_conductor, adult_fare, kilomtrs, tripcode;
            TextView busnotxt, departtxt, seatavailtxt, routetxt, footertxt, conductor_mob, fare, kms, tripcotxt;

            Button btnBook, btnTrack, callopt;
            LinearLayout linearLayoutRouteDetails;

            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                tvServiceRouteName = (TextView) itemView.findViewById(R.id.li_FromToPlace);
                tvVehicleNo = (TextView) itemView.findViewById(R.id.bus_NO_Text);
                tvDeparted = (TextView) itemView.findViewById(R.id.departed_Text);
                tvBusRunningStatus = (TextView) itemView.findViewById(R.id.bus_running_status_Text);
                tvSeatsAvailable = (TextView) itemView.findViewById(R.id.seatavailable_Heading_Text);
                mobile_conductor = (TextView) itemView.findViewById(R.id.conductor_mobilenum);
                adult_fare = (TextView) itemView.findViewById(R.id.adult_fare);
                kilomtrs = (TextView) itemView.findViewById(R.id.kilo_meters);
                tripcode = (TextView) itemView.findViewById(R.id.tripco_Text);
//                callopt = (Button) itemView.findViewById(R.id.call_icon);
                btnBook = (Button) itemView.findViewById(R.id.li_book_btn);
                btnTrack = (Button) itemView.findViewById(R.id.li_track_btn);
                linearLayoutRouteDetails = (LinearLayout) itemView.findViewById(R.id.linearLayoutRouteDetails);

                busnotxt = (TextView) itemView.findViewById(R.id.bus_NO_Heading);
                departtxt = (TextView) itemView.findViewById(R.id.departed_Heading);
                seatavailtxt = (TextView) itemView.findViewById(R.id.seatavailable_Heading);
                routetxt = (TextView) itemView.findViewById(R.id.li_RouteDetails);
                footertxt = (TextView) itemView.findViewById(R.id.bus_footer);
                conductor_mob = (TextView) itemView.findViewById(R.id.conductor_mobile);
                fare = (TextView) itemView.findViewById(R.id.adult);
                kms = (TextView) itemView.findViewById(R.id.kms);
                tripcotxt = (TextView) itemView.findViewById(R.id.tripco_Heading);

                if (textlang.equalsIgnoreCase("gujarati")) {

                    busnotxt.setText(R.string.crtserv_busno);
                    departtxt.setText(R.string.crtserv_departed);
                    seatavailtxt.setText(R.string.crtserv_seats);
                    routetxt.setText(R.string.crtserv_route);
                    footertxt.setText(R.string.crtserv_text);
                    btnBook.setText(R.string.crtserv_book);
                    btnBook.setText(R.string.gtrack_bus);
                    conductor_mob.setText(R.string.trvehic_con_mobile);
                    fare.setText(R.string.crtserv_adult_fare);
                    kms.setText(R.string.crtserv_kms);
                    tripcotxt.setText(R.string.smsdet_tripco);

                }
            }
        }
    }


    public void goNextpnr()
    {
        if (tracking.getPnrDetailsResponse == null) {
            CustomisedProgressDialog.SHOW_CUST_DIA(TimeTableServices.this, "Loading Available Trips...");
            getAllBusespnr();
            return;
        }
        else {
            hideProgressDialog();
        }
        CustomisedProgressDialog.STOP_CUST_DIA();
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putString("bookType","TIME_TABLE");
        bundle.putSerializable(AppConstants.INTENT_EXTRA.TRACK_MY_BUS, tracking);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParamsCurrent);
        ActivityUtil.openNewActivity(this, CurrentTrackBus.class, bundle);
    }

    private void getAllBusespnr() {

        TrackMyBusNetworkUtil.GetPnrDetails(tracking, new TrackMyBusNetworkUtil.GetPnrDetailsCallback() {

            public void onSuccess(GetPnrDetailsResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        //hideProgressDialog();
                        ActivityUtil.showToast(TimeTableServices.this, "No Data Available");
                    }
                    tracking.getPnrDetailsResponse = response;
                    //recycleviewProcesspnr();
                    goNextpnr();

                } else {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(TimeTableServices.this, "No Data Available");
                    //hideProgressDialog();
                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                //hideProgressDialog();
                ActivityUtil.showErrorToast(TimeTableServices.this, e);
            }
        });
    }

    public  void showProgressDialog() {
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
}
