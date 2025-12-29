package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Divyangresponse {

    String GetDivyangStatuBYID;

    public String getGetDivyangStatuBYID() {
        return GetDivyangStatuBYID;
    }

    public void setGetDivyangStatuBYID(String getDivyangStatuBYID) {
        GetDivyangStatuBYID = getDivyangStatuBYID;
    }
}
