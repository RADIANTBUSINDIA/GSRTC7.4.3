package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Swastik on 4/30/2017.
 */

public class PassengerInfo implements Serializable {
    String email;
    String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
