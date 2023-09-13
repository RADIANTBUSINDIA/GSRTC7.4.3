package com.gsrtc.mobileweb.ui.activities.track_mybus_native;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.trackmybus.GetPickupPointDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.trackmybus.GetPnrDetailsResponse;
import com.gsrtc.mobileweb.models.trackmybus.GetVehicleCurrentStatusResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.ui.activities.current_booking_native.CurrentBookingSearchResultActivity;
import com.gsrtc.mobileweb.ui.activities.time_table.Timetable_Search;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TrackMyBusNetworkUtil;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CurrentTrackBus extends BaseNavigationDrawerActivity {

    Trackmybus tracking;
    ActivityViewHolder holder;
    String TAG = "Response";
    List<GetVehicleCurrentStatusResponse> data = new ArrayList<>();
    List<GetPickupPointDetailsByPnrNoResponse> pickuppoints = new ArrayList<>();
    boolean dataFetched = false;
    SearchParamsCurrent searchParams;
    SearchResultAdapter searchResultAdapter;
    private Button Btnmap,bookbtn;
    private TextView moreinfo;
    private LinearLayout linearLayout;
    SoapPrimitive resultString;
    private String textlang,currentBusTime,exactBustTime,bookType;
    private String StartPlace,EndPlace,scheduleDate;
    static String adult;
    static String child;
    private TextView pickup_name,pick_time,pick_day;
    Date busTime,prevTime;


    private String pnrNo, vehicleNo,vehicle,resultdata,tripcode,status,trackBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_current_track_bus);

        if(getIntent()!=null && getIntent().getExtras()!=null){
            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);

            Bundle extras =  getIntent().getExtras();

            textlang =extras.getString("textlang");
            bookType =extras.getString("bookType");
            trackBus =extras.getString("cred");
            StartPlace =searchParams.getFromLocation().getPlaceName();
            EndPlace =searchParams.getToLocation().getPlaceName();

            Btnmap=(Button) findViewById(R.id.mapbtn);
            bookbtn=(Button) findViewById(R.id.bookbtn);
            linearLayout = (LinearLayout)findViewById(R.id.moreinfo_title);
            moreinfo=(TextView) findViewById(R.id.more_info);
            pickup_name=(TextView) findViewById(R.id.txtpickup);
            pick_time=(TextView) findViewById(R.id.txtpickuptime);
            pick_day=(TextView) findViewById(R.id.txtpickday);
            tracking = (Trackmybus) extras.getSerializable(AppConstants.INTENT_EXTRA.TRACK_MY_BUS);
            if(textlang.equalsIgnoreCase("gujarati")){
                moreinfo.setText(R.string.gRoute_Live_Info);
               // bookbtn.setText(R.string.gBack);
                pickup_name.setText(R.string.gPickup_Name);
                pick_time.setText(R.string.gTime);
                pick_day.setText(R.string.gDay);
            }

            setup();
        }
    }

    public void setup() {

        holder = new ActivityViewHolder(this);

        if(textlang.equalsIgnoreCase("gujarati")){
            holder.head.setText("ટ્રેકિંગ માહિતી");
        }
        datainfo();

        Btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(0).getLattitude()==null&& data.get(0).getLongitude()==null) {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentTrackBus.this, "Unable to fetch exact location of Bus");
                }else {
                    CustomisedProgressDialog.SHOW_CUST_DIA(CurrentTrackBus.this, "Please wait loading...");
                    Intent intent = new Intent(CurrentTrackBus.this, MapsTrackbus.class);

                    Double lat=Double.parseDouble(data.get(0).getLattitude());
                    Double lang =Double.parseDouble(data.get(0).getLongitude());
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    data.putString("bookType", bookType);
                    data.putDouble("Lattitude", lat);
                    data.putDouble("Longtitude", lang);
                    data.putString("startplace", StartPlace);
                    data.putString("endplace", EndPlace);
                    data.putSerializable("pickuplist", (Serializable) pickuppoints);

                    data.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });

        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bookType.equalsIgnoreCase("ON_ROUTE")) {

                    Intent intent = new Intent(CurrentTrackBus.this, CurrentBookingSearchResultActivity.class);
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    data.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                    intent.putExtras(data);
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(CurrentTrackBus.this, Timetable_Search.class);
                    Bundle data = new Bundle();
                    data.putString("textlang", textlang);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });
        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trackmore_info();
            }
        });
    }
    private void datainfo() {
        searchResultAdapter = new SearchResultAdapter(this, Arrays.asList(tracking.getPnrDetailsResponse));
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(),
                layoutManager.getOrientation() );
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        holder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();
    }

    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

        Context context;
        private List<GetPnrDetailsResponse> bookList;

        public SearchResultAdapter(Context context, List<GetPnrDetailsResponse> bookList) {
            if (bookList == null)
                bookList = new ArrayList<GetPnrDetailsResponse>();
            this.bookList = bookList;
            this.context = context;
        }
        public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.track_mybus_list_view, parent, false);
            // Return a new holder instance
            return new ViewHolder(contactView);
        }
        public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
            GetPnrDetailsResponse book = this.bookList.get(position);

            holder.container.setTag(position);

            holder.VehicleNo.setText(book.getVehicleNo());
            holder.RouteName.setText(book.getRounteName());
            holder.DepartureDateTime.setText(book.getDepartureDateTime());
            holder.LastBusStation.setText(book.getLastBusStation());
            holder.LastArrivalDateTime.setText(book.getLastArrivalDateTime());
            holder.Status.setText(book.getStatus());
            holder.ETA.setText(book.getETA());
            holder.NextLocation.setText(book.getNextLocation());
            vehicleNo =book.getVehicleNo();

            String strDepart = holder.DepartureDateTime.getText().toString();
            if(!strDepart.isEmpty()) {

            if(!book.getDepartureDateTime().equalsIgnoreCase("N/A")) {
                String schedule = book.getDepartureDateTime();
                String fetchdate = schedule.substring(0, 10);
                String year = fetchdate.substring(6, 10);
                String month = fetchdate.substring(3, 5);
                String date = fetchdate.substring(0, 2);
                scheduleDate = year + "-" + month + "-" + date;

            if(vehicleNo.equalsIgnoreCase(""))
            {
                Btnmap.setVisibility(View.GONE);
                CustomisedProgressDialog.STOP_CUST_DIA();
                //hideProgressDialog();
                ActivityUtil.showToast(CurrentTrackBus.this, "Invalid Tripcode");
            }
            else if(vehicleNo != "N/A" && !vehicleNo.equalsIgnoreCase("N/A")&&!vehicleNo.equalsIgnoreCase("")) {
                goNext();

            }else
            {
                Btnmap.setVisibility(View.GONE);
                CustomisedProgressDialog.STOP_CUST_DIA();
                //hideProgressDialog();
                ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
            }
         }
            else
            {
                holder.VehicleNo.setText("N/A");
                holder.RouteName.setText("N/A");
                holder.DepartureDateTime.setText("N/A");
                holder.LastBusStation.setText("N/A");
                holder.LastArrivalDateTime.setText("N/A");
                holder.Status.setText("N/A");
                holder.ETA.setText("N/A");
                holder.NextLocation.setText("N/A");

                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                tracking.trackmybusHistorySearchParam.TrackStatus = "F";
                tracking.trackmybusHistorySearchParam.ModuleName=vehicleNo;
                tracking.trackmybusHistorySearchParam.TrackDate = formattedDate;
                insertTrackingData();
                Btnmap.setVisibility(View.GONE);
            }
         }
            else
            {
                holder.VehicleNo.setText("N/A");
                holder.RouteName.setText("N/A");
                holder.DepartureDateTime.setText("N/A");
                holder.LastBusStation.setText("N/A");
                holder.LastArrivalDateTime.setText("N/A");
                holder.Status.setText("N/A");
                holder.ETA.setText("N/A");
                holder.NextLocation.setText("N/A");

                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                tracking.trackmybusHistorySearchParam.TrackStatus="F";
                tracking.trackmybusHistorySearchParam.ModuleName=vehicleNo;
                tracking.trackmybusHistorySearchParam.TrackDate=formattedDate;
                insertTrackingData();
                Btnmap.setVisibility(View.GONE);
            }
        }

        // Clean all elements of the recycler
        public void clear() {
            bookList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView VehicleNo;
            TextView RouteName;
            TextView DepartureDateTime;
            TextView LastBusStation;
            TextView LastArrivalDateTime;
            TextView Status;
            TextView ETA;
            TextView NextLocation;
            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                VehicleNo = (TextView) itemView.findViewById(R.id.VehicleNo1);
                RouteName = (TextView) itemView.findViewById(R.id.RouteName);
                DepartureDateTime = (TextView) itemView.findViewById(R.id.DepartureDateTime);
                LastBusStation = (TextView) itemView.findViewById(R.id.LastBusStation);
                LastArrivalDateTime = (TextView) itemView.findViewById(R.id.LastArrivalDateTime);
                Status = (TextView) itemView.findViewById(R.id.Status);
                ETA = (TextView) itemView.findViewById(R.id.ETA);
                NextLocation = (TextView) itemView.findViewById(R.id.NextLocation);
            }
        }
    }


    public class ActivityViewHolder{

        RecyclerView recyclerView;
        RecyclerView recyclerViewinfo;
        SwipeRefreshLayout swipeContainer;
        TextView head;

        ActivityViewHolder(Activity activity) {
            head=(TextView) activity.findViewById(R.id.header_text);
            recyclerView = (RecyclerView) activity.findViewById(R.id.track_data_recycler_view);
            recyclerViewinfo = (RecyclerView) activity.findViewById(R.id.track_more_view);
        }
    }

    /***vehicle number ****/

    public void goNext()
    {
        holder.recyclerView.setVisibility(View.VISIBLE);
        CustomisedProgressDialog.SHOW_CUST_DIA(CurrentTrackBus.this, "Please wait loading...");
        getAllBuses();
    }

    private void getAllBuses() {

        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            trackData();
            return null;
        }

        /**Parsing Response data **/

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (resultdata.equalsIgnoreCase("NO DATA AVAILABLE")) {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
                } else{

                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(resultString.toString());
                    JSONArray array = (JSONArray) obj;
                    JSONObject jsonobject = (JSONObject) array.get(0);
                    Log.i(TAG, "JSONOBJ : " + jsonobject.toString());
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

                    currentBusTime =(String) jsonobject.get("LastArrivalDateTime");
                    if(currentBusTime.length()!=0) {
                        String crnttime = currentBusTime;
                        Log.d("crnttime", crnttime);
                        String s1 = crnttime.substring(10, 16);
                       // String s2 = crnttime.substring(2);
                        String bustime = (s1);
                        exactBustTime =bustime;
                    }


                    Log.i(TAG, "vo string : " + getVehicleCurrentStatusResponse.toString());
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            CustomisedProgressDialog.STOP_CUST_DIA();
            //hideProgressDialog();
            recycleviewProcess();
            trackmore_info();
            if(data.isEmpty()){
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                tracking.trackmybusHistorySearchParam.TrackStatus="F";
                tracking.trackmybusHistorySearchParam.ModuleName=vehicleNo;
                tracking.trackmybusHistorySearchParam.TrackDate=formattedDate;
                insertTrackingData();
                Btnmap.setVisibility(View.GONE);
            }
            else{
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                tracking.trackmybusHistorySearchParam.TrackStatus="S";
                tracking.trackmybusHistorySearchParam.ModuleName=vehicleNo;
                tracking.trackmybusHistorySearchParam.TrackDate=formattedDate;
                insertTrackingData();
                CustomisedProgressDialog.STOP_CUST_DIA();
                Btnmap.setVisibility(View.VISIBLE);
            }
        }
    }

    /** calling googlemap trackdata method **/

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void trackData() {

        String[] split = trackBus.split(",");

        String SOAP_ACTION = "http://tempuri.org/GetVehicleCurrentStatus";
        String METHOD_NAME = "GetVehicleCurrentStatus";
        String NAMESPACE = "http://tempuri.org/";
//        String URL = "http://180.150.248.52/GSRTCMobile/Trackingservice.asmx";
        String URL = "http://180.150.248.52/GSRTCMobile/Trackingservice.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("APIUserName", split[0]);
            Request.addProperty("APIPassword", split[1]);
            Request.addProperty("VehicleNo", vehicleNo );
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = sdf.format(c.getTime());
            Request.addProperty("ScheduleDate", scheduleDate.trim());

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            resultdata= String.valueOf(resultString);
            Log.i(TAG, "Result : " + resultString);

        } catch (Exception ex) {
            CustomisedProgressDialog.STOP_CUST_DIA();
        }
    }


    public void recycleviewProcess() {

        ActivityViewHolder holder = new ActivityViewHolder(this);
        SearchResultAdaptermap searchResultAdaptermap = new SearchResultAdaptermap(this,data );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(), layoutManager.getOrientation());
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        holder.recyclerView.setAdapter(searchResultAdaptermap);
        searchResultAdaptermap.notifyDataSetChanged();
    }

    //USING RECYCLER VIEW ADAPTER

    class SearchResultAdaptermap extends RecyclerView.Adapter<SearchResultAdaptermap.ViewHolder> {

        Context context;
        private List<GetVehicleCurrentStatusResponse> data;

        public SearchResultAdaptermap(Context context, List<GetVehicleCurrentStatusResponse> data) {
            if (data == null) data = new ArrayList<GetVehicleCurrentStatusResponse>();
            this.data = data;
            this.context = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.track_mybus_list_view, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }

        public void onBindViewHolder(SearchResultAdaptermap.ViewHolder holder, int position) {
            GetVehicleCurrentStatusResponse book = this.data.get(position);

            holder.container.setTag(position);

            if(vehicleNo != null && !vehicleNo.equalsIgnoreCase("")){

                holder.VehicleNo.setText(vehicleNo);
            }else
            {
                holder.VehicleNo.setText("N/A");
            }

            holder.RouteName.setText(book.getRouteName());
            holder.DepartureDateTime.setText(book.getDepartureDateTime());
            holder.LastBusStation.setText(book.getLastBusStation());
            holder.LastArrivalDateTime.setText(book.getLastArrivalDateTime());
            holder.Status.setText(book.getStatus());
            holder.ETA.setText(book.getETA());
            holder.NextLocation.setText(book.getNextLocation());

            CustomisedProgressDialog.STOP_CUST_DIA();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView VehicleNo;
            TextView Vehino;
            TextView RouteName;
            TextView DepartureDateTime;
            TextView LastBusStation;
            TextView LastArrivalDateTime;
            TextView Status;
            TextView ETA;
            TextView NextLocation;
            TextView txtVehicleNo,txtRouteName,txtDepartureDateTime,txtLastBusStation,txtLastArrivalDateTime,txtStatus,
                    txtETA,txtNextLocation,txtdata;

            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                VehicleNo = (TextView) itemView.findViewById(R.id.VehicleNo1);
                Vehino = (TextView) itemView.findViewById(R.id.txtvehino);
                RouteName = (TextView) itemView.findViewById(R.id.RouteName);
                DepartureDateTime = (TextView) itemView.findViewById(R.id.DepartureDateTime);
                LastBusStation = (TextView) itemView.findViewById(R.id.LastBusStation);
                LastArrivalDateTime = (TextView) itemView.findViewById(R.id.LastArrivalDateTime);
                Status = (TextView) itemView.findViewById(R.id.Status);
                ETA = (TextView) itemView.findViewById(R.id.ETA);
                NextLocation = (TextView) itemView.findViewById(R.id.NextLocation);

                txtVehicleNo = (TextView) itemView.findViewById(R.id.txtvehino);
                txtRouteName = (TextView) itemView.findViewById(R.id.txtrotename);
                txtDepartureDateTime = (TextView) itemView.findViewById(R.id.txtdeparture);
                txtLastBusStation = (TextView) itemView.findViewById(R.id.txtlastbusstation);
                txtLastArrivalDateTime = (TextView) itemView.findViewById(R.id.txtlastarrival);
                txtStatus = (TextView) itemView.findViewById(R.id.txtstatus);
                txtETA = (TextView) itemView.findViewById(R.id.txteta);
                txtNextLocation = (TextView) itemView.findViewById(R.id.txtNextLocation);
                txtdata = (TextView) itemView.findViewById(R.id.txtdata);

                if(textlang.equalsIgnoreCase("gujarati")){

                    txtVehicleNo.setText(R.string.trackopt_vehicleno);
                    txtRouteName.setText(R.string.trackopt_route_name);
                    txtDepartureDateTime.setText(R.string.trackopt_depart_dt_time);
                    txtLastBusStation.setText(R.string.trackopt_last_bus_stn);
                    txtLastArrivalDateTime.setText(R.string.trackopt_last_arrv_dttime);
                    txtStatus.setText(R.string.trackopt_status);
                    txtETA.setText(R.string.trackopt_eta);
                    txtNextLocation.setText(R.string.trackopt_next_locat);
                    txtdata.setText(R.string.trackopt_data_text);
                }
            }
        }
    }

    //tracking more info data
    private void trackmore_info() {
        CustomisedProgressDialog.SHOW_CUST_DIA(CurrentTrackBus.this, "Please wait loading...");
        TrackMyBusNetworkUtil.getPickupPointDetailsByPnrNo(tracking, new TrackMyBusNetworkUtil.GetPickupPointDetailsByPnrNoCallback() {

            public void onSuccess(GetPickupPointDetailsByPnrNoResponse[] response) {
                if (response != null) {
                    if(response.length == 0){
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
                    }
                    tracking.getPickupPointDetailsByPnrNoResponses = response;
                    linearLayout.setVisibility(View.VISIBLE);
                    recycleviewProcesspnr();
                } else {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentTrackBus.this, "No Data Available");
                }
            }
            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(CurrentTrackBus.this, e);
            }
        });
    }

    public void recycleviewProcesspnr() {

        ActivityViewHolder holder = new ActivityViewHolder(this);
        SearchResultAdapterpnr searchResultAdapterpnr = new SearchResultAdapterpnr(this, Arrays.asList(tracking.getPickupPointDetailsByPnrNoResponses));
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        holder.recyclerViewinfo.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerViewinfo.getContext(),
                layoutManager.getOrientation() );
        holder.recyclerViewinfo.addItemDecoration(dividerItemDecoration);
        holder.recyclerViewinfo.setAdapter(searchResultAdapterpnr);
        searchResultAdapterpnr.notifyDataSetChanged();
    }

    class SearchResultAdapterpnr extends RecyclerView.Adapter<SearchResultAdapterpnr.ViewHolder> {

        Context context;
        private List<GetPickupPointDetailsByPnrNoResponse> moreinfoList;
        private int lastSelectedPosition = -1;

        public SearchResultAdapterpnr(Context context, List<GetPickupPointDetailsByPnrNoResponse> moreinfoList ) {
            if (moreinfoList == null)
                moreinfoList = new ArrayList<GetPickupPointDetailsByPnrNoResponse>();
            this.moreinfoList = moreinfoList;
            this.context = context;
        }

        public SearchResultAdapterpnr.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.track_more_info, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }

        public void onBindViewHolder(SearchResultAdapterpnr.ViewHolder holder, int position) {
            CustomisedProgressDialog.STOP_CUST_DIA();
            GetPickupPointDetailsByPnrNoResponse moreinfo = this.moreinfoList.get(position);
            holder.container.setTag(position);

            holder.DayType.setText(moreinfo.getDayType());
            holder.PickPointName.setText(moreinfo.getPickPointName());
            holder.PickupPointTime.setText(moreinfo.getPickupPointTime());
            holder.selectionState.setChecked(lastSelectedPosition == position);

            GetPickupPointDetailsByPnrNoResponse getPickupPointDetailsByPnrNoResponse =new GetPickupPointDetailsByPnrNoResponse();
            getPickupPointDetailsByPnrNoResponse.setPickPointName(moreinfo.getPickPointName());
            pickuppoints.add(getPickupPointDetailsByPnrNoResponse);

            if(exactBustTime!=null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    busTime = simpleDateFormat.parse(exactBustTime);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                try {
                    prevTime = simpleDateFormat.parse(moreinfo.getPickupPointTime());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                if (busTime.after(prevTime)) {
                    if (moreinfo.getDayType().equalsIgnoreCase("SAME DAY")) {
                        holder.selectionState.setChecked(true);
                    } else {
                        holder.selectionState.setChecked(false);
                    }
                } else if (busTime.equals(prevTime)) {
                    if (moreinfo.getDayType().equalsIgnoreCase("SAME DAY")) {
                        holder.selectionState.setChecked(true);
                    } else {
                        holder.selectionState.setChecked(false);
                    }
                } else {
                    holder.selectionState.setChecked(false);
                }
            }
        }

        @Override
        public int getItemCount() {
            return moreinfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView DayType;
            TextView PickPointName;
            TextView PickupPointTime;
            RadioButton selectionState;

            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                DayType = (TextView) itemView.findViewById(R.id.datatype);
                PickPointName = (TextView) itemView.findViewById(R.id.pickname);
                PickupPointTime = (TextView) itemView.findViewById(R.id.picktime);
                selectionState = (RadioButton) itemView.findViewById(R.id.pick_select);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(bookType.equalsIgnoreCase("ON_ROUTE")) {

            Intent i = new Intent(CurrentTrackBus.this, CurrentBookingSearchResultActivity.class);
            Bundle data = new Bundle();
            data.putString("textlang", textlang);
            data.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
            i.putExtras(data);
            startActivity(i);

        }else{

            Intent i = new Intent(CurrentTrackBus.this, Timetable_Search.class);
            Bundle data = new Bundle();
            data.putString("textlang", textlang);
            i.putExtras(data);
            startActivity(i);
        }
    }


    private void insertTrackingData() {

        //CustomisedProgressDialog.SHOW_CUST_DIA(CurrentTrackBus.this, "please wait loading...");

       TrackMyBusNetworkUtil.GetTrackingDataInsert(tracking, new TrackMyBusNetworkUtil.GetTrackingDataInsertCallback() {
            public void onSuccess(String response) {
                if (response != null)
                {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(CurrentTrackBus.this, String.valueOf(response));

                } else
                {

                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(CurrentTrackBus.this, e);
            }
        });
    }
}


