package com.gsrtc.mobileweb.ui.activities.packageBooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.Passenger;
import com.gsrtc.mobileweb.models.PassengerInfo;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.packageBooking.Model.GetAllHotelInfoByPackageID;
import com.gsrtc.mobileweb.ui.activities.packageBooking.Model.GetPackageTripsList;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.ValidationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PackagePassengerInfo extends BaseNavigationDrawerActivity {

    Booking onwardBooking;
    Booking returnBooking;
    Booking divyangBooking;
    int passengercount,singleLady;
    String textlang;

    Button confirm_btn;
    TextView pass_title;
    EditText phone, email;
    EditText passengerNameA, passengerNameB, passengerNameC, passengerNameD, passengerNameE, passengerNameF;
    EditText ageA, ageB, ageC, ageD, ageE, ageF;
    Spinner genderA, genderB, genderC, genderD, genderE, genderF;


    TextView checkInDate, checkOutDate;
    TextView textRooms, textAdults, textChildren;
    EditText addressField, idProofNumber;
    Spinner idProofType;

    List<GetAllHotelInfoByPackageID> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_passenger_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);
            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
                returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
            }
        }

        textlang =onwardBooking.getTextlanguage().trim();
        passengercount = onwardBooking.getSearchParams().getNumOfPassengers();

        if (passengercount == 1) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
            }

            passengerNameA.setVisibility(View.VISIBLE);
            //genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            singleLady=onwardBooking.getSearchParams().getAdultFemaleCount();
            if(singleLady ==1){
                genderA = (Spinner) findViewById(R.id.gender_lady_one);
                genderA.setVisibility(View.VISIBLE);
            }else{
                genderA = (Spinner) findViewById(R.id.gender_one);
                genderA.setVisibility(View.VISIBLE);
            }

        }

        if (passengercount == 2) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
                passengerNameB.setHint(R.string.pass_pass_name);
                ageB.setHint(R.string.pass_age);
            }

            passengerNameA.setVisibility(View.VISIBLE);
            genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            passengerNameB.setVisibility(View.VISIBLE);
            genderB.setVisibility(View.VISIBLE);
            ageB.setVisibility(View.VISIBLE);

            passengerNameA.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {


                }
            });
        }

        if (passengercount == 3) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
                passengerNameB.setHint(R.string.pass_pass_name);
                ageB.setHint(R.string.pass_age);
                passengerNameC.setHint(R.string.pass_pass_name);
                ageC.setHint(R.string.pass_age);
            }


            passengerNameA.setVisibility(View.VISIBLE);
            genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            passengerNameB.setVisibility(View.VISIBLE);
            genderB.setVisibility(View.VISIBLE);
            ageB.setVisibility(View.VISIBLE);

            passengerNameC.setVisibility(View.VISIBLE);
            genderC.setVisibility(View.VISIBLE);
            ageC.setVisibility(View.VISIBLE);

            passengerNameA.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {


                }
            });
        }

        if (passengercount == 4) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
                passengerNameB.setHint(R.string.pass_pass_name);
                ageB.setHint(R.string.pass_age);
                passengerNameC.setHint(R.string.pass_pass_name);
                ageC.setHint(R.string.pass_age);
                passengerNameD.setHint(R.string.pass_pass_name);
                ageD.setHint(R.string.pass_age);
            }


            passengerNameA.setVisibility(View.VISIBLE);
            genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            passengerNameB.setVisibility(View.VISIBLE);
            genderB.setVisibility(View.VISIBLE);
            ageB.setVisibility(View.VISIBLE);

            passengerNameC.setVisibility(View.VISIBLE);
            genderC.setVisibility(View.VISIBLE);
            ageC.setVisibility(View.VISIBLE);

            passengerNameD.setVisibility(View.VISIBLE);
            genderD.setVisibility(View.VISIBLE);
            ageD.setVisibility(View.VISIBLE);

            passengerNameA.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        if (passengercount == 5) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameE = (EditText) findViewById(R.id.passenger_name_five);
            passengerNameE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderE = (Spinner) findViewById(R.id.gender_five);
            ageE = (EditText) findViewById(R.id.age_five);
            ageE.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
                passengerNameB.setHint(R.string.pass_pass_name);
                ageB.setHint(R.string.pass_age);
                passengerNameC.setHint(R.string.pass_pass_name);
                ageC.setHint(R.string.pass_age);
                passengerNameD.setHint(R.string.pass_pass_name);
                ageD.setHint(R.string.pass_age);
                passengerNameE.setHint(R.string.pass_pass_name);
                ageE.setHint(R.string.pass_age);
            }

            passengerNameA.setVisibility(View.VISIBLE);
            genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            passengerNameB.setVisibility(View.VISIBLE);
            genderB.setVisibility(View.VISIBLE);
            ageB.setVisibility(View.VISIBLE);

            passengerNameC.setVisibility(View.VISIBLE);
            genderC.setVisibility(View.VISIBLE);
            ageC.setVisibility(View.VISIBLE);

            passengerNameD.setVisibility(View.VISIBLE);
            genderD.setVisibility(View.VISIBLE);
            ageD.setVisibility(View.VISIBLE);

            passengerNameE.setVisibility(View.VISIBLE);
            genderE.setVisibility(View.VISIBLE);
            ageE.setVisibility(View.VISIBLE);

            passengerNameA.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {


                }
            });
        }

        if (passengercount == 6) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameE = (EditText) findViewById(R.id.passenger_name_five);
            passengerNameE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderE = (Spinner) findViewById(R.id.gender_five);
            ageE = (EditText) findViewById(R.id.age_five);
            ageE.setInputType(InputType.TYPE_CLASS_NUMBER);
            passengerNameF = (EditText) findViewById(R.id.passenger_name_six);
            passengerNameF.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            genderF = (Spinner) findViewById(R.id.gender_six);
            ageF = (EditText) findViewById(R.id.age_six);
            ageF.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(textlang.equalsIgnoreCase("gujarati")) {
                passengerNameA.setHint(R.string.pass_pass_name);
                ageA.setHint(R.string.pass_age);
                passengerNameB.setHint(R.string.pass_pass_name);
                ageB.setHint(R.string.pass_age);
                passengerNameC.setHint(R.string.pass_pass_name);
                ageC.setHint(R.string.pass_age);
                passengerNameD.setHint(R.string.pass_pass_name);
                ageD.setHint(R.string.pass_age);
                passengerNameE.setHint(R.string.pass_pass_name);
                ageE.setHint(R.string.pass_age);
                passengerNameF.setHint(R.string.pass_pass_name);
                ageF.setHint(R.string.pass_age);
            }

            passengerNameA.setVisibility(View.VISIBLE);
            genderA.setVisibility(View.VISIBLE);
            ageA.setVisibility(View.VISIBLE);

            passengerNameB.setVisibility(View.VISIBLE);
            genderB.setVisibility(View.VISIBLE);
            ageB.setVisibility(View.VISIBLE);

            passengerNameC.setVisibility(View.VISIBLE);
            genderC.setVisibility(View.VISIBLE);
            ageC.setVisibility(View.VISIBLE);

            passengerNameD.setVisibility(View.VISIBLE);
            genderD.setVisibility(View.VISIBLE);
            ageD.setVisibility(View.VISIBLE);

            passengerNameE.setVisibility(View.VISIBLE);
            genderE.setVisibility(View.VISIBLE);
            ageE.setVisibility(View.VISIBLE);

            passengerNameF.setVisibility(View.VISIBLE);
            genderF.setVisibility(View.VISIBLE);
            ageF.setVisibility(View.VISIBLE);

            passengerNameA.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    passengerNameB.setText(passengerNameA.getText());
                    passengerNameC.setText(passengerNameA.getText());
                    passengerNameD.setText(passengerNameA.getText());
                    passengerNameE.setText(passengerNameA.getText());
                    passengerNameF.setText(passengerNameA.getText());

                }
            });
        }

        pass_title = (TextView) findViewById(R.id.passinfo_title);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);

        if(textlang.equalsIgnoreCase("gujarati")){
            pass_title.setText(R.string.pass_pass_details);
            phone.setHint(R.string.pass_phone);
            email.setHint(R.string.pass_email);
            confirm_btn.setText(R.string.pass_cnfm_btn);
        }

        setupViews();

        fetHotelDetails();

    }

    private void fetHotelDetails() {
        SoapClientUtil.getHotelDetailsList(onwardBooking.getSearchParams().getPackageDetails().getTripSelected().getPackageId()+"","0",onwardBooking.getJourneyDate() ,new SoapClientUtil.GetHotelListCallBack() {
            @Override
            public void onSuccess(List<GetAllHotelInfoByPackageID> serviceList) {
                hotelList = new ArrayList<>();
                hotelList.addAll(serviceList);
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }

    public void setupViews() {

        // Dates
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);

        // Rooms and People
        textRooms = findViewById(R.id.textRooms);
        textAdults = findViewById(R.id.textAdults);
        textChildren = findViewById(R.id.textChildren);

        // Address
        addressField = findViewById(R.id.addressField);

        // ID Proof
        idProofType = findViewById(R.id.idProofType);
        idProofNumber = findViewById(R.id.idProofNumber);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onSubmit();
            }
        });
    }

    // Inside your Activity or Fragment
    private void showHotelInfoDialog() {
        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_hotel_info, null);

        // Get references to views
        TextView checkInDate = dialogView.findViewById(R.id.checkInDate);
        TextView checkOutDate = dialogView.findViewById(R.id.checkOutDate);
        TextView numRooms = dialogView.findViewById(R.id.textRooms);
        TextView adultsPerRoom = dialogView.findViewById(R.id.textAdults);
        TextView childrenCount = dialogView.findViewById(R.id.textChildren);
        EditText addressField = dialogView.findViewById(R.id.addressField);
        Spinner idProofType = dialogView.findViewById(R.id.idProofType);
        EditText idProofNumber = dialogView.findViewById(R.id.idProofNumber);

        checkInDate.setText(onwardBooking.getJourneyDate());
        checkOutDate.setText(getNextDate(onwardBooking.getJourneyDate()));

        int adultCout = onwardBooking.getSearchParams().getAdultCount() + onwardBooking.getSearchParams().getAdultFemaleCount();
        int childCount = onwardBooking.getSearchParams().getChildrenCount();
        adultsPerRoom.setText(adultCout+"");
        childrenCount.setText(childCount+"");

        String roomsCount = calculateRooms(adultCout) + "";
        numRooms.setText(roomsCount);


        // Set hotel info (mock values or real from intent, server, etc.)

        GetAllHotelInfoByPackageID hotel = hotelList.get(0);
        onwardBooking.getSearchParams().getPackageDetails().setHotelInfo(hotel);

        ((TextView) dialogView.findViewById(R.id.hotelName)).setText("Hotel Name: " + hotel.getHotelName());
        ((TextView) dialogView.findViewById(R.id.hotelAddress)).setText("Address: " + hotel.getHotelAddress());
        ((TextView) dialogView.findViewById(R.id.hotelContact)).setText("Contact: " + hotel.getContactNo());
        ((TextView) dialogView.findViewById(R.id.checkInTime)).setText("Check-In Time: " + hotel.getCheckInTime());
        ((TextView) dialogView.findViewById(R.id.checkOutTime)).setText("Check-Out Time: " +hotel.getCheckOutTime());
        ((TextView) dialogView.findViewById(R.id.chargePerRoom)).setText("Charge Per Room: ₹" + hotel.getChargePerRoom());
        ((TextView) dialogView.findViewById(R.id.extraBed)).setText("Extra Bed: " + (hotel.getHmdExtraBedYesNo().equalsIgnoreCase("Y") ? "Yes" : "No"));
        ((TextView) dialogView.findViewById(R.id.extraBedCharge)).setText("Extra Bed Charge: ₹" + hotel.getExtraBedCharge());
        ((TextView) dialogView.findViewById(R.id.gst)).setText("GST: " + hotel.getGstPer() + "%");
        ((TextView) dialogView.findViewById(R.id.roomsCount)).setText("Rooms Count: " + hotel.getAvailableRoomsCount());







        ArrayAdapter<CharSequence> idProofAdapter = ArrayAdapter.createFromResource(
                this, R.array.documents, android.R.layout.simple_spinner_item);
        idProofAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idProofType.setAdapter(idProofAdapter);

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Hotel Booking Information");
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            // Get user input here
            String checkIn = checkInDate.getText().toString();
            String checkOut = checkOutDate.getText().toString();
            String rooms = numRooms.getText().toString();
            String adults = adultsPerRoom.getText().toString();
            String children = childrenCount.getText().toString();
            String address = addressField.getText().toString();
            String proofType = idProofType.getSelectedItem().toString();
            String proofNumber = idProofNumber.getText().toString();

            onwardBooking.getSearchParams().getPackageDetails().getHotelInfo().setRoomsAllocated(roomsCount);
            onwardBooking.getSearchParams().getPackageDetails().getHotelInfo().setCheckInDate(checkIn);
            onwardBooking.getSearchParams().getPackageDetails().getHotelInfo().setCheckOutDate(checkOut);


            // Do something with the data
            goNext();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    public static int calculateRooms(int numberOfPeople) {
        // Each room can accommodate up to 2 people
        if (numberOfPeople <= 0) {
            return 0; // or throw exception based on your requirement
        }
        return (int) Math.ceil(numberOfPeople / 2.0);
    }

    public static String getNextDate(String inputDate) {
        try {
            // Define the date format
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Parse the input date
            Date date = sdf.parse(inputDate);

            // Add one day
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            // Format the new date
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or handle the error appropriately
        }
    }






    public void onSubmit() {
        passengercount = onwardBooking.getSearchParams().getNumOfPassengers();
        List<Passenger> passengers = new ArrayList<>();
        // Passenger passenger = new Passenger();
        int adultCount = 0;
        int childCount = 0;


        if (passengercount == 1) {
            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            singleLady=onwardBooking.getSearchParams().getAdultFemaleCount();
            if(singleLady ==1){

                if (genderA.getSelectedItemPosition() == 0) {
                    passenger.setGenderA("F");
                } else {
                    passenger.setGenderA("M");
                }

            }else {
                if (genderA.getSelectedItemPosition() == 0) {
                    passenger.setGenderA("M");
                } else {
                    passenger.setGenderA("F");
                }
            }

            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
        }


        if (passengercount == 2) {
            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            if (genderA.getSelectedItemPosition() == 0) {
                passenger.setGenderA("M");
            } else {
                passenger.setGenderA("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameB(passengerNameB.getText().toString());
            passenger.setAgeB(ageB.getText().toString());
            if (genderB.getSelectedItemPosition() == 0) {
                passenger.setGenderB("M");
            } else {
                passenger.setGenderB("F");
            }
            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(passengerNameB.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }


            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 5d) {
                adultCount += 1;
            }
        }


        if (passengercount == 3) {
            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            if (genderA.getSelectedItemPosition() == 0) {
                passenger.setGenderA("M");
            } else {
                passenger.setGenderA("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameB(passengerNameB.getText().toString());
            passenger.setAgeB(ageB.getText().toString());
            if (genderB.getSelectedItemPosition() == 0) {
                passenger.setGenderB("M");
            } else {
                passenger.setGenderB("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameC(passengerNameC.getText().toString());
            passenger.setAgeC(ageC.getText().toString());
            if (genderC.getSelectedItemPosition() == 0) {
                passenger.setGenderC("M");
            } else {
                passenger.setGenderC("F");
            }
            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(passengerNameB.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameC.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageC.getText())) || ((double) Double.parseDouble(String.valueOf(ageC.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 5d) {
                adultCount += 1;
            }

        }

        if (passengercount == 4) {
            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            if (genderA.getSelectedItemPosition() == 0) {
                passenger.setGenderA("M");
            } else {
                passenger.setGenderA("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameB(passengerNameB.getText().toString());
            passenger.setAgeB(ageB.getText().toString());
            if (genderB.getSelectedItemPosition() == 0) {
                passenger.setGenderB("M");
            } else {
                passenger.setGenderB("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameC(passengerNameC.getText().toString());
            passenger.setAgeC(ageC.getText().toString());
            if (genderC.getSelectedItemPosition() == 0) {
                passenger.setGenderC("M");
            } else {
                passenger.setGenderC("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameD(passengerNameD.getText().toString());
            passenger.setAgeD(ageD.getText().toString());
            if (genderD.getSelectedItemPosition() == 0) {
                passenger.setGenderD("M");
            } else {
                passenger.setGenderD("F");
            }
            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(passengerNameB.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameC.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameD.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageC.getText())) || ((double) Double.parseDouble(String.valueOf(ageC.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageD.getText())) || ((double) Double.parseDouble(String.valueOf(ageD.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 5d) {
                adultCount += 1;
            }
        }

        if (passengercount == 5) {
            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            if (genderA.getSelectedItemPosition() == 0) {
                passenger.setGenderA("M");
            } else {
                passenger.setGenderA("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameB(passengerNameB.getText().toString());
            passenger.setAgeB(ageB.getText().toString());
            if (genderB.getSelectedItemPosition() == 0) {
                passenger.setGenderB("M");
            } else {
                passenger.setGenderB("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameC(passengerNameC.getText().toString());
            passenger.setAgeC(ageC.getText().toString());
            if (genderC.getSelectedItemPosition() == 0) {
                passenger.setGenderC("M");
            } else {
                passenger.setGenderC("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameD(passengerNameD.getText().toString());
            passenger.setAgeD(ageD.getText().toString());
            if (genderD.getSelectedItemPosition() == 0) {
                passenger.setGenderD("M");
            } else {
                passenger.setGenderD("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameE(passengerNameE.getText().toString());
            passenger.setAgeE(ageE.getText().toString());
            if (genderE.getSelectedItemPosition() == 0) {
                passenger.setGenderE("M");
            } else {
                passenger.setGenderE("F");
            }
            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(passengerNameB.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameC.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameD.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameE.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageC.getText())) || ((double) Double.parseDouble(String.valueOf(ageC.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageD.getText())) || ((double) Double.parseDouble(String.valueOf(ageD.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageE.getText())) || ((double) Double.parseDouble(String.valueOf(ageE.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) > 5d) {
                adultCount += 1;
            }
        }

        if (passengercount == 6) {

            Passenger passenger = new Passenger();
            passenger.setPassengerNameA(passengerNameA.getText().toString());
            passenger.setAgeA(ageA.getText().toString());
            if (genderA.getSelectedItemPosition() == 0) {
                passenger.setGenderA("M");
            } else {
                passenger.setGenderA("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameB(passengerNameB.getText().toString());
            passenger.setAgeB(ageB.getText().toString());
            if (genderB.getSelectedItemPosition() == 0) {
                passenger.setGenderB("M");
            } else {
                passenger.setGenderB("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameC(passengerNameC.getText().toString());
            passenger.setAgeC(ageC.getText().toString());
            if (genderC.getSelectedItemPosition() == 0) {
                passenger.setGenderC("M");
            } else {
                passenger.setGenderC("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameD(passengerNameD.getText().toString());
            passenger.setAgeD(ageD.getText().toString());
            if (genderD.getSelectedItemPosition() == 0) {
                passenger.setGenderD("M");
            } else {
                passenger.setGenderD("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameE(passengerNameE.getText().toString());
            passenger.setAgeE(ageE.getText().toString());
            if (genderE.getSelectedItemPosition() == 0) {
                passenger.setGenderE("M");
            } else {
                passenger.setGenderE("F");
            }
            passengers.add(passenger);

            passenger.setPassengerNameF(passengerNameF.getText().toString());
            passenger.setAgeF(ageF.getText().toString());
            if (genderF.getSelectedItemPosition() == 0) {
                passenger.setGenderF("M");
            } else {
                passenger.setGenderF("F");
            }
            passengers.add(passenger);

            if (TextUtils.isEmpty(passengerNameA.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(passengerNameB.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameC.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameD.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameE.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(passengerNameF.getText())) {
                ActivityUtil.showToast(this, "Passenger name cannot be empty");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageC.getText())) || ((double) Double.parseDouble(String.valueOf(ageC.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageD.getText())) || ((double) Double.parseDouble(String.valueOf(ageD.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageE.getText())) || ((double) Double.parseDouble(String.valueOf(ageE.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageF.getText())) || ((double) Double.parseDouble(String.valueOf(ageF.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) > 5d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeF()) || ((double) Double.parseDouble(passenger.getAgeF())) <= 5d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeF()) || ((double) Double.parseDouble(passenger.getAgeF())) > 5d) {
                adultCount += 1;
            }

        }

        if (onwardBooking.getSearchParams().getChildrenCount() != childCount) {
            ActivityUtil.showToast(this, "Child passenger count mismatch");
            return;
        }

        if ((onwardBooking.getSearchParams().getAdultCount()+onwardBooking.getSearchParams().getAdultFemaleCount()) != adultCount) {
            ActivityUtil.showToast(this, "Adult passenger count mismatch");
            return;
        }

        if (!ValidationUtil.isValidEmail(email.getText())) {
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }

        if (!ValidationUtil.isValidPhone(phone.getText())) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        if (phone.getText().length()<10) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        phone.getText().toString().replaceAll("\\s", "");
        if(phone.getText().toString().trim().length()==13)
        {
            String mobnum=phone.getText().toString();

            if (mobnum.startsWith("+"))
            {
                if (mobnum.length() == 13)
                {
                    String str_getMOBILE = mobnum.substring(3);
                    phone.setText(str_getMOBILE);

                }
            }
            else
            {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }
        else if(phone.getText().toString().trim().length()>10)
        {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        else if(phone.getText().toString().trim().length()==10) {

            String mobnum = phone.getText().toString();

            if (mobnum.startsWith("+")) {
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }

        onwardBooking.setPassengers(passengers);
        PassengerInfo onwardPassengerInfo = (onwardBooking.getPassengerInfo() == null) ? new PassengerInfo() : onwardBooking.getPassengerInfo();
        onwardPassengerInfo.setEmail(email.getText().toString());
        onwardPassengerInfo.setPhone(phone.getText().toString());
        onwardBooking.setPassengerInfo(onwardPassengerInfo);

        if (returnBooking != null) {
            returnBooking.setPassengers(passengers);
            PassengerInfo returnPassengerInfo = (returnBooking.getPassengerInfo() == null) ? new PassengerInfo() : returnBooking.getPassengerInfo();
            returnPassengerInfo.setEmail(email.getText().toString());
            returnPassengerInfo.setPhone(phone.getText().toString());
            returnBooking.setPassengerInfo(returnPassengerInfo);
        }


        if (onwardBooking.getSearchParams().getPackageDetails().getTripSelected().getPackageId()==2){
            showHotelInfoDialog();
        }else {
            goNext();
        }


    }

    public void goNext() {
        Bundle extras = new Bundle();
        extras.putString("textlang", textlang);
        extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD, onwardBooking);
        if (returnBooking != null) {
            extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN, returnBooking);
        }
        if (onwardBooking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            extras.putSerializable(AppConstants.INTENT_EXTRA.BOOKING_DIVYANG, onwardBooking);
        }

        ActivityUtil.openNewActivity(PackagePassengerInfo.this, PackageFareDetails.class, extras);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PackagePassengerInfo.this,R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ActivityUtil.openNewActivityAsOnlyActivity(PackagePassengerInfo.this, HomeActivity.class, null);
                Intent i = new Intent(PackagePassengerInfo.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });

        alertDialog.setNegativeButton("NO", null);
        alertDialog.setMessage("Going back will discard your booking.Do you want to continue?");
        alertDialog.setTitle("GSRTC");
        alertDialog.show();
    }
}
