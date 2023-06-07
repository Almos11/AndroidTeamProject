package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.MediaController;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
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

    final static String token = "a81d8233-4c17-40cc-9b73-d7644841429a";
    ArrayList<File> videoFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video_from_server);
        videoFiles = new ArrayList<>();
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
                        sendDownloadRequest(videoData.getId(), videoFiles);
                    }
                } else {
                    // Обработка неуспешного ответа от сервера
                }
                response.close();
            }
        });
        startPlayVideos(videoFiles);
    }
    public void sendDownloadRequest(String id, ArrayList<File> videoFiles) {
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


                        File videoFile = saveVideoToFile(data);
                         //videoFiles.add(videoFile);

                        // Воспроизведение видео на экране
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<File> videos = new ArrayList<>();
                                videos.add(videoFile);
                                startPlayVideos(videos);
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

    void startPlayVideos(ArrayList<File> videoFiles) {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VideoAdapterTest videoAdapterTest = new VideoAdapterTest(videoFiles);
        viewPager.setAdapter(videoAdapterTest);
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