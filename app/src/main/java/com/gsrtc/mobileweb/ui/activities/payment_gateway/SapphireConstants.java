package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.util.Log;
import com.payphi.customersdk.views.Application;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SapphireConstants {
    private static final String TAG = "SapphireConstants";

    // ICICI Orange PG / Sapphire Lookup Constants
    public static final String ICICI_ORANGE_PG_GATEWAY_LOOKUPID = "758";
    public static final String ICICI_ORANGE_PG_LOOKUP_VALUE = "ICICI ORANGE PG";
    public static final String ICICI_ORANGE_PG_SUCCESS_STATUS_VALUE = "0000";
    public static final String ICICI_ORANGE_PG_FAILURE_STATUS_VALUE = "FAILURE";

    // ICICI Orange PG Credentials
    public static final String ICICI_MID = "100000000501740";
    public static final String ICICI_AGGREGATE_ID = "100000000501739";
    public static final String ICICI_HASH_KEY = "5b3fbc61-0797-4e1f-ba23-13d646513d29";

    // Gateway Lookup ID / Card Mode
    public static final String GATEWAY_CODE = ICICI_ORANGE_PG_GATEWAY_LOOKUPID; // "758"
    public static final String GATEWAY_LOOKUP_ID = ICICI_ORANGE_PG_GATEWAY_LOOKUPID;
    public static final String GATEWAY_LOOKUP_VALUE = ICICI_ORANGE_PG_LOOKUP_VALUE;
    public static final String SUCCESS_STATUS_VALUE = ICICI_ORANGE_PG_SUCCESS_STATUS_VALUE;
    public static final String FAILURE_STATUS_VALUE = ICICI_ORANGE_PG_FAILURE_STATUS_VALUE;

    // Active Credentials used by SDK
    // Default starting in INTEGRATION environment (can switch to Application.PROD as needed)
    public static final String STARTING_ENV = Application.PROD;
    public static final String MERCHANT_ID = ICICI_MID;
    public static final String AGGREGATOR_ID = ICICI_AGGREGATE_ID;
    public static final String SECRET_KEY = ICICI_HASH_KEY;
    public static final String APP_ID = "80bc18249511f868";
    public static final String CURRENCY_CODE = "356"; // INR currency code
    public static final String MERCHANT_NAME = "GSRTC";
    public static final String API_VERSION = "4";

    /**
     * Generate HMAC SHA256 secure token for payment request
     * message = amount + currencyCode + merchantId + orderId
     */
    public static String generateSecureToken(String formattedAmount, String merchantId, String orderId, String secretKey) {
        String message = formattedAmount + CURRENCY_CODE + merchantId + orderId;
        Log.d(TAG, "Token Generation String == " + message);

        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            byte[] hashedBytes = sha256HMAC.doFinal(message.getBytes());
            return bytesToHex(hashedBytes);
        } catch (Exception e) {
            Log.e(TAG, "Error generating secure token", e);
            return null;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
