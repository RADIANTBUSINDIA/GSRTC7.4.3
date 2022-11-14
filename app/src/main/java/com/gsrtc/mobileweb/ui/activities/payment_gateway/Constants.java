package com.gsrtc.mobileweb.ui.activities.payment_gateway;

/**
 * Created by Ajith on 6/21/2019.
 */

public class Constants {
    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";

    /*********Aggrepay gateway ************/
    //Production_LIVE
    public static final String PG_API_KEY = "8f4de788-5bd7-478d-b994-b60dda0096ed";
    //staging
    //public static final String PG_API_KEY = "246ab4d71cb85d0062a1dc17c95b264e8df20ab6";

    //URL to Accept Payment Response Afdashboardter Payment. This needs to be done at the client's web server.
    public static final String PG_RETURN_URL = "https://gsrtc.in/gmobile/androidPayment.do";

    //Enter the Mode of Payment Here . Allowed Values are "LIVE" or "TEST".
    public static final String PG_MODE = "LIVE";

    //PG_CURRENCY is given by the Payment Gateway. Only "INR" Allowed.
    public static final String PG_CURRENCY = "INR";

    //PG_COUNTRY is given by the Payment Gateway. Only "IND" Allowed.
    public static final String PG_COUNTRY = "IND";

    public static final String PG_AMOUNT = "2";
    public static final String PG_EMAIL = "test@gmail.com";
    public static final String PG_NAME = "testing";
    public static final String PG_PHONE = "";
    public static       String PG_ORDER_ID = "";
    public static final String PG_DESCRIPTION = "GSRTC-ANDROID";
    public static final String PG_CITY = "AHMEDABAD";
    public static final String PG_STATE = "GUJARAT";
    public static final String PG_ADD_1 = "AHMEDABAD";
    public static final String PG_ADD_2 = "GUJARAT";
    public static final String PG_ZIPCODE = "380002";
    public static final String PG_UDF1 = "";
    public static final String PG_UDF2 = "";
    public static final String PG_UDF3 = "";
    public static final String PG_UDF4 = "";
    public static final String PG_UDF5 = "";
}