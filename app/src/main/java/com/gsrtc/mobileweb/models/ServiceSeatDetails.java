package com.gsrtc.mobileweb.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Swastik on 4/20/2017.
 */

public class ServiceSeatDetails implements Serializable {
    Set<Integer> availableSeats = new HashSet<>();
    Set<Integer> bookedSeats= new HashSet<>();
    Set<Integer> availableLadiesSeats= new HashSet<>();
    Set<Integer> conductorSeats= new HashSet<>();
    Set<Integer> bookedLadiesSeats= new HashSet<>();

    public Set<Integer> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Set<Integer> availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setAvailableSeats(String[] seats) {
        this.availableSeats = new HashSet<>();
        for(String seat:seats){
            this.availableSeats.add(Integer.parseInt(seat));
        }
    }

    public Set<Integer> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Set<Integer> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void setBookedSeats(String[] seats) {
        this.bookedSeats = new HashSet<>();
        for(String seat:seats){
            this.bookedSeats.add(Integer.parseInt(seat));
        }
    }

    public Set<Integer> getAvailableLadiesSeats() {
        return availableLadiesSeats;
    }

    public void setAvailableLadiesSeats(Set<Integer> availableLadiesSeats) {
        this.availableLadiesSeats = availableLadiesSeats;
    }

    public void setAvailableLadiesSeats(String[] seats) {
        this.availableLadiesSeats = new HashSet<>();
        for(String seat:seats){
            this.availableLadiesSeats.add(Integer.parseInt(seat));
        }
    }

    public Set<Integer> getBookedLadiesSeats() {
        return bookedLadiesSeats;
    }

    public void setBookedLadiesSeats(Set<Integer> bookedLadiesSeats) {
        this.bookedLadiesSeats = bookedLadiesSeats;
    }

    public void setBookedLadiesSeats(String[] seats) {
        this.bookedLadiesSeats = new HashSet<>();
        for(String seat:seats){
            this.bookedLadiesSeats.add(Integer.parseInt(seat));
        }
    }

    public Set<Integer> getConductorSeats() {
        return conductorSeats;
    }

    public void setConductorSeats(String[] seats) {
        this.conductorSeats = new HashSet<>();
        for(String seat:seats){
            this.conductorSeats.add(Integer.parseInt(seat));
        }
    }

    public void setConductorSeats(Set<Integer> conductorSeats) {
        this.conductorSeats = conductorSeats;
    }
}
