package com.gsrtc.mobileweb.dto;

import android.content.ContentResolver;

import com.gsrtc.mobileweb.GSRTC;
import com.gsrtc.mobileweb.models.BusService;

/**
 * Created by Swastik on 4/17/2017.
 */

public class BusServiceDTO {

    private static ContentResolver mResolver = GSRTC.getAppContext().getContentResolver();

    public static void add(BusService busService){

    }

    public static void addAll(BusService[] busServices){

    }

    public static void clearAll(){
      //  mResolver.delete()
    }



}
