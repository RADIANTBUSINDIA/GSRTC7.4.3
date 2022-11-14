package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.trackmybus.GetNearby_location_list;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.TrackmybusHistorySearchParam;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TrackMyBusNetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Neraby_location extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Trackmybus tracking;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 50;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private String locate,textlang;
    private FusedLocationProviderClient fusedLocationClient;

    List<GetNearby_location_list> pickLocations = new ArrayList<>();

//    private static String[] abd={"AHMEDABAD ADALAJ X","AHMEDABAD BAPUNAGAR","AHMEDABAD BOPAL","AHMEDABAD CBS GEETA MANDIR","AHMEDABAD CHANDKHEDA","AHMEDABAD CTM EXPRESSWAY","AHMEDABAD INDIRA BRIDGE CIRCLE","AHMEDABAD ISCON CROSS ROAD X","AHMEDABAD KALUPUR (SARANGPUR)","AHMEDABAD KRISHNANAGAR STAND","AHMEDABAD NARODA PATIYA","AHMEDABAD NEHRUNAGAR","AHMEDABAD NIKOLGAM","AHMEDABAD PALDI","AHMEDABAD RANIP","AHMEDABAD SARKHEJ/SANAND X","AHMEDABAD WADAJ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neraby_location);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        tracking = new Trackmybus();
        tracking.trackmybusHistorySearchParam = new TrackmybusHistorySearchParam();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {

            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {

            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        Button btnhome = (Button) findViewById(R.id.home_btn);
        btnhome.setOnClickListener(new View.OnClickListener() {
            String BusStation = locate;

            @Override
            public void onClick(View v) {
//                trackmore_info();
//                Log.d("onClick", "Button is Clicked");
//                mMap.clear();
//                String url = getUrl(latitude, longitude, locate);
//                Object[] DataTransfer = new Object[3];
//                DataTransfer[0] = mMap;
//                DataTransfer[1] = url;
//                DataTransfer[2] = locate;
//                Log.d("onClick", url);
//                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
//                getNearbyPlacesData.execute(DataTransfer);
//                Toast.makeText(Neraby_location.this, "Nearby Bus Stations", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Neraby_location.this, HomeActivity.class);
                Bundle data = new Bundle();
                data.putString("textlang", textlang);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//        Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,
                getApplicationContext(), new GeocoderHandler());

//        latitude = 23.0133;
//        longitude = 72.5925;
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(locate);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(Neraby_location.this,"Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("HandlerLeak")
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            locate = locationAddress;
            if (locate != null) {
                tracking.trackmybusHistorySearchParam.PickLocation = locate;
                //tracking.trackmybusHistorySearchParam.PickLocation="ahmedabad";

                trackmore_info();

//                Log.d("onClick", "Button is Clicked");
//                mMap.clear();
//                String url = getUrl(latitude, longitude, locate);
//                Object[] DataTransfer = new Object[3];
//                DataTransfer[0] = mMap;
//                DataTransfer[1] = url;
//                DataTransfer[2] = locate;
//                Log.d("onClick", url);
//                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
//                getNearbyPlacesData.execute(DataTransfer);

            }else{
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showToast(Neraby_location.this, "No Data Available");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private void ShowNearbyPlaces() {

        double lat,lang;
        String place_name;

        List<Address> addressList = null;
        Geocoder geocoder;
        geocoder = new Geocoder(Neraby_location.this);

        for (int i = 0; i < pickLocations.size(); i++) {
            try {
                addressList = geocoder.getFromLocationName(pickLocations.get(i).getPlaceName(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList != null && addressList.size() >= 1) {
                Address address = addressList.get(0);
                lat = Double.parseDouble(String.valueOf(address.getLatitude()));
                lang = Double.parseDouble(String.valueOf(address.getLongitude()));
                place_name = pickLocations.get(i).getPlaceName();

                Log.d("onPostExecute", "Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();

                LatLng latLng = new LatLng(lat, lang);
                markerOptions.position(latLng);
                markerOptions.title(place_name);
//                    if(place_name.toString().contains("BUS") || place_name.toString().contains("Bus") || place_name.toString().contains("bus")){
                mMap.addMarker(markerOptions);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            }
        }
        CustomisedProgressDialog.STOP_CUST_DIA();
    }


    public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

            String googlePlacesData;
            GoogleMap mMap;
            String url;
            double lat,lang;
            String place_name,current_place;

            @Override
            protected String doInBackground(Object... params) {
                try {
                    Log.d("GetNearbyPlacesData", "doInBackground entered");
                    mMap = (GoogleMap) params[0];
                    url = (String) params[1];
                    current_place = (String) params[2];
                    DownloadUrl downloadUrl = new DownloadUrl();
                    googlePlacesData = downloadUrl.readUrl(url);
                    Log.d("GooglePlacesReadTask", "doInBackground Exit");
                } catch (Exception e) {
                    Log.d("GooglePlacesReadTask", e.toString());
                }
                return googlePlacesData;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d("GooglePlacesReadTask", "onPostExecute Entered");
//                List<HashMap<String, String>> nearbyPlacesList = null;
//                DataParser dataParser = new DataParser();
//                nearbyPlacesList =  dataParser.parse(result);
//                ShowNearbyPlaces(nearbyPlacesList);
                ShowNearbyPlaces();
                Log.d("GooglePlacesReadTask", "onPostExecute Exit");
            }

//            private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
             private void ShowNearbyPlaces() {
                List<Address> addressList = null;
                Geocoder geocoder;
                geocoder = new Geocoder(Neraby_location.this);

                for (int i = 0; i < pickLocations.size(); i++) {
                    try {
                        addressList = geocoder.getFromLocationName(pickLocations.get(i).getPlaceName(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null && addressList.size() >= 1) {
                        Address address = addressList.get(0);
                        lat = Double.parseDouble(String.valueOf(address.getLatitude()));
                        lang = Double.parseDouble(String.valueOf(address.getLongitude()));
                        place_name = pickLocations.get(i).getPlaceName();

                        Log.d("onPostExecute", "Entered into showing locations");
                        MarkerOptions markerOptions = new MarkerOptions();

//                    double lat = Double.parseDouble(googlePlace.get("lat"));
//                    double lng = Double.parseDouble(googlePlace.get("lng"));
//                    String placeName = googlePlace.get("place_name");
//                    String vicinity = googlePlace.get("vicinity");
                        LatLng latLng = new LatLng(lat, lang);
                        markerOptions.position(latLng);
                        markerOptions.title(place_name);
//                    if(place_name.toString().contains("BUS") || place_name.toString().contains("Bus") || place_name.toString().contains("bus")){
                        mMap.addMarker(markerOptions);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        CustomisedProgressDialog.STOP_CUST_DIA();
                }
              }
            }
        }

    private void trackmore_info() {
        //CustomisedProgressDialog.SHOW_CUST_DIA(Neraby_location.this, "please wait...");

        TrackMyBusNetworkUtil.getNearbylocationDetails(tracking, new TrackMyBusNetworkUtil.getNearbylocationDetailsCallback() {

            public void onSuccess(GetNearby_location_list[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(Neraby_location.this, "No Data Available");
                    }else {

                        tracking.getNearby_location_list = response;
                        pickLocations = Arrays.asList(tracking.getNearby_location_list);

//                        ShowNearbyPlaces();
                    }
                    if(response.length > 0) {
                        ShowNearbyPlaces();
                    }
                } else {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(Neraby_location.this, "No Data Available");
                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(Neraby_location.this, e);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Neraby_location.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}

