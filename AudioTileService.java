package com.example.audioquickplay;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

public class AudioTileService extends TileService {

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
            tile.setLabel(getString(R.string.notification_playing));
            Toast.makeText(this, getString(R.string.label_audio_tile), Toast.LENGTH_SHORT).show();
        } else {
            svcIntent.setAction(AudioPlayerService.ACTION_PAUSE);
            startService(svcIntent);
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel(getString(R.string.label_audio_tile));
            Toast.makeText(this, getString(R.string.action_pause), Toast.LENGTH_SHORT).show();
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
            boolean playing = AudioPlayerService.isPlaying;
            tile.setState(playing ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.setLabel(playing ? getString(R.string.notification_playing) : getString(R.string.label_audio_tile));
            tile.setIcon(android.graphics.drawable.Icon.createWithResource(this, R.drawable.ic_play));
            tile.updateTile();
        }
    }
}
