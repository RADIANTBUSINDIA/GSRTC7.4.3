package com.gsrtc.mobileweb.utils;

import java.util.UUID;

/**
 * Created by Ajith on 10/1/17.
 */

public class UUIDUtil {

    /**
     * Generates a unique ID
     * @return
     */
    public static String generateUniqueID(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
