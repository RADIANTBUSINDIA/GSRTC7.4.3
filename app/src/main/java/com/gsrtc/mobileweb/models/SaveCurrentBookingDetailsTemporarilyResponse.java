package com.gsrtc.mobileweb.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

/**
 * Created by bala on 31-12-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveCurrentBookingDetailsTemporarilyResponse implements Serializable{

    private String adultTicketFare;
    private String childTicketFare;
    private String ksrtcRefNo;
    private String pnrMasterID;
    private String pnrNumber;

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

    public String getKsrtcRefNo() {
        return ksrtcRefNo;
    }

    public void setKsrtcRefNo(String ksrtcRefNo) {
        this.ksrtcRefNo = ksrtcRefNo;
    }

    public String getPnrMasterID() {
        return pnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        this.pnrMasterID = pnrMasterID;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [adultTicketFare = "+adultTicketFare+", " +
                "childTicketFare = "+childTicketFare+", " +
                "ksrtcRefNo = "+ksrtcRefNo+", " +
                "pnrNumber = "+pnrNumber+"]";
    }

}
