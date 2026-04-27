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

    TextToSpeech tts;
    boolean isTtsReady = false;
    boolean hasSpoken = false;


    ProgressDialog seatBlockingProgressDialog;

    boolean layoutFetched = false, serviceSeatDetailsFetched = false;

    private ArrayList<TripSheetData> mArrLsttripsheetdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_seat_selection);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
                isTtsReady = true;

                trySpeakSeats();
            }
        });

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

    private boolean isDivyangUser() {
        if (searchParams == null) return false;

        String type = searchParams.getDivyangBooking();

        if (type == null) return false;

        return type.equalsIgnoreCase("Divyang") ||
                type.equalsIgnoreCase("Divyang with Helper");
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
                    public void onSeatSelected(int seatNo) {
                        if (isTtsReady && isDivyangUser()) {
                            tts.stop();
                            tts.speak("Seat " + seatNo + " selected", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }

                    @Override
                    public void onSeatAlreadyBooked(int seatNo) {
                        if (isTtsReady && isDivyangUser()) {
                            tts.stop();
                            tts.speak("Seat " + seatNo + " already booked", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        if (isDivyangUser()) {
                            vibrate();
                        }
                    }

                    @Override
                    public void onSeatUnavailable(int seatNo) {
                        if (isTtsReady && isDivyangUser()) {
                            tts.stop();
                            tts.speak("Seat " + seatNo + " not available", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
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

    private void speakSelectedSeat(ArrayList<Seat> selectedSeats) {
        if (!isTtsReady || selectedSeats == null || selectedSeats.isEmpty()) {
            return;
        }
        if (!isDivyangUser()) {
            return;
        }
        StringBuilder message = new StringBuilder();
        if (selectedSeats.size() == 1) {
            message.append("Seat ")
                    .append(selectedSeats.get(0).getSeatNo())
                    .append(" selected");
        } else {
            message.append("Selected seats are ");

            for (int i = 0; i < selectedSeats.size(); i++) {
                message.append(selectedSeats.get(i).getSeatNo());

                if (i != selectedSeats.size() - 1) {
                    message.append(", ");
                }
            }
        }
        tts.stop();
        tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
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

    private void trySpeakSeats() {
        Log.d("TTS_DEBUG", "trySpeakSeats called");
        if (!isTtsReady) {
            Log.d("TTS_DEBUG", "TTS not ready");
            return;
        }
        if (!serviceSeatDetailsFetched || !layoutFetched) {
            Log.d("TTS_DEBUG", "Data not ready yet");
            return;
        }
        if (hasSpoken) {
            Log.d("TTS_DEBUG", "Already spoken once");
            return;
        }
        if (searchParams == null) {
            Log.d("TTS_DEBUG", "searchParams is null");
            return;
        }
        String type = searchParams.getDivyangBooking();
        Log.d("TTS_DEBUG", "DivyangBooking value = " + type);
        if (type != null &&
                (type.equalsIgnoreCase("Divyang") ||
                        type.equalsIgnoreCase("Divyang with Helper"))) {
            Log.d("TTS_DEBUG", "User is Divyang → speaking seats");
            tts.stop();
            speakAvailableSeats(serviceSeatDetails.getAvailableSeats());
            hasSpoken = true;

        } else {
            Log.d("TTS_DEBUG", "User is NOT Divyang → skipping voice");
        }
    }

    private void speakAvailableSeats(Set<Integer> seats) {

        Log.d("TTS_DEBUG", "speakAvailableSeats called");
        if (seats == null || seats.isEmpty()) {
            Log.d("TTS_DEBUG", "Seats are empty or null");
            return;
        }
        if (!isTtsReady) {
            Log.d("TTS_DEBUG", "TTS not ready");
            return;
        }
        if (!isDivyangUser()) {
            Log.d("TTS_DEBUG", "Not a Divyang user → skipping available seats voice");
            return;
        }
        List<Integer> sortedSeats = new ArrayList<>(seats);
        Collections.sort(sortedSeats);
        Log.d("TTS_DEBUG", "Available seats count = " + sortedSeats.size());
        Log.d("TTS_DEBUG", "Available seats list = " + sortedSeats.toString());
        String message = formatSeats(sortedSeats);
        Log.d("TTS_DEBUG", "Speaking message = " + message);
        tts.stop(); // important to avoid overlap
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private String formatSeats(List<Integer> seats) {
        StringBuilder result = new StringBuilder("Available seats are ");

        for (int i = 0; i < seats.size(); i++) {
            result.append(seats.get(i));

            if (i != seats.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString();
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
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}
