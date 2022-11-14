package com.gsrtc.mobileweb.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Ajith on 3/25/2017.
 */

public class JacksonUtil {
    private static ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);;

    public static ObjectMapper getMapper(){
        return mapper;
    }

}
