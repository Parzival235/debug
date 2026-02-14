package com.example.audioquickplay;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * Secure preferences manager using Android security library.
 * All sensitive data is encrypted at rest.
 */
public class PreferencesManager {
    private static PreferencesManager instance;
    private final SharedPreferences prefs;

    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_AUDIO_URI = "audio_uri";
    public static final String KEY_REPEAT_MODE = "repeat_mode";
    public static final String KEY_VOLUME_LEVEL = "volume_level";

    public enum RepeatMode {
        OFF(0), ONE(1), ALL(2);
        private final int value;
        RepeatMode(int value) { this.value = value; }
        public int getValue() { return value; }
        public static RepeatMode fromValue(int value) {
            for (RepeatMode mode : RepeatMode.values()) {
                if (mode.value == value) return mode;
            }
            return OFF;
        }
    }

    private PreferencesManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            prefs = EncryptedSharedPreferences.create(
                    context,
                    "audio_quick_play_secure",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EncryptedSharedPreferences", e);
        }
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    // Login & Session
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean logged) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, logged).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public void setUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    // Audio Preferences
    public String getAudioUri() {
        return prefs.getString(KEY_AUDIO_URI, null);
    }

    public void setAudioUri(String uri) {
        prefs.edit().putString(KEY_AUDIO_URI, uri).apply();
    }

    public RepeatMode getRepeatMode() {
        int value = prefs.getInt(KEY_REPEAT_MODE, RepeatMode.OFF.getValue());
        return RepeatMode.fromValue(value);
    }

    public void setRepeatMode(RepeatMode mode) {
        prefs.edit().putInt(KEY_REPEAT_MODE, mode.getValue()).apply();
    }

    public int getVolumeLevel() {
        return prefs.getInt(KEY_VOLUME_LEVEL, 100);
    }

    public void setVolumeLevel(int level) {
        prefs.edit().putInt(KEY_VOLUME_LEVEL, Math.max(0, Math.min(100, level))).apply();
    }
}
