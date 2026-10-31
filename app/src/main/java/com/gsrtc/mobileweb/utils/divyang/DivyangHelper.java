package com.gsrtc.mobileweb.utils.divyang;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * Manages Divyang accessibility mode detection from Android Settings TalkBack.
 * Automatically active when TalkBack or any spoken accessibility service is enabled in Android Settings.
 */
public class DivyangHelper {

    public static final String PREF_DIVYANG_MODE = "PREF_DIVYANG_VOICE_MODE";

    /**
     * Checks if TalkBack or touch-exploration screen reader is enabled in Android Settings.
     *
     * @param context Application or Activity context.
     * @return true if TalkBack / accessibility screen reader is active in Android Settings.
     */
    public static boolean isTalkBackEnabled(Context context) {
        if (context == null) return false;
        try {
            AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
            if (am == null || !am.isEnabled()) return false;

            // Touch exploration is specifically enabled when TalkBack is actively running
            if (am.isTouchExplorationEnabled()) {
                return true;
            }

            // Also check for any enabled spoken feedback accessibility services
            List<AccessibilityServiceInfo> services = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
            return services != null && !services.isEmpty();
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Checks if Divyang voice/accessibility mode is active.
     * Directly detects if TalkBack or an accessibility service is enabled from Android Settings.
     *
     * @param context Application or Activity context.
     * @return true if TalkBack is enabled in Android Settings.
     */
    public static boolean isDivyangModeEnabled(Context context) {
        if (context == null) return false;
        return isTalkBackEnabled(context);
    }

    public static boolean isDivyangType(String type) {
        return type != null && (type.equalsIgnoreCase("Divyang") || type.equalsIgnoreCase("Divyang with Helper"));
    }

    public static void setDivyangModeEnabled(Context context, boolean enabled) {
        if (context == null) return;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            prefs.edit().putBoolean(PREF_DIVYANG_MODE, enabled).apply();
        } catch (Exception ignored) {
        }
    }
}
