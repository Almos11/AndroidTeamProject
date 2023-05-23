package com.example.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void goToVideo(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendDataToServer(View v) {
        EditText loginEditText = findViewById(R.id.loginEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login", login);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new RegisterTask().execute(jsonObject.toString());
    }

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://192.168.1.104:8080/register");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Отправка данных на сервер
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(params[0]);
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Обработка успешной регистрации
                Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
            } else {
                // Обработка ошибки регистрации
                Toast.makeText(RegistrationActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
