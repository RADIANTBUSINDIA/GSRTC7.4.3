package com.gsrtc.mobileweb.models.trackmybus;

import java.io.Serializable;

/**
 * Created by Ajith on 11/20/2019.
 */


public class GetNearby_location_list implements Serializable {

    String placeID;
    String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

}
