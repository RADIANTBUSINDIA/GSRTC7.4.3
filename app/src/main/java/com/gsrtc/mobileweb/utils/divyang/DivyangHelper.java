package com.gsrtc.mobileweb.utils.divyang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioButton;

import com.gsrtc.mobileweb.models.Booking;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.utils.AppConstants;

/**
 * Manages Divyang accessibility mode preferences and state.
 * By default voice mode is disabled; it strictly only speaks once enabled from the menu.
 */
public class DivyangHelper {

    public static final String PREF_DIVYANG_MODE = "PREF_DIVYANG_VOICE_MODE";

    /**
     * Checks if Divyang voice mode is active.
     * By default returns false. Returns true only once enabled from the menu.
     *
     * @param context Application or Activity context.
     * @return true if Divyang mode has been enabled from the menu.
     */
    public static boolean isDivyangModeEnabled(Context context) {
        if (context == null) return false;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            return prefs.getBoolean(PREF_DIVYANG_MODE, false);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDivyangType(String type) {
        return type != null && (type.equalsIgnoreCase("Divyang") || type.equalsIgnoreCase("Divyang with Helper"));
    }

    /**
     * Enables or disables Divyang voice mode from the menu.
     *
     * @param context Application or Activity context.
     * @param enabled true to enable Divyang mode, false to disable.
     */
    public static void setDivyangModeEnabled(Context context, boolean enabled) {
        if (context == null) return;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            prefs.edit().putBoolean(PREF_DIVYANG_MODE, enabled).apply();
        } catch (Exception ignored) {
        }
    }
}
