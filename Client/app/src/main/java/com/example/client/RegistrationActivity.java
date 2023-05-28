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

public class RegistrationActivity extends AppCompatActivity {
    final String address = "http://192.168.1.104:8080/register";
    String username;
    String password;

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    void getNameAndPassword() {
        EditText loginEditText = findViewById(R.id.loginEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        username = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();
    }
    public void sendDataToServerLogin(View v) throws UnsupportedEncodingException {
        getNameAndPassword();
        String urlParameters = "username=" + URLEncoder.encode(username, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8");

        new SendLoginRequestTask().execute(address, urlParameters);
    }

    private class SendLoginRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String endpoint = params[0];
            String urlParameters = params[1];

            HttpURLConnection connection = null;
            try {
                // Create connection
                URL url = new URL(endpoint + "?" + urlParameters);
                connection = (HttpURLConnection) url.openConnection();

                // Set request method
                connection.setRequestMethod("GET");

                // Get the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                token = response.toString();
                return token;
            } catch (IOException e) {
                Log.e("SendLoginRequest", "Error sending login request", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                Toast.makeText(RegistrationActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this, VideoView.class);
                startActivity(intent);
            } else {
                Toast.makeText(RegistrationActivity.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendDataToServerRegister(View v) {
        getNameAndPassword();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login", username);
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
                URL url = new URL(address);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

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
                Toast.makeText(RegistrationActivity.this, "Регистрация успешна. Теперь снова введите данные для входа", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistrationActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
