package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.List;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.GetAllServicePickupPointsByServiceIDResponse;
import com.gsrtc.mobileweb.models.GetTotalFareDetailsOfTicketResponse;
import com.gsrtc.mobileweb.models.SaveBookingDetailsTemporary;
import com.gsrtc.mobileweb.models.SaveReturnBookingDetailsTemporary;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;

import com.gsrtc.mobileweb.ui.widgets.AccordionWidget;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;

public class FareDetailsActivity extends BaseNavigationDrawerActivity {

    Booking onwardBooking, returnBooking,divyangBooking;
    ViewHolder holder;
    private String isCard="733";
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_fare_details);

        holder = new ViewHolder(this);

        Bundle extras = getIntent().getExtras();
        onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);

        if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
            returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
        } else {
            holder.fare_details_return.setVisibility(View.GONE);
            holder.travel_details_content_return.setVisibility(View.GONE);
        }
        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            divyangBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG);
            holder.fare_details_return.setVisibility(View.VISIBLE);
            holder.travel_details_content_return.setVisibility(View.VISIBLE);
        }
        textlang = onwardBooking.getSearchParams().getTextlanguage().trim();

        AccordionWidget accordionWidget = (AccordionWidget) findViewById(R.id.travel_details_accordion);
        accordionWidget.data(textlang);
        accordionWidget.setExpandLayout(findViewById(R.id.travel_details_content));

        if (textlang.equalsIgnoreCase("gujarati")) {

            /**onward ticket pass info**/
            holder.txtfrom1.setText(R.string.fare_from);
            holder.txtto1.setText(R.string.fare_to);
            holder.txtjourneyDate1.setText(R.string.fare_journey);
            holder.txttripCode1.setText(R.string.fare_tripco);
            holder.txtserviceClass1.setText(R.string.fare_cls_service);
            holder.txtboardingPoint1.setText(R.string.fare_boarding);
            holder.txtdepTime1.setText(R.string.fare_departure);
            holder.txtseatCount1.setText(R.string.fare_no_seats);
            holder.txtseatNo1.setText(R.string.fare_seat_no);
            holder.txtpassenger_name1.setText(R.string.fare_psname);

            //return ticket passinfo
            holder.txtfrom2.setText(R.string.fare_from);
            holder.txtto2.setText(R.string.fare_to);
            holder.txtjourneyDate2.setText(R.string.fare_journey);
            holder.txttripCode2.setText(R.string.fare_from);
            holder.txtserviceClass2.setText(R.string.fare_cls_service);
            holder.txtboardingPoint2.setText(R.string.fare_boarding);
            holder.txtdepTime2.setText(R.string.fare_departure);
            holder.txtseatCount2.setText(R.string.fare_no_seats);
            holder.txtseatNo2.setText(R.string.fare_seat_no);
            holder.txtpassenger_name2.setText(R.string.fare_psname);

            //onward ticket fareinfo
            holder.txtbasic_fare1.setText(R.string.fare_basicfare);
            holder.txtres_fee1.setText(R.string.fare_resv_fee);
            holder.txtacc_ins_charge1.setText(R.string.fare_acc_ins_charges);
            holder.txttoll_fee1.setText(R.string.fare_toll_fee);
            holder.toll_fee_rajasthan1.setText(R.string.fare_toll_fee_rajs);
            holder.txtother_levies1.setText(R.string.fare_other_levies);
            holder.txtserv_tax1.setText(R.string.fare_gst);
            holder.txtconcession1.setText(R.string.fare_concession);
            holder.txtdiscounts1.setText(R.string.fare_discounts);
            holder.txttotal_fare1.setText(R.string.fare_tot_fare);

            // return ticket fareinfo //
            holder.txtbasic_fare2.setText(R.string.fare_basicfare);
            holder.txtres_fee2.setText(R.string.fare_resv_fee);
            holder.txtacc_ins_charge2.setText(R.string.fare_acc_ins_charges);
            holder.txttoll_fee2.setText(R.string.fare_toll_fee);
            holder.txttoll_fee_rajasthan2.setText(R.string.fare_toll_fee_rajs);
            holder.txtother_levies2.setText(R.string.fare_other_levies);
            holder.txtserv_tax2.setText(R.string.fare_gst);
            holder.txtconcession2.setText(R.string.fare_concession);
            holder.txtdiscounts2.setText(R.string.fare_discounts);
            holder.txttotal_fare2.setText(R.string.fare_tot_fare);

            holder.fare_title.setText(R.string.fare_fare_details);

            holder.submitBtn.setText(R.string.fare_mak_paybtn);
            holder.onward_tkt.setText(R.string.fare_onward_ticket);
            holder.return_tkt.setText(R.string.fare_return_ticket);
            holder.onward_fare.setText(R.string.fare_on_tkt_fare);
            holder.return_fare.setText(R.string.fare_rtn_tkt_fare);

        }

        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")){
            holder.onward_tkt.setText("Divyang Ticket");
            holder.return_tkt.setText("Divyang with Helper Ticket");
            holder.onward_fare.setText("Divyang Fare");
            holder.return_fare.setText("Divyang with Helper Fare");
    }

        holder.main_content.setVisibility(View.GONE);
        showLoading(true);
        CustomisedProgressDialog.SHOW_CUST_DIA(FareDetailsActivity.this, "Please wait loading... ");

        fetchPickupPoints(onwardBooking, false);
        fetchPickupPoints(onwardBooking, true);//For Drop point

        if (returnBooking != null) {
            fetchPickupPoints(returnBooking, false);
            fetchPickupPoints(returnBooking, true);
        }

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomisedProgressDialog.SHOW_CUST_DIA(FareDetailsActivity.this, "Please wait loading...");
                goNext();
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

    /***calling PICKUP AND DROPOFF POINT METHOD  ****/

    public void fetchPickupPoints(final Booking booking, final boolean drop) {

        AdvanceBookingNetworkUtil.GetAllServicePickupPointsByServiceID(booking, drop, new AdvanceBookingNetworkUtil.GetAllServicePickupPointsByServiceIDCallback() {
            @Override
            public void onSuccess(GetAllServicePickupPointsByServiceIDResponse response, boolean drop) {
                if (drop) {
                    booking.setDropPointDetails(response);
                } else {
                    booking.setPickupPointDetails(response);
                }

                pickUpPointDetailsFetched();
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }


    public void pickUpPointDetailsFetched() {


        if (onwardBooking.getPickupPointDetails() == null || onwardBooking.getDropPointDetails() == null ||
                (returnBooking != null && (returnBooking.getPickupPointDetails() == null || returnBooking.getDropPointDetails() == null))) {
            return;
        }

        if (returnBooking != null) {
            //Booking objBooking = new Booking()
            fetchFareDetails(onwardBooking, true);
            //onwardReferenceNo(onwardBooking,true);
        } else {
            fetchFareDetails(onwardBooking, false);
            //onwardReferenceNo(onwardBooking,false);
        }
    }

    /***calling FAREDETAILS METHOD  ****/

    public void fetchFareDetails(final Booking booking, Boolean hasReturn) {

        AdvanceBookingNetworkUtil.GetTotalFareDetailsOfTicket(booking, hasReturn, new AdvanceBookingNetworkUtil.GetTotalFareDetailsOfTicketCallback() {
            @Override
            public void onSuccess(GetTotalFareDetailsOfTicketResponse response) {
                booking.setTotalFareDetailsOfTicketResponse(response);

                if (returnBooking == null) {
                    if (onwardBooking.getPickupPointDetails() == null || onwardBooking.getDropPointDetails() == null) {
                        return;
                    }
                    if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
                        if (divyangBooking.getTotalFareDetailsOfTicketResponse() == null) {
                            fetchDivyangFareDetails(onwardBooking, false);
                        }
                    }else{
                        populateViews();
                    }

                } else {
                    if (returnBooking.getTotalFareDetailsOfTicketResponse() == null) {
                        fetchFareDetails(returnBooking, true);
                    } else {
                        if (onwardBooking.getPickupPointDetails() == null || onwardBooking.getDropPointDetails() == null ||
                                (returnBooking.getPickupPointDetails() == null || returnBooking.getDropPointDetails() == null)) {
                            return;
                        }
                        populateViews();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }


    /***calling DIVYANG FAREDETAILS METHOD  ****/

    public void fetchDivyangFareDetails(final Booking divyangbk,Boolean hasReturn) {

        AdvanceBookingNetworkUtil.GetDivyangTotalFareDetailsOfTicket(divyangbk, hasReturn, new AdvanceBookingNetworkUtil.GetDivyangTotalFareDetailsOfTicketCallback() {
            @Override
            public void onSuccess(GetTotalFareDetailsOfTicketResponse response) {
                divyangBooking.setTotalFareDetailsOfTicketResponse(response);

                    if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

                            populateViews();
                    }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }
    /***GET ONWARD REFNO METHOD  ****/

    public void onwardReferenceNo(final Booking booking, Boolean hasReturn, String payType) {

        AdvanceBookingNetworkUtil.GetOnwardReferenceNumber(booking, hasReturn, payType, new AdvanceBookingNetworkUtil.GetOnwardReferenceNumberCallback() {
            @Override
            public void onSuccess(SaveBookingDetailsTemporary response) {
                booking.setSaveBookingDetailsTemporary(response);
                onwardBooking.setSaveBookingDetailsTemporary(response);

                if (onwardBooking.getSaveBookingDetailsTemporary() != null) {

                    Log.d("RTCRefNo", onwardBooking.getSaveBookingDetailsTemporary().getRtcRefNo());

                        Bundle extras = new Bundle();
                        extras.putString("textlang", textlang);
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                        double tF = Double.valueOf(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());

                        extras.putString("totalFare", String.valueOf(tF));
                        extras.putString("iscard", isCard);

                        ActivityUtil.openNewActivity(FareDetailsActivity.this, ReferenceNumberActivity.class, extras);
                        CustomisedProgressDialog.STOP_CUST_DIA();


                } else {

                    Log.d("RTCRefNo", "No RTCRefNo");
                    CustomisedProgressDialog.STOP_CUST_DIA();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FareDetailsActivity.this,R.style.CustomDialogTheme);

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(FareDetailsActivity.this, HomeActivity.class);
                            Bundle data = new Bundle();
                            data.putString("textlang", textlang);
                            i.putExtras(data);
                            startActivity(i);
                        }
                    });

                    alertDialog.setMessage("Error Occured! Please Try again.");
                    alertDialog.setTitle("GSRTC");
                    alertDialog.show();
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }

    /***GET ONWARD DIVYANG HELPER REFNO METHOD  ****/

    public void divyangReferenceNo(final Booking booking,final Booking divyangbook, Boolean hasReturn, String payType) {

        AdvanceBookingNetworkUtil.GetDivyangReferenceNumber(booking,divyangbook, hasReturn, payType, new AdvanceBookingNetworkUtil.GetDivyangReferenceNumberCallback() {
            @Override
            public void onSuccess(SaveReturnBookingDetailsTemporary response) {
                booking.setSaveReturnBookingDetailsTemporary(response);
                divyangbook.setSaveReturnBookingDetailsTemporary(response);
                onwardBooking.setSaveReturnBookingDetailsTemporary(response);
                divyangBooking.setSaveReturnBookingDetailsTemporary(response);

                if (onwardBooking.getSaveReturnBookingDetailsTemporary() != null) {

                    Log.d("RTCRefNo", onwardBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo());

                    Bundle extras = new Bundle();
                    extras.putString("textlang", textlang);
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                    double tF = Double.valueOf(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                    if (divyangBooking != null) {
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, divyangBooking);
                        tF += Double.valueOf(divyangBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                    }

                    extras.putString("totalFare", String.valueOf(tF));
                    extras.putString("iscard", isCard);

                    ActivityUtil.openNewActivity(FareDetailsActivity.this, ReferenceNumberActivity.class, extras);
                    CustomisedProgressDialog.STOP_CUST_DIA();

                } else {

                    Log.d("RTCRefNo", "No RTCRefNo");
                    CustomisedProgressDialog.STOP_CUST_DIA();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FareDetailsActivity.this,R.style.CustomDialogTheme);

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(FareDetailsActivity.this, HomeActivity.class);
                            Bundle data = new Bundle();
                            data.putString("textlang", textlang);
                            i.putExtras(data);
                            startActivity(i);
                        }
                    });

                    alertDialog.setMessage("Error Occured! Please Try again.");
                    alertDialog.setTitle("GSRTC");
                    alertDialog.show();
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }

    //GET RETURN REFNO METHOD

    public void returnReferenceNo(final Booking argOnwardBooking, final Booking argReturnBooking, Boolean hasReturn, String payType) {
        AdvanceBookingNetworkUtil.GetReturnReferenceNumber(argOnwardBooking, argReturnBooking, hasReturn, payType, new AdvanceBookingNetworkUtil.GetReturnReferenceNumberCallback() {
            @Override
            public void onSuccess(SaveReturnBookingDetailsTemporary response) {
                argOnwardBooking.setSaveReturnBookingDetailsTemporary(response);
                argReturnBooking.setSaveReturnBookingDetailsTemporary(response);
                onwardBooking.setSaveReturnBookingDetailsTemporary(response);
                returnBooking.setSaveReturnBookingDetailsTemporary(response);


                if (onwardBooking.getSaveReturnBookingDetailsTemporary() != null) {
                    Log.d("RTCRefNo", onwardBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo());

                    Bundle extras = new Bundle();
                    extras.putString("textlang", textlang);
                    extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
                    double tF = Double.valueOf(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                    if (returnBooking != null) {
                        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
                        tF += Double.valueOf(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
                    }
                    extras.putString("totalFare", String.valueOf(tF));
                    extras.putString("iscard", isCard);

                    ActivityUtil.openNewActivity(FareDetailsActivity.this, ReferenceNumberActivity.class, extras);
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
                else
                {
                    Log.d("RTCRefNo", "No RTCRefNo");
                    CustomisedProgressDialog.STOP_CUST_DIA();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FareDetailsActivity.this,R.style.CustomDialogTheme);

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(FareDetailsActivity.this, HomeActivity.class);
                            Bundle data = new Bundle();
                            data.putString("textlang", textlang);
                            i.putExtras(data);
                            startActivity(i);
                        }
                    });

                    // alertDialog.setNegativeButton("", null);
                    alertDialog.setMessage("Error Occured! Please Try again.");
                    alertDialog.setTitle("GSRTC");
                    alertDialog.show();
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(FareDetailsActivity.this, e);
            }
        });
    }



    @SuppressLint("SetTextI18n")
    public void populateViews() {
        //When all details are not fetched
        if (onwardBooking.getTotalFareDetailsOfTicketResponse() == null || (returnBooking != null && returnBooking.getTotalFareDetailsOfTicketResponse() == null)) {   //This will be true when both are loaded
            return;
        }
        holder.main_content.setVisibility(View.VISIBLE);
        showLoading(false);
        CustomisedProgressDialog.STOP_CUST_DIA();

        holder.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getText().toString().equalsIgnoreCase("ICICI")) {

                    isCard = "733";

                } else if(button.getText().toString().equalsIgnoreCase("PayGov")) {

                    isCard = "724";

                }else if(button.getText().toString().equalsIgnoreCase("AggrePay")) {

                    isCard = "721";

                }else if(button.getText().toString().equalsIgnoreCase("AXIS")) {

                    isCard = "744";

                } else{

                    isCard = "722";

                }
            }
        });


        holder.radio.check(R.id.paytm);
        holder.from1.setText(onwardBooking.getStartPlace().getPlaceName());
        holder.to1.setText(onwardBooking.getEndPlace().getPlaceName());
        holder.journeyDate1.setText(onwardBooking.getJourneyDate());
        holder.tripCode1.setText(onwardBooking.getBusService().getTripCode());
        holder.serviceClass1.setText(onwardBooking.getBusService().getServiceClass());
        holder.boardingPoint1.setText(onwardBooking.getPickupPointDetails().getPickupPointName());
        holder.depTime1.setText(onwardBooking.getBusService().getDepartureTime());
        if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")){
            holder.seatCount1.setText("1");
            holder.seatNo1.setText(listToStringDivyang(onwardBooking.getSelectedSeats()));
        }else{
            holder.seatCount1.setText("" + onwardBooking.getSearchParams().getNumOfPassengers());
            holder.seatNo1.setText(listToString(onwardBooking.getSelectedSeats()));
        }
        holder.passenger_name1.setText(onwardBooking.getPassengers().get(0).getPassengerNameA());

        holder.basic_fare1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getBasicFare());
        holder.res_fee1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getReservationFee());
        holder.toll_fee1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee());
        holder.entryfee1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
        //holder.toll_fee_rajasthan1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getTo());//TODO
        holder.other_levies1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies());
        holder.serv_tax1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
        //holder.concession1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getCon());//TODO
        holder.concession1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession());
        holder.discounts1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getDiscounts());
        holder.total_fare1.setText(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());


        if (returnBooking != null) {

            holder.from2.setText(returnBooking.getStartPlace().getPlaceName());
            holder.to2.setText(returnBooking.getEndPlace().getPlaceName());
            holder.journeyDate2.setText(returnBooking.getJourneyDate());
            holder.tripCode2.setText(returnBooking.getBusService().getTripCode());
            holder.serviceClass2.setText(returnBooking.getBusService().getServiceClass());
            holder.boardingPoint2.setText(returnBooking.getPickupPointDetails().getPickupPointName());
            holder.depTime2.setText(returnBooking.getBusService().getDepartureTime());
            holder.seatCount2.setText("" + returnBooking.getSearchParams().getNumOfPassengers());
            holder.seatNo2.setText(listToString(returnBooking.getSelectedSeats()));
            holder.passenger_name2.setText(returnBooking.getPassengers().get(0).getPassengerNameA());

            holder.basic_fare2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getBasicFare());
            holder.res_fee2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getReservationFee());
            holder.toll_fee2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee());
            holder.entryfee2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
            //holder.toll_fee_rajasthan2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getTo());//TODO
            holder.other_levies2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies());
            holder.serv_tax2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
            //holder.concession2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getCon());//TODO
            holder.concession2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession());
            holder.discounts2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getDiscounts());
            holder.total_fare2.setText(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());


        } else {
            holder.travel_details_content_return.setVisibility(View.GONE);
        }

        if (divyangBooking != null) {

            holder.travel_details_content_return.setVisibility(View.VISIBLE);
            holder.from2.setText(onwardBooking.getStartPlace().getPlaceName());
            holder.to2.setText(onwardBooking.getEndPlace().getPlaceName());
            holder.journeyDate2.setText(onwardBooking.getJourneyDate());
            holder.tripCode2.setText(onwardBooking.getBusService().getTripCode());
            holder.serviceClass2.setText(onwardBooking.getBusService().getServiceClass());
            holder.boardingPoint2.setText(onwardBooking.getPickupPointDetails().getPickupPointName());
            holder.depTime2.setText(onwardBooking.getBusService().getDepartureTime());
            holder.seatCount2.setText("1");
            holder.seatNo2.setText(listToStringDivyangHelper(onwardBooking.getSelectedSeats()));
            holder.passenger_name2.setText(onwardBooking.getPassengers().get(0).getPassengerNameB());

            holder.basic_fare2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getBasicFare());
            holder.res_fee2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getReservationFee());
            holder.toll_fee2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee());
            holder.entryfee2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
            holder.other_levies2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies());
            holder.serv_tax2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getEntryFee());
            holder.concession2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession());
            holder.discounts2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getDiscounts());
            holder.total_fare2.setText(divyangBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());


        } else {
            if(returnBooking != null){
                holder.travel_details_content_return.setVisibility(View.VISIBLE);
            }else {
                holder.travel_details_content_return.setVisibility(View.GONE);
            }
        }
    }

    public void goNext() {

        if (onwardBooking.getTotalFareDetailsOfTicketResponse() == null || (returnBooking != null && returnBooking.getTotalFareDetailsOfTicketResponse() == null)) {
            return;
        }

        if(!isCard.isEmpty()) {

            if (returnBooking != null) {
                returnReferenceNo(onwardBooking, returnBooking, false, isCard);
            } else {
                if(onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")){
                    divyangReferenceNo(onwardBooking,divyangBooking, false, isCard);
                }else {
                    onwardReferenceNo(onwardBooking, false, isCard);
                }
            }
        }
        else
        {
            CustomisedProgressDialog.STOP_CUST_DIA();
            ActivityUtil.showToast(this, "Please select your PaymentType");
        }
    }

    public String listToString(List<Seat> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                sb.append(list.get(i));
            } else {
                sb.append("," + list.get(i));
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

    public class ViewHolder {
        View travel_details_content_onward, travel_details_content_return;
        TextView from1, to1, journeyDate1, tripCode1, serviceClass1, boardingPoint1, alightingPoint1, depTime1, seatCount1, seatNo1, passenger_name1;
        TextView from2, to2, journeyDate2, tripCode2, serviceClass2, boardingPoint2, alightingPoint2, depTime2, seatCount2, seatNo2, passenger_name2;

        View fare_details_onward, fare_details_return;
        TextView basic_fare1, res_fee1, acc_ins_charge1, toll_fee1, toll_fee_rajasthan1, other_levies1, serv_tax1, concession1, discounts1, total_fare1, entryfee1;
        TextView basic_fare2, res_fee2, acc_ins_charge2, toll_fee2, toll_fee_rajasthan2, other_levies2, serv_tax2, concession2, discounts2, total_fare2, entryfee2;

        TextView txtfrom1, txtto1, txtjourneyDate1, txttripCode1, txtserviceClass1, txtboardingPoint1, txtalightingPoint1, txtdepTime1, txtseatCount1, txtseatNo1, txtpassenger_name1;
        TextView txtfrom2, txtto2, txtjourneyDate2, txttripCode2, txtserviceClass2, txtboardingPoint2, txtalightingPoint2, txtdepTime2, txtseatCount2, txtseatNo2, txtpassenger_name2;

        View txtfare_details_onward, txtfare_details_return;
        TextView txtbasic_fare1, txtres_fee1, txtacc_ins_charge1, txttoll_fee1, txttoll_fee_rajasthan1, txtother_levies1, txtserv_tax1, txtconcession1, txtdiscounts1, txttotal_fare1, txtentryfee1;
        TextView txtbasic_fare2, txtres_fee2, txtacc_ins_charge2, txttoll_fee2, txttoll_fee_rajasthan2, txtother_levies2, txtserv_tax2, txtconcession2, txtdiscounts2, txttotal_fare2, txtentryfee2;
        TextView fare_title,credit_debit,net_bank,onward_tkt,return_tkt,onward_fare,return_fare;

        Button submitBtn;
        SwipeRefreshLayout swipeContainer;
        View main_content;
        RadioGroup radio;
        RadioButton paygov;
        RadioButton phonepe;
        RadioButton aggrepay;
        RadioButton paytm;
        RadioButton axis;

        View loadingLayout;

        @SuppressLint("CutPasteId")
        ViewHolder(Activity activity) {
            swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);

            main_content = activity.findViewById(R.id.main_content);
            radio = (RadioGroup) activity.findViewById(R.id.radio);
            paygov = (RadioButton) activity.findViewById(R.id.paygov);
            phonepe = (RadioButton) activity.findViewById(R.id.phonepe);
            aggrepay = (RadioButton) activity.findViewById(R.id.aggrepay);
            paytm = (RadioButton) activity.findViewById(R.id.paytm);
            axis = (RadioButton) activity.findViewById(R.id.axisPay);

            from1 = (TextView) activity.findViewById(R.id.from1);
            to1 = (TextView) activity.findViewById(R.id.to1);
            journeyDate1 = (TextView) activity.findViewById(R.id.journeyDate1);
            tripCode1 = (TextView) activity.findViewById(R.id.tripCode1);
            serviceClass1 = (TextView) activity.findViewById(R.id.serviceClass1);
            boardingPoint1 = (TextView) activity.findViewById(R.id.boardingPoint1);
            alightingPoint1 = (TextView) activity.findViewById(R.id.alightingPoint1);
            depTime1 = (TextView) activity.findViewById(R.id.depTime1);
            seatCount1 = (TextView) activity.findViewById(R.id.seatCount1);
            seatNo1 = (TextView) activity.findViewById(R.id.seatNo1);
            passenger_name1 = (TextView) activity.findViewById(R.id.passenger_name1);

            from2 = (TextView) activity.findViewById(R.id.from2);
            to2 = (TextView) activity.findViewById(R.id.to2);
            journeyDate2 = (TextView) activity.findViewById(R.id.journeyDate2);
            tripCode2 = (TextView) activity.findViewById(R.id.tripCode2);
            serviceClass2 = (TextView) activity.findViewById(R.id.serviceClass2);
            boardingPoint2 = (TextView) activity.findViewById(R.id.boardingPoint2);
            alightingPoint2 = (TextView) activity.findViewById(R.id.alightingPoint2);
            depTime2 = (TextView) activity.findViewById(R.id.depTime2);
            seatCount2 = (TextView) activity.findViewById(R.id.seatCount2);
            seatNo2 = (TextView) activity.findViewById(R.id.seatNo2);
            passenger_name2 = (TextView) activity.findViewById(R.id.passenger_name2);

            travel_details_content_onward = activity.findViewById(R.id.travel_details_content_onward);
            travel_details_content_return = activity.findViewById(R.id.travel_details_content_return);

            basic_fare1 = (TextView) activity.findViewById(R.id.basic_fare1);
            res_fee1 = (TextView) activity.findViewById(R.id.res_fee1);
            entryfee1 = (TextView) activity.findViewById(R.id.entryfee1);
            acc_ins_charge1 = (TextView) activity.findViewById(R.id.acc_ins_charge1);
            toll_fee1 = (TextView) activity.findViewById(R.id.toll_fee1);
            toll_fee_rajasthan1 = (TextView) activity.findViewById(R.id.toll_fee_rajasthan1);
            other_levies1 = (TextView) activity.findViewById(R.id.other_levies1);
            serv_tax1 = (TextView) activity.findViewById(R.id.serv_tax1);
            concession1 = (TextView) activity.findViewById(R.id.concession1);
            discounts1 = (TextView) activity.findViewById(R.id.discounts1);
            total_fare1 = (TextView) activity.findViewById(R.id.total_fare1);

            basic_fare2 = (TextView) activity.findViewById(R.id.basic_fare2);
            res_fee2 = (TextView) activity.findViewById(R.id.res_fee2);
            entryfee2 = (TextView) activity.findViewById(R.id.entryfee2);
            acc_ins_charge2 = (TextView) activity.findViewById(R.id.acc_ins_charge2);
            toll_fee2 = (TextView) activity.findViewById(R.id.toll_fee2);
            toll_fee_rajasthan2 = (TextView) activity.findViewById(R.id.toll_fee_rajasthan2);
            other_levies2 = (TextView) activity.findViewById(R.id.other_levies2);
            serv_tax2 = (TextView) activity.findViewById(R.id.serv_tax2);
            concession2 = (TextView) activity.findViewById(R.id.concession2);
            discounts2 = (TextView) activity.findViewById(R.id.discounts2);
            total_fare2 = (TextView) activity.findViewById(R.id.total_fare2);

            fare_details_onward = activity.findViewById(R.id.fare_details_onward);
            fare_details_return = activity.findViewById(R.id.fare_details_return);

            /**text language**/
            txtfrom1 = (TextView) activity.findViewById(R.id.on_from);
            txtto1 = (TextView) activity.findViewById(R.id.on_to);
            txtjourneyDate1 = (TextView) activity.findViewById(R.id.on_journey);
            txttripCode1 = (TextView) activity.findViewById(R.id.on_tripco);
            txtserviceClass1 = (TextView) activity.findViewById(R.id.on_cls_servc);
            txtboardingPoint1 = (TextView) activity.findViewById(R.id.on_boarding);
            txtalightingPoint1 = (TextView) activity.findViewById(R.id.alightingPoint1);
            txtdepTime1 = (TextView) activity.findViewById(R.id.on_depart_time);
            txtseatCount1 = (TextView) activity.findViewById(R.id.on_no_of_seats);
            txtseatNo1 = (TextView) activity.findViewById(R.id.on_seat_no);
            txtpassenger_name1 = (TextView) activity.findViewById(R.id.on_pass_name);

            txtfrom2 = (TextView) activity.findViewById(R.id.rtn_from);
            txtto2 = (TextView) activity.findViewById(R.id.rtn_to);
            txtjourneyDate2 = (TextView) activity.findViewById(R.id.rtn_journey);
            txttripCode2 = (TextView) activity.findViewById(R.id.rtn_tripco);
            txtserviceClass2 = (TextView) activity.findViewById(R.id.rtn_cls_serv);
            txtboardingPoint2 = (TextView) activity.findViewById(R.id.rtn_boarding);
            txtalightingPoint2 = (TextView) activity.findViewById(R.id.alightingPoint2);
            txtdepTime2 = (TextView) activity.findViewById(R.id.rtn_depart_time);
            txtseatCount2 = (TextView) activity.findViewById(R.id.rtn_no_seats);
            txtseatNo2 = (TextView) activity.findViewById(R.id.rtn_seat_no);
            txtpassenger_name2 = (TextView) activity.findViewById(R.id.rtn_pass_name);

            travel_details_content_onward = activity.findViewById(R.id.travel_details_content_onward);
            travel_details_content_return = activity.findViewById(R.id.travel_details_content_return);

            txtbasic_fare1 = (TextView) activity.findViewById(R.id.on_basic_fare);
            txtres_fee1 = (TextView) activity.findViewById(R.id.on_resv_fee);
            txtentryfee1 = (TextView) activity.findViewById(R.id.entryfee1);
            txtacc_ins_charge1 = (TextView) activity.findViewById(R.id.on_acc_ins_charge);
            txttoll_fee1 = (TextView) activity.findViewById(R.id.on_tollfee);
            toll_fee_rajasthan1 = (TextView) activity.findViewById(R.id.on_toll_rajs);
            txtother_levies1 = (TextView) activity.findViewById(R.id.on_other_levi);
            txtserv_tax1 = (TextView) activity.findViewById(R.id.on_gst);
            txtconcession1 = (TextView) activity.findViewById(R.id.on_concess);
            txtdiscounts1 = (TextView) activity.findViewById(R.id.on_discounts);
            txttotal_fare1 = (TextView) activity.findViewById(R.id.on_total_fare);

            txtbasic_fare2 = (TextView) activity.findViewById(R.id.rtn_basic_fare);
            txtres_fee2 = (TextView) activity.findViewById(R.id.rtn_resv_fare);
            txtentryfee2 = (TextView) activity.findViewById(R.id.entryfee2);
            txtacc_ins_charge2 = (TextView) activity.findViewById(R.id.rtn_acc_ins_charge);
            txttoll_fee2 = (TextView) activity.findViewById(R.id.rtn_toll_fare);
            txttoll_fee_rajasthan2 = (TextView) activity.findViewById(R.id.rtn_toll_fare_rajs);
            txtother_levies2 = (TextView) activity.findViewById(R.id.rtn_other_levi);
            txtserv_tax2 = (TextView) activity.findViewById(R.id.rtn_gst);
            txtconcession2 = (TextView) activity.findViewById(R.id.rtn_concess);
            txtdiscounts2 = (TextView) activity.findViewById(R.id.rtn_discounts);
            txttotal_fare2 = (TextView) activity.findViewById(R.id.rtn_total_fare);

            txtfare_details_onward = activity.findViewById(R.id.fare_details_onward);
            txtfare_details_return = activity.findViewById(R.id.fare_details_return);

            submitBtn = (Button) activity.findViewById(R.id.submitBtn);
            loadingLayout = activity.findViewById(R.id.loading_layout);
            fare_title = (TextView) activity.findViewById(R.id.header_text);

            onward_tkt = (TextView) activity.findViewById(R.id.onward_ticket);
            return_tkt = (TextView) activity.findViewById(R.id.return_ticket);
            onward_fare = (TextView) activity.findViewById(R.id.onward_ticket_fare);
            return_fare = (TextView) activity.findViewById(R.id.return_ticket_fare);
        }
    }
}

