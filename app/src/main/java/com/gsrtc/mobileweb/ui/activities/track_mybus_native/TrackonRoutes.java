package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.trackmybus.Finaltrack;
import com.gsrtc.mobileweb.models.trackmybus.GetVehicleCurrentStatusResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.VehicleList;
import com.gsrtc.mobileweb.ui.activities.advance_booking.SeatSelectionActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TrackonRoutes extends BaseNavigationDrawerActivity {

    ViewHolder holder;
    SearchParams searchParams;
    Place place;
    boolean addressListFetched = false;
    List<GetVehicleCurrentStatusResponse> data = new ArrayList<>();
    List<VehicleList> busvehicList = new ArrayList<>();
    List<Place> locationList = new ArrayList<Place>();
    List<Finaltrack> vehiclefinal = new ArrayList<>();
    List<String> locationListNames = new ArrayList<String>();
    SoapPrimitive resultString;
    private String resultdata,Vehic,CurrentDate,Vehiclast;
    Trackmybus tracking;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_trackon_routes);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        setupViews();
    }

    public void setupViews()
    {
        holder = new ViewHolder(this);
        searchParams = new SearchParams();
        searchParams.setTextlanguage(textlang);

        holder.to_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.to_location.getText().toString();
                searchParams.setToLocation(locationList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationListNames);
        holder.from_location.setAdapter(adapter);
        holder.from_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.from_location.getText().toString();
                searchParams.setFromLocation(locationList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        holder.search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat df2 = new SimpleDateFormat("dd", Locale.getDefault());
                int cur_year = Integer.valueOf(df1.format(c.getTime()));
                String cur_month = String.valueOf(df.format(c.getTime()));
                String cur_day = String.valueOf(df2.format(c.getTime()));
                CurrentDate = (cur_year + "-" + cur_month + "-" + cur_day);

                goToResultActivity();
            }
        });
        holder.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromvalue=holder.from_location.getText().toString();
                String tovalue=holder.to_location.getText().toString();
                if ((fromvalue.matches("")) || tovalue.matches("")){

                    Toast.makeText(TrackonRoutes.this, "Please Enter valid place", Toast.LENGTH_SHORT).show();
                    return;
                }
                holder.from_location.setText(tovalue);
                holder.to_location.setText(fromvalue);
                holder.to_location.clearFocus();
                holder.from_location.clearFocus();

            }
        });

        fetchServerData();
    }
    public void goToResultActivity() {
        Place startPlace = null;
        Place endPlace = null;
        if (TextUtils.isEmpty(holder.from_location.getText().toString()) || TextUtils.isEmpty(holder.to_location.getText().toString())) {
            ActivityUtil.showToast(TrackonRoutes.this, "Please select a location");
            return;
        } else {
            for (Place place : locationList) {
                if (place.getPlaceName().equals(holder.from_location.getText().toString())) {
                    startPlace = place;
                }
                if (place.getPlaceName().equals(holder.to_location.getText().toString())) {
                    endPlace = place;
                }
            }
        }
        if (startPlace == null) {
            ActivityUtil.showToast(TrackonRoutes.this, "Please select a valid start place");
            return;
        }
        if (endPlace == null) {
            ActivityUtil.showToast(TrackonRoutes.this,  "Please select a valid end place");
            return;
        }

        searchParams.setFromLocation(startPlace);
        searchParams.setToLocation(endPlace);

        fetchVehicleNo();

    }

    public void fetchVehicleNo(){

        CustomisedProgressDialog.SHOW_CUST_DIA(TrackonRoutes.this, "Please wait loading...");

        SoapClientUtil.getVehicleNoDetails(searchParams, new SoapClientUtil.GetVehicleNoDetailsCallback() {
            @Override
            public void onSuccess(List<VehicleList> vehiNoList) {
                if(vehiNoList.size() == 0){
                    ActivityUtil.showToast(TrackonRoutes.this, "No Data Available");
                        finish();
                    }else{

                    busvehicList = vehiNoList;

                        getAllBuses();

                    }
            }

            @Override
            public void onError(Exception e) {
                ActivityUtil.showErrorToast(TrackonRoutes.this, e);
            }

        });

    }


    private void getAllBuses()
    {
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.d(String.valueOf(data),"getdata");
            Log.d(String.valueOf(vehiclefinal),"getdataone");

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            for (int i = 0; i < busvehicList.size(); i++) {
                Vehic = busvehicList.get(i).getVehicleNo().toString();
                calculate();
            }
                return null;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void result) {

          finaldata();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/GetVehicleCurrentStatus";
        String METHOD_NAME = "GetVehicleCurrentStatus";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "https://gujarattransporttrackbusapi.infinium.management/TrackingService.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("APIUserName", "infinium");
            Request.addProperty("APIPassword", "Infinium1234");
            Request.addProperty("VehicleNo", Vehic);
            Request.addProperty("ScheduleDate", CurrentDate);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            resultdata = String.valueOf(resultString);

            try {

                if(resultdata.equalsIgnoreCase("NO DATA AVAILABLE")){

                }

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(resultString.toString());
                JSONArray array = (JSONArray) obj;
                JSONObject jsonobject = (JSONObject) array.get(0);

                GetVehicleCurrentStatusResponse getVehicleCurrentStatusResponse = new GetVehicleCurrentStatusResponse();
                getVehicleCurrentStatusResponse.setDepartureDateTime((String) jsonobject.get("DepartureDateTime"));
                getVehicleCurrentStatusResponse.setLastBusStation((String) jsonobject.get("LastBusStation"));
                getVehicleCurrentStatusResponse.setLastArrivalDateTime((String) jsonobject.get("LastArrivalDateTime"));
                getVehicleCurrentStatusResponse.setStatus((String) jsonobject.get("Status"));
                getVehicleCurrentStatusResponse.setRouteName((String) jsonobject.get("RouteName"));
                getVehicleCurrentStatusResponse.setNextLocation((String) jsonobject.get("NextLocation"));
                getVehicleCurrentStatusResponse.setETA((String) jsonobject.get("ETA"));
                getVehicleCurrentStatusResponse.setLattitude((String) jsonobject.get("Latitude"));
                getVehicleCurrentStatusResponse.setLongitude((String) jsonobject.get("Longitude"));
                data.add(getVehicleCurrentStatusResponse);
                Log.d(String.valueOf(data), "getdata");

                VehicleList vehicleList = null;

                for (int i = 0; i < busvehicList.size(); i++) {
                    Vehiclast = busvehicList.get(i).getVehicleNo().toString();
                    Log.d(String.valueOf(vehiclefinal), "getdataone");
                    vehicleList = (VehicleList) (busvehicList.get(i));
                    if (Vehiclast.equalsIgnoreCase(Vehic)) {

                        vehiclefinal.add(vehicleList);
                        for(int k=0;k<vehiclefinal.size();k++) {
                            for (int j = k + 1; j < vehiclefinal.size(); j++) {
                                if (vehiclefinal.get(k).equals(vehiclefinal.get(j))) {
                                    vehiclefinal.remove(j);
                                    j--;
                                }
                            }
                        }
                    }
                }

                Log.d(String.valueOf(vehiclefinal), "getdataone");

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {

            finaldata();
        }
    }

    private void finaldata() {

            Intent intent = new Intent(TrackonRoutes.this, RouteTrackMap.class);
            Bundle bundle = new Bundle();
            bundle.putString("textlang", textlang);
            bundle.putSerializable("vehicleData", (Serializable) vehiclefinal);
            bundle.putSerializable("trackData", (Serializable) data);
            intent.putExtras(bundle);
            startActivity(intent);
    }

    public void fetchServerData() {

     SoapClientUtil.fetchAddressListAsync(new SoapClientUtil.AddressListCallback() {
         @Override
         public void onSuccess(List<Place> placeList) {
             locationList = placeList;
             locationListNames = new ArrayList<String>();
             for (Place location : locationList) {
                 locationListNames.add(location.getPlaceName());
             }
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrackonRoutes.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
             holder.from_location.setAdapter(adapter);
             adapter.notifyDataSetChanged();
             ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TrackonRoutes.this, android.R.layout.simple_dropdown_item_1line, locationListNames);
             holder.to_location.setAdapter(adapter2);
             adapter2.notifyDataSetChanged();
             addressListFetched = true;
             if (addressListFetched)
             {
                 CustomisedProgressDialog.STOP_CUST_DIA();
             }
         }

         @Override
         public void onError(Exception e)
         {
             CustomisedProgressDialog.STOP_CUST_DIA();
             ActivityUtil.showNoNetworkToast(TrackonRoutes.this);
         }
     });
 }
    public class ViewHolder{
        View container;
        AutoCompleteTextView from_location, to_location;
        Button search_btn;
        TextView head,subhead,source,dest,note;
        ImageView swap;

        ViewHolder(Activity rootView){
            container = rootView.findViewById(R.id.container);
            from_location = (AutoCompleteTextView) rootView.findViewById(R.id.source);
            from_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            to_location = (AutoCompleteTextView) rootView.findViewById(R.id.destination);
            to_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            head =(TextView)rootView.findViewById(R.id.title_head);
            subhead =(TextView)rootView.findViewById(R.id.title_subhead);
            source =(TextView)rootView.findViewById(R.id.txt_source);
            dest =(TextView)rootView.findViewById(R.id.txt_destination);
            note =(TextView)rootView.findViewById(R.id.txt_note);

            swap= (ImageView) rootView.findViewById(R.id.swap);
            search_btn = (Button) rootView.findViewById(R.id.search);

            if(textlang.equalsIgnoreCase("gujarati")){

                from_location.setHint(R.string.fare_from);
                to_location.setHint(R.string.fare_to);
                head.setText(R.string.trvehic_header);
                subhead.setText(R.string.trvehic_sub_header);
                source.setText(R.string.trvehic_source);
                dest.setText(R.string.trvehic_dest);
                note.setText(R.string.trvehic_note);
                search_btn.setText(R.string.trvehic_search);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TrackonRoutes.this, Track_bus.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        intent.putExtras(data);
        startActivity(intent);
    }
}
