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
import com.gsrtc.mobileweb.models.trackmybus.GetPickupPointDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Double lat,lon;
    Button submit;
    String textlang,startplace,endplace;
    List<GetPickupPointDetailsByPnrNoResponse> pickupDetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle=getIntent().getExtras();
        textlang = bundle.getString("textlang");
        lat = bundle.getDouble("Lattitude");
        lon = bundle.getDouble("Longtitude");

        pickupDetails = (List<GetPickupPointDetailsByPnrNoResponse>) bundle.getSerializable("pickuplist");
        submit = (Button) findViewById(R.id.homebtn);
        if(textlang.equalsIgnoreCase("gujarati")){
            submit.setText("ઘર");
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setup();
    }
    private void setup() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < pickupDetails.size(); i++)
        {
            String pick =pickupDetails.get(i).getPickPointName();
            List<Address> addressList3 = null;

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

        LatLng gujarat = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(gujarat).title("Current Bus Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_map)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gujarat));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(gujarat)      // Sets the center of the map to Mountain View
                .zoom(10)
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();

        CustomisedProgressDialog.STOP_CUST_DIA();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MapsActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        intent.putExtras(data);
        startActivity(intent);
    }
}

