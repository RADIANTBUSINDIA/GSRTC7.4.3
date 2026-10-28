package com.gsrtc.mobileweb.utils;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (xmlResponseAsJson == null) return null;
        try {
            if (xmlResponseAsJson.has("soap:Envelope")) {
                JSONObject env = xmlResponseAsJson.getJSONObject("soap:Envelope");
                if (env.has("soap:Body")) return env.getJSONObject("soap:Body");
                if (env.has("Body")) return env.getJSONObject("Body");
            } else if (xmlResponseAsJson.has("soapenv:Envelope")) {
                JSONObject env = xmlResponseAsJson.getJSONObject("soapenv:Envelope");
                if (env.has("soapenv:Body")) return env.getJSONObject("soapenv:Body");
                if (env.has("Body")) return env.getJSONObject("Body");
            } else if (xmlResponseAsJson.has("Envelope")) {
                JSONObject env = xmlResponseAsJson.getJSONObject("Envelope");
                if (env.has("Body")) return env.getJSONObject("Body");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getRequiredResponse(JSONObject responseBody, String methodName){
        if (responseBody == null) return null;
        try {
            if (responseBody.has("ns2:"+methodName+"Response")) {
                return responseBody.getJSONObject("ns2:"+methodName+"Response");
            } else if (responseBody.has(methodName+"Response")) {
                return responseBody.getJSONObject(methodName+"Response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
