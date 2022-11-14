package com.gsrtc.mobileweb.models.trackmybus;

import java.io.Serializable;

/**
 * Created by Ajith on 9/10/2019.
 */

public class GetPickupPointDetailsByPnrNoResponse implements Serializable {

    String dayType;
    String pickPointName;
    String pickupPointTime;
    String pickuppointID;
    String serviceID;

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getPickPointName() {
        return pickPointName;
    }

    public void setPickPointName(String pickPointName) {
        this.pickPointName = pickPointName;
    }

    public String getPickupPointTime() {
        return pickupPointTime;
    }

    public void setPickupPointTime(String pickupPointTime) {
        this.pickupPointTime = pickupPointTime;
    }

    public String getPickuppointID() {
        return pickuppointID;
    }

    public void setPickuppointID(String pickuppointID) {
        this.pickuppointID = pickuppointID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

}


