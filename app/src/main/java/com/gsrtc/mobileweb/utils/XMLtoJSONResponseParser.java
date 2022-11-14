package com.gsrtc.mobileweb.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by Ajith on 4/19/2017.
 */

public class XMLtoJSONResponseParser {
    public static JSONObject getJsonResponseFromXml(String xmlString){
        try {
            return XML.toJSONObject(xmlString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getResponseBody(JSONObject xmlResponseAsJson){
        try {
            return (xmlResponseAsJson.getJSONObject("soap:Envelope")).getJSONObject("soap:Body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getRequiredResponse(JSONObject responseBody, String methodName){
        try {
            return (responseBody.getJSONObject("ns2:"+methodName+"Response"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
