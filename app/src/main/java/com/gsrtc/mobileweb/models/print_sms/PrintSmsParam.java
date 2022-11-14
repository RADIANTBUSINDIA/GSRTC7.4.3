package com.gsrtc.mobileweb.models.print_sms;

import java.io.Serializable;

/**
 * Created by Ajith on 3/12/2019.
 */

public class PrintSmsParam implements Serializable {

    public String emailID;
    public String mobileNo;
    public String pnrNo;



    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }
}

