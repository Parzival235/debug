package com.example.audioquickplay;

import java.util.concurrent.TimeUnit;

/**
 * Utility methods for audio player formatting and conversion.
 */
public class PlayerUtils {

    /**
     * Format milliseconds to MM:SS format.
     */
    public static String formatTime(long millis) {
        if (millis < 0) return "00:00";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Parse MM:SS format to milliseconds.
     */
    public static long parseTime(String timeStr) {
        String[] parts = timeStr.split(":");
        if (parts.length != 2) return 0;
        try {
            long minutes = Long.parseLong(parts[0]);
            long seconds = Long.parseLong(parts[1]);
            return TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Convert progress (0-100) to milliseconds based on duration.
     */
    public static int progressToMillis(int progress, int duration) {
        return (int) (duration * progress / 100.0);
    }

    /**
     * Convert milliseconds to progress (0-100) based on duration.
     */
    public static int millisToProgress(int currentMs, int duration) {
        if (duration == 0) return 0;
        return (int) (currentMs * 100.0 / duration);
    }

    /**
     * Check if file URI is valid audio format.
     */
    public static boolean isValidAudioFile(String filename) {
        if (filename == null) return false;
        filename = filename.toLowerCase();
        return filename.endsWith(".mp3") || 
               filename.endsWith(".wav") || 
               filename.endsWith(".ogg") || 
               filename.endsWith(".flac") || 
               filename.endsWith(".aac") ||
               filename.endsWith(".m4a");
    }
}
