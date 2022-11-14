package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.trackmybus.GetPickupPointDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSearchResultActivity;
import com.gsrtc.mobileweb.ui.activities.time_table.Timetable_Search;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsTrackbus extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Double lat,lon;
    Button submit,backbtn;
    SearchParamsCurrent searchParams;
    String textlang,startplace,endplace,bookType;
    Place StartPlaceID;
    Place EndPlaceID;
    static String adult;
    static String child;
    List<GetPickupPointDetailsByPnrNoResponse> pickupDetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_trackbus);

        Bundle bundle=getIntent().getExtras();
        textlang = bundle.getString("textlang");
        bookType = bundle.getString("bookType");
        lat = bundle.getDouble("Lattitude");
        lon = bundle.getDouble("Longtitude");
        startplace = bundle.getString("startplace");
        endplace = bundle.getString("endplace");
        pickupDetails = (List<GetPickupPointDetailsByPnrNoResponse>) bundle.getSerializable("pickuplist");
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);

        }
//        adult = String.valueOf(searchParams.getAdultCount());
//        child = String.valueOf(searchParams.getChildrenCount());
//        StartPlaceID = searchParams.getFromLocation();
//        EndPlaceID = searchParams.getToLocation();

        submit = (Button) findViewById(R.id.homebtn);
        backbtn = (Button) findViewById(R.id.backbtn);
        if(textlang.equalsIgnoreCase("gujarati")){
            submit.setText("ઘર");
            backbtn.setText(R.string.gBack);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setup();
    }
    private void setup() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsTrackbus.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bookType.equalsIgnoreCase("ON_ROUTE")) {

                    Intent intent = new Intent(MapsTrackbus.this, CurrentBookingSearchResultActivity.class);
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    data.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    intent.putExtras(data);
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(MapsTrackbus.this, Timetable_Search.class);
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Gujarat and move the camera
        LatLng gujarat = new LatLng(lat, lon);
        //LatLng gujarat = new LatLng(24.081621, 73.011009);
       // mMap.addMarker(new MarkerOptions().position(gujarat).title("Bus Location"));
        mMap.addMarker(new MarkerOptions().position(gujarat).title("Current Bus Location").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gujarat));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(gujarat)      // Sets the center of the map to Mountain View
                .zoom(12)
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();

        List<Address> addressList = null;
        List<Address> addressList1 = null;
        if (startplace != null || !startplace.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(startplace, 1);
                addressList1 = geocoder.getFromLocationName(endplace, 2);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if ((addressList != null) && !addressList.isEmpty() &&(addressList1 != null) && !addressList1.isEmpty() ) {
                Address address = addressList.get(0);
                Address address1 = addressList1.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(startplace).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                LatLng latLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng1).title(endplace).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

//            mMap.addMarker(new MarkerOptions().position(latLng1).title(startplace).icon(BitmapDescriptorFactory.fromResource(R.drawable.trackmydra)
//            ));

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
                CameraPosition cameraPosition1 = new CameraPosition.Builder().target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(12)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();

                // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1), 500, null);

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
                CameraPosition cameraPosition2 = new CameraPosition.Builder().target(latLng1)      // Sets the center of the map to Mountain View
                        .zoom(12)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();

                // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 1000, null);

                float zoomLevel = 06.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, zoomLevel));
            }
        }

        for (int i = 0; i < pickupDetails.size(); i++)
        {
            List<Address> addressList3 = null;

            String pick =pickupDetails.get(i).getPickPointName();

                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList3 = geocoder.getFromLocationName(pick, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (addressList3 != null && addressList3.size()>=1) {
                Address address = addressList3.get(0);

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(pickupDetails.get(i).getPickPointName()).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//            mMap.addMarker(new MarkerOptions().position(latLng1).title(startplace).icon(BitmapDescriptorFactory.fromResource(R.drawable.trackmydra)
//            ));

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
                CameraPosition cameraPosition1 = new CameraPosition.Builder().target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(12)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();

                // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1), 500, null);
                float zoomLevel = 06.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }
        }
        CustomisedProgressDialog.STOP_CUST_DIA();
    }
    @Override
    public void onBackPressed() {

        if(bookType.equalsIgnoreCase("ON_ROUTE")) {
            Intent intent = new Intent(MapsTrackbus.this, CurrentBookingSearchResultActivity.class);
            Bundle data = new Bundle();
            data.putString("textlang", textlang);
            data.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            intent.putExtras(data);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MapsTrackbus.this, Timetable_Search.class);
            Bundle data = new Bundle();
            data.putString("textlang", textlang);
            intent.putExtras(data);
            startActivity(intent);
        }
    }
}

