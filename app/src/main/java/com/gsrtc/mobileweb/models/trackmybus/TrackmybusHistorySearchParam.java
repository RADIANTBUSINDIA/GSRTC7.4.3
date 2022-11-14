package com.gsrtc.mobileweb.models.trackmybus;

/**
 * Created by Ramu on 12/29/2017.
 */
import android.text.Editable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gsrtc.mobileweb.models.Place;
import com.gsrtc.mobileweb.models.booking_history.BookingHistorySearchParam;
import com.gsrtc.mobileweb.models.booking_history.GetViewBookingHistoryDetailsResponse;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
public class TrackmybusHistorySearchParam implements Serializable{
    public String VehicleNo;
    public String PnrNo;
    public String CurrentDate;
    public String TripCode;
    public String StatusCode;
    public String PickLocation;
    public String TrackStatus;
    public String ModuleName;
    public String TrackDate;



    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }


}
