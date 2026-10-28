package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetFullCancellationTransactionDetailsResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetPartialCancellationTransactionDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.net.TicketCancelNetworkUtil;

public class CancellationPassengerSelectionActivity extends BaseNavigationDrawerActivity {

    BookingCancellation booking;
    PassengerListAdapter passengerListAdapter;
    ViewHolder holder;
    ProgressDialog progressDialog;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_cancellation_passenger_selection);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle extras =  getIntent().getExtras();
            booking = (BookingCancellation)extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION);
            textlang = extras.getString("textlang");
            setup();
        }
    }

    public void setup(){
        holder = new ViewHolder(this);
        holder.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
            }
        });
        passengerListAdapter = new PassengerListAdapter(this, Arrays.asList(booking.getAllPassengerDetailsByPnrNoResponse), booking.isDivyangWithHelper);
        holder.recycler_view.setAdapter(passengerListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recycler_view.getContext(),
                layoutManager.getOrientation() );
        holder.recycler_view.setLayoutManager(layoutManager);
        holder.recycler_view.addItemDecoration(dividerItemDecoration);
        passengerListAdapter.notifyDataSetChanged();

        holder.pnr2.setText(booking.getFullCancellationDetailsResponse.getPnrNumber());
        holder.from2.setText(booking.getFullCancellationDetailsResponse.getStartPlaceName());
        holder.to2.setText(booking.getFullCancellationDetailsResponse.getEndPlaceName());
        holder.journeyDate2.setText(booking.getFullCancellationDetailsResponse.getJourneyDate());
        holder.tripCode2.setText(booking.getFullCancellationDetailsResponse.getTripCode());
        holder.serviceClass2.setText(booking.getFullCancellationDetailsResponse.getClassID());

        if (booking != null && booking.isDivyangWithHelper) {
            String from = booking.getFullCancellationDetailsResponse != null ? booking.getFullCancellationDetailsResponse.getStartPlaceName() : "";
            String to = booking.getFullCancellationDetailsResponse != null ? booking.getFullCancellationDetailsResponse.getEndPlaceName() : "";
            com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Cancellation Passenger Selection. Divyang and Helper tickets are linked and must be cancelled together. Journey from " + from + " to " + to + ". Tap Confirm to review refund details.");
        }
    }

    public void goNext(){
        List<GetAllPassengerDetailsByPnrNoResponse> selectedPassengers = passengerListAdapter.getPassengerDetails();
        if(selectedPassengers.size() == 0){
            ActivityUtil.showToast(this, "No passenger selected");
            return;
        }

        booking.selectedPassengers = selectedPassengers;
        showProgressDialog();
        if (booking.isDivyangWithHelper) {
            booking.isPartialCancellation = false;
            if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this)) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Fetching refund details for Divyang and Helper tickets.");
            }
        } else {
            booking.isPartialCancellation = (booking.selectedPassengers.size() != booking.getAllPassengerDetailsByPnrNoResponse.length);
        }

        //partial cancellation
        if(booking.isPartialCancellation){
            TicketCancelNetworkUtil.GetPartialCancellationTransactionDetails(booking, new TicketCancelNetworkUtil.GetPartialCancellationTransactionDetailsCallback() {
                @Override
                public void onSuccess(GetPartialCancellationTransactionDetailsResponse response) {
                    if(response !=null){
                        booking.getPartialCancellationTransactionDetailsResponse = response;
                        Bundle bundle = new Bundle();
                        bundle.putString("textlang",textlang);
                        bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION, booking);
                        ActivityUtil.openNewActivity(CancellationPassengerSelectionActivity.this, CancellationFareDetailsActivity.class, bundle);
                    }
                    else{
                        ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, new Exception());
                    }
                    hideProgressDialog();

                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, e);
                }
            });
        }
        else{   //If Full Cancellation
            TicketCancelNetworkUtil.GetFullCancellationTransactionDetails(booking, new TicketCancelNetworkUtil.GetFullCancellationTransactionDetailsCallback() {
                @Override
                public void onSuccess(GetFullCancellationTransactionDetailsResponse response) {
                    if(response !=null){
                        booking.getFullCancellationTransactionDetailsResponse = response;

                        if (booking.isDivyangWithHelper && booking.helperFullCancellationDetailsResponse != null) {
                            TicketCancelNetworkUtil.GetFullCancellationTransactionDetails(booking.helperFullCancellationDetailsResponse, new TicketCancelNetworkUtil.GetFullCancellationTransactionDetailsCallback() {
                                @Override
                                public void onSuccess(GetFullCancellationTransactionDetailsResponse helperResponse) {
                                    hideProgressDialog();
                                    if (helperResponse != null) {
                                        booking.helperFullCancellationTransactionDetailsResponse = helperResponse;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("textlang",textlang);
                                        bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION, booking);
                                        ActivityUtil.openNewActivity(CancellationPassengerSelectionActivity.this, CancellationFareDetailsActivity.class, bundle);
                                    } else {
                                        ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, new Exception("Failed to load helper transaction details"));
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    hideProgressDialog();
                                    ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, e);
                                }
                            });
                        } else {
                            hideProgressDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString("textlang",textlang);
                            bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION, booking);
                            ActivityUtil.openNewActivity(CancellationPassengerSelectionActivity.this, CancellationFareDetailsActivity.class, bundle);
                        }
                    }
                    else{
                        hideProgressDialog();
                        ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, new Exception());
                    }
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    ActivityUtil.showErrorToast(CancellationPassengerSelectionActivity.this, e);
                }
            });
        }

    }

    public class ViewHolder{
        RecyclerView recycler_view;
        TextView pnr2, from2, to2, journeyDate2, tripCode2, serviceClass2;
        TextView pnrtxt, fromtxt, totxt, journeyDatetxt, tripCodetxt, serviceClasstxt,title;
        Button confirm_btn;
        ViewHolder(Activity activity){
            confirm_btn = (Button) activity.findViewById(R.id.confirm_btn);
            recycler_view = (RecyclerView) activity.findViewById(R.id.recycler_view);
            pnr2 = (TextView) activity.findViewById(R.id.pnr2);
            from2 = (TextView) activity.findViewById(R.id.from2);
            to2 = (TextView) activity.findViewById(R.id.to2);
            journeyDate2 = (TextView) activity.findViewById(R.id.journeyDate2);
            tripCode2 = (TextView) activity.findViewById(R.id.tripCode2);
            serviceClass2 = (TextView) activity.findViewById(R.id.serviceClass2);

            pnrtxt = (TextView) activity.findViewById(R.id.txtpnr);
            fromtxt = (TextView) activity.findViewById(R.id.txtfrom);
            totxt = (TextView) activity.findViewById(R.id.txtto);
            journeyDatetxt = (TextView) activity.findViewById(R.id.txtjourney);
            tripCodetxt = (TextView) activity.findViewById(R.id.txttripco);
            serviceClasstxt = (TextView) activity.findViewById(R.id.txtcls_serv);
            title = (TextView) activity.findViewById(R.id.canclsum_title);

            if(textlang.equalsIgnoreCase("gujarati")){
                pnrtxt.setText(R.string.cnlsum_pnr);
                fromtxt.setText(R.string.cnlsum_from);
                totxt.setText(R.string.cnlsum_to);
                journeyDatetxt.setText(R.string.cnlsum_journey);
                tripCodetxt.setText(R.string.cnlsum_tripco);
                serviceClasstxt.setText(R.string.cnlsum_cls_srv);
                title.setText(R.string.cnlsum_title);
                confirm_btn.setText(R.string.cnlsum_submit);
            }
        }
    }

    public void showProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing() ){
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }
    public void hideProgressDialog(){
        if(progressDialog == null || !progressDialog.isShowing() ){
            return;
        }
        progressDialog.dismiss();
    }

    static class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.ViewHolder>{

        Context context;
        Map<Integer, PassengerListAdapter.ViewHolder> viewMap;
        List<GetAllPassengerDetailsByPnrNoResponse> passengers;
        boolean isDivyangWithHelper;

        PassengerListAdapter(Context context, List<GetAllPassengerDetailsByPnrNoResponse> list){
            this(context, list, false);
        }

        PassengerListAdapter(Context context, List<GetAllPassengerDetailsByPnrNoResponse> list, boolean isDivyangWithHelper){
            this.context = context;
            this.passengers = list;
            this.isDivyangWithHelper = isDivyangWithHelper;
            viewMap = new LinkedHashMap<>();
        }

        @Override
        public PassengerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.list_item_ticket_cancel_passenger_select, parent, false);
            return new PassengerListAdapter.ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(PassengerListAdapter.ViewHolder holder, int position) {
            viewMap.put(position, holder);
            GetAllPassengerDetailsByPnrNoResponse passenger = passengers.get(position);
            holder.passengerName.setText(passenger.getPassengerName());
            holder.seatNo.setText("Seat No: "+passenger.getSeatNo());
            if (isDivyangWithHelper) {
                holder.isSelected.setChecked(true);
                holder.isSelected.setEnabled(false);
            }
        }

        public List<GetAllPassengerDetailsByPnrNoResponse> getPassengerDetails(){
            List<GetAllPassengerDetailsByPnrNoResponse> selectedList = new ArrayList<>();
            String[] genderrray = context.getResources().getStringArray(R.array.gender_array);
            for (Map.Entry<Integer, PassengerListAdapter.ViewHolder> entry : viewMap.entrySet())
            {
                PassengerListAdapter.ViewHolder viewHolder = entry.getValue();
                GetAllPassengerDetailsByPnrNoResponse passenger = new GetAllPassengerDetailsByPnrNoResponse();
                passenger.setPassengerName(viewHolder.passengerName.getText().toString());


                String strSeatNo = "";
                if(viewHolder.seatNo.getText() != null){
                    strSeatNo = viewHolder.seatNo.getText().toString();
                    if (strSeatNo.contains(":")) {
                        strSeatNo = strSeatNo.split(":")[1].trim();
                    }
                }

                passenger.setSeatNo(strSeatNo);
                if(isDivyangWithHelper || viewHolder.isSelected.isChecked())
                    selectedList.add(passenger);
            }
            if (isDivyangWithHelper && selectedList.isEmpty() && passengers != null) {
                selectedList.addAll(passengers);
            }
            return selectedList;
        }

        public PassengerListAdapter.ViewHolder getViewHolder(int position){
            if(viewMap.containsKey(position))
                return viewMap.get(position);
            else
                return null;
        }

        @Override
        public int getItemCount() {
            return passengers.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView passengerName;
            TextView seatNo;
            CheckBox isSelected;
            public ViewHolder(View itemView) {
                super(itemView);
                passengerName = (TextView)itemView.findViewById(R.id.passenger_name);
                seatNo = (TextView)itemView.findViewById(R.id.seatNo);
                isSelected = (CheckBox)itemView.findViewById(R.id.isSelected);
            }
        }
    }
}
