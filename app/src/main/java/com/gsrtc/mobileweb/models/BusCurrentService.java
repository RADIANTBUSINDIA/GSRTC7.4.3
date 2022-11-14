package com.gsrtc.mobileweb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

/**
 * Created by bala on 29-12-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusCurrentService implements Serializable{

    private String adultFare;
    private String adultTicketFare;
    private String childFare;
    private String childTicketFare;
    private String classID;
    private String conductorMobileNo;
    private String departureTime;
    private String destination;
    private String journeyDate;
    private String journeyHours;
    private String lastBusStation;
    private String lastBusStationTime;
    private String origin;
    private String pickServiceId;
    private String serviceClass;
    private String serviceID;
    private String serviceOrginTime;
    private String serviceRouteId;
    private String serviceRouteName;
    private String tripCode;
    private String vehicleId;
    private String vehicleNo;
    private String vehicleStatus;
    private String seatavailble;
    private String kms;

    public String getSeatavailble() {
        return seatavailble;
    }

    public void setSeatavailble(String seatavailble) {
        this.seatavailble = seatavailble;
    }


    public String getAdultFare() {
        return adultFare;
    }

    public void setAdultFare(String adultFare) {
        this.adultFare = adultFare;
    }

    public String getAdultTicketFare() {
        return adultTicketFare;
    }

    public void setAdultTicketFare(String adultTicketFare) {
        this.adultTicketFare = adultTicketFare;
    }

    public String getChildFare() {
        return childFare;
    }

    public void setChildFare(String childFare) {
        this.childFare = childFare;
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

    public String getConductorMobileNo() {
        return conductorMobileNo;
    }

    public void setConductorMobileNo(String conductorMobileNo) {
        this.conductorMobileNo = conductorMobileNo;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPickServiceId() {
        return pickServiceId;
    }

    public void setPickServiceId(String pickServiceId) {
        this.pickServiceId = pickServiceId;
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

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
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


    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public static BusCurrentService fromSoapObject(SoapObject soapObject){
        BusCurrentService service = new BusCurrentService();
        service.setAdultFare(soapObject.getProperty("adultFare").toString());
        service.setAdultTicketFare(soapObject.getProperty("adultTicketFare").toString());
        service.setChildFare(soapObject.getProperty("childFare").toString());
        service.setChildTicketFare(soapObject.getProperty("childTicketFare").toString());
        service.setClassID(soapObject.getProperty("classID").toString());
        service.setConductorMobileNo(soapObject.getProperty("conductorMobileNo").toString());
        service.setDepartureTime(soapObject.getProperty("departureTime").toString());
        service.setDestination(soapObject.getProperty("destination").toString());
        service.setJourneyDate(soapObject.getProperty("journeyDate").toString());
        service.setJourneyHours(soapObject.getProperty("journeyHours").toString());
        service.setLastBusStation(soapObject.getProperty("lastBusStation").toString());
        service.setLastBusStationTime(soapObject.getProperty("lastBusStationTime").toString());
        service.setOrigin(soapObject.getProperty("origin").toString());
        service.setPickServiceId(soapObject.getProperty("pickServiceId").toString());
        service.setServiceClass(soapObject.getProperty("serviceClass").toString());
        service.setServiceID(soapObject.getProperty("serviceID").toString());
        service.setServiceOrginTime(soapObject.getProperty("serviceOrginTime").toString());
        service.setServiceRouteId(soapObject.getProperty("serviceRouteId").toString());
        service.setServiceRouteName(soapObject.getProperty("serviceRouteName").toString());
        service.setTripCode(soapObject.getProperty("tripCode").toString());
        service.setVehicleId(soapObject.getProperty("vehicleId").toString());
        service.setVehicleNo(soapObject.getProperty("vehicleNo").toString());
        service.setVehicleStatus(soapObject.getProperty("vehicleStatus").toString());
        service.setKms(soapObject.getProperty("kms").toString());

        return service;
    }


}
