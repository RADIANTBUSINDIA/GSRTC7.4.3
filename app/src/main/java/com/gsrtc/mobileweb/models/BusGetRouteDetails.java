package com.gsrtc.mobileweb.models;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;


/**
 * Created by bala on 30-12-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusGetRouteDetails implements Serializable{

    private String adultTicketFare;
    private String childTicketFare;
    private String pickupPointName;
    private String pickupPointTime;

    public String getAdultTicketFare() {
        return adultTicketFare;
    }

    public void setAdultTicketFare(String adultTicketFare) {
        this.adultTicketFare = adultTicketFare;
    }

    public String getChildTicketFare() {
        return childTicketFare;
    }

    public void setChildTicketFare(String childTicketFare) {
        this.childTicketFare = childTicketFare;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getPickupPointTime() {
        return pickupPointTime;
    }

    public void setPickupPointTime(String pickupPointTime) {
        this.pickupPointTime = pickupPointTime;
    }

    public static BusGetRouteDetails fromSoapObject(SoapObject soapObject){
        BusGetRouteDetails routeDetails = new BusGetRouteDetails();
        routeDetails.setAdultTicketFare(soapObject.getProperty("adultTicketFare").toString());
        routeDetails.setChildTicketFare(soapObject.getProperty("childTicketFare").toString());
        routeDetails.setPickupPointName(soapObject.getProperty("pickupPointName").toString());
        routeDetails.setPickupPointTime(soapObject.getProperty("pickupPointTime").toString());

        return routeDetails;
    }


}
