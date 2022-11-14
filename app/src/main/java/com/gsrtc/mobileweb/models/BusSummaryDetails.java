package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;


/**
 * Created by bala on 29-12-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusSummaryDetails implements Serializable{

    private String accidentReliefFund;
    private String adultFare;
    private String adultFemale;
    private String adultMale;
    private String adultMales;
    private String adultTicketFare;
    private String basicFare;
    private String bridgeFee;
    private String childFare;
    private String childFemale;
    private String childMale;
    private String childMales;
    private String childTicketFare;
    private String classId;
    private String concessionID;
    private String concessionPercentage;
    private String concessionTypeId;
    private String counterCode;
    private String createdBy;
    private String discountPercentage;
    private String discounts;
    private String endPlaceID;
    private String entryFee;
    private String fareWithOutbasic;
    private String journeyDate;
    private String messageFromCorp;
    private String onewayOrReturnTrip;
    private String onlineDifferenceFare;
    private String otherLevies;
    private String pickServiceId;
    private String reservationFee;
    private String startPlaceID;
    private String status;
    private String tollFee;
    private String totalAmount;
    private String totalFare;
    private String totalNumberOfSeats;
    private String userFee;
    private String userName;
    private String weekdayConcession;

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

    public String getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(String basicFare) {
        this.basicFare = basicFare;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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

    public String getConcessionTypeId() {
        return concessionTypeId;
    }

    public void setConcessionTypeId(String concessionTypeId) {
        this.concessionTypeId = concessionTypeId;
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

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
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

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getMessageFromCorp() {
        return messageFromCorp;
    }

    public void setMessageFromCorp(String messageFromCorp) {
        this.messageFromCorp = messageFromCorp;
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

    public String getOtherLevies() {
        return otherLevies;
    }

    public void setOtherLevies(String otherLevies) {
        this.otherLevies = otherLevies;
    }

    public String getPickServiceId() {
        return pickServiceId;
    }

    public void setPickServiceId(String pickServiceId) {
        this.pickServiceId = pickServiceId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTollFee() {
        return tollFee;
    }

    public void setTollFee(String tollFee) {
        this.tollFee = tollFee;
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

    public String getTotalNumberOfSeats() {
        return totalNumberOfSeats;
    }

    public void setTotalNumberOfSeats(String totalNumberOfSeats) {
        this.totalNumberOfSeats = totalNumberOfSeats;
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

    public String getWeekdayConcession() {
        return weekdayConcession;
    }

    public void setWeekdayConcession(String weekdayConcession) {
        this.weekdayConcession = weekdayConcession;
    }





    @Override
    public String toString()
    {
        return "ClassPojo [" +
                "accidentReliefFund = "+accidentReliefFund+", " +
                "adultFare = "+adultFare+", " +
                "adultFemale = "+adultFemale+", " +
                "adultMale = "+adultMale+", " +
                "adultMales = "+adultMales+", " +
                "adultTicketFare = "+adultTicketFare+", " +
                "basicFare = "+basicFare+", " +
                "bridgeFee = "+bridgeFee+", " +
                "childFare = "+childFare+", " +
                "childFemale = "+childFemale+", " +
                "childMale = "+childMale+", " +
                "childMales = "+childMales+", " +
                "childTicketFare = "+childTicketFare+", " +
                "classId = "+classId+", " +
                "concessionID = "+concessionID+", " +
                "concessionPercentage = "+concessionPercentage+", " +
                "concessionTypeId = "+concessionTypeId+", " +
                "counterCode = "+counterCode+", " +
                "createdBy = "+createdBy+", " +
                "discountPercentage = "+discountPercentage+", " +
                "discounts = "+discounts+", " +
                "endPlaceID = "+endPlaceID+", " +
                "entryFee = "+entryFee+", " +
                "fareWithOutbasic = "+fareWithOutbasic+", " +
                "journeyDate = "+journeyDate+", " +
                "messageFromCorp = "+messageFromCorp+", " +
                "onewayOrReturnTrip = "+onewayOrReturnTrip+", " +
                "onlineDifferenceFare = "+onlineDifferenceFare+", " +
                "otherLevies = "+otherLevies+", " +
                "pickServiceId = "+pickServiceId+", " +
                "reservationFee = "+reservationFee+", " +
                "startPlaceID = "+startPlaceID+", " +
                "status = "+status+", " +
                "tollFee = "+tollFee+", " +
                "totalAmount = "+totalAmount+", " +
                "totalFare = "+totalFare+", " +
                "totalNumberOfSeats = "+totalNumberOfSeats+", " +
                "userFee = "+userFee+", " +
                "userName = "+userName+", " +
                "weekdayConcession = "+weekdayConcession+"]";
    }


    /*public static BusSummaryDetails fromSoapObject(SoapObject soapObject){
        BusSummaryDetails service = new BusSummaryDetails();

        service.setAccidentReliefFund(soapObject.getProperty("accidentReliefFund").toString());
        service.setAdultFemale(soapObject.getProperty("adultFemale").toString());
        service.setAdultMales(soapObject.getProperty("adultMales").toString());
        service.setAdultTicketFare(soapObject.getProperty("adultTicketFare").toString());
        service.setBasicFare(soapObject.getProperty("basicFare").toString());
        service.setBridgeFee(soapObject.getProperty("bridgeFee").toString());
        service.setChildFemale(soapObject.getProperty("childFemale").toString());
        service.setChildMales(soapObject.getProperty("childMales").toString());
        service.setChildTicketFare(soapObject.getProperty("childTicketFare").toString());
        service.setClassID(soapObject.getProperty("classID").toString());
        service.setConcessionID(soapObject.getProperty("concessionID").toString());
        service.setConcessionPercentage(soapObject.getProperty("concessionPercentage").toString());
        service.setCounterCode(soapObject.getProperty("counterCode").toString());
        service.setDiscountPercentage(soapObject.getProperty("discountPercentage").toString());
        service.setDiscounts(soapObject.getProperty("discounts").toString());
        service.setEndPlaceID(soapObject.getProperty("endPlaceID").toString());
        service.setEntryFee(soapObject.getProperty("entryFee").toString());
        service.setFareWithOutbasic(soapObject.getProperty("fareWithOutbasic").toString());
        service.setJourneyDate(soapObject.getProperty("journeyDate").toString());
        service.setJourneyFromTime(soapObject.getProperty("journeyFromTime").toString());
        service.setJourneyToTime(soapObject.getProperty("journeyToTime").toString());
        service.setMessageFromCorp(soapObject.getProperty("messageFromCorp").toString());
        service.setOnewayOrReturnTrip(soapObject.getProperty("onewayOrReturnTrip").toString());
        service.setOnlineDifferenceFare(soapObject.getProperty("onlineDifferenceFare").toString());
        service.setOtherLevies(soapObject.getProperty("otherLevies").toString());
        service.setPickServiceId(soapObject.getProperty("pickServiceId").toString());
        service.setReservationFee(soapObject.getProperty("reservationFee").toString());
        service.setStartPlaceID(soapObject.getProperty("startPlaceID").toString());
        service.setStatus(soapObject.getProperty("status").toString());
        service.setTollFee(soapObject.getProperty("tollFee").toString());
        service.setTotalAmount(soapObject.getProperty("totalAmount").toString());
        service.setTotalFare(soapObject.getProperty("totalFare").toString());
        service.setUserFee(soapObject.getProperty("userFee").toString());
        service.setUserName(soapObject.getProperty("userName").toString());
        service.setWeekdayConcession(soapObject.getProperty("weekdayConcession").toString());


        return service;
    }*/


}
