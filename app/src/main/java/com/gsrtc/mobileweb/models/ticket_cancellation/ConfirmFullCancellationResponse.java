package com.gsrtc.mobileweb.models.ticket_cancellation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by swastik on 8/5/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmFullCancellationResponse implements Serializable {
    private String counterCode;

    private String canclBridgeFee;

    private String modeOfPayment;

    private String bookingTicketID;

    private String totalNumberOfSeats;

    private String canclOtherLevies;

    private String canclBasicFare;

    private String reservationFee;

    private String startPlaceID;

    private String otherLevies;

    private String serviceID;

    private String pnrMasterID;

    private String userName;

    private String seatNosToInActive;

    private String netRefundAmount;

    private String refundPrcntOrLumpsumValue;

    private String canclAccidentReliefFund;

    private String cancellationFee;

    private String canclReservationFee;

    private String pnrNumber;

    private String refundPrcntOrLumpsum;

    private String canclRefundAmount;

    private String status;

    private String journeyDate;

    private String otherConcession;

    private String bridgeFee;

    private String cancelOtherDiscount;

    private String entryFee;

    private String WSRefNo;

    private String resvOtherConcession;

    private String userFee;

    private String totalFare;

    private String createdBy;

    private String tollFee;

    private String canclTollFee;

    private String canclEntryFee;

    private String franchiseeUser;

    private String concessionTypeId;

    private String canclUserFee;

    private String basicFare;

    private String endPlaceID;

    private String accidentReliefFund;

    private String resvOtherDiscount;

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getCanclBridgeFee() {
        return canclBridgeFee;
    }

    public void setCanclBridgeFee(String canclBridgeFee) {
        this.canclBridgeFee = canclBridgeFee;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getBookingTicketID() {
        return bookingTicketID;
    }

    public void setBookingTicketID(String bookingTicketID) {
        this.bookingTicketID = bookingTicketID;
    }

    public String getTotalNumberOfSeats() {
        return totalNumberOfSeats;
    }

    public void setTotalNumberOfSeats(String totalNumberOfSeats) {
        this.totalNumberOfSeats = totalNumberOfSeats;
    }

    public String getCanclOtherLevies() {
        return canclOtherLevies;
    }

    public void setCanclOtherLevies(String canclOtherLevies) {
        this.canclOtherLevies = canclOtherLevies;
    }

    public String getCanclBasicFare() {
        return canclBasicFare;
    }

    public void setCanclBasicFare(String canclBasicFare) {
        this.canclBasicFare = canclBasicFare;
    }

    public String getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(String reservationFee) {
        this.reservationFee = reservationFee;
    }

    public String getStartPlaceID() {
        return startPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        this.startPlaceID = startPlaceID;
    }

    public String getOtherLevies() {
        return otherLevies;
    }

    public void setOtherLevies(String otherLevies) {
        this.otherLevies = otherLevies;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPnrMasterID() {
        return pnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        this.pnrMasterID = pnrMasterID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSeatNosToInActive() {
        return seatNosToInActive;
    }

    public void setSeatNosToInActive(String seatNosToInActive) {
        this.seatNosToInActive = seatNosToInActive;
    }

    public String getNetRefundAmount() {
        return netRefundAmount;
    }

    public void setNetRefundAmount(String netRefundAmount) {
        this.netRefundAmount = netRefundAmount;
    }

    public String getRefundPrcntOrLumpsumValue() {
        return refundPrcntOrLumpsumValue;
    }

    public void setRefundPrcntOrLumpsumValue(String refundPrcntOrLumpsumValue) {
        this.refundPrcntOrLumpsumValue = refundPrcntOrLumpsumValue;
    }

    public String getCanclAccidentReliefFund() {
        return canclAccidentReliefFund;
    }

    public void setCanclAccidentReliefFund(String canclAccidentReliefFund) {
        this.canclAccidentReliefFund = canclAccidentReliefFund;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public String getCanclReservationFee() {
        return canclReservationFee;
    }

    public void setCanclReservationFee(String canclReservationFee) {
        this.canclReservationFee = canclReservationFee;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getRefundPrcntOrLumpsum() {
        return refundPrcntOrLumpsum;
    }

    public void setRefundPrcntOrLumpsum(String refundPrcntOrLumpsum) {
        this.refundPrcntOrLumpsum = refundPrcntOrLumpsum;
    }

    public String getCanclRefundAmount() {
        return canclRefundAmount;
    }

    public void setCanclRefundAmount(String canclRefundAmount) {
        this.canclRefundAmount = canclRefundAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getOtherConcession() {
        return otherConcession;
    }

    public void setOtherConcession(String otherConcession) {
        this.otherConcession = otherConcession;
    }

    public String getBridgeFee() {
        return bridgeFee;
    }

    public void setBridgeFee(String bridgeFee) {
        this.bridgeFee = bridgeFee;
    }

    public String getCancelOtherDiscount() {
        return cancelOtherDiscount;
    }

    public void setCancelOtherDiscount(String cancelOtherDiscount) {
        this.cancelOtherDiscount = cancelOtherDiscount;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    @JsonProperty("WSRefNo")
    public String getWSRefNo() {
        return WSRefNo;
    }

    public void setWSRefNo(String WSRefNo) {
        this.WSRefNo = WSRefNo;
    }

    public String getResvOtherConcession() {
        return resvOtherConcession;
    }

    public void setResvOtherConcession(String resvOtherConcession) {
        this.resvOtherConcession = resvOtherConcession;
    }

    public String getUserFee() {
        return userFee;
    }

    public void setUserFee(String userFee) {
        this.userFee = userFee;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTollFee() {
        return tollFee;
    }

    public void setTollFee(String tollFee) {
        this.tollFee = tollFee;
    }

    public String getCanclTollFee() {
        return canclTollFee;
    }

    public void setCanclTollFee(String canclTollFee) {
        this.canclTollFee = canclTollFee;
    }

    public String getCanclEntryFee() {
        return canclEntryFee;
    }

    public void setCanclEntryFee(String canclEntryFee) {
        this.canclEntryFee = canclEntryFee;
    }

    public String getFranchiseeUser() {
        return franchiseeUser;
    }

    public void setFranchiseeUser(String franchiseeUser) {
        this.franchiseeUser = franchiseeUser;
    }

    public String getConcessionTypeId() {
        return concessionTypeId;
    }

    public void setConcessionTypeId(String concessionTypeId) {
        this.concessionTypeId = concessionTypeId;
    }

    public String getCanclUserFee() {
        return canclUserFee;
    }

    public void setCanclUserFee(String canclUserFee) {
        this.canclUserFee = canclUserFee;
    }

    public String getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(String basicFare) {
        this.basicFare = basicFare;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
    }

    public String getAccidentReliefFund() {
        return accidentReliefFund;
    }

    public void setAccidentReliefFund(String accidentReliefFund) {
        this.accidentReliefFund = accidentReliefFund;
    }

    public String getResvOtherDiscount() {
        return resvOtherDiscount;
    }

    public void setResvOtherDiscount(String resvOtherDiscount) {
        this.resvOtherDiscount = resvOtherDiscount;
    }
}
