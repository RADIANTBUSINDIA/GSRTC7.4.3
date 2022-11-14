package com.gsrtc.mobileweb.models.trackmybus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Ajith on 2/13/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Finaltrack implements Serializable {
    private String arrTime;
    private String conMobileNo;
    private String depTime;
    private String destination;
    private String fromPlace;
    private String orgin;
    private String toPlace;
    private String tripCode;
    private String vehicleNo;


    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getConMobileNo() {
        return conMobileNo;
    }

    public void setConMobileNo(String conMobileNo) {
        this.conMobileNo = conMobileNo;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }


}