package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

/**
 * Created by bala on 02-01-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmCurrentSeatBookingAndroidResponse implements Serializable{

    String accidentReliefFund;
    String adultFare;
    String adultFemale;
    String adultMale;
    String adultMales;
    String adultTicketFare;
    String authStatus;
    String authenticationStatus;
    String bankID;
    String bankMerchantId;
    String bankRefNo;
    String basicFare;
    String bookingAmount;
    String bookingTransactionID;
    String bridgeFee;
    String childFare;
    String childFemale;
    String childMale;
    String childMales;
    String childTicketFare;
    String classID;
    String classId;
    String concession;
    String concessionID;
    String concessionPercentage;
    String concessionType;
    String concessionTypeId;
    String conductorMobileNo;
    String corpCode;
    String counterCode;
    String createdBy;
    String createdDate;
    String creditCardNumber;
    String departureTime;
    String destination;
    String discountPercentage;
    String discountType;
    String discounts;
    String distance;
    String emailId;
    String endPlaceID;
    String endPoint;
    String entryFee;
    String fareWithOutbasic;
    String fromPlaceName;
    String gatewayAmount;
    String gatewayLookupID;
    String gatewayMoreInfo;
    String gatewayRemarks;
    String gatewayStatus;
    String journeyDate;
    String journeyFromTime;
    String journeyHours;
    String journeyToTime;
    String ksrtcRefNo;
    String lastBusStation;
    String lastBusStationTime;
    String maxValidResvDate;
    String messageFromCorp;
    String modeOfPayment;
    String modifyBy;
    String modifyDate;
    String noOfAdults;
    String noOfChilds;
    String noOfSeats;
    String onewayOrReturnTrip;
    String onlineDifferenceFare;
    String onlineDiscountedFare;
    String onlinePaymentDetailsID;
    String onlinePaymentStatus;
    String origin;
    String otherLevies;
    String passengerDestPlace;
    String passengerName;
    String passengerNames;
    String passengerStartPlace;
    String phoneNumber;
    String pickServiceId;
    String pickupPointList;
    String pickupPointName;
    String pickupPointTime;
    String platformNumber;
    String pnrMasterID;
    String pnrNo;
    String pnrNumber;
    String refNumber;
    String remarks;
    String reservationFee;
    String returnDate;
    String returnDepartureTime;
    String returnEndPlaceID;
    String returnPassengerDestPlace;
    String returnPassengerStartPlace;
    String returnPlatformNumber;
    String returnServiceClass;
    String returnServiceID;
    String returnStartPlaceID;
    String returnTripCode;
    String seatIDs;
    String seatsAvailable;
    String serviceClass;
    String serviceID;
    String serviceOrginTime;
    String serviceRouteId;
    String serviceRouteName;
    String serviceType;
    String sessionID;
    String startPlaceID;
    String startPoint;
    String status;
    String stopBookingTime;
    String tempPNRId;
    String ticketNumber;
    String toPlaceName;
    String tollFee;
    String totFemales;
    String totMales;
    String totalAmount;
    String totalFare;
    String totalFareWithLevies;
    String totalNumberOfSeats;
    String totalfare;
    String tranDateTime;
    String transRefNo;
    String transactionDate;
    String tripCode;
    String userFee;
    String userName;
    String vehicleId;
    String vehicleNo;
    String vehicleStatus;
    String viaPlaces;
    String WSRefNo;
    String weekdayConcession;
    String cPnrStatus;
    String confirmStatus;

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }



    public String getAccidentReliefFund() {
        return accidentReliefFund;
    }

    public void setAccidentReliefFund(String accidentReliefFund) {
        this.accidentReliefFund = accidentReliefFund;
    }

    public String getAdultFare() {
        return adultFare;
    }

    public void setAdultFare(String adultFare) {
        this.adultFare = adultFare;
    }

    public String getAdultFemale() {
        return adultFemale;
    }

    public void setAdultFemale(String adultFemale) {
        this.adultFemale = adultFemale;
    }

    public String getAdultMale() {
        return adultMale;
    }

    public void setAdultMale(String adultMale) {
        this.adultMale = adultMale;
    }

    public String getAdultMales() {
        return adultMales;
    }

    public void setAdultMales(String adultMales) {
        this.adultMales = adultMales;
    }

    public String getAdultTicketFare() {
        return adultTicketFare;
    }

    public void setAdultTicketFare(String adultTicketFare) {
        this.adultTicketFare = adultTicketFare;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(String authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getBankMerchantId() {
        return bankMerchantId;
    }

    public void setBankMerchantId(String bankMerchantId) {
        this.bankMerchantId = bankMerchantId;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public String getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(String basicFare) {
        this.basicFare = basicFare;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getBookingTransactionID() {
        return bookingTransactionID;
    }

    public void setBookingTransactionID(String bookingTransactionID) {
        this.bookingTransactionID = bookingTransactionID;
    }

    public String getBridgeFee() {
        return bridgeFee;
    }

    public void setBridgeFee(String bridgeFee) {
        this.bridgeFee = bridgeFee;
    }

    public String getChildFare() {
        return childFare;
    }

    public void setChildFare(String childFare) {
        this.childFare = childFare;
    }

    public String getChildFemale() {
        return childFemale;
    }

    public void setChildFemale(String childFemale) {
        this.childFemale = childFemale;
    }

    public String getChildMale() {
        return childMale;
    }

    public void setChildMale(String childMale) {
        this.childMale = childMale;
    }

    public String getChildMales() {
        return childMales;
    }

    public void setChildMales(String childMales) {
        this.childMales = childMales;
    }

    public String getChildTicketFare() {
        return childTicketFare;
    }

    public void setChildTicketFare(String childTicketFare) {
        this.childTicketFare = childTicketFare;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getConcession() {
        return concession;
    }

    public void setConcession(String concession) {
        this.concession = concession;
    }

    public String getConcessionID() {
        return concessionID;
    }

    public void setConcessionID(String concessionID) {
        this.concessionID = concessionID;
    }

    public String getConcessionPercentage() {
        return concessionPercentage;
    }

    public void setConcessionPercentage(String concessionPercentage) {
        this.concessionPercentage = concessionPercentage;
    }

    public String getConcessionType() {
        return concessionType;
    }

    public void setConcessionType(String concessionType) {
        this.concessionType = concessionType;
    }

    public String getConcessionTypeId() {
        return concessionTypeId;
    }

    public void setConcessionTypeId(String concessionTypeId) {
        this.concessionTypeId = concessionTypeId;
    }

    public String getConductorMobileNo() {
        return conductorMobileNo;
    }

    public void setConductorMobileNo(String conductorMobileNo) {
        this.conductorMobileNo = conductorMobileNo;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getFareWithOutbasic() {
        return fareWithOutbasic;
    }

    public void setFareWithOutbasic(String fareWithOutbasic) {
        this.fareWithOutbasic = fareWithOutbasic;
    }

    public String getFromPlaceName() {
        return fromPlaceName;
    }

    public void setFromPlaceName(String fromPlaceName) {
        this.fromPlaceName = fromPlaceName;
    }

    public String getGatewayAmount() {
        return gatewayAmount;
    }

    public void setGatewayAmount(String gatewayAmount) {
        this.gatewayAmount = gatewayAmount;
    }

    public String getGatewayLookupID() {
        return gatewayLookupID;
    }

    public void setGatewayLookupID(String gatewayLookupID) {
        this.gatewayLookupID = gatewayLookupID;
    }

    public String getGatewayMoreInfo() {
        return gatewayMoreInfo;
    }

    public void setGatewayMoreInfo(String gatewayMoreInfo) {
        this.gatewayMoreInfo = gatewayMoreInfo;
    }

    public String getGatewayRemarks() {
        return gatewayRemarks;
    }

    public void setGatewayRemarks(String gatewayRemarks) {
        this.gatewayRemarks = gatewayRemarks;
    }

    public String getGatewayStatus() {
        return gatewayStatus;
    }

    public void setGatewayStatus(String gatewayStatus) {
        this.gatewayStatus = gatewayStatus;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getJourneyFromTime() {
        return journeyFromTime;
    }

    public void setJourneyFromTime(String journeyFromTime) {
        this.journeyFromTime = journeyFromTime;
    }

    public String getJourneyHours() {
        return journeyHours;
    }

    public void setJourneyHours(String journeyHours) {
        this.journeyHours = journeyHours;
    }

    public String getJourneyToTime() {
        return journeyToTime;
    }

    public void setJourneyToTime(String journeyToTime) {
        this.journeyToTime = journeyToTime;
    }

    public String getKsrtcRefNo() {
        return ksrtcRefNo;
    }

    public void setKsrtcRefNo(String ksrtcRefNo) {
        this.ksrtcRefNo = ksrtcRefNo;
    }

    public String getLastBusStation() {
        return lastBusStation;
    }

    public void setLastBusStation(String lastBusStation) {
        this.lastBusStation = lastBusStation;
    }

    public String getLastBusStationTime() {
        return lastBusStationTime;
    }

    public void setLastBusStationTime(String lastBusStationTime) {
        this.lastBusStationTime = lastBusStationTime;
    }

    public String getMaxValidResvDate() {
        return maxValidResvDate;
    }

    public void setMaxValidResvDate(String maxValidResvDate) {
        this.maxValidResvDate = maxValidResvDate;
    }

    public String getMessageFromCorp() {
        return messageFromCorp;
    }

    public void setMessageFromCorp(String messageFromCorp) {
        this.messageFromCorp = messageFromCorp;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getNoOfAdults() {
        return noOfAdults;
    }

    public void setNoOfAdults(String noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public String getNoOfChilds() {
        return noOfChilds;
    }

    public void setNoOfChilds(String noOfChilds) {
        this.noOfChilds = noOfChilds;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(String noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getOnewayOrReturnTrip() {
        return onewayOrReturnTrip;
    }

    public void setOnewayOrReturnTrip(String onewayOrReturnTrip) {
        this.onewayOrReturnTrip = onewayOrReturnTrip;
    }

    public String getOnlineDifferenceFare() {
        return onlineDifferenceFare;
    }

    public void setOnlineDifferenceFare(String onlineDifferenceFare) {
        this.onlineDifferenceFare = onlineDifferenceFare;
    }

    public String getOnlineDiscountedFare() {
        return onlineDiscountedFare;
    }

    public void setOnlineDiscountedFare(String onlineDiscountedFare) {
        this.onlineDiscountedFare = onlineDiscountedFare;
    }

    public String getOnlinePaymentDetailsID() {
        return onlinePaymentDetailsID;
    }

    public void setOnlinePaymentDetailsID(String onlinePaymentDetailsID) {
        this.onlinePaymentDetailsID = onlinePaymentDetailsID;
    }

    public String getOnlinePaymentStatus() {
        return onlinePaymentStatus;
    }

    public void setOnlinePaymentStatus(String onlinePaymentStatus) {
        this.onlinePaymentStatus = onlinePaymentStatus;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOtherLevies() {
        return otherLevies;
    }

    public void setOtherLevies(String otherLevies) {
        this.otherLevies = otherLevies;
    }

    public String getPassengerDestPlace() {
        return passengerDestPlace;
    }

    public void setPassengerDestPlace(String passengerDestPlace) {
        this.passengerDestPlace = passengerDestPlace;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(String passengerNames) {
        this.passengerNames = passengerNames;
    }

    public String getPassengerStartPlace() {
        return passengerStartPlace;
    }

    public void setPassengerStartPlace(String passengerStartPlace) {
        this.passengerStartPlace = passengerStartPlace;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPickServiceId() {
        return pickServiceId;
    }

    public void setPickServiceId(String pickServiceId) {
        this.pickServiceId = pickServiceId;
    }

    public String getPickupPointList() {
        return pickupPointList;
    }

    public void setPickupPointList(String pickupPointList) {
        this.pickupPointList = pickupPointList;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getPickupPointTime() {
        return pickupPointTime;
    }

    public void setPickupPointTime(String pickupPointTime) {
        this.pickupPointTime = pickupPointTime;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber;
    }

    public String getPnrMasterID() {
        return pnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        this.pnrMasterID = pnrMasterID;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(String reservationFee) {
        this.reservationFee = reservationFee;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnDepartureTime() {
        return returnDepartureTime;
    }

    public void setReturnDepartureTime(String returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }

    public String getReturnEndPlaceID() {
        return returnEndPlaceID;
    }

    public void setReturnEndPlaceID(String returnEndPlaceID) {
        this.returnEndPlaceID = returnEndPlaceID;
    }

    public String getReturnPassengerDestPlace() {
        return returnPassengerDestPlace;
    }

    public void setReturnPassengerDestPlace(String returnPassengerDestPlace) {
        this.returnPassengerDestPlace = returnPassengerDestPlace;
    }

    public String getReturnPassengerStartPlace() {
        return returnPassengerStartPlace;
    }

    public void setReturnPassengerStartPlace(String returnPassengerStartPlace) {
        this.returnPassengerStartPlace = returnPassengerStartPlace;
    }

    public String getReturnPlatformNumber() {
        return returnPlatformNumber;
    }

    public void setReturnPlatformNumber(String returnPlatformNumber) {
        this.returnPlatformNumber = returnPlatformNumber;
    }

    public String getReturnServiceClass() {
        return returnServiceClass;
    }

    public void setReturnServiceClass(String returnServiceClass) {
        this.returnServiceClass = returnServiceClass;
    }

    public String getReturnServiceID() {
        return returnServiceID;
    }

    public void setReturnServiceID(String returnServiceID) {
        this.returnServiceID = returnServiceID;
    }

    public String getReturnStartPlaceID() {
        return returnStartPlaceID;
    }

    public void setReturnStartPlaceID(String returnStartPlaceID) {
        this.returnStartPlaceID = returnStartPlaceID;
    }

    public String getReturnTripCode() {
        return returnTripCode;
    }

    public void setReturnTripCode(String returnTripCode) {
        this.returnTripCode = returnTripCode;
    }

    public String getSeatIDs() {
        return seatIDs;
    }

    public void setSeatIDs(String seatIDs) {
        this.seatIDs = seatIDs;
    }

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(String seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceOrginTime() {
        return serviceOrginTime;
    }

    public void setServiceOrginTime(String serviceOrginTime) {
        this.serviceOrginTime = serviceOrginTime;
    }

    public String getServiceRouteId() {
        return serviceRouteId;
    }

    public void setServiceRouteId(String serviceRouteId) {
        this.serviceRouteId = serviceRouteId;
    }

    public String getServiceRouteName() {
        return serviceRouteName;
    }

    public void setServiceRouteName(String serviceRouteName) {
        this.serviceRouteName = serviceRouteName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getStartPlaceID() {
        return startPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        this.startPlaceID = startPlaceID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStopBookingTime() {
        return stopBookingTime;
    }

    public void setStopBookingTime(String stopBookingTime) {
        this.stopBookingTime = stopBookingTime;
    }

    public String getTempPNRId() {
        return tempPNRId;
    }

    public void setTempPNRId(String tempPNRId) {
        this.tempPNRId = tempPNRId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getToPlaceName() {
        return toPlaceName;
    }

    public void setToPlaceName(String toPlaceName) {
        this.toPlaceName = toPlaceName;
    }

    public String getTollFee() {
        return tollFee;
    }

    public void setTollFee(String tollFee) {
        this.tollFee = tollFee;
    }

    public String getTotFemales() {
        return totFemales;
    }

    public void setTotFemales(String totFemales) {
        this.totFemales = totFemales;
    }

    public String getTotMales() {
        return totMales;
    }

    public void setTotMales(String totMales) {
        this.totMales = totMales;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTotalFareWithLevies() {
        return totalFareWithLevies;
    }

    public void setTotalFareWithLevies(String totalFareWithLevies) {
        this.totalFareWithLevies = totalFareWithLevies;
    }

    public String getTotalNumberOfSeats() {
        return totalNumberOfSeats;
    }

    public void setTotalNumberOfSeats(String totalNumberOfSeats) {
        this.totalNumberOfSeats = totalNumberOfSeats;
    }

    public String getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(String totalfare) {
        this.totalfare = totalfare;
    }

    public String getTranDateTime() {
        return tranDateTime;
    }

    public void setTranDateTime(String tranDateTime) {
        this.tranDateTime = tranDateTime;
    }

    public String getTransRefNo() {
        return transRefNo;
    }

    public void setTransRefNo(String transRefNo) {
        this.transRefNo = transRefNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getUserFee() {
        return userFee;
    }

    public void setUserFee(String userFee) {
        this.userFee = userFee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getViaPlaces() {
        return viaPlaces;
    }

    public void setViaPlaces(String viaPlaces) {
        this.viaPlaces = viaPlaces;
    }

    public String getWSRefNo() {
        return WSRefNo;
    }

    public void setWSRefNo(String WSRefNo) {
        this.WSRefNo = WSRefNo;
    }

    public String getWeekdayConcession() {
        return weekdayConcession;
    }

    public void setWeekdayConcession(String weekdayConcession) {
        this.weekdayConcession = weekdayConcession;
    }

    public String getcPnrStatus() {
        return cPnrStatus;
    }

    public void setcPnrStatus(String cPnrStatus) {
        this.cPnrStatus = cPnrStatus;
    }



    @Override
    public String toString()
    {
        return "ClassPojo [ksrtcRefNo = "+ksrtcRefNo+", " +
                "pnrNumber = "+pnrNumber+", " +
                "totalFare = "+ksrtcRefNo+", " +
                "confirmStatus = "+confirmStatus+"]";
    }


}
