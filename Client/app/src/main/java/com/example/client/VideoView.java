package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class VideoView extends AppCompatActivity {

    final static String token = "bda9f28d-b22a-4f03-a59e-6ce80f84055a";
    VideoAdapter videoAdapter;

    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        startPlayVideos();

    }

    public void getNextVideo(FileCallback callback, int number) {
        OkHttpClient client = new OkHttpClient();
        final File[] videoFile = new File[1];
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
                        sendDownloadRequest(jsonData, videoData.getId(), number, new FileCallback() {
                            @Override
                            public void onFileReceived(File videoFile, String jsonData) {
                                callback.onFileReceived(videoFile, jsonData);
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


    public void sendDownloadRequest(String jsonData, String id, int number, FileCallback callback) {
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
                        File videoFile = saveVideoToFile(data, number);
                        if (callback != null) {
                            runOnUiThread(() -> callback.onFileReceived(videoFile, jsonData));
                        }
                    }
                } else {
                    // Обработка неуспешного ответа от сервера
                }
                response.close();
            }
        });
    }


    void startPlayVideos() {
        userInfo = new UserInfo();
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        videoAdapter = new VideoAdapter(this);
        viewPager.setAdapter(videoAdapter);
    }

    private File saveVideoToFile(byte[] data, int number) throws IOException {
        String nameVideoFile = "tempVideo" + number +".mp4";
        File videoFile = new File(getExternalFilesDir(null), nameVideoFile);
        FileOutputStream fos = new FileOutputStream(videoFile);
        fos.write(data);
        fos.flush();
        fos.close();

        return videoFile;
    }
    public interface FileCallback {
        void onFileReceived(File videoFile, String jsonData);
    }

    public void showUserInfo(View v) {
        String infoVideo = videoAdapter.currentVideoInfo;
        Gson gson = new Gson();
        VideoData videoData = gson.fromJson(infoVideo, VideoData.class);
        userInfo.setId(videoData.getAuthor_id());
        Intent intent = new Intent(VideoView.this, UserInfo.class);
        startActivity(intent);
    }

    public void addLike(View v) {
        String infoVideo = videoAdapter.currentVideoInfo;
        Gson gson = new Gson();
        VideoData videoData = gson.fromJson(infoVideo, VideoData.class);
        sendLikeRequest(videoData.getId());
    }

    private  void sendLikeRequest(String id) {
        OkHttpClient client = new OkHttpClient();

        String url = RegistrationActivity.ADDRESS + "like?Id=" + id;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", VideoView.token)
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
                   // Toast.makeText(VideoView.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(VideoView.this, "Error", Toast.LENGTH_SHORT).show();
                }
                response.close();
            }
        });
    }
}