package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Swastik on 4/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddBlockSeatsResponse implements Serializable {
    private String counterCode;

    private String journeyDate;

    private String classID;

    private String layoutID;

    private String[] seatBlockIds;

    private String WSRefNo;

    private String startPlaceID;

    private String totalNumberOfAdults;

    private String userID;

    private String createdBy;

    private String serviceID;

    private String franchiseeUser;

    private String[] seatNumber;

    private String userName;

    private String endPlaceID;

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }

    public String[] getSeatBlockIds() {
        return seatBlockIds;
    }

    public void setSeatBlockIds(String[] seatBlockIds) {
        this.seatBlockIds = seatBlockIds;
    }

    public String getWSRefNo() {
        return WSRefNo;
    }

    public void setWSRefNo(String WSRefNo) {
        this.WSRefNo = WSRefNo;
    }

    public String getStartPlaceID() {
        return startPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        this.startPlaceID = startPlaceID;
    }

    public String getTotalNumberOfAdults() {
        return totalNumberOfAdults;
    }

    public void setTotalNumberOfAdults(String totalNumberOfAdults) {
        this.totalNumberOfAdults = totalNumberOfAdults;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getFranchiseeUser() {
        return franchiseeUser;
    }

    public void setFranchiseeUser(String franchiseeUser) {
        this.franchiseeUser = franchiseeUser;
    }

    public String[] getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String[] seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
    }
}
