package com.gsrtc.mobileweb.models;

import java.io.Serializable;

/**
 * Created by Swastik on 4/18/2017.
 */

public class Seat implements Serializable {
    Integer seatNo;
    String description;

    public Integer getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return seatNo+"("+description+") ";
    }

    public static Seat getBlankSeat(){
        Seat seat = new Seat();
        seat.setSeatNo(0);
        return seat;
    }
}
