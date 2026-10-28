package com.gsrtc.mobileweb.utils.net;

import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gsrtc.mobileweb.models.Fault;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.ConfirmFullCancellationResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.ConfirmPartialCancellationResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.DivyangHelperPNRDetailsResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetFullCancellationDetailsResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetFullCancellationTransactionDetailsResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetPartialCancellationDetailsResponse;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetPartialCancellationTransactionDetailsResponse;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.JacksonUtil;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.XMLtoJSONResponseParser;

/**
 * Created by Ajith on 4/25/2017.
 */

public class TicketCancelNetworkUtil extends SoapClientUtil {

    public static void GetAllPassengerDetailsByPnrNo(BookingCancellation booking, final GetAllPassengerDetailsByPnrNoCallback mCallback){
        String arg0 = "<arg0> \n" +
                "<counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<franchiseeUser>false</franchiseeUser> \n" +
                "<pnrNumber>"+booking.cancellationSearchParams.pnr+"</pnrNumber> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchResponseString("GetAllPassengerDetailsByPnrNo", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                GetAllPassengerDetailsByPnrNoResponse[] responseModel = null;
                Fault[] faults = null;
                try {
                    if(response.contains("<faultstring>")){
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "Fault");
                        Object jsonOb = requiredResponse.get("Fault");
                        String jsonString = "";
                        if(jsonOb instanceof JSONObject){
                            jsonString = ((JSONObject)jsonOb).toString();
                        }
                        else if(jsonOb instanceof JSONArray){
                            jsonString = ((JSONArray)jsonOb).toString();
                        }
                        faults = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<Fault[]>() {});
                        mCallback.onSuccess(faults);

                    } else {
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetAllPassengerDetailsByPnrNo");
                        Object jsonOb = requiredResponse.get("GetAllPassengerDetailsByPnrNo");
                        String jsonString = "";
                        if(jsonOb instanceof JSONObject){
                            jsonString = ((JSONObject)jsonOb).toString();
                        }
                        else if(jsonOb instanceof JSONArray){
                            jsonString = ((JSONArray)jsonOb).toString();
                        }
                        responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetAllPassengerDetailsByPnrNoResponse[]>() {});
                        mCallback.onSuccess(responseModel);
                    }
                } catch (JSONException ex) {
                    mCallback.onError(ex);
                } catch (IOException Ex) {
                    mCallback.onError(Ex);
                } catch (Exception Ex){
                    mCallback.onError(Ex);
                }
            }
        });
    }

    public interface GetAllPassengerDetailsByPnrNoCallback{
        void onSuccess(GetAllPassengerDetailsByPnrNoResponse[] response);
        void onSuccess(Fault[] faults);
        void onError(Exception e);
    }

    public static void GetFullCancellationDetails(String pnrNumber, String emailId, String phoneNumber, String guestUserPassword, final GetFullCancellationDetailsCallback mCallback){
        String arg0 = "<arg0> \n" +
                "                <cancellationType>F</cancellationType> \n" +
                "                <counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "                <createdBy>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</createdBy> \n" +
                "                <franchiseeUser>true</franchiseeUser> \n" +
                "                <pnrNumber>"+pnrNumber+"</pnrNumber> \n" +
                "                <userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "                <guestUserPassword>"+(guestUserPassword != null ? guestUserPassword : "")+"</guestUserPassword> \n" +
                "                <emailId>"+(emailId != null ? emailId : "")+"</emailId> \n" +
                "                <phoneNumber>"+(phoneNumber != null ? phoneNumber : "")+"</phoneNumber> \n" +
                "            </arg0>";
        String arg1 = "";

        fetchResponseString("GetFullCancellationDetailsNew", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                GetFullCancellationDetailsResponse responseModel = null;
                Fault[] faults = null;
                try {
                    if(response.contains("<faultstring>")){
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "Fault");
                        Object jsonOb = requiredResponse != null ? requiredResponse.opt("Fault") : null;
                        String jsonString = "";
                        if(jsonOb instanceof JSONObject){
                            jsonString = ((JSONObject)jsonOb).toString();
                        }
                        else if(jsonOb instanceof JSONArray){
                            jsonString = ((JSONArray)jsonOb).toString();
                        }
                        if (!jsonString.isEmpty()) {
                            faults = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<Fault[]>() {});
                        }
                        mCallback.onSuccess(faults);
                    } else {
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetFullCancellationDetailsNew");
                        if (requiredResponse != null && requiredResponse.has("GetFullCancellationDetailsNew")) {
                            JSONObject json = requiredResponse.getJSONObject("GetFullCancellationDetailsNew");
                            responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetFullCancellationDetailsResponse>() {
                            });
                        }
                        mCallback.onSuccess(responseModel);
                    }

                } catch (JSONException ex) {
                    mCallback.onError(ex);
                } catch (IOException Ex) {
                    mCallback.onError(Ex);
                } catch (Exception Ex){
                    mCallback.onError(Ex);
                }
            }
        });
    }

    public static void GetFullCancellationDetails(BookingCancellation booking, final GetFullCancellationDetailsCallback mCallback){
        GetFullCancellationDetails(booking.cancellationSearchParams.pnr, booking.cancellationSearchParams.email, booking.cancellationSearchParams.mobile, booking.cancellationSearchParams.txtPassword, mCallback);
    }

    public interface GetFullCancellationDetailsCallback{
        void onSuccess(GetFullCancellationDetailsResponse response);
        void onSuccess(Fault[] faults);
        void onError(Exception e);
    }

    public static void GetFullCancellationTransactionDetails (GetFullCancellationDetailsResponse fullCancellationDetails, final GetFullCancellationTransactionDetailsCallback mCallback){
        if (fullCancellationDetails == null) {
            mCallback.onError(new IllegalArgumentException("fullCancellationDetails is null"));
            return;
        }
        String arg0 = " <arg0> \n" +
                "                <cancellationType>F</cancellationType> \n" +
                "                <concessionTypeId>"+fullCancellationDetails.getConcessionTypeId()+"</concessionTypeId> \n" +
                "                <counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "                <createdBy>"+AppConstants.CREDENTIALS.USER_ID+"</createdBy> \n" +
                "                <endPlaceID>"+fullCancellationDetails.getEndPlaceID()+"</endPlaceID> \n" +
                "                <franchiseeUser>true</franchiseeUser> \n" +
                "                <fullCancellationType>NFC</fullCancellationType> \n" +
                "                <journeyDate>"+fullCancellationDetails.getJourneyDate()+"</journeyDate> \n" +
                "                <pnrMasterID>"+fullCancellationDetails.getPnrMasterID()+"</pnrMasterID> \n" +
                "                <pnrNumber>"+fullCancellationDetails.getPnrNumber()+"</pnrNumber> \n" +
                "                <serviceID>"+fullCancellationDetails.getServiceID()+"</serviceID> \n" +
                "                <startPlaceID>"+fullCancellationDetails.getStartPlaceID()+"</startPlaceID> \n" +
                "                <userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "                <WSRefNo>"+fullCancellationDetails.getWSRefNo()+"</WSRefNo> \n" +
                "            </arg0>";
        String arg1 = "";

        fetchResponseString("GetFullCancellationTransactionDetails", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                GetFullCancellationTransactionDetailsResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetFullCancellationTransactionDetails");
                    if (requiredResponse != null && requiredResponse.has("GetFullCancellationTransactionDetails")) {
                        JSONObject json = requiredResponse.getJSONObject("GetFullCancellationTransactionDetails");
                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetFullCancellationTransactionDetailsResponse>() {});
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

    public static void GetFullCancellationTransactionDetails (BookingCancellation booking, final GetFullCancellationTransactionDetailsCallback mCallback){
        GetFullCancellationTransactionDetails(booking.getFullCancellationDetailsResponse, mCallback);
    }

    public interface GetFullCancellationTransactionDetailsCallback{
        void onSuccess(GetFullCancellationTransactionDetailsResponse response);
        void onError(Exception e);
    }

    public static void ConfirmFullCancellation (GetFullCancellationDetailsResponse detailsResponse, GetFullCancellationTransactionDetailsResponse txnDetailsResponse, final ConfirmFullCancellationCallback mCallback){
        if (detailsResponse == null || txnDetailsResponse == null) {
            mCallback.onError(new IllegalArgumentException("detailsResponse or txnDetailsResponse is null"));
            return;
        }
        String arg0 = "<arg0> \n" +
                "                <accidentReliefFund>"+txnDetailsResponse.getAccidentReliefFund()+"</accidentReliefFund> \n" +
                "                <basicFare>"+txnDetailsResponse.getBasicFare()+"</basicFare> \n" +
                "                <bookingTicketID>"+detailsResponse.getBookingTicketID()+"</bookingTicketID> \n" +
                "                <bridgeFee>"+txnDetailsResponse.getBridgeFee()+"</bridgeFee> \n" +
                "                <cancelOtherDiscount>"+txnDetailsResponse.getCancelOtherDiscount()+"</cancelOtherDiscount> \n" +
                "                <cancellationFee>"+txnDetailsResponse.getCancellationFee()+"</cancellationFee> \n" +
                "                <canclAccidentReliefFund>"+txnDetailsResponse.getCanclAccidentReliefFund()+"</canclAccidentReliefFund> \n" +
                "                <canclBasicFare>"+txnDetailsResponse.getCanclBasicFare()+"</canclBasicFare> \n" +
                "                <canclBridgeFee>"+txnDetailsResponse.getCanclBridgeFee()+"</canclBridgeFee> \n" +
                "                <canclEntryFee>"+txnDetailsResponse.getCanclEntryFee()+"</canclEntryFee> \n" +
                "                <canclOtherLevies>"+txnDetailsResponse.getCanclOtherLevies()+"</canclOtherLevies> \n" +
                "                <canclRefundAmount>"+txnDetailsResponse.getNetRefundAmount()+"</canclRefundAmount> \n" +    //TODO - doubt
                "                <canclReservationFee>"+txnDetailsResponse.getCanclReservationFee()+"</canclReservationFee> \n" +
                "                <canclTollFee>"+txnDetailsResponse.getCanclTollFee()+"</canclTollFee> \n" +
                "                <canclUserFee>"+txnDetailsResponse.getCanclUserFee()+"</canclUserFee> \n" +
                "                <concessionTypeId>"+txnDetailsResponse.getConcessionTypeId()+"</concessionTypeId> \n" +
                "                <counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "                <createdBy>"+ AppConstants.CREDENTIALS.USER_ID+"</createdBy> \n" +
                "                <endPlaceID>"+detailsResponse.getEndPlaceID()+"</endPlaceID> \n" +
                "                <entryFee>"+txnDetailsResponse.getEntryFee()+"</entryFee> \n" +
                "                <franchiseeUser>true</franchiseeUser> \n" +
                "                <journeyDate>"+detailsResponse.getJourneyDate()+"</journeyDate> \n" +
                "                <modeOfPayment>74</modeOfPayment> \n" +
                "                <netRefundAmount>"+txnDetailsResponse.getNetRefundAmount()+"</netRefundAmount> \n" +
                "                <otherConcession>"+txnDetailsResponse.getOtherConcession()+"</otherConcession> \n" +
                "                <otherLevies>"+txnDetailsResponse.getOtherLevies()+"</otherLevies> \n" +
                "                <pnrMasterID>"+detailsResponse.getPnrMasterID()+"</pnrMasterID> \n" +
                "                <pnrNumber>"+detailsResponse.getPnrNumber()+"</pnrNumber> \n" +
                "                <refundPrcntOrLumpsum>"+txnDetailsResponse.getRefundPrcntOrLumpsum()+"</refundPrcntOrLumpsum> \n" +
                "                <refundPrcntOrLumpsumValue>"+txnDetailsResponse.getRefundPrcntOrLumpsumValue()+"</refundPrcntOrLumpsumValue> \n" +
                "                <reservationFee>"+txnDetailsResponse.getReservationFee()+"</reservationFee> \n" +
                "                <resvOtherConcession>"+txnDetailsResponse.getResvOtherConcession()+"</resvOtherConcession> \n" +
                "                <resvOtherDiscount>"+txnDetailsResponse.getResvOtherDiscount()+"</resvOtherDiscount> \n" +
                "                <seatNosToInActive>"+detailsResponse.getSeatNosToInActive()+"</seatNosToInActive> \n" +
                "                <serviceID>"+detailsResponse.getServiceID()+"</serviceID> \n" + //doubt
                "                <startPlaceID>"+detailsResponse.getStartPlaceID()+"</startPlaceID> \n" +
                "                <tollFee>"+txnDetailsResponse.getTollFee()+"</tollFee> \n" +
                //"                <totalFare>"+txnDetailsResponse.getTotalFare()+"</totalFare> \n" +
                "                <totalFare>"+txnDetailsResponse.getNetRefundAmount()+"</totalFare> \n" +
                "                <totalNumberOfSeats>"+detailsResponse.getTotalNumberOfSeats()+"</totalNumberOfSeats> \n" +
                "                <userFee>"+txnDetailsResponse.getUserFee()+"</userFee> \n" +
                "                <userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName>"+
                "                <WSRefNo>"+detailsResponse.getWSRefNo()+"</WSRefNo>"+
                "            </arg0>";
        String arg1 = "";

        fetchResponseString("ConfirmFullCancellation", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ConfirmFullCancellationResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmFullCancellation");
                    if (requiredResponse != null && requiredResponse.has("ConfirmFullCancellation")) {
                        JSONObject json = requiredResponse.getJSONObject("ConfirmFullCancellation");
                        responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmFullCancellationResponse>() {});
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

    public static void ConfirmFullCancellation (BookingCancellation booking, final ConfirmFullCancellationCallback mCallback){
        ConfirmFullCancellation(booking.getFullCancellationDetailsResponse, booking.getFullCancellationTransactionDetailsResponse, mCallback);
    }

    public interface ConfirmFullCancellationCallback{
        void onSuccess(ConfirmFullCancellationResponse response);
        void onError(Exception e);
    }

    public interface GetDivyangHelperPNRDetailsCallback {
        void onSuccess(DivyangHelperPNRDetailsResponse response);
        void onSuccess(Fault[] faults);
        void onError(Exception e);
    }

    public static void GetDivyangHelperPNRDetails(String pnrNo, final GetDivyangHelperPNRDetailsCallback mCallback) {
        String arg0 = "<arg0>\n" +
                "   <i_PNRNO>" + pnrNo + "</i_PNRNO>\n" +
                "</arg0>";
        String arg1 = "";

        fetchResponseString("GetDivyangHelperPNRDetails", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println("GetDivyangHelperPNRDetails Response: " + response);
                DivyangHelperPNRDetailsResponse responseModel = null;
                Fault[] faults = null;
                try {
                    if (response != null && response.contains("<faultstring>")) {
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "Fault");
                        Object jsonOb = requiredResponse != null ? requiredResponse.opt("Fault") : null;
                        String jsonString = "";
                        if (jsonOb instanceof JSONObject) {
                            jsonString = ((JSONObject) jsonOb).toString();
                        } else if (jsonOb instanceof JSONArray) {
                            jsonString = ((JSONArray) jsonOb).toString();
                        }
                        if (!jsonString.isEmpty()) {
                            faults = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<Fault[]>() {});
                        }
                        mCallback.onSuccess(faults);
                    } else if (response != null) {
                        JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                        JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                        JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetDivyangHelperPNRDetails");
                        if (requiredResponse != null && requiredResponse.has("GetDivyangHelperPNRDetails")) {
                            JSONObject json = requiredResponse.getJSONObject("GetDivyangHelperPNRDetails");
                            responseModel = JacksonUtil.getMapper().readValue(json.toString(), DivyangHelperPNRDetailsResponse.class);
                        }
                        mCallback.onSuccess(responseModel);
                    } else {
                        mCallback.onSuccess((DivyangHelperPNRDetailsResponse) null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mCallback.onError(ex);
                }
            }
        });
    }

    public static void GetPartialCancellationDetails(BookingCancellation booking, final GetPartialCancellationDetailsCallback mCallback){
        String arg0 = "<arg0> \n" +
                "                <cancellationType>F</cancellationType> \n" +
                "                <counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "                <createdBy>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</createdBy> \n" +
                "                <franchiseeUser>true</franchiseeUser> \n" +
                "                <pnrNumber>"+booking.cancellationSearchParams.pnr+"</pnrNumber> \n" +
                "                <userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "                <WSRefNo>"+booking.cancellationSearchParams.WSRefNo+"</WSRefNo> \n" +//TODO
                "            </arg0>";
        String arg1 = "";

        fetchResponseString("GetPartialCancellationDetailsNew", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                GetPartialCancellationDetailsResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetPartialCancellationDetailsNewy");
                    JSONObject json = requiredResponse.getJSONObject("GetPartialCancellationDetailsNew");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetPartialCancellationDetailsResponse>() {});

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

    public interface GetPartialCancellationDetailsCallback{
        void onSuccess(GetPartialCancellationDetailsResponse response);
        void onError(Exception e);
    }



    public static void ConfirmPartialCancellation(BookingCancellation booking, final ConfirmPartialCancellationCallback mCallback){
        String strSeatNumber = "";
        String arg0 = "<arg0>\n" +
                "                <bookingTicketID>"+booking.getPartialCancellationDetailsResponse.getBookingTicketID()+"</bookingTicketID> \n" +
                "<cancelOtherDiscount>-"+booking.getPartialCancellationTransactionDetailsResponse.getCancelOtherDiscount()+"</cancelOtherDiscount> \n" +
                "<cancellationFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCancellationFee()+"</cancellationFee> \n" +
                "<canclAccidentReliefFund>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclAccidentReliefFund()+"</canclAccidentReliefFund> \n" +
                "<canclBasicFare>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclBasicFare()+"</canclBasicFare> \n" +
                "<canclBridgeFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclBridgeFee()+"</canclBridgeFee> \n" +
                "<canclEntryFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclEntryFee()+"</canclEntryFee> \n" +
                "<canclOtherConcessions>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclOtherConcessions()+"</canclOtherConcessions> \n" +
                "<canclRefundAmount>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclRefundAmount()+"</canclRefundAmount> \n" +
                "<canclReservationFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclReservationFee()+"</canclReservationFee> \n" +
                "<canclServiceFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclServiceFee()+"</canclServiceFee> \n" +
                "<canclTollFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclTollFee()+"</canclTollFee> \n" +
                "<canclUserFee>"+booking.getPartialCancellationTransactionDetailsResponse.getCanclUserFee()+"</canclUserFee> \n" +
                "<concessionTypeId>"+booking.getPartialCancellationTransactionDetailsResponse.getConcessionTypeId()+"</concessionTypeId> \n" +
                "<counterCode>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<createdBy>"+AppConstants.CREDENTIALS.USER_ID+"</createdBy> \n" +
                "<endPlaceID>"+booking.getPartialCancellationDetailsResponse.getEndPlaceID()+"</endPlaceID> \n" +
                "<fareAfterRefund>"+booking.getPartialCancellationTransactionDetailsResponse.getFareAfterRefund()+"</fareAfterRefund> \n" +
                "<franchiseeUser>false</franchiseeUser> \n" +
                "<journeyDate>"+booking.getPartialCancellationDetailsResponse.getJourneyDate()+"</journeyDate> \n" +
                "<pnrMasterID>"+booking.getPartialCancellationDetailsResponse.getPnrMasterID()+"</pnrMasterID> \n" +
                "<pnrNumber>"+booking.getPartialCancellationDetailsResponse.getPnrNumber()+"</pnrNumber> \n" +
                "<refundPrcntOrLumpsum>"+booking.getPartialCancellationTransactionDetailsResponse.getRefundPrcntOrLumpsum()+"</refundPrcntOrLumpsum> \n" +
                "<refundPrcntOrLumpsumValue>"+booking.getPartialCancellationTransactionDetailsResponse.getRefundPrcntOrLumpsumValue()+"</refundPrcntOrLumpsumValue> \n";
            for(GetAllPassengerDetailsByPnrNoResponse passengers: booking.selectedPassengers){
    //            arg0 +=  "<seatNo>"+passengers.getSeatNo()+"</seatNo> \n";
    //            arg0 +=  "<seatNosToInActive>"+passengers.getSeatNo()+"</seatNosToInActive>\n";
                arg0 +=  "<seatNumber>"+passengers.getSeatNo()+"</seatNumber> \n";
                if(strSeatNumber != ""){
                    strSeatNumber += ",";
                }
                strSeatNumber += passengers.getSeatNo();
            }
            arg0+= "<seatNo>"+strSeatNumber+"</seatNo> \n";
            arg0+= "<seatNosToInActive>"+strSeatNumber+"</seatNosToInActive> \n";
            arg0+= "<seriesNumber/> \n" +
                "<serviceID>"+booking.getPartialCancellationDetailsResponse.getServiceID()+"</serviceID> \n" +
                "<startPlaceID>"+booking.getPartialCancellationDetailsResponse.getStartPlaceID()+"</startPlaceID> \n" +
                //"<totalFare>"+booking.getPartialCancellationTransactionDetailsResponse.getTotalFare()+"</totalFare> \n" +
                "<totalFare>"+booking.getPartialCancellationTransactionDetailsResponse.getNetRefundAmount()+"</totalFare> \n" +
                "<totalNumberOfSeats>"+booking.selectedPassengers.size()+"</totalNumberOfSeats> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "<WSRefNo>"+booking.cancellationSearchParams.WSRefNo+"</WSRefNo> \n" +
                "            </arg0>";
        String arg1 = "";

        fetchResponseString("ConfirmPartialCancellation", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ConfirmPartialCancellationResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ConfirmPartialCancellation");
                    JSONObject json = requiredResponse.getJSONObject("ConfirmPartialCancellation");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<ConfirmPartialCancellationResponse>() {});

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

    public interface ConfirmPartialCancellationCallback{
        void onSuccess(ConfirmPartialCancellationResponse response);
        void onError(Exception e);
    }

    public static void GetPartialCancellationTransactionDetails(BookingCancellation booking, final GetPartialCancellationTransactionDetailsCallback mCallback){

        String strSelectedPassengers = "";
        if(booking.selectedPassengers != null){
            for (GetAllPassengerDetailsByPnrNoResponse pass: booking.selectedPassengers) {
                strSelectedPassengers += "<seatNumber>" + pass.getSeatNo() + "</seatNumber> \n";
            }
        }

        String arg0 = "<arg0> \n" +
                "<concessionTypeId>"+booking.getPartialCancellationDetailsResponse.getConcessionTypeId()+"</concessionTypeId> \n" +
                "<counterCode>"+ AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<createdBy>"+ AppConstants.CREDENTIALS.USER_ID+"</createdBy> \n" +
                "<endPlaceID>"+booking.getPartialCancellationDetailsResponse.getEndPlaceID()+"</endPlaceID> \n" +
                "<franchiseeUser>true</franchiseeUser> \n" +
                "<journeyDate>"+booking.getPartialCancellationDetailsResponse.getJourneyDate()+"</journeyDate> \n" +
                "<pnrNumber>"+booking.getPartialCancellationDetailsResponse.getPnrNumber()+"</pnrNumber> \n" +
                strSelectedPassengers +   //TODO
                "<serviceID>"+booking.getPartialCancellationDetailsResponse.getServiceID()+"</serviceID> \n" +
                "<startPlaceID>"+booking.getPartialCancellationDetailsResponse.getStartPlaceID()+"</startPlaceID> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "<WSRefNo>"+booking.getPartialCancellationDetailsResponse.getWSRefNo()+"</WSRefNo> \n" +
                "</arg0>";
        String arg1 = "";

        fetchResponseString("GetPartialCancellationTransactionDetails", arg0,arg1,  new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                GetPartialCancellationTransactionDetailsResponse responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetPartialCancellationTransactionDetails");
                    JSONObject json = requiredResponse.getJSONObject("GetPartialCancellationTransactionDetails");
                    responseModel = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetPartialCancellationTransactionDetailsResponse>() {});

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

    public interface GetPartialCancellationTransactionDetailsCallback{
        void onSuccess(GetPartialCancellationTransactionDetailsResponse response);
        void onError(Exception e);
    }

    public static void GetRetrivetxnPass(BookingCancellation booking, final TicketCancelNetworkUtil.GetRetrivetxnPassCallback mCallback){

        String arg0 = "<arg0> \n" +
                "<counterCode>"+AppConstants.CREDENTIALS.COUNTER_CODE+"</counterCode> \n" +
                "<pnrNo>" + booking.cancellationSearchParams.pnr + "</pnrNo> \n" +
                "<userName>"+AppConstants.CREDENTIALS.USER_NAME+"</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchRetrivetxnResponseString("RetrivePassword",arg0,arg1, new BaseDefaultHttpCallback() {
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
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "RetrivePassword");
                    Object jsonOb = requiredResponse.get("RetrivePassword");
                    String jsonString = String.valueOf(jsonOb);
                    if (jsonString.equalsIgnoreCase("success")) {
//                    if(jsonOb instanceof JSONObject){
//                        jsonString = ((JSONObject)jsonOb).toString();
//                    }
//                    else if(jsonOb instanceof JSONArray){
//                        jsonString = ((JSONArray)jsonOb).toString();
//                    }
//                        responseModel = JacksonUtil.getMapper().readValue("failure", new TypeReference<RetrivePasswordResponse>() {
//                        });
                        responseModel ="Message Sent";
                    }
                    else
                    {
                        responseModel ="Invalid PNR.NO";
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

    public interface GetRetrivetxnPassCallback
    {
        void onSuccess(String response);
        void onError(Exception e);
    }
    public static void GetCommonOTP(
            String mobileNo,
            final GetCommonOTPCallback mCallback) {

        // Backend expects SIMPLE arg0
        String arg0 = "<arg0>" + mobileNo + "</arg0>";
        String arg1 = "";

        fetchResponseString("GetCommonOTP", arg0, arg1,
                new BaseDefaultHttpCallback() {

                    @Override
                    public void onError(Exception e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onSuccess(String response) {

                        System.out.println(response);

                        String otp = null;

                        try {
                            JSONObject responseJson =
                                    XMLtoJSONResponseParser.getJsonResponseFromXml(response);

                            JSONObject responseBody =
                                    XMLtoJSONResponseParser.getResponseBody(responseJson);

                            JSONObject requiredResponse =
                                    XMLtoJSONResponseParser.getRequiredResponse(
                                            responseBody, "GetCommonOTP");

                            Object jsonOb = requiredResponse.opt("GetCommonOTP");

                            if (jsonOb != null) {
                                otp = jsonOb.toString(); // 🔥 ACTUAL OTP
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mCallback.onSuccess(otp);
                    }
                });
    }


    public interface GetCommonOTPCallback {
        void onSuccess(String otp);
        void onError(Exception e);
    }










}
