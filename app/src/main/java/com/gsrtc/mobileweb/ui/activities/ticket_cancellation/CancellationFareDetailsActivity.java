package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.ConfirmFullCancellationResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.ConfirmPartialCancellationResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.net.TicketCancelNetworkUtil;

//import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;

public class CancellationFareDetailsActivity extends BaseNavigationDrawerActivity {

    BookingCancellation booking;
    ActivityViewHolder holder;

    RemainingPassengerAdapter remainingPassengerAdapter;
    ProgressDialog progressDialog;
    private String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_cancellation_fare_details);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle extras =  getIntent().getExtras();
            textlang = extras.getString("textlang");
            booking = (BookingCancellation)extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION);
            setup();
        }
    }


    public void setup(){
        holder = new ActivityViewHolder(this);
        //added by abhi tarun on 26-02-2026
        if (booking != null && booking.cancellationSearchParams != null) {
            holder.email.setText(booking.cancellationSearchParams.email);
            holder.phone.setText(booking.cancellationSearchParams.mobile);
        }
        if(booking.isPartialCancellation){
            holder.fullCancellation.container.setVisibility(View.GONE);
            populatePartialCancellationViews();
        }
        else{
            holder.partialCancellation.container.setVisibility(View.GONE);
            populateFullCancellationViews();
        }

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
            }
        });
    }

    public void populateFullCancellationViews(){
        holder.fullCancellation.pnr2.setText(booking.getFullCancellationDetailsResponse.getPnrNumber());
        holder.fullCancellation.from2.setText(booking.getFullCancellationDetailsResponse.getStartPlaceName());
        holder.fullCancellation.to2.setText(booking.getFullCancellationDetailsResponse.getEndPlaceName());
        holder.fullCancellation.journeyDate2.setText(booking.getFullCancellationDetailsResponse.getJourneyDate());
        holder.fullCancellation.tripCode2.setText(booking.getFullCancellationDetailsResponse.getTripCode());
        holder.fullCancellation.serviceClass2.setText(booking.getFullCancellationDetailsResponse.getClassID());//TODO doubt
        holder.fullCancellation.cancellation_percentage2R.setText(booking.getFullCancellationTransactionDetailsResponse.getRefundPrcntOrLumpsumValue());

        if (booking.isDivyangWithHelper && booking.helperFullCancellationTransactionDetailsResponse != null) {
            try {
                double mainRefund = Double.parseDouble(booking.getFullCancellationTransactionDetailsResponse.getNetRefundAmount());
                double helperRefund = Double.parseDouble(booking.helperFullCancellationTransactionDetailsResponse.getNetRefundAmount());
                double totalRefund = mainRefund + helperRefund;
                holder.fullCancellation.refund_amount2R.setText(String.format(java.util.Locale.US, "%.2f", totalRefund));
            } catch (Exception e) {
                holder.fullCancellation.refund_amount2R.setText(booking.getFullCancellationTransactionDetailsResponse.getNetRefundAmount());
            }
        } else {
            holder.fullCancellation.refund_amount2R.setText(booking.getFullCancellationTransactionDetailsResponse.getNetRefundAmount());
        }

        if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this)) {
            String from = booking.getFullCancellationDetailsResponse != null ? booking.getFullCancellationDetailsResponse.getStartPlaceName() : "";
            String to = booking.getFullCancellationDetailsResponse != null ? booking.getFullCancellationDetailsResponse.getEndPlaceName() : "";
            String refund = holder.fullCancellation.refund_amount2R.getText().toString();
            String pnr = booking.getFullCancellationDetailsResponse != null ? booking.getFullCancellationDetailsResponse.getPnrNumber() : "";
            if (booking.isDivyangWithHelper) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak(
                        "Cancellation refund details loaded for Divyang with Helper. PNR " + pnr + ", journey from " + from + " to " + to + ". Total refund amount for Divyang and Helper is " + refund + " Rupees. Please enter email and phone number and tap Cancel Ticket to proceed."
                );
            } else {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak(
                        "Cancellation refund details loaded. PNR " + pnr + ", journey from " + from + " to " + to + ". Refund amount is " + refund + " Rupees. Please enter email and phone number and tap Cancel Ticket to proceed."
                );
            }
        }
    }

    public void populatePartialCancellationViews(){
        holder.partialCancellation.pnr1.setText(booking.getPartialCancellationDetailsResponse.getPnrNumber());
        holder.partialCancellation.from1.setText(booking.getPartialCancellationDetailsResponse.getStartPlaceName());
        holder.partialCancellation.to1.setText(booking.getPartialCancellationDetailsResponse.getEndPlaceName());
        holder.partialCancellation.journeyDate1.setText(booking.getPartialCancellationDetailsResponse.getJourneyDate());
        holder.partialCancellation.tripCode1.setText(booking.getPartialCancellationDetailsResponse.getTripCode());
        holder.partialCancellation.serviceClass1.setText(booking.getPartialCancellationDetailsResponse.getClassID());//TODO doubt

        holder.partialCancellation.basic_fare1.setText(booking.getPartialCancellationTransactionDetailsResponse.getBasicFare());
        holder.partialCancellation.res_fee1.setText(booking.getPartialCancellationTransactionDetailsResponse.getReservationFee());
        holder.partialCancellation.entryfee1.setText(booking.getPartialCancellationTransactionDetailsResponse.getEntryFee());
        holder.partialCancellation.acc_ins_charge1.setText("0");
        //holder.partialCancellation.toll_fee1.setText(booking.getPartialCancellationTransactionDetailsResponse.getTollFee());
        holder.partialCancellation.toll_fee1.setText(booking.getPartialCancellationTransactionDetailsResponse.getBridgeFee());
        holder.partialCancellation.toll_fee_rajasthan1.setText("0");
        holder.partialCancellation.other_levies1.setText(booking.getPartialCancellationTransactionDetailsResponse.getOtherLevies());
        holder.partialCancellation.less_concession1.setText(booking.getPartialCancellationTransactionDetailsResponse.getResvOtherConcession());
        holder.partialCancellation.less_discount1.setText(booking.getPartialCancellationTransactionDetailsResponse.getResvOtherDiscount());
        holder.partialCancellation.less_cancellation_fee1.setText(booking.getPartialCancellationTransactionDetailsResponse.getResvCancellationFee());
        //holder.partialCancellation.transaction_amount1.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclRefundAmount());
        holder.partialCancellation.transaction_amount1.setText(booking.getPartialCancellationTransactionDetailsResponse.getTotalFare());

        holder.partialCancellation.basic_fare1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclBasicFare());
        holder.partialCancellation.res_fee1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclReservationFee());
        holder.partialCancellation.entryfee1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclEntryFee());
        holder.partialCancellation.acc_ins_charge1R.setText("0");
        //holder.partialCancellation.toll_fee1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclTollFee());
        holder.partialCancellation.toll_fee1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclBridgeFee());
        holder.partialCancellation.toll_fee_rajasthan1R.setText("0");
        holder.partialCancellation.other_levies1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclOtherLevies());
        holder.partialCancellation.serv_tax1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclServiceFee());
        holder.partialCancellation.less_concession1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCanclOtherConcessions());
        holder.partialCancellation.less_discount1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCancelOtherDiscount());
        holder.partialCancellation.less_cancellation_fee1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getCancellationFee());

        holder.partialCancellation.cancellation_percentage1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getRefundPrcntOrLumpsumValue());
        holder.partialCancellation.refund_amount1R.setText(booking.getPartialCancellationTransactionDetailsResponse.getNetRefundAmount());

        //HashMap<String,String> passengers=new HashMap<String, String>();

        ArrayList<GetAllPassengerDetailsByPnrNoResponse> passengers = new ArrayList<GetAllPassengerDetailsByPnrNoResponse>(Arrays.asList(booking.getAllPassengerDetailsByPnrNoResponse));

        for(GetAllPassengerDetailsByPnrNoResponse selectedPassenger:booking.selectedPassengers){
            for(int i=0; i<passengers.size(); i++){
                Log.d("seat number", "\"" + passengers.get(i).getSeatNo() + "\":\"" + selectedPassenger.getSeatNo() + "\"");
                if(passengers.get(i).getSeatNo().equals(selectedPassenger.getSeatNo())){
                    Log.d("in loop seat number", "\"" + passengers.get(i).getSeatNo() + "\":\"" + selectedPassenger.getSeatNo() + "\"");
                    passengers.remove(i);
                }
            }
        }

        //GetAllPassengerDetailsByPnrNoResponse [] otherPassengers = passengers.toArray(new GetAllPassengerDetailsByPnrNoResponse[passengers.size()]);

        Log.d("passengers Count", passengers.size() + "");


        remainingPassengerAdapter = new RemainingPassengerAdapter(this, passengers);

        Log.d("remainingPassengerCnt",remainingPassengerAdapter.getItemCount() + "");

        holder.partialCancellation.remaining_passengers_list.setAdapter(remainingPassengerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.partialCancellation.remaining_passengers_list.getContext(),
                layoutManager.getOrientation() );
        holder.partialCancellation.remaining_passengers_list.setLayoutManager(layoutManager);
        holder.partialCancellation.remaining_passengers_list.addItemDecoration(dividerItemDecoration);

        remainingPassengerAdapter.notifyDataSetChanged();

        Log.d("List Count", holder.partialCancellation.remaining_passengers_list.getChildCount() + "");

    }

    public void confirmCancellation(){
        showProgressDialog();
        if (com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this)) {
            if (booking.isDivyangWithHelper) {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Processing cancellation for Divyang and Helper tickets.");
            } else {
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak("Processing ticket cancellation.");
            }
        }
        if(booking.isPartialCancellation){
            TicketCancelNetworkUtil.ConfirmPartialCancellation(booking, new TicketCancelNetworkUtil.ConfirmPartialCancellationCallback() {
                @Override
                public void onSuccess(ConfirmPartialCancellationResponse response) {
                    if(response!=null){
                        booking.confirmPartialCancellationResponse = response;
                        goNext();
                    }
                    else{
                        ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, new Exception());
                    }
                    hideProgressDialog();
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, e);
                }
            });
        }
        else{
            TicketCancelNetworkUtil.ConfirmFullCancellation(booking, new TicketCancelNetworkUtil.ConfirmFullCancellationCallback() {
                @Override
                public void onSuccess(ConfirmFullCancellationResponse response) {
                    if(response!=null){
                        booking.confirmFullCancellationResponse = response;
                        if (booking.isDivyangWithHelper && booking.helperFullCancellationDetailsResponse != null && booking.helperFullCancellationTransactionDetailsResponse != null) {
                            // Cancel helper ticket
                            TicketCancelNetworkUtil.ConfirmFullCancellation(booking.helperFullCancellationDetailsResponse, booking.helperFullCancellationTransactionDetailsResponse, new TicketCancelNetworkUtil.ConfirmFullCancellationCallback() {
                                @Override
                                public void onSuccess(ConfirmFullCancellationResponse helperResponse) {
                                    hideProgressDialog();
                                    if (helperResponse != null) {
                                        booking.helperConfirmFullCancellationResponse = helperResponse;
                                        goNext();
                                    } else {
                                        ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, new Exception("Failed to confirm helper cancellation"));
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    hideProgressDialog();
                                    ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, e);
                                }
                            });
                        } else {
                            hideProgressDialog();
                            goNext();
                        }
                    }
                    else{
                        hideProgressDialog();
                        ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, new Exception());
                    }
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    ActivityUtil.showErrorToast(CancellationFareDetailsActivity.this, e);
                }
            });
        }

    }

    public void goNext(){
        if(!ValidationUtil.isValidEmail(holder.email.getText().toString())){
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }
        else if(!ValidationUtil.isValidPhone(holder.phone.getText().toString())){
            ActivityUtil.showToast(this, "Please enter valid phone");
            return;
        }


        if(booking.isPartialCancellation){
            if(booking.confirmPartialCancellationResponse == null){
                confirmCancellation();
                return;
            }
        }
        else{
            if(booking.confirmFullCancellationResponse == null || (booking.isDivyangWithHelper && booking.helperConfirmFullCancellationResponse == null)){
                confirmCancellation();
                return;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_CANCELLATION, booking);
        ActivityUtil.openNewActivity(this, CancellationSuccessActivity.class, bundle);
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

    public class ActivityViewHolder{
        PartialCancellationViewHolder partialCancellation;
        FullCancellationViewHolder fullCancellation;

        EditText email, phone;
        TextView emailtxt,mobiletxt;

        Button submitBtn;
        ActivityViewHolder(Activity activity){
            partialCancellation = new PartialCancellationViewHolder(activity);
            fullCancellation = new FullCancellationViewHolder(activity);
            email = (EditText) activity.findViewById(R.id.email);
            phone = (EditText) activity.findViewById(R.id.phone);

            emailtxt = (TextView) activity.findViewById(R.id.cantxt_email);
            mobiletxt= (TextView) activity.findViewById(R.id.cantxt_mobile);
            submitBtn = (Button) activity.findViewById(R.id.submitBtn);
            if(textlang.equalsIgnoreCase("gujarati")){
                emailtxt.setText(R.string.vwsms_email);
                mobiletxt.setText(R.string.vwsms_mobile);
                submitBtn.setText(R.string.prtlcnlsum_cnrm_btn);
            }
        }

        public class PartialCancellationViewHolder{
            View container;
            TextView pnr1, from1, to1,journeyDate1,tripCode1,serviceClass1;
            TextView basic_fare1, res_fee1,entryfee1,acc_ins_charge1,toll_fee1,
                    toll_fee_rajasthan1,other_levies1, less_concession1, less_discount1
                    , less_cancellation_fee1, transaction_amount1;

            //Refund
            TextView basic_fare1R,res_fee1R, entryfee1R, acc_ins_charge1R, toll_fee1R,
                    toll_fee_rajasthan1R, other_levies1R,serv_tax1R, less_concession1R, less_discount1R,
                    less_cancellation_fee1R, cancellation_percentage1R, refund_amount1R;

            /**gujarati language**/
            TextView txtpnr1, txtfrom1, txtto1,txtjourneyDate1,txttripCode1,txtserviceClass1;
            TextView txtbasic_fare1, txtres_fee1,txtentryfee1,txtacc_ins_charge1,txttoll_fee1,
                    txttoll_fee_rajasthan1,txtother_levies1, txtless_concession1, txtless_discount1
                    , txtless_cancellation_fee1, txttransaction_amount1,title,titlesum,titleR;

            //Refund
            TextView txtbasic_fare1R,txtres_fee1R, txtentryfee1R, txtacc_ins_charge1R, txttoll_fee1R,
                    txttoll_fee_rajasthan1R, txtother_levies1R,txtserv_tax1R, txtless_concession1R, txtless_discount1R,
                    txtless_cancellation_fee1R, txtcancellation_percentage1R, txtrefund_amount1R,titlepass,passname,seatnum;

            RecyclerView remaining_passengers_list;
            PartialCancellationViewHolder(Activity activity){
                container = activity.findViewById(R.id.partialCancellationLayout);

                remaining_passengers_list = (RecyclerView) activity.findViewById(R.id.remaining_passengers_list);

                pnr1 = (TextView) activity.findViewById(R.id.pnr1);
                from1 = (TextView) activity.findViewById(R.id.from1);
                to1 = (TextView) activity.findViewById(R.id.to1);
                journeyDate1 = (TextView) activity.findViewById(R.id.journeyDate1);
                tripCode1 = (TextView) activity.findViewById(R.id.tripCode1);
                serviceClass1 = (TextView) activity.findViewById(R.id.serviceClass1);

                basic_fare1 = (TextView) activity.findViewById(R.id.basic_fare1);
                res_fee1 = (TextView) activity.findViewById(R.id.res_fee1);
                entryfee1 = (TextView) activity.findViewById(R.id.entryfee1);
                acc_ins_charge1 = (TextView) activity.findViewById(R.id.acc_ins_charge1);
                toll_fee1 = (TextView) activity.findViewById(R.id.toll_fee1);
                toll_fee_rajasthan1 = (TextView) activity.findViewById(R.id.toll_fee_rajasthan1);
                other_levies1 = (TextView) activity.findViewById(R.id.other_levies1);
                less_concession1 = (TextView) activity.findViewById(R.id.less_concession1);
                less_discount1 = (TextView) activity.findViewById(R.id.less_discount1);
                less_cancellation_fee1 = (TextView) activity.findViewById(R.id.less_cancellation_fee1);
                transaction_amount1 = (TextView) activity.findViewById(R.id.transaction_amount1);


                basic_fare1R = (TextView) activity.findViewById(R.id.basic_fare1R);
                res_fee1R = (TextView) activity.findViewById(R.id.res_fee1R);
                entryfee1R = (TextView) activity.findViewById(R.id.entryfee1R);
                acc_ins_charge1R = (TextView) activity.findViewById(R.id.acc_ins_charge1R);
                toll_fee1R = (TextView) activity.findViewById(R.id.toll_fee1R);
                toll_fee_rajasthan1R = (TextView) activity.findViewById(R.id.toll_fee_rajasthan1R);
                other_levies1R = (TextView) activity.findViewById(R.id.other_levies1R);
                serv_tax1R = (TextView) activity.findViewById(R.id.serv_tax1R);
                less_concession1R = (TextView) activity.findViewById(R.id.less_concession1R);
                less_discount1R = (TextView) activity.findViewById(R.id.less_discount1R);
                less_cancellation_fee1R = (TextView) activity.findViewById(R.id.less_cancellation_fee1R);
                cancellation_percentage1R = (TextView) activity.findViewById(R.id.cancellation_percentage1R);
                refund_amount1R = (TextView) activity.findViewById(R.id.refund_amount1R);

                /**gujarati language**/
                txtpnr1 = (TextView) activity.findViewById(R.id.cantxtpnr2);
                txtfrom1 = (TextView) activity.findViewById(R.id.cantxtfrom2);
                txtto1 = (TextView) activity.findViewById(R.id.cantxtto2);
                txtjourneyDate1 = (TextView) activity.findViewById(R.id.cantxtjourney2);
                txttripCode1 = (TextView) activity.findViewById(R.id.cantxttripco2);
                txtserviceClass1 = (TextView) activity.findViewById(R.id.cantxtclsserv2);
                title = (TextView) activity.findViewById(R.id.cansummry2);

                txtbasic_fare1 = (TextView) activity.findViewById(R.id.cantxtbasicfare);
                txtres_fee1 = (TextView) activity.findViewById(R.id.cantxtresvfee);
                txtentryfee1 = (TextView) activity.findViewById(R.id.entryfee1);
                txtacc_ins_charge1 = (TextView) activity.findViewById(R.id.cantxtaccins);
                txttoll_fee1 = (TextView) activity.findViewById(R.id.cantxttollfee);
                txttoll_fee_rajasthan1 = (TextView) activity.findViewById(R.id.cantxttollfeerjs);
                txtother_levies1 = (TextView) activity.findViewById(R.id.cantxtothrlevi);
                txtless_concession1 = (TextView) activity.findViewById(R.id.cantxtlescons);
                txtless_discount1 = (TextView) activity.findViewById(R.id.cantxtlesdiscnt);
                txtless_cancellation_fee1 = (TextView) activity.findViewById(R.id.cantxtlescanfee);
                txttransaction_amount1 = (TextView) activity.findViewById(R.id.cantxttotamt);
                titlesum = (TextView) activity.findViewById(R.id.cantxtorginaltrans);
                // titlesum = (TextView) activity.findViewById(R.id.cancelsum_title);


                txtbasic_fare1R = (TextView) activity.findViewById(R.id.cantxt_refbasicfare);
                txtres_fee1R = (TextView) activity.findViewById(R.id.cantxt_ref_revfee);
                txtentryfee1R = (TextView) activity.findViewById(R.id.entryfee1R);
                txtacc_ins_charge1R = (TextView) activity.findViewById(R.id.cantxtref_acc_ins);
                txttoll_fee1R = (TextView) activity.findViewById(R.id.cantxtreftollfee);
                txttoll_fee_rajasthan1R = (TextView) activity.findViewById(R.id.cantxtref_tolfeerjs);
                txtother_levies1R = (TextView) activity.findViewById(R.id.cantxt_ref_otherlevi);
                txtserv_tax1R = (TextView) activity.findViewById(R.id.cantxt_ref_servtax);
                txtless_concession1R = (TextView) activity.findViewById(R.id.cantxt_ref_les_cons);
                txtless_discount1R = (TextView) activity.findViewById(R.id.cantxt_ref_les_dicnt);
                txtless_cancellation_fee1R = (TextView) activity.findViewById(R.id.cantxt_ref_lescanfee);
                txtcancellation_percentage1R = (TextView) activity.findViewById(R.id.cantxt_ref_canpercent_lump);
                txtrefund_amount1R = (TextView) activity.findViewById(R.id.cantxt_refamnt);
                titleR = (TextView) activity.findViewById(R.id.cantxtrefunded);
                passname = (TextView)activity.findViewById(R.id.cantxt_passname);
                seatnum = (TextView)activity.findViewById(R.id.cantxt_seatno);
                titlepass = (TextView)activity.findViewById(R.id.cantxt_remain_pass);

                if(textlang.equalsIgnoreCase("gujarati")){
                    txtpnr1.setText(R.string.cnlsum_pnr);
                    txtfrom1.setText(R.string.cnlsum_from);
                    txtto1.setText(R.string.cnlsum_to);
                    txtjourneyDate1.setText(R.string.cnlsum_journey);
                    txttripCode1.setText(R.string.cnlsum_tripco);
                    txtserviceClass1.setText(R.string.cnlsum_cls_srv);
                    title.setText(R.string.prtlcnlsum_title);

                    txtbasic_fare1.setText(R.string.prtlcnlsum_basic_fare);
                    txtres_fee1.setText(R.string.prtlcnlsum_resv_fee);
                    //txtentryfee1.setText(R.string.cnlsum_pnr);
                    txtacc_ins_charge1.setText(R.string.prtlcnlsum_acc_ins);
                    txttoll_fee1.setText(R.string.prtlcnlsum_toolfee);
                    txttoll_fee_rajasthan1.setText(R.string.prtlcnlsum_toolfeerjs);
                    txtother_levies1.setText(R.string.prtlcnlsum_other_levi);
                    txtless_concession1.setText(R.string.prtlcnlsum_lescns);
                    txtless_discount1.setText(R.string.prtlcnlsum_lesdiscnt);
                    txtless_cancellation_fee1.setText(R.string.prtlcnlsum_les_cnclfee);
                    txttransaction_amount1.setText(R.string.prtlcnlsum_totalamt);
                    titlesum.setText(R.string.prtlcnlsum_orgnl_trans);

                    txtbasic_fare1R.setText(R.string.prtlcnlsum_basic_farep);
                    txtres_fee1R.setText(R.string.prtlcnlsum_resv_feep);
                    txtentryfee1R.setText(R.string.cnlsum_pnr);
                    txtacc_ins_charge1R.setText(R.string.prtlcnlsum_acc_insp);
                    txttoll_fee1R.setText(R.string.prtlcnlsum_toolfeep);
                    txttoll_fee_rajasthan1R.setText(R.string.prtlcnlsum_toolfeerjsp);
                    txtother_levies1R.setText(R.string.prtlcnlsum_other_levip);
                    txtserv_tax1R.setText(R.string.prtlcnlsum_gstp);
                    txtless_concession1R.setText(R.string.prtlcnlsum_lescnsp);
                    txtless_discount1R.setText(R.string.prtlcnlsum_lesdiscntp);
                    txtless_cancellation_fee1R.setText(R.string.prtlcnlsum_les_cnclfeep);
                    txtcancellation_percentage1R.setText(R.string.prtlcnlsum_les_cnclpercntp);
                    txtrefund_amount1R.setText(R.string.prtlcnlsum_les_rfndamntp);
                    titleR.setText(R.string.prtlcnlsum_tobe_refund);
                    titlepass.setText(R.string.prtlcnlsum_remaingpassp);
                    passname.setText(R.string.prtlcnlsum_passnamep);
                    seatnum.setText(R.string.prtlcnlsum_seatno);

                }
            }
        }

        public class FullCancellationViewHolder{
            TextView pnr2, from2, to2,journeyDate2,tripCode2,serviceClass2, cancellation_percentage2R, refund_amount2R;
            TextView txtpnr2, txtfrom2, txtto2,txtjourneyDate2,txttripCode2,txtserviceClass2, txtcancellation_percentage2R, txtrefund_amount2R,titleful;
            View container;
            FullCancellationViewHolder(Activity activity) {
                container = activity.findViewById(R.id.fullCancellationLayout);
                pnr2 = (TextView) activity.findViewById(R.id.pnr2);
                from2 = (TextView) activity.findViewById(R.id.from2);
                to2 = (TextView) activity.findViewById(R.id.to2);
                journeyDate2 = (TextView) activity.findViewById(R.id.journeyDate2);
                tripCode2 = (TextView) activity.findViewById(R.id.tripCode2);
                serviceClass2 = (TextView) activity.findViewById(R.id.serviceClass2);
                cancellation_percentage2R = (TextView) activity.findViewById(R.id.cancellation_percentage2R);
                refund_amount2R = (TextView) activity.findViewById(R.id.refund_amount2R);

                txtpnr2 = (TextView) activity.findViewById(R.id.cantxtpnr);
                txtfrom2 = (TextView) activity.findViewById(R.id.cantxtfrom);
                txtto2 = (TextView) activity.findViewById(R.id.cantxtto);
                txtjourneyDate2 = (TextView) activity.findViewById(R.id.cantxtjourney);
                txttripCode2 = (TextView) activity.findViewById(R.id.cantxttripco);
                txtserviceClass2 = (TextView) activity.findViewById(R.id.cantxtclsserv);
                txtcancellation_percentage2R = (TextView) activity.findViewById(R.id.canpercent_lump);
                txtrefund_amount2R = (TextView) activity.findViewById(R.id.cantxtrefund);
                titleful = (TextView) activity.findViewById(R.id.cancelsum_title);

                if(textlang.equalsIgnoreCase("gujarati")){
                    txtpnr2.setText(R.string.cnlsum_pnr);
                    txtfrom2.setText(R.string.cnlsum_from);
                    txtto2.setText(R.string.cnlsum_to);
                    txtjourneyDate2.setText(R.string.cnlsum_journey);
                    txttripCode2.setText(R.string.cnlsum_tripco);
                    txtserviceClass2.setText(R.string.cnlsum_cls_srv);
                    txtcancellation_percentage2R.setText(R.string.cnlsum_cncl_percent);
                    txtrefund_amount2R.setText(R.string.cnlsum_refund_amt);
                    titleful.setText(R.string.cnlsum_title);
                }
            }
        }
    }

    public class RemainingPassenger {

        private String seatNumber;
        private String PassangerName;

        public String getSeatNumber() {
            return seatNumber;
        }

        public String getPassangerName() {
            return PassangerName;
        }

        public RemainingPassenger(String seatNumber, String passangerName) {
            this.seatNumber = seatNumber;
            PassangerName = passangerName;
        }

        @Override
        public String toString() {
            return "RemainingPassenger{" +
                    "seatNumber='" + seatNumber + '\'' +
                    ", PassangerName='" + PassangerName + '\'' +
                    '}';
        }
    }

    public class RemainingPassengerAdapter extends RecyclerView.Adapter<RemainingPassengerAdapter.ViewHolder>{

        Context context;
        Map<Integer, RemainingPassengerAdapter.ViewHolder> viewMap;
        List<GetAllPassengerDetailsByPnrNoResponse> passengers;

        RemainingPassengerAdapter(Context context, List<GetAllPassengerDetailsByPnrNoResponse> list){
            this.context = context;
            this.passengers = list;
            viewMap = new LinkedHashMap<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.list_item_remaining_passengers, parent, false);
            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            viewMap.put(position, holder);
            GetAllPassengerDetailsByPnrNoResponse passenger = passengers.get(position);
            holder.name.setText(passenger.getPassengerName());
            holder.seatNo.setText(passenger.getSeatNo());
        }

        public ViewHolder getViewHolder(int position){
            if(viewMap.containsKey(position))
                return viewMap.get(position);
            else
                return null;
        }

        @Override
        public int getItemCount() {
            return passengers.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView seatNo;

            public ViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.passenger_name);
                seatNo = (TextView)view.findViewById(R.id.seatNo);

                if(textlang.equalsIgnoreCase("gujarati")){
                    name.setText(R.string.prtlcnlsum_passnamep);
                    seatNo.setText(R.string.prtlcnlsum_seatno);
                }
            }
        }
    }
}
