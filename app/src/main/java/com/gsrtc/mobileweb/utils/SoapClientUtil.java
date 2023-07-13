package com.gsrtc.mobileweb.utils;

import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.BusCurrentService;
import com.gsrtc.mobileweb.models.BusGetRouteDetails;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.BusSummaryDetails;
import com.gsrtc.mobileweb.models.ConfirmCurrentSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SaveCurrentBookingDetailsTemporarilyResponse;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.models.SeatRow;
import com.gsrtc.mobileweb.models.ServiceSeatDetails;
import com.gsrtc.mobileweb.models.trackmybus.Vehicle;
import com.gsrtc.mobileweb.models.trackmybus.VehicleList;
import com.gsrtc.mobileweb.utils.net.NetworkException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ajith on 4/15/2017.
 */

public class SoapClientUtil {

    protected static final String NAMESPACE = "com.gsrtc.prws.service.bo";
    protected static final String SOAP_ACTION = "";

     /****** LOCAL URL ******/
    //protected static final String URL = "http://111.93.131.113:7070/GSRTCWS2/reservationservice?wsdl";

     /******* LIVE URL *******/
    protected static final String URL = AppConstants.server_url + "GSRTCWSAnd/reservationservice?wsdl";

    protected static final String username = "radwsnic";
    protected static final String password = "radwsnic";

    @Deprecated
    @Nullable

    public static void fetchSoapObjectList(final String METHOD_NAME, final List<Pair<String, String>> properties, final BaseCallback mCallback) {
        AsyncTask<Void, Void, Vector<SoapObject>> task = new AsyncTask<Void, Void, Vector<SoapObject>>() {

            @Override
            protected void onPostExecute(Vector<SoapObject> response) {
                mCallback.onSuccess(response);
            }

            @Override
            protected Vector<SoapObject> doInBackground(Void... params) {

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                SoapObject request = new SoapObject("http://schemas.xmlsoap.org/soap/envelope/", "com:" + METHOD_NAME);
                request.addAttribute("xmlns:com", "com.gsrtc.prws.service.bo");
                SoapObject ob = new SoapObject("", "arg0");
                if (properties != null) {
                    for (Pair<String, String> property : properties) {
                        ob.addProperty("", property.first, property.second);
                    }
                    request.addSoapObject(ob);
                }
                envelope.implicitTypes = true;
                // Enable the below property if consuming .Net service
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                MarshalBase64 marshal = new MarshalBase64();
                marshal.register(envelope);
                envelope.setAddAdornments(false);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(AppConstants.server_url+"GSRTCWSAnd/reservationservice");

                try {
                    List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((username + ":" + password).getBytes())));

                    androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
                    Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                    //response.getProperty(0).toString();
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        else
            task.execute((Void[]) null);
    }



    @Deprecated
    @Nullable
    public static void fetchSoapObject(final String METHOD_NAME, final List<Pair<String, String>> properties, final BaseCallback mCallback) {
        AsyncTask<Void, Void, SoapPrimitive> task = new AsyncTask<Void, Void, SoapPrimitive>() {

            @Override
            protected void onPostExecute(SoapPrimitive response) {
                mCallback.onSuccess(response);
            }

            @Override
            protected SoapPrimitive doInBackground(Void... params) {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                // Set all input params
                //request.addProperty("property", "value");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                if (properties != null) {
                    for (Pair<String, String> property : properties) {
                        request.addProperty(property.first, property.second);
                    }
                }
                // Enable the below property if consuming .Net service
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((username + ":" + password).getBytes())));

                    androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    //response.getProperty(0).toString();
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        else
            task.execute((Void[]) null);
    }



    public static void fetchResponseString(final String METHOD_NAME, final List<Pair<String, String>> arg0, final List<Pair<String, String>> arg1, final BaseDefaultHttpCallback mCallback) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... distance) {
                String strresponse = "";

                HttpPost httppost = new HttpPost(AppConstants.server_url + "GSRTCWSAnd/reservationservice");
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    BasicHttpResponse send_call_response;

                    StringBuilder arg0String = new StringBuilder();
                    StringBuilder arg1String = new StringBuilder();
                    if (arg0 != null) {
                        if (arg0.size() == 1 && arg0.get(0).first.trim().length() == 0) {
                            arg0String.append("<arg0>" + arg0.get(0).second + "</arg0>\n");
                        } else {
                            arg0String.append("<arg0>\n");
                            for (Pair<String, String> property : arg0) {
                                arg0String.append("<" + property.first + ">" + property.second + "</" + property.first + ">\n");
                            }
                            arg0String.append("</arg0>\n");
                        }

                    }
                    if (arg1 != null) {
                        if (arg1.size() == 1 && arg1.get(0).first.trim().length() == 0) {
                            arg0String.append("<arg1>" + arg1.get(0).second + "</arg1>\n");
                        } else {
                            arg1String.append("<arg1>\n");
                            for (Pair<String, String> property : arg1) {
                                arg1String.append("<" + property.first + ">" + property.second + "</" + property.first + ">\n");
                            }
                            arg1String.append("</arg1>\n");
                        }

                    }
                    String xml1 = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.gsrtc.prws.service.bo\">" +
                            "<x:Header/>\n" +
                            "<x:Body>\n" +
                            "<com:" + METHOD_NAME + ">\n";
                    if (!arg0String.toString().isEmpty()) {
                        xml1 += arg0String.toString();
                    }
                    if (!arg1String.toString().isEmpty()) {
                        xml1 += arg1String.toString();
                    }
                    xml1 += "</com:" + METHOD_NAME + ">\n" +
                            "</x:Body>\n" +
                            "</x:Envelope>";
                    Log.d("xml1", xml1);
                    httppost.addHeader("Authorization", "Basic cmFkd3NuaWM6cmFkd3NuaWM=");
                    HttpEntity entity = new ByteArrayEntity(xml1.getBytes("UTF-8"));
                    httppost.setEntity(entity);
                    send_call_response = (BasicHttpResponse) client.execute(httppost);
                    strresponse = EntityUtils.toString(send_call_response.getEntity());

                    System.out.println(strresponse);


                } catch (ConnectException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                return strresponse;
            }

            @Override
            protected void onPostExecute(String result) {
                //  System.out.println(result);
                if (result != null) {
                    mCallback.onSuccess(result);
                } else {
                    mCallback.onError(new NetworkException());
                }

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (new String[]{""}));
        else
            task.execute(new String[]{""});
    }


    public static void fetchResponseString(final String METHOD_NAME, final String arg0, final String arg1, final BaseDefaultHttpCallback mCallback) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... distance) {
                String strresponse = "";

                HttpPost httppost = new HttpPost(AppConstants.server_url + "GSRTCWSAnd/reservationservice");
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    BasicHttpResponse send_call_response;

                    StringBuilder arg0String = new StringBuilder(arg0);
                    StringBuilder arg1String = new StringBuilder(arg1);

                    String xml1 = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.gsrtc.prws.service.bo\">" +
                            "<x:Header/>\n" +
                            "<x:Body>\n" +
                            "<com:" + METHOD_NAME + ">\n";
                    if (arg0 != null && !arg0.isEmpty()) {
                        xml1 += arg0String.toString();
                    }
                    if (arg1 != null && !arg1.isEmpty()) {
                        xml1 += arg1String.toString();
                    }
                    xml1 += "</com:" + METHOD_NAME + ">\n" +
                            "</x:Body>\n" +
                            "</x:Envelope>";

                    Log.d("xml1", xml1);
                    httppost.addHeader("Authorization", "Basic cmFkd3NuaWM6cmFkd3NuaWM=");
                    HttpEntity entity = new ByteArrayEntity(xml1.getBytes("UTF-8"));
                    httppost.setEntity(entity);
                    send_call_response = (BasicHttpResponse) client.execute(httppost);
                    strresponse = EntityUtils.toString(send_call_response.getEntity());

                    System.out.println(strresponse);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return strresponse;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && !result.equals("")) {
                    mCallback.onSuccess(result);
                } else {
                    mCallback.onError(new NetworkException());
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (new String[]{""}));
        else
            task.execute(new String[]{""});
    }


    //addresslist
    public static void fetchAddressListAsync(final AddressListCallback mCallback) {
        fetchSoapObjectList("GetAddressPlaceList", null, new BaseCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
                System.out.println(e);
                Log.d("errorPlaces", String.valueOf(e));
            }

            @Override
            public void onSuccess(Vector<SoapObject> responseList) {
                if (responseList != null) {
                    List<Place> placeList = new ArrayList<>();
                    for (SoapObject soapObject : responseList) {
                        Place place = new Place();
                        place.setPlaceCode(soapObject.getProperty("placeCode").toString());
                        place.setPlaceID(soapObject.getProperty("placeID").toString());
                        place.setPlaceName(soapObject.getProperty("placeName").toString());
                        place.setStateCode(soapObject.getProperty("stateCode").toString());
                        place.setStateName(soapObject.getProperty("stateName").toString());
                        placeList.add(place);
                    }
                    if (mCallback != null) {
                        mCallback.onSuccess(placeList);
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onError(new Exception("Null Response"));
                    }
                }
            }

            @Override
            public void onSuccess(SoapPrimitive response) {

            }
        });
    }


    //vehiclelist
    public static void fetchVehicleListAsync(final VehicleListCallback mCallback) {
        fetchSoapObjectList("getVehicleDetailsList", null, new BaseCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(Vector<SoapObject> responseList) {
                if (responseList != null) {
                    List<Vehicle> vehicList = new ArrayList<>();
                    for (SoapObject soapObject : responseList) {
                        Vehicle num = new Vehicle();
                        num.setVehicleNo(soapObject.getProperty("vehicleNo").toString());

                        vehicList.add(num);
                    }
                    if (mCallback != null) {
                        mCallback.onSuccess(vehicList);
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onError(new Exception("Null Response"));
                    }
                }
            }

            @Override
            public void onSuccess(SoapPrimitive response) {

            }
        });
    }


    //datefetch
    public static void getMaxValidDateForAdvResvBooking(final GetMaxValidDateForAdvResvBookingCallback mCallback) {

        fetchSoapObject("GetMaxValidDateForAdvResvBooking", null, new BaseCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
                System.out.println(e);
                Log.d("errorPlace", String.valueOf(e));
            }

            @Override
            public void onSuccess(Vector<SoapObject> responseList) {
            }

            @Override
            public void onSuccess(SoapPrimitive response) {
                if (response != null) {
                    String[] responseSplit = response.toString().split("-");
                    if (responseSplit.length == 3) {
                        int year = Integer.parseInt(responseSplit[0]);
                        int month = Integer.parseInt(responseSplit[1]);
                        int date = Integer.parseInt(responseSplit[2]);
                        if (mCallback != null) {
                            mCallback.onSuccess(year, month, date);
                        }
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onError(new Exception("Null Response"));
                    }
                }
            }
        });
    }


    //servicedetails
    public static void getAvailableServiceDetails(SearchParams searchParams, boolean returnJourney, final GetAvailableServiceDetailsCallback mCallback) {

        String journeyDate = (!returnJourney) ? searchParams.getOnWardDate() : searchParams.getReturnDate();
        String startPlaceId = (!returnJourney) ? searchParams.getFromLocation().getPlaceID() : searchParams.getToLocation().getPlaceID();
        String endPlaceId = (!returnJourney) ? searchParams.getToLocation().getPlaceID() : searchParams.getFromLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();
        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("userID", AppConstants.CREDENTIALS.USER_ID));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        properties.add(new Pair<String, String>("startPlaceID", startPlaceId));
        properties.add(new Pair<String, String>("endPlaceID", endPlaceId));
        properties.add(new Pair<String, String>("journeyToTime", "23:59"));
        properties.add(new Pair<String, String>("journeyFromTime", "00:00"));
      /*  if (searchParams.getDivyangBooking() != null || !searchParams.getDivyangBooking().equalsIgnoreCase("Divyang")) {
            properties.add(new Pair<String, String>("type", searchParams.getDivyangBooking()));
        }else{
            properties.add(new Pair<String, String>("type",searchParams.getDivyangBooking()));
        }*/

        properties.add(new Pair<String, String>("journeyDate", journeyDate));
        properties.add(new Pair<String, String>("noOfSeats", "" + (searchParams.getNumOfPassengers())));
        properties.add(new Pair<String, String>("serviceClass", "0"));
        properties.add(new Pair<String, String>("totFemales", String.valueOf(searchParams.getAdultFemaleCount())));
        properties.add(new Pair<String, String>("totMales", String.valueOf(searchParams.getAdultCount())));
        properties.add(new Pair<String, String>("advanceOrCurrentBooking", "ADVANCE"));
        fetchResponseString("GetAvailableServiceDetails", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response", response);
                List<BusService> serviceList = new ArrayList<BusService>();
                JSONObject requiredResponse = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetAvailableServiceDetails");
                    serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONArray("GetAvailableServiceDetails").toString(), new TypeReference<List<BusService>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    try {
                        serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONObject("GetAvailableServiceDetails").toString(), new TypeReference<List<BusService>>() {
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(serviceList);
            }
        });
    }

    //Local_Trip_time_table_service_details
    public static void getLocalTimeTableService(SearchParams searchParams, final GetLocalTimeTableServiceCallback mCallback) {

        String startPlaceId = searchParams.getFromLocation().getPlaceID();
        String endPlaceId =  searchParams.getToLocation().getPlaceID();

        String arg0 = "<arg0>" + startPlaceId +"</arg0> ";
        String arg1 = "<arg1>" + endPlaceId +"</arg1> ";

        fetchGPSResponseString("GetLocalTripsAvailabilityList", arg0,arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response", response);
                List<BusService> serviceList = new ArrayList<BusService>();
                JSONObject requiredResponse = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetLocalTripsAvailabilityList");
                    serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONArray("GetLocalTripsAvailabilityList").toString(), new TypeReference<List<BusService>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    try {
                        serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONObject("GetLocalTripsAvailabilityList").toString(), new TypeReference<List<BusService>>() {
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(serviceList);
            }
        });
    }

    //current_service_details
    public static void getAvailableCurrentServiceDetails(SearchParamsCurrent searchParams, final GetAvailableCurrentServiceDetailsCallback mCallback) {

        String startPlaceId = searchParams.getFromLocation().getPlaceID();
        String endPlaceId = searchParams.getToLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();
        properties.add(new Pair<String, String>("adultTicketFare", "0"));
        properties.add(new Pair<String, String>("childTicketFare", "0"));
        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("createdBy", AppConstants.CREDENTIALS.USER_ID));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        properties.add(new Pair<String, String>("startPlaceID", startPlaceId));
        properties.add(new Pair<String, String>("endPlaceID", endPlaceId));
        properties.add(new Pair<String, String>("totFemales", "0"));
        properties.add(new Pair<String, String>("totMales", String.valueOf(searchParams.getAdultCount())));
        fetchResponseString("GetCurrentServiceDetails", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response Current", response);
                List<BusCurrentService> serviceList = new ArrayList<BusCurrentService>();

                JSONObject requiredResponse = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetCurrentServiceDetails");
                    Log.d("JsonResp : ", requiredResponse.toString());
                    serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONArray("GetCurrentServiceDetails").toString(), new TypeReference<List<BusCurrentService>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    try {
                        serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONObject("GetCurrentServiceDetails").toString(), new TypeReference<List<BusCurrentService>>() {
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(serviceList);
            }
        });
    }

    //current_summary_details
    public static void getSummaryDetails(SearchParamsCurrent searchParams, final GetCurrentTotalFareDetailsOfTicketCallback mCallback) {

        String startPlaceId = searchParams.getFromLocation().getPlaceID();
        String endPlaceId = searchParams.getToLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();

        properties.add(new Pair<String, String>("adultFare", searchParams.getAdultFareGetCurrentServiceDetails()));
        properties.add(new Pair<String, String>("adultFemale", "0"));
        properties.add(new Pair<String, String>("adultMale", String.valueOf(searchParams.getAdultCount())));
        properties.add(new Pair<String, String>("adultMales", String.valueOf(searchParams.getAdultCount())));
        properties.add(new Pair<String, String>("adultTicketFare", searchParams.getAdultTicketFareGetCurrentServiceDetails()));

        if(searchParams.getChildrenCount() != 0){

            properties.add(new Pair<String, String>("childFare", searchParams.getChildFareGetCurrentServiceDetails()));
        }else {
            properties.add(new Pair<String, String>("childFare", "0"));
        }

        properties.add(new Pair<String, String>("childFemale", "0"));
        properties.add(new Pair<String, String>("childMale", String.valueOf(searchParams.getChildrenCount())));
        properties.add(new Pair<String, String>("childMales", String.valueOf(searchParams.getChildrenCount())));
        properties.add(new Pair<String, String>("childTicketFare", "0"));
        properties.add(new Pair<String, String>("classId", searchParams.getClassIDGetCurrentServiceDetails()));
        properties.add(new Pair<String, String>("concessionID", "79"));
        properties.add(new Pair<String, String>("concessionTypeId", "79"));
        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("createdBy", AppConstants.CREDENTIALS.USER_ID));
        properties.add(new Pair<String, String>("endPlaceID", endPlaceId));
        properties.add(new Pair<String, String>("journeyDate", searchParams.getJourneyDateGetCurrentServiceDetails()));
        properties.add(new Pair<String, String>("onewayOrReturnTrip", "O"));
        properties.add(new Pair<String, String>("pickServiceId", searchParams.getPickServiceIdGetCurrentServiceDetails()));
        properties.add(new Pair<String, String>("startPlaceID", startPlaceId));
        properties.add(new Pair<String, String>("totalNumberOfSeats", String.valueOf(searchParams.getAdultCount() + searchParams.getChildrenCount())));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));


        fetchResponseString("GetCurrentTotalFareDetailsOfTicket", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                //System.out.println(response);
                Log.d("response Current", response);
                BusSummaryDetails summaryDetails = null;

                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetCurrentTotalFareDetailsOfTicket");
                    JSONObject json = requiredResponse.getJSONObject("GetCurrentTotalFareDetailsOfTicket");
                    summaryDetails = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<BusSummaryDetails>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(summaryDetails);
            }
        });
    }

    //current_savetempdetails
    public static void saveTempDetails(SearchParamsCurrent searchParams, String isCard, final SaveCurrentBookingDetailsTemporarilyCallback mCallback) {

        String startPlaceId = searchParams.getFromLocation().getPlaceID();
        String endPlaceId = searchParams.getToLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();
        properties.add(new Pair<String, String>("accidentReliefFund", searchParams.getAccidentReliefFund_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("adultFare", searchParams.getAdultFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("adultFemale", searchParams.getAdultFemale_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("adultMale", searchParams.getAdultMale_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("adultMales", searchParams.getAdultMales_GetCurrentTotalFareDetailsOfTicket()));

        properties.add(new Pair<String, String>("adultTicketFare", searchParams.getAdultTicketFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("basicFare", searchParams.getBasicFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("bridgeFee", searchParams.getBridgeFee_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("childFare", searchParams.getChildFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("childFemale", searchParams.getChildFemale_GetCurrentTotalFareDetailsOfTicket()));

        properties.add(new Pair<String, String>("childMale", searchParams.getChildMale_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("childMales", searchParams.getChildMales_GetCurrentTotalFareDetailsOfTicket()));

        properties.add(new Pair<String, String>("childTicketFare", searchParams.getChildTicketFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("classID", searchParams.getClassId_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("classId", searchParams.getClassId_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("concessionID", searchParams.getConcessionID_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("createdBy", AppConstants.CREDENTIALS.USER_ID));
        properties.add(new Pair<String, String>("discountPercentage", searchParams.getDiscountPercentage_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("discounts", searchParams.getDiscounts_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("emailId", searchParams.getPassengerEmail()));
        properties.add(new Pair<String, String>("endPlaceID", endPlaceId));
        properties.add(new Pair<String, String>("entryFee", searchParams.getEntryFee_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("fareWithOutbasic", searchParams.getFareWithOutbasic_GetCurrentTotalFareDetailsOfTicket()));

        properties.add(new Pair<String, String>("gatewayLookupID", isCard));
        properties.add(new Pair<String, String>("gatewayStatus", "0"));
        properties.add(new Pair<String, String>("journeyDate", searchParams.getJourneyDate_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("modifyBy", "2800"));
        properties.add(new Pair<String, String>("otherLevies", searchParams.getOtherLevies_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("passengerName", searchParams.getPassengerName()));
        properties.add(new Pair<String, String>("phoneNumber", searchParams.getPassengerMobile()));
        properties.add(new Pair<String, String>("pickServiceId", searchParams.getPickServiceId_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("reservationFee", searchParams.getReservationFee_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("startPlaceID", startPlaceId));
        properties.add(new Pair<String, String>("totalFare", searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("userFee", searchParams.getUserFee_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("tollFee", searchParams.getTollFee_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        properties.add(new Pair<String, String>("weekdayConcession", searchParams.getWeekdayConcession_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("totalNumberOfSeats", String.valueOf(searchParams.getAdultCount() + searchParams.getChildrenCount())));


        fetchResponseString("SaveCurrentBookingDetailsTemporarily", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                //System.out.println(response);
                Log.d("response Current", response);
                SaveCurrentBookingDetailsTemporarilyResponse summaryDetails = null;

                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "SaveCurrentBookingDetailsTemporarily");
                    JSONObject json = requiredResponse.getJSONObject("SaveCurrentBookingDetailsTemporarily");
                    summaryDetails = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<SaveCurrentBookingDetailsTemporarilyResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(summaryDetails);

            }
        });
    }

    public static void conformCurrentSeatBooking(SearchParamsCurrent searchParams,  final ConfirmCurrentSeatBookingAndroidCallback mCallback) {

        String startPlaceId = searchParams.getFromLocation().getPlaceID();
        String endPlaceId = searchParams.getToLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();

        properties.add(new Pair<String, String>("bookingTransactionID", searchParams.getBookingTransactionID()));

        properties.add(new Pair<String, String>("gatewayLookupID", searchParams.getGatewayLookupId()));
        properties.add(new Pair<String, String>("childTicketFare", "0"));
        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("createdBy", AppConstants.CREDENTIALS.USER_ID));
        properties.add(new Pair<String, String>("totalFare", searchParams.getTotalFare_GetCurrentTotalFareDetailsOfTicket()));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        properties.add(new Pair<String, String>("ksrtcRefNo", searchParams.getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse()));
        properties.add(new Pair<String, String>("pnrNumber", searchParams.getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse()));

        fetchResponseString("ConfirmCurrentSeatBookingAndroid", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                //System.out.println(response);
                Log.d("response Current", response);
                ConfirmCurrentSeatBookingAndroidResponse summaryDetails = null;

                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmCurrentSeatBookingAndroid");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmCurrentSeatBookingAndroid");
                    summaryDetails = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmCurrentSeatBookingAndroidResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(summaryDetails);
            }
        });
    }

    //current_routedetails
    public static void getRouteDetails(SearchParamsCurrent searchParams, String PickServiceId, final GetRouteDetailsCallback mCallback) {

        List<Pair<String, String>> properties = new ArrayList<>();

        properties.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        properties.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        properties.add(new Pair<String, String>("pickServiceId", PickServiceId));
        properties.add(new Pair<String, String>("serviceClass", "0"));
        properties.add(new Pair<String, String>("totFemales", String.valueOf(searchParams.getChildrenCount())));
        properties.add(new Pair<String, String>("totMales", String.valueOf(searchParams.getAdultCount())));

        fetchResponseString("GetCurrentServicePickupPoints", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response Current", response);
                List<BusGetRouteDetails> serviceList = new ArrayList<BusGetRouteDetails>();
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetCurrentServicePickupPoints");
                    Log.d("JsonResp : ",requiredResponse.toString());
                    serviceList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONArray("GetCurrentServicePickupPoints").toString().toString(), new TypeReference<List<BusGetRouteDetails>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(serviceList);
            }
        });
    }

    public static void getLayoutDetailByLayoutID(String layoutId, final GetLayoutDetailByLayoutIDCallback mCallback) {
        List<Pair<String, String>> arg0 = new ArrayList<>();
        arg0.add(new Pair<String, String>("", layoutId));
        List<Pair<String, String>> arg1 = new ArrayList<>();
        arg1.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        arg1.add(new Pair<String, String>("userId", AppConstants.CREDENTIALS.USER_ID));
        arg1.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        fetchResponseString("GetLayoutDetailByLayoutID", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                List<SeatRow> seatRowList = new ArrayList<SeatRow>();
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetLayoutDetailByLayoutID");
                    JSONArray jsonArray = requiredResponse.getJSONArray("GetLayoutDetailByLayoutID");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SeatRow row = SeatRow.fromJsonObject(jsonArray.getJSONObject(i));
                        if (row != null) {
                            seatRowList.add(row);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(seatRowList);
            }
        });
    }


    public static void getServiceSeatDetails(BusService busService, SearchParams params,String methodName, final GetServiceSeatDetailsCallback mCallback) {
        List<Pair<String, String>> arg0 = new ArrayList<>();
        arg0.add(new Pair<String, String>("serviceID", busService.getServiceID().split(",")[0].trim()));
        arg0.add(new Pair<String, String>("startPlaceID", busService.getStartPlaceID()));
        arg0.add(new Pair<String, String>("endPlaceID", busService.getEndPlaceID()));
        arg0.add(new Pair<String, String>("totFemales", "" + params.getAdultFemaleCount()));
        arg0.add(new Pair<String, String>("totMales", "" + params.getAdultCount()));
        arg0.add(new Pair<String, String>("userID", AppConstants.CREDENTIALS.USER_ID));
        arg0.add(new Pair<String, String>("journeyDate", busService.getJourneyDate()));
        arg0.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        arg0.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        arg0.add(new Pair<String, String>("viaPlaces", busService.getViaPlaces()));

        fetchResponseString(methodName, arg0, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                ServiceSeatDetails serviceSeatDetails = new ServiceSeatDetails();
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, methodName);
                    JSONArray jsonArray = requiredResponse.getJSONArray(methodName);
                    if (jsonArray.getString(0).trim().length() > 0)
                        serviceSeatDetails.setAvailableSeats(jsonArray.getString(0).split(","));
                    if (jsonArray.getString(1).trim().length() > 0)
                        serviceSeatDetails.setBookedSeats(jsonArray.getString(1).split(","));
                    if (jsonArray.getString(2).trim().length() > 0)
                        serviceSeatDetails.setAvailableLadiesSeats(jsonArray.getString(2).split(","));
                    if (jsonArray.getString(3).trim().length() > 0)
                        serviceSeatDetails.setConductorSeats(jsonArray.getString(3).split(","));
                    if (jsonArray.getString(4).trim().length() > 0)
                        serviceSeatDetails.setBookedLadiesSeats(jsonArray.getString(4).split(","));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(serviceSeatDetails);
            }
        });
    }


    public static void fetchGPSResponseString(final String METHOD_NAME, final String arg0, final String arg1, final BaseDefaultHttpCallback mCallback) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... distance) {
                String strresponse = "";
                //HttpPost httppost = new HttpPost("http://111.93.131.113:7070/GSRTCWS2/reservationservice");
                //HttpPost httppost = new HttpPost("http://192.168.222.131:8080/GSRTCWSAnd/reservationservice");
                HttpPost httppost = new HttpPost(AppConstants.server_url + "GSRTCWSAnd/reservationservice");
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    BasicHttpResponse send_call_response;

                    StringBuilder arg0String = new StringBuilder(arg0);
                    StringBuilder arg1String = new StringBuilder(arg1);

                    String xml1 = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.gsrtc.prws.service.bo\">" +
                            "<x:Header/>\n" +
                            "<x:Body>\n" +
                            "<com:" + METHOD_NAME + ">\n";
                    if (arg0 != null && !arg0.isEmpty()) {
                        xml1 += arg0String.toString();
                    }
                    if (arg1 != null && !arg1.isEmpty()) {
                        xml1 += arg1String.toString();
                    }
                    xml1 += "</com:" + METHOD_NAME + ">\n" +
                            "</x:Body>\n" +
                            "</x:Envelope>";

                    Log.d("xml1", xml1);
                    httppost.addHeader("Authorization", "Basic cmFkd3NuaWM6cmFkd3NuaWM=");
                    HttpEntity entity = new ByteArrayEntity(xml1.getBytes("UTF-8"));
                    httppost.setEntity(entity);
                    send_call_response = (BasicHttpResponse) client.execute(httppost);
                    strresponse = EntityUtils.toString(send_call_response.getEntity());

                    System.out.println(strresponse);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return strresponse;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && !result.equals("")) {
                    mCallback.onSuccess(result);
                } else {
                    mCallback.onError(new NetworkException());
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (new String[]{""}));
        else
            task.execute(new String[]{""});
    }

    public static void fetchRetrivetxnResponseString(final String METHOD_NAME, final String arg0, final String arg1, final BaseDefaultHttpCallback mCallback) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... distance) {
                String strresponse = "";
                //HttpPost httppost = new HttpPost("http://111.93.131.113:7070/GSRTCWSAnd/reservationservice");
                //HttpPost httppost = new HttpPost("http://192.168.222.114:7070/GSRTCWSAnd/reservationservice");
                HttpPost httppost = new HttpPost(AppConstants.server_url + "GSRTCWSAnd/reservationservice");
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    BasicHttpResponse send_call_response;

                    StringBuilder arg0String = new StringBuilder(arg0);
                    StringBuilder arg1String = new StringBuilder(arg1);

                    String xml1 = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.gsrtc.prws.service.bo\">" +
                            "<x:Header/>\n" +
                            "<x:Body>\n" +
                            "<com:" + METHOD_NAME + ">\n";
                    if (arg0 != null && !arg0.isEmpty()) {
                        xml1 += arg0String.toString();
                    }
                    if (arg1 != null && !arg1.isEmpty()) {
                        xml1 += arg1String.toString();
                    }
                    xml1 += "</com:" + METHOD_NAME + ">\n" +
                            "</x:Body>\n" +
                            "</x:Envelope>";

                    Log.d("xml1", xml1);
                    httppost.addHeader("Authorization", "Basic cmFkd3NuaWM6cmFkd3NuaWM=");
                    HttpEntity entity = new ByteArrayEntity(xml1.getBytes("UTF-8"));
                    httppost.setEntity(entity);
                    send_call_response = (BasicHttpResponse) client.execute(httppost);
                    strresponse = EntityUtils.toString(send_call_response.getEntity());

                    System.out.println(strresponse);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return strresponse;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && !result.equals("")) {
                    mCallback.onSuccess(result);
                } else {
                    mCallback.onError(new NetworkException());
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (new String[]{""}));
        else
            task.execute(new String[]{""});
    }


    public static void getVehicleNoDetails(SearchParams searchParams, final GetVehicleNoDetailsCallback mCallback) {

        String startPlaceId =searchParams.getFromLocation().getPlaceID();
        String endPlaceId = searchParams.getToLocation().getPlaceID();

        List<Pair<String, String>> properties = new ArrayList<>();

        properties.add(new Pair<String, String>("fromPlaceID", startPlaceId));
        properties.add(new Pair<String, String>("toPlaceID", endPlaceId));

        fetchResponseString("GetVechileNosByPlaceID", properties, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response", response);
                List<VehicleList> vehiNoList = new ArrayList<VehicleList>();
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetVechileNosByPlaceID");
                    vehiNoList = JacksonUtil.getMapper().readValue(requiredResponse.getJSONArray("GetVechileNosByPlaceID").toString().toString(), new TypeReference<List<VehicleList>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(vehiNoList);
            }
        });
    }



    public static void ConfirmAdvSeatBooking(Booking booking, final ConfirmAdvSeatBookingCallback mCallback) {
        mCallback.onSuccess();
    }

    public interface ConfirmAdvSeatBookingCallback {
        void onSuccess();

        void onError();
    }

    public interface GetServiceSeatDetailsCallback {
        void onSuccess(ServiceSeatDetails serviceSeatDetails);

        void onError(Exception e);
    }

    public interface GetLayoutDetailByLayoutIDCallback {
        void onSuccess(List<SeatRow> seatRowList);

        void onError(Exception e);
    }

    public interface GetAvailableServiceDetailsCallback {
        void onSuccess(List<BusService> serviceList);

        void onError(Exception e);
    }

    public interface GetLocalTimeTableServiceCallback {
        void onSuccess(List<BusService> serviceList);

        void onError(Exception e);
    }

    public interface GetVehicleNoDetailsCallback {
        void onSuccess(List<VehicleList> vehiNoList);

        void onError(Exception e);
    }

    public interface GetAvailableCurrentServiceDetailsCallback {
        void onSuccess(List<BusCurrentService> serviceList);

        void onError(Exception e);
    }

    public interface GetCurrentTotalFareDetailsOfTicketCallback {
        void onSuccess(BusSummaryDetails summaryDetails);

        void onError(Exception e);
    }

    public interface SaveCurrentBookingDetailsTemporarilyCallback {
        void onSuccess(SaveCurrentBookingDetailsTemporarilyResponse saveTempDetails);

        void onError(Exception e);
    }

    public interface ConfirmCurrentSeatBookingAndroidCallback {
        void onSuccess(ConfirmCurrentSeatBookingAndroidResponse confirmCurrentSeatBookingAndroidResponse);

        void onError(Exception e);
    }

    public interface GetRouteDetailsCallback {
        void onSuccess(List<BusGetRouteDetails> routeDetailsList);

        void onError(Exception e);
    }

    public interface GetMaxValidDateForAdvResvBookingCallback {
        void onSuccess(int year, int month, int date);

        void onError(Exception e);
    }

    public interface AddressListCallback {
        void onSuccess(List<Place> placeList);

        void onError(Exception e);
    }
    public interface VehicleListCallback {
        void onSuccess(List<Vehicle>vehicList);

        void onError(Exception e);
    }

    public interface BaseCallback {
        void onError(Exception e);

        void onSuccess(Vector<SoapObject> responseList);

        void onSuccess(SoapPrimitive response);
    }

    public interface BaseDefaultHttpCallback {
        void onError(Exception e);

        void onSuccess(String response);
    }

}
