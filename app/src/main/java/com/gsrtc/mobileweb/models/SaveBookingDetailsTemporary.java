package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Swastik on 4/29/2017.
 */

public class SaveBookingDetailsTemporary implements Serializable {
    private String FranchiseeUser;

    private String OnlinePaymentDetailsID;

    private String PnrMasterID;

    private String PnrNumber;

    private String RtcRefNo;

    private String SeatIdsAnd;


    public String getFranchiseeUser() {
        return FranchiseeUser;
    }

    public void setFranchiseeUser(String franchiseeUser) {
        FranchiseeUser = franchiseeUser;
    }

    public String getOnlinePaymentDetailsID() {
        return OnlinePaymentDetailsID;
    }

    public void setOnlinePaymentDetailsID(String onlinePaymentDetailsID) {
        OnlinePaymentDetailsID = onlinePaymentDetailsID;
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

    public String getRtcRefNo() {
        return RtcRefNo;
    }

    public void setRtcRefNo(String rtcRefNo) {
        RtcRefNo = rtcRefNo;
    }

    public String getSeatIdsAnd() {
        return SeatIdsAnd;
    }

    public void setSeatIdsAnd(String seatIdsAnd) {
        SeatIdsAnd = seatIdsAnd;
    }
}
