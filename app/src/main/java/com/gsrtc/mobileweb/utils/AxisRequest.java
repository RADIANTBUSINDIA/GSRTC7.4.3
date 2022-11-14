package com.gsrtc.mobileweb.utils;

//import android.util.Base64;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AxisRequest {

    //STAGING ENVIRONMENT
//	String URL = "https://uat-geniusepay.in/VAS/DCC/doEnc.action";
//	static String SECURE_SECRET = "2D18DEBE8C5C4401C4E9DE0D71B39AFB"; //Add secure secret here
//	String encKey=  "B1066F3138834F41AFCAEB254DD600D7"; //Add encryption key here

    String URL = "https://geniusepay.in/VAS/DCC/doEnc.action";
    static String SECURE_SECRET = "9198952C3E15418DDF07D5A97CFB000F"; //2D18DEBE8C5C4401C4E9DE0D71B39AFB
    String encKey = "3a5c1c4e81d7eb133a5c1c4e81d7eb13";

    static String hashKeys = new String();
    static String hashValues = new String();

    public static String hashAllFields(Map fields) {
        //System.out.println("Requsted fields:"+fields);
        hashKeys = "";
        hashValues = "";
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        // create a buffer for the SHA-256 input and add the secure secret first
        StringBuffer buf = new StringBuffer();
        buf.append(SECURE_SECRET);
        // iterate through the list and add the remaining field values
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            hashKeys += fieldName + ", ";
            if ((fieldValue != null) && (fieldValue.length() > 0)) {

                buf.append(fieldValue);

            }
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(buf.toString().getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            //System.out.println("input data="+buf);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    } // end hashAllFields()

    public static String encrypt(String Data, String keySet) throws Exception {
        byte[] keyByte = keySet.getBytes();
        Key key = generateKey(keyByte);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key); //2
        byte[] encVal = c.doFinal(Data.getBytes()); //1
        //String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        //return encryptedValue;

        byte[] encryptedByteValue = new Base64().encode(encVal); //3
        String encryptedValue = new String(encryptedByteValue); //4
        return encryptedValue;
    }

    private static Key generateKey(byte[] keyByte) throws Exception {
        Key key = new SecretKeySpec(keyByte, "AES");
        return key;
    }

}
