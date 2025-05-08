package com.gsrtc.mobileweb.ui.activities.packageBooking.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class GetPackageTripsList implements Serializable {

    @JsonProperty("PACKAGE_ID")
    private int packageId;

    @JsonProperty("FROM_PLACEID")
    private int fromPlaceId;

    @JsonProperty("FROM_PLACE_CODE")
    private String fromPlaceCode;

    @JsonProperty("FROM_PLACE_NAME")
    private String fromPlaceName;

    @JsonProperty("PACKAGE_NAME")
    private String packageName;

    @JsonProperty("TO_PLACECODE")
    private String toPlaceCode;

    @JsonProperty("TO_PLACEID")
    private int toPlaceId;

    @JsonProperty("TO_PLACE_NAME")
    private String toPlaceName;

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getFromPlaceId() {
        return fromPlaceId;
    }

    public void setFromPlaceId(int fromPlaceId) {
        this.fromPlaceId = fromPlaceId;
    }

    public String getFromPlaceCode() {
        return fromPlaceCode;
    }

    public void setFromPlaceCode(String fromPlaceCode) {
        this.fromPlaceCode = fromPlaceCode;
    }

    public String getFromPlaceName() {
        return fromPlaceName;
    }

    public void setFromPlaceName(String fromPlaceName) {
        this.fromPlaceName = fromPlaceName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getToPlaceCode() {
        return toPlaceCode;
    }

    public void setToPlaceCode(String toPlaceCode) {
        this.toPlaceCode = toPlaceCode;
    }

    public int getToPlaceId() {
        return toPlaceId;
    }

    public void setToPlaceId(int toPlaceId) {
        this.toPlaceId = toPlaceId;
    }

    public String getToPlaceName() {
        return toPlaceName;
    }

    public void setToPlaceName(String toPlaceName) {
        this.toPlaceName = toPlaceName;
    }
}
