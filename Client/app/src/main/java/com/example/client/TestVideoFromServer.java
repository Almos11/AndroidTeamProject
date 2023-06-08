package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Base64;
import android.widget.MediaController;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.net.Uri;
import android.widget.VideoView;

public class TestVideoFromServer extends AppCompatActivity {

    final static String token = "60c0004f-511a-4a99-be5b-85d606a873c1";
    ArrayList<byte[]> videoBytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video_from_server);
        videoBytes = new ArrayList<>();
        getNextVideo();
        //startPlayVideos(videoBytes);
    }

    public void getNextVideo() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RegistrationActivity.ADDRESS + "nextVideo")
                .header("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String jsonData = responseBody.string();
                        Gson gson = new Gson();
                        VideoData videoData = gson.fromJson(jsonData, VideoData.class);
                        sendDownloadRequest(videoData.getId());
                    }
                } else {
                    // Обработка неуспешного ответа от сервера
                }
                response.close();
            }
        });
    }

    public void sendDownloadRequest(String id) {
        OkHttpClient client = new OkHttpClient();

        String url = RegistrationActivity.ADDRESS + "download?Id=" + id;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибки при отправке запроса
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Обработка ответа от сервера
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        byte[] data = responseBody.bytes();
                        videoBytes.add(data);

                        // Воспроизведение видео на экране
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //startPlayVideos(videoBytes);
                                ViewPager2 viewPager = findViewById(R.id.viewPager);
                                VideoAdapterTest videoAdapterTest = new VideoAdapterTest(videoBytes);
                                viewPager.setAdapter(videoAdapterTest);
                            }
                        });
                    }
                } else {
                    // Обработка неуспешного ответа от сервера
                }
                response.close();
            }
        });
    }

    void startPlayVideos(ArrayList<byte[]> videoBytes) {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VideoAdapterTest videoAdapterTest = new VideoAdapterTest(videoBytes);
        viewPager.setAdapter(videoAdapterTest);
    }
}
