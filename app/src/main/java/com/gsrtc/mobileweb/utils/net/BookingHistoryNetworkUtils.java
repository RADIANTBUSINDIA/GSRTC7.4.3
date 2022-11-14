package com.gsrtc.mobileweb.utils.net;

import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gsrtc.mobileweb.models.print_sms.GetPrintSmsDetailslResponse;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsData;
import com.gsrtc.mobileweb.models.ticket_cancellation.GetAllPassengerDetailsByPnrNoResponse;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.JacksonUtil;
import com.gsrtc.mobileweb.utils.SoapClientUtil;

import com.gsrtc.mobileweb.models.booking_history.*;
import com.gsrtc.mobileweb.utils.XMLtoJSONResponseParser;

/**
 * Created by Ayush on 16-May-17.
 */

public class BookingHistoryNetworkUtils extends SoapClientUtil {
    public static void GetViewBookingHistoryDetails(BookingHistory booking, final GetViewBookingHistoryDetailsCallback mCallback) {
        String arg0 = "<arg0> \n" +
                "<counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode> \n" +
                "<emailID>" + booking.bookingHistorySearchParam.email + "</emailID> \n" +
                "<fromDate>" + booking.bookingHistorySearchParam.fromDate + "</fromDate> \n" +
                "<status>" + booking.bookingHistorySearchParam.status + "</status> \n" +
                "<toDate>" + booking.bookingHistorySearchParam.toDate + "</toDate> \n" +
                "<userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchResponseString("GetViewBookingHistoryDetails", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetViewBookingHistoryDetailsResponse[] responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetViewBookingHistoryDetails");
                    Object jsonOb = requiredResponse.get("GetViewBookingHistoryDetails");
                    String jsonString = "";
                    if (jsonOb instanceof JSONObject) {
                        jsonString = ((JSONObject) jsonOb).toString();
                    } else if (jsonOb instanceof JSONArray) {
                        jsonString = ((JSONArray) jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetViewBookingHistoryDetailsResponse[]>() {
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

    public interface GetViewBookingHistoryDetailsCallback {
        void onSuccess(GetViewBookingHistoryDetailsResponse[] response);

        void onError(Exception e);
    }


    public static void GetPrintTicketEmail(PrintSmsData ticketsms, final GetPrintTicketEmailCallback mCallback) {


        String arg0 =

                "<arg0>" + ticketsms.printSmsParam.emailID + "</arg0> " ;

        String arg1 = "";

        fetchResponseString("GetTrasactionDetailsByEmail", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetPrintSmsDetailslResponse[] responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTrasactionDetailsByEmail");
                    Object jsonOb = requiredResponse.get("GetTrasactionDetailsByEmail");
                    String jsonString = "";
                    if (jsonOb instanceof JSONObject) {
                        jsonString = ((JSONObject) jsonOb).toString();
                    } else if (jsonOb instanceof JSONArray) {
                        jsonString = ((JSONArray) jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetPrintSmsDetailslResponse[]>() {
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

    public interface GetPrintTicketEmailCallback {
        void onSuccess(GetPrintSmsDetailslResponse[] response);

        void onError(Exception e);
    }



    public static void GetPrintTicketMobile(PrintSmsData ticketsms, final GetPrintTicketMobileCallback mCallback) {

        String arg0 =

                "<arg0> " + ticketsms.printSmsParam.mobileNo  +"</arg0> ";


        String arg1 = "";

        fetchResponseString("GetTrasactionDetailsByMobileNo", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetPrintSmsDetailslResponse[] responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTrasactionDetailsByMobileNo");
                    Object jsonOb = requiredResponse.get("GetTrasactionDetailsByMobileNo");
                    String jsonString = "";
                    if (jsonOb instanceof JSONObject) {
                        jsonString = ((JSONObject) jsonOb).toString();
                    } else if (jsonOb instanceof JSONArray) {
                        jsonString = ((JSONArray) jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetPrintSmsDetailslResponse[]>() {
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

    public interface GetPrintTicketMobileCallback {
        void onSuccess(GetPrintSmsDetailslResponse[] response);
        void onError(Exception e);
    }


    public static void GetTrackTicketHistory(PrintSmsData ticketsms, final GetTrackTicketHistoryCallback mCallback) {

        String arg0 = "<arg0> \n" +
                "<counterCode>" + AppConstants.CREDENTIALS.COUNTER_CODE + "</counterCode> \n" +
                "<mobileNo>" + ticketsms.printSmsParam.mobileNo + "</mobileNo> \n" +
                "<pnrNo>" + ticketsms.printSmsParam.pnrNo + "</pnrNo> \n" +
                "<userName>" + AppConstants.CREDENTIALS.USER_NAME + "</userName> \n" +
                "</arg0> ";
        String arg1 = "";

        fetchResponseString("GetTicketTrackHistoryDetails", arg0, arg1, new BaseDefaultHttpCallback() {
            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onSuccess(String response) {
                System.out.println(response);

                GetTrackTicketDetails[] responseModel = null;
                try {
                    JSONObject responseJson = XMLtoJSONResponseParser.getJsonResponseFromXml(response);
                    JSONObject responseBody = XMLtoJSONResponseParser.getResponseBody(responseJson);
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "GetTicketTrackHistoryDetails");
                    Object jsonOb = requiredResponse.get("GetTicketTrackHistoryDetails");
                    String jsonString = "";
                    if (jsonOb instanceof JSONObject) {
                        jsonString = ((JSONObject) jsonOb).toString();
                    } else if (jsonOb instanceof JSONArray) {
                        jsonString = ((JSONArray) jsonOb).toString();
                    }
                    responseModel = JacksonUtil.getMapper().readValue(jsonString, new TypeReference<GetTrackTicketDetails[]>() {
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

    public interface GetTrackTicketHistoryCallback {
        void onSuccess(GetTrackTicketDetails[] response);
        void onError(Exception e);
    }



    public static void GetResendSms(PrintSmsData ticketsms, final GetResendSmsCallback mCallback){

        String arg0 =

                "<arg0>" + ticketsms.printSmsParam.pnrNo + "</arg0>";

        String arg1 = "";

        fetchRetrivetxnResponseString("ReSendSMSTicket",arg0,arg1, new BaseDefaultHttpCallback() {
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
                    JSONObject requiredResponse = XMLtoJSONResponseParser.getRequiredResponse(responseBody, "ReSendSMSTicket");
                    Object jsonOb = requiredResponse.get("ReSendSMSTicket");
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

    public interface GetResendSmsCallback
    {
        void onSuccess(String response);
        void onError(Exception e);
    }

}