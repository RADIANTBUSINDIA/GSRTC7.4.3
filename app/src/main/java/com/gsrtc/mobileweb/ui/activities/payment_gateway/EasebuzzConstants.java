package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EasebuzzConstants {
    private static final String TAG = "EasebuzzConstants";

    // Gateway Lookup & Status Codes
    public static final String GATEWAY_CODE = "766";
    public static final String LEGACY_GATEWAY_CODE = "760";
    public static final String PG_LOOKUP_VALUE = "UPI";
    public static final String STATUS_SUCCESS = "TXN_SUCCESS";
    public static final String STATUS_FAILURE = "FAILURE";

    // Production Credentials
    public static final String MERCHANT_KEY = "0ODKVAHGPI";
    public static final String MERCHANT_SALT = "7BR5I3XBCA";

    // Configuration
    public static final String PAY_MODE = "production";
    public static final String PRODUCT_INFO = "GSRTC";
    public static final String DEFAULT_FIRST_NAME = "GSRTCPassenger";
    public static final int EPG_REQUEST_CODE = 786;

    // Production URLs
    public static final String INITIATE_URL = "https://pay.easebuzz.in/payment/initiateLink";
    public static final String PAY_URL = "https://pay.easebuzz.in/pay/";
    public static final String CALLBACK_URL = "https://gsrtc.in/OPRSOnline/confirmPayment.do";

    public static String getPayUrl(String accessKey) {
        return PAY_URL + accessKey;
    }

    /**
     * Exact Bank Format SHA-512 Hash Generation:
     * MERCHANT_KEY + "|" + txn_id + "|" + amount + "|" + productinfo + "|" + firstname + "|" + email + "|||||||||||" + SALT
     */
    public static String generateSha512Hash(String txnid, String amount, String firstname, String email) {
        String hashSequence = MERCHANT_KEY + "|"
                + (txnid != null ? txnid : "") + "|"
                + (amount != null ? amount : "") + "|"
                + PRODUCT_INFO + "|"
                + (firstname != null ? firstname : DEFAULT_FIRST_NAME) + "|"
                + (email != null ? email : "")
                + "|||||||||||"
                + MERCHANT_SALT;

        Log.d(TAG, "Hash String: " + hashSequence);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(hashSequence.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error generating SHA-512 hash", e);
            return null;
        }
    }
}
