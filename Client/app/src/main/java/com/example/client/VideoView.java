package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoView extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        playVideo();
    }

    public void addLike(View v) {
        String address = RegistrationActivity.ADDRESS;
        String token = RegistrationActivity.token;
        String endpoint = "like";
        OkHttpClient client = new OkHttpClient();
        String url = address + endpoint + "?Id=" + token;
        //String url = "http://192.168.1.102:8080/like?Id=идентификатор";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VideoView.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            Button likeButton = findViewById(R.id.likeButton);
                            likeButton.setBackgroundColor(Color.GRAY);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, java.io.IOException e) {
                // Обработка ошибки
                e.printStackTrace();
                Toast.makeText(VideoView.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void playVideo() {
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