package com.gsrtc.mobileweb.models;

import android.util.Log;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import static com.gsrtc.mobileweb.utils.AppConstants.SEAT_ROW_COUNT;

/**
 * Created by Swastik on 4/18/2017.
 */

public class SeatRow {

    String layoutID;

    Integer rowNumber;
    String status;
    Seat[] seats = new Seat[SEAT_ROW_COUNT];


    public Seat[] getSeatss() {
        return seats;
    }

    public void setSeatss(Seat[] seats) {
        this.seats = seats;
    }

    public Seat getSeat(int position){
        if(position<1 || position > SEAT_ROW_COUNT){
            Log.v("Seat Out of Bound", "Seat position has to be between 1 and 6");
            return null;
        }
        return seats[position-1];
    }

    public int getSeatCount(){
        return seats.length;
    }



    public static SeatRow fromSoapObject(SoapObject soapObject){
        SeatRow row = new SeatRow();
        for(int i=0; i <SEAT_ROW_COUNT;i++){
            Seat seat = new Seat();
            seat.setSeatNo(Integer.parseInt(soapObject.getProperty("col"+(i+1)+"SeatNo").toString()));
            if(soapObject.getProperty("col"+(i+1)+"SeatDesc")!=null)
                seat.setDescription(soapObject.getProperty("col"+(i+1)+"SeatDesc").toString());
            row.seats[i] = seat;
        }
        row.layoutID = soapObject.getProperty("layoutID").toString();
        row.rowNumber = Integer.parseInt(soapObject.getProperty("rowNumber").toString());
        row.status =soapObject.getProperty("status").toString();
        return row;
    }

    public static SeatRow fromJsonObject(JSONObject jsonObject){
        SeatRow row = null;
        try {
            row = new SeatRow();
            for (int i = 0; i < SEAT_ROW_COUNT; i++) {
                Seat seat = new Seat();
                seat.setSeatNo(Integer.parseInt(jsonObject.getString("col" + (i + 1) + "SeatNo").toString()));
                if (jsonObject.has("col" + (i + 1) + "SeatDesc"))
                    seat.setDescription(jsonObject.getString("col" + (i + 1) + "SeatDesc").toString());
                row.seats[i] = seat;
            }
            row.layoutID = jsonObject.getString("layoutID").toString();
            row.rowNumber = Integer.parseInt(jsonObject.getString("rowNumber").toString());
            row.status = jsonObject.getString("status").toString();
            return row;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
