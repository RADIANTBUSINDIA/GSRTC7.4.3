package com.gsrtc.mobileweb.utils;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Ajith on 4/22/2017.
 */

public class ValidationUtil {

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPhone(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }

        String phone = target.toString().trim();


        if (!phone.matches("\\d+")) {
            return false;
        }


        if (!phone.matches("^[6-9].*")) {
            return false;
        }


        return Patterns.PHONE.matcher(phone).matches()
                && PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }


    public static boolean isValidNumber(String target){
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return target.matches("[0-9]+");
        }
    }
}
