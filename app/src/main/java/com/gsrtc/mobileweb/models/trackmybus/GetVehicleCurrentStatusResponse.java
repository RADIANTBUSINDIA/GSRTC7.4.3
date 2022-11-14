package com.gsrtc.mobileweb.models.trackmybus;

/**
 * Created by Ramu on 12/29/2017.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gsrtc.mobileweb.models.booking_history.BookingHistorySearchParam;
import com.gsrtc.mobileweb.models.booking_history.GetViewBookingHistoryDetailsResponse;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class GetVehicleCurrentStatusResponse
        implements Serializable {

    String DepartureDateTime;
    String LastBusStation;
    String LastArrivalDateTime;
    String Status;
    String RouteName;
    String NextLocation;
    String ETA;
    String Lattitude;
    String Longitude;
    String VehicleNo;

    public GetVehicleCurrentStatusResponse() {
    }

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

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String RouteName) {
        this.RouteName = RouteName;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longtitude) {
        this.Longitude = Longtitude;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String Lattitude) {
        this.Lattitude = Lattitude;
    }
    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }



}
