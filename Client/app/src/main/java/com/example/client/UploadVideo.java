package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadVideo extends AppCompatActivity {

    public void goBack(View v) {
        Intent intent = new Intent(this, VideoView.class);
        startActivity(intent);
    }

    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 1;
    private TextView tvSelectedVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
    }

    public void uploadVideo(View v) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите видео"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            uploadVideoToServer(videoUri);
        }
    }


    private void uploadVideoToServer(Uri videoUri) {
        OkHttpClient client = new OkHttpClient();
        AsyncTask.execute(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(videoUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "video.mp4",
                                RequestBody.create(MediaType.parse("video/*"), byteArrayOutputStream.toByteArray()))
                        .build();

                Request request = new Request.Builder()
                        .url("http://192.168.1.106:8080/upload")
                        .header("Authorization", "66f5cc5f-bb47-4950-99ba-b8d5e795acea")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String id = response.body().string();
                    // Обработка успешного ответа от сервера
                } else {
                    // Обработка неуспешного ответа от сервера
                }

                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки
            }
        });
    }
}