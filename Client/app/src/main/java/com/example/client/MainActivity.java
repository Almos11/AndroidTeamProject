package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void nextScreen(View v) {
        Intent intent = new Intent(this, SecondScreen.class);
        startActivity(intent);
    }

    public void runVideo(View v) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        try {
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }
}