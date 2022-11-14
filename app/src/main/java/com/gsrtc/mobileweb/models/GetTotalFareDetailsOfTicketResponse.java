package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Swastik on 4/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTotalFareDetailsOfTicketResponse implements Serializable {
    private String counterCode;

    private String adultFemale;

    private String messageFromCorp;

    private String classID;

    private String reservationFee;

    private String startPlaceID;

    private String childMale;

    private String startPlaceCode;

    private String totalNumberOfAdults;

    private String otherLevies;

    private String adultMale;

    private String platformNumber;

    private String serviceID;

    private String discounts;

    private String userName;

    private String returnServiceID;

    private String concessionType;

    private String serviceFee;

    private String diffbasicFare;

    private String pickupPointPlaceId;

    private String status;

    private String journeyDate;

    private String childFemale;

    private String bridgeFee;

    private String childFare;

    private String pickupPointDropOffId;

    private String[] seatBlockIds;

    private String entryFee;

    private String WSRefNo;

    private String adultFare;

    private String userFee;

    private String weekdayConcession;

    private String totalFare;

    private String createdBy;

    private String tollFee;

    private String endPlaceCode;

    private String franchiseeUser;

    private String concessionTypeId;

    private String infraStructureFee;

    private String basicFare;

    private String endPlaceID;

    private String accidentReliefFund;

    private String totalNumberOfChild;

    public String getCounterCode ()
    {
        return counterCode;
    }

    public void setCounterCode (String counterCode)
    {
        this.counterCode = counterCode;
    }

    public String getAdultFemale ()
    {
        return adultFemale;
    }

    public void setAdultFemale (String adultFemale)
    {
        this.adultFemale = adultFemale;
    }

    public String getMessageFromCorp ()
    {
        return messageFromCorp;
    }

    public void setMessageFromCorp (String messageFromCorp)
    {
        this.messageFromCorp = messageFromCorp;
    }

    public String getClassID ()
    {
        return classID;
    }

    public void setClassID (String classID)
    {
        this.classID = classID;
    }

    public String getReservationFee ()
    {
        return reservationFee;
    }

    public void setReservationFee (String reservationFee)
    {
        this.reservationFee = reservationFee;
    }

    public String getStartPlaceID ()
    {
        return startPlaceID;
    }

    public void setStartPlaceID (String startPlaceID)
    {
        this.startPlaceID = startPlaceID;
    }

    public String getChildMale ()
    {
        return childMale;
    }

    public void setChildMale (String childMale)
    {
        this.childMale = childMale;
    }

    public String getStartPlaceCode ()
    {
        return startPlaceCode;
    }

    public void setStartPlaceCode (String startPlaceCode)
    {
        this.startPlaceCode = startPlaceCode;
    }

    public String getTotalNumberOfAdults ()
    {
        return totalNumberOfAdults;
    }

    public void setTotalNumberOfAdults (String totalNumberOfAdults)
    {
        this.totalNumberOfAdults = totalNumberOfAdults;
    }

    public String getOtherLevies ()
    {
        return otherLevies;
    }

    public void setOtherLevies (String otherLevies)
    {
        this.otherLevies = otherLevies;
    }

    public String getAdultMale ()
    {
        return adultMale;
    }

    public void setAdultMale (String adultMale)
    {
        this.adultMale = adultMale;
    }

    public String getPlatformNumber ()
    {
        return platformNumber;
    }

    public void setPlatformNumber (String platformNumber)
    {
        this.platformNumber = platformNumber;
    }

    public String getServiceID ()
    {
        return serviceID;
    }

    public void setServiceID (String serviceID)
    {
        this.serviceID = serviceID;
    }

    public String getDiscounts ()
    {
        return discounts;
    }

    public void setDiscounts (String discounts)
    {
        this.discounts = discounts;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getReturnServiceID ()
    {
        return returnServiceID;
    }

    public void setReturnServiceID (String returnServiceID)
    {
        this.returnServiceID = returnServiceID;
    }

    public String getConcessionType ()
    {
        return concessionType;
    }

    public void setConcessionType (String concessionType)
    {
        this.concessionType = concessionType;
    }

    public String getServiceFee ()
    {
        return serviceFee;
    }

    public void setServiceFee (String serviceFee)
    {
        this.serviceFee = serviceFee;
    }

    public String getDiffbasicFare ()
    {
        return diffbasicFare;
    }

    public void setDiffbasicFare (String diffbasicFare)
    {
        this.diffbasicFare = diffbasicFare;
    }

    public String getPickupPointPlaceId ()
    {
        return pickupPointPlaceId;
    }

    public void setPickupPointPlaceId (String pickupPointPlaceId)
    {
        this.pickupPointPlaceId = pickupPointPlaceId;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getJourneyDate ()
    {
        return journeyDate;
    }

    public void setJourneyDate (String journeyDate)
    {
        this.journeyDate = journeyDate;
    }

    public String getChildFemale ()
    {
        return childFemale;
    }

    public void setChildFemale (String childFemale)
    {
        this.childFemale = childFemale;
    }

    public String getBridgeFee ()
    {
        return bridgeFee;
    }

    public void setBridgeFee (String bridgeFee)
    {
        this.bridgeFee = bridgeFee;
    }

    public String getChildFare ()
    {
        return childFare;
    }

    public void setChildFare (String childFare)
    {
        this.childFare = childFare;
    }

    public String getPickupPointDropOffId ()
    {
        return pickupPointDropOffId;
    }

    public void setPickupPointDropOffId (String pickupPointDropOffId)
    {
        this.pickupPointDropOffId = pickupPointDropOffId;
    }

    public String[] getSeatBlockIds ()
    {
        return seatBlockIds;
    }

    public void setSeatBlockIds (String[] seatBlockIds)
    {
        this.seatBlockIds = seatBlockIds;
    }

    public String getEntryFee ()
    {
        return entryFee;
    }

    public void setEntryFee (String entryFee)
    {
        this.entryFee = entryFee;
    }

    @JsonProperty("WSRefNo")
    public String getWSRefNo ()
    {
        return WSRefNo;
    }

    public void setWSRefNo (String WSRefNo)
    {
        this.WSRefNo = WSRefNo;
    }

    public String getAdultFare ()
    {
        return adultFare;
    }

    public void setAdultFare (String adultFare)
    {
        this.adultFare = adultFare;
    }

    public String getUserFee ()
    {
        return userFee;
    }

    public void setUserFee (String userFee)
    {
        this.userFee = userFee;
    }

    public String getWeekdayConcession ()
    {
        return weekdayConcession;
    }

    public void setWeekdayConcession (String weekdayConcession)
    {
        this.weekdayConcession = weekdayConcession;
    }

    public String getTotalFare ()
    {
        return totalFare;
    }

    public void setTotalFare (String totalFare)
    {
        this.totalFare = totalFare;
    }

    public String getCreatedBy ()
    {
        return createdBy;
    }

    public void setCreatedBy (String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getTollFee ()
    {
        return tollFee;
    }

    public void setTollFee (String tollFee)
    {
        this.tollFee = tollFee;
    }

    public String getEndPlaceCode ()
    {
        return endPlaceCode;
    }

    public void setEndPlaceCode (String endPlaceCode)
    {
        this.endPlaceCode = endPlaceCode;
    }

    public String getFranchiseeUser ()
    {
        return franchiseeUser;
    }

    public void setFranchiseeUser (String franchiseeUser)
    {
        this.franchiseeUser = franchiseeUser;
    }

    public String getConcessionTypeId ()
    {
        return concessionTypeId;
    }

    public void setConcessionTypeId (String concessionTypeId)
    {
        this.concessionTypeId = concessionTypeId;
    }

    public String getInfraStructureFee ()
    {
        return infraStructureFee;
    }

    public void setInfraStructureFee (String infraStructureFee)
    {
        this.infraStructureFee = infraStructureFee;
    }

    public String getBasicFare ()
    {
        return basicFare;
    }

    public void setBasicFare (String basicFare)
    {
        this.basicFare = basicFare;
    }

    public String getEndPlaceID ()
    {
        return endPlaceID;
    }

    public void setEndPlaceID (String endPlaceID)
    {
        this.endPlaceID = endPlaceID;
    }

    public String getAccidentReliefFund ()
    {
        return accidentReliefFund;
    }

    public void setAccidentReliefFund (String accidentReliefFund)
    {
        this.accidentReliefFund = accidentReliefFund;
    }

    public String getTotalNumberOfChild ()
    {
        return totalNumberOfChild;
    }

    public void setTotalNumberOfChild (String totalNumberOfChild)
    {
        this.totalNumberOfChild = totalNumberOfChild;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [counterCode = "+counterCode+", adultFemale = "+adultFemale+", messageFromCorp = "+messageFromCorp+", classID = "+classID+", reservationFee = "+reservationFee+", startPlaceID = "+startPlaceID+", childMale = "+childMale+", startPlaceCode = "+startPlaceCode+", totalNumberOfAdults = "+totalNumberOfAdults+", otherLevies = "+otherLevies+", adultMale = "+adultMale+", platformNumber = "+platformNumber+", serviceID = "+serviceID+", discounts = "+discounts+", userName = "+userName+", returnServiceID = "+returnServiceID+", concessionType = "+concessionType+", serviceFee = "+serviceFee+", diffbasicFare = "+diffbasicFare+", pickupPointPlaceId = "+pickupPointPlaceId+", status = "+status+", journeyDate = "+journeyDate+", childFemale = "+childFemale+", bridgeFee = "+bridgeFee+", childFare = "+childFare+", pickupPointDropOffId = "+pickupPointDropOffId+", seatBlockIds = "+seatBlockIds+", entryFee = "+entryFee+", WSRefNo = "+WSRefNo+", adultFare = "+adultFare+", userFee = "+userFee+", weekdayConcession = "+weekdayConcession+", totalFare = "+totalFare+", createdBy = "+createdBy+", tollFee = "+tollFee+", endPlaceCode = "+endPlaceCode+", franchiseeUser = "+franchiseeUser+", concessionTypeId = "+concessionTypeId+", infraStructureFee = "+infraStructureFee+", basicFare = "+basicFare+", endPlaceID = "+endPlaceID+", accidentReliefFund = "+accidentReliefFund+", totalNumberOfChild = "+totalNumberOfChild+"]";
    }
}
