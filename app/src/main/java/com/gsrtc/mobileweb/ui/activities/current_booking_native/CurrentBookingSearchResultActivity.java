package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.BusCurrentService;

import com.gsrtc.mobileweb.models.BusGetRouteDetails;
import com.gsrtc.mobileweb.models.RouteListAdapter;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.trackmybus.GetPnrDetailsResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.TrackmybusHistorySearchParam;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.track_mybus_native.CurrentTrackBus;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TrackMyBusNetworkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CurrentBookingSearchResultActivity extends BaseNavigationDrawerActivity {

    Trackmybus tracking;
    ProgressDialog progressDialog;
    TrackmybusHistorySearchParam trackmyParams;
    SearchParamsCurrent searchParams;
    ActivityViewHolder vHolder;
    SearchResultAdapter searchResultAdapter;
    List<BusCurrentService> busServiceList = new ArrayList<>();
    List<BusGetRouteDetails> busRouteList = new ArrayList<>();
    boolean dataFetched = false;
    private String call_phone;

    static String StartPlaceID;
    static String EndPlaceID;
    static String JourneyDate;
    static String ClassId;

    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_current_booking_search_result_screen);

        if (getIntent() != null && getIntent().getExtras() != null) {
            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);

            textlang = searchParams.getCrntlanguage().trim();
            StartPlaceID = searchParams.getFromLocation().getPlaceID();
            EndPlaceID = searchParams.getToLocation().getPlaceID();

            tracking = new Trackmybus();
            tracking.trackmybusHistorySearchParam = new TrackmybusHistorySearchParam();
        }
        setupViews();
    }

    public void setupViews() {
        vHolder = new ActivityViewHolder(this);

        vHolder.header_text.setText(searchParams.getFromLocation().getPlaceName() + " to " + searchParams.getToLocation().getPlaceName());
        setupListView(null);
        vHolder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                vHolder.swipeContainer.setRefreshing(false);
                if (!dataFetched) {
                    vHolder.loadingLayout.setVisibility(View.VISIBLE);
                    vHolder.recyclerView.setVisibility(View.GONE);
                }
                fetchAvailableServices();
            }
        });

        fetchAvailableServices();
    }

    public void setupListView(List<BusCurrentService> serviceList) {
        searchResultAdapter = new SearchResultAdapter(this, serviceList) {
            @Override
            public void onItemClick(BusCurrentService busService) {

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

    public static class ActivityViewHolder {
        View rootView, loadingLayout;
        RecyclerView recyclerView;
        TextView header_text;
        SwipeRefreshLayout swipeContainer;

        ActivityViewHolder(Activity activity) {
            rootView = activity.findViewById(R.id.rootView);
            header_text = (TextView) activity.findViewById(R.id.header_text);
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout = activity.findViewById(R.id.search_results_loading);
            recyclerView = (RecyclerView) activity.findViewById(R.id.search_results_recycler_view);
            //ActivityUtil.loadLoadingImage(activity, (ImageView)activity.findViewById(R.id.loading_gif));
        }
    }

    public static class RouteViewHolder {
        View rootView, loadingLayout;
        RecyclerView recyclerView;
        TextView header_text;
        SwipeRefreshLayout swipeContainer;


        RouteViewHolder(Activity activity) {
            rootView = activity.findViewById(R.id.rootView);
            header_text = (TextView) activity.findViewById(R.id.header_text);
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout = activity.findViewById(R.id.search_results_loading);
            recyclerView = (RecyclerView) activity.findViewById(R.id.search_results_recycler_view);

            // ActivityUtil.loadLoadingImage(activity, (ImageView)activity.findViewById(R.id.loading_gif));
        }
    }

    abstract class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

        private List<BusCurrentService> busList;
        Context context;

        public SearchResultAdapter(Context context, List<BusCurrentService> busList) {
            if (busList == null)
                busList = new ArrayList<BusCurrentService>();
            this.busList = busList;
            this.context = context;
        }

        @Override
        public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.current_booking_list_item, parent, false);

            // Return a new holder instance
            return new SearchResultAdapter.ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(final SearchResultAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

                    searchParams.setServiceRouteName(holder.tvServiceRouteName.getText().toString());
                    searchParams.setJourneyDate(JourneyDate);
                    searchParams.setPickServiceId(pickServiceID);
                    searchParams.setDepartureTime(departureTime);
                    searchParams.setClassId(ClassId);
                    searchParams.setRouteNameAppended(holder.tvVehicleNo.getText().toString());

                    searchParams.setAdultFareGetCurrentServiceDetails(busList.get(position).getAdultFare());
                    searchParams.setAdultTicketFareGetCurrentServiceDetails(busList.get(position).getAdultTicketFare());
                    searchParams.setChildFareGetCurrentServiceDetails(busList.get(position).getChildFare());
                    searchParams.setChildTicketFareGetCurrentServiceDetails(busList.get(position).getChildTicketFare());
                    searchParams.setClassIDGetCurrentServiceDetails(busList.get(position).getClassID());
                    searchParams.setConductorMobileNoGetCurrentServiceDetails(busList.get(position).getConductorMobileNo());
                    searchParams.setDepartureTimeGetCurrentServiceDetails(busList.get(position).getDepartureTime());
                    searchParams.setDestinationGetCurrentServiceDetails(busList.get(position).getDestination());
                    searchParams.setJourneyDateGetCurrentServiceDetails(busList.get(position).getJourneyDate());
                    searchParams.setJourneyHoursGetCurrentServiceDetails(busList.get(position).getJourneyDate());
                    searchParams.setLastBusStationGetCurrentServiceDetails(busList.get(position).getLastBusStation());
                    searchParams.setLastBusStationTimeGetCurrentServiceDetails(busList.get(position).getLastBusStationTime());
                    searchParams.setOriginGetCurrentServiceDetails(busList.get(position).getOrigin());
                    searchParams.setPickServiceIdGetCurrentServiceDetails(busList.get(position).getPickServiceId());
                    searchParams.setSeatavailbleGetCurrentServiceDetails(busList.get(position).getSeatavailble());
                    searchParams.setServiceClassGetCurrentServiceDetails(busList.get(position).getServiceClass());
                    searchParams.setServiceIDGetCurrentServiceDetails(busList.get(position).getServiceID());
                    searchParams.setServiceOrginTimeGetCurrentServiceDetails(busList.get(position).getServiceOrginTime());
                    searchParams.setServiceRouteIdGetCurrentServiceDetails(busList.get(position).getServiceRouteId());
                    searchParams.setServiceRouteNameGetCurrentServiceDetails(busList.get(position).getServiceRouteName());
                    searchParams.setTripCodeGetCurrentServiceDetails(busList.get(position).getTripCode());
                    searchParams.setVehicleIdGetCurrentServiceDetails(busList.get(position).getVehicleId());
                    searchParams.setVehicleNoGetCurrentServiceDetails(busList.get(position).getVehicleNo());
                    searchParams.setVehicleStatusGetCurrentServiceDetails(busList.get(position).getVehicleStatus());
                    searchParams.setKms(busList.get(position).getKms());

                    searchParams.setAdultFareServiceDetails(busList.get(position).getAdultFare());
                    searchParams.setChildFareServiceDetails(busList.get(position).getChildFare());

                    Bundle extra = new Bundle();
                    extra.putString("textlang", textlang);
                    extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
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

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CurrentBookingSearchResultActivity.this);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            LayoutInflater inflater = CurrentBookingSearchResultActivity.this.getLayoutInflater();
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
            SoapClientUtil.getRouteDetails(searchParams, pickServiceID, new SoapClientUtil.GetRouteDetailsCallback() {
                @Override
                public void onSuccess(List<BusGetRouteDetails> routeDetailsList) {

                    busRouteList = routeDetailsList;
                    RouteListAdapter routeListAdapter = new RouteListAdapter(CurrentBookingSearchResultActivity.this, R.layout.route_details_listview_item, busRouteList);
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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CurrentBookingSearchResultActivity.this);
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setLayout(800, 1400);
            LayoutInflater inflater = CurrentBookingSearchResultActivity.this.getLayoutInflater();
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

    public void fetchAvailableServices() {

        CustomisedProgressDialog.SHOW_CUST_DIA(CurrentBookingSearchResultActivity.this, "Searching Buses....");
        SoapClientUtil.getAvailableCurrentServiceDetails(searchParams, new SoapClientUtil.GetAvailableCurrentServiceDetailsCallback() {
            @Override
            public void onSuccess(List<BusCurrentService> serviceList) {

                if (serviceList.size() != 0) {
                    busServiceList = serviceList;

                    setupListView(busServiceList);
                    vHolder.loadingLayout.setVisibility(View.GONE);
                    vHolder.recyclerView.setVisibility(View.VISIBLE);
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                } else {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentBookingSearchResultActivity.this, "Sorry, no bus service is available for your selection");
                    // ActivityUtil.openNewActivity(CurrentBookingSearchResultActivity.this, CurrentBookingHomeActivity.class);
                    Intent i = new Intent(CurrentBookingSearchResultActivity.this, CurrentBookingHomeActivity.class);
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    vHolder.loadingLayout.setVisibility(View.GONE);
                    vHolder.recyclerView.setVisibility(View.VISIBLE);
                    showLoading(false);
                    i.putExtras(data);
                    startActivity(i);
                }
            }

            @Override
            public void onError(Exception e) {
                vHolder.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(CurrentBookingSearchResultActivity.this, e);
            }
        });
    }

    public void fetchRouteDetails(String pickServiceId) {

        SoapClientUtil.getRouteDetails(searchParams, pickServiceId, new SoapClientUtil.GetRouteDetailsCallback() {
            @Override
            public void onSuccess(List<BusGetRouteDetails> routeDetailsList) {

                busRouteList = routeDetailsList;
            }

            @Override
            public void onError(Exception e) {
                vHolder.loadingLayout.setVisibility(View.GONE);
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(CurrentBookingSearchResultActivity.this, e);
                //  ActivityUtil.showSnackBar(vHolder.rootView, "Server Error");
            }
        });
    }

    public void goNextpnr()
    {
        if (tracking.getPnrDetailsResponse == null) {
            CustomisedProgressDialog.SHOW_CUST_DIA(CurrentBookingSearchResultActivity.this, "Please wait loading...");
            getAllBusespnr();
            return;
        }
        else {
            hideProgressDialog();
        }
        CustomisedProgressDialog.STOP_CUST_DIA();
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putString("bookType","ON_ROUTE");
        bundle.putSerializable(AppConstants.INTENT_EXTRA.TRACK_MY_BUS, tracking);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
        ActivityUtil.openNewActivity(this, CurrentTrackBus.class, bundle);
    }

    private void getAllBusespnr() {

        TrackMyBusNetworkUtil.GetPnrDetails(tracking, new TrackMyBusNetworkUtil.GetPnrDetailsCallback() {

            public void onSuccess(GetPnrDetailsResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        //hideProgressDialog();
                        ActivityUtil.showToast(CurrentBookingSearchResultActivity.this, "No Data Available");
                    }
                    tracking.getPnrDetailsResponse = response;
                    //recycleviewProcesspnr();
                    goNextpnr();

                } else {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentBookingSearchResultActivity.this, "No Data Available");
                    //hideProgressDialog();
                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                //hideProgressDialog();
                ActivityUtil.showErrorToast(CurrentBookingSearchResultActivity.this, e);
            }
        });
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


    public void showLoading(boolean show){
        if(show){
            vHolder.loadingLayout.setVisibility(View.VISIBLE);
        }
        else{
            vHolder.loadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CurrentBookingSearchResultActivity.this, CurrentBookingHomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        intent.putExtras(data);
        startActivity(intent);
    }

}
