package com.gsrtc.mobileweb.models.trackmybus;

/**
 * Created by Ramu on 12/29/2017.
 */


import java.io.Serializable;

public class Trackmybus implements Serializable {
    public TrackmybusHistorySearchParam trackmybusHistorySearchParam;
    public GetPnrDetailsResponse[] getPnrDetailsResponse;
    public GetPickupPointDetailsByPnrNoResponse[] getPickupPointDetailsByPnrNoResponses;
    public GetNearby_location_list[] getNearby_location_list;

}