package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by swastik on 15/4/17.
 */

public class Place implements Serializable {

    String placeCode;
    String placeID;
    String placeName;
    String stateCode;
    String stateName;

    public Place() {
    }

    public Place(String placeCode, String placeID, String placeName, String stateCode, String stateName) {
        this.placeCode = placeCode;
        this.placeID = placeID;
        this.placeName = placeName;
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
