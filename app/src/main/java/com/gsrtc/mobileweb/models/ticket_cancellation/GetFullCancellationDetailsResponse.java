package com.gsrtc.mobileweb.models.ticket_cancellation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import com.gsrtc.mobileweb.utils.JacksonUtil;

/**
 * Created by Swastik on 5/8/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetFullCancellationDetailsResponse implements Serializable {
    private String pickupPointTime;

    private String counterCode;

    private String totalNumberOfAdultFemales;

    private String modeOfPayment;

    private String messageFromCorp;

    private String classID;

    private String bookingTicketID;

    private String seriesNumber;

    private String totalNumberOfSeats;

    private String tripSheetPrintTime;

    private String ticketNumber;

    private String startPlaceID;

    private String startPlaceCode;

    private String pickupPointDropOffTime;

    private String totalNumberOfAdults;

    private String platformNumber;

    private String cancellationType;

    private String tripCode;

    private String corpCode;

    private String startPlaceName;

    private String serviceID;

    private String totalNumberOfChildFemales;

    private String modeOfPaymentLookupID;

    private String pnrMasterID;

    private String seatNosToInActive;

    private String userName;

    private String serviceDepartureTime;

    private String totalNumberOfChildMales;

    private String pnrNumber;

    private String pickupPointPlaceId;

    private String journeyDate;

    private String status;

    private String origin;

    private String pickupPointDropOffId;

    private String WSRefNo;

    private String refNumber;

    private String destination;

    private String routeNo;

    private String endPlaceName;

    private String createdBy;

    private String seatNosToDisplay;

    private String endPlaceCode;

    private String passengerName;

    private String concessionTypeId;

    private String franchiseeUser;

    private String totalNumberOfAdultMales;

    private String endPlaceID;

    private String totalNumberOfChild;

    public String getPickupPointTime() {
        return pickupPointTime;
    }

    public void setPickupPointTime(String pickupPointTime) {
        this.pickupPointTime = pickupPointTime;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getTotalNumberOfAdultFemales() {
        return totalNumberOfAdultFemales;
    }

    public void setTotalNumberOfAdultFemales(String totalNumberOfAdultFemales) {
        this.totalNumberOfAdultFemales = totalNumberOfAdultFemales;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getMessageFromCorp() {
        return messageFromCorp;
    }

    public void setMessageFromCorp(String messageFromCorp) {
        this.messageFromCorp = messageFromCorp;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
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

    public String getTripSheetPrintTime() {
        return tripSheetPrintTime;
    }

    public void setTripSheetPrintTime(String tripSheetPrintTime) {
        this.tripSheetPrintTime = tripSheetPrintTime;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getStartPlaceID() {
        return startPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        this.startPlaceID = startPlaceID;
    }

    public String getStartPlaceCode() {
        return startPlaceCode;
    }

    public void setStartPlaceCode(String startPlaceCode) {
        this.startPlaceCode = startPlaceCode;
    }

    public String getPickupPointDropOffTime() {
        return pickupPointDropOffTime;
    }

    public void setPickupPointDropOffTime(String pickupPointDropOffTime) {
        this.pickupPointDropOffTime = pickupPointDropOffTime;
    }

    public String getTotalNumberOfAdults() {
        return totalNumberOfAdults;
    }

    public void setTotalNumberOfAdults(String totalNumberOfAdults) {
        this.totalNumberOfAdults = totalNumberOfAdults;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber;
    }

    public String getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(String cancellationType) {
        this.cancellationType = cancellationType;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getTotalNumberOfChildFemales() {
        return totalNumberOfChildFemales;
    }

    public void setTotalNumberOfChildFemales(String totalNumberOfChildFemales) {
        this.totalNumberOfChildFemales = totalNumberOfChildFemales;
    }

    public String getModeOfPaymentLookupID() {
        return modeOfPaymentLookupID;
    }

    public void setModeOfPaymentLookupID(String modeOfPaymentLookupID) {
        this.modeOfPaymentLookupID = modeOfPaymentLookupID;
    }

    public String getPnrMasterID() {
        return pnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        this.pnrMasterID = pnrMasterID;
    }

    public String getSeatNosToInActive() {
        return seatNosToInActive;
    }

    public void setSeatNosToInActive(String seatNosToInActive) {
        this.seatNosToInActive = seatNosToInActive;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceDepartureTime() {
        return serviceDepartureTime;
    }

    public void setServiceDepartureTime(String serviceDepartureTime) {
        this.serviceDepartureTime = serviceDepartureTime;
    }

    public String getTotalNumberOfChildMales() {
        return totalNumberOfChildMales;
    }

    public void setTotalNumberOfChildMales(String totalNumberOfChildMales) {
        this.totalNumberOfChildMales = totalNumberOfChildMales;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getPickupPointPlaceId() {
        return pickupPointPlaceId;
    }

    public void setPickupPointPlaceId(String pickupPointPlaceId) {
        this.pickupPointPlaceId = pickupPointPlaceId;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPickupPointDropOffId() {
        return pickupPointDropOffId;
    }

    public void setPickupPointDropOffId(String pickupPointDropOffId) {
        this.pickupPointDropOffId = pickupPointDropOffId;
    }

    @JsonProperty("WSRefNo")
    public String getWSRefNo() {
        return WSRefNo;
    }

    public void setWSRefNo(String WSRefNo) {
        this.WSRefNo = WSRefNo;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSeatNosToDisplay() {
        return seatNosToDisplay;
    }

    public void setSeatNosToDisplay(String seatNosToDisplay) {
        this.seatNosToDisplay = seatNosToDisplay;
    }

    public String getEndPlaceCode() {
        return endPlaceCode;
    }

    public void setEndPlaceCode(String endPlaceCode) {
        this.endPlaceCode = endPlaceCode;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    public String getTotalNumberOfAdultMales() {
        return totalNumberOfAdultMales;
    }

    public void setTotalNumberOfAdultMales(String totalNumberOfAdultMales) {
        this.totalNumberOfAdultMales = totalNumberOfAdultMales;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
    }

    public String getTotalNumberOfChild() {
        return totalNumberOfChild;
    }

    public void setTotalNumberOfChild(String totalNumberOfChild) {
        this.totalNumberOfChild = totalNumberOfChild;
    }

    public GetPartialCancellationDetailsResponse toPartialCancellationDetails() {

        GetPartialCancellationDetailsResponse response = null;
        try{
            String jsonString = JacksonUtil.getMapper().writeValueAsString(this);
            JSONObject json = new JSONObject(jsonString);
            response = JacksonUtil.getMapper().readValue(json.toString(), new TypeReference<GetPartialCancellationDetailsResponse>() {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
