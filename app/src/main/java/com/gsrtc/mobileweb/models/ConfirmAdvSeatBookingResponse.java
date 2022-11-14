package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Swastik on 4/29/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmAdvSeatBookingResponse implements Serializable {

    private String counterCode;

    private String idProofLookupId;

    private String classID;

    private String[] addnlPasngrName;

    private String bookingType;

    private String seriesNumber;

    private String ticketNumber;

    private String reservationFee;

    private String startPlaceCode;

    private String childMale;

    private String city;

    private String emailId;

    private String adultMale;

    private String phoneNumber;

    private String serviceID;

    private String age;

    private String pnrMasterID;

    private String creditCardNumber;

    private String gender;

    private String userName;

    private String advanceOrCurrentBooking;

    private String returnServiceID;

    private String pickupPointPlaceId;

    private String journeyDate;

    private String[] addnlAge;

    private String bridgeFee;

    private String userFee;

    private String[] addnlAdultOrChild;

    private String totalFare;

    private String tollFee;

    private String createdBy;

    private String endPlaceCode;

    private String onewayOrReturnTrip;

    private String passengerName;

    private String[] seatNumber;

    private String basicFare;

    private String infraStructureFee;

    private String[] addnlGender;

    private String pickupPointTime;

    private String adultFemale;

    private String modeOfPayment;

    private String idProofRefernce;

    private String startPlaceID;

    private String pickupPointDropOffTime;

    private String adultOrChild;

    private String otherLevies;

    private String discounts;

    private String pnrNumber;

    private String serviceFee;

    private String[] seatIDs;

    private String childFemale;

    private String[] seatBlockIds;

    private String pickupPointDropOffId;

    private String entryFee;

    private String WSRefNo;

    private String weekdayConcession;

    private String address;

    private String franchiseeUser;

    private String concessionTypeId;

    private String endPlaceID;

    private String accidentReliefFund;

    private String blockingKeyNo;

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getIdProofLookupId() {
        return idProofLookupId;
    }

    public void setIdProofLookupId(String idProofLookupId) {
        this.idProofLookupId = idProofLookupId;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String[] getAddnlPasngrName() {
        return addnlPasngrName;
    }

    public void setAddnlPasngrName(String[] addnlPasngrName) {
        this.addnlPasngrName = addnlPasngrName;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(String reservationFee) {
        this.reservationFee = reservationFee;
    }

    public String getStartPlaceCode() {
        return startPlaceCode;
    }

    public void setStartPlaceCode(String startPlaceCode) {
        this.startPlaceCode = startPlaceCode;
    }

    public String getChildMale() {
        return childMale;
    }

    public void setChildMale(String childMale) {
        this.childMale = childMale;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAdultMale() {
        return adultMale;
    }

    public void setAdultMale(String adultMale) {
        this.adultMale = adultMale;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPnrMasterID() {
        return pnrMasterID;
    }

    public void setPnrMasterID(String pnrMasterID) {
        this.pnrMasterID = pnrMasterID;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdvanceOrCurrentBooking() {
        return advanceOrCurrentBooking;
    }

    public void setAdvanceOrCurrentBooking(String advanceOrCurrentBooking) {
        this.advanceOrCurrentBooking = advanceOrCurrentBooking;
    }

    public String getReturnServiceID() {
        return returnServiceID;
    }

    public void setReturnServiceID(String returnServiceID) {
        this.returnServiceID = returnServiceID;
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

    public String[] getAddnlAge() {
        return addnlAge;
    }

    public void setAddnlAge(String[] addnlAge) {
        this.addnlAge = addnlAge;
    }

    public String getBridgeFee() {
        return bridgeFee;
    }

    public void setBridgeFee(String bridgeFee) {
        this.bridgeFee = bridgeFee;
    }

    public String getUserFee() {
        return userFee;
    }

    public void setUserFee(String userFee) {
        this.userFee = userFee;
    }

    public String[] getAddnlAdultOrChild() {
        return addnlAdultOrChild;
    }

    public void setAddnlAdultOrChild(String[] addnlAdultOrChild) {
        this.addnlAdultOrChild = addnlAdultOrChild;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTollFee() {
        return tollFee;
    }

    public void setTollFee(String tollFee) {
        this.tollFee = tollFee;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEndPlaceCode() {
        return endPlaceCode;
    }

    public void setEndPlaceCode(String endPlaceCode) {
        this.endPlaceCode = endPlaceCode;
    }

    public String getOnewayOrReturnTrip() {
        return onewayOrReturnTrip;
    }

    public void setOnewayOrReturnTrip(String onewayOrReturnTrip) {
        this.onewayOrReturnTrip = onewayOrReturnTrip;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    public String getInfraStructureFee() {
        return infraStructureFee;
    }

    public void setInfraStructureFee(String infraStructureFee) {
        this.infraStructureFee = infraStructureFee;
    }

    public String[] getAddnlGender() {
        return addnlGender;
    }

    public void setAddnlGender(String[] addnlGender) {
        this.addnlGender = addnlGender;
    }

    public String getPickupPointTime() {
        return pickupPointTime;
    }

    public void setPickupPointTime(String pickupPointTime) {
        this.pickupPointTime = pickupPointTime;
    }

    public String getAdultFemale() {
        return adultFemale;
    }

    public void setAdultFemale(String adultFemale) {
        this.adultFemale = adultFemale;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getIdProofRefernce() {
        return idProofRefernce;
    }

    public void setIdProofRefernce(String idProofRefernce) {
        this.idProofRefernce = idProofRefernce;
    }

    public String getStartPlaceID() {
        return startPlaceID;
    }

    public void setStartPlaceID(String startPlaceID) {
        this.startPlaceID = startPlaceID;
    }

    public String getPickupPointDropOffTime() {
        return pickupPointDropOffTime;
    }

    public void setPickupPointDropOffTime(String pickupPointDropOffTime) {
        this.pickupPointDropOffTime = pickupPointDropOffTime;
    }

    public String getAdultOrChild() {
        return adultOrChild;
    }

    public void setAdultOrChild(String adultOrChild) {
        this.adultOrChild = adultOrChild;
    }

    public String getOtherLevies() {
        return otherLevies;
    }

    public void setOtherLevies(String otherLevies) {
        this.otherLevies = otherLevies;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String[] getSeatIDs() {
        return seatIDs;
    }

    public void setSeatIDs(String[] seatIDs) {
        this.seatIDs = seatIDs;
    }

    public String getChildFemale() {
        return childFemale;
    }

    public void setChildFemale(String childFemale) {
        this.childFemale = childFemale;
    }

    public String[] getSeatBlockIds() {
        return seatBlockIds;
    }

    public void setSeatBlockIds(String[] seatBlockIds) {
        this.seatBlockIds = seatBlockIds;
    }

    public String getPickupPointDropOffId() {
        return pickupPointDropOffId;
    }

    public void setPickupPointDropOffId(String pickupPointDropOffId) {
        this.pickupPointDropOffId = pickupPointDropOffId;
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

    public String getWeekdayConcession() {
        return weekdayConcession;
    }

    public void setWeekdayConcession(String weekdayConcession) {
        this.weekdayConcession = weekdayConcession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBlockingKeyNo() {
        return blockingKeyNo;
    }

    public void setBlockingKeyNo(String blockingKeyNo) {
        this.blockingKeyNo = blockingKeyNo;
    }
}
