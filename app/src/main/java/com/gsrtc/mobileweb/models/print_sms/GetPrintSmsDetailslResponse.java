package com.gsrtc.mobileweb.models.print_sms;

import java.io.Serializable;

/**
 * Created by Ajith on 3/15/2019.
 */

public class GetPrintSmsDetailslResponse implements Serializable {

    private String endPlaceName;
    private String franchiseeUser;
    private String gatewayAmount;
    private String journeyDate;
    private String phoneNumber;
    private String pnrNumber;
    private String startPlaceName;
    private String tripCode;

    public String getEndPlaceName ()
    {
        return endPlaceName;
    }

    public void setEndPlaceName (String endPlaceName)
    {
        this.endPlaceName = endPlaceName;
    }
    public String getFranchiseeUser ()
    {
        return franchiseeUser;
    }

    public void setFranchiseeUser (String franchiseeUser)
    {
        this.franchiseeUser = franchiseeUser;
    }

    public String getJourneyDate ()
    {
        return journeyDate;
    }

    public void setJourneyDate (String journeyDate)
    {
        this.journeyDate = journeyDate;
    }

    public String getGatewayAmount ()
    {
        return gatewayAmount;
    }

    public void setGatewayAmount (String gatewayAmount)
    {
        this.gatewayAmount = gatewayAmount;
    }

    public String getPnrNumber ()
    {
        return pnrNumber;
    }

    public void setPnrNumber (String pnrNumber)
    {
        this.pnrNumber = pnrNumber;
    }

    public String getTripCode ()
    {
        return tripCode;
    }

    public void setTripCode (String tripCode)
    {
        this.tripCode = tripCode;
    }


    public String getStartPlaceName ()
    {
        return startPlaceName;
    }

    public void setStartPlaceName (String startPlaceName)
    {
        this.startPlaceName = startPlaceName;
    }


    public String getPhoneNumber ()
    {
        return phoneNumber;
    }

    public void setPhoneNumber (String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

}


