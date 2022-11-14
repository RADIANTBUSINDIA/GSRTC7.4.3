package com.gsrtc.mobileweb.models.trackmybus;

/**
 * Created by Ramu on 12/29/2017.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)

public class GetPnrDetailsResponse implements Serializable {

    String DepartureDateTime;
    String LastBusStation;
    String LastArrivalDateTime;
    String Status;
    String ETA;
    String NextLocation;
    String VehicleNo;
    String RounteName;
    String JourneyDate;

    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public void setDepartureDateTime(String DepartureDateTime) {
        this.DepartureDateTime = DepartureDateTime;
    }

    public String getLastBusStation() {
        return LastBusStation;
    }

    public void setLastBusStation(String LastBusStation) {
        this.LastBusStation = LastBusStation;
    }

    public String getLastArrivalDateTime() {
        return LastArrivalDateTime;
    }

    public void setLastArrivalDateTime(String LastArrivalDateTime) {
        this.LastArrivalDateTime = LastArrivalDateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getNextLocation() {
        return NextLocation;
    }

    public void setNextLocation(String NextLocation) {
        this.NextLocation = NextLocation;
    }
    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }
    public String getRounteName() {
        return RounteName;
    }

    public void setRounteName(String RounteName) {
        this.RounteName = RounteName;
    }


    public String getJourneyDate() {
        return JourneyDate;
    }

    public void setJourneyDate(String JourneyDate) {
        this.JourneyDate = JourneyDate;
    }
}
