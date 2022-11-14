package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

/**
 * Created by Swastik on 4/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusService implements Serializable {
    private String adultFare;
    private String advanceOrCurrentBooking;
    private String arrivalDate;
    private String arrivalTime;
    private String childFare;
    private String classID;
    private String corpCode;
    private String departureTime;
    private String destination;
    private String distance;
    private String endPlaceID;
    private String journeyDate;
    private String journeyHours;
    private String noOfSeats;
    private String origin;
    private String routeNo;
    private String serviceClass;
    private String serviceID;
    private String startPlaceID;
    private String startPoint;
    private String stopBookingTime;
    private String tripCode;
    private String viaPlaces;
    private String textlanguage;

    private String className;
    private String depTime;
    private String depotName;
    private String fromPickupPoint;
    private String fromPlaceName;
    private String journeyHrs;
    private String routeName;
    private String toPickupPoint;
    private String toPlaceName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getFromPickupPoint() {
        return fromPickupPoint;
    }

    public void setFromPickupPoint(String fromPickupPoint) {
        this.fromPickupPoint = fromPickupPoint;
    }

    public String getFromPlaceName() {
        return fromPlaceName;
    }

    public void setFromPlaceName(String fromPlaceName) {
        this.fromPlaceName = fromPlaceName;
    }

    public String getJourneyHrs() {
        return journeyHrs;
    }

    public void setJourneyHrs(String journeyHrs) {
        this.journeyHrs = journeyHrs;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getToPickupPoint() {
        return toPickupPoint;
    }

    public void setToPickupPoint(String toPickupPoint) {
        this.toPickupPoint = toPickupPoint;
    }

    public String getToPlaceName() {
        return toPlaceName;
    }

    public void setToPlaceName(String toPlaceName) {
        this.toPlaceName = toPlaceName;
    }


    public String getAdultFare() {
        return adultFare;
    }

    public void setAdultFare(String adultFare) {
        this.adultFare = adultFare;
    }

    public String getAdvanceOrCurrentBooking() {
        return advanceOrCurrentBooking;
    }

    public void setAdvanceOrCurrentBooking(String advanceOrCurrentBooking) {
        this.advanceOrCurrentBooking = advanceOrCurrentBooking;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getChildFare() {
        return childFare;
    }

    public void setChildFare(String childFare) {
        this.childFare = childFare;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEndPlaceID() {
        return endPlaceID;
    }

    public void setEndPlaceID(String endPlaceID) {
        this.endPlaceID = endPlaceID;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getJourneyHours() {
        return journeyHours;
    }

    public void setJourneyHours(String journeyHours) {
        this.journeyHours = journeyHours;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(String noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
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

    public String getStopBookingTime() {
        return stopBookingTime;
    }

    public void setStopBookingTime(String stopBookingTime) {
        this.stopBookingTime = stopBookingTime;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getViaPlaces() {
        return viaPlaces;
    }

    public void setViaPlaces(String viaPlaces) {
        this.viaPlaces = viaPlaces;
    }

    public void setTextlanguage(String textlanguage) {
        this.textlanguage = textlanguage;
    }

    public String getTextlanguage() {
        return textlanguage;
    }

    public static BusService fromSoapObject(SoapObject soapObject){
        BusService service = new BusService();
        service.setAdultFare(soapObject.getProperty("adultFare").toString());
        service.setAdvanceOrCurrentBooking(soapObject.getProperty("advanceOrCurrentBooking").toString());
        service.setArrivalDate(soapObject.getProperty("arrivalDate").toString());
        service.setArrivalTime(soapObject.getProperty("arrivalTime").toString());
        service.setChildFare(soapObject.getProperty("childFare").toString());
        service.setClassID(soapObject.getProperty("classID").toString());
        service.setCorpCode(soapObject.getProperty("corpCode").toString());
        service.setDepartureTime(soapObject.getProperty("departureTime").toString());
        service.setDestination(soapObject.getProperty("destination").toString());
        service.setDistance(soapObject.getProperty("distance").toString());
        service.setEndPlaceID(soapObject.getProperty("endPlaceID").toString());
        service.setJourneyDate(soapObject.getProperty("journeyDate").toString());
        service.setJourneyHours(soapObject.getProperty("journeyHours").toString());
        service.setNoOfSeats(soapObject.getProperty("noOfSeats").toString());
        service.setOrigin(soapObject.getProperty("origin").toString());
        service.setRouteNo(soapObject.getProperty("routeNo").toString());
        service.setServiceClass(soapObject.getProperty("serviceClass").toString());
        service.setServiceID(soapObject.getProperty("serviceID").toString());
        service.setStartPlaceID(soapObject.getProperty("startPlaceID").toString());
        service.setStartPoint(soapObject.getProperty("startPoint").toString());
        service.setStopBookingTime(soapObject.getProperty("stopBookingTime").toString());
        service.setTripCode(soapObject.getProperty("tripCode").toString());
        service.setViaPlaces(soapObject.getProperty("viaPlaces").toString());
        service.setTextlanguage(soapObject.getProperty("textlanguage").toString());
        return service;
    }
}
