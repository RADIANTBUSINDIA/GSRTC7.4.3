package com.gsrtc.mobileweb.models.trackmybus;

public class IpDetails {

    private String mobileNo;
    private String ipAddress;
    private String emailId;
    private String pnrNo;
    private String obRefNo;

    public IpDetails() {
    }

    public IpDetails(String mobileNo, String ipAddress, String emailId, String pnrNo, String obRefNo) {
        this.mobileNo = mobileNo;
        this.ipAddress = ipAddress;
        this.emailId = emailId;
        this.pnrNo = pnrNo;
        this.obRefNo = obRefNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }

    public String getObRefNo() {
        return obRefNo;
    }

    public void setObRefNo(String obRefNo) {
        this.obRefNo = obRefNo;
    }
}
