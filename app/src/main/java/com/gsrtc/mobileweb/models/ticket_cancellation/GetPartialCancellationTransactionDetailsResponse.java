package com.gsrtc.mobileweb.models.ticket_cancellation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Swastik on 5/8/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPartialCancellationTransactionDetailsResponse implements Serializable {
    private String counterCode;

    private String canclBridgeFee;

    private String canclOtherLevies;

    private String canclBasicFare;

    private String reservationFee;

    private String startPlaceID;

    private String otherLevies;

    private String resvCancellationFee;

    private String fareAfterRefund;

    private String serviceID;

    private String userName;

    private String netRefundAmount;

    private String refundPrcntOrLumpsumValue;

    private String canclAccidentReliefFund;

    private String cancellationFee;

    private String canclReservationFee;

    private String pnrNumber;

    private String refundPrcntOrLumpsum;

    private String canclRefundAmount;

    private String journeyDate;

    private String bridgeFee;

    private String cancelOtherDiscount;

    private String WSRefNo;

    private String entryFee;

    private String resvOtherConcession;

    private String userFee;

    private String totalFare;

    private String createdBy;

    private String tollFee;

    private String canclTollFee;

    private String canclEntryFee;

    private String franchiseeUser;

    private String concessionTypeId;

    private String canclServiceFee;

    private String canclUserFee;

    private String[] seatNumber;

    private String basicFare;

    private String canclOtherConcessions;

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

    public String getResvCancellationFee() {
        return resvCancellationFee;
    }

    public void setResvCancellationFee(String resvCancellationFee) {
        this.resvCancellationFee = resvCancellationFee;
    }

    public String getFareAfterRefund() {
        return fareAfterRefund;
    }

    public void setFareAfterRefund(String fareAfterRefund) {
        this.fareAfterRefund = fareAfterRefund;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
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

    @JsonProperty("WSRefNo")
    public String getWSRefNo() {
        return WSRefNo;
    }

    public void setWSRefNo(String WSRefNo) {
        this.WSRefNo = WSRefNo;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
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

    public String getCanclServiceFee() {
        return canclServiceFee;
    }

    public void setCanclServiceFee(String canclServiceFee) {
        this.canclServiceFee = canclServiceFee;
    }

    public String getCanclUserFee() {
        return canclUserFee;
    }

    public void setCanclUserFee(String canclUserFee) {
        this.canclUserFee = canclUserFee;
    }

    public String[] getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String[] seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(String basicFare) {
        this.basicFare = basicFare;
    }

    public String getCanclOtherConcessions() {
        return canclOtherConcessions;
    }

    public void setCanclOtherConcessions(String canclOtherConcessions) {
        this.canclOtherConcessions = canclOtherConcessions;
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
