package com.gsrtc.mobileweb.utils.divyang;

import android.app.Activity;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Window.Callback wrapper that intercepts touch clicks and menu interactions
 * across all activities when Divyang mode is active.
 */
public class DivyangWindowCallback implements Window.Callback {

    private static final String TAG = "DivyangWindowCallback";

    private final Window.Callback originalCallback;
    private final WeakReference<Activity> activityRef;
    private float downX;
    private float downY;
    private long downTime;
    private final int touchSlop;

    public DivyangWindowCallback(Activity activity, Window.Callback originalCallback) {
        this.activityRef = new WeakReference<>(activity);
        this.originalCallback = originalCallback;
        int slop = 24;
        try {
            slop = ViewConfiguration.get(activity).getScaledTouchSlop();
        } catch (Exception ignored) {
        }
        this.touchSlop = slop;
    }

    public Window.Callback getOriginalCallback() {
        return originalCallback;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean handled = originalCallback != null && originalCallback.dispatchTouchEvent(event);
        Activity activity = activityRef.get();
        if (activity != null && DivyangHelper.isDivyangModeEnabled(activity)) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                downX = event.getRawX();
                downY = event.getRawY();
                downTime = SystemClock.uptimeMillis();
            } else if (action == MotionEvent.ACTION_UP) {
                float upX = event.getRawX();
                float upY = event.getRawY();
                long duration = SystemClock.uptimeMillis() - downTime;
                float distance = (float) Math.hypot(upX - downX, upY - downY);

                if (duration <= 1500 && distance <= touchSlop * 4.0f) {
                    View decor = activity.getWindow() != null ? activity.getWindow().getDecorView() : null;
                    if (decor != null) {
                        decor.postDelayed(() -> {
                            if (!activity.isFinishing()) {
                                DivyangViewInspector.inspectAndSpeakClick(activity, upX, upY);
                            }
                        }, 80);
                    }
                }
            }
        }
        return handled;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        Activity activity = activityRef.get();
        if (activity != null && DivyangHelper.isDivyangModeEnabled(activity)) {
            if (item.getItemId() == android.R.id.home) {
                DivyangSpeaker.getInstance(activity).speak("Navigation menu or back");
            } else {
                CharSequence title = item.getTitle();
                if (title != null && !title.toString().trim().isEmpty()) {
                    Log.d(TAG, "Menu item selected: " + title);
                    DivyangSpeaker.getInstance(activity).speak(title.toString().trim());
                }
            }
        }
        return originalCallback != null && originalCallback.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return originalCallback != null && originalCallback.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return originalCallback != null && originalCallback.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return originalCallback != null && originalCallback.dispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return originalCallback != null && originalCallback.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return originalCallback != null && originalCallback.dispatchPopulateAccessibilityEvent(event);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return originalCallback != null ? originalCallback.onCreatePanelView(featureId) : null;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        return originalCallback != null && originalCallback.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, @Nullable View view, @NonNull Menu menu) {
        return originalCallback != null && originalCallback.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, @NonNull Menu menu) {
        return originalCallback != null && originalCallback.onMenuOpened(featureId, menu);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        if (originalCallback != null) {
            originalCallback.onWindowAttributesChanged(attrs);
        }
    }

    @Override
    public void onContentChanged() {
        if (originalCallback != null) {
            originalCallback.onContentChanged();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (originalCallback != null) {
            originalCallback.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void onAttachedToWindow() {
        if (originalCallback != null) {
            originalCallback.onAttachedToWindow();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        if (originalCallback != null) {
            originalCallback.onDetachedFromWindow();
        }
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        if (originalCallback != null) {
            originalCallback.onPanelClosed(featureId, menu);
        }
    }

    @Override
    public boolean onSearchRequested() {
        return originalCallback != null && originalCallback.onSearchRequested();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return originalCallback != null && originalCallback.onSearchRequested(searchEvent);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return originalCallback != null ? originalCallback.onWindowStartingActionMode(callback) : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return originalCallback != null ? originalCallback.onWindowStartingActionMode(callback, type) : null;
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (originalCallback != null) {
            originalCallback.onActionModeStarted(mode);
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        if (originalCallback != null) {
            originalCallback.onActionModeFinished(mode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onProvideKeyboardShortcuts(List<android.view.KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {
        if (originalCallback != null) {
            originalCallback.onProvideKeyboardShortcuts(data, menu, deviceId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if (originalCallback != null) {
            originalCallback.onPointerCaptureChanged(hasCapture);
        }
    }
}
