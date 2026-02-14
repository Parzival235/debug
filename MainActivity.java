package com.example.audioquickplay;

import android.content.SharedPreferences;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView tvSelectedFile;
    private MaterialButton btnPickAudio, btnPlay;
    private MaterialButton btnLogout;
    private Uri selectedUri;
    private SharedPreferences prefs;

    private final ActivityResultLauncher<String> pickAudioLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedUri = uri;
                    tvSelectedFile.setText("Đã chọn: " + uri.getLastPathSegment());
                    prefs.edit().putString("audio_uri", uri.toString()).apply();
                    Toast.makeText(this, "File sẵn sàng!", Toast.LENGTH_SHORT).show();
                }
            });

    private BroadcastReceiver stateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            boolean playing = intent.getBooleanExtra("is_playing", false);
            btnPlay.setText(playing ? "Tạm dừng" : "Phát ngay");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedFile = findViewById(R.id.tv_selected_file);
        btnPickAudio = findViewById(R.id.btn_pick_audio);
        btnPlay = findViewById(R.id.btn_play);
        btnLogout = findViewById(R.id.btn_logout);

        prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);

        String uriString = prefs.getString("audio_uri", null);
        if (uriString != null) {
            selectedUri = Uri.parse(uriString);
            tvSelectedFile.setText("Đã chọn: " + selectedUri.getLastPathSegment());
        }

        btnPickAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAudioLauncher.launch("audio/*");
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUri == null) {
                    Toast.makeText(MainActivity.this, "Chọn file trước!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(MainActivity.this, AudioPlayerService.class);
                i.setAction(AudioPlayerService.ACTION_TOGGLE);
                startService(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(LoginActivity.KEY_IS_LOGGED_IN, false).apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Unregister receiver
        try {
            unregisterReceiver(stateReceiver);
        } catch (Exception ignored) {}

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register receiver to update button state
        IntentFilter f = new IntentFilter(AudioPlayerService.ACTION_STATE);
        registerReceiver(stateReceiver, f);
        // set initial button text
        btnPlay.setText(AudioPlayerService.isPlaying ? "Tạm dừng" : "Phát ngay");
    }
}
