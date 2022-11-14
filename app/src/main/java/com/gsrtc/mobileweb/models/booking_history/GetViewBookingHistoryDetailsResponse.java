package com.gsrtc.mobileweb.models.booking_history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Created by Ayush on 23-May-17.
 */

public class GetViewBookingHistoryDetailsResponse implements Serializable {

    private String ticketStatusDescription;

    private String endPlace;

    private String counterCode;

    private String stopBookingStatus;

    private String status;

    private String journeyDate;

    private String fare;

    private String toDate;

    private String pnrNo;

    private String bookedOn;

    private String emailID;

    private String fromDate;

    private String seatNo;

    private String serviceID;

    private String pnrMasterID;

    private String startPlace;

    private String gsrtcOBRefNo;

    private String userName;

    private String transactionStatus;

    private String mobileNo;

    private String serviceCancelStatus;


        public String getTicketStatusDescription ()
        {
            return ticketStatusDescription;
        }

        public void setTicketStatusDescription (String ticketStatusDescription)
        {
            this.ticketStatusDescription = ticketStatusDescription;
        }

        public String getEndPlace ()
        {
            return endPlace;
        }

        public void setEndPlace (String endPlace)
        {
            this.endPlace = endPlace;
        }

        public String getCounterCode ()
        {
            return counterCode;
        }

        public void setCounterCode (String counterCode)
        {
            this.counterCode = counterCode;
        }

        public String getStopBookingStatus ()
        {
            return stopBookingStatus;
        }

        public void setStopBookingStatus (String stopBookingStatus)
        {
            this.stopBookingStatus = stopBookingStatus;
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

        public String getFare ()
        {
            return fare;
        }

        public void setFare (String fare)
        {
            this.fare = fare;
        }

        public String getToDate ()
        {
            return toDate;
        }

        public void setToDate (String toDate)
        {
            this.toDate = toDate;
        }

        public String getPnrNo ()
        {
            return pnrNo;
        }

        public void setPnrNo (String pnrNo)
        {
            this.pnrNo = pnrNo;
        }

        public String getBookedOn ()
        {
            return bookedOn;
        }

        public void setBookedOn (String bookedOn)
        {
            this.bookedOn = bookedOn;
        }

        public String getEmailID ()
        {
            return emailID;
        }

        public void setEmailID (String emailID)
        {
            this.emailID = emailID;
        }

        public String getFromDate ()
        {
            return fromDate;
        }

        public void setFromDate (String fromDate)
        {
            this.fromDate = fromDate;
        }

        public String getSeatNo ()
        {
            return seatNo;
        }

        public void setSeatNo (String seatNo)
        {
            this.seatNo = seatNo;
        }

        public String getServiceID ()
        {
            return serviceID;
        }

        public void setServiceID (String serviceID)
        {
            this.serviceID = serviceID;
        }

        public String getPnrMasterID ()
        {
            return pnrMasterID;
        }

        public void setPnrMasterID (String pnrMasterID)
        {
            this.pnrMasterID = pnrMasterID;
        }

        public String getStartPlace ()
        {
            return startPlace;
        }

        public void setStartPlace (String startPlace)
        {
            this.startPlace = startPlace;
        }

        public String getGsrtcOBRefNo ()
        {
            return gsrtcOBRefNo;
        }

        public void setGsrtcOBRefNo (String gsrtcOBRefNo)
        {
            this.gsrtcOBRefNo = gsrtcOBRefNo;
        }

        public String getUserName ()
        {
            return userName;
        }

        public void setUserName (String userName)
        {
            this.userName = userName;
        }

        public String getTransactionStatus ()
        {
            return transactionStatus;
        }

        public void setTransactionStatus (String transactionStatus)
        {
            this.transactionStatus = transactionStatus;
        }

        public String getMobileNo ()
        {
            return mobileNo;
        }

        public void setMobileNo (String mobileNo)
        {
            this.mobileNo = mobileNo;
        }

        public String getServiceCancelStatus ()
        {
            return serviceCancelStatus;
        }

        public void setServiceCancelStatus (String serviceCancelStatus)
        {
            this.serviceCancelStatus = serviceCancelStatus;
        }
    }

