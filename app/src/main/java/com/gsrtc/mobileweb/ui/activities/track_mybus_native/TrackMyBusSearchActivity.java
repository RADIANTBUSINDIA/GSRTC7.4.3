package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.trackmybus.GetPnrDetailsResponse;
import com.gsrtc.mobileweb.models.trackmybus.GetVehicleCurrentStatusResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.models.trackmybus.TrackmybusHistorySearchParam;
import com.gsrtc.mobileweb.models.trackmybus.Vehicle;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.XMLtoJSONResponseParser;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TrackMyBusNetworkUtil;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class TrackMyBusSearchActivity extends BaseNavigationDrawerActivity  {

    private RecyclerView recycler;
    String TAG = "Response";
    Trackmybus tracking;
    SoapPrimitive resultString;
    List<GetVehicleCurrentStatusResponse> data = new ArrayList<>();
    ActivityViewHolder holder;
    ProgressDialog progressDialog;
    EditText PnrNo,TripCode;
    private Button submit,Btnmap;
    private GoogleMap mMap;
    private double lattitude, longtitude;
    private String firstLetter;
    private String pnrNo, vehicleNo,vehicle, currentDate, value,resultdata,tripcode,trackBus;
    String textlang;

    TrackmybusHistorySearchParam trackmyParams;

    List<Vehicle> vehicleList = new ArrayList<Vehicle>();
    List<String> vehicleListNames = new ArrayList<String>();
    boolean vehiclenoListFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_track_my_bus_search);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");
        getTrackBusCredentials();
        submit = (Button) findViewById(R.id.submit);
        Btnmap=(Button) findViewById(R.id.mapbtn);
        PnrNo = (EditText) findViewById(R.id.PnrNo);
        TripCode = (EditText) findViewById(R.id.tripcode);

        if(textlang.equalsIgnoreCase("gujarati")){
            submit.setText(R.string.trackopt_submit);
            Btnmap.setText(R.string.trackopt_viewmap);
            PnrNo.setHint(R.string.trackopt_pnrno);
            TripCode.setHint(R.string.trackopt_tripco);
        }
        setup();
    }

    private void setup() {
        holder = new ActivityViewHolder(this);
        trackmyParams = new TrackmybusHistorySearchParam();

        fetchServerData();

        holder.VehicleNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = holder.VehicleNo.getText().toString();
                trackmyParams.setVehicleNo(String.valueOf(vehicleList.get(i)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tracking = new Trackmybus();
        tracking.trackmybusHistorySearchParam = new TrackmybusHistorySearchParam();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                // HIDE INPUT KEYBORD
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.VehicleNo.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(PnrNo.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(TripCode.getWindowToken(), 0);
                onsubmit();
            }
        });

      /**  calling mapActivity  **/

        Btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TrackMyBusSearchActivity.this, MapsActivity.class);
                holder.recyclerView.setVisibility(View.GONE);
                Btnmap.setVisibility(View.GONE);
                intent.putExtra("textlang", textlang);
                intent.putExtra("Lattitude",Double.parseDouble(data.get(0).getLattitude() ));
                intent.putExtra("Longtitude",Double.parseDouble(data.get(0).getLongitude()));
                startActivity(intent);
            }
        });
    }

    private void onsubmit() {

        data.clear();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd", Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));
        tracking.trackmybusHistorySearchParam.CurrentDate = cur_year+cur_day+cur_month;

        tracking.trackmybusHistorySearchParam.PnrNo = PnrNo.getText().toString();
        tracking.trackmybusHistorySearchParam.TripCode = TripCode.getText().toString();
        tracking.trackmybusHistorySearchParam.VehicleNo = holder.VehicleNo.getText().toString();


        pnrNo = tracking.trackmybusHistorySearchParam.PnrNo;
        vehicle = tracking.trackmybusHistorySearchParam.VehicleNo;
        tripcode = tracking.trackmybusHistorySearchParam.TripCode;
        currentDate = tracking.trackmybusHistorySearchParam.CurrentDate;

        if(pnrNo.equalsIgnoreCase("") &&vehicle.equalsIgnoreCase("GJ-")&&tripcode.equalsIgnoreCase("")){
            ActivityUtil.showToast(this, "Please Enter valid details");
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            return;
        }
        if(pnrNo.equalsIgnoreCase("") &&vehicle.equalsIgnoreCase("")&&tripcode.equalsIgnoreCase("")){
            ActivityUtil.showToast(this, "Please Enter valid details");
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            return;
        }
        if(pnrNo!=null &&vehicle.equalsIgnoreCase("GJ-")&&tripcode.equalsIgnoreCase("")) {
            tracking.trackmybusHistorySearchParam.StatusCode = "P";
            tracking.trackmybusHistorySearchParam.ModuleName = PnrNo.getText().toString();
            firstLetter = PnrNo.getText().toString().substring(0, 1);
            System.out.println("firstLetter" + firstLetter);
            if (firstLetter != null && firstLetter.equalsIgnoreCase("G")) {
                goNextpnr();
            } else {
                PnrNo.setText("");
                Btnmap.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.GONE);
                ActivityUtil.showToast(this, "Please Enter valid PNR Number");
                return;
            }
        }
        else if(pnrNo.equalsIgnoreCase("") &&vehicle!=null&&tripcode.equalsIgnoreCase(""))
        {
            tracking.trackmybusHistorySearchParam.StatusCode = "V";
            tracking.trackmybusHistorySearchParam.ModuleName = holder.VehicleNo.getText().toString();
            firstLetter = holder.VehicleNo.getText().toString().substring(0, 3);
            System.out.println("firstLetter" + firstLetter);
            if (firstLetter != null && firstLetter.equalsIgnoreCase("GJ-") && holder.VehicleNo.getText().toString().length()>11) {
                goNextpnr();
            } else {
                holder.VehicleNo.setText("GJ-");
                Btnmap.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.GONE);
                ActivityUtil.showToast(this, "Please Enter valid Vehicle Number");
                return;
            }
            //goNextpnr();

        }else if(pnrNo.equalsIgnoreCase("")&&vehicle.equalsIgnoreCase("GJ-")&&tripcode!=null){
            tracking.trackmybusHistorySearchParam.StatusCode = "T";
            tracking.trackmybusHistorySearchParam.ModuleName = TripCode.getText().toString();
            goNextTripdetails();

        }else if(pnrNo!=null&&vehicle!=null&&tripcode!=null)
        {
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            ActivityUtil.showToast(this, "Please Enter Any One detail");
            return;
        }
        else if(pnrNo!=null&&vehicle!=null&&tripcode.equalsIgnoreCase(""))
        {
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            ActivityUtil.showToast(this, "Please Enter Any One detail");
            return;
        }
        else if(pnrNo!=null&&vehicle.equalsIgnoreCase("GJ-")&&tripcode!=null)
        {
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            ActivityUtil.showToast(this, "Please Enter Any One details");
            return;
        }
        else if(pnrNo.equalsIgnoreCase("")&&vehicle!=null&&tripcode!=null)
        {
            holder.recyclerView.setVisibility(View.GONE);
            Btnmap.setVisibility(View.GONE);
            ActivityUtil.showToast(this, "Please Enter Any One details");
            return;
        }
    }

    /****pnr number *****/
    public void goNextpnr()
    {
        if (tracking.getPnrDetailsResponse == null) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            CustomisedProgressDialog.SHOW_CUST_DIA(TrackMyBusSearchActivity.this, "Please wait loading...");
            getAllBusespnr();
            return;
        }
        else {
            hideProgressDialog();
        }
        CustomisedProgressDialog.STOP_CUST_DIA();
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putString("cred",trackBus);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.TRACK_MY_BUS, tracking);
        ActivityUtil.openNewActivity(this, TrackingInfo.class, bundle);
    }


    public void goNextTripdetails()
    {
        if (tracking.getPnrDetailsResponse == null) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            CustomisedProgressDialog.SHOW_CUST_DIA(TrackMyBusSearchActivity.this, "Please wait loading...");
            goNextTrip();
            return;
        }
        else {
            hideProgressDialog();
        }

        CustomisedProgressDialog.STOP_CUST_DIA();
        Bundle bundle = new Bundle();
        bundle.putString("textlang",textlang);
        bundle.putString("cred",trackBus);
        bundle.putSerializable(AppConstants.INTENT_EXTRA.TRACK_MY_BUS, tracking);
        ActivityUtil.openNewActivity(this, TrackingInfo.class, bundle);
    }


    /** get trackdata based on tripcode **/

    public void goNextTrip()
    {
        holder.recyclerView.setVisibility(View.VISIBLE);
        CustomisedProgressDialog.SHOW_CUST_DIA(TrackMyBusSearchActivity.this, "Please wait loading...");

        TrackMyBusNetworkUtil.GetTripcodeDetails(tracking, new TrackMyBusNetworkUtil.GetTripcodeDetailsCallback() {

            public void onSuccess(GetPnrDetailsResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();

                        ActivityUtil.showToast(TrackMyBusSearchActivity.this, "No Data Available");
                    }
                    tracking.getPnrDetailsResponse = response;
                   // recycleviewProcesspnr();
                    goNextTripdetails();

                } else {
                    PnrNo.setText("");
                    holder.VehicleNo.setText("");
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(TrackMyBusSearchActivity.this, "No Data Available");

                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(TrackMyBusSearchActivity.this, e);
            }
        });
    }

    /** trackdata based onpnr  **/

    private void getAllBusespnr() {

        TrackMyBusNetworkUtil.GetPnrDetails(tracking, new TrackMyBusNetworkUtil.GetPnrDetailsCallback() {

            public void onSuccess(GetPnrDetailsResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(TrackMyBusSearchActivity.this, "No Data Available");
                    }
                    tracking.getPnrDetailsResponse = response;
                    //recycleviewProcesspnr();

                    goNextpnr();

                } else {
                    PnrNo.setText("");
                    holder.VehicleNo.setText("");
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(TrackMyBusSearchActivity.this, "No Data Available");

                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(TrackMyBusSearchActivity.this, e);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tracking != null) {
            tracking.getPnrDetailsResponse = null;
        }
    }

    public void showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                progressDialog.dismiss();
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public void hideProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(TrackMyBusSearchActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        intent.putExtras(data);
        startActivity(intent);
    }

    public class ActivityViewHolder {
        AutoCompleteTextView VehicleNo;
        RecyclerView recyclerView;
        SwipeRefreshLayout swipeContainer;
        TextView no_internet_text,head,or_one,or_two;

        ActivityViewHolder(Activity activity) {
            VehicleNo= (AutoCompleteTextView) activity.findViewById(R.id.VehicleNo);
            head = (TextView) activity.findViewById(R.id.header_title);
            or_one = (TextView) activity.findViewById(R.id.txt_or);
            or_two = (TextView) activity.findViewById(R.id.txt_or_one);
            no_internet_text = (TextView) activity.findViewById(R.id.no_internet_text);
            recyclerView = (RecyclerView) activity.findViewById(R.id.history_search_results_recycler_view);

            if(textlang.equalsIgnoreCase("gujarati")){
                VehicleNo.setHint(R.string.trackopt_vehicno);
                head.setText(R.string.trackopt_trackmy_bus);
                or_one.setText(R.string.trackopt_or);
                or_two.setText(R.string.trackopt_or);
            }
        }
    }

    /** fetch VehicleNo List **/

    public void fetchServerData() {

        SoapClientUtil.fetchVehicleListAsync(new SoapClientUtil.VehicleListCallback() {
            @Override
            public void onSuccess(List<Vehicle> vehicList) {
                vehicleList = vehicList;
                vehicleListNames = new ArrayList<String>();
                for (Vehicle data : vehicleList) {
                    vehicleListNames.add(data.getVehicleNo().trim());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrackMyBusSearchActivity.this, android.R.layout.simple_dropdown_item_1line, vehicleListNames);
                holder.VehicleNo.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                vehiclenoListFetched = true;
                if (vehiclenoListFetched) {
                }
            }

            @Override
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showNoNetworkToast(TrackMyBusSearchActivity.this);
            }
        });
    }

    public void getTrackBusCredentials(){

        SoapClientUtil.fetchResponseString("GetTrackmyBusCredentials", "", "",
                new SoapClientUtil.BaseDefaultHttpCallback() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onSuccess(String response) {


                        try {
                            org.json.JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                            org.json.JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                            String requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTrackMyBusCredentials").getString("GetTrackMyBusCredentials");
                            System.out.println(requiredResponse);
                            trackBus =requiredResponse;




                        }catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });


    }

}