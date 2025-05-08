package com.gsrtc.mobileweb.ui.activities.packageBooking.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllHotelInfoByPackageID implements Serializable {

    @JsonProperty("AVAILABLE_ROOMS_COUNT")
    private int availableRoomsCount;

    @JsonProperty("CHARGE_PER_ROOM")
    private double chargePerRoom;

    @JsonProperty("CHECK_IN_TIME")
    private String checkInTime;

    @JsonProperty("CHECK_OUT_TIME")
    private String checkOutTime;

    @JsonProperty("CONTACT_NO")
    private String contactNo;

    @JsonProperty("EXTRA_BED_CHARGE")
    private double extraBedCharge;

    @JsonProperty("GST_PER")
    private double gstPer;

    @JsonProperty("HMD_EXTRA_BED_YES_NO")
    private String hmdExtraBedYesNo;

    @JsonProperty("HOTEL_ADDRESS")
    private String hotelAddress;

    @JsonProperty("HOTEL_ID")
    private int hotelId;

    @JsonProperty("HOTEL_NAME")
    private String hotelName;

    @JsonProperty("HOTEL_PIN_CODE")
    private String hotelPinCode;

    private String roomsAllocated;
    private String checkInDate;
    private String checkOutDate;

    // Getters and Setters


    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getRoomsAllocated() {
        return roomsAllocated;
    }

    public void setRoomsAllocated(String roomsAllocated) {
        this.roomsAllocated = roomsAllocated;
    }

    public int getAvailableRoomsCount() {
        return availableRoomsCount;
    }

    public void setAvailableRoomsCount(int availableRoomsCount) {
        this.availableRoomsCount = availableRoomsCount;
    }

    public double getChargePerRoom() {
        return chargePerRoom;
    }

    public void setChargePerRoom(double chargePerRoom) {
        this.chargePerRoom = chargePerRoom;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public double getExtraBedCharge() {
        return extraBedCharge;
    }

    public void setExtraBedCharge(double extraBedCharge) {
        this.extraBedCharge = extraBedCharge;
    }

    public double getGstPer() {
        return gstPer;
    }

    public void setGstPer(double gstPer) {
        this.gstPer = gstPer;
    }

    public String getHmdExtraBedYesNo() {
        return hmdExtraBedYesNo;
    }

    public void setHmdExtraBedYesNo(String hmdExtraBedYesNo) {
        this.hmdExtraBedYesNo = hmdExtraBedYesNo;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelPinCode() {
        return hotelPinCode;
    }

    public void setHotelPinCode(String hotelPinCode) {
        this.hotelPinCode = hotelPinCode;
    }
}