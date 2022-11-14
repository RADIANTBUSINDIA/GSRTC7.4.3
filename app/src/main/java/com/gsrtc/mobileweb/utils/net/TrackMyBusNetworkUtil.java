package com.gsrtc.mobileweb.utils.net;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.trackmybus.GetNearby_location_list;
import com.gsrtc.mobileweb.models.trackmybus.GetPickupPointDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.trackmybus.GetPnrDetailsResponse;
import com.gsrtc.mobileweb.models.trackmybus.Trackmybus;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.JacksonUtil;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.XMLtoJSONResponseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ajith on 4/25/2017.
 */

public class TrackMyBusNetworkUtil extends SoapClientUtil {

    public static void GetPnrDetails(Trackmybus tracking, final TrackMyBusNetworkUtil.GetPnrDetailsCallback mCallback){

        String pnrNo = tracking.trackmybusHistorySearchParam.PnrNo ;
        String vehicleNo =tracking.trackmybusHistorySearchParam.VehicleNo ;
        String pnr,vehicle;
        if(pnrNo!=null && !pnrNo.equalsIgnoreCase("")){
            pnr = pnrNo;
            vehicle ="0";
        }else{
            pnr = "0";
            vehicle =vehicleNo;
        }

        String arg0 = "<arg0> \n" +

                "<counterCode>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<pnrNo>" + pnr + "</pnrNo> \n" +
                "<vehicleNo>" + vehicle + "</vehicleNo> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchGPSResponseString("GetPnrDetails",arg0,arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetPnrDetailsResponse[] responseModel=null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetPnrDetails");
                    Object jsonOb = requiredResponse.get("GetPnrDetails");
                    String jsonString = "";
                    if(jsonOb instanceof JSONObject){
                        jsonString = ((JSONObject)jsonOb).toString();
                    }
                    else if(jsonOb instanceof JSONArray){
                        jsonString = ((JSONArray)jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetPnrDetailsResponse[]>() {});

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public static void GetTripcodeDetails(Trackmybus tracking, final TrackMyBusNetworkUtil.GetTripcodeDetailsCallback mCallback){

        String tripcod = tracking.trackmybusHistorySearchParam.TripCode ;


        String arg0 = "<arg0> \n" +

                "<counterCode>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<tripCode>" +tripcod+ "</tripCode> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchGPSResponseString("GetVechileNosByTripCode",arg0,arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetPnrDetailsResponse[] responseModel=null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetVechileNosByTripCode");
                    Object jsonOb = requiredResponse.get("GetVechileNosByTripCode");
                    String jsonString = "";
                    if(jsonOb instanceof JSONObject){
                        jsonString = ((JSONObject)jsonOb).toString();
                    }
                    else if(jsonOb instanceof JSONArray){
                        jsonString = ((JSONArray)jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetPnrDetailsResponse[]>() {});

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public static void getPickupPointDetailsByPnrNo (Trackmybus tracking, final TrackMyBusNetworkUtil.GetPickupPointDetailsByPnrNoCallback mCallback){

        String tripcod = tracking.trackmybusHistorySearchParam.TripCode ;
        String pnrNo = tracking.trackmybusHistorySearchParam.PnrNo ;
        String vehicleNo =tracking.trackmybusHistorySearchParam.VehicleNo ;
        String status = tracking.trackmybusHistorySearchParam.StatusCode ;
        String pnr,vehicle,tripco;
        if(pnrNo!=null &&vehicleNo.equalsIgnoreCase("GJ-")&&tripcod.equalsIgnoreCase("")) {
            pnr = pnrNo;
            vehicle ="0";
            tripco ="0";
        }else if(pnrNo.equalsIgnoreCase("") &&vehicleNo!=null &&tripcod.equalsIgnoreCase("")) {
            pnr = "0";
            vehicle =vehicleNo;
            tripco ="0";
        }else{
            pnr = "0";
            vehicle ="0";
            tripco =tripcod;
        }

        String arg0 = "<arg0> \n" +

                "<pnrNo>" +pnr+ "</pnrNo> \n" +
                "<status>" +status+ "</status> \n" +
                "<tripCode>" +tripco+ "</tripCode> \n" +
                "<vehicleNo>" +vehicle+ "</vehicleNo> \n" +

                "</arg0> ";
        String arg1 = "";

        fetchGPSResponseString("GetPickupPointDetailsByPnrNo ",arg0,arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetPickupPointDetailsByPnrNoResponse[] responseModel=null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetPickupPointDetailsByPnrNo");
                    Object jsonOb = requiredResponse.get("GetPickupPointDetailsByPnrNo");
                    String jsonString = "";
                    if(jsonOb instanceof JSONObject){
                        jsonString = ((JSONObject)jsonOb).toString();
                    }
                    else if(jsonOb instanceof JSONArray){
                        jsonString = ((JSONArray)jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetPickupPointDetailsByPnrNoResponse[]>() {});

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }


    public static void getNearbylocationDetails (Trackmybus tracking, final TrackMyBusNetworkUtil.getNearbylocationDetailsCallback mCallback){

        String locate_place = tracking.trackmybusHistorySearchParam.PickLocation ;


        String arg0 = "<arg0>" +locate_place +"</arg0> ";
//                "<pnrNo>" +pnr+ "</pnrNo> \n" +
//                "<status>" +status+ "</status> \n" +
//                "<tripCode>" +tripco+ "</tripCode> \n" +
//                "<vehicleNo>" +vehicle+ "</vehicleNo> \n" +


        String arg1 = "";

        fetchGPSResponseString("GetAddressPlaceListforMap",arg0,arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetNearby_location_list[] responseModel=null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetAddressPlaceListforMap");
                    Object jsonOb = requiredResponse.get("GetAddressPlaceListforMap");
                    String jsonString = "";
                    if(jsonOb instanceof JSONObject){
                        jsonString = ((JSONObject)jsonOb).toString();
                    }
                    else if(jsonOb instanceof JSONArray){
                        jsonString = ((JSONArray)jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetNearby_location_list[]>() {});

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }


    public static void GetTrackingDataInsert(Trackmybus tracking, final TrackMyBusNetworkUtil.GetTrackingDataInsertCallback mCallback){

       // public static void GetRetrivetxnPass(BookingCancellation booking, final TicketCancelNetworkUtil.GetRetrivetxnPassCallback mCallback){

            String arg0 = "<arg0> \n" +
                    "<createDateTime>"+tracking.trackmybusHistorySearchParam.TrackDate+"</createDateTime> \n" +
                    "<createdBY>"+AppConstants.CREDENTIALS.USER_ID+"</createdBY> \n" +
                    "<moduleName>"+tracking.trackmybusHistorySearchParam.ModuleName+"</moduleName> \n" +
                    "<status>"+tracking.trackmybusHistorySearchParam.TrackStatus+"</status> \n" +
                    "</arg0> ";
            String arg1 = "";

            fetchRetrivetxnResponseString("GPSRealTimeTracking",arg0,arg1, new BaseDefaultHttpCallback() {
                @Override
                public void onError(Exception e) {
                    mCallback.onError(e);
                }

                @Override
                public void onSuccess(String response) {
                    System.out.println(response);

                    String responseModel=null;
                    try {
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GPSRealTimeTracking");
                        Object jsonOb = requiredResponse.get("GPSRealTimeTracking");
                        String jsonString = String.valueOf(jsonOb);
                        if (jsonString.equalsIgnoreCase("success")) {

                            //responseModel ="Message Sent";
                        }
                        else
                        {
                            //responseModel ="Invalid PNR.NO";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    mCallback.onSuccess(responseModel);
                }
            });
        }

    public interface GetPnrDetailsCallback{
        void onSuccess(GetPnrDetailsResponse[] response);
        void onError(Exception e);
    }

    public interface GetTripcodeDetailsCallback{
        void onSuccess(GetPnrDetailsResponse[] response);
        void onError(Exception e);
    }
    public interface GetPickupPointDetailsByPnrNoCallback{
        void onSuccess(GetPickupPointDetailsByPnrNoResponse[] response);
        void onError(Exception e);
    }
    public interface getNearbylocationDetailsCallback{
        void onSuccess(GetNearby_location_list[] response);
        void onError(Exception e);
    }
    public interface GetTrackingDataInsertCallback
    {
        void onSuccess(String response);
        void onError(Exception e);
    }
}
