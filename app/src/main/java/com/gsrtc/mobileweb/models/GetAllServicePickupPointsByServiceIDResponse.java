package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Swastik on 4/29/2017.
 */

public class GetAllServicePickupPointsByServiceIDResponse implements Serializable {
    private String pickupPointID;

    private String platformNo;

    private String pickupPointName;

    private String pickupTime;

    public String getPickupPointID() {
        return pickupPointID;
    }

    public void setPickupPointID(String pickupPointID) {
        this.pickupPointID = pickupPointID;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }
}
