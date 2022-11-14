package com.gsrtc.mobileweb.models.booking_history;

import java.io.Serializable;

/**
 * Created by Ajith on 11/21/2019.
 */


public class GetTrackTicketDetails implements Serializable {

    private String adultCount;
    private String childCount;
    private String dateTime;
    private String dateofJourney;
    private String dropoffPoint;
    private String fromPlace;
    private String pickupPointName;
    private String pickupPointTime;
    private String seatNo;
    private String toPlace;
    private String totalFare;
    private String tripCode;
    private String classofService;

    public String getAdultCount ()
    {
        return adultCount;
    }
    public void setAdultCount (String adultCount)
    {
        this.adultCount = adultCount;
    }

    public String getChildCount ()
    {
        return childCount;
    }

    public void setChildCount (String childCount)
    {
        this.childCount = childCount;
    }

    public String getDateTime ()
    {
        return dateTime;
    }

    public void setDateTime (String dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getDateofJourney ()
    {
        return dateofJourney;
    }

    public void setDateofJourney (String dateofJourney)
    {
        this.dateofJourney = dateofJourney;
    }

    public String getDropoffPoint ()
    {
        return dropoffPoint;
    }

    public void setDropoffPoint (String dropoffPoint)
    {
        this.dropoffPoint = dropoffPoint;
    }

    public String getTripCode ()
    {
        return tripCode;
    }

    public void setTripCode (String tripCode)
    {
        this.tripCode = tripCode;
    }


    public String getFromPlace ()
    {
        return fromPlace;
    }

    public void setFromPlace (String fromPlace)
    {
        this.fromPlace = fromPlace;
    }

    public String getPickupPointName ()
    {
        return pickupPointName;
    }

    public void setPickupPointName (String pickupPointName)
    {
        this.pickupPointName = pickupPointName;
    }

    public String getPickupPointTime ()
    {
        return pickupPointTime;
    }

    public void setPickupPointTime (String pickupPointTime)
    {
        this.pickupPointTime = pickupPointTime;
    }

    public String getSeatNo ()
    {
        return seatNo;
    }

    public void setSeatNo (String seatNo)
    {
        this.seatNo = seatNo;
    }

    public String getToPlace ()
    {
        return toPlace;
    }

    public void setToPlace (String toPlace)
    {
        this.toPlace = toPlace;
    }

    public String getTotalFare ()
    {
        return totalFare;
    }

    public void setTotalFare (String totalFare)
    {
        this.totalFare = totalFare;
    }

    public String getClassofService ()
    {
        return classofService;
    }

    public void setClassofService (String classofService)
    {
        this.classofService = classofService;
    }

}
