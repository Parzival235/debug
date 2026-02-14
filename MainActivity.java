package com.example.audioquickplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView tvSelectedFile, tvPlaybackTime;
    private MaterialButton btnPickAudio, btnPlay, btnRepeat;
    private MaterialButton btnLogout;
    private SeekBar seekBar;
    private Uri selectedUri;
    private PreferencesManager prefs;

    private final ActivityResultLauncher<String> pickAudioLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedUri = uri;
                    tvSelectedFile.setText(getString(R.string.label_selected, uri.getLastPathSegment()));
                    PreferencesManager.getInstance(this).setAudioUri(uri.toString());
                    Toast.makeText(this, R.string.msg_file_ready, Toast.LENGTH_SHORT).show();
                }
            });

    private BroadcastReceiver stateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            boolean playing = intent.getBooleanExtra("is_playing", false);
            btnPlay.setText(playing ? getString(R.string.action_pause) : getString(R.string.btn_play));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedFile = findViewById(R.id.tv_selected_file);
        btnPickAudio = findViewById(R.id.btn_pick_audio);
        btnPlay = findViewById(R.id.btn_play);
        btnRepeat = findViewById(R.id.btn_repeat);
        btnLogout = findViewById(R.id.btn_logout);
        tvPlaybackTime = findViewById(R.id.tv_playback_time);
        seekBar = findViewById(R.id.seekbar_progress);

        prefs = PreferencesManager.getInstance(this);

        String uriString = prefs.getAudioUri();
        if (uriString != null) {
            selectedUri = Uri.parse(uriString);
            tvSelectedFile.setText(getString(R.string.label_selected, selectedUri.getLastPathSegment()));
        }

        updateRepeatButton();

        btnPickAudio.setOnClickListener(v -> pickAudioLauncher.launch("audio/*"));

        btnPlay.setOnClickListener(v -> {
            if (selectedUri == null) {
                Toast.makeText(MainActivity.this, R.string.msg_pick_file_first, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MainActivity.this, AudioPlayerService.class);
            i.setAction(AudioPlayerService.ACTION_TOGGLE);
            startService(i);
        });

        btnLogout.setOnClickListener(v -> {
            prefs.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnRepeat.setOnClickListener(v -> {
            PreferencesManager.RepeatMode current = prefs.getRepeatMode();
            PreferencesManager.RepeatMode next = current == PreferencesManager.RepeatMode.OFF ? 
                    PreferencesManager.RepeatMode.ONE : 
                    current == PreferencesManager.RepeatMode.ONE ? 
                    PreferencesManager.RepeatMode.ALL : 
                    PreferencesManager.RepeatMode.OFF;
            prefs.setRepeatMode(next);
            updateRepeatButton();
            Toast.makeText(MainActivity.this, getRepeatModeText(next), Toast.LENGTH_SHORT).show();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && selectedUri != null) {
                    Intent i = new Intent(MainActivity.this, AudioPlayerService.class);
                    i.setAction(AudioPlayerService.ACTION_SEEK);
                    i.putExtra("progress", progress);
                    startService(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f = new IntentFilter(AudioPlayerService.ACTION_STATE);
        registerReceiver(stateReceiver, f, Context.RECEIVER_EXPORTED);
        btnPlay.setText(AudioPlayerService.isPlaying ? getString(R.string.action_pause) : getString(R.string.btn_play));
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(stateReceiver);
        } catch (Exception ignored) {}
        super.onPause();
    }

    private void updateRepeatButton() {
        if (prefs == null) prefs = PreferencesManager.getInstance(this);
        PreferencesManager.RepeatMode mode = prefs.getRepeatMode();
        String text;
        float alpha;
        switch (mode) {
            case OFF:
                text = "🔁 OFF";
                alpha = 0.6f;
                break;
            case ONE:
                text = "🔂 ONE";
                alpha = 1.0f;
                break;
            case ALL:
                text = "🔁 ALL";
                alpha = 1.0f;
                break;
            default:
                text = "🔁";
                alpha = 0.6f;
                break;
        }
        btnRepeat.setText(text);
        btnRepeat.setAlpha(alpha);
    }

    private String getRepeatModeText(PreferencesManager.RepeatMode mode) {
        switch (mode) {
            case OFF:
                return "Repeat OFF";
            case ONE:
                return "Repeat ONE";
            case ALL:
                return "Repeat ALL";
            default:
                return "";
        }
    }
}
