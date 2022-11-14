package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Ajith on 11/8/2019.
 */

public class CalenderList implements Serializable {
    private String clnmonth;
    private String clndate;
    private String clnday;

    public String getClnmonth() {
        return clnmonth;
    }

    public void setClnmonth(String clnmonth) {
        this.clnmonth = clnmonth;
    }

    public String getClndate() {
        return clndate;
    }

    public void setClndate(String clndate) {
        this.clndate = clndate;
    }

    public String getClnday() {
        return clnday;
    }

    public void setClnday(String clnday) {
        this.clnday = clnday;
    }


}


