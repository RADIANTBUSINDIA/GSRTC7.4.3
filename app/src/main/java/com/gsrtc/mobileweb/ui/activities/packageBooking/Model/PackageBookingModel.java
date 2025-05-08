package com.gsrtc.mobileweb.ui.activities.packageBooking.Model;

import java.io.Serializable;

public class PackageBookingModel implements Serializable {

    GetPackageTripsList tripSelected;
    String date;
    String noOfPassengers;
    String hotelType;
    GetAllHotelInfoByPackageID hotelInfo;
    GetTotalPackageFareDetails fareDetails ;

    public GetTotalPackageFareDetails getFareDetails() {
        return fareDetails;
    }

    public void setFareDetails(GetTotalPackageFareDetails fareDetails) {
        this.fareDetails = fareDetails;
    }

    public GetAllHotelInfoByPackageID getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(GetAllHotelInfoByPackageID hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public GetPackageTripsList getTripSelected() {
        return tripSelected;
    }

    public void setTripSelected(GetPackageTripsList tripSelected) {
        this.tripSelected = tripSelected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(String noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }
}
