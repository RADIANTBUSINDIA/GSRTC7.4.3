package com.gsrtc.mobileweb.models;

public class DivyangDetails {

    private String status;
    private String divName;
    private String address1;
    private String divId;
    private String gender;
    private String dob;
    private String place;
    private String type;
    private String percentage;
    private String expired;
    private String mobile;
    private String count;

    // Constructor
    public DivyangDetails(String status, String divName, String address1, String divId, String gender,
                           String dob, String place, String type, String percentage, String expired,
                           String mobile, String count) {
        this.status = status;
        this.divName = divName;
        this.address1 = address1;
        this.divId = divId;
        this.gender = gender;
        this.dob = dob;
        this.place = place;
        this.type = type;
        this.percentage = percentage;
        this.expired = expired;
        this.mobile = mobile;
        this.count = count;
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDivName() { return divName; }
    public void setDivName(String divName) { this.divName = divName; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getDivId() { return divId; }
    public void setDivId(String divId) { this.divId = divId; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPercentage() { return percentage; }
    public void setPercentage(String percentage) { this.percentage = percentage; }

    public String getExpired() { return expired; }
    public void setExpired(String expired) { this.expired = expired; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getCount() { return count; }
    public void setCount(String count) { this.count = count; }
}
