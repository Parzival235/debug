package com.example.audioquickplay;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

/**
 * Wrapper around MediaPlayer for cleaner audio playback management.
 * Handles state, repeat modes, error handling, and callbacks.
 */
public class AudioPlayer {
    private static final String TAG = "AudioPlayer";

    private MediaPlayer mediaPlayer;
    private Uri currentUri;
    private PreferencesManager.RepeatMode repeatMode = PreferencesManager.RepeatMode.OFF;
    private OnStateChangeListener stateListener;
    private OnProgressUpdateListener progressListener;

    private boolean isInitialized = false;

    public interface OnStateChangeListener {
        void onPlayStateChanged(boolean isPlaying);
        void onCompletion();
        void onError(String message);
    }

    public interface OnProgressUpdateListener {
        void onProgressUpdate(int currentMs, int totalMs);
    }

    public AudioPlayer() {
        // Initialize will happen on demand
    }

    /**
     * Initialize or re-initialize MediaPlayer.
     */
    private void initialize(Context context, Uri uri) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setOnCompletionListener(mp -> handleCompletion());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + " extra=" + extra);
                if (stateListener != null) {
                    stateListener.onError("Lỗi MediaPlayer: " + what);
                }
                return true;
            });
            mediaPlayer.setAudioAttributes(
                    new android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );
            mediaPlayer.prepare();
            isInitialized = true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize MediaPlayer", e);
            if (stateListener != null) {
                stateListener.onError("Lỗi khởi tạo: " + e.getMessage());
            }
        }
    }

    /**
     * Play audio from URI.
     */
    public void play(Context context, Uri uri) {
        if (!uri.equals(currentUri) || !isInitialized) {
            currentUri = uri;
            initialize(context, uri);
        }
        if (mediaPlayer != null && isInitialized) {
            mediaPlayer.start();
            if (stateListener != null) {
                stateListener.onPlayStateChanged(true);
            }
        }
    }

    /**
     * Pause playback.
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (stateListener != null) {
                stateListener.onPlayStateChanged(false);
            }
        }
    }

    /**
     * Toggle play/pause.
     */
    public void togglePlayPause(Context context, Uri uri) {
        if (isPlaying()) {
            pause();
        } else {
            play(context, uri);
        }
    }

    /**
     * Seek to position.
     */
    public void seek(int positionMs) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.seekTo(positionMs);
            } catch (Exception e) {
                Log.e(TAG, "Seek failed", e);
            }
        }
    }

    /**
     * Get current playback position.
     */
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * Get total duration.
     */
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * Check if playing.
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * Set repeat mode and update playback if needed.
     */
    public void setRepeatMode(PreferencesManager.RepeatMode mode) {
        this.repeatMode = mode;
    }

    /**
     * Set state change listener.
     */
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.stateListener = listener;
    }

    /**
     * Set progress update listener.
     */
    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        this.progressListener = listener;
    }

    /**
     * Handle track completion based on repeat mode.
     */
    private void handleCompletion() {
        switch (repeatMode) {
            case ONE:
                // Repeat single track
                seek(0);
                mediaPlayer.start();
                break;
            case ALL:
                // Repeat playlist (in this case, just restart)
                seek(0);
                mediaPlayer.start();
                break;
            case OFF:
            default:
                pause();
                if (stateListener != null) {
                    stateListener.onCompletion();
                }
                break;
        }
    }

    /**
     * Release MediaPlayer resources.
     */
    public void release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, "Error releasing MediaPlayer", e);
            }
            mediaPlayer = null;
        }
        isInitialized = false;
    }
}
