package com.gsrtc.mobileweb.utils;

/**
 * Created by Ajith on 4/6/2017.
 */

public class URLUtils {
//    public static final String BASE_URL = "http://111.93.131.113:7070/GSRTCWS2/reservationservice?wsdl";
//    public static final String BASE_URL = "http://111.93.131.113:7070/reservationservice?wsdl";
      public static final String BASE_URL = AppConstants.server_url+"/reservationservice?wsdl";

      public static final String FETCH_ALL_LOCATIONS_URL = BASE_URL+"/locations/getAll";
}
