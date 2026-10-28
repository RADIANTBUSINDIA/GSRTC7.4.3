package com.gsrtc.mobileweb.utils.divyang;

import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive View Inspector for Divyang accessibility mode.
 * Inspects clicked views and extracts all context, labels, values,
 * and card details into natural spoken speech.
 */
public class DivyangViewInspector {

    private static final String TAG = "DivyangViewInspector";

    private static String lastSpokenText = "";
    private static long lastSpokenTime = 0;

    /**
     * Inspects the view hierarchy at the clicked coordinates and speaks comprehensive details.
     */
    public static void inspectAndSpeakClick(Activity activity, float x, float y) {
        if (activity == null || activity.isFinishing()) return;

        // If an explicit listener already spoke recently (< 400ms), don't duplicate or overwrite
        if (SystemClock.uptimeMillis() - DivyangSpeaker.getInstance(activity).getLastSpeakTime() < 400) {
            Log.d(TAG, "Suppressed inspector speech because an explicit listener already spoke recently.");
            return;
        }

        View decorView = null;
        try {
            if (activity.getWindow() != null) {
                decorView = activity.getWindow().getDecorView();
            }
        } catch (Exception ignored) {
        }

        if (decorView == null) return;

        View target = findDeepestViewAt(decorView, x, y);
        Log.d(TAG, "Touch at (" + x + ", " + y + ") hit target: " + (target != null ? target.getClass().getSimpleName() : "null"));
        if (target == null) return;

        // In SeatSelectionActivity, seat interactions are directly handled by SeatSelectionAdapter
        if (activity instanceof com.gsrtc.mobileweb.ui.activities.advance_booking.SeatSelectionActivity) {
            View cur = target;
            while (cur != null) {
                if (cur instanceof RecyclerView || (cur.getId() != View.NO_ID && cur.getId() == com.gsrtc.mobileweb.R.id.recycler_view)) {
                    return;
                }
                if (cur.getParent() instanceof View) {
                    cur = (View) cur.getParent();
                } else {
                    break;
                }
            }
        }

        String detailedText = extractComprehensiveDetails(target, activity);
        if (detailedText != null) {
            detailedText = DivyangSpeaker.cleanText(detailedText);
        }

        Log.d(TAG, "Extracted detailed text: '" + detailedText + "'");

        if (detailedText != null && !detailedText.isEmpty()) {
            long now = SystemClock.uptimeMillis();
            if (detailedText.equalsIgnoreCase(lastSpokenText) && (now - lastSpokenTime < 350)) {
                Log.d(TAG, "Suppressed duplicate speech for: " + detailedText);
                return;
            }
            lastSpokenText = detailedText;
            lastSpokenTime = now;

            Log.d(TAG, "==> Speaking comprehensive details on click: " + detailedText);
            DivyangSpeaker.getInstance(activity).speak(detailedText);

            try {
                decorView.announceForAccessibility(detailedText);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Finds the deepest visible view containing coordinates (x, y).
     */
    public static View findDeepestViewAt(View root, float x, float y) {
        if (root == null || root.getVisibility() != View.VISIBLE) {
            return null;
        }

        int[] location = new int[2];
        root.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + root.getWidth();
        int bottom = top + root.getHeight();

        if (x < left || x > right || y < top || y > bottom) {
            return null;
        }

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                View target = findDeepestViewAt(child, x, y);
                if (target != null) {
                    return target;
                }
            }
        }

        return root;
    }

    /**
     * Extracts full, rich details for the given view and its surroundings.
     */
    public static String extractComprehensiveDetails(View view, Activity activity) {
        if (view == null) return null;

        // 1. Direct interactive form controls
        String controlDetails = checkInteractiveControl(view);
        if (controlDetails != null) {
            return controlDetails;
        }

        // 2. Check if view is inside a CardView, RecyclerView Item, or major item container
        ViewGroup container = findContainingCardOrItem(view);
        if (container != null) {
            String containerDetails = extractContainerDetails(container, activity);
            if (!TextUtils.isEmpty(containerDetails)) {
                return containerDetails;
            }
        }

        // 3. Check if view is part of a Label-Value row (e.g. "Fare: 289 Rupees")
        String rowDetails = checkLabelValueRow(view);
        if (rowDetails != null) {
            return rowDetails;
        }

        // 4. Check Content Description
        CharSequence contentDesc = view.getContentDescription();
        if (contentDesc != null && !contentDesc.toString().trim().isEmpty()) {
            return contentDesc.toString().trim();
        }

        // 5. Direct TextView, EditText or Spinner
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            CharSequence text = tv.getText();
            if (text != null && !text.toString().trim().isEmpty()) {
                return formatTextWithNearbyContext(tv, text.toString().trim());
            }
            CharSequence hint = tv.getHint();
            if (hint != null && !hint.toString().trim().isEmpty()) {
                return hint.toString().trim();
            }
        }

        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (spinner.getSelectedItem() != null) {
                return spinner.getSelectedItem().toString().trim() + ", dropdown menu";
            }
        }

        // 6. If target view is itself a ViewGroup, collect all text
        if (view instanceof ViewGroup) {
            String groupText = collectTextFromViewGroup((ViewGroup) view);
            if (groupText != null && !groupText.isEmpty()) {
                return groupText;
            }
        }

        // 7. Check parent view groups up to 3 levels
        View current = view;
        for (int depth = 0; depth < 3; depth++) {
            if (current.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) current.getParent();
                String collectedText = collectTextFromViewGroup(parent);
                if (collectedText != null && !collectedText.isEmpty()) {
                    return collectedText;
                }
                current = parent;
            } else {
                break;
            }
        }

        // 8. Resource ID entry name fallback
        if (activity != null && view.getId() != View.NO_ID) {
            return fallbackFromResourceId(view.getId(), activity);
        }

        return null;
    }

    /**
     * Checks if view is a specific form control (RadioButton, CheckBox, Switch, Button, EditText).
     */
    private static String checkInteractiveControl(View view) {
        // RadioButton
        if (view instanceof RadioButton) {
            RadioButton rb = (RadioButton) view;
            String label = rb.getText() != null ? rb.getText().toString().trim() : "";
            return label + (rb.isChecked() ? ", selected" : ", not selected");
        }

        // CheckBox
        if (view instanceof CheckBox) {
            CheckBox cb = (CheckBox) view;
            String label = cb.getText() != null ? cb.getText().toString().trim() : "";
            return label + (cb.isChecked() ? ", checked" : ", not checked");
        }

        // Switch
        if (view instanceof SwitchCompat || (view instanceof CompoundButton && view.getClass().getSimpleName().contains("Switch"))) {
            CompoundButton sw = (CompoundButton) view;
            String label = sw.getText() != null ? sw.getText().toString().trim() : "";
            return label + (sw.isChecked() ? ", on" : ", off");
        }

        // EditText
        if (view instanceof EditText) {
            EditText et = (EditText) view;
            String hint = et.getHint() != null ? et.getHint().toString().trim() : "";
            String text = et.getText() != null ? et.getText().toString().trim() : "";
            if (TextUtils.isEmpty(text)) {
                return (TextUtils.isEmpty(hint) ? "Input field" : hint) + ", empty. Tap to enter.";
            } else {
                return (TextUtils.isEmpty(hint) ? "" : hint + ": ") + text;
            }
        }

        // Button
        if (view instanceof Button) {
            Button btn = (Button) view;
            String text = btn.getText() != null ? btn.getText().toString().trim() : "";
            if (!TextUtils.isEmpty(text)) {
                return text + " button";
            }
        }

        // ImageView or ImageButton
        if (view instanceof android.widget.ImageView) {
            CharSequence desc = view.getContentDescription();
            if (desc != null && !TextUtils.isEmpty(desc)) {
                return desc.toString();
            }
            if (view.getId() != View.NO_ID && view.getContext() != null) {
                try {
                    String resName = view.getContext().getResources().getResourceEntryName(view.getId()).toLowerCase();
                    if (resName.contains("swap")) return "Swap places";
                    if (resName.contains("ham") || resName.contains("menu")) return "Navigation menu";
                    if (resName.contains("back")) return "Back button";
                    if (resName.contains("calendar") || resName.contains("date")) return "Select date";
                    if (resName.contains("clear") || resName.contains("cross")) return "Clear text";
                    if (resName.contains("search")) return "Search";
                    if (resName.contains("filter")) return "Filter options";
                    if (resName.contains("share")) return "Share application";
                } catch (Exception ignored) {
                }
            }
        }

        return null;
    }

    /**
     * Finds containing item card or list row.
     */
    private static ViewGroup findContainingCardOrItem(View view) {
        View current = view;
        while (current != null && current.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) current.getParent();
            // Direct child of RecyclerView, ListView or ViewPager
            if (parent instanceof RecyclerView || parent.getClass().getSimpleName().contains("RecyclerView")
                    || parent.getClass().getSimpleName().contains("ListView")
                    || parent.getClass().getSimpleName().contains("ViewPager")) {
                if (current instanceof ViewGroup) {
                    return (ViewGroup) current;
                }
            }
            // CardView
            if (current.getClass().getSimpleName().contains("CardView")) {
                return (ViewGroup) current;
            }
            // Item container by resource ID
            if (current.getId() != View.NO_ID) {
                try {
                    String entry = current.getResources().getResourceEntryName(current.getId()).toLowerCase();
                    if (entry.contains("card") || entry.equals("container") || entry.contains("item_row")
                            || entry.contains("bus_item") || entry.contains("service_item") || entry.contains("ticket_item")) {
                        if (current instanceof ViewGroup && ((ViewGroup) current).getChildCount() > 1) {
                            return (ViewGroup) current;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
            current = parent;
        }
        return null;
    }

    /**
     * Extracts all information from a list card or container into a coherent description.
     */
    private static String extractContainerDetails(ViewGroup container, Activity activity) {
        if (container == null) return null;

        // Try smart bus service item extraction
        String busCardSpeech = tryExtractBusCard(container);
        if (!TextUtils.isEmpty(busCardSpeech)) {
            return busCardSpeech;
        }

        // Generic container extraction: gather all informative TextViews
        List<String> items = new ArrayList<>();
        collectInformativeStrings(container, items);

        if (items.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            if (sb.length() > 0) sb.append(". ");
            sb.append(item);
        }

        if (container.isClickable() || container.hasOnClickListeners()) {
            sb.append(". Tap to select");
        }

        return sb.toString().trim();
    }

    /**
     * Checks if container is a GSRTC Bus Search Result card and builds a natural sentence.
     */
    private static String tryExtractBusCard(ViewGroup container) {
        try {
            BusViewCollector collector = new BusViewCollector();
            findBusViewsRecursive(container, collector);

            if (collector.hasAnyBusInfo()) {
                StringBuilder sb = new StringBuilder();
                if (collector.serviceClass != null) {
                    sb.append("Bus Service: ").append(collector.serviceClass).append(". ");
                }
                if (collector.route != null) {
                    sb.append("Route: ").append(collector.route).append(". ");
                }
                if (collector.depTime != null) {
                    sb.append("Departure: ").append(collector.depTime).append(". ");
                }
                if (collector.arrTime != null) {
                    sb.append("Arrival: ").append(collector.arrTime).append(". ");
                }
                if (collector.hours != null) {
                    sb.append("Journey duration: ").append(collector.hours).append(". ");
                }
                if (collector.seats != null) {
                    if (collector.seats.equalsIgnoreCase("FULL") || collector.seats.equalsIgnoreCase("0")) {
                        sb.append("Bus Full, no seats available. ");
                    } else {
                        sb.append(collector.seats).append(" available. ");
                    }
                }
                if (collector.price != null) {
                    sb.append("Fare: ").append(collector.price).append(" Rupees. ");
                }
                if (collector.viaPlaces != null && !collector.viaPlaces.trim().isEmpty()) {
                    sb.append(collector.viaPlaces).append(". ");
                }

                sb.append("Tap to select seats.");
                return sb.toString().trim();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static class BusViewCollector {
        String serviceClass;
        String route;
        String depTime;
        String arrTime;
        String hours;
        String seats;
        String price;
        String viaPlaces;

        boolean hasAnyBusInfo() {
            return (route != null || depTime != null || seats != null || price != null);
        }
    }

    private static void findBusViewsRecursive(View view, BusViewCollector collector) {
        if (view == null || view.getVisibility() != View.VISIBLE) return;

        if (view instanceof TextView && view.getId() != View.NO_ID) {
            try {
                String entry = view.getResources().getResourceEntryName(view.getId()).toLowerCase();
                String text = ((TextView) view).getText() != null ? ((TextView) view).getText().toString().trim() : "";
                if (!TextUtils.isEmpty(text)) {
                    if (entry.contains("bus_route") || entry.contains("route")) {
                        collector.route = text;
                    } else if (entry.contains("booking_class") || entry.contains("serviceclass")) {
                        collector.serviceClass = text;
                    } else if (entry.contains("dep_time") || entry.contains("departure_time")) {
                        collector.depTime = text;
                    } else if (entry.contains("arrival_time") || entry.contains("arr_time")) {
                        collector.arrTime = text;
                    } else if (entry.contains("journey_hours")) {
                        collector.hours = text;
                    } else if (entry.contains("seat_avail") || entry.contains("seats")) {
                        collector.seats = text;
                    } else if (entry.contains("price") || entry.contains("fare")) {
                        collector.price = text;
                    } else if (entry.contains("via_places")) {
                        collector.viaPlaces = text;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                findBusViewsRecursive(vg.getChildAt(i), collector);
            }
        }
    }

    /**
     * Checks if view belongs to a horizontal Label-Value row (e.g. [Label: "Basic Fare", Value: "289.00"]).
     */
    private static String checkLabelValueRow(View view) {
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent.getChildCount() >= 2 && parent.getChildCount() <= 4) {
                List<TextView> textViews = new ArrayList<>();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    if (child instanceof TextView && child.getVisibility() == View.VISIBLE) {
                        textViews.add((TextView) child);
                    }
                }

                if (textViews.size() == 2) {
                    String label = textViews.get(0).getText() != null ? textViews.get(0).getText().toString().trim() : "";
                    String value = textViews.get(1).getText() != null ? textViews.get(1).getText().toString().trim() : "";
                    if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(value)) {
                        return label + ": " + value;
                    }
                }
            }
        }
        return null;
    }

    private static String formatTextWithNearbyContext(TextView tv, String text) {
        if (text.matches("^[0-9]+(\\.[0-9]+)?$") || text.matches("(?i).*\\b(inr|rs)\\b.*")) {
            if (tv.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) tv.getParent();
                int index = parent.indexOfChild(tv);
                if (index > 0) {
                    View prev = parent.getChildAt(index - 1);
                    if (prev instanceof TextView) {
                        CharSequence prevText = ((TextView) prev).getText();
                        if (prevText != null && !TextUtils.isEmpty(prevText)) {
                            return prevText.toString().trim() + ": " + text;
                        }
                    }
                }
            }
        }
        return text;
    }

    private static void collectInformativeStrings(ViewGroup group, List<String> list) {
        if (group == null || group.getVisibility() != View.VISIBLE) return;

        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE) continue;

            if (child instanceof ViewGroup) {
                ViewGroup subGroup = (ViewGroup) child;
                if (subGroup.getChildCount() >= 2 && subGroup.getChildCount() <= 4) {
                    String row = checkLabelValueRow(subGroup.getChildAt(0));
                    if (row != null) {
                        list.add(row);
                        continue;
                    }
                }
                collectInformativeStrings(subGroup, list);
            } else if (child instanceof TextView) {
                CharSequence text = ((TextView) child).getText();
                if (text == null || text.toString().trim().isEmpty()) {
                    text = ((TextView) child).getHint();
                }
                if (text != null && !text.toString().trim().isEmpty()) {
                    String s = text.toString().trim();
                    if (!list.contains(s)) {
                        list.add(s);
                    }
                }
            }
        }
    }

    private static String collectTextFromViewGroup(ViewGroup group) {
        List<String> list = new ArrayList<>();
        collectInformativeStrings(group, list);
        if (list.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if (sb.length() > 0) sb.append(". ");
            sb.append(s);
        }
        return sb.toString();
    }

    private static String fallbackFromResourceId(int id, Activity activity) {
        try {
            String entryName = activity.getResources().getResourceEntryName(id).toLowerCase();
            if (entryName.contains("back") || entryName.contains("prev")) {
                return "Back button";
            } else if (entryName.contains("close")) {
                return "Close button";
            } else if (entryName.contains("ham") || entryName.contains("menu") || entryName.contains("drawer")) {
                return "Menu button";
            } else if (entryName.contains("share")) {
                return "Share button";
            } else if (entryName.contains("search")) {
                return "Search button";
            } else if (entryName.contains("filter")) {
                return "Filter button";
            } else if (entryName.contains("refresh")) {
                return "Refresh button";
            } else if (entryName.contains("help")) {
                return "Help button";
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
