package com.gsrtc.mobileweb.models.ticket_cancellation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Swastik on 5/8/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmPartialCancellationResponse implements Serializable {
    private String counterCode;

    private String canclBridgeFee;

    private String bookingTicketID;

    private String seriesNumber;

    private String totalNumberOfSeats;

    private String canclBasicFare;

    private String reservationFee;

    private String startPlaceID;

    private String otherLevies;

    private String fareAfterRefund;

    private String serviceID;

    private String pnrMasterID;

    private String userName;

    private String seatNosToInActive;

    private String refundPrcntOrLumpsumValue;

    private String canclAccidentReliefFund;

    private String cancellationFee;

    private String canclReservationFee;

    private String concessions;

    private String pnrNumber;

    private String refundPrcntOrLumpsum;

    private String serviceFee;

    private String canclRefundAmount;

    private String journeyDate;

    private String status;

    private String bridgeFee;

    private String cancelOtherDiscount;

    private String entryFee;

    private String WSRefNo;

    private String resvOtherConcession;

    private String userFee;

    private String franServiceFee;

    private String collectedBasicFee;

    private String totalFare;

    private String createdBy;

    private String tollFee;

    private String canclTollFee;

    private String canclEntryFee;

    private String[] seatNo;

    private String concessionTypeId;

    private String franchiseeUser;

    private String canclServiceFee;

    private String canclUserFee;

    private String[] seatNumber;

    private String basicFare;

    private String canclOtherConcessions;

    private String endPlaceID;

    private String accidentReliefFund;

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

    public String getBookingTicketID() {
        return bookingTicketID;
    }

    public void setBookingTicketID(String bookingTicketID) {
        this.bookingTicketID = bookingTicketID;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getTotalNumberOfSeats() {
        return totalNumberOfSeats;
    }

    public void setTotalNumberOfSeats(String totalNumberOfSeats) {
        this.totalNumberOfSeats = totalNumberOfSeats;
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

    public String getConcessions() {
        return concessions;
    }

    public void setConcessions(String concessions) {
        this.concessions = concessions;
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

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFranServiceFee() {
        return franServiceFee;
    }

    public void setFranServiceFee(String franServiceFee) {
        this.franServiceFee = franServiceFee;
    }

    public String getCollectedBasicFee() {
        return collectedBasicFee;
    }

    public void setCollectedBasicFee(String collectedBasicFee) {
        this.collectedBasicFee = collectedBasicFee;
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

    public String[] getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String[] seatNo) {
        this.seatNo = seatNo;
    }

    public String getConcessionTypeId() {
        return concessionTypeId;
    }

    public void setConcessionTypeId(String concessionTypeId) {
        this.concessionTypeId = concessionTypeId;
    }

    public String getFranchiseeUser() {
        return franchiseeUser;
    }

    public void setFranchiseeUser(String franchiseeUser) {
        this.franchiseeUser = franchiseeUser;
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
}
