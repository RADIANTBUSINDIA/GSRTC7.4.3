package com.gsrtc.mobileweb.ui.activities.packageBooking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.ui.activities.advance_booking.AdvanceBookingSearchActivity;
import com.gsrtc.mobileweb.ui.activities.advance_booking.AdvanceBookingSearchResultActivity;
import com.gsrtc.mobileweb.ui.activities.packageBooking.Model.GetPackageTripsList;
import com.gsrtc.mobileweb.ui.activities.packageBooking.Model.PackageBookingModel;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PackageBookingHome extends AppCompatActivity {

    public static int REQUEST_NO_SERVICE_AVAILABLE = 1;
    CalendarDatePickerDialogFragment calendarDialog;
    SearchParams searchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_booking_home);
        searchParams = new SearchParams();


        findViewById(R.id.backButton).setOnClickListener(v -> {
            finish();
        });


        LinearLayout btnBooking = findViewById(R.id.bookTicket);
        LinearLayout btnCancellation = findViewById(R.id.cancelTicket);

        RecyclerView packageResult = findViewById(R.id.packageResult);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPackageTripList();
                packageResult.setVisibility(View.VISIBLE); // Show the RecyclerView
            }
        });

        btnCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackageBookingHome.this, CancellationWebViewActivity.class);
                startActivity(intent);
            }
        });


    }

    private void fetchPackageTripList() {
        SoapClientUtil.getPackageTripDetails(new SoapClientUtil.GetPackageTripCallBack() {
            @Override
            public void onSuccess(List<GetPackageTripsList> serviceList) {
                RecyclerView recyclerView = findViewById(R.id.packageResult);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                PackageTripAdapter adapter = new PackageTripAdapter(getApplicationContext(), serviceList);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }


    public class PackageTripAdapter extends RecyclerView.Adapter<PackageTripAdapter.PackageTripViewHolder> {

        private Context context;
        private List<GetPackageTripsList> tripList;


        public PackageTripAdapter(Context context, List<GetPackageTripsList> tripList) {
            this.context = context;
            this.tripList = tripList;
        }

        @NonNull
        @Override
        public PackageTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_package_trip, parent, false);
            return new PackageTripViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PackageTripViewHolder holder, int position) {
            GetPackageTripsList trip = tripList.get(position);

            // Show layout only for packageId = 2
            if (trip.getPackageId() == 2) {
                holder.packageDetailsLayout.setVisibility(View.VISIBLE);
            } else {
                holder.packageDetailsLayout.setVisibility(View.GONE);
            }

            holder.packageName.setText(trip.getPackageName());
            holder.fromTo.setText("From: " + trip.getFromPlaceName() + "    →    To: " + trip.getToPlaceName());

            // Setup Spinner
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.num_of_pss, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.passengerCountSpinner.setAdapter(adapter);

            holder.datePickerEditText.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PackageBookingHome.this, // This is safe here since it's triggered from a View
                        (view, year, month, dayOfMonth) -> {
                            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                            holder.datePickerEditText.setText(selectedDate);
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                // Disallow past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            });

            // Book button click
            holder.bookButton.setOnClickListener(v -> {
                String selectedDate = holder.datePickerEditText.getText().toString().trim();
                int passengerCount = Integer.parseInt(holder.passengerCountSpinner.getSelectedItem().toString());
                if (selectedDate.isEmpty()) {
                    Toast.makeText(context, "Please select a travel date.", Toast.LENGTH_SHORT).show();
                } else {
                    // navigate

                    if (trip.getPackageId() != 2) {
                        searchParams.setAdultCount(passengerCount);
                        searchParams.setAdultFemaleCount(0);
                        searchParams.setChildrenCount(0);
                        searchParams.setDivyangBooking("General");
                        searchParams.setNumOfPassengers(passengerCount);
                        searchParams.setOnWardDate(selectedDate);
                        searchParams.setTextlanguage("english");
                        searchParams.setSingleLady(0);
                        Place fromPlace = new Place(trip.getFromPlaceCode(), String.valueOf(trip.getFromPlaceId()), trip.getFromPlaceName(), "", "");
                        Place toPlace = new Place(trip.getToPlaceCode(), String.valueOf(trip.getToPlaceId()), trip.getToPlaceName(), "", "");


                        PackageBookingModel packageBookingModel = new PackageBookingModel();
                        packageBookingModel.setTripSelected(trip);
                        packageBookingModel.setDate(selectedDate);
                        packageBookingModel.setHotelType("");
                        packageBookingModel.setNoOfPassengers(passengerCount + "");
                        searchParams.setPackageDetails(packageBookingModel);

                        searchParams.setFromLocation(fromPlace);
                        searchParams.setToLocation(toPlace);
                        Bundle extra = new Bundle();
                        extra.putString("textlang", "english");
                        extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                        ActivityUtil.openNewActivityForResult(PackageBookingHome.this, PackageBookingSearchResult.class, extra, REQUEST_NO_SERVICE_AVAILABLE);
                    } else {
                        searchParams.setAdultCount(passengerCount);
                        searchParams.setAdultFemaleCount(0);
                        searchParams.setChildrenCount(0);
                        searchParams.setDivyangBooking("General");
                        searchParams.setNumOfPassengers(passengerCount);
                        searchParams.setOnWardDate(selectedDate);
                        searchParams.setTextlanguage("english");
                        searchParams.setSingleLady(0);
                        Place fromPlace = new Place(trip.getFromPlaceCode(), String.valueOf(trip.getFromPlaceId()), trip.getFromPlaceName(), "", "");
                        Place toPlace = new Place(trip.getToPlaceCode(), String.valueOf(trip.getToPlaceId()), trip.getToPlaceName(), "", "");

                        searchParams.setFromLocation(fromPlace);
                        searchParams.setToLocation(toPlace);
                        Bundle extra = new Bundle();
                        extra.putString("textlang", "english");
                        extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);

                        if (trip.getPackageId() == 2) {
                            // Get selected room type
                            int selectedRoomId = holder.roomTypeGroup.getCheckedRadioButtonId();
                            String selectedRoomType = "";
                            if (selectedRoomId == holder.deluxeOption.getId()) {
                                selectedRoomType = "Deluxe";
                            } else if (selectedRoomId == holder.standardOption.getId()) {
                                selectedRoomType = "Standard";
                            } else if (selectedRoomId == holder.suiteOption.getId()) {
                                selectedRoomType = "Suite";
                            }

                            PackageBookingModel packageBookingModel = new PackageBookingModel();
                            packageBookingModel.setTripSelected(trip);
                            packageBookingModel.setDate(selectedDate);
                            packageBookingModel.setHotelType(selectedRoomType);
                            packageBookingModel.setNoOfPassengers(passengerCount + "");
                            searchParams.setPackageDetails(packageBookingModel);

                            ActivityUtil.openNewActivityForResult(PackageBookingHome.this, PackageBookingSearchResult.class, extra, REQUEST_NO_SERVICE_AVAILABLE);

                        } else {
                            // your existing logic for other packages
                        }


                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return tripList.size();
        }

        public class PackageTripViewHolder extends RecyclerView.ViewHolder {
            TextView packageName, fromTo;
            EditText datePickerEditText;
            Spinner passengerCountSpinner;
            Button bookButton;
            LinearLayout packageDetailsLayout;
            RadioGroup roomTypeGroup;
            RadioButton deluxeOption, standardOption, suiteOption;

            public PackageTripViewHolder(@NonNull View itemView) {
                super(itemView);
                packageName = itemView.findViewById(R.id.packageName);
                fromTo = itemView.findViewById(R.id.fromTo);
                datePickerEditText = itemView.findViewById(R.id.datePickerEditText);
                passengerCountSpinner = itemView.findViewById(R.id.passengerCountSpinner);
                bookButton = itemView.findViewById(R.id.bookButton);
                packageDetailsLayout = itemView.findViewById(R.id.packageDetailsLayout);
                roomTypeGroup = itemView.findViewById(R.id.roomTypeGroup);
                deluxeOption = itemView.findViewById(R.id.deluxeOption);
                standardOption = itemView.findViewById(R.id.standardOption);
                suiteOption = itemView.findViewById(R.id.suiteOption);
            }
        }
    }
}