package com.gsrtc.mobileweb.utils.net;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gsrtc.mobileweb.models.AddBlockSeatsResponse;
import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.BusService;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.ConfirmAdvSeatBookingResponse;
import com.gsrtc.mobileweb.models.ConfirmRtnSeatBookingAndroidResponse;
import com.gsrtc.mobileweb.models.DivyangDetails;
import com.gsrtc.mobileweb.models.DivyangStore;
import com.gsrtc.mobileweb.models.Divyangresponse;
import com.gsrtc.mobileweb.models.GetAllServicePickupPointsByServiceIDResponse;
import com.gsrtc.mobileweb.models.GetReferenceNumberResponse;
import com.gsrtc.mobileweb.models.GetTotalFareDetailsOfTicketResponse;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.SaveBookingDetailsTemporary;
import com.gsrtc.mobileweb.models.SaveReturnBookingDetailsTemporary;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.trackmybus.IpDetails;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.JacksonUtil;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.XMLtoJSONResponseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ajith on 4/25/2017.
 */
public class AdvanceBookingNetworkUtil extends SoapClientUtil {
    //public static void AddBlockSeats(BusService busService, List<Seat> seatList, SearchParams searchParams, final GetReferenceNumberResponse referenceNumberResponse, final AddBlockSeatsCallback mCallback){

    /***AddBlockSeats***/

    public static void AddBlockSeats(BusService busService, List<Seat> seatList, SearchParams searchParams, final AddBlockSeatsCallback mCallback) {
        List<Pair<String, String>> arg0 = new ArrayList<>();
        arg0.add(new Pair<String, String>("startPlaceID", busService.getStartPlaceID()));
        arg0.add(new Pair<String, String>("counterCode", AppConstants.CREDENTIALS.COUNTER_CODE));
        arg0.add(new Pair<String, String>("classID", busService.getClassID()));
        arg0.add(new Pair<String, String>("userID", AppConstants.CREDENTIALS.USER_ID));
        arg0.add(new Pair<String, String>("userName", AppConstants.CREDENTIALS.USER_NAME));
        arg0.add(new Pair<String, String>("franchiseeUser", "false"));
        arg0.add(new Pair<String, String>("endPlaceID", busService.getEndPlaceID()));
        arg0.add(new Pair<String, String>("layoutID", busService.getServiceID().split(",")[3].trim()));
        for (Seat seat : seatList) {
            arg0.add(new Pair<String, String>("seatNumber", "" + seat.getSeatNo()));
        }
        arg0.add(new Pair<String, String>("journeyDate", busService.getJourneyDate()));
        arg0.add(new Pair<String, String>("serviceID", busService.getServiceID().split(",")[0].trim()));
        arg0.add(new Pair<String, String>("totalNumberOfAdults", "" + searchParams.getAdultCount()));
        arg0.add(new Pair<String, String>("WSRefNo", "0"));
        arg0.add(new Pair<String, String>("createdBy", AppConstants.CREDENTIALS.USER_ID));

        fetchResponseString("AddBlockSeats", arg0, null, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                AddBlockSeatsResponse serviceSeatDetails = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "AddBlockSeats");
                    JSONObject json = requiredResponse.getJSONObject("AddBlockSeats");
                    serviceSeatDetails = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<AddBlockSeatsResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallback.onError(new RuntimeException("Error Occured!"));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(new RuntimeException("Error Occured!"));
                    return;
                }
                mCallback.onSuccess(serviceSeatDetails);
            }
        });
    }

    public interface AddBlockSeatsCallback {
        void onSuccess(AddBlockSeatsResponse serviceSeatDetails);

        void onError(Exception e);
    }


    /***Pickup and Dropoff point***/

    public static void GetAllServicePickupPointsByServiceID(Booking booking, final boolean drop, final GetAllServicePickupPointsByServiceIDCallback mCallback) {
        BusService busService = booking.getBusService();
        Place place = (drop) ? booking.getEndPlace() : booking.getStartPlace();
        String arg0 = "<arg0>\n" +
                "                                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                                <franchiseeUser>false</franchiseeUser>\n" +
                "                                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                                <startPlaceID>" + place.getPlaceCode() + "</startPlaceID>\n" +
                "                                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                        </arg0>";
        String arg1 = "";

        fetchResponseString("GetAllServicePickupPointsByServiceID", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                if (response == null || response.trim().isEmpty()) {
                    mCallback.onEmpty(drop);
                    return;
                }

                // Explicit empty SOAP response check
                if (response.contains("<GetAllServicePickupPointsByServiceIDResponse")
                        && response.contains("/>")) {

                    mCallback.onEmpty(drop);
                    return;
                }

                // Namespace-safe check (IMPORTANT)
                if (response.matches(".*GetAllServicePickupPointsByServiceIDResponse[^>]*\\/>.*")) {
                    mCallback.onEmpty(drop);
                    return;
                }

                JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                JSONObject requiredResponse =
                        XMLtoJSONResponseParser.getRequiredResponse(
                                responseBody,
                                "GetAllServicePickupPointsByServiceID");

                GetAllServicePickupPointsByServiceIDResponse responseModel = null;

                try {
                    Object jsonOb = requiredResponse.get("GetAllServicePickupPointsByServiceID");

                    if (jsonOb instanceof JSONObject) {
                        responseModel = JacksonUtil.getMapper().readValue(
                                ((JSONObject) jsonOb).toString(),
                                new TypeReference<GetAllServicePickupPointsByServiceIDResponse>() {});
                    } else if (jsonOb instanceof JSONArray) {
                        responseModel = JacksonUtil.getMapper().readValue(
                                ((JSONArray) jsonOb).getJSONObject(0).toString(),
                                new TypeReference<GetAllServicePickupPointsByServiceIDResponse>() {});
                    }

                } catch (Exception e) {
                    mCallback.onError(e);
                    return;
                }

                mCallback.onSuccess(responseModel, drop);
            }



        });
    }

    public interface GetAllServicePickupPointsByServiceIDCallback {
        void onSuccess(GetAllServicePickupPointsByServiceIDResponse response, boolean drop);

        void onError(Exception e);
        void onEmpty(boolean drop);
    }


    /***Total FareDetails ***/

    public static void GetTotalFareDetailsOfTicket(Booking booking, Boolean hasReturn, final GetTotalFareDetailsOfTicketCallback mCallback) {

        BusService busService = booking.getBusService();
        String consId;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            consId = "2";
        } else if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            consId = "2";
        } else {
            consId = "1";
        }

        int totalNumAdltsFemale = 0;
        int totaltotalAdlt = 0;
        int totachildmale = 0;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            if (booking.getSearchParams().getAdultFemaleCount() > 0) {
                totachildmale = 0;
                totaltotalAdlt = 0;
                totalNumAdltsFemale = 1;
            } else {
                totachildmale = 0;
                totaltotalAdlt = 1;
                totalNumAdltsFemale = 0;
            }
        } else {
            totachildmale = booking.getSearchParams().getChildrenCount();
            totaltotalAdlt = booking.getSearchParams().getAdultCount();
            totalNumAdltsFemale = booking.getSearchParams().getAdultFemaleCount();
        }


        String arg0 = " <arg0>\n" +
                "                                           <adultFemale>" + totalNumAdltsFemale + "</adultFemale>\n" +
                "                                            <adultMale>" + totaltotalAdlt + "</adultMale>\n" +
                "                                            <childFemale>0</childFemale>\n" +
                "                                            <childMale>" + totachildmale + "</childMale>\n" +
                "                                            <classID>" + busService.getClassID() + "</classID>\n" +
//                "                                            <concessionTypeId>218</concessionTypeId>\n" +
                "                                            <concessionTypeId>" + consId + "</concessionTypeId>\n" +
                "                                            <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                                            <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                                            <endPlaceCode>" + booking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                                            <endPlaceID>" + booking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                                            <franchiseeUser>false</franchiseeUser>\n" +
                "                                            <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                "                                            <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                "                                            <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                "                                            <returnServiceID>0</returnServiceID>\n";
        int totalNumAdlts = 0;
        int totalNumchld = 0;
        int totalSeats = 0;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            totalNumchld = 0;
            totalNumAdlts = 1;
            totalSeats = 1;
            for (int i = 0; i < booking.getSelectedSeats().size() - 1; i++) {
                if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                    arg0 += "      <seatBlockIds>" + booking.getAddBlockSeatsResponse().getSeatBlockIds()[i] + "</seatBlockIds>\n";
                }
            }
        } else {
            totalNumAdlts = (booking.getSearchParams().getAdultCount() + booking.getSearchParams().getAdultFemaleCount());
            totalNumchld = booking.getSearchParams().getChildrenCount();
            totalSeats = booking.getSelectedSeats().size();
            for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
                if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                    arg0 += "      <seatBlockIds>" + booking.getAddBlockSeatsResponse().getSeatBlockIds()[i] + "</seatBlockIds>\n";
                }
            }
        }

        arg0 += "                                            <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                                            <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                                            <startPlaceID>" + booking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                                            <totalNumberOfAdults>" + totalNumAdlts + "</totalNumberOfAdults>\n" +
                "                                            <totalNumberOfChild>" + totalNumchld + "</totalNumberOfChild>\n" +
                "                                            <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                                            <WSRefNo>0</WSRefNo>\n" +
                "                                 </arg0>";

        String arg1 = "<arg1>" + (hasReturn ? "R" : "O") + "</arg1>";
        fetchResponseString("GetTotalFareDetailsOfTicket", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                Log.d("response", response);

                GetTotalFareDetailsOfTicketResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTotalFareDetailsOfTicket");
                    JSONObject json = requiredResponse.getJSONObject("GetTotalFareDetailsOfTicket");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetTotalFareDetailsOfTicketResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }


    /***Pickup and Dropoff point***/

    public static void GetDivyangStatus(Booking booking, final boolean drop, final GetTDivyangStatusCallback mCallback) {

        BusService busService = booking.getBusService();

        // Get Divyang ID
        String divId = "";
        DivyangDetails dy = DivyangStore.getDivyangDetails();
        if (dy != null) {
            divId = dy.getDivId();
        }

        // Determine Concession ID
        String bookingType = booking.getSearchParams().getDivyangBooking();
        String consId;

        if (bookingType.equalsIgnoreCase("Divyang") ||
                bookingType.equalsIgnoreCase("Divyang with Helper")) {
            consId = "2";
        } else {
            consId = "1";
        }

        // Determine from/to place based on drop
        Place Splace = drop ? booking.getEndPlace() : booking.getStartPlace();
        Place Eplace = drop ? booking.getStartPlace() : booking.getEndPlace(); // Corrected

        // Prepare XML request
        String arg0 =
                "<arg0>\n" +
                        "   <concessionLookupID>" + consId + "</concessionLookupID>\n" +
                        "   <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                        "   <fromPlaceCode>" + booking.getStartPlace().getPlaceID() + "</fromPlaceCode>\n" +
                        "   <toPlaceCode>" + booking.getEndPlace().getPlaceID() + "</toPlaceCode>\n" +
                        "   <divIdCardNo>" + divId + "</divIdCardNo>\n" +
                        "   <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                        "</arg0>";

        Log.d("arg0", arg0);

        fetchResponseString("GetDivyangStatuBYID", arg0, "", new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetDivyangStatuBYID");

                    Object value = requiredResponse.get("GetDivyangStatuBYID");

                    Divyangresponse divyangDetails = new Divyangresponse();

                    if (value instanceof JSONObject) {
                        JSONObject json = (JSONObject) value;
                        divyangDetails = JacksonUtil.getMapper()
                                .readValue(json.toString(), new TypeReference<Divyangresponse>() {});
                    } else {
                        divyangDetails.setGetDivyangStatuBYID(String.valueOf(value));
                    }

                    // Send parsed result
                    mCallback.onSuccess(divyangDetails);

                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(new RuntimeException("Error occurred!"));
                }
            }
        });

    }



    public interface GetTDivyangStatusCallback {

        void onSuccess(Divyangresponse response);


        void onError(Exception e);
    }


    public static void GetcaptureIPDetalsBP(Booking booking,String ipAddress, final GetcaptureIPDetalsBPCallback mCallback) {

         String mobleNo  = booking.getPassengerInfo().getPhone();
         String ipAddressS  = ipAddress;
         String emailId  = booking.getPassengerInfo().getEmail();
         String pnrNo  = booking.getSaveBookingDetailsTemporary().getPnrNumber();
         String obRefNo  = booking.getSaveBookingDetailsTemporary().getRtcRefNo();
         String userId  = AppConstants.CREDENTIALS.USER_ID;


        // Prepare XML request
        String arg0 =
                "<arg0>\n" +
                        "   <mobileNo>" + mobleNo + "</mobileNo>\n" +
                        "   <ipAdress>" +ipAddress + "</ipAdress>\n" +
                        "   <emailId>" + emailId + "</emailId>\n" +
                        "   <pnrNo>" + pnrNo + "</pnrNo>\n" +
                        "   <obRefNo>" + obRefNo + "</obRefNo>\n" +
                        "   <userId>" + userId + "</userId>\n" +
                        "</arg0>";

        Log.d("arg0", arg0);

        fetchResponseString("CaptureIPDetalsBP", arg0, "", new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "CaptureIPDetalsBP");

                    Object obj = requiredResponse.get("CaptureIPDetalsBP");

                    if (obj instanceof JSONObject) {
                        JSONObject json = (JSONObject) obj;

                        Divyangresponse divyangDetails =
                                JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<Divyangresponse>() {});

                        mCallback.onSuccess(divyangDetails);

                    } else {
                        // Handle plain integer response
                        Divyangresponse res = new Divyangresponse();
                        res.getGetDivyangStatuBYID();

                        mCallback.onSuccess(res);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(new RuntimeException("Error occurred!"));
                }
            }
        });
    }



    public interface GetcaptureIPDetalsBPCallback {

        void onSuccess(Divyangresponse response);


        void onError(Exception e);
    }

    public static void GetcaptureIPDetalsAP(Booking booking,String ipAddress, final GetcaptureIPDetalsBPCallback mCallback) {

        String mobleNo  = booking.getPassengerInfo().getPhone();
        String ipAddressS  = ipAddress;
        String emailId  = booking.getPassengerInfo().getEmail();
        String pnrNo  = booking.getSaveBookingDetailsTemporary().getPnrNumber();
        String obRefNo  = booking.getSaveBookingDetailsTemporary().getRtcRefNo();
        String userId  = AppConstants.CREDENTIALS.USER_ID;


        // Prepare XML request
        String arg0 =
                "<arg0>\n" +
                        "   <mobileNo>" + mobleNo + "</mobileNo>\n" +
                        "   <ipAdress>" +ipAddress + "</ipAdress>\n" +
                        "   <emailId>" + emailId + "</emailId>\n" +
                        "   <pnrNo>" + pnrNo + "</pnrNo>\n" +
                        "   <obRefNo>" + obRefNo + "</obRefNo>\n" +
                        "   <userId>" + userId + "</userId>\n" +
                        "</arg0>";

        Log.d("arg0", arg0);

        fetchResponseString("CaptureIPDetalsAP", arg0, "", new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "CaptureIPDetalsAP");

                    Object obj = requiredResponse.get("CaptureIPDetalsAP");

                    if (obj instanceof JSONObject) {
                        JSONObject json = (JSONObject) obj;

                        Divyangresponse divyangDetails =
                                JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<Divyangresponse>() {});

                        mCallback.onSuccess(divyangDetails);

                    } else {
                        // Handle plain integer response
                        Divyangresponse res = new Divyangresponse();
                        res.getGetDivyangStatuBYID();

                        mCallback.onSuccess(res);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(new RuntimeException("Error occurred!"));
                }
            }
        });
    }



    public interface GetcaptureIPDetalsAPCallback {

        void onSuccess(Divyangresponse response);


        void onError(Exception e);
    }

    public interface GetTotalFareDetailsOfTicketCallback {
        void onSuccess(GetTotalFareDetailsOfTicketResponse response);

        void onError(Exception e);
    }


    /***Total FareDetails Divyang Helper***/

    public static void GetDivyangTotalFareDetailsOfTicket(Booking booking, Boolean hasReturn, final GetDivyangTotalFareDetailsOfTicketCallback mCallback) {

        BusService busService = booking.getBusService();
        String consId;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            consId = "2";
        } else if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("219")) {
                consId = "49";
            } else if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("158")) {
                consId = "3";
            } else {
                consId = "1";
            }


            String percentageStr = booking.getSearchParams().getHelperPercentage();

            int percentage = (percentageStr == null || percentageStr.trim().isEmpty())
                    ? 0
                    : (int) Double.parseDouble(percentageStr);

            if (percentage == 0) {
                consId = "1";
            } else if (percentage == 50) {
                consId = "49";
            } else if (percentage == 100) {
                consId = "3";
            }


            consId = "3";


        } else {
            consId = "1";
        }

        int totalNumAdltsFemale = 0;
        int totaltotalAdlt = 0;
        int totachildmale = 0;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            if (booking.getSearchParams().getAdultFemaleCount() > 1) {
                totachildmale = 0;
                totaltotalAdlt = 0;
                totalNumAdltsFemale = 1;
            } else {
                totachildmale = 0;
                totaltotalAdlt = 1;
                totalNumAdltsFemale = 0;
            }
        } else {
            totachildmale = booking.getSearchParams().getChildrenCount();
            totaltotalAdlt = booking.getSearchParams().getAdultCount();
            totalNumAdltsFemale = booking.getSearchParams().getAdultFemaleCount();
        }

        String arg0 = " <arg0>\n" +
                "                                           <adultFemale>0</adultFemale>\n" +
                "                                            <adultMale>1</adultMale>\n" +
                "                                            <childFemale>0</childFemale>\n" +
                "                                            <childMale>0</childMale>\n" +
                "                                            <classID>" + busService.getClassID() + "</classID>\n" +
                "                                            <concessionTypeId>" + consId + "</concessionTypeId>\n" +
                "                                            <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                                            <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                                            <endPlaceCode>" + booking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                                            <endPlaceID>" + booking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                                            <franchiseeUser>false</franchiseeUser>\n" +
                "                                            <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                "                                            <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                "                                            <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                "                                            <returnServiceID>0</returnServiceID>\n";
        for (int i = 1; i < booking.getSelectedSeats().size(); i++) {
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                arg0 += "      <seatBlockIds>" + booking.getAddBlockSeatsResponse().getSeatBlockIds()[i] + "</seatBlockIds>\n";
            }
        }
        arg0 += "                                            <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                                            <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                                            <startPlaceID>" + booking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                                            <totalNumberOfAdults>1</totalNumberOfAdults>\n" +
                "                                            <totalNumberOfChild>0</totalNumberOfChild>\n" +
                "                                            <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                                            <WSRefNo>0</WSRefNo>\n" +
                "                                 </arg0>";

        String arg1 = "<arg1>" + (hasReturn ? "R" : "O") + "</arg1>";
        fetchResponseString("GetTotalFareDetailsOfTicket", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                Log.d("response", response);

                GetTotalFareDetailsOfTicketResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTotalFareDetailsOfTicket");
                    JSONObject json = requiredResponse.getJSONObject("GetTotalFareDetailsOfTicket");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetTotalFareDetailsOfTicketResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface GetDivyangTotalFareDetailsOfTicketCallback {
        void onSuccess(GetTotalFareDetailsOfTicketResponse response);

        void onError(Exception e);
    }


    /***Onward SavebookingDetails temporary (or) GetOnward ReferenceNumber ***/

    public static void GetOnwardReferenceNumber(Booking booking, final boolean drop, final String isCard, final GetOnwardReferenceNumberCallback mCallback) {

        BusService busService = booking.getBusService();
        Place place = (drop) ? booking.getEndPlace() : booking.getStartPlace();
        String consId;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            consId = "2";
        } else if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("219")) {
                consId = "49";
            } else if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("158")) {
                consId = "3";
            } else {
                consId = "1";
            }

        } else {
            consId = "1";
        }

        String arg0 = "<arg0>\n" +

                "                <classID>" + busService.getClassID() + "</classID>\n" +
//                "                <concessionTypeId>1</concessionTypeId>\n" +
                "                <concessionTypeId>" + consId + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";


        arg0 +=

                "                <idProofLookupId>166</idProofLookupId>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n";

        String passengerGender = "";
        String passengerAdult = "";
        String passengerName = "";
        String passengerAge = "";


        if (booking.getSelectedSeats().size() == 1) {
            passengerAge += booking.getPassengers().get(0).getAgeA();
            passengerName += booking.getPassengers().get(0).getPassengerNameA();
            passengerGender += booking.getPassengers().get(0).getGenderA();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 2) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 3) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + "," + booking.getPassengers().get(2).getPassengerNameC();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + "," + booking.getPassengers().get(2).getGenderC();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 4) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 5) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD() + "," + booking.getPassengers().get(4).getAgeE();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD() + "," + booking.getPassengers().get(4).getPassengerNameE();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD() + "," + booking.getPassengers().get(4).getGenderE();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 6) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD() + "," + booking.getPassengers().get(4).getAgeE() + "," + booking.getPassengers().get(5).getAgeF();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD() + "," + booking.getPassengers().get(4).getPassengerNameE() + "," + booking.getPassengers().get(5).getPassengerNameF();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD() + "," + booking.getPassengers().get(4).getGenderE() + "," + booking.getPassengers().get(5).getGenderF();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(5).getAgeF()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";

        }


        arg0 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg0 +=
                "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + booking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + booking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>0</returnServiceID>\n";

        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }


        arg0 += "        <seatNumber>" + strSeatNo + "</seatNumber>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";

        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg0 += "        <seatID>" + strSeatId + "</seatID>\n";


        arg0 += "" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg0 += "                <totalFare>" + Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {

        }


        int totalNumAdlts = 0;
        int totalNumchld = 0;
        int totalSeats = 0;

        totalNumAdlts = (booking.getSearchParams().getAdultCount() + booking.getSearchParams().getAdultFemaleCount());
        totalNumchld = booking.getSearchParams().getChildrenCount();
        totalSeats = booking.getSelectedSeats().size();


        arg0 +=
                "               <totalNumberOfAdults>" + totalNumAdlts + "</totalNumberOfAdults>\n" +
                        "                <totalNumberOfChild>" + totalNumchld + "</totalNumberOfChild>\n" +
                        "                <totalNumberOfSeats>" + totalSeats + "</totalNumberOfSeats>\n" +
                        "                <totalSeats>" + totalSeats + "</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg0>\n";

        String arg1 = "";

        Log.d("arg0", arg0);

        fetchResponseString("SaveBookingDetailsTemporary", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "SaveBookingDetailsTemporary");
                SaveBookingDetailsTemporary responseModel = null;
                try {
                    Object jsonOb = requiredResponse.get("SaveBookingDetailsTemporary");
                    responseModel = JacksonUtil.getMapper().readValue(jsonOb.toString(), new TypeReference<SaveBookingDetailsTemporary>() {
                    });
//                    if(jsonOb instanceof JSONObject){
//                        JSONObject json = requiredResponse.getJSONObject("SaveBookingDetailsTemporary");
//                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<SaveBookingDetailsTemporary>() {});
//                    }
//                    else if(jsonOb instanceof JSONArray){
//                        JSONObject json = requiredResponse.getJSONArray("SaveBookingDetailsTemporary").getJSONObject(0);
//                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<SaveBookingDetailsTemporary>() {});
//                    }
                    Log.d("responseModel", responseModel.getRtcRefNo());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }




    public static void GetOnwardPackageReferenceNumber(Booking booking, final boolean drop, final String isCard,String hotelId, final GetOnwardReferenceNumberCallback mCallback) {

        BusService busService = booking.getBusService();
        Place place = (drop) ? booking.getEndPlace() : booking.getStartPlace();
        String consId;
        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            consId = "2";
        } else if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {

            if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("219")) {
                consId = "49";
            } else if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("158")) {
                consId = "3";
            } else {
                consId = "1";
            }

        } else {
            consId = "1";
        }

        String arg0 = "<arg0>\n" +

                "                <classID>" + busService.getClassID() + "</classID>\n" +
//                "                <concessionTypeId>1</concessionTypeId>\n" +
                "                <concessionTypeId>" + consId + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";


        arg0 +=

                "                <idProofLookupId>166</idProofLookupId>\n" +
                "                <HOTEL_ID>"+hotelId+"</HOTEL_ID>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n";

        String passengerGender = "";
        String passengerAdult = "";
        String passengerName = "";
        String passengerAge = "";


        if (booking.getSelectedSeats().size() == 1) {
            passengerAge += booking.getPassengers().get(0).getAgeA();
            passengerName += booking.getPassengers().get(0).getPassengerNameA();
            passengerGender += booking.getPassengers().get(0).getGenderA();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 2) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 3) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + "," + booking.getPassengers().get(2).getPassengerNameC();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + "," + booking.getPassengers().get(2).getGenderC();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 4) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 5) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD() + "," + booking.getPassengers().get(4).getAgeE();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD() + "," + booking.getPassengers().get(4).getPassengerNameE();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD() + "," + booking.getPassengers().get(4).getGenderE();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (booking.getSelectedSeats().size() == 6) {
            passengerAge += booking.getPassengers().get(0).getAgeA() + "," + booking.getPassengers().get(1).getAgeB() + "," + booking.getPassengers().get(2).getAgeC() + ","
                    + booking.getPassengers().get(3).getAgeD() + "," + booking.getPassengers().get(4).getAgeE() + "," + booking.getPassengers().get(5).getAgeF();
            passengerName += booking.getPassengers().get(0).getPassengerNameA() + "," + booking.getPassengers().get(1).getPassengerNameB() + ","
                    + booking.getPassengers().get(2).getPassengerNameC() + "," + booking.getPassengers().get(3).getPassengerNameD() + "," + booking.getPassengers().get(4).getPassengerNameE() + "," + booking.getPassengers().get(5).getPassengerNameF();
            passengerGender += booking.getPassengers().get(0).getGenderA() + "," + booking.getPassengers().get(1).getGenderB() + ","
                    + booking.getPassengers().get(2).getGenderC() + "," + booking.getPassengers().get(3).getGenderD() + "," + booking.getPassengers().get(4).getGenderE() + "," + booking.getPassengers().get(5).getGenderF();
            if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(booking.getPassengers().get(5).getAgeF()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";

        }


        arg0 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg0 +=
                "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + booking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + booking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>0</returnServiceID>\n";

        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }


        arg0 += "        <seatNumber>" + strSeatNo + "</seatNumber>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";

        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg0 += "        <seatID>" + strSeatId + "</seatID>\n";


        arg0 += "" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg0 += "                <totalFare>" + Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {

        }


        int totalNumAdlts = 0;
        int totalNumchld = 0;
        int totalSeats = 0;

        totalNumAdlts = (booking.getSearchParams().getAdultCount() + booking.getSearchParams().getAdultFemaleCount());
        totalNumchld = booking.getSearchParams().getChildrenCount();
        totalSeats = booking.getSelectedSeats().size();


        arg0 +=
                "               <totalNumberOfAdults>" + totalNumAdlts + "</totalNumberOfAdults>\n" +
                        "                <totalNumberOfChild>" + totalNumchld + "</totalNumberOfChild>\n" +
                        "                <totalNumberOfSeats>" + totalSeats + "</totalNumberOfSeats>\n" +
                        "                <totalSeats>" + totalSeats + "</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg0>\n";

        String arg1 = "";

        Log.d("arg0", arg0);

        fetchResponseString("SavePackageBookingDetailsTemporary", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "SavePackageBookingDetailsTemporary");
                SaveBookingDetailsTemporary responseModel = null;
                try {
                    Object jsonOb = requiredResponse.get("SavePackageBookingDetailsTemporary");
                    responseModel = JacksonUtil.getMapper().readValue(jsonOb.toString(), new TypeReference<SaveBookingDetailsTemporary>() {
                    });

                    Log.d("responseModel", responseModel.getRtcRefNo());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }


    public interface GetOnwardReferenceNumberCallback {
        void onSuccess(SaveBookingDetailsTemporary response);

        void onError(Exception e);
    }


    /***Onward divyangHelper SavebookingDetails temporary (or) GetOnward ReferenceNumber ***/

    public static void GetDivyangReferenceNumber(Booking booking, Booking divyangbooking, final boolean drop, final String isCard, final GetDivyangReferenceNumberCallback mCallback) {

        BusService busService = booking.getBusService();
        BusService busService1 = divyangbooking.getBusService();
        Double dblgrandtotal = 0.00;

        String consId;
        if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("219")) {
            consId = "49";
        } else if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("158")) {
            consId = "3";
        } else {
            consId = "1";
        }


        try {
            dblgrandtotal = Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + Double.parseDouble(divyangbooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
        } catch (Exception Ex) {

        }

        String arg0 = "<arg0>\n" +

                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>2</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=
                "                <grandTotal>" + dblgrandtotal + "</grandTotal>\n" +
                        "                <idProofLookupId>166</idProofLookupId>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n";

        String passengerGender = "";
        String passengerAdult = "";
        String passengerName = "";
        String passengerAge = "";

        passengerAge += booking.getPassengers().get(0).getAgeA();
        passengerName += booking.getPassengers().get(0).getPassengerNameA();
        passengerGender += booking.getPassengers().get(0).getGenderA();
        if (Integer.parseInt(booking.getPassengers().get(0).getAgeA()) < 12)
            passengerAdult += "C";
        else
            passengerAdult += "A";


        arg0 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg0 +=
                "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + booking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + booking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>0</returnServiceID>\n";

        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < booking.getSelectedSeats().size() - 1; i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg0 += "        <seatID>" + strSeatId + "</seatID>\n";

        arg0 += "" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg0 += "                <totalFare>" + Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {
            Log.d("totalFare", "totalFare->Exception" + E.toString());
        }

        arg0 +=
                "                <totalNumberOfAdults>1</totalNumberOfAdults>\n" +
                        "                <totalNumberOfSeats>1</totalNumberOfSeats>\n" +
                        "                <totalSeats>1</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg0>\n";

        String arg1 = "<arg1>\n" +

                "                <classID>" + busService1.getClassID() + "</classID>\n" +
                "                <concessionTypeId>" + consId + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg1 +=
                "                <grandTotal>" + dblgrandtotal + "</grandTotal>\n" +
                        "                <idProofLookupId>166</idProofLookupId>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n";

        passengerGender = "";
        passengerAdult = "";
        passengerName = "";
        passengerAge = "";

        passengerAge += booking.getPassengers().get(0).getAgeB();
        passengerName += booking.getPassengers().get(0).getPassengerNameB();
        passengerGender += booking.getPassengers().get(0).getGenderB();
        if (Integer.parseInt(booking.getPassengers().get(0).getAgeB()) < 12)
            passengerAdult += "C";
        else
            passengerAdult += "A";

        arg1 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg1 +=
                "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + booking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + booking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>0</returnServiceID>\n";

        strSeatNo = "";
        strSeatId = "";

        for (int i = 1; i < booking.getSelectedSeats().size(); i++) {
            if (i != 1)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 1)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg1 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg1 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg1 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg1 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg1 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg1 += "        <seatID>" + strSeatId + "</seatID>\n";


        arg1 += "" +
                "                <serviceID>" + busService1.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg1 += "                <totalFare>" + Double.parseDouble(divyangbooking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {
            Log.d("totalFare", "totalFare->Exception" + E.toString());
        }

        arg1 +=
                "                <totalNumberOfAdults>1</totalNumberOfAdults>\n" +
                        "                <totalNumberOfSeats>1</totalNumberOfSeats>\n" +
                        "                <totalSeats>1</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg1>\n";

        Log.d("arg0", arg0);
        Log.d("arg1", arg1);

        fetchResponseString("SaveDivyangBookingDetailsTemporary", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "SaveDivyangBookingDetailsTemporary");
                SaveReturnBookingDetailsTemporary responseModel = null;
                try {
                    Object jsonOb = requiredResponse.get("SaveDivyangBookingDetailsTemporary");
                    responseModel = JacksonUtil.getMapper().readValue(jsonOb.toString(), new TypeReference<SaveReturnBookingDetailsTemporary>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface GetDivyangReferenceNumberCallback {
        void onSuccess(SaveReturnBookingDetailsTemporary response);

        void onError(Exception e);
    }

    /***Return SavebookingDetails temporary (or) GetReturn ReferenceNumber ***/

    public static void GetReturnReferenceNumber(Booking onwardBooking, Booking returnBooking, final boolean drop, final String isCard, final GetReturnReferenceNumberCallback mCallback) {

        BusService busService = onwardBooking.getBusService();
        BusService busService1 = returnBooking.getBusService();
        Double dblgrandtotal = 0.00;
        //Place place = (drop)?booking.getEndPlace(): booking.getStartPlace();

        try {
            dblgrandtotal = Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + Double.parseDouble(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
        } catch (Exception Ex) {

        }

        String arg0 = "<arg0>\n" +

                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>1</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + onwardBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + onwardBooking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=
                "                <grandTotal>" + dblgrandtotal + "</grandTotal>\n" +
                        "                <idProofLookupId>166</idProofLookupId>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + onwardBooking.getJourneyDate() + "</journeyDate>\n";

        String passengerGender = "";
        String passengerAdult = "";
        String passengerName = "";
        String passengerAge = "";


        if (onwardBooking.getSelectedSeats().size() == 1) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }

        if (onwardBooking.getSelectedSeats().size() == 2) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA() + "," + onwardBooking.getPassengers().get(1).getAgeB();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA() + "," + onwardBooking.getPassengers().get(1).getPassengerNameB();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA() + "," + onwardBooking.getPassengers().get(1).getGenderB();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (onwardBooking.getSelectedSeats().size() == 3) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA() + "," + onwardBooking.getPassengers().get(1).getAgeB() + "," + onwardBooking.getPassengers().get(2).getAgeC();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA() + "," + onwardBooking.getPassengers().get(1).getPassengerNameB() + "," + onwardBooking.getPassengers().get(2).getPassengerNameC();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA() + "," + onwardBooking.getPassengers().get(1).getGenderB() + "," + onwardBooking.getPassengers().get(2).getGenderC();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (onwardBooking.getSelectedSeats().size() == 4) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA() + "," + onwardBooking.getPassengers().get(1).getAgeB() + "," + onwardBooking.getPassengers().get(2).getAgeC() + ","
                    + onwardBooking.getPassengers().get(3).getAgeD();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA() + "," + onwardBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + onwardBooking.getPassengers().get(2).getPassengerNameC() + "," + onwardBooking.getPassengers().get(3).getPassengerNameD();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA() + "," + onwardBooking.getPassengers().get(1).getGenderB() + ","
                    + onwardBooking.getPassengers().get(2).getGenderC() + "," + onwardBooking.getPassengers().get(3).getGenderD();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (onwardBooking.getSelectedSeats().size() == 5) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA() + "," + onwardBooking.getPassengers().get(1).getAgeB() + "," + onwardBooking.getPassengers().get(2).getAgeC() + ","
                    + onwardBooking.getPassengers().get(3).getAgeD() + "," + onwardBooking.getPassengers().get(4).getAgeE();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA() + "," + onwardBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + onwardBooking.getPassengers().get(2).getPassengerNameC() + "," + onwardBooking.getPassengers().get(3).getPassengerNameD() + "," + onwardBooking.getPassengers().get(4).getPassengerNameE();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA() + "," + onwardBooking.getPassengers().get(1).getGenderB() + ","
                    + onwardBooking.getPassengers().get(2).getGenderC() + "," + onwardBooking.getPassengers().get(3).getGenderD() + "," + onwardBooking.getPassengers().get(4).getGenderE();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (onwardBooking.getSelectedSeats().size() == 6) {
            passengerAge += onwardBooking.getPassengers().get(0).getAgeA() + "," + onwardBooking.getPassengers().get(1).getAgeB() + "," + onwardBooking.getPassengers().get(2).getAgeC() + ","
                    + onwardBooking.getPassengers().get(3).getAgeD() + "," + onwardBooking.getPassengers().get(4).getAgeE() + "," + onwardBooking.getPassengers().get(5).getAgeF();
            passengerName += onwardBooking.getPassengers().get(0).getPassengerNameA() + "," + onwardBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + onwardBooking.getPassengers().get(2).getPassengerNameC() + "," + onwardBooking.getPassengers().get(3).getPassengerNameD() + "," + onwardBooking.getPassengers().get(4).getPassengerNameE() + "," + onwardBooking.getPassengers().get(5).getPassengerNameF();
            passengerGender += onwardBooking.getPassengers().get(0).getGenderA() + "," + onwardBooking.getPassengers().get(1).getGenderB() + ","
                    + onwardBooking.getPassengers().get(2).getGenderC() + "," + onwardBooking.getPassengers().get(3).getGenderD() + "," + onwardBooking.getPassengers().get(4).getGenderE() + "," + onwardBooking.getPassengers().get(5).getGenderF();
            if (Integer.parseInt(onwardBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(onwardBooking.getPassengers().get(5).getAgeF()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";

        }

        arg0 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg0 +=
                "                <phoneNumber>" + onwardBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + onwardBooking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + onwardBooking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + onwardBooking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + onwardBooking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>0</returnServiceID>\n";

        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < onwardBooking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +onwardBooking.getSelectedSeats().get(i).getSeatNo();
            if (onwardBooking.getAddBlockSeatsResponse() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";

        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg0 += "        <seatID>" + strSeatId + "</seatID>\n";


        arg0 += "" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + onwardBooking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + onwardBooking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg0 += "                <totalFare>" + Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {
            Log.d("totalFare", "totalFare->Exception" + E.toString());
        }


        arg0 +=
                "                <totalNumberOfAdults>" + (onwardBooking.getSearchParams().getAdultCount() + onwardBooking.getSearchParams().getAdultFemaleCount()) + "</totalNumberOfAdults>\n" +
                        //"                <totalNumberOfChild>"+onwardBooking.getSearchParams().getChildrenCount()+"</totalNumberOfChild>\n" +
                        "                <totalNumberOfSeats>" + onwardBooking.getSelectedSeats().size() + "</totalNumberOfSeats>\n" +
                        "                <totalSeats>" + onwardBooking.getSelectedSeats().size() + "</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg0>\n";

        String arg1 = "<arg1>\n" +

                "                <classID>" + busService1.getClassID() + "</classID>\n" +
                "                <concessionTypeId>1</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <counterId>" + AppConstants.CREDENTIALS.COUNTER_ID + "</counterId>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber>0</creditCardNumber>\n" +
                "                <emailId>" + returnBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceID>" + returnBooking.getEndPlace().getPlaceCode() + "</endPlaceID>\n" +
                "                <franchiseeUser>false</franchiseeUser>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg1 +=
                "                <grandTotal>" + dblgrandtotal + "</grandTotal>\n" +
                        "                <idProofLookupId>166</idProofLookupId>\n" +
                        "                <idProofRefernce>pan card</idProofRefernce>\n" +
                        "                <journeyDate>" + returnBooking.getJourneyDate() + "</journeyDate>\n";

        passengerGender = "";
        passengerAdult = "";
        passengerName = "";
        passengerAge = "";


        if (returnBooking.getSelectedSeats().size() == 1) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }
//}

        if (returnBooking.getSelectedSeats().size() == 2) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA() + "," + returnBooking.getPassengers().get(1).getAgeB();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA() + "," + returnBooking.getPassengers().get(1).getPassengerNameB();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA() + "," + returnBooking.getPassengers().get(1).getGenderB();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (returnBooking.getSelectedSeats().size() == 3) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA() + "," + returnBooking.getPassengers().get(1).getAgeB() + "," + returnBooking.getPassengers().get(2).getAgeC();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA() + "," + returnBooking.getPassengers().get(1).getPassengerNameB() + "," + returnBooking.getPassengers().get(2).getPassengerNameC();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA() + "," + returnBooking.getPassengers().get(1).getGenderB() + "," + returnBooking.getPassengers().get(2).getGenderC();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (returnBooking.getSelectedSeats().size() == 4) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA() + "," + returnBooking.getPassengers().get(1).getAgeB() + "," + returnBooking.getPassengers().get(2).getAgeC() + ","
                    + returnBooking.getPassengers().get(3).getAgeD();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA() + "," + returnBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + returnBooking.getPassengers().get(2).getPassengerNameC() + "," + returnBooking.getPassengers().get(3).getPassengerNameD();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA() + "," + returnBooking.getPassengers().get(1).getGenderB() + ","
                    + returnBooking.getPassengers().get(2).getGenderC() + "," + returnBooking.getPassengers().get(3).getGenderD();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (returnBooking.getSelectedSeats().size() == 5) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA() + "," + returnBooking.getPassengers().get(1).getAgeB() + "," + returnBooking.getPassengers().get(2).getAgeC() + ","
                    + returnBooking.getPassengers().get(3).getAgeD() + "," + returnBooking.getPassengers().get(4).getAgeE();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA() + "," + returnBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + returnBooking.getPassengers().get(2).getPassengerNameC() + "," + returnBooking.getPassengers().get(3).getPassengerNameD() + "," + returnBooking.getPassengers().get(4).getPassengerNameE();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA() + "," + returnBooking.getPassengers().get(1).getGenderB() + ","
                    + returnBooking.getPassengers().get(2).getGenderC() + "," + returnBooking.getPassengers().get(3).getGenderD() + "," + returnBooking.getPassengers().get(4).getGenderE();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";
        }


        if (returnBooking.getSelectedSeats().size() == 6) {
            passengerAge += returnBooking.getPassengers().get(0).getAgeA() + "," + returnBooking.getPassengers().get(1).getAgeB() + "," + returnBooking.getPassengers().get(2).getAgeC() + ","
                    + returnBooking.getPassengers().get(3).getAgeD() + "," + returnBooking.getPassengers().get(4).getAgeE() + "," + returnBooking.getPassengers().get(5).getAgeF();
            passengerName += returnBooking.getPassengers().get(0).getPassengerNameA() + "," + returnBooking.getPassengers().get(1).getPassengerNameB() + ","
                    + returnBooking.getPassengers().get(2).getPassengerNameC() + "," + returnBooking.getPassengers().get(3).getPassengerNameD() + "," + returnBooking.getPassengers().get(4).getPassengerNameE() + "," + returnBooking.getPassengers().get(5).getPassengerNameF();
            passengerGender += returnBooking.getPassengers().get(0).getGenderA() + "," + returnBooking.getPassengers().get(1).getGenderB() + ","
                    + returnBooking.getPassengers().get(2).getGenderC() + "," + returnBooking.getPassengers().get(3).getGenderD() + "," + returnBooking.getPassengers().get(4).getGenderE() + "," + returnBooking.getPassengers().get(5).getGenderF();
            if (Integer.parseInt(returnBooking.getPassengers().get(0).getAgeA()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(1).getAgeB()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(2).getAgeC()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(3).getAgeD()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(4).getAgeE()) < 12)
                passengerAdult += "C" + ",";
            else
                passengerAdult += "A" + ",";

            if (Integer.parseInt(returnBooking.getPassengers().get(5).getAgeF()) < 12)
                passengerAdult += "C";
            else
                passengerAdult += "A";

        }

        arg1 +=
                "                <childOrAdultsAnd>" + passengerAdult + "</childOrAdultsAnd>\n" +
                        "                <gendersAnd>" + passengerGender + "</gendersAnd>\n" +
                        "                <passengerAgeAnd>" + passengerAge + "</passengerAgeAnd>\n" +
                        "                <passengerName>" + passengerName + "</passengerName>\n" +
                        "                <passengerNameAnd>" + passengerName + "</passengerNameAnd>\n";

        arg1 +=
                "                <phoneNumber>" + returnBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pickupPointDropOffId>" + returnBooking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                        "                <pickupPointDropOffTime>" + returnBooking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                        "                <pickupPointPlaceId>" + returnBooking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                        "                <pickupPointTime>" + returnBooking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                        "                <pinCode>560202</pinCode>\n" +
                        "                <refundPrcntOrLumpsum>0</refundPrcntOrLumpsum>\n" +
                        "                <refundPrcntOrLumpsumValue>0</refundPrcntOrLumpsumValue>\n" +
                        "                <returnServiceID>" + busService1.getServiceID().split(",")[0].trim() + "</returnServiceID>\n";

        strSeatNo = "";
        strSeatId = "";

        for (int i = 0; i < returnBooking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +returnBooking.getSelectedSeats().get(i).getSeatNo();
            if (returnBooking.getAddBlockSeatsResponse() != null && returnBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && returnBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += returnBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];

            }
        }

        arg1 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";

        arg1 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg1 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg1 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg1 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";
        arg1 += "        <seatID>" + strSeatId + "</seatID>\n";

        arg1 += "" +
                "                <serviceID>" + busService1.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                //"                <startPlaceCode>"+returnBooking.getStartPlace().getPlaceCode()+"</startPlaceCode>\n" +
                "                <startPlaceID>" + returnBooking.getStartPlace().getPlaceCode() + "</startPlaceID>\n";

        try {
            arg1 += "                <totalFare>" + Double.parseDouble(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare()) + "</totalFare>\n";
        } catch (Exception E) {
            Log.d("totalFare", "totalFare->Exception" + E.toString());
        }


        arg1 +=
                "                <totalNumberOfAdults>" + (returnBooking.getSearchParams().getAdultCount() + returnBooking.getSearchParams().getAdultFemaleCount()) + "</totalNumberOfAdults>\n" +
                        //"                <totalNumberOfChild>"+returnBooking.getSearchParams().getChildrenCount()+"</totalNumberOfChild>\n" +
                        "                <totalNumberOfSeats>" + returnBooking.getSelectedSeats().size() + "</totalNumberOfSeats>\n" +
                        "                <totalSeats>" + returnBooking.getSelectedSeats().size() + "</totalSeats>\n" +
                        "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                        "                <WSRefNo>0</WSRefNo>\n" +
                        "            </arg1>\n";

        Log.d("arg0", arg0);
        Log.d("arg1", arg1);

        fetchResponseString("SaveReturnBookingDetailsTemporary", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "SaveReturnBookingDetailsTemporary");
                SaveReturnBookingDetailsTemporary responseModel = null;
                try {
                    Object jsonOb = requiredResponse.get("SaveReturnBookingDetailsTemporary");
                    responseModel = JacksonUtil.getMapper().readValue(jsonOb.toString(), new TypeReference<SaveReturnBookingDetailsTemporary>() {
                    });
//                    if(jsonOb instanceof JSONObject){
//                        JSONObject json = requiredResponse.getJSONObject("SaveReturnBookingDetailsTemporary");
//                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<SaveBookingDetailsTemporary>() {});
//                    }
//                    else if(jsonOb instanceof JSONArray){
//                        JSONObject json = requiredResponse.getJSONArray("SaveReturnBookingDetailsTemporary").getJSONObject(0);
//                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<SaveBookingDetailsTemporary>() {});
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface GetReturnReferenceNumberCallback {
        void onSuccess(SaveReturnBookingDetailsTemporary response);

        void onError(Exception e);
    }

    /***Insert Divyang Informations***/

    public static void InsertDivyangInformations(Booking booking, final InsertDivyangInformationsCallback mCallback) {

        String pnr = null;
        String pnrId = null;
        String refno = null;

        if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang")) {
            pnr = booking.getSaveBookingDetailsTemporary().getPnrNumber();
            pnrId = booking.getSaveBookingDetailsTemporary().getPnrMasterID();
            refno = booking.getSaveBookingDetailsTemporary().getRtcRefNo();
        } else if (booking.getSearchParams().getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
            pnr = booking.getSaveReturnBookingDetailsTemporary().getPnrNumber();
            pnrId = booking.getSaveReturnBookingDetailsTemporary().getPnrMasterID();
            refno = booking.getSaveReturnBookingDetailsTemporary().getRtcRefNo();
        }
        String journeyDate = null;
        String jdate = booking.getJourneyDate();
        String fetchdate = jdate.substring(0, 10);
        String year = fetchdate.substring(6, 10);
        String month = fetchdate.substring(3, 5);
        String date = fetchdate.substring(0, 2);
        journeyDate = year + "-" + month + "-" + date;
        String arg0 = " <arg0>\n" +
                "                                           <pnrNo>" + pnr + "</pnrNo>\n" +
                "                                            <refNo>0</refNo>\n" +
                "                                            <pnrId>" + pnrId + "</pnrId>\n" +
                "                                            <pnrRefId>" + pnrId + "</pnrRefId>\n" +
                "                                            <jdate>" + journeyDate + "</jdate>\n" +
                "                                            <pCat>218</pCat>\n" +
                "                                            <typeDis>" + booking.getSearchParams().getDivyangDisablityID() + "</typeDis>\n" +
                "                                            <pDis>100</pDis>\n" +
                "                                            <icardno>" + booking.getSearchParams().getDivyangId() + "</icardno>\n" +
                "                                            <issDis>" + booking.getSearchParams().getDivyangIssueDistrictId() + "</issDis>\n" +
                "                                            <issDisName>" + booking.getSearchParams().getDivyangIssueDistrict() + "</issDisName>\n" +
                "                                            <passName>" + booking.getPassengers().get(0).getPassengerNameA() + "</passName>\n" +
                "                                            <passAge>" + booking.getPassengers().get(0).getAgeA() + "</passAge>\n" +
                "                                            <mobileNo>" + booking.getPassengerInfo().getPhone() + "</mobileNo>\n" +
                "                                            <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                                            <gender>" + booking.getPassengers().get(0).getGenderA() + "</gender>\n" +
                "                                            <totFare>" + booking.getTotalFareDetailsOfTicketResponse().getTotalFare() + "</totFare>\n" +
                "                                            <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +

                "                                 </arg0>";

        String arg1 = "";

        Log.d("arg0", arg0);
        fetchResponseString("UpdateDivyangInformation", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                Log.d("response", response);

                GetTotalFareDetailsOfTicketResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "UpdateDivyangInformation");
                    JSONObject json = requiredResponse.getJSONObject("UpdateDivyangInformation");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetTotalFareDetailsOfTicketResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface InsertDivyangInformationsCallback {
        void onSuccess(GetTotalFareDetailsOfTicketResponse response);

        void onError(Exception e);
    }

    /***Insert Divyang With Helper Informations***/

    public static void InsertDivyangHelperInformations(Booking booking, final InsertDivyangHelperInformationsCallback mCallback) {

        String journeyDate = null;
        String jdate = booking.getJourneyDate();
        String fetchdate = jdate.substring(0, 10);
        String year = fetchdate.substring(6, 10);
        String month = fetchdate.substring(3, 5);
        String date = fetchdate.substring(0, 2);
        journeyDate = year + "-" + month + "-" + date;

        String consId;
        if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("219")) {
            consId = "49";
        } else if (booking.getSearchParams().getDivyangWithHelperIds().equalsIgnoreCase("158")) {
            consId = "3";
        } else {
            consId = "1";
        }

        String arg0 = " <arg0>\n" +
                "                                           <pnrNo>" + booking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber() + "</pnrNo>\n" +
                "                                            <refNo>" + booking.getSaveReturnBookingDetailsTemporary().getPnrNumber() + "</refNo>\n" +
                "                                            <pnrId>" + booking.getSaveReturnBookingDetailsTemporary().getReturnPnrMasterID() + "</pnrId>\n" +
                "                                            <pnrRefId>" + booking.getSaveReturnBookingDetailsTemporary().getReturnPnrMasterID() + "</pnrRefId>\n" +
                "                                            <jdate>" + journeyDate + "</jdate>\n" +
                "                                            <pCat>" + booking.getSearchParams().getDivyangWithHelperIds() + "</pCat>\n" +
                "                                            <typeDis>" + booking.getSearchParams().getDivyangDisablityID() + "</typeDis>\n" +
                "                                            <pDis>" + booking.getSearchParams().getDivyangPercentage() + "</pDis>\n" +
                "                                            <icardno>" + booking.getSearchParams().getDivyangId() + "</icardno>\n" +
                "                                            <issDis>" + booking.getSearchParams().getDivyangIssueDistrictId() + "</issDis>\n" +
                "                                            <issDisName>" + booking.getSearchParams().getDivyangIssueDistrict() + "</issDisName>\n" +
                "                                            <passName>" + booking.getPassengers().get(0).getPassengerNameB() + "</passName>\n" +
                "                                            <passAge>" + booking.getPassengers().get(0).getAgeB() + "</passAge>\n" +
                "                                            <mobileNo>" + booking.getPassengerInfo().getPhone() + "</mobileNo>\n" +
                "                                            <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                                            <gender>" + booking.getPassengers().get(0).getGenderB() + "</gender>\n" +
                "                                            <totFare>" + booking.getTotalFareDetailsOfTicketResponse().getTotalFare() + "</totFare>\n" +
                "                                            <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +

                "                                 </arg0>";

        String arg1 = "";

        Log.d("arg0", arg0);
        fetchResponseString("UpdateDivyangInformation", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                Log.d("response", response);

                GetTotalFareDetailsOfTicketResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "UpdateDivyangInformation");
                    JSONObject json = requiredResponse.getJSONObject("UpdateDivyangInformation");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetTotalFareDetailsOfTicketResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public static void getOTPForPackageBooking(String mobile, final GetOTPInfo mCallback) {

        String arg0 = " <arg0>" + mobile+"</arg0>";
        Log.d("arg0", arg0);
        fetchResponseString("GetPackageOTP", arg0, "", new BaseDefaultHttpCallback() {
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
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetPackageOTP");
                    responseModel = String.valueOf(requiredResponse.get("GetPackageOTP"));


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


    public interface InsertDivyangHelperInformationsCallback {
        void onSuccess(GetTotalFareDetailsOfTicketResponse response);

        void onError(Exception e);
    }

    /***Confirm Onward Ticket Booking ***/

    public static void ConfirmAdvSeatBookingAndroid(Booking booking, String authStatus, String bankId, String bankMerchantId, String bankRefNo, String bookingTransactionID, String gatewayAmount, String gatewayRemarks, String gatewayMoreInfo, String transactionDate, String isCard, final ConfirmAdvSeatBookingAndroidCallback mCallback) {
        BusService busService = booking.getBusService();

        Double dbltotalfare = 0.00;
        Double dblgatewayAmount = 0.00;

        try {
            dbltotalfare = Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblgatewayAmount = Double.parseDouble(gatewayAmount);
        } catch (Exception e) {
            Log.d("Exception", "ConfirmAdvSeatBookingAndroid-> Number exception" + e.getMessage());
        }

        String arg0 = "<arg0>\n" +
                "                <accidentReliefFund>" + booking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>" + booking.getSearchParams().getAdultFemaleCount() + "</adultFemale>\n" +
                "                <adultMale>" + booking.getSearchParams().getAdultCount() + "</adultMale>\n" +
                "                <age>" + booking.getPassengers().get(0).getAge() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + booking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + booking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + booking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>" + booking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + booking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + booking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + booking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +

                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=
                "                <infraStructureFee>" + booking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + booking.getSaveBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + booking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + booking.getSaveBookingDetailsTemporary().getPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + booking.getSaveBookingDetailsTemporary().getPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + booking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnServiceID>0</returnServiceID>\n" +
                        "                <rtcRefNo>" + booking.getSaveBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";


        String strSeatNo = "";
        String strSeatId = "";


        for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";


        arg0 += "                <serviceFee>" + booking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +

                "                <tollFee>" + booking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dbltotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + booking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +
                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + booking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg0>\n";

        String arg1 = "";

        fetchResponseString("ConfirmAdvSeatBookingAndroid", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ConfirmAdvSeatBookingAndroidResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmAdvSeatBookingAndroid");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmAdvSeatBookingAndroid");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmAdvSeatBookingAndroidResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface ConfirmAdvSeatBookingAndroidCallback {
        void onSuccess(ConfirmAdvSeatBookingAndroidResponse response);

        void onError(Exception e);
    }

    /***Confirm Divyang Ticket Booking ***/

    public static void ConfirmAdvDivyangSeatBookingAndroid(Booking booking, String authStatus, String bankId, String bankMerchantId, String bankRefNo, String bookingTransactionID, String gatewayAmount, String gatewayRemarks, String gatewayMoreInfo, String transactionDate, String isCard, final ConfirmAdvDivyangSeatBookingAndroidCallback mCallback) {
        BusService busService = booking.getBusService();

        Double dbltotalfare = 0.00;
        Double dblgatewayAmount = 0.00;

        try {
            dbltotalfare = Double.parseDouble(booking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblgatewayAmount = Double.parseDouble(gatewayAmount);
        } catch (Exception e) {
            Log.d("Exception", "ConfirmDivSeatBookingAndroid-> Number exception" + e.getMessage());
        }


        String arg0 = "<arg0>\n" +
                "                <accidentReliefFund>" + booking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>" + booking.getSearchParams().getAdultFemaleCount() + "</adultFemale>\n" +
                "                <adultMale>" + booking.getSearchParams().getAdultCount() + "</adultMale>\n" +
                "                <age>" + booking.getPassengers().get(0).getAgeA() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + booking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + booking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + booking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>218</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + booking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + booking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + booking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +

                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=
                "                <infraStructureFee>" + booking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + booking.getSaveBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + booking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + booking.getSaveBookingDetailsTemporary().getPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + booking.getSaveBookingDetailsTemporary().getPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + booking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnServiceID>0</returnServiceID>\n" +
                        "                <rtcRefNo>" + booking.getSaveBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";


        String strSeatNo = "";
        String strSeatId = "";


        for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +booking.getSelectedSeats().get(i).getSeatNo();
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += booking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";

        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";
        arg0 += "        <seatIDs>" + strSeatId + "</seatIDs>\n";
        arg0 += "        <seatBlockIds>" + strSeatId + "</seatBlockIds>\n";
        arg0 += "        <seatBlockIdsAnd>" + strSeatId + "</seatBlockIdsAnd>\n";


        arg0 += "                <serviceFee>" + booking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +

                "                <tollFee>" + booking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dbltotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + booking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +
                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + booking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg0>\n";

        String arg1 = "";

        fetchResponseString("ConfirmDivSeatBookingAndroid", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ConfirmAdvSeatBookingAndroidResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmDivSeatBookingAndroid");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmDivSeatBookingAndroid");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmAdvSeatBookingAndroidResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface ConfirmAdvDivyangSeatBookingAndroidCallback {
        void onSuccess(ConfirmAdvSeatBookingAndroidResponse response);

        void onError(Exception e);
    }


    /***Confirm Return Ticket Booking ***/

    public static void ConfirmRtnSeatBookingAndroid(Booking onwardBooking, Booking returnBooking, String authStatus, String bankId, String bankMerchantId, String bankRefNo, String bookingTransactionID, String gatewayAmount, String gatewayRemarks, String gatewayMoreInfo, String transactionDate, String isCard, final ConfirmRtnSeatBookingAndroidCallback mCallback) {
        BusService busService = onwardBooking.getBusService();
        BusService busService1 = returnBooking.getBusService();

        Double dblrettotalfare = 0.00;
        Double dblontotalfare = 0.00;
        Double dblgatewayAmount = 0.00;
        Double dblgrandtotal = 0.00;
        try {
            dblrettotalfare = Double.parseDouble(returnBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblontotalfare = Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblgatewayAmount = Double.parseDouble(gatewayAmount);
            dblgrandtotal = dblrettotalfare + dblontotalfare;
        } catch (Exception e) {
            Log.d("Exception", "ConfirmAdvSeatBookingAndroid-> Number exception" + e.getMessage());
        }

        String arg0 = "<arg0>\n" +
                "                <accidentReliefFund>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>" + onwardBooking.getSearchParams().getAdultFemaleCount() + "</adultFemale>\n" +
                "                <adultMale>" + onwardBooking.getSearchParams().getAdultCount() + "</adultMale>\n" +
                "                <age>" + onwardBooking.getPassengers().get(0).getAge() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + onwardBooking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + onwardBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + onwardBooking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + onwardBooking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +

                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=

                "                <infraStructureFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + onwardBooking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + onwardBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnPnrNumber></returnPnrNumber>\n" +
                        "                <returnServiceID>0</returnServiceID>\n" +
                        "                <rtcRefNo>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";


        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < onwardBooking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +onwardBooking.getSelectedSeats().get(i).getSeatNo();
            if (onwardBooking.getAddBlockSeatsResponse() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";

        arg0 += "                <serviceFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + onwardBooking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + onwardBooking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                <tollFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dblontotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +

                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg0>\n";

        String arg1 = "<arg1>\n" +
                "                <accidentReliefFund>" + returnBooking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>" + returnBooking.getSearchParams().getAdultFemaleCount() + "</adultFemale>\n" +
                "                <adultMale>" + returnBooking.getSearchParams().getAdultCount() + "</adultMale>\n" +
                "                <age>" + returnBooking.getPassengers().get(0).getAge() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + returnBooking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + returnBooking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService1.getClassID() + "</classID>\n" +
                "                <concessionTypeId>" + returnBooking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + returnBooking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + returnBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + returnBooking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + returnBooking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +

                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";


        arg1 +=
                "                <infraStructureFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + returnBooking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + returnBooking.getSaveReturnBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + returnBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + returnBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnPnrNumber>" + returnBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber() + "</returnPnrNumber>\n" +
                        "                <returnServiceID>" + busService1.getServiceID().split(",")[0].trim() + "</returnServiceID>\n" +

                        "                <rtcRefNo>" + returnBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";
        strSeatNo = "";
        strSeatId = "";

        for (int i = 0; i < returnBooking.getSelectedSeats().size(); i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +returnBooking.getSelectedSeats().get(i).getSeatNo();
            if (returnBooking.getAddBlockSeatsResponse() != null && returnBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && returnBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += returnBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];

            }
        }

        arg1 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg1 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg1 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";

        arg1 += "                <serviceFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService1.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + returnBooking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + returnBooking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +

                "                <tollFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dblrettotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + returnBooking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +

                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + returnBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg1>\n";

        fetchResponseString("ConfirmRtnSeatBookingAndroid", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response", response);
                ConfirmRtnSeatBookingAndroidResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmRtnSeatBookingAndroid");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmRtnSeatBookingAndroid");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmRtnSeatBookingAndroidResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface ConfirmRtnSeatBookingAndroidCallback {
        void onSuccess(ConfirmRtnSeatBookingAndroidResponse response);

        void onError(Exception e);
    }


    /***Confirm Divyang with Helper Ticket Booking ***/

    public static void ConfirmDivyangRtnSeatBookingAndroid(Booking onwardBooking, Booking divyangBooking, String authStatus, String bankId, String bankMerchantId, String bankRefNo, String bookingTransactionID, String gatewayAmount, String gatewayRemarks, String gatewayMoreInfo, String transactionDate, String isCard, final ConfirmDivyangRtnSeatBookingAndroidCallback mCallback) {
        BusService busService = onwardBooking.getBusService();
        BusService busService1 = divyangBooking.getBusService();


        Double dblrettotalfare = 0.00;
        Double dblontotalfare = 0.00;
        Double dblgatewayAmount = 0.00;
        Double dblgrandtotal = 0.00;
        try {
            dblrettotalfare = Double.parseDouble(divyangBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblontotalfare = Double.parseDouble(onwardBooking.getTotalFareDetailsOfTicketResponse().getTotalFare());
            dblgatewayAmount = Double.parseDouble(gatewayAmount);
            dblgrandtotal = dblrettotalfare + dblontotalfare;
        } catch (Exception e) {
            Log.d("Exception", "ConfirmDivRtnSeatBookingAndroid-> Number exception" + e.getMessage());
        }

        String arg0 = "<arg0>\n" +
                "                <accidentReliefFund>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>0</adultFemale>\n" +
                "                <adultMale>1</adultMale>\n" +
                "                <age>" + onwardBooking.getPassengers().get(0).getAgeA() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + onwardBooking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
//                "                <concessionTypeId>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <concessionTypeId>218</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + onwardBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + onwardBooking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + onwardBooking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +

                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";

        arg0 +=

                "                <infraStructureFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + onwardBooking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + onwardBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnPnrNumber></returnPnrNumber>\n" +
                        "                <returnServiceID>0</returnServiceID>\n" +

                        "                <rtcRefNo>" + onwardBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";


        String strSeatNo = "";
        String strSeatId = "";

        for (int i = 0; i < onwardBooking.getSelectedSeats().size() - 1; i++) {
            if (i != 0)
                strSeatNo += ",";
            strSeatNo += +onwardBooking.getSelectedSeats().get(i).getSeatNo();
            if (onwardBooking.getAddBlockSeatsResponse() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 0)
                    strSeatId += ",";
                strSeatId += onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];
            }
        }

        arg0 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg0 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg0 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";

        arg0 += "                <serviceFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + onwardBooking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + onwardBooking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                <tollFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dblontotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +
                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + onwardBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg0>\n";

        String arg1 = "<arg1>\n" +
                "                <accidentReliefFund>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <adultFemale>0</adultFemale>\n" +
                "                <adultMale>1</adultMale>\n" +
                "                <age>" + onwardBooking.getPassengers().get(0).getAgeB() + "</age>\n" +
                "                <authStatus>" + authStatus + "</authStatus>\n" +
                "                <bankId>" + bankId + "</bankId>\n" +
                "                <bankMerchantId>" + bankMerchantId + "</bankMerchantId>\n" +
                "                <bankRefNo>" + bankRefNo + "</bankRefNo>\n" +
                "                <basicFare>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockedReferenceNo>0</blockedReferenceNo>\n" +
                "                <blockingKeyNo>0</blockingKeyNo>\n" +
                "                <bookingTicketID>0</bookingTicketID>\n" +
                "                <bookingTransactionID>" + bookingTransactionID + "</bookingTransactionID>\n" +
                "                <bridgeFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFare>0</childFare>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + onwardBooking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
//                "                <concessionTypeId>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <concessionTypeId>" + onwardBooking.getSearchParams().getDivyangWithHelperIds() + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <discounts>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + onwardBooking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + onwardBooking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + onwardBooking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <familyPassFareDifferenceValue>0</familyPassFareDifferenceValue>\n" +
                "                <gatewayAmount>" + dblgatewayAmount + "</gatewayAmount>\n" +
                "                <gatewayMoreInfo>" + gatewayMoreInfo + "</gatewayMoreInfo>\n" +
                "                <gatewayRemarks>" + gatewayRemarks + "</gatewayRemarks>\n" +
                "                <gatewayStatus>" + authStatus + "</gatewayStatus>\n" +
                "                <gatewayLookupID>" + isCard + "</gatewayLookupID>\n";


        arg1 +=

                "                <infraStructureFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                        "                <journeyDate>" + onwardBooking.getJourneyDate() + "</journeyDate>\n" +
                        "                <onlinePaymentDetailsID>" + divyangBooking.getSaveReturnBookingDetailsTemporary().getOnlinePaymentDetailsID() + "</onlinePaymentDetailsID>\n" +
                        "                <otherLevies>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                        "                <phoneNumber>" + onwardBooking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                        "                <pnrMasterID>" + divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrMasterID() + "</pnrMasterID>\n" +
                        "                <pnrNumber>" + divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber() + "</pnrNumber>\n" +
                        "                <reservationFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                        "                <returnPnrNumber>" + divyangBooking.getSaveReturnBookingDetailsTemporary().getReturnPnrNumber() + "</returnPnrNumber>\n" +
                        "                <returnServiceID>0</returnServiceID>\n" +
                        "                <rtcRefNo>" + divyangBooking.getSaveReturnBookingDetailsTemporary().getRtcRefNo() + "</rtcRefNo>\n";


        strSeatNo = "";
        strSeatId = "";

        for (int i = 1; i < onwardBooking.getSelectedSeats().size(); i++) {
            if (i != 1)
                strSeatNo += ",";
            strSeatNo += +onwardBooking.getSelectedSeats().get(i).getSeatNo();
            if (onwardBooking.getAddBlockSeatsResponse() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds() != null && onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                if (i != 1)
                    strSeatId += ",";
                strSeatId += onwardBooking.getAddBlockSeatsResponse().getSeatBlockIds()[i];

            }
        }

        arg1 += "        <seatNo>" + strSeatNo + "</seatNo>\n";
        arg1 += "        <seatNoAndroid>" + strSeatNo + "</seatNoAndroid>\n";
        arg1 += "        <seatIDsAnd>" + strSeatId + "</seatIDsAnd>\n";

        arg1 += "                <serviceFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + onwardBooking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + onwardBooking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                <tollFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + dblrettotalfare + "</totalFare>\n" +
                "                <totalSeats>1</totalSeats>\n" +
                "                <tranDateTime>" + transactionDate + "</tranDateTime>\n" +
                "                <userFee>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +
                "                <userID>" + AppConstants.CREDENTIALS.USER_ID + "</userID>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
                "                <weekdayConcession>" + divyangBooking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n" +
                "            </arg1>\n";

        fetchResponseString("ConfirmDivRtnSeatBookingAndroid", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                Log.d("response", response);
                ConfirmRtnSeatBookingAndroidResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmDivRtnSeatBookingAndroid");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmDivRtnSeatBookingAndroid");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmRtnSeatBookingAndroidResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface ConfirmDivyangRtnSeatBookingAndroidCallback {
        void onSuccess(ConfirmRtnSeatBookingAndroidResponse response);

        void onError(Exception e);
    }


    /*** paymentGateway Bypass Using Confirm TicketBooking ***/

    public static void ConfirmAdvSeatBooking(Booking booking, final ConfirmAdvSeatBookingCallback mCallback) {
        BusService busService = booking.getBusService();
        String arg0 = "<arg0>\n" +
                "                <accidentReliefFund>" + booking.getTotalFareDetailsOfTicketResponse().getAccidentReliefFund() + "</accidentReliefFund>\n" +
                "                <address/>\n" +
                "                <adultFemale>0</adultFemale>\n" +
                "                <adultMale>" + booking.getSearchParams().getAdultCount() + "</adultMale>\n" +
                "                <adultOrChild>A</adultOrChild>\n" +
                "                <advanceOrCurrentBooking>y</advanceOrCurrentBooking>\n" +
                "                <age>" + booking.getPassengers().get(0).getAge() + "</age>\n" +
                "                <basicFare>" + booking.getTotalFareDetailsOfTicketResponse().getBasicFare() + "</basicFare>\n" +
                "                <blockingKeyNo/>\n" +
                "                <bookingType>y</bookingType>\n" +
                "                <bridgeFee>" + booking.getTotalFareDetailsOfTicketResponse().getBridgeFee() + "</bridgeFee>\n" +
                "                <childFemale>0</childFemale>\n" +
                "                <childMale>" + booking.getSearchParams().getChildrenCount() + "</childMale>\n" +
                "                <city/>\n" +
                "                <classID>" + busService.getClassID() + "</classID>\n" +
                "                <concessionTypeId>" + booking.getTotalFareDetailsOfTicketResponse().getConcessionTypeId() + "</concessionTypeId>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <creditCardNumber/>\n" +
                "                <discounts>" + booking.getTotalFareDetailsOfTicketResponse().getDiscounts() + "</discounts>\n" +
                "                <emailId>" + booking.getPassengerInfo().getEmail() + "</emailId>\n" +
                "                <endPlaceCode>" + booking.getEndPlace().getPlaceCode() + "</endPlaceCode>\n" +
                "                <endPlaceID>" + booking.getEndPlace().getPlaceID() + "</endPlaceID>\n" +
                "                <entryFee>" + booking.getTotalFareDetailsOfTicketResponse().getEntryFee() + "</entryFee>\n" +
                "                <franchiseeUser>true</franchiseeUser>\n" +
                "                <gender>" + booking.getPassengers().get(0).getGender() + "</gender>\n" +
                "                <idProofLookupId>0</idProofLookupId>\n" +
                "                <idProofRefernce/>\n" +
                "                <infraStructureFee>" + booking.getTotalFareDetailsOfTicketResponse().getInfraStructureFee() + "</infraStructureFee>\n" +
                "                <journeyDate>" + booking.getJourneyDate() + "</journeyDate>\n" +
                "                <modeOfPayment>75</modeOfPayment>\n" +
                "                <onewayOrReturnTrip>O</onewayOrReturnTrip>\n" +
                "                <otherLevies>" + booking.getTotalFareDetailsOfTicketResponse().getOtherLevies() + "</otherLevies>\n" +
                "                <passengerName>" + booking.getPassengers().get(0).getPassengerName() + "</passengerName>\n" +
                "                <phoneNumber>" + booking.getPassengerInfo().getPhone() + "</phoneNumber>\n" +
                "                <pickupPointDropOffId>" + booking.getDropPointDetails().getPickupPointID().split(",")[0] + "</pickupPointDropOffId>\n" +
                "                <pickupPointDropOffTime>" + booking.getDropPointDetails().getPickupTime() + "</pickupPointDropOffTime>\n" +
                "                <pickupPointPlaceId>" + booking.getPickupPointDetails().getPickupPointID().split(",")[0] + "</pickupPointPlaceId>\n" +
                "                <pickupPointTime>" + booking.getPickupPointDetails().getPickupTime() + "</pickupPointTime>\n" +
                "                <reservationFee>" + booking.getTotalFareDetailsOfTicketResponse().getReservationFee() + "</reservationFee>\n" +
                "                <returnServiceID>0</returnServiceID>\n" +
                "                <seatIDs/>\n" +
                "                <seriesNumber>BI</seriesNumber>\n" +
                "                <serviceFee>" + booking.getTotalFareDetailsOfTicketResponse().getServiceFee() + "</serviceFee>\n" +
                "                <serviceID>" + busService.getServiceID().split(",")[0].trim() + "</serviceID>\n" +
                "                <startPlaceCode>" + booking.getStartPlace().getPlaceCode() + "</startPlaceCode>\n" +
                "                <startPlaceID>" + booking.getStartPlace().getPlaceID() + "</startPlaceID>\n" +
                "                <ticketNumber>1</ticketNumber>\n" +
                "                <tollFee>" + booking.getTotalFareDetailsOfTicketResponse().getTollFee() + "</tollFee>\n" +
                "                <totalFare>" + booking.getTotalFareDetailsOfTicketResponse().getTotalFare() + "</totalFare>\n" +
                "                <userFee>" + booking.getTotalFareDetailsOfTicketResponse().getUserFee() + "</userFee>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "                <WSRefNo>0</WSRefNo>\n" +
//                "                <WSRefNo>"+booking.getReferenceNumberResponse().toString()+"</WSRefNo>\n" +
                "                <weekdayConcession>" + booking.getTotalFareDetailsOfTicketResponse().getWeekdayConcession() + "</weekdayConcession>\n";
        for (int i = 0; i < booking.getSelectedSeats().size(); i++) {
            arg0 += "        <seatNumber>" + booking.getSelectedSeats().get(i).getSeatNo() + "</seatNumber>\n";
            if (booking.getAddBlockSeatsResponse() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds() != null && booking.getAddBlockSeatsResponse().getSeatBlockIds().length > i) {
                arg0 += "      <seatBlockIds>" + booking.getAddBlockSeatsResponse().getSeatBlockIds()[i] + "</seatBlockIds>\n";
            }
        }
        for (int i = 0; i < booking.getPassengers().size(); i++) {
            arg0 += "          <addnlAdultOrChild>A</addnlAdultOrChild>\n" +
                    "                <addnlAge>" + booking.getPassengers().get(i).getAge() + "</addnlAge>\n" +
                    "                <addnlGender>" + booking.getPassengers().get(i).getGender() + "</addnlGender>\n" +
                    "                <addnlPasngrName>" + booking.getPassengers().get(i).getPassengerName() + "</addnlPasngrName>\n";

        }
        arg0 += "            </arg0>\n";
        String arg1 = "<arg1>\n" +
                "                <counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode>\n" +
                "                <createdBy>" + AppConstants.CREDENTIALS.USER_ID + "</createdBy>\n" +
                "                <franchiseeUser>true</franchiseeUser>\n" +
                "                <idProofLookupId>0</idProofLookupId>\n" +
                "                <idProofRefernce/>\n" +
                "                <userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName>\n" +
                "            </arg1>\n";


        fetchResponseString("ConfirmAdvSeatBooking", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ConfirmAdvSeatBookingResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmAdvSeatBooking");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmAdvSeatBooking");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmAdvSeatBookingResponse>() {
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCallback.onSuccess(responseModel);
            }
        });
    }

    public interface ConfirmAdvSeatBookingCallback {
        void onSuccess(ConfirmAdvSeatBookingResponse response);

        void onError(Exception e);
    }


    public static void GetReferenceNumber(final GetReferenceNumberCallback mCallback) {

        fetchSoapObject("GetRefNumberDetails", null, new BaseCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(Vector<SoapObject> responseList) {
            }

            @Override
            public void onSuccess(SoapPrimitive response) {
                if (response != null) {
                    mCallback.onSuccess(new GetReferenceNumberResponse(response.toString()));
                } else {
                    if (mCallback != null) {
                        mCallback.onError(new Exception("Null Response"));
                    }
                }
            }
        });
    }

    public interface GetReferenceNumberCallback {
        void onSuccess(GetReferenceNumberResponse response);

        void onError(Exception e);
    }

    public interface GetOTPInfo {
        void onSuccess(String response);

        void onError(Exception e);
    }


}
