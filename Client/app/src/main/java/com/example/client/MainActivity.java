package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

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

    public void runVideo(View view) {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(videoPath);
        videoView.start();
    }

    public void runVideoURL(View view) {
        String videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();
    }



}