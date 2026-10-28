package com.gsrtc.mobileweb.utils.divyang;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

/**
 * Singleton Text-to-Speech (TTS) speaker engine for Divyang accessibility mode.
 * Speaks information in English and provides subtle haptic confirmation.
 */
public class DivyangSpeaker {

    private static final String TAG = "DivyangSpeaker";
    private static volatile DivyangSpeaker instance;

    private final Context context;
    private TextToSpeech tts;
    private boolean isReady = false;
    private final Vibrator vibrator;
    private final Queue<String> pendingQueue = new LinkedList<>();
    private long lastSpeakTime = 0;

    private DivyangSpeaker(Context context) {
        this.context = context.getApplicationContext();
        this.vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        initTts();
    }

    public static DivyangSpeaker getInstance(Context context) {
        if (instance == null) {
            synchronized (DivyangSpeaker.class) {
                if (instance == null) {
                    instance = new DivyangSpeaker(context);
                }
            }
        }
        return instance;
    }

    public long getLastSpeakTime() {
        return lastSpeakTime;
    }

    public static void init(Context context) {
        getInstance(context);
    }

    private synchronized void initTts() {
        try {
            Log.d(TAG, "Initializing TextToSpeech engine...");
            TextToSpeech.OnInitListener listener = status -> {
                Log.d(TAG, "TextToSpeech onInit status: " + status);
                if (status == TextToSpeech.SUCCESS) {
                    try {
                        int langResult = tts.setLanguage(Locale.ENGLISH);
                        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                            tts.setLanguage(Locale.US);
                        }
                        tts.setSpeechRate(0.95f);
                        tts.setPitch(1.0f);

                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                                Log.d(TAG, "TTS onStart utterance: " + utteranceId);
                            }

                            @Override
                            public void onDone(String utteranceId) {
                                Log.d(TAG, "TTS onDone utterance: " + utteranceId);
                            }

                            @Override
                            public void onError(String utteranceId) {
                                Log.e(TAG, "TTS onError utterance: " + utteranceId);
                            }
                        });

                        isReady = true;
                        Log.d(TAG, "TTS successfully initialized and ready!");
                        flushPending();
                    } catch (Exception e) {
                        Log.e(TAG, "Error configuring TTS parameters", e);
                    }
                } else {
                    Log.e(TAG, "TTS initialization failed with status: " + status);
                }
            };

            // Attempt to bind to Google TTS first, fallback to system default
            tts = new TextToSpeech(context, listener, "com.google.android.tts");
        } catch (Exception e) {
            Log.e(TAG, "Exception during TTS init, falling back to default constructor", e);
            try {
                tts = new TextToSpeech(context, status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.ENGLISH);
                        isReady = true;
                        flushPending();
                    }
                });
            } catch (Exception ex) {
                Log.e(TAG, "Failed to create fallback TTS", ex);
            }
        }
    }

    private synchronized void flushPending() {
        if (!DivyangHelper.isDivyangModeEnabled(context)) {
            pendingQueue.clear();
            return;
        }
        while (!pendingQueue.isEmpty()) {
            String text = pendingQueue.poll();
            if (text != null && !text.isEmpty()) {
                Log.d(TAG, "Flushing pending speech: " + text);
                speakInternal(text);
            }
        }
    }

    /**
     * Speaks the provided text out loud if it's not empty and Divyang mode is enabled.
     * Stops previous speech to speak the latest clicked item immediately.
     */
    public synchronized void speak(String rawText) {
        speak(rawText, false);
    }

    /**
     * Speaks the provided text.
     * If force is false, speech is strictly suppressed when Divyang mode is disabled.
     * If force is true, speech is allowed (e.g. for menu toggle confirmation announcements).
     */
    public synchronized void speak(String rawText, boolean force) {
        if (!force && !DivyangHelper.isDivyangModeEnabled(context)) {
            return;
        }
        if (rawText == null) return;
        final String text = cleanText(rawText);
        if (text.isEmpty()) return;

        vibrateFeedback();
        lastSpeakTime = android.os.SystemClock.uptimeMillis();

        if (tts == null) {
            initTts();
        }

        if (!isReady) {
            Log.d(TAG, "TTS not ready yet, queuing text: " + text);
            pendingQueue.clear();
            pendingQueue.add(text);
            return;
        }

        speakInternal(text);
    }

    private void speakInternal(String text) {
        try {
            Log.d(TAG, "Speaking text: " + text);
            tts.stop();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle params = new Bundle();
                params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);
                params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "divyang_" + System.currentTimeMillis());
            } else {
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
                params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0");
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error speaking text", e);
        }
    }

    /**
     * Subtle haptic vibration feedback on click.
     */
    private void vibrateFeedback() {
        if (!DivyangHelper.isDivyangModeEnabled(context)) {
            return;
        }
        if (vibrator != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(35);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized void stop() {
        if (tts != null) {
            try {
                tts.stop();
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized void shutdown() {
        // Kept for explicit application teardown only. Not called on activity destroy.
        if (tts != null) {
            try {
                tts.stop();
                tts.shutdown();
            } catch (Exception ignored) {
            }
            tts = null;
            isReady = false;
        }
    }

    public static String cleanText(String input) {
        if (input == null) return "";
        String s = input.trim();

        // Standardize common currency expressions
        s = s.replaceAll("(?i)\\bINR\\s*", " ");
        s = s.replaceAll("₹\\s*", " ");
        s = s.replaceAll("(?i)\\bRs\\.?\\s*", " ");

        // Expand common travel abbreviations for natural speech
        s = s.replaceAll("(?i)\\bNo\\(s\\)\\b|\\bNo\\.\\b", " Number ");
        s = s.replaceAll("(?i)\\bDep\\.?\\b", " Departure ");
        s = s.replaceAll("(?i)\\bArr\\.?\\b", " Arrival ");
        s = s.replaceAll("(?i)\\bExp\\.?\\b", " Express ");
        s = s.replaceAll("(?i)\\bSrv\\.?\\b", " Service ");
        s = s.replaceAll("(?i)\\bHrs\\b|\\bhrs\\b", " hours ");
        s = s.replaceAll("(?i)\\bDob\\b", " Date of Birth ");
        s = s.replaceAll("(?i)\\bOtp\\b", " O T P ");
        s = s.replaceAll("(?i)\\bAuth\\b", " Authority ");
        s = s.replaceAll("(?i)\\bId\\b", " I D ");
        s = s.replaceAll("(?i)\\bRef\\.?\\s*No\\b", " Reference Number ");
        s = s.replaceAll("(?i)\\bPNR\\b", " P N R ");
        s = s.replaceAll("(?i)\\bPass\\b", " Passenger ");
        s = s.replaceAll("(?i)\\bCls\\b", " Class ");
        s = s.replaceAll("(?i)\\bServ\\b", " Service ");
        s = s.replaceAll("(?i)\\bRtn\\b", " Return ");
        s = s.replaceAll("(?i)\\bRes\\.?\\s*Fee\\b", " Reservation Fee ");
        s = s.replaceAll("(?i)\\bServ\\.?\\s*Tax\\b", " Service Tax ");
        s = s.replaceAll("(?i)\\bAc\\b", " A C ");
        s = s.replaceAll("(?i)\\bNon-?Ac\\b", " Non A C ");
        s = s.replaceAll("(?i)\\bLb\\b", " Lower Berth ");
        s = s.replaceAll("(?i)\\bUb\\b", " Upper Berth ");
        s = s.replaceAll("(?i)\\bSl\\b", " Sleeper ");
        s = s.replaceAll("(?i)\\bSt\\b", " Seater ");
        s = s.replaceAll("(?i)\\bSleeper\\b", " Sleeper ");
        s = s.replaceAll("(?i)\\bSeater\\b", " Seater ");
        s = s.replaceAll("(?i)\\bGurjarnagari\\b", " Gurjarnagari ");
        s = s.replaceAll("(?i)\\bKm\\b", " Kilometers ");
        s = s.replaceAll("(?i)\\bGovt\\.?\\b", " Government ");

        s = s.replaceAll("[\\r\\n\\t]+", " ");
        s = s.replaceAll("^[\\s:;\\-*#]+|[\\s:;\\-*#]+$", "");
        s = s.replaceAll("\\s{2,}", " ");
        return s.trim();
    }
}
