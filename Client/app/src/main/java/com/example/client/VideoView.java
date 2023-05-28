package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class VideoView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void nextScreen(View v) {
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }

    public void runVideo(View view) {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        android.widget.VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(videoPath);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        videoView.start();
    }

    public void runVideoURL(View view) {
        String videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        android.widget.VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoUrl));

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

        videoView.start();
    }

    public void trySwipe(View view) {
         String[] videoPaths = {
                "android.resource://" + getPackageName() + "/" + R.raw.video1,
                "android.resource://" + getPackageName() + "/" + R.raw.video2,
                "android.resource://" + getPackageName() + "/" + R.raw.video3,
                 "android.resource://" + getPackageName() + "/" + R.raw.video4,
                 "android.resource://" + getPackageName() + "/" + R.raw.video5,
                 "android.resource://" + getPackageName() + "/" + R.raw.video6,
        };
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VideoAdapter videoAdapter = new VideoAdapter(videoPaths); // videoPaths - массив путей к видео
        viewPager.setAdapter(videoAdapter);

    }




}