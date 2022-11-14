package com.gsrtc.mobileweb.utils;

/**
 * Created by Ajith on 4/6/2017.
 */

public class AppConstants {

    public static int SEAT_ROW_COUNT = 6; //Number of seats in seat row layout

      public static String live_url = "https://gsrtc.in/";
      //public static String live_url = "http://192.168.222.131:8080/";
      public static String server_url = live_url;

    public static class CREDENTIALS {

        /***** LOCAL URL USING ******/
//        public static final String USER_NAME = "GSANDROID";
//        public static final String COUNTER_CODE = "GSANDROID";
//        public static final String USER_ID = "2800";
//        public static final String COUNTER_ID = "2801";

        /***** LIVE URL USING  *****/
        public static final String USER_NAME = "GSANDROID";
        public static final String COUNTER_CODE = "MBSTC";
        public static final String USER_ID = "2800";
        public static final String COUNTER_ID = "328";

        public static final String HDFC_LOOKUPID = "148";
        public static final String AXIS_LOOKUPID = "146";
        public static final String BILLDESK_LOOKUPID = "175";
    }

    public static class INTENT_EXTRA {
        public static final String SEARCH_PARAM = "SEARCH_PARAM";
        public static final String SEARCH_PARAMCURRENT = "SEARCH_PARAM_CURRENT";
        public static final String BUS_SERVICE = "BUS_SERVICE";
        public static final String SELECTED_SEATS = "SELECTED_SEATS";
        public static final String REFERENCE_NUMBER = "REFERENCE_NUMBER";
        public static final String BOOKING_ONWARD = "BOOKING_ONWARD";
        public static final String BOOKING_DIVYANG = "BOOKING_DIVYANG";
        public static final String BOOKING_RETURN = "BOOKING_RETURN";
        public static final String ADD_BLOCK_SEATS_RESPONSE = "ADD_BLOCK_SEATS_RESPONSE";
        public static final String BOOKING_HISTORY = "BOOKING_HISTORY";
        public static final String TRACK_MY_BUS = "TRACKMY_BUS";
        public static final String PRINT_SMS_TICKET = "PRINT_SMS_TICKET";
        public static final String BOOKING_CANCELLATION = "BOOKING_CANCELLATION";
        public static final String SEARCH_PARAM_ONROUTE = "SEARCH_PARAM_ONROUTE";
    }

    public static class JOURNEY_TYPE {
        public static final String ONWARD = "ONWARD";
        public static final String RETURN = "RETURN";
    }


    public static class SEAT_SELECTION {
        public static final Integer UNSELECTED = 0;
        public static final Integer SELECTED = 1;
        public static final Integer RESERVED = 2;
    }

    public static String SHARE_TEXT="I am happy to share, The official GSRTC Bus Ticket Booking App.You too can download from https://t.ly/OA1RD";
    public static String SHARE_TEXT_GUJARATI="મને શેર કરવામાં આનંદ થાય છે,  જીએસઆરટીસીની ઓફીસિયલ બસ ટિકિટ બુકિંગ એપ. તમે પણ આ લીંક https://t.ly/OA1RD પરથી ડાઉનલોડ કરી શકો છો.";

    public static String SHARE_TEXT_SUB="GSRTC";

}
