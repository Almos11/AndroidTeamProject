package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

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

public class TestVideoFromServer extends AppCompatActivity {

    final static String token = "aff4f189-7ea6-4a16-8b9a-05949c942ff7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video_from_server);
        startPlayVideos();

    }

    public void getNextVideo(FileCallback callback) {
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
                        sendDownloadRequest(videoData.getId(), new FileCallback() {
                            @Override
                            public void onFileReceived(File videoFile) {
                                callback.onFileReceived(videoFile);
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

    public void sendDownloadRequest(String id, FileCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = RegistrationActivity.ADDRESS + "download?Id=" + id;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();

        final File[] videoFile = new File[1];
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
                        videoFile[0] = saveVideoToFile(data);
                        if (callback != null) {
                            callback.onFileReceived(videoFile[0]);
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
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VideoAdapterTest videoAdapterTest = new VideoAdapterTest(this);
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
    public interface FileCallback {
        void onFileReceived(File videoFile);
    }



}