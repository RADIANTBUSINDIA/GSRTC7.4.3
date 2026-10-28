package com.gsrtc.mobileweb.utils.divyang;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Automatically attaches DivyangWindowCallback to every Activity across the entire application lifecycle,
 * and announces screen context when a screen opens in Divyang mode.
 */
public class DivyangAccessibilityManager implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "DivyangAccessManager";

    public static void init(Application application) {
        if (application != null) {
            application.registerActivityLifecycleCallbacks(new DivyangAccessibilityManager());
        }
    }

    private void attachCallback(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        try {
            Window window = activity.getWindow();
            if (window != null) {
                Window.Callback currentCallback = window.getCallback();
                if (!(currentCallback instanceof DivyangWindowCallback)) {
                    window.setCallback(new DivyangWindowCallback(activity, currentCallback));
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        attachCallback(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        attachCallback(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        attachCallback(activity);

        // Announce screen entrance after short delay for layouts to finish inflating
        try {
            if (activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
                activity.getWindow().getDecorView().postDelayed(() -> {
                    if (activity.isFinishing()) return;
                    if (DivyangHelper.isDivyangModeEnabled(activity)) {
                        announceActivityScreen(activity);
                    }
                }, 600);
            }
        } catch (Exception ignored) {
        }
    }

    private void announceActivityScreen(Activity activity) {
        String className = activity.getClass().getSimpleName();
        String announcement = "";

        switch (className) {
            case "Language_Select":
                // Do not speak prior to user selection of language
                break;
            case "HomeActivity":
                announcement = "Home screen. Select Advance Booking, Current Booking, or open menu.";
                break;
            case "AdvanceBookingSearchActivity":
                announcement = "Advance Booking Search screen. Select from place, to place, journey date, and passenger category.";
                break;
            case "AdvanceBookingSearchResultActivity":
                announcement = "Bus Search Results. Tap any bus card to hear full details and select seats.";
                break;
            case "SeatSelectionActivity":
                // Spoken directly by SeatSelectionActivity with complete bus description (Seater/Sleeper, class, trip code)
                break;
            case "PassengerInfoActivity":
                announcement = "Passenger Information screen. Enter passenger details and Divyang card information.";
                break;
            case "FareDetailsActivity":
                announcement = "Fare Details screen. Review your journey and fare summary.";
                break;
            case "ReferenceNumberActivity":
                announcement = "Booking Confirmation screen. Reference number generated.";
                break;
            case "CurrentBookingNativeSearchActivity":
                announcement = "Current Booking screen. Select from, to, and service.";
                break;
            case "BookingSuccessActivity":
                announcement = "Booking Confirmation screen.";
                break;
            case "TicketCancellationActivity":
            case "CancelTicketActivity":
                announcement = "Ticket Cancellation screen. Enter PNR and mobile number to search booking.";
                break;
            case "CancellationPassengerSelectionActivity":
                announcement = "Cancellation Passenger Selection screen.";
                break;
            case "CancellationFareDetailsActivity":
                announcement = "Cancellation Refund Details screen. Review refund amount.";
                break;
            case "CancellationSuccessActivity":
                announcement = "Cancellation Confirmation screen.";
                break;
            case "BookingHistoryActivity":
            case "MyBookingsActivity":
                announcement = "My Bookings screen.";
                break;
            case "PackageBookingSearchResult":
                announcement = "Package Bus Search Results screen. Tap any bus to select seats.";
                break;
            case "PackageFareDetails":
                announcement = "Package Fare Details screen. Review your journey and fare summary.";
                break;
            case "PackageSuccessActivity":
                announcement = "Package Booking Confirmation screen.";
                break;
            default:
                break;
        }

        if (!TextUtils.isEmpty(announcement)) {
            Log.d(TAG, "Screen entrance announcement: " + announcement);
            DivyangSpeaker.getInstance(activity).speak(announcement);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}
