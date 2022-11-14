package com.gsrtc.mobileweb.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Swastik on 4/20/2017.
 */

public class Booking implements Serializable {
    BusService busService;
    List<Seat> selectedSeats;
    List<Passenger> passengers;
    SearchParams searchParams;
    boolean isReturn;   //false for onward
    String journeyDate;
    Place startPlace;
    Place endPlace;
    String referenceNumber;
    AddBlockSeatsResponse addBlockSeatsResponse;
    //GetReferenceNumberResponse referenceNumberResponse;
    GetAllServicePickupPointsByServiceIDResponse pickupPointDetails;
    GetAllServicePickupPointsByServiceIDResponse dropPointDetails;
    GetTotalFareDetailsOfTicketResponse totalFareDetailsOfTicketResponse;
    PassengerInfo passengerInfo;
    private String textlanguage;

    SaveBookingDetailsTemporary saveBookingDetailsTemporary;

    SaveReturnBookingDetailsTemporary saveReturnBookingDetailsTemporary;

    ConfirmAdvSeatBookingResponse confirmAdvSeatBookingResponse;

    ConfirmAdvSeatBookingAndroidResponse confirmAdvSeatBookingAndroidResponse;

    ConfirmRtnSeatBookingAndroidResponse confirmRtnSeatBookingAndroidResponse;

    public BusService getBusService() {
        return busService;
    }

    public void setBusService(BusService busService) {
        this.busService = busService;
    }

    public List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<Seat> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public boolean getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public Place getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(Place endPlace) {
        this.endPlace = endPlace;
    }

    public AddBlockSeatsResponse getAddBlockSeatsResponse() {
        return addBlockSeatsResponse;
    }

    public void setAddBlockSeatsResponse(AddBlockSeatsResponse addBlockSeatsResponse) {
        this.addBlockSeatsResponse = addBlockSeatsResponse;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

//    public GetReferenceNumberResponse getReferenceNumberResponse() {
//        return referenceNumberResponse;
//    }
//
//    public void setReferenceNumberResponse(GetReferenceNumberResponse referenceNumberResponse) {
//        this.referenceNumberResponse = referenceNumberResponse;
//    }

    public GetAllServicePickupPointsByServiceIDResponse getPickupPointDetails() {
        return pickupPointDetails;
    }

    public void setPickupPointDetails(GetAllServicePickupPointsByServiceIDResponse pickupPointDetails) {
        this.pickupPointDetails = pickupPointDetails;
    }

    public GetTotalFareDetailsOfTicketResponse getTotalFareDetailsOfTicketResponse() {
        return totalFareDetailsOfTicketResponse;
    }

    public void setTotalFareDetailsOfTicketResponse(GetTotalFareDetailsOfTicketResponse totalFareDetailsOfTicketResponse) {
        this.totalFareDetailsOfTicketResponse = totalFareDetailsOfTicketResponse;
    }

    public PassengerInfo getPassengerInfo() {
        return passengerInfo;
    }

    public void setPassengerInfo(PassengerInfo passengerInfo) {
        this.passengerInfo = passengerInfo;
    }

    public GetAllServicePickupPointsByServiceIDResponse getDropPointDetails() {
        return dropPointDetails;
    }

    public void setDropPointDetails(GetAllServicePickupPointsByServiceIDResponse dropPointDetails) {
        this.dropPointDetails = dropPointDetails;
    }

    public ConfirmAdvSeatBookingResponse getConfirmAdvSeatBookingResponse() {
        return confirmAdvSeatBookingResponse;
    }

    public void setConfirmAdvSeatBookingResponse(ConfirmAdvSeatBookingResponse confirmAdvSeatBookingResponse) {
        this.confirmAdvSeatBookingResponse = confirmAdvSeatBookingResponse;
    }

    public SaveBookingDetailsTemporary getSaveBookingDetailsTemporary() {
        return saveBookingDetailsTemporary;
    }

    public void setSaveBookingDetailsTemporary(SaveBookingDetailsTemporary saveBookingDetailsTemporary) {
        this.saveBookingDetailsTemporary = saveBookingDetailsTemporary;
    }

    public SaveReturnBookingDetailsTemporary getSaveReturnBookingDetailsTemporary() {
        return saveReturnBookingDetailsTemporary;
    }

    public void setSaveReturnBookingDetailsTemporary(SaveReturnBookingDetailsTemporary saveReturnBookingDetailsTemporary) {
        this.saveReturnBookingDetailsTemporary = saveReturnBookingDetailsTemporary;
    }

    public ConfirmAdvSeatBookingAndroidResponse getConfirmAdvSeatBookingAndroidResponse() {
        return confirmAdvSeatBookingAndroidResponse;
    }

    public void setConfirmAdvSeatBookingAndroidResponse(ConfirmAdvSeatBookingAndroidResponse confirmAdvSeatBookingAndroidResponse) {
        this.confirmAdvSeatBookingAndroidResponse = confirmAdvSeatBookingAndroidResponse;
    }

    public ConfirmRtnSeatBookingAndroidResponse getConfirmRtnSeatBookingAndroidResponse() {
        return confirmRtnSeatBookingAndroidResponse;
    }

    public void setConfirmRtnSeatBookingAndroidResponse(ConfirmRtnSeatBookingAndroidResponse confirmRtnSeatBookingAndroidResponse) {
        this.confirmRtnSeatBookingAndroidResponse = confirmRtnSeatBookingAndroidResponse;
    }

    public void setTextlanguage(String textlanguage) {
        this.textlanguage = textlanguage;
    }

    public String getTextlanguage() {
        return textlanguage;
    }
}
