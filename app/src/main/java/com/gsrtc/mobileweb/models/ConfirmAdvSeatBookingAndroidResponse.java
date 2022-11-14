package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Swastik on 4/29/2017.
 */

public class ConfirmAdvSeatBookingAndroidResponse implements Serializable {
    private String FranchiseeUser;

//    private String OnlinePaymentDetailsID;

    private String PnrMasterID;

    private String PnrNumber;

//    private String RtcRefNo;

    private String SeatIDsPnrSeatDetails;   //seatIDsPnrSeatDetails


    public String getFranchiseeUser() {
        return FranchiseeUser;
    }

    public void setFranchiseeUser(String franchiseeUser) {
        FranchiseeUser = franchiseeUser;
    }

    public String getPnrMasterID() {
        return PnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        PnrMasterID = pnrMasterID;
    }

    public String getPnrNumber() {
        return PnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        PnrNumber = pnrNumber;
    }

    public String getSeatIDsPnrSeatDetails() {
        return SeatIDsPnrSeatDetails;
    }

    public void setSeatIDsPnrSeatDetails(String seatIDsPnrSeatDetails) {
        SeatIDsPnrSeatDetails = seatIDsPnrSeatDetails;
    }
}
