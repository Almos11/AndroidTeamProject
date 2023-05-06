package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Main {

    static String token;
    static String username = "user_2";
    static String password = "password_2";

    public static boolean register(String username, String password) throws IOException, InterruptedException {
        ObjectNode requestObject = JsonNodeFactory.instance.objectNode();
        requestObject.put("username", username);
        requestObject.put("password", password);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestObject.toString()))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        return response.statusCode() == 200;
    }

    public static void login(String username, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login?username=" + username + "&password=" + password))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            token =  response.body();
        }
    }

    public static String getIdNextVideo(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/nextVideo"))
                .header("Authorization", token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String jsonData = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            return jsonNode.get("id").asText();
        } else {
            return null;
        }
    }

    public static void downloadVideo(String id, String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/download?Id=" + id))
                .header("Authorization", token)
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() == 200) {
            byte[] responseData = response.body();
            System.out.println(Arrays.toString(responseData));
        } else {
            System.out.println("Request failed");
        }
    }


    public static void getNextVideo(String token) throws IOException, InterruptedException {
        String identifier = getIdNextVideo(token);
        downloadVideo(identifier, token);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        login(username, password);
        getNextVideo(token);
    }
}