package com.gsrtc.mobileweb.ui.activities.advance_booking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArraySet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import android.os.VibrationEffect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;
import android.os.Looper;
import java.util.Set;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.SeatRow;
import com.gsrtc.mobileweb.models.ServiceSeatDetails;
import com.gsrtc.mobileweb.models.TripSheetData;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.adapters.SeatSelectionAdapter;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.AdvanceBookingNetworkUtil;
import android.speech.tts.TextToSpeech;
import java.util.Collections;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    boolean isTtsReady = true;
    boolean hasSpoken = false;
    private boolean hasSpokenDescription = false;


    ProgressDialog seatBlockingProgressDialog;

    boolean layoutFetched = false, serviceSeatDetailsFetched = false;

    private ArrayList<TripSheetData> mArrLsttripsheetdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_seat_selection);

        isTtsReady = true;
        trySpeakSeats();

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
           // getTripSheetDetails();

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
                hasSpokenDescription = false;
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

        View.OnClickListener headerClickListener = v -> speakSeatSelectionDescription();
        if (seatcls != null) seatcls.setOnClickListener(headerClickListener);
        if (classservice != null) classservice.setOnClickListener(headerClickListener);
        if (tripcode != null) tripcode.setOnClickListener(headerClickListener);
        if (tripco != null) tripco.setOnClickListener(headerClickListener);
        if (seatcls != null && seatcls.getParent() instanceof View) {
            ((View) seatcls.getParent()).setOnClickListener(headerClickListener);
        }

        View categories = findViewById(R.id.seat_categories);
        if (categories != null) {
            categories.setOnClickListener(v -> {
                boolean isSleeper = isSleeperBus();
                String msg = isSleeper ? "Seat layout legend: Sleeper berths are arranged in lower and upper deck."
                        : "Seat layout legend: Available seats are in white, booked seats in grey, and ladies seats in pink.";
                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(msg);
            });
        }
    }

    private boolean isDivyangUser() {
        return com.gsrtc.mobileweb.utils.divyang.DivyangHelper.isDivyangModeEnabled(this);
    }
    public void setupSeatView(List<SeatRow> seatRows, ServiceSeatDetails serviceSeatDetails, String layout_id, String srv_class_id) {

        List<String> sideBerths;
        if (searchParams.getAdultFemaleCount() == 1) {
            String singleSide = singleSideBerth(seatRows, layout_id, serviceSeatDetails);
            sideBerths = Arrays.asList(singleSide.split(","));

            System.out.println(sideBerths);
            if (!sideBerths.get(0).equalsIgnoreCase("") || sideBerths.size() > 1) {
                String[] ber = sideBerths.toArray(new String[0]);
                serviceSeatDetails.setAvailableSeats(ber);

                int totalSeats = seatRows.size() * 6;

                String bookedDummy = "";
                for (int i = 0; i < totalSeats; i++) {
                    if (!sideBerths.contains(String.valueOf(i))) {
                        bookedDummy += i + ",";
                    }
                }

                String[] split = bookedDummy.split(",");
                serviceSeatDetails.setBookedSeats(split);
            }
        }

        seatSelectionAdapter = new SeatSelectionAdapter(
                this,
                seatRows,
                serviceSeatDetails,
                searchParams,
                layout_id,
                srv_class_id,
                new SeatSelectionAdapter.OnSeatActionListener() {

                    @Override
                    public void onSeatSelected(Seat seat) {
                        if (isTtsReady && isDivyangUser()) {
                            boolean isSleeper = isSleeperBus();
                            String seatLabel = getSeatLabel(seat, isSleeper);
                            boolean isHelper = searchParams != null && "Divyang with Helper".equalsIgnoreCase(searchParams.getDivyangBooking());
                            int count = seatSelectionAdapter != null ? seatSelectionAdapter.getSelectedSeats().size() : 1;
                            if (isHelper) {
                                if (count == 1) {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(seatLabel + " selected for Divyang. Please select one more " + (isSleeper ? "berth" : "seat") + " for Helper.");
                                } else {
                                    com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(seatLabel + " selected for Helper.");
                                }
                            } else {
                                com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(seatLabel + " selected");
                            }
                        }
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }

                    @Override
                    public void onSeatAlreadyBooked(Seat seat) {
                        int seatNo = seat != null && seat.getSeatNo() != null ? seat.getSeatNo() : 0;
                        boolean isSleeper = isSleeperBus();
                        String seatLabel = getSeatLabel(seat, isSleeper);
                        String unit = isSleeper ? "berth" : "seat";
                        String msg;
                        if (serviceSeatDetails != null
                                && serviceSeatDetails.getBookedLadiesSeats() != null
                                && serviceSeatDetails.getBookedLadiesSeats().contains(seatNo)) {
                            msg = seatLabel + " is already booked for ladies. Please select another " + unit + ".";
                        } else {
                            msg = seatLabel + " is already booked. Please select another " + unit + ".";
                        }
                        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(msg);
                        Toast.makeText(SeatSelectionActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }

                    @Override
                    public void onSeatUnavailable(Seat seat) {
                        boolean isSleeper = isSleeperBus();
                        String seatLabel = getSeatLabel(seat, isSleeper);
                        String unit = isSleeper ? "berth" : "seat";
                        String msg = seatLabel + " is not available. Please select another " + unit + ".";
                        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(msg);
                        Toast.makeText(SeatSelectionActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }

                    @Override
                    public void onSeatUnselected(Seat seat) {
                        boolean isSleeper = isSleeperBus();
                        String seatLabel = getSeatLabel(seat, isSleeper);
                        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(SeatSelectionActivity.this).speak(seatLabel + " unselected");
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }
                }
        ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void allSeatsSelected(ArrayList<Seat> selectedSeats) {
                if (isTtsReady && selectedSeats != null && !selectedSeats.isEmpty()) {
                    speakSelectedSeat(selectedSeats);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        getReferenceNumber(busService, selectedSeats, searchParams);
                    }, 1700);

                } else {
                    getReferenceNumber(busService, selectedSeats, searchParams);
                }
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(seatSelectionAdapter);
        seatSelectionAdapter.notifyDataSetChanged();
    }

    public boolean isSleeperBus() {
        if (class_layout_id != null && (
                class_layout_id.equalsIgnoreCase("64") ||
                class_layout_id.equalsIgnoreCase("143") ||
                class_layout_id.equalsIgnoreCase("188") ||
                class_layout_id.equalsIgnoreCase("191") ||
                class_layout_id.equalsIgnoreCase("169"))) {
            return true;
        }
        if (class_id != null && (
                class_id.equalsIgnoreCase("21") ||
                class_id.equalsIgnoreCase("22") ||
                class_id.equalsIgnoreCase("42") ||
                class_id.equalsIgnoreCase("45"))) {
            return true;
        }
        if (busService != null && busService.getServiceClass() != null &&
                busService.getServiceClass().toUpperCase().contains("SLEEPER")) {
            return true;
        }
        return false;
    }

    public static String getSeatLabel(Seat seat, boolean isSleeper) {
        if (seat == null) return isSleeper ? "Sleeper berth" : "Seater seat";
        int seatNo = seat.getSeatNo() != null ? seat.getSeatNo() : 0;
        String desc = seat.getDescription();
        String descText = "";
        if (desc != null && !desc.trim().isEmpty()) {
            String d = desc.trim().toUpperCase();
            if (d.equals("LB") || d.contains("LOWER")) {
                descText = "Lower Berth";
            } else if (d.equals("UB") || d.contains("UPPER")) {
                descText = "Upper Berth";
            } else if (d.equals("W") || d.contains("WINDOW")) {
                descText = "Window";
            } else if (d.equals("A") || d.contains("AISLE")) {
                descText = "Aisle";
            } else if (d.equals("M") || d.contains("MIDDLE")) {
                descText = "Middle";
            } else if (d.equals("SL") || d.contains("SLEEPER")) {
                descText = "Sleeper";
            } else if (d.equals("ST") || d.contains("SEATER")) {
                descText = "Seater";
            } else if (d.equals("L") || d.contains("LADIES")) {
                descText = "Ladies";
            } else if (d.equals("D") || d.contains("DIVYANG")) {
                descText = "Divyang";
            } else if (!d.equalsIgnoreCase("NA") && !d.equalsIgnoreCase("NONE") && !d.equalsIgnoreCase("ANY")) {
                descText = desc.trim();
            }
        }

        boolean isThisSleeper = isSleeper || descText.contains("Berth") || descText.equalsIgnoreCase("Sleeper");
        StringBuilder sb = new StringBuilder();
        if (isThisSleeper) {
            sb.append("Sleeper ");
            if (!descText.isEmpty() && !descText.equalsIgnoreCase("Sleeper")) {
                sb.append(descText).append(" ");
            } else {
                sb.append("berth ");
            }
            sb.append(seatNo);
        } else {
            sb.append("Seater ");
            if (!descText.isEmpty() && !descText.equalsIgnoreCase("Seater")) {
                sb.append(descText).append(" seat ");
            } else {
                sb.append("seat ");
            }
            sb.append(seatNo);
        }
        return sb.toString().trim();
    }

    private void speakSelectedSeat(ArrayList<Seat> selectedSeats) {
        if (!isTtsReady || selectedSeats == null || selectedSeats.isEmpty()) {
            return;
        }
        if (!isDivyangUser()) {
            return;
        }
        StringBuilder message = new StringBuilder();
        boolean isSleeper = isSleeperBus();
        boolean isHelper = searchParams != null && "Divyang with Helper".equalsIgnoreCase(searchParams.getDivyangBooking());
        if (isHelper && selectedSeats.size() == 2) {
            message.append("Both ").append(isSleeper ? "berths" : "seats").append(" selected. ")
                    .append(getSeatLabel(selectedSeats.get(0), isSleeper))
                    .append(" for Divyang, and ")
                    .append(getSeatLabel(selectedSeats.get(1), isSleeper))
                    .append(" for Helper. Proceeding to passenger information.");
        } else if (selectedSeats.size() == 1) {
            message.append(getSeatLabel(selectedSeats.get(0), isSleeper))
                    .append(" selected. Proceeding to passenger information.");
        } else {
            message.append("Selected ").append(isSleeper ? "berths" : "seats").append(" are ");

            for (int i = 0; i < selectedSeats.size(); i++) {
                message.append(getSeatLabel(selectedSeats.get(i), isSleeper));

                if (i != selectedSeats.size() - 1) {
                    message.append(", ");
                }
            }
            message.append(". Proceeding to passenger information.");
        }
        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak(message.toString());
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
        }
    }

    private String singleSideBerth(List<SeatRow> Seats,String class_layout_id,ServiceSeatDetails serviceSeatDetails){
        StringBuilder seats= new StringBuilder();

        if (class_layout_id.equalsIgnoreCase("64")||class_layout_id.equalsIgnoreCase("143")||
                class_layout_id.equalsIgnoreCase("188")||class_layout_id.equalsIgnoreCase("191")||
                class_layout_id.equalsIgnoreCase("169")) {
            if (Seats.size() > 0) {
                for (int i =0;i<Seats.size();i++){
                    int seatNo =Seats.get(i).getSeatss()[0].getSeatNo();

                    if(!serviceSeatDetails.getBookedSeats().contains(seatNo)&&
                            serviceSeatDetails.getAvailableSeats().contains(seatNo)){
                        seats.append(seatNo).append(",");
                    }
                }
            }

        }

        return seats.toString();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getNearBySeatAndGender(ArrayList<Seat> selectedSeats, String class_layout_id) {


        ArrayList<int []> berth = new ArrayList<>();
        if(class_layout_id.equalsIgnoreCase("143")){
            for (int i=0;i<seatRows.size();i++){
                List<Seat> seats = Arrays.asList(seatRows.get(i).getSeatss());
                int[] sns =new int[2];
                for (int m=0;m<2;m++){
                    sns[m]=seats.get(m+4).getSeatNo();
                }
                berth.add(sns);
            }
            System.out.println(berth);
            Set<String> seatsAndGenderToAllow = new ArraySet<String>();

            for (int i=0;i<berth.size();i++){

                int []a = berth.get(i);
                for (int num:a){
                    for (int k=0;k<selectedSeats.size();k++){
                        if (selectedSeats.get(k).getSeatNo()==num){
                            String selSeat =selectedSeats.get(k).getSeatNo().toString();
                            System.out.println(selSeat+" ::SELECTED ROW  ::"+Arrays.toString(a));
                            for (int bookedSeat:a){
                                if (bookedSeat!=selectedSeats.get(k).getSeatNo()){
                                    if (mArrLsttripsheetdata.size()>0){
                                        for (int h =0 ;h<mArrLsttripsheetdata.size();h++){
                                            if (bookedSeat==Integer.parseInt(mArrLsttripsheetdata.get(h).getSeatno())){
                                                String seatno = mArrLsttripsheetdata.get(h).getSeatno();
                                                String gender = mArrLsttripsheetdata.get(h).getGender();
                                                if (gender.equalsIgnoreCase("M")){
                                                    Toast.makeText(this, "Sorry You cannot book the seat !!..\n Nearby seat is booked by Male Passenger!", Toast.LENGTH_SHORT).show();
                                                    Intent in = new Intent(this,AdvanceBookingSearchActivity.class);
                                                    in.putExtra("textlang","english");
                                                    startActivity(in);

                                                }
                                            }
                                        }
                                    }
                                    System.out.println(selSeat+" ::NEARBY SEAT  ::"+bookedSeat);
                                    seatsAndGenderToAllow.add(selSeat+" ::NEARBY SEAT  ::"+bookedSeat);
                                }
                            }

                        }
                    }
                }
            }
            System.out.println(seatsAndGenderToAllow);
        }else if (class_layout_id.equalsIgnoreCase("64") || class_layout_id.equalsIgnoreCase("188") ||
                class_layout_id.equalsIgnoreCase("191")|| class_layout_id.equalsIgnoreCase("169"))  {
            for (Seat ss :selectedSeats){
                if(ss.getSeatNo()>3){
                    if ((ss.getSeatNo()%3)==1){
                        System.out.println("SEL SEAT "+ss.getSeatNo()+" ::NEARBY SEAT "+String.valueOf(ss.getSeatNo()+1));

                        for (int h =0 ;h<mArrLsttripsheetdata.size();h++){
                            if ((ss.getSeatNo()+1)==Integer.parseInt(mArrLsttripsheetdata.get(h).getSeatno())){
                                String seatno = mArrLsttripsheetdata.get(h).getSeatno();
                                String gender = mArrLsttripsheetdata.get(h).getGender();
                                if (gender.equalsIgnoreCase("M")){
                                    Toast.makeText(this, "Sorry You cannot book the seat !!..\n Nearby seat is booked by Male Passenger!", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(this,AdvanceBookingSearchActivity.class);
                                    in.putExtra("textlang","english");
                                    startActivity(in);
                                }
                            }
                        }



                    }else if ((ss.getSeatNo()%3)==2){
                        System.out.println("SEL SEAT "+ss.getSeatNo()+" ::NEARBY SEAT "+String.valueOf(ss.getSeatNo()-1));

                        for (int h =0 ;h<mArrLsttripsheetdata.size();h++){
                            if ((ss.getSeatNo()-1)==Integer.parseInt(mArrLsttripsheetdata.get(h).getSeatno())){
                                String seatno = mArrLsttripsheetdata.get(h).getSeatno();
                                String gender = mArrLsttripsheetdata.get(h).getGender();
                                if (gender.equalsIgnoreCase("M")){
                                    Toast.makeText(this, "Sorry You cannot book the seat !!..\n Nearby seat is booked by Male Passenger!", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(this,AdvanceBookingSearchActivity.class);
                                    in.putExtra("textlang","english");
                                    startActivity(in);
                                }
                            }
                        }

                    }
                }

            }
        }

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
                    trySpeakSeats();
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

    public void speakSeatSelectionDescription() {
        if (!isDivyangUser()) return;
        if (busService == null || serviceSeatDetails == null) return;

        String serviceClass = (busService.getServiceClass() != null) ? busService.getServiceClass() : "";
        String tripCodeVal = (busService.getTripCode() != null) ? busService.getTripCode() : "";
        boolean isSleeper = isSleeperBus();
        String seatType = isSleeper ? "Sleeper" : "Seater";
        int availableCount = (serviceSeatDetails.getAvailableSeats() != null)
                ? serviceSeatDetails.getAvailableSeats().size() : 0;

        StringBuilder sb = new StringBuilder();
        sb.append("Seat Selection for ").append(serviceClass);
        if (!serviceClass.toUpperCase().contains(seatType.toUpperCase())) {
            sb.append(" ").append(seatType);
        }
        sb.append(" bus");
        if (!tripCodeVal.isEmpty()) {
            sb.append(", Trip Code ").append(tripCodeVal);
        }
        sb.append(". ");
        if (availableCount > 0) {
            sb.append(availableCount).append(" ").append(isSleeper ? "berths" : "seats").append(" available. ");
        }
        sb.append("Double tap any ").append(isSleeper ? "sleeper berth" : "seater seat").append(" to select.");

        String description = sb.toString();
        if (seatcls != null && seatcls.getParent() instanceof View) {
            View parent = (View) seatcls.getParent();
            parent.setContentDescription(description);
            parent.setFocusable(true);
            parent.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        View categories = findViewById(R.id.seat_categories);
        if (categories != null) {
            categories.setContentDescription(isSleeper ? "Seat layout legend: Sleeper berths are arranged in lower and upper deck."
                    : "Seat layout legend: Available seats are in white, booked seats in grey, and ladies seats in pink.");
            categories.setFocusable(true);
            categories.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.getInstance(this).speak(description);
    }

    private void trySpeakSeats() {
        if (!hasSpokenDescription && serviceSeatDetailsFetched && layoutFetched) {
            hasSpokenDescription = true;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    speakSeatSelectionDescription();
                }
            }, 300);
        }
    }

    private void speakAvailableSeats(Set<Integer> seats) {
        // Disabled: do not speak all available seats in seat selection screen
    }




    public void fetchServiceSeatDetails(BusService busService, SearchParams searchParams) {

        String methodName="";
        if (searchParams.getDivyangBooking().equalsIgnoreCase("Divyang") || searchParams.getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            methodName="GetDivyangServiceSeatDetails";

        }else{
            methodName="GetServiceSeatDetails";
        }

        SoapClientUtil.getServiceSeatDetails(busService, searchParams,methodName, new SoapClientUtil.GetServiceSeatDetailsCallback() {
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
                    trySpeakSeats();

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

                Toast.makeText(SeatSelectionActivity.this, "Cannot Block the seat at the time. Please try later!", Toast.LENGTH_SHORT).show();

                finish();
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

    private void getTripSheetDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.gsrtc.in/GSRTCOPRSETM/webserver/tripSheetData";
        final String[] ResultStatus = new String[1];
        mArrLsttripsheetdata = new ArrayList<>();


        String jdate = convertDate(busService.getJourneyDate());

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("date",jdate);
            postparams.put("conductormobileno", "0");
            postparams.put("vehicleno","0");
            postparams.put("tripcode", busService.getTripCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        try {
                            ResultStatus[0] = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (ResultStatus[0].equalsIgnoreCase("success")) {

                            try {
                                JSONArray subArray = response.getJSONArray("result");
                                for (int i = 0; i < subArray.length(); i++) {

                                    TripSheetData tripSheetData = new TripSheetData();


                                        tripSheetData.setSeatno(subArray.getJSONObject(i).getString("seatno"));
                                        tripSheetData.setPhoneno(subArray.getJSONObject(i).getString("phoneno"));
                                        tripSheetData.setPassengername(subArray.getJSONObject(i).getString("passengername"));
                                        tripSheetData.setFromplaceid(subArray.getJSONObject(i).getString("fromplaceid"));
                                        tripSheetData.setFrompickupid(subArray.getJSONObject(i).getString("frompickupid"));
                                        tripSheetData.setToplaceid(subArray.getJSONObject(i).getString("toplaceid"));
                                        tripSheetData.setLayoutName(subArray.getJSONObject(i).getString("layoutName"));
                                        tripSheetData.setMaxSeats(subArray.getJSONObject(i).getString("maxSeats"));
                                        tripSheetData.setCondutorSeats(subArray.getJSONObject(i).getString("condutorSeats"));
                                        tripSheetData.setBookedSeats(subArray.getJSONObject(i).getString("bookedSeats"));
                                        tripSheetData.setPnrno(subArray.getJSONObject(i).getString("pnrno"));
                                        tripSheetData.setTicket(subArray.getJSONObject(i).getString("ticket"));
                                        tripSheetData.setAge(subArray.getJSONObject(i).getString("age"));
                                        tripSheetData.setAdultorchild(subArray.getJSONObject(i).getString("adultorchild"));
                                        tripSheetData.setGender(subArray.getJSONObject(i).getString("gender"));
                                        tripSheetData.setTotal(subArray.getJSONObject(i).getString("total"));
                                        tripSheetData.setPassengercategoryId(subArray.getJSONObject(i).getString("passengercategoryId"));
                                        tripSheetData.setPickuptime(subArray.getJSONObject(i).getString("pickuptime"));
                                        tripSheetData.setCollectFare(subArray.getJSONObject(i).getString("coll"));
                                        tripSheetData.setIssuCounter(subArray.getJSONObject(i).getString("issuingCounter"));
                                        tripSheetData.setOthers(subArray.getJSONObject(i).getString("others"));

                                    mArrLsttripsheetdata.add(tripSheetData);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String cred = String.format("%s:%s", "oprsEtm", "Oprs@Etm");
                String auth = "Basic " + Base64.encodeToString(cred.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);

                return headers;
            }
        };
// Adding request to request queue
        requestQueue.add(jsonObjReq);
    }

    public String convertDate(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
