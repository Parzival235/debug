package com.example.audioquickplay;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
    private Uri selectedUri;
    private MediaPlayer mediaPlayer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedFile = findViewById(R.id.tv_selected_file);
        btnPickAudio = findViewById(R.id.btn_pick_audio);
        btnPlay = findViewById(R.id.btn_play);

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
                if (selectedUri != null) {
                    playAudio(selectedUri);
                } else {
                    Toast.makeText(MainActivity.this, "Chọn file trước!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void playAudio(Uri uri) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
            Toast.makeText(this, "Đang phát...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi phát: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
