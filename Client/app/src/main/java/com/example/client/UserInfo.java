package com.example.client;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;


public class UserInfo extends AppCompatActivity {

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        client = new OkHttpClient();
        String BASE_URL = "http://192.168.1.104:8080/getUserInfo";
        long user_id = 123;

        Request request = new Request.Builder()
                .url(BASE_URL + "/getUserInfo?user_id=" + user_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayUserInfo(responseData);
                        }
                    });
                }
            }
        });
    }
    private void displayUserInfo(String userInfoJson) {
        Gson gson = new Gson();

        // Разберите JSON-ответ и преобразуйте его в объект UserData
        UserData userData = gson.fromJson(userInfoJson, UserData.class);

        // Выведите значения полей на экран
        TextView idTextView = findViewById(R.id.idTextView);
        idTextView.setText(String.valueOf(userData.getId()));

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(userData.getName());

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(userData.getDescription());

        TextView countLikesTextView = findViewById(R.id.countLikesTextView);
        countLikesTextView.setText(String.valueOf(userData.getCountLikes()));

        TextView countVideosTextView = findViewById(R.id.countVideosTextView);
        countVideosTextView.setText(String.valueOf(userData.getCountVideos()));
    }
}
