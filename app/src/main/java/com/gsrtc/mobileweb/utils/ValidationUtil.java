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
        } else {
            return Patterns.PHONE.matcher(target).matches() || PhoneNumberUtils.isGlobalPhoneNumber(target.toString());
        }
    }

    public static boolean isValidNumber(String target){
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return target.matches("[0-9]+");
        }
    }
}
