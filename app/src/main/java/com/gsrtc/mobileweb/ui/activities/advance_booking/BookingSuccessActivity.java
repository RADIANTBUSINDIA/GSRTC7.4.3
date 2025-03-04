package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.AppConstants;

public class BookingSuccessActivity extends BaseNavigationDrawerActivity {

    ViewHolder holder;
    Booking onwardBooking;
    Booking returnBooking;
    Booking divyangBooking;
    String textlang;

    TextView header,total_fare,returnjrny;
    ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_booking_success);
        holder = new ViewHolder(this);
        reviewManager = ReviewManagerFactory.create(this);
        // Referencing the button
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
                returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
            }
            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG)) {
                divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
            }

            header = (TextView) findViewById(R.id.header);
            total_fare = (TextView) findViewById(R.id.tot_faretxt);
            returnjrny = (TextView) findViewById(R.id.return_jrny);

            textlang = onwardBooking.getSearchParams().getTextlanguage().trim();
        }
        if (textlang.equalsIgnoreCase("gujarati")) {
            header.setText(R.string.adcnfm_header);
            total_fare.setText(R.string.adcnfm_total_fare);
            returnjrny.setText(R.string.adrtn_jrny);
        }
        if (textlang.equalsIgnoreCase("gujarati")) {

            holder.onwardRoute.setText(onwardBooking.getStartPlace().getPlaceName() + " - " + onwardBooking.getEndPlace().getPlaceName());
            holder.onwardJourneyDate.setText("પ્રવાસ તારીખ : " + onwardBooking.getJourneyDate());
            if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")){
                holder.onwardSeats.setText("બેઠકો : " + listToStringDivyang(onwardBooking.getSelectedSeats()));
            }else{
                holder.onwardSeats.setText("બેઠકો : " + listToString(onwardBooking.getSelectedSeats()));
            }

            Double onwardPrice = Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            holder.price1.setText("રૂપિયા " + onwardPrice);
            holder.departure_time1.setText("પ્રસ્થાન સમય : " + onwardBooking.getBusService().getDepartureTime());
            holder.service_class1.setText("સેવા વર્ગ : " + onwardBooking.getBusService().getServiceClass());

            if (returnBooking != null) {
                holder.pnr1.setText("પી.એન.આર : " + returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber());
                holder.pnr2.setText("પી.એન.આર : " + returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber());
            } else if(divyangBooking != null) {
                holder.pnr1.setText("પી.એન.આર : " + divyangBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber());
                holder.pnr2.setText("પી.એન.આર : " + divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber());
            }else{
                holder.pnr1.setText("પી.એન.આર : " + onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber());
            }

            Double returnPrice = 0d;
            if (returnBooking != null) {

                holder.returnRoute.setText(returnBooking.getStartPlace().getPlaceName() + "-" + returnBooking.getEndPlace().getPlaceName());
                holder.returnJourneyDate.setText("પ્રવાસ તારીખ : " + returnBooking.getJourneyDate());
                holder.returnSeats.setText("બેઠકો : " + listToString(returnBooking.getSelectedSeats()));
                returnPrice = Double.parseDouble(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                holder.price2.setText("રૂપિયા " + returnPrice);
                holder.departure_time2.setText("પ્રસ્થાન સમય : " + returnBooking.getBusService().getDepartureTime());
                holder.service_class2.setText("સેવા વર્ગ : " + returnBooking.getBusService().getServiceClass());

            } else {

                holder.returnLayout.setVisibility(View.GONE);
            }

            if (divyangBooking != null) {

                holder.returnLayout.setVisibility(View.VISIBLE);
                returnjrny.setText("Divyang Helper");
                holder.returnRoute.setText(onwardBooking.getStartPlace().getPlaceName() + "-" + onwardBooking.getEndPlace().getPlaceName());
                holder.returnJourneyDate.setText("પ્રવાસ તારીખ : " + onwardBooking.getJourneyDate());
                holder.returnSeats.setText("બેઠકો : " + listToStringDivyangHelper(onwardBooking.getSelectedSeats()));
                returnPrice = Double.parseDouble(divyangBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                holder.price2.setText("રૂપિયા " + returnPrice);
                holder.departure_time2.setText("પ્રસ્થાન સમય : " + onwardBooking.getBusService().getDepartureTime());
                holder.service_class2.setText("સેવા વર્ગ : " + onwardBooking.getBusService().getServiceClass());

            } else {

                if (returnBooking != null) {
                    holder.returnLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.returnLayout.setVisibility(View.GONE);
                }
            }
            holder.total_fare.setText("રૂપિયા " + (onwardPrice + returnPrice));
        } else {
            holder.onwardRoute.setText(onwardBooking.getStartPlace().getPlaceName() + " - " + onwardBooking.getEndPlace().getPlaceName());
            holder.onwardJourneyDate.setText("Journey Date : " + onwardBooking.getJourneyDate());
            if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")){
                holder.onwardSeats.setText("Seats : " + listToStringDivyang(onwardBooking.getSelectedSeats()));
            }else{
                holder.onwardSeats.setText("Seats : " + listToString(onwardBooking.getSelectedSeats()));
            }

            Double onwardPrice = Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            holder.price1.setText("INR " + onwardPrice);
            holder.departure_time1.setText("Dep.time : " + onwardBooking.getBusService().getDepartureTime());
            holder.service_class1.setText("Service class : " + onwardBooking.getBusService().getServiceClass());

            if (returnBooking != null) {
                holder.pnr1.setText("PNR : " + returnBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber());
                holder.pnr2.setText("PNR : " + returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber());
            } else if(divyangBooking != null) {
                holder.pnr1.setText("PNR : " + divyangBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber());
                holder.pnr2.setText("PNR : " + divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber());
            }else{
                holder.pnr1.setText("PNR : " + onwardBooking.getSaveBookingDetailsTemporary().getPnrNumber());
            }

            Double returnPrice = 0d;
            if (returnBooking != null) {

                holder.returnRoute.setText(returnBooking.getStartPlace().getPlaceName() + "-" + returnBooking.getEndPlace().getPlaceName());
                holder.returnJourneyDate.setText("Journey Date : " + returnBooking.getJourneyDate());
                holder.returnSeats.setText("Seats : " + listToString(returnBooking.getSelectedSeats()));
                returnPrice = Double.parseDouble(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                holder.price2.setText("INR " + returnPrice);
                holder.departure_time2.setText("Dep.time : " + returnBooking.getBusService().getDepartureTime());
                holder.service_class2.setText("Service class : " + returnBooking.getBusService().getServiceClass());

            } else {

                holder.returnLayout.setVisibility(View.GONE);
            }
            if (divyangBooking != null) {
                holder.returnLayout.setVisibility(View.VISIBLE);
                returnjrny.setText("Divyang Helper");
                holder.returnRoute.setText(onwardBooking.getStartPlace().getPlaceName() + "-" + onwardBooking.getEndPlace().getPlaceName());
                holder.returnJourneyDate.setText("Journey Date : " + onwardBooking.getJourneyDate());
                holder.returnSeats.setText("Seats : " + listToStringDivyangHelper(onwardBooking.getSelectedSeats()));
                returnPrice = Double.parseDouble(divyangBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                holder.price2.setText("INR " + returnPrice);
                holder.departure_time2.setText("Dep.time : " + onwardBooking.getBusService().getDepartureTime());
                holder.service_class2.setText("Service class : " + onwardBooking.getBusService().getServiceClass());

            } else {
                if (returnBooking != null) {
                    holder.returnLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.returnLayout.setVisibility(View.GONE);
                }
            }

            holder.total_fare.setText("INR " + (onwardPrice + returnPrice));
        }
        showRateApp();
    }

    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Getting the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                });
            }
        });
    }


    public String listToString(List<Seat> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size() ; i++){
            if(i == 0){
                sb.append(list.get(i));
            }
            else{
                sb.append(","+list.get(i));
            }
        }
        return sb.toString();
    }

    public String listToStringDivyang(List<Seat> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size()-1 ; i++){
            if(i == 0){
                sb.append(list.get(i));
            }
            else{
                sb.append(","+list.get(i));
            }
        }
        return sb.toString();
    }


    public String listToStringDivyangHelper(List<Seat> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < list.size() ; i++){
            if(i == 1){
                sb.append(list.get(i));
            }
            else{
                sb.append(","+list.get(i));
            }
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(BookingSuccessActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        intent.putExtras(data);
        startActivity(intent);
    }
    
    static class ViewHolder{
        View onwardLayout, returnLayout;
        TextView total_fare;
        TextView onwardRoute, onwardJourneyDate, onwardSeats, departure_time1, service_class1, price1, ticket_number1, pnr1;
        TextView returnRoute, returnJourneyDate, returnSeats, departure_time2, service_class2, price2, ticket_number2, pnr2;
        ViewHolder(Activity activity){
            onwardLayout = activity.findViewById(R.id.onward_layout);
            returnLayout = activity.findViewById(R.id.return_layout);

            total_fare = (TextView)activity.findViewById(R.id.total_fare);

            onwardRoute = (TextView)activity.findViewById(R.id.route1);
            onwardJourneyDate = (TextView)activity.findViewById(R.id.journey_date1);
            onwardSeats = (TextView)activity.findViewById(R.id.seat_numbers1);
            departure_time1 = (TextView)activity.findViewById(R.id.departure_time1);
            service_class1 = (TextView)activity.findViewById(R.id.service_class1);
            price1 = (TextView)activity.findViewById(R.id.price1);
            ticket_number1 = (TextView)activity.findViewById(R.id.ticket_number1);
            pnr1 = (TextView)activity.findViewById(R.id.pnr1);
            
            returnRoute = (TextView)activity.findViewById(R.id.route2);
            returnJourneyDate = (TextView)activity.findViewById(R.id.journey_date2);
            returnSeats = (TextView)activity.findViewById(R.id.seat_numbers2);
            departure_time2 = (TextView)activity.findViewById(R.id.departure_time2);
            service_class2 = (TextView)activity.findViewById(R.id.service_class2);
            price2 = (TextView)activity.findViewById(R.id.price2);
            ticket_number2 = (TextView)activity.findViewById(R.id.ticket_number2);
            pnr2 = (TextView)activity.findViewById(R.id.pnr2);
        }
    }
}
