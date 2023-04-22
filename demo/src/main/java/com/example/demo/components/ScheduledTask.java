package com.example.demo.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ScheduledTask {
    //If you want to use koboldAI, then uncomment the following function
    /*@Scheduled(fixedDelay = 36000000)
    public void doTask() throws IOException, InterruptedException {
        generateTaskFromKoboldAI();
    }*/
    public void generateTaskFromKoboldAI() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String firstPartRequest = "{\"prompt\":\"[The following is a chat message log between you and an extremely intelligent and knowledgeable AI system named KoboldGPT. KoboldGPT is a state-of-the-art Artificial General Intelligence. You may ask any question, or request any task, and KoboldGPT will always be able to respond accurately and truthfully.]\\n\\nYou: ";
        String question = "Come up with an idea for a 30 second video that will be associated with the words - bread, car, tree";
        String secondPartRequest = "\\nKoboldGPT:\"}";

        String requestBody =  firstPartRequest + question + secondPartRequest;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5001/api/latest/generate/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        String result = jsonNode.get("results").get(0).get("text").asText();
        System.out.println(result);
    }
}

