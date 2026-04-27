package com.gsrtc.mobileweb.ui.activities.advance_booking;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
    boolean isDivyang;
    EditText passengerNameA, passengerNameB, passengerNameC, passengerNameD, passengerNameE, passengerNameF;
    EditText ageA, ageB, ageC, ageD, ageE, ageF;
    Spinner genderA, genderB, genderC, genderD, genderE, genderF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_passenger_info);

        View rootView = findViewById(android.R.id.content);


        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            int imeHeight = imeInsets.bottom;

            v.setPadding(0, 0, 0, imeHeight);  // Push layout above keyboard
            return insets;
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            onwardBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_ONWARD);
            if (extras.containsKey(AppConstants.INTENT_EXTRA.BOOKING_RETURN)) {
                returnBooking = (Booking) extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_RETURN);
            }
        }

        textlang =onwardBooking.getTextlanguage().trim();
        passengercount = onwardBooking.getSearchParams().getNumOfPassengers();
         isDivyang =
                onwardBooking.getSearchParams() != null &&
                        ("Divyang".equalsIgnoreCase(onwardBooking.getSearchParams().getDivyangBooking())
                                || "Divyang with Helper".equalsIgnoreCase(onwardBooking.getSearchParams().getDivyangBooking()));


        if (passengercount == 1) {
            passengerNameA = (EditText) findViewById(R.id.passenger_name_one);
            passengerNameA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameA);
            //genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);
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
            applyNameValidation(passengerNameA);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameB);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageB);


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
            applyNameValidation(passengerNameA);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameB);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageB);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameC);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageC);

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
            applyNameValidation(passengerNameA);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            applyNameValidation(passengerNameB);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageB);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameC);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageC);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageD);


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
            applyNameValidation(passengerNameA);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);

            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameB);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageB);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameC);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageC);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageD);
            passengerNameE = (EditText) findViewById(R.id.passenger_name_five);
            passengerNameE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameE);
            genderE = (Spinner) findViewById(R.id.gender_five);
            ageE = (EditText) findViewById(R.id.age_five);
            ageE.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageE);

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
            applyNameValidation(passengerNameA);
            genderA = (Spinner) findViewById(R.id.gender_one);
            ageA = (EditText) findViewById(R.id.age_one);
            ageA.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageA);
            passengerNameB = (EditText) findViewById(R.id.passenger_name_two);
            passengerNameB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameB);
            genderB = (Spinner) findViewById(R.id.gender_two);
            ageB = (EditText) findViewById(R.id.age_two);
            ageB.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageB);
            passengerNameC = (EditText) findViewById(R.id.passenger_name_three);
            passengerNameC.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameC);
            genderC = (Spinner) findViewById(R.id.gender_three);
            ageC = (EditText) findViewById(R.id.age_three);
            ageC.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageC);
            passengerNameD = (EditText) findViewById(R.id.passenger_name_four);
            passengerNameD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameD);
            genderD = (Spinner) findViewById(R.id.gender_four);
            ageD = (EditText) findViewById(R.id.age_four);
            ageD.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageD);
            passengerNameE = (EditText) findViewById(R.id.passenger_name_five);
            passengerNameE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameE);
            genderE = (Spinner) findViewById(R.id.gender_five);
            ageE = (EditText) findViewById(R.id.age_five);
            ageE.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageE);
            passengerNameF = (EditText) findViewById(R.id.passenger_name_six);
            passengerNameF.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            applyNameValidation(passengerNameF);
            genderF = (Spinner) findViewById(R.id.gender_six);
            ageF = (EditText) findViewById(R.id.age_six);
            ageF.setInputType(InputType.TYPE_CLASS_NUMBER);
            applyAgeValidation(ageF);

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


                }
            });
        }

        pass_title = (TextView) findViewById(R.id.passinfo_title);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        phone = (EditText) findViewById(R.id.phone);
        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        phone.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(10) });

        email = (EditText) findViewById(R.id.email);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        if(textlang.equalsIgnoreCase("gujarati")){
            pass_title.setText(R.string.pass_pass_details);
            phone.setHint(R.string.pass_phone);
            email.setHint(R.string.pass_email);
            confirm_btn.setText(R.string.pass_cnfm_btn);
        }

        // DIVYANG AUTO FILL SECTION
        // =========================
        if (isDivyang) {

            if (passengerNameA != null &&
                    onwardBooking.getSearchParams().getDivyangName() != null) {

                passengerNameA.setText(
                        onwardBooking.getSearchParams().getDivyangName());
                lockEditText(passengerNameA);
            }

            if (ageA != null &&
                    onwardBooking.getSearchParams().getDivyangDob() != null) {

                int age = calculateAgeFromDob(
                        onwardBooking.getSearchParams().getDivyangDob());
                ageA.setText(String.valueOf(age));
                lockEditText(ageA);
            }

            if (genderA != null &&
                    onwardBooking.getSearchParams().getDivyangGender() != null) {

                if (onwardBooking.getSearchParams()
                        .getDivyangGender().equalsIgnoreCase("Male")) {
                    genderA.setSelection(0);
                } else {
                    genderA.setSelection(1);
                }
                lockSpinner(genderA);
            }

            if (phone != null &&
                    onwardBooking.getSearchParams().getDivyangMobile() != null) {

                phone.setText(onwardBooking.getSearchParams().getDivyangMobile());
                lockEditText(phone);
            }
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
            Log.d("DEBUG_BOOKING", "Entered Age A = " + passenger.getAgeA());
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
            if (!passengerNameA.getText().toString().matches("^[\\p{L} ]{1,50}$")) {
                ActivityUtil.showToast(this, "Invalid passenger name");
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


        Log.d("DEBUG_BOOKING", "----- START -----");

        Log.d("DEBUG_BOOKING", "isDivyang = " + isDivyang);

        Log.d("DEBUG_BOOKING", "Passenger Count = " + passengercount);



        Log.d("DEBUG_BOOKING", "Calculated childCount = " + childCount);
        Log.d("DEBUG_BOOKING", "Calculated adultCount = " + adultCount);

        Log.d("DEBUG_BOOKING", "Backend childrenCount = " + onwardBooking.getSearchParams().getChildrenCount());
        Log.d("DEBUG_BOOKING", "Backend adultCount = " + onwardBooking.getSearchParams().getAdultCount());
        Log.d("DEBUG_BOOKING", "Backend adultFemaleCount = " + onwardBooking.getSearchParams().getAdultFemaleCount());

        Log.d("DEBUG_BOOKING", "Divyang Type = " + onwardBooking.getSearchParams().getDivyangBooking());

        Log.d("DEBUG_BOOKING", "----- END -----");

        if (onwardBooking.getSearchParams().getChildrenCount() != childCount) {
            ActivityUtil.showToast(this, "Child passenger count mismatch");
            return;
        }

        if ((onwardBooking.getSearchParams().getAdultCount()+onwardBooking.getSearchParams().getAdultFemaleCount()) != adultCount) {
            ActivityUtil.showToast(this, "Adult passenger count mismatch");
            return;
        }
        String emailStr = email.getText().toString().trim();
        if (!ValidationUtil.isValidEmail(email.getText())) {
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
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

        String phoneStr = phone.getText().toString().trim();

        if (TextUtils.isEmpty(phoneStr)) {
            ActivityUtil.showToast(this, "Please enter Mobile Number");
            return;
        }

// Must be exactly 10 digits
        if (!phoneStr.matches("^[0-9]{10}$")) {
            ActivityUtil.showToast(this, "Mobile number must be 10 digits");
            return;
        }

// Should not start with 0 (Indian mobile rule)
        if (phoneStr.startsWith("0")) {
            ActivityUtil.showToast(this, "Invalid Mobile Number");
            return;
        }

// Should not be all same digits like 9999999999
        if (phoneStr.matches("(\\d)\\1{9}")) {
            ActivityUtil.showToast(this, "Invalid Mobile Number");
            return;
        }

// Should not be simple sequence like 1234567890
        if (phoneStr.equals("1234567890") || phoneStr.equals("0987654321") || phoneStr.equals("9876543210")) {
            ActivityUtil.showToast(this, "Invalid Mobile Number");
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
    private int calculateAgeFromDob(String dob) {
        try {
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
            java.util.Date birthDate = sdf.parse(dob);

            java.util.Calendar today = java.util.Calendar.getInstance();
            java.util.Calendar birth = java.util.Calendar.getInstance();
            birth.setTime(birthDate);

            int age = today.get(java.util.Calendar.YEAR) - birth.get(java.util.Calendar.YEAR);

            if (today.get(java.util.Calendar.DAY_OF_YEAR)
                    < birth.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }


    private void lockEditText(EditText et) {
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setClickable(false);
        et.setLongClickable(false);
        et.setCursorVisible(false);
        et.setInputType(InputType.TYPE_NULL);
    }

    private void lockSpinner(Spinner sp) {
        sp.setEnabled(false);
    }

    private void applyNameValidation(EditText editText) {

        InputFilter nameFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    char ch = source.charAt(i);

                    if (!Character.isLetter(ch) && !Character.isSpaceChar(ch)) {
                        return "";
                    }
                }
                return null;
            }
        };

        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(50),
                nameFilter
        });
    }
    private void applyAgeValidation(EditText editText) {

        InputFilter rangeFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                try {
                    String newValue = dest.subSequence(0, dstart)
                            + source.toString()
                            + dest.subSequence(dend, dest.length());

                    if (newValue.isEmpty())
                        return null;

                    int input = Integer.parseInt(newValue);

                    if (input >= 1 && input <= 120)
                        return null; // Accept
                } catch (NumberFormatException ignored) {}

                return ""; // Reject if <1 or >120
            }
        };

        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(3),
                rangeFilter
        });
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
