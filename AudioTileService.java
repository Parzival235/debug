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
        Intent svcIntent = new Intent(this, AudioPlayerService.class);
        if (!AudioPlayerService.isPlaying) {
            svcIntent.setAction(AudioPlayerService.ACTION_PLAY);
            startService(svcIntent);
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Đang phát");
            Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();
        } else {
            svcIntent.setAction(AudioPlayerService.ACTION_PAUSE);
            startService(svcIntent);
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Phát âm thanh");
            Toast.makeText(this, "Tạm dừng", Toast.LENGTH_SHORT).show();
        }
        tile.updateTile();
    }

    private void playAudio() {
        // Deprecated: kept for compatibility but prefer AudioPlayerService
        Intent svcIntent = new Intent(this, AudioPlayerService.class);
        svcIntent.setAction(AudioPlayerService.ACTION_PLAY);
        startService(svcIntent);
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
