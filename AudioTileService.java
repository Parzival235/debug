package com.example.audioquickplay;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

public class AudioTileService extends TileService {

    private static MediaPlayer mediaPlayer;

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            playAudio();
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Đang phát");
            Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.pause();
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Phát âm thanh");
            Toast.makeText(this, "Tạm dừng", Toast.LENGTH_SHORT).show();
        }
        tile.updateTile();
    }

    private void playAudio() {
        try {
            SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
            String uriString = prefs.getString("audio_uri", null);
            if (uriString == null) {
                Toast.makeText(this, "Chưa chọn file!", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri uri = Uri.parse(uriString);
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            }
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState((mediaPlayer != null && mediaPlayer.isPlaying()) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.setLabel((mediaPlayer != null && mediaPlayer.isPlaying()) ? "Đang phát" : "Phát âm thanh");
            tile.setIcon(android.graphics.drawable.Icon.createWithResource(this, R.drawable.ic_play));
            tile.updateTile();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
