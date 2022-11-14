package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.SeatRow;
import com.gsrtc.mobileweb.models.ServiceSeatDetails;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.adapters.SeatSelectionAdapter;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

public class SeatSelectionActivity extends BaseNavigationDrawerActivity {

    BusService busService;
    SearchParams searchParams;
    SeatSelectionAdapter seatSelectionAdapter;
    ViewHolder holder;
    List<SeatRow> seatRows;

    TextView tripco, classservice;
    TextView seatcls, tripcode;

    ServiceSeatDetails serviceSeatDetails;
    Set<Integer> availseat;
    Set<Integer> condseat;
    String textlang, class_layout_id, class_id;


    ProgressDialog seatBlockingProgressDialog;

    boolean layoutFetched = false, serviceSeatDetailsFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_seat_selection);

        seatcls = (TextView) findViewById(R.id.classservice);
        tripcode = (TextView) findViewById(R.id.tripco);
        tripco = (TextView) findViewById(R.id.trip);
        classservice = (TextView) findViewById(R.id.classserv);

        setupViews();

        if (getIntent() != null && getIntent().getExtras() != null) {
            busService = (BusService) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE);
            searchParams = (SearchParams) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            showLoading(true);
            CustomisedProgressDialog.SHOW_CUST_DIA(SeatSelectionActivity.this, "Please wait loading....");

            tripco.setText(busService.getTripCode());
            classservice.setText(busService.getServiceClass());

            class_layout_id = busService.getServiceID().split(",")[3].trim();
            class_id = busService.getClassID();

            fetchLayoutObject(busService);
            fetchServiceSeatDetails(busService, searchParams);

            textlang = searchParams.getTextlanguage().trim();
            if (textlang.equalsIgnoreCase("gujarati")) {
                seatcls.setText(R.string.seat_class);
                tripcode.setText(R.string.seat_tripco);

            }
        }
    }

    public void setupViews() {
        holder = new ViewHolder(this);
        holder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoading(true);
                CustomisedProgressDialog.SHOW_CUST_DIA(SeatSelectionActivity.this, "Please wait loading....");
                holder.swipeContainer.setRefreshing(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                fetchServiceSeatDetails(busService, searchParams);
                fetchLayoutObject(busService);
                layoutFetched = false;
                serviceSeatDetailsFetched = false;
            }
        });
    }

    public void setupSeatView(List<SeatRow> seatRows, ServiceSeatDetails serviceSeatDetails, String layout_id, String srv_class_id) {
        seatSelectionAdapter = new SeatSelectionAdapter(this, seatRows, serviceSeatDetails, searchParams, layout_id, srv_class_id) {
            @Override
            public void allSeatsSelected(ArrayList<Seat> selectedSeats) {
                getReferenceNumber(busService, selectedSeats, searchParams);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(seatSelectionAdapter);
        seatSelectionAdapter.notifyDataSetChanged();
    }

    public void fetchLayoutObject(BusService busService) {
        String layoutID = busService.getServiceID().split(",")[3].trim();
        class_layout_id = busService.getServiceID().split(",")[3].trim();
        class_id = busService.getClassID();
        SoapClientUtil.getLayoutDetailByLayoutID(layoutID, new SoapClientUtil.GetLayoutDetailByLayoutIDCallback() {
            @Override
            public void onSuccess(List<SeatRow> seatRowList) {
                if (seatRowList != null) {
                    seatRows = seatRowList;
                    layoutFetched = true;
                }
                if (serviceSeatDetailsFetched && layoutFetched) {
                    setupSeatView(seatRows, serviceSeatDetails, class_layout_id, class_id);
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
                layoutFetched = true;
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
            }
        });
    }

    public void fetchServiceSeatDetails(BusService busService, SearchParams searchParams) {
        SoapClientUtil.getServiceSeatDetails(busService, searchParams, new SoapClientUtil.GetServiceSeatDetailsCallback() {
            @Override
            public void onSuccess(ServiceSeatDetails serviceSeatDetailsRemote) {

                serviceSeatDetails = serviceSeatDetailsRemote;
                serviceSeatDetailsFetched = true;
                availseat = serviceSeatDetails.getAvailableSeats();
                condseat = serviceSeatDetails.getConductorSeats();

                if (availseat.isEmpty()) {
                    showLoading(true);
                    layoutFetched = false;
                    serviceSeatDetailsFetched = false;
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    //Available seats are less than requested seats. Please select other service.
                    ActivityUtil.showToast(SeatSelectionActivity.this, "Available seats are less than requested seats / Booking time over.Please select some other services.");
                    finish();
                } else if (serviceSeatDetailsFetched && layoutFetched) {
                    setupSeatView(seatRows, serviceSeatDetails, class_layout_id, class_id);
                    showLoading(false);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();

            }
        });
    }

    public void getReferenceNumber(final BusService busService, final ArrayList<Seat> seatList, final SearchParams searchParams) {

        blockSeats(busService, seatList, searchParams);

    }


    public void blockSeats(final BusService busService, final ArrayList<Seat> seatList, final SearchParams searchParams) {
        CustomisedProgressDialog.SHOW_CUST_DIA(SeatSelectionActivity.this, "Please wait loading....");

        AdvanceBookingNetworkUtil.AddBlockSeats(busService, seatList, searchParams, new AdvanceBookingNetworkUtil.AddBlockSeatsCallback() {
            @Override
            public void onSuccess(AddBlockSeatsResponse addBlockSeatsResponse) {
                if (serviceSeatDetails != null) {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.INTENT_EXTRA.BUS_SERVICE, busService);
                    bundle.putSerializable(AppConstants.INTENT_EXTRA.SELECTED_SEATS, seatList);
                    bundle.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    bundle.putSerializable(AppConstants.INTENT_EXTRA.ADD_BLOCK_SEATS_RESPONSE, addBlockSeatsResponse);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    showLoading(true);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    fetchServiceSeatDetails(busService, searchParams);
                    layoutFetched = false;
                    serviceSeatDetailsFetched = false;
                    //Available seats are less than requested seats. Please select other service.
                    ActivityUtil.showToast(SeatSelectionActivity.this, "Available seats are less than requested seats / Booking time over.Please select some other services.");
                }
            }

            @Override
            public void onError(Exception e) {
                showLoading(true);
                CustomisedProgressDialog.STOP_CUST_DIA();
                fetchServiceSeatDetails(busService, searchParams);
                layoutFetched = true;
                serviceSeatDetailsFetched = false;
                ActivityUtil.showErrorToast(SeatSelectionActivity.this, e);
            }
        });
    }


    public void showLoading(boolean show) {
        if (show) {
            holder.loadingLayout.setVisibility(View.VISIBLE);
        } else {
            holder.loadingLayout.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder {
        RecyclerView recyclerView;
        SwipeRefreshLayout swipeContainer;
        View loadingLayout;

        public ViewHolder(Activity activity) {
            recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
            loadingLayout = activity.findViewById(R.id.loading_layout);
        }
    }
}
