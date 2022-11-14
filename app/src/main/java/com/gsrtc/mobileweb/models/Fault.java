package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by swastik on 15/4/17.
 */

public class Fault implements Serializable {

    String faultcode;
    String faultstring;
    String detail;

    public String getFaultcode() {
        return faultcode;
    }

    public void setFaultcode(String faultcode) {
        this.faultcode = faultcode;
    }

    public String getFaultstring() {
        return faultstring;
    }

    public void setFaultstring(String faultstring) {
        this.faultstring = faultstring;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
