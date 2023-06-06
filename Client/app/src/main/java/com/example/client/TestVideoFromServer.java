package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.MediaController;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.net.Uri;
import android.widget.VideoView;

public class TestVideoFromServer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video_from_server);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RegistrationActivity.ADDRESS + "nextVideo")
                .header("Authorization", "10d79774-a817-438a-83f0-c95d7622569a")
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
                .header("Authorization", "10d79774-a817-438a-83f0-c95d7622569a")
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


                        File videoFile = saveVideoToFile(data);

                        // Воспроизведение видео на экране
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                VideoView videoView = findViewById(R.id.videoView); // Замените videoView на ID вашего VideoView
                                videoView.setVideoURI(Uri.fromFile(videoFile));
                                videoView.start();
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

    private File saveVideoToFile(byte[] data) throws IOException {
        File videoFile = new File(getExternalFilesDir(null), "tempVideo.mp4");

        FileOutputStream fos = new FileOutputStream(videoFile);
        fos.write(data);
        fos.flush();
        fos.close();

        return videoFile;
    }


}