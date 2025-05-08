package com.gsrtc.mobileweb.ui.activities.packageBooking.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class GetTotalPackageFareDetails implements Serializable {

    @JsonProperty("o_EXTRA_BED_CHARGE")
    private double extraBedCharge;

    @JsonProperty("o_GST_CHARGES")
    private double gstCharges;

    @JsonProperty("o_LIGHT_AND_SOUND_CHARGES")
    private double lightAndSoundCharges;

    @JsonProperty("o_ROOM_CHAREGS")
    private double roomCharges;

    @JsonProperty("o_TOTAL_AMOUNT")
    private double totalAmount;

    public GetTotalPackageFareDetails(double extraBedCharge, double gstCharges, double lightAndSoundCharges, double roomCharges, double totalAmount) {
        this.extraBedCharge = extraBedCharge;
        this.gstCharges = gstCharges;
        this.lightAndSoundCharges = lightAndSoundCharges;
        this.roomCharges = roomCharges;
        this.totalAmount = totalAmount;
    }

    public GetTotalPackageFareDetails() {
    }


    // Getters and Setters

    public double getExtraBedCharge() {
        return extraBedCharge;
    }

    public void setExtraBedCharge(double extraBedCharge) {
        this.extraBedCharge = extraBedCharge;
    }

    public double getGstCharges() {
        return gstCharges;
    }

    public void setGstCharges(double gstCharges) {
        this.gstCharges = gstCharges;
    }

    public double getLightAndSoundCharges() {
        return lightAndSoundCharges;
    }

    public void setLightAndSoundCharges(double lightAndSoundCharges) {
        this.lightAndSoundCharges = lightAndSoundCharges;
    }

    public double getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(double roomCharges) {
        this.roomCharges = roomCharges;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
