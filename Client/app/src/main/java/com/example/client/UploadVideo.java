package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadVideo extends AppCompatActivity {

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 1;


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(videoUri);

                // Отправка файла на сервер
                uploadToServer(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToServer(InputStream inputStream) {
        String url = "http://192.168.1.112:8080/upload";
        String token = "accd3cc4-1c02-415d-8679-b6854aebf31e";

        try {
            URL serverUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", token);

            OutputStream outputStream = connection.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Видео успешно загружено на сервер
                // Обработайте ответ от сервера, если необходимо
                showNotification("Успех!", "Видео успешно загружено на сервер");
            } else {
                // Обработка ошибки при загрузке видео на сервер
                showNotification("Ошибка!", "Ошибка при загрузке видео на сервер");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}