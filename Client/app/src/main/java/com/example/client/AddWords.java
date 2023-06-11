package com.example.client;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.*;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddWords extends AppCompatActivity {

    final String token = "6d99d269-233d-406f-8a10-77c99617204b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words);
    }

    public void loadWords(View v) {
        EditText editTextWord1 = findViewById(R.id.editTextWord1);
        EditText editTextWord2 = findViewById(R.id.editTextWord2);
        EditText editTextWord3 = findViewById(R.id.editTextWord3);
        String word1 = editTextWord1.getText().toString();
        String word2 = editTextWord2.getText().toString();
        String word3 = editTextWord3.getText().toString();
        List<String> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);
        words.add(word3);
        sendWords(words);
    }

    private void sendWords(List<String> words) {
        JSONArray jsonArray = new JSONArray(words);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonArray.toString());

        Request request = new Request.Builder()
                .url(RegistrationActivity.ADDRESS + "words")
                .post(requestBody)
                .header("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddWords.this, responseBody, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
