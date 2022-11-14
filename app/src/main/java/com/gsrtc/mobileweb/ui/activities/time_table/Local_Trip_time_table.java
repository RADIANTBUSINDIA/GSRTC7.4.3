package com.gsrtc.mobileweb.ui.activities.time_table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.BusCurrentService;
import com.gsrtc.mobileweb.models.BusGetRouteDetails;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.CalenderList;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.TrackmybusHistorySearchParam;
import com.gsrtc.mobileweb.ui.activities.advance_booking.PassengerInfoActivity;
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

import static com.gsrtc.mobileweb.ui.activities.time_table.TimeTableServices.SORT_METHOD.DEP_TIME;

public class Local_Trip_time_table extends BaseNavigationDrawerActivity {


    SearchResultAdapter searchResultAdapter;
    //    SearchResultAdapter1 calendersearchAdapter;
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
    //    ActivityViewHolderCrnt vHolderCrnt;
//    SearchResultAdapterOnRoute searchResultAdapterOnRoute;
    List<BusCurrentService> busServiceListCrnt = new ArrayList<>();
    List<BusGetRouteDetails> busRouteList = new ArrayList<>();
    private TextView advcBook,onRoutebook;

    static String StartPlaceID;
    static String EndPlaceID;
    static String JourneyDate;
    static String ClassId;
    private String crntDate;
    private LinearLayout onRouteList,advBookList;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_local__trip_time_table);

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

        //calenderListView();
        setupViews();
    }

    public void calenderListView(){
        // calenderview();
    }

    public void setupViews(){
        vHolder = new ActivityViewHolder(this);

        vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName()+" to "+searchParams.getToLocation().getPlaceName()+" - "+slctDate);

        searchParams.setAdultCount(Integer.parseInt("1"));
        searchParams.setChildrenCount(Integer.parseInt("0"));
        searchParams.setNumOfPassengers(Integer.parseInt("1"));
        searchParams.setOnWardDate(slctDate);

        fetchAvailableServices();
    }


    public void setupListView(List<BusService> serviceList){

        searchResultAdapter = new SearchResultAdapter(this, serviceList) {

            @Override
            public void onItemClick(BusService busService) {
//                Bundle extra = new Bundle();
//                extra.putString("textlang", textlang);
//                extra.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
//                extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
//                Intent intent = new Intent(Local_Trip_time_table.this, SeatSelectionActivity.class);
//                intent.putExtras(extra);
//                startActivityForResult(intent, SEAT_SELECTION_REQUEST);
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
                        comp = new SimpleDateFormat("HH:mm:ss").parse(a1.getDepartureTime()).compareTo(new SimpleDateFormat("HH:mm").parse(a2.getDepartureTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return comp;
                }
            });
        }
    }

    public void fetchAvailableServices(){

        CustomisedProgressDialog.SHOW_CUST_DIA(com.gsrtc.mobileweb.ui.activities.time_table.Local_Trip_time_table.this, "Loading Available Trips...");

        SoapClientUtil.getLocalTimeTableService(searchParams, new SoapClientUtil.GetLocalTimeTableServiceCallback() {
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
                            ActivityUtil.showToast(com.gsrtc.mobileweb.ui.activities.time_table.Local_Trip_time_table.this, "Sorry, no service is available for your Advance Booking");
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
                ActivityUtil.showErrorToast(com.gsrtc.mobileweb.ui.activities.time_table.Local_Trip_time_table.this, e);
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
            View contactView = inflater.inflate(R.layout.local_trip_list, parent, false);

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

            holder.booking_class.setText(busService.getClassName());
            holder.departure_time.setText("Dep.Time : "+busService.getDepTime());
            holder.price.setText("INR "+busService.getAdultFare());
            holder.bus_route.setText(busService.getRouteName());
            holder.arrival_time.setText(busService.getArrivalTime());
            holder.tripcode.setText(busService.getTripCode());
            holder.depotname.setText(busService.getDepotName());
            holder.journey_hours.setText("Jrny.Hrs : "+busService.getJourneyHrs());

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
            TextView booking_class, bus_route, departure_time, price,journey_hours,arrival_time,seat_avail,tripcode,depotname;
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
                tripcode = (TextView) itemView.findViewById(R.id.tripcode);
                depotname = (TextView) itemView.findViewById(R.id.depot_name);

            }
        }
    }

    @Override
    public void onBackPressed() {
        if(onwardBooking !=null){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    com.gsrtc.mobileweb.ui.activities.time_table.Local_Trip_time_table.this, R.style.CustomDialogTheme);

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_calender);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation() );
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);

        mAdapter = new CalenderTableAdapter(this, calenderLists);
        recyclerView.setAdapter(mAdapter);

    }

    private int mGetDaysDifferenceForward(Date paramJourneyDate)
    {
        Date mTodayDate = new Date();
        long diff = mTodayDate.getTime() - paramJourneyDate.getTime();
        return((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)*(-1));
    }

    @SuppressLint("WrongConstant")
    private Date mCalculateDate(Date paramaDate, int paramDay)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(paramaDate);
        c.add(Calendar.DATE, paramDay);
        return c.getTime();
    }

    public class CalenderTableAdapter extends RecyclerView.Adapter<CalenderTableAdapter.ViewHolder> {

        private Context context;
        private List<CalenderList> calenderlist;
        private int selectedItem = -1;
        private int lastSelected =-1;

        public CalenderTableAdapter(Context context, List calenderlist) {
            this.context = context;
            this.calenderlist = calenderlist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_calender_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.itemView.setTag(calenderlist.get(position));

            holder.Clnmonth.setText(calenderlist.get(position).getClnmonth());
            holder.Clndate.setText(calenderlist.get(position).getClndate());
            clndate = calenderlist.get(position).getClndate();
            holder.Clnday.setText(calenderlist.get(position).getClnday());

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    lastSelected =position;
                    notifyDataSetChanged();

                    searchParams.setOnWardDate(calenderlist.get(position).getClndate());

                    slctDate =calenderlist.get(position).getClndate();
                    searchParams.setAdultCount(Integer.parseInt("1"));
                    searchParams.setChildrenCount(Integer.parseInt("0"));
                    searchParams.setNumOfPassengers(Integer.parseInt("1"));
                    slctDate = searchParams.getOnWardDate().trim();
                    vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName()+" to "+searchParams.getToLocation().getPlaceName()+" - "+slctDate);

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
        }

        @Override
        public int getItemCount() {
            return calenderlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView Clnmonth, Clnday, Clndate;
            LinearLayout calviewLay;

            public ViewHolder(View itemView) {
                super(itemView);

                calviewLay = (LinearLayout) itemView.findViewById(R.id.calListlayout);
                Clnmonth = (TextView) itemView.findViewById(R.id.month);
                Clndate = (TextView) itemView.findViewById(R.id.date);
                Clnday = (TextView) itemView.findViewById(R.id.day);

            }
        }
    }
}

