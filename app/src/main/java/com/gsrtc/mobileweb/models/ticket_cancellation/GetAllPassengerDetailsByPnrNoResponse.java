package com.gsrtc.mobileweb.models.ticket_cancellation;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Swastik on 5/8/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllPassengerDetailsByPnrNoResponse implements Serializable {
    private String seatID;

    private String seatNo;

    private String age;

    private String franchiseeUser;

    private String passengerName;

    private String gender;

    private String adultOrChild;

    public String getSeatID ()
    {
        return seatID;
    }

    public void setSeatID (String seatID)
    {
        this.seatID = seatID;
    }

    public String getSeatNo ()
    {
        return seatNo;
    }

    public void setSeatNo (String seatNo)
    {
        this.seatNo = seatNo;
    }

    public String getAge ()
    {
        return age;
    }

    public void setAge (String age)
    {
        this.age = age;
    }

    public String getFranchiseeUser ()
    {
        return franchiseeUser;
    }

    public void setFranchiseeUser (String franchiseeUser)
    {
        this.franchiseeUser = franchiseeUser;
    }

    public String getPassengerName ()
    {
        return passengerName;
    }

    public void setPassengerName (String passengerName)
    {
        this.passengerName = passengerName;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getAdultOrChild ()
    {
        return adultOrChild;
    }

    public void setAdultOrChild (String adultOrChild)
    {
        this.adultOrChild = adultOrChild;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [seatID = "+seatID+", seatNo = "+seatNo+", age = "+age+", franchiseeUser = "+franchiseeUser+", passengerName = "+passengerName+", gender = "+gender+", adultOrChild = "+adultOrChild+"]";
    }
}
