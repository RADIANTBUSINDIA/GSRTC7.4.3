package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by mail4 on 30-12-2017.
 */

public class SearchParamsCurrent implements Serializable{

    Boolean isRoundTrip;
    Place fromLocation;
    Place toLocation;
    Integer numOfPassengers;
    String onWardDate, returnDate;
    Integer adultCount = 0;
    Integer childrenCount = 0;

    String passengerName;
    String passengerEmail;
    String passengerMobile;


    String pickServiceId;
    String JourneyDate;
    String ClassId;
    String StartPlaceID;
    String EndPlaceID;
    String AdultMales;
    String AdultFemale;
    String ChildMales;
    String ChildFemale;
    String ConcessionID;
    String OnewayOrReturnTrip;
    String ServiceRouteName;
    String crntlanguage;
    String kms;

     String accidentReliefFund_GetCurrentTotalFareDetailsOfTicket;
     String adultFare_GetCurrentTotalFareDetailsOfTicket;
     String adultFemale_GetCurrentTotalFareDetailsOfTicket;
     String adultMale_GetCurrentTotalFareDetailsOfTicket;
     String adultMales_GetCurrentTotalFareDetailsOfTicket;
     String adultTicketFare_GetCurrentTotalFareDetailsOfTicket;
     String basicFare_GetCurrentTotalFareDetailsOfTicket;
     String bridgeFee_GetCurrentTotalFareDetailsOfTicket;
     String childFare_GetCurrentTotalFareDetailsOfTicket;
     String childFemale_GetCurrentTotalFareDetailsOfTicket;
     String childMale_GetCurrentTotalFareDetailsOfTicket;
     String childMales_GetCurrentTotalFareDetailsOfTicket;
     String childTicketFare_GetCurrentTotalFareDetailsOfTicket;
     String classId_GetCurrentTotalFareDetailsOfTicket;
     String concessionID_GetCurrentTotalFareDetailsOfTicket;
     String concessionPercentage_GetCurrentTotalFareDetailsOfTicket;
     String concessionTypeId_GetCurrentTotalFareDetailsOfTicket;
     String counterCode_GetCurrentTotalFareDetailsOfTicket;
     String createdBy_GetCurrentTotalFareDetailsOfTicket;
     String discountPercentage_GetCurrentTotalFareDetailsOfTicket;
     String discounts_GetCurrentTotalFareDetailsOfTicket;
     String endPlaceID_GetCurrentTotalFareDetailsOfTicket;
     String entryFee_GetCurrentTotalFareDetailsOfTicket;
     String fareWithOutbasic_GetCurrentTotalFareDetailsOfTicket;
     String journeyDate_GetCurrentTotalFareDetailsOfTicket;
     String messageFromCorp_GetCurrentTotalFareDetailsOfTicket;
     String onewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket;
     String onlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket;
     String otherLevies_GetCurrentTotalFareDetailsOfTicket;
     String pickServiceId_GetCurrentTotalFareDetailsOfTicket;
     String reservationFee_GetCurrentTotalFareDetailsOfTicket;
     String startPlaceID_GetCurrentTotalFareDetailsOfTicket;
     String status_GetCurrentTotalFareDetailsOfTicket;
     String tollFee_GetCurrentTotalFareDetailsOfTicket;
     String totalAmount_GetCurrentTotalFareDetailsOfTicket;
     String totalFare_GetCurrentTotalFareDetailsOfTicket;
     String totalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket;
     String userFee_GetCurrentTotalFareDetailsOfTicket;
     String userName_GetCurrentTotalFareDetailsOfTicket;
     String weekdayConcession_GetCurrentTotalFareDetailsOfTicket;

    String adultFareServiceDetails;
    String childFareServiceDetails;

    String gatewayHDFC;
    String gatewayLookupId;
    String gatewayBILLDESK;

    String adultTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    String childTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    String gsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse;
    String pnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse;
    String pnrNumberSaveCurrentBookingDetailsTemporarilyResponse;
    String bookingTransactionID;
    String confirmStatus;
    String departureTime;
    String routeNameAppended;

    String adultFareGetCurrentServiceDetails;
    String adultTicketFareGetCurrentServiceDetails;
    String childFareGetCurrentServiceDetails;
    String childTicketFareGetCurrentServiceDetails;
    String classIDGetCurrentServiceDetails;
    String conductorMobileNoGetCurrentServiceDetails;
    String departureTimeGetCurrentServiceDetails;
    String destinationGetCurrentServiceDetails;
    String journeyDateGetCurrentServiceDetails;
    String journeyHoursGetCurrentServiceDetails;
    String lastBusStationGetCurrentServiceDetails;
    String lastBusStationTimeGetCurrentServiceDetails;
    String originGetCurrentServiceDetails;
    String pickServiceIdGetCurrentServiceDetails;
    String serviceClassGetCurrentServiceDetails;
    String serviceIDGetCurrentServiceDetails;
    String serviceOrginTimeGetCurrentServiceDetails;
    String serviceRouteIdGetCurrentServiceDetails;
    String serviceRouteNameGetCurrentServiceDetails;
    String tripCodeGetCurrentServiceDetails;
    String vehicleIdGetCurrentServiceDetails;
    String vehicleNoGetCurrentServiceDetails;
    String vehicleStatusGetCurrentServiceDetails;
    String seatavailbleGetCurrentServiceDetails;


    public String getAccidentReliefFund_GetCurrentTotalFareDetailsOfTicket() {
        return accidentReliefFund_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAccidentReliefFund_GetCurrentTotalFareDetailsOfTicket(String accidentReliefFund_GetCurrentTotalFareDetailsOfTicket) {
        this.accidentReliefFund_GetCurrentTotalFareDetailsOfTicket = accidentReliefFund_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getAdultFare_GetCurrentTotalFareDetailsOfTicket() {
        return adultFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAdultFare_GetCurrentTotalFareDetailsOfTicket(String adultFare_GetCurrentTotalFareDetailsOfTicket) {
        this.adultFare_GetCurrentTotalFareDetailsOfTicket = adultFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getAdultFemale_GetCurrentTotalFareDetailsOfTicket() {
        return adultFemale_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAdultFemale_GetCurrentTotalFareDetailsOfTicket(String adultFemale_GetCurrentTotalFareDetailsOfTicket) {
        this.adultFemale_GetCurrentTotalFareDetailsOfTicket = adultFemale_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getAdultMale_GetCurrentTotalFareDetailsOfTicket() {
        return adultMale_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAdultMale_GetCurrentTotalFareDetailsOfTicket(String adultMale_GetCurrentTotalFareDetailsOfTicket) {
        this.adultMale_GetCurrentTotalFareDetailsOfTicket = adultMale_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getAdultMales_GetCurrentTotalFareDetailsOfTicket() {
        return adultMales_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAdultMales_GetCurrentTotalFareDetailsOfTicket(String adultMales_GetCurrentTotalFareDetailsOfTicket) {
        this.adultMales_GetCurrentTotalFareDetailsOfTicket = adultMales_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getAdultTicketFare_GetCurrentTotalFareDetailsOfTicket() {
        return adultTicketFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setAdultTicketFare_GetCurrentTotalFareDetailsOfTicket(String adultTicketFare_GetCurrentTotalFareDetailsOfTicket) {
        this.adultTicketFare_GetCurrentTotalFareDetailsOfTicket = adultTicketFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getBasicFare_GetCurrentTotalFareDetailsOfTicket() {
        return basicFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setBasicFare_GetCurrentTotalFareDetailsOfTicket(String basicFare_GetCurrentTotalFareDetailsOfTicket) {
        this.basicFare_GetCurrentTotalFareDetailsOfTicket = basicFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getBridgeFee_GetCurrentTotalFareDetailsOfTicket() {
        return bridgeFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setBridgeFee_GetCurrentTotalFareDetailsOfTicket(String bridgeFee_GetCurrentTotalFareDetailsOfTicket) {
        this.bridgeFee_GetCurrentTotalFareDetailsOfTicket = bridgeFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getChildFare_GetCurrentTotalFareDetailsOfTicket() {
        return childFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setChildFare_GetCurrentTotalFareDetailsOfTicket(String childFare_GetCurrentTotalFareDetailsOfTicket) {
        this.childFare_GetCurrentTotalFareDetailsOfTicket = childFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getChildFemale_GetCurrentTotalFareDetailsOfTicket() {
        return childFemale_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setChildFemale_GetCurrentTotalFareDetailsOfTicket(String childFemale_GetCurrentTotalFareDetailsOfTicket) {
        this.childFemale_GetCurrentTotalFareDetailsOfTicket = childFemale_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getChildMale_GetCurrentTotalFareDetailsOfTicket() {
        return childMale_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setChildMale_GetCurrentTotalFareDetailsOfTicket(String childMale_GetCurrentTotalFareDetailsOfTicket) {
        this.childMale_GetCurrentTotalFareDetailsOfTicket = childMale_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getChildMales_GetCurrentTotalFareDetailsOfTicket() {
        return childMales_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setChildMales_GetCurrentTotalFareDetailsOfTicket(String childMales_GetCurrentTotalFareDetailsOfTicket) {
        this.childMales_GetCurrentTotalFareDetailsOfTicket = childMales_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getChildTicketFare_GetCurrentTotalFareDetailsOfTicket() {
        return childTicketFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setChildTicketFare_GetCurrentTotalFareDetailsOfTicket(String childTicketFare_GetCurrentTotalFareDetailsOfTicket) {
        this.childTicketFare_GetCurrentTotalFareDetailsOfTicket = childTicketFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getClassId_GetCurrentTotalFareDetailsOfTicket() {
        return classId_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setClassId_GetCurrentTotalFareDetailsOfTicket(String classId_GetCurrentTotalFareDetailsOfTicket) {
        this.classId_GetCurrentTotalFareDetailsOfTicket = classId_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getConcessionID_GetCurrentTotalFareDetailsOfTicket() {
        return concessionID_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setConcessionID_GetCurrentTotalFareDetailsOfTicket(String concessionID_GetCurrentTotalFareDetailsOfTicket) {
        this.concessionID_GetCurrentTotalFareDetailsOfTicket = concessionID_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getConcessionPercentage_GetCurrentTotalFareDetailsOfTicket() {
        return concessionPercentage_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setConcessionPercentage_GetCurrentTotalFareDetailsOfTicket(String concessionPercentage_GetCurrentTotalFareDetailsOfTicket) {
        this.concessionPercentage_GetCurrentTotalFareDetailsOfTicket = concessionPercentage_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getConcessionTypeId_GetCurrentTotalFareDetailsOfTicket() {
        return concessionTypeId_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setConcessionTypeId_GetCurrentTotalFareDetailsOfTicket(String concessionTypeId_GetCurrentTotalFareDetailsOfTicket) {
        this.concessionTypeId_GetCurrentTotalFareDetailsOfTicket = concessionTypeId_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getCounterCode_GetCurrentTotalFareDetailsOfTicket() {
        return counterCode_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setCounterCode_GetCurrentTotalFareDetailsOfTicket(String counterCode_GetCurrentTotalFareDetailsOfTicket) {
        this.counterCode_GetCurrentTotalFareDetailsOfTicket = counterCode_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getCreatedBy_GetCurrentTotalFareDetailsOfTicket() {
        return createdBy_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setCreatedBy_GetCurrentTotalFareDetailsOfTicket(String createdBy_GetCurrentTotalFareDetailsOfTicket) {
        this.createdBy_GetCurrentTotalFareDetailsOfTicket = createdBy_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getDiscountPercentage_GetCurrentTotalFareDetailsOfTicket() {
        return discountPercentage_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setDiscountPercentage_GetCurrentTotalFareDetailsOfTicket(String discountPercentage_GetCurrentTotalFareDetailsOfTicket) {
        this.discountPercentage_GetCurrentTotalFareDetailsOfTicket = discountPercentage_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getDiscounts_GetCurrentTotalFareDetailsOfTicket() {
        return discounts_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setDiscounts_GetCurrentTotalFareDetailsOfTicket(String discounts_GetCurrentTotalFareDetailsOfTicket) {
        this.discounts_GetCurrentTotalFareDetailsOfTicket = discounts_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getEndPlaceID_GetCurrentTotalFareDetailsOfTicket() {
        return endPlaceID_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setEndPlaceID_GetCurrentTotalFareDetailsOfTicket(String endPlaceID_GetCurrentTotalFareDetailsOfTicket) {
        this.endPlaceID_GetCurrentTotalFareDetailsOfTicket = endPlaceID_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getEntryFee_GetCurrentTotalFareDetailsOfTicket() {
        return entryFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setEntryFee_GetCurrentTotalFareDetailsOfTicket(String entryFee_GetCurrentTotalFareDetailsOfTicket) {
        this.entryFee_GetCurrentTotalFareDetailsOfTicket = entryFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getFareWithOutbasic_GetCurrentTotalFareDetailsOfTicket() {
        return fareWithOutbasic_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setFareWithOutbasic_GetCurrentTotalFareDetailsOfTicket(String fareWithOutbasic_GetCurrentTotalFareDetailsOfTicket) {
        this.fareWithOutbasic_GetCurrentTotalFareDetailsOfTicket = fareWithOutbasic_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getJourneyDate_GetCurrentTotalFareDetailsOfTicket() {
        return journeyDate_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setJourneyDate_GetCurrentTotalFareDetailsOfTicket(String journeyDate_GetCurrentTotalFareDetailsOfTicket) {
        this.journeyDate_GetCurrentTotalFareDetailsOfTicket = journeyDate_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getMessageFromCorp_GetCurrentTotalFareDetailsOfTicket() {
        return messageFromCorp_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setMessageFromCorp_GetCurrentTotalFareDetailsOfTicket(String messageFromCorp_GetCurrentTotalFareDetailsOfTicket) {
        this.messageFromCorp_GetCurrentTotalFareDetailsOfTicket = messageFromCorp_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getOnewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket() {
        return onewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setOnewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket(String onewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket) {
        this.onewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket = onewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getOnlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket() {
        return onlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setOnlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket(String onlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket) {
        this.onlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket = onlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getOtherLevies_GetCurrentTotalFareDetailsOfTicket() {
        return otherLevies_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setOtherLevies_GetCurrentTotalFareDetailsOfTicket(String otherLevies_GetCurrentTotalFareDetailsOfTicket) {
        this.otherLevies_GetCurrentTotalFareDetailsOfTicket = otherLevies_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getPickServiceId_GetCurrentTotalFareDetailsOfTicket() {
        return pickServiceId_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setPickServiceId_GetCurrentTotalFareDetailsOfTicket(String pickServiceId_GetCurrentTotalFareDetailsOfTicket) {
        this.pickServiceId_GetCurrentTotalFareDetailsOfTicket = pickServiceId_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getReservationFee_GetCurrentTotalFareDetailsOfTicket() {
        return reservationFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setReservationFee_GetCurrentTotalFareDetailsOfTicket(String reservationFee_GetCurrentTotalFareDetailsOfTicket) {
        this.reservationFee_GetCurrentTotalFareDetailsOfTicket = reservationFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getStartPlaceID_GetCurrentTotalFareDetailsOfTicket() {
        return startPlaceID_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setStartPlaceID_GetCurrentTotalFareDetailsOfTicket(String startPlaceID_GetCurrentTotalFareDetailsOfTicket) {
        this.startPlaceID_GetCurrentTotalFareDetailsOfTicket = startPlaceID_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getStatus_GetCurrentTotalFareDetailsOfTicket() {
        return status_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setStatus_GetCurrentTotalFareDetailsOfTicket(String status_GetCurrentTotalFareDetailsOfTicket) {
        this.status_GetCurrentTotalFareDetailsOfTicket = status_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getTollFee_GetCurrentTotalFareDetailsOfTicket() {
        return tollFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setTollFee_GetCurrentTotalFareDetailsOfTicket(String tollFee_GetCurrentTotalFareDetailsOfTicket) {
        this.tollFee_GetCurrentTotalFareDetailsOfTicket = tollFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getTotalAmount_GetCurrentTotalFareDetailsOfTicket() {
        return totalAmount_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setTotalAmount_GetCurrentTotalFareDetailsOfTicket(String totalAmount_GetCurrentTotalFareDetailsOfTicket) {
        this.totalAmount_GetCurrentTotalFareDetailsOfTicket = totalAmount_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getTotalFare_GetCurrentTotalFareDetailsOfTicket() {
        return totalFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setTotalFare_GetCurrentTotalFareDetailsOfTicket(String totalFare_GetCurrentTotalFareDetailsOfTicket) {
        this.totalFare_GetCurrentTotalFareDetailsOfTicket = totalFare_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getTotalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket() {
        return totalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setTotalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket(String totalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket) {
        this.totalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket = totalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getUserFee_GetCurrentTotalFareDetailsOfTicket() {
        return userFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setUserFee_GetCurrentTotalFareDetailsOfTicket(String userFee_GetCurrentTotalFareDetailsOfTicket) {
        this.userFee_GetCurrentTotalFareDetailsOfTicket = userFee_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getUserName_GetCurrentTotalFareDetailsOfTicket() {
        return userName_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setUserName_GetCurrentTotalFareDetailsOfTicket(String userName_GetCurrentTotalFareDetailsOfTicket) {
        this.userName_GetCurrentTotalFareDetailsOfTicket = userName_GetCurrentTotalFareDetailsOfTicket;
    }

    public String getWeekdayConcession_GetCurrentTotalFareDetailsOfTicket() {
        return weekdayConcession_GetCurrentTotalFareDetailsOfTicket;
    }

    public void setWeekdayConcession_GetCurrentTotalFareDetailsOfTicket(String weekdayConcession_GetCurrentTotalFareDetailsOfTicket) {
        this.weekdayConcession_GetCurrentTotalFareDetailsOfTicket = weekdayConcession_GetCurrentTotalFareDetailsOfTicket;
    }



    public String getAdultFareGetCurrentServiceDetails() {
        return adultFareGetCurrentServiceDetails;
    }

    public void setAdultFareGetCurrentServiceDetails(String adultFareGetCurrentServiceDetails) {
        this.adultFareGetCurrentServiceDetails = adultFareGetCurrentServiceDetails;
    }

    public String getAdultTicketFareGetCurrentServiceDetails() {
        return adultTicketFareGetCurrentServiceDetails;
    }

    public void setAdultTicketFareGetCurrentServiceDetails(String adultTicketFareGetCurrentServiceDetails) {
        this.adultTicketFareGetCurrentServiceDetails = adultTicketFareGetCurrentServiceDetails;
    }

    public String getChildFareGetCurrentServiceDetails() {
        return childFareGetCurrentServiceDetails;
    }

    public void setChildFareGetCurrentServiceDetails(String childFareGetCurrentServiceDetails) {
        this.childFareGetCurrentServiceDetails = childFareGetCurrentServiceDetails;
    }

    public String getChildTicketFareGetCurrentServiceDetails() {
        return childTicketFareGetCurrentServiceDetails;
    }

    public void setChildTicketFareGetCurrentServiceDetails(String childTicketFareGetCurrentServiceDetails) {
        this.childTicketFareGetCurrentServiceDetails = childTicketFareGetCurrentServiceDetails;
    }

    public String getClassIDGetCurrentServiceDetails() {
        return classIDGetCurrentServiceDetails;
    }

    public void setClassIDGetCurrentServiceDetails(String classIDGetCurrentServiceDetails) {
        this.classIDGetCurrentServiceDetails = classIDGetCurrentServiceDetails;
    }

    public String getConductorMobileNoGetCurrentServiceDetails() {
        return conductorMobileNoGetCurrentServiceDetails;
    }

    public void setConductorMobileNoGetCurrentServiceDetails(String conductorMobileNoGetCurrentServiceDetails) {
        this.conductorMobileNoGetCurrentServiceDetails = conductorMobileNoGetCurrentServiceDetails;
    }

    public String getDepartureTimeGetCurrentServiceDetails() {
        return departureTimeGetCurrentServiceDetails;
    }

    public void setDepartureTimeGetCurrentServiceDetails(String departureTimeGetCurrentServiceDetails) {
        this.departureTimeGetCurrentServiceDetails = departureTimeGetCurrentServiceDetails;
    }

    public String getDestinationGetCurrentServiceDetails() {
        return destinationGetCurrentServiceDetails;
    }

    public void setDestinationGetCurrentServiceDetails(String destinationGetCurrentServiceDetails) {
        this.destinationGetCurrentServiceDetails = destinationGetCurrentServiceDetails;
    }

    public String getJourneyDateGetCurrentServiceDetails() {
        return journeyDateGetCurrentServiceDetails;
    }

    public void setJourneyDateGetCurrentServiceDetails(String journeyDateGetCurrentServiceDetails) {
        this.journeyDateGetCurrentServiceDetails = journeyDateGetCurrentServiceDetails;
    }

    public String getJourneyHoursGetCurrentServiceDetails() {
        return journeyHoursGetCurrentServiceDetails;
    }

    public void setJourneyHoursGetCurrentServiceDetails(String journeyHoursGetCurrentServiceDetails) {
        this.journeyHoursGetCurrentServiceDetails = journeyHoursGetCurrentServiceDetails;
    }

    public String getLastBusStationGetCurrentServiceDetails() {
        return lastBusStationGetCurrentServiceDetails;
    }

    public void setLastBusStationGetCurrentServiceDetails(String lastBusStationGetCurrentServiceDetails) {
        this.lastBusStationGetCurrentServiceDetails = lastBusStationGetCurrentServiceDetails;
    }

    public String getLastBusStationTimeGetCurrentServiceDetails() {
        return lastBusStationTimeGetCurrentServiceDetails;
    }

    public void setLastBusStationTimeGetCurrentServiceDetails(String lastBusStationTimeGetCurrentServiceDetails) {
        this.lastBusStationTimeGetCurrentServiceDetails = lastBusStationTimeGetCurrentServiceDetails;
    }

    public String getOriginGetCurrentServiceDetails() {
        return originGetCurrentServiceDetails;
    }

    public void setOriginGetCurrentServiceDetails(String originGetCurrentServiceDetails) {
        this.originGetCurrentServiceDetails = originGetCurrentServiceDetails;
    }

    public String getPickServiceIdGetCurrentServiceDetails() {
        return pickServiceIdGetCurrentServiceDetails;
    }

    public void setPickServiceIdGetCurrentServiceDetails(String pickServiceIdGetCurrentServiceDetails) {
        this.pickServiceIdGetCurrentServiceDetails = pickServiceIdGetCurrentServiceDetails;
    }

    public String getServiceClassGetCurrentServiceDetails() {
        return serviceClassGetCurrentServiceDetails;
    }

    public void setServiceClassGetCurrentServiceDetails(String serviceClassGetCurrentServiceDetails) {
        this.serviceClassGetCurrentServiceDetails = serviceClassGetCurrentServiceDetails;
    }

    public String getServiceIDGetCurrentServiceDetails() {
        return serviceIDGetCurrentServiceDetails;
    }

    public void setServiceIDGetCurrentServiceDetails(String serviceIDGetCurrentServiceDetails) {
        this.serviceIDGetCurrentServiceDetails = serviceIDGetCurrentServiceDetails;
    }

    public String getServiceOrginTimeGetCurrentServiceDetails() {
        return serviceOrginTimeGetCurrentServiceDetails;
    }

    public void setServiceOrginTimeGetCurrentServiceDetails(String serviceOrginTimeGetCurrentServiceDetails) {
        this.serviceOrginTimeGetCurrentServiceDetails = serviceOrginTimeGetCurrentServiceDetails;
    }

    public String getServiceRouteIdGetCurrentServiceDetails() {
        return serviceRouteIdGetCurrentServiceDetails;
    }

    public void setServiceRouteIdGetCurrentServiceDetails(String serviceRouteIdGetCurrentServiceDetails) {
        this.serviceRouteIdGetCurrentServiceDetails = serviceRouteIdGetCurrentServiceDetails;
    }

    public String getServiceRouteNameGetCurrentServiceDetails() {
        return serviceRouteNameGetCurrentServiceDetails;
    }

    public void setServiceRouteNameGetCurrentServiceDetails(String serviceRouteNameGetCurrentServiceDetails) {
        this.serviceRouteNameGetCurrentServiceDetails = serviceRouteNameGetCurrentServiceDetails;
    }

    public String getTripCodeGetCurrentServiceDetails() {
        return tripCodeGetCurrentServiceDetails;
    }

    public void setTripCodeGetCurrentServiceDetails(String tripCodeGetCurrentServiceDetails) {
        this.tripCodeGetCurrentServiceDetails = tripCodeGetCurrentServiceDetails;
    }

    public String getVehicleIdGetCurrentServiceDetails() {
        return vehicleIdGetCurrentServiceDetails;
    }

    public void setVehicleIdGetCurrentServiceDetails(String vehicleIdGetCurrentServiceDetails) {
        this.vehicleIdGetCurrentServiceDetails = vehicleIdGetCurrentServiceDetails;
    }

    public String getVehicleNoGetCurrentServiceDetails() {
        return vehicleNoGetCurrentServiceDetails;
    }

    public void setVehicleNoGetCurrentServiceDetails(String vehicleNoGetCurrentServiceDetails) {
        this.vehicleNoGetCurrentServiceDetails = vehicleNoGetCurrentServiceDetails;
    }

    public String getVehicleStatusGetCurrentServiceDetails() {
        return vehicleStatusGetCurrentServiceDetails;
    }

    public void setVehicleStatusGetCurrentServiceDetails(String vehicleStatusGetCurrentServiceDetails) {
        this.vehicleStatusGetCurrentServiceDetails = vehicleStatusGetCurrentServiceDetails;
    }

    public String getSeatavailbleGetCurrentServiceDetails() {
        return seatavailbleGetCurrentServiceDetails;
    }

    public void setSeatavailbleGetCurrentServiceDetails(String seatavailbleGetCurrentServiceDetails) {
        this.seatavailbleGetCurrentServiceDetails = seatavailbleGetCurrentServiceDetails;
    }



    public String getRouteNameAppended() {
        return routeNameAppended;
    }

    public void setRouteNameAppended(String routeNameAppended) {
        this.routeNameAppended = routeNameAppended;
    }



    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }



    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }




    public String getBookingTransactionID() {
        return bookingTransactionID;
    }

    public void setBookingTransactionID(String bookingTransactionID) {
        this.bookingTransactionID = bookingTransactionID;
    }



    public String getAdultTicketFareSaveCurrentBookingDetailsTemporarilyResponse() {
        return adultTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public void setAdultTicketFareSaveCurrentBookingDetailsTemporarilyResponse(String adultTicketFareSaveCurrentBookingDetailsTemporarilyResponse) {
        this.adultTicketFareSaveCurrentBookingDetailsTemporarilyResponse = adultTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public String getChildTicketFareSaveCurrentBookingDetailsTemporarilyResponse() {
        return childTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public void setChildTicketFareSaveCurrentBookingDetailsTemporarilyResponse(String childTicketFareSaveCurrentBookingDetailsTemporarilyResponse) {
        this.childTicketFareSaveCurrentBookingDetailsTemporarilyResponse = childTicketFareSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public String getGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse() {
        return gsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public void setGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse(String gsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse) {
        this.gsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse = gsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public String getPnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse() {
        return pnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public void setPnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse(String pnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse) {
        this.pnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse = pnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public String getPnrNumberSaveCurrentBookingDetailsTemporarilyResponse() {
        return pnrNumberSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public void setPnrNumberSaveCurrentBookingDetailsTemporarilyResponse(String pnrNumberSaveCurrentBookingDetailsTemporarilyResponse) {
        this.pnrNumberSaveCurrentBookingDetailsTemporarilyResponse = pnrNumberSaveCurrentBookingDetailsTemporarilyResponse;
    }

    public String getGatewayHDFC() {
        return gatewayHDFC;
    }

    public void setGatewayHDFC(String gatewayHDFC) {
        this.gatewayHDFC = gatewayHDFC;
    }

    public String getGatewayLookupId() {
        return gatewayLookupId;
    }

    public void setGatewayLookupId(String gatewayLookupId) {
        this.gatewayLookupId = gatewayLookupId;
    }

    public String getGatewayBILLDESK() {
        return gatewayBILLDESK;
    }

    public void setGatewayBILLDESK(String gatewayBILLDESK) {
        this.gatewayBILLDESK = gatewayBILLDESK;
    }


    public String getAdultFareServiceDetails() {
        return adultFareServiceDetails;
    }

    public void setAdultFareServiceDetails(String adultFareServiceDetails) {
        this.adultFareServiceDetails = adultFareServiceDetails;
    }

    public String getChildFareServiceDetails() {
        return childFareServiceDetails;
    }

    public void setChildFareServiceDetails(String childFareServiceDetails) {
        this.childFareServiceDetails = childFareServiceDetails;
    }


    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerMobile() {
        return passengerMobile;
    }

    public void setPassengerMobile(String passengerMobile) {
        this.passengerMobile = passengerMobile;
    }








    public String getServiceRouteName() {
        return ServiceRouteName;
    }

    public void setServiceRouteName(String serviceRouteName) {
        ServiceRouteName = serviceRouteName;
    }



    public void setNumOfPassengers(Integer numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public String getJourneyDate() {
        return JourneyDate;
    }

    public void setJourneyDate(String journeyDate) {
        JourneyDate = journeyDate;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getStartPlaceID() {
        return StartPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        StartPlaceID = startPlaceID;
    }

    public String getEndPlaceID() {
        return EndPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        EndPlaceID = endPlaceID;
    }

    public String getAdultMales() {
        return AdultMales;
    }

    public void setAdultMales(String adultMales) {
        AdultMales = adultMales;
    }

    public String getAdultFemale() {
        return AdultFemale;
    }

    public void setAdultFemale(String adultFemale) {
        AdultFemale = adultFemale;
    }

    public String getChildMales() {
        return ChildMales;
    }

    public void setChildMales(String childMales) {
        ChildMales = childMales;
    }

    public String getChildFemale() {
        return ChildFemale;
    }

    public void setChildFemale(String childFemale) {
        ChildFemale = childFemale;
    }

    public String getConcessionID() {
        return ConcessionID;
    }

    public void setConcessionID(String concessionID) {
        ConcessionID = concessionID;
    }

    public String getOnewayOrReturnTrip() {
        return OnewayOrReturnTrip;
    }

    public void setOnewayOrReturnTrip(String onewayOrReturnTrip) {
        OnewayOrReturnTrip = onewayOrReturnTrip;
    }

    public String getPickServiceId() {
        return pickServiceId;
    }

    public void setPickServiceId(String pickServiceId) {
        this.pickServiceId = pickServiceId;
    }



    public Boolean getRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(Boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public Place getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Place fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Place getToLocation() {
        return toLocation;
    }

    public void setToLocation(Place toLocation) {
        this.toLocation = toLocation;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public String getOnWardDate() {
        return onWardDate;
    }

    public void setOnWardDate(String onWardDate) {
        this.onWardDate = onWardDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public String getCrntlanguage() {
        return crntlanguage;
    }

    public void setCrntlanguage(String crntlanguage) {
        this.crntlanguage = crntlanguage;
    }
    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

}
