package com.example.audioquickplay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class AudioPlayerService extends Service {

    public static final String ACTION_PLAY = "com.example.audioquickplay.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.audioquickplay.ACTION_PAUSE";
    public static final String ACTION_TOGGLE = "com.example.audioquickplay.ACTION_TOGGLE";
    public static final String ACTION_SEEK = "com.example.audioquickplay.ACTION_SEEK";
    public static final String ACTION_STATE = "com.example.audioquickplay.ACTION_STATE_CHANGED";

    public static boolean isPlaying = false;

    private static final int NOTIF_ID = 101;
    private static final String CHANNEL_ID = "audio_player_channel";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) return START_NOT_STICKY;

        String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY:
                handlePlay();
                break;
            case ACTION_PAUSE:
                handlePause();
                break;
            case ACTION_TOGGLE:
                if (isPlaying) handlePause(); else handlePlay();
                break;
            case ACTION_SEEK:
                handleSeek(intent.getIntExtra("progress", 0));
                break;
        }

        return START_STICKY;
    }

    private void handlePlay() {
        try {
            SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
            String uriString = prefs.getString("audio_uri", null);
            if (uriString == null) {
                Toast.makeText(this, R.string.msg_no_file_selected, Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = Uri.parse(uriString);

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            if (mediaPlayer == null) {
                Toast.makeText(this, R.string.msg_no_selection, Toast.LENGTH_SHORT).show();
                return;
            }

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                sendStateBroadcast(false);
                stopForeground(false);
                updateNotification(false);
            });

            mediaPlayer.start();
            isPlaying = true;
            startForeground(NOTIF_ID, buildNotification(true));
            sendStateBroadcast(true);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.msg_playback_error, e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void handlePause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            updateNotification(false);
            sendStateBroadcast(false);
        }
    }

    private void handleSeek(int progress) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int duration = mediaPlayer.getDuration();
            int targetPos = (int) (duration * progress / 100.0);
            mediaPlayer.seekTo(targetPos);
        }
    }

    private void sendStateBroadcast(boolean playing) {
        Intent b = new Intent(ACTION_STATE);
        b.putExtra("is_playing", playing);
        sendBroadcast(b);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, "Audio Player", NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(chan);
        }
    }

    private Notification buildNotification(boolean playing) {
        Intent playPauseIntent = new Intent(this, AudioPlayerService.class);
        playPauseIntent.setAction(playing ? ACTION_PAUSE : ACTION_PLAY);
        PendingIntent pi = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        builder.setContentTitle(getString(R.string.notification_title))
                .setContentText(playing ? getString(R.string.notification_playing) : getString(R.string.notification_paused))
                .setSmallIcon(R.drawable.ic_play)
                .addAction(0, playing ? getString(R.string.action_pause) : getString(R.string.action_play), pi)
                .setOngoing(playing)
                .setOnlyAlertOnce(true);

        return builder.build();
    }

    private void updateNotification(boolean playing) {
        Notification n = buildNotification(playing);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify(NOTIF_ID, n);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
