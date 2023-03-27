package com.gsrtc.mobileweb.ui.activities.advance_booking;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.Passenger;
import com.gsrtc.mobileweb.models.PassengerInfo;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;



public class PassengerInfoActivity extends BaseNavigationDrawerActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_passenger_info);

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
                    passengerNameB.setText(passengerNameA.getText());

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
                    passengerNameB.setText(passengerNameA.getText());
                    passengerNameC.setText(passengerNameA.getText());

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
                    passengerNameB.setText(passengerNameA.getText());
                    passengerNameC.setText(passengerNameA.getText());
                    passengerNameD.setText(passengerNameA.getText());
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
                    passengerNameB.setText(passengerNameA.getText());
                    passengerNameC.setText(passengerNameA.getText());
                    passengerNameD.setText(passengerNameA.getText());
                    passengerNameE.setText(passengerNameA.getText());

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
    }

    public void setupViews() {

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
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

//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
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
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

            if (!ValidationUtil.isValidNumber(String.valueOf(ageA.getText())) || ((double) Double.parseDouble(String.valueOf(ageA.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }
            if (!ValidationUtil.isValidNumber(String.valueOf(ageB.getText())) || ((double) Double.parseDouble(String.valueOf(ageB.getText()))) <= 0d) {
                ActivityUtil.showToast(this, "Please enter valid age");
                return;
            }

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 11d) {
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
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

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

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 11d) {
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

//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

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

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 11d) {
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

//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageE.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageE.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

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

            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) > 11d) {
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

//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageA.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageB.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageC.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageD.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//            if (((double) Double.parseDouble(String.valueOf(ageE.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageE.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageF.getText()))) < 10d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }
//
//            if (((double) Double.parseDouble(String.valueOf(ageF.getText()))) > 59d) {
//                ActivityUtil.showToast(this, "Not a valid age");
//                return;
//            }

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
            if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeA()) || ((double) Double.parseDouble(passenger.getAgeA())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeB()) || ((double) Double.parseDouble(passenger.getAgeB())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeC()) || ((double) Double.parseDouble(passenger.getAgeC())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeD()) || ((double) Double.parseDouble(passenger.getAgeD())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeE()) || ((double) Double.parseDouble(passenger.getAgeE())) > 11d) {
                adultCount += 1;
            }
            if (!ValidationUtil.isValidNumber(passenger.getAgeF()) || ((double) Double.parseDouble(passenger.getAgeF())) <= 11d) {
                childCount += 1;
            } else if (!ValidationUtil.isValidNumber(passenger.getAgeF()) || ((double) Double.parseDouble(passenger.getAgeF())) > 11d) {
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
        goNext();
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

        ActivityUtil.openNewActivity(PassengerInfoActivity.this, FareDetailsActivity.class, extras);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PassengerInfoActivity.this,R.style.CustomDialogTheme);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ActivityUtil.openNewActivityAsOnlyActivity(PassengerInfoActivity.this, HomeActivity.class, null);
                Intent i = new Intent(PassengerInfoActivity.this, HomeActivity.class);
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
