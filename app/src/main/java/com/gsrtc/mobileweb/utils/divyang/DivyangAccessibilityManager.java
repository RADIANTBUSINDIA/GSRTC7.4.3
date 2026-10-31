package com.gsrtc.mobileweb.utils.divyang;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Automatically manages TalkBack accessibility integration and screen announcements.
 * When TalkBack is enabled from Android Settings, provides rich content descriptions
 * and announcements matching native TalkBack navigation flow.
 */
public class DivyangAccessibilityManager implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "DivyangAccessManager";
    private static WeakReference<Activity> currentActivityRef;

    public static void init(Application application) {
        if (application != null) {
            application.registerActivityLifecycleCallbacks(new DivyangAccessibilityManager());
        }
    }

    public static Activity getCurrentActivity() {
        return currentActivityRef != null ? currentActivityRef.get() : null;
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
        currentActivityRef = new WeakReference<>(activity);
        attachCallback(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
        attachCallback(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
        attachCallback(activity);

        // Announce screen entrance and attach rich accessibility descriptions after layout settles
        try {
            if (activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
                View decor = activity.getWindow().getDecorView();
                decor.postDelayed(() -> {
                    if (activity.isFinishing()) return;
                    if (DivyangHelper.isDivyangModeEnabled(activity)) {
                        announceActivityScreen(activity);
                        attachAccessibilityDescriptionsToTree(decor, activity);
                    }
                }, 500);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Traverses view tree and attaches dynamic accessibility delegates to views
     * without content descriptions, providing rich TalkBack descriptions.
     */
    public static void attachAccessibilityDescriptionsToTree(View root, Activity activity) {
        if (root == null || activity == null || activity.isFinishing()) return;

        try {
            if (TextUtils.isEmpty(root.getContentDescription())) {
                if (root.isClickable() || root.isFocusable() || root instanceof android.widget.TextView || root instanceof android.widget.ImageView) {
                    root.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                        @Override
                        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
                            super.onInitializeAccessibilityNodeInfo(host, info);
                            if (TextUtils.isEmpty(host.getContentDescription())) {
                                String details = DivyangViewInspector.extractComprehensiveDetails(host, activity);
                                if (!TextUtils.isEmpty(details)) {
                                    info.setContentDescription(DivyangSpeaker.cleanText(details));
                                }
                            }
                        }
                    });
                }
            }

            if (root instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) root;
                for (int i = 0; i < group.getChildCount(); i++) {
                    attachAccessibilityDescriptionsToTree(group.getChildAt(i), activity);
                }
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
                announcement = "Advance Booking Search screen. Select departure place, arrival place, journey date, and passenger category.";
                break;
            case "AdvanceBookingSearchResultActivity":
                announcement = "Bus Search Results. Explore buses and double tap to select seats.";
                break;
            case "SeatSelectionActivity":
                // Handled directly by SeatSelectionActivity with full bus service description
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
                announcement = "Package Bus Search Results screen. Double tap any bus to select seats.";
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
        if (currentActivityRef != null && currentActivityRef.get() == activity) {
            currentActivityRef.clear();
        }
    }
}
