package com.gsrtc.mobileweb.models;

import com.gsrtc.mobileweb.ui.activities.packageBooking.Model.PackageBookingModel;

import java.io.Serializable;

/**
 * Created by Swastik on 4/6/2017.
 */

public class SearchParams implements Serializable{

    Boolean isRoundTrip;
    Place fromLocation;
    Place toLocation;
    Integer numOfPassengers;
    String onWardDate, returnDate;
    Integer adultCount = 0;
    Integer adultFemaleCount = 0;
    Integer childrenCount = 0;
    Integer singleLady = 0;
    String textlanguage;
    String divyangBooking;
    String divyangId;

    String divyangDisablityType;
    String divyangDisablityID;
    String divyangIssueDistrict;
    String divyangIssueDistrictId;
    String divyangPercentage;
    String divyangWithHelperIds;

    //divyang helpers added by abhi tarun
    String divyangName;
    String divyangGender;
    String divyangDob;
    String divyangMobile;

    PackageBookingModel packageDetails;

    public PackageBookingModel getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(PackageBookingModel packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getDivyangWithHelperIds() {
        return divyangWithHelperIds;
    }

    public void setDivyangWithHelperIds(String divyangWithHelperIds) {
        this.divyangWithHelperIds = divyangWithHelperIds;
    }



    public String getDivyangDisablityType() {
        return divyangDisablityType;
    }

    public void setDivyangDisablityType(String divyangDisablityType) {
        this.divyangDisablityType = divyangDisablityType;
    }

    public String getDivyangDisablityID() {
        return divyangDisablityID;
    }

    public void setDivyangDisablityID(String divyangDisablityID) {
        this.divyangDisablityID = divyangDisablityID;
    }

    public String getDivyangIssueDistrict() {
        return divyangIssueDistrict;
    }

    public void setDivyangIssueDistrict(String divyangIssueDistrict) {
        this.divyangIssueDistrict = divyangIssueDistrict;
    }

    public String getDivyangIssueDistrictId() {
        return divyangIssueDistrictId;
    }

    public void setDivyangIssueDistrictId(String divyangIssueDistrictId) {
        this.divyangIssueDistrictId = divyangIssueDistrictId;
    }

    public String getDivyangPercentage() {
        return divyangPercentage;
    }

    public void setDivyangPercentage(String divyangPercentage) {
        this.divyangPercentage = divyangPercentage;
    }




    public String getDivyangId() {
        return divyangId;
    }

    public void setDivyangId(String divyangId) {
        this.divyangId = divyangId;
    }



    /*String pickServiceId;
    String JourneyDate;


    public String getPickServiceId() {
        return pickServiceId;
    }

    public void setPickServiceId(String pickServiceId) {
        this.pickServiceId = pickServiceId;
    }*/



    public Boolean getRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(Boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public Place getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Place fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Place getToLocation() {
        return toLocation;
    }

    public void setToLocation(Place toLocation) {
        this.toLocation = toLocation;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public String getOnWardDate() {
        return onWardDate;
    }

    public void setOnWardDate(String onWardDate) {
        this.onWardDate = onWardDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public void setTextlanguage(String textlanguage) {
        this.textlanguage = textlanguage;
    }

    public String getTextlanguage() {
        return textlanguage;
    }

    public String getDivyangBooking() {
        return divyangBooking;
    }

    public void setDivyangBooking(String divyangBooking) {
        this.divyangBooking = divyangBooking;
    }


    public void setSingleLady(int singleLady) {
        this.singleLady = singleLady;
    }

    public int getSingleLady() {
        return singleLady;
    }

    public void setAdultFemaleCount(int adultFemaleCount) {
        this.adultFemaleCount = adultFemaleCount;
    }

    public int getAdultFemaleCount() {
        return adultFemaleCount;
    }

    public String getDivyangName() {
        return divyangName;
    }

    public void setDivyangName(String divyangName) {
        this.divyangName = divyangName;
    }

    public String getDivyangGender() {
        return divyangGender;
    }

    public void setDivyangGender(String divyangGender) {
        this.divyangGender = divyangGender;
    }

    public String getDivyangDob() {
        return divyangDob;
    }

    public void setDivyangDob(String divyangDob) {
        this.divyangDob = divyangDob;
    }

    public String getDivyangMobile() {
        return divyangMobile;
    }

    public void setDivyangMobile(String divyangMobile) {
        this.divyangMobile = divyangMobile;
    }



}
