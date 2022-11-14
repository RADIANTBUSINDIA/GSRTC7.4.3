package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.trackmybus.Finaltrack;
import com.gsrtc.mobileweb.models.trackmybus.GetVehicleCurrentStatusResponse;
import com.gsrtc.mobileweb.models.trackmybus.VehicleList;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class RouteTrackMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    String textlang;

    // Latitude & Longitude
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    List<Finaltrack> vehicleDetails = new ArrayList<>();
    List<GetVehicleCurrentStatusResponse> trackDetails = new ArrayList<>();

    List<VehicleList> vehicleData = new ArrayList<>();
    List<GetVehicleCurrentStatusResponse> trackData = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_track_map);

        Bundle bundle = getIntent().getExtras();
        textlang = bundle.getString("textlang");
        vehicleDetails = (List<Finaltrack>) bundle.getSerializable("vehicleData");
        trackDetails = (List<GetVehicleCurrentStatusResponse>) bundle.getSerializable("trackData");

        // *** Display Google Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button btnhome = (Button) findViewById(R.id.home_bt);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RouteTrackMap.this, Track_bus.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                i.putExtras(data);
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < vehicleDetails.size(); i++)
        {
            Latitude = Double.parseDouble(trackDetails.get(i).getLattitude());
            Longitude = Double.parseDouble(trackDetails.get(i).getLongitude());

            LatLng gujarat = new LatLng(Latitude, Longitude);
            if(textlang.equalsIgnoreCase("gujarati")){
                String name = ("ટ્રિપ કોડ  ::   " + vehicleDetails.get(i).getTripCode() +
                        "\n મૂળ / પ્રસ્થાન સમય   ::   " + vehicleDetails.get(i).getOrgin() +", "+ vehicleDetails.get(i).getDepTime() +
                        "\n લક્ષ્યસ્થાન / આગમન સમય  ::   " +vehicleDetails.get(i).getDestination()+", "+vehicleDetails.get(i).getArrTime()+
                        "\n કંડક્ટર મોબાઈલ  ::   " +vehicleDetails.get(i).getConMobileNo()+
                        "\n વાહન નંબર  ::   " +vehicleDetails.get(i).getVehicleNo()) ;
                mMap.addMarker(new MarkerOptions().position(gujarat).title("બસની વિગતો").snippet(name));

            }else {
                String name = ("Tripcode  ::   " + vehicleDetails.get(i).getTripCode() +
                        "\n Origin / Dept.Time  ::   " + vehicleDetails.get(i).getOrgin() + ", " + vehicleDetails.get(i).getDepTime() +
                        "\n Destinition / Arrival Time  ::   " + vehicleDetails.get(i).getDestination() + ", " + vehicleDetails.get(i).getArrTime() +
                        "\n Conductor Mobile  ::   " + vehicleDetails.get(i).getConMobileNo() +
                        "\n Vehicle Number  ::   " + vehicleDetails.get(i).getVehicleNo());
                mMap.addMarker(new MarkerOptions().position(gujarat).title("Bus Details").snippet(name));
            }
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(RouteTrackMap.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(RouteTrackMap.this);
                    title.setTextColor(Color.RED);
                    title.setTextSize((float) 20);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(RouteTrackMap.this);
                    snippet.setTextColor(Color.BLUE);
                    snippet.setTypeface(null, Typeface.BOLD);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gujarat, 6));
        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(RouteTrackMap.this, Track_bus.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}



