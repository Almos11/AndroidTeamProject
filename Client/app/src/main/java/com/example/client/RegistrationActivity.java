package com.example.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {
    static String ADDRESS = "http://192.168.1.101:8080/";

    String username;
    String password;

    static String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    JSONObject getNameAndPassword() {
        EditText loginEditText = findViewById(R.id.loginEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        username = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public void sendDataToServerLogin(View v) throws UnsupportedEncodingException {
        sendDataToServer("login");
    }
    public void sendDataToServerRegister(View v) throws UnsupportedEncodingException {
        sendDataToServer("register");
    }


    public void sendDataToServer(String name_endpoint) throws UnsupportedEncodingException {
        JSONObject jsonObject = getNameAndPassword();
        String json = jsonObject.toString();
        String address = ADDRESS + name_endpoint;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    token = response.body().string();

                    // Обработка ответа сервера в UI-потоке
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistrationActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            if (!Objects.equals(token, "")) {
                                /*Intent intent = new Intent(RegistrationActivity.this, VideoView.class);
                                startActivity(intent);
                                finish();*/
                                Intent intent = new Intent(RegistrationActivity.this, UserInfo.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    // Обработка ошибки сервера
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
