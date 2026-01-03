package com.ai.chatbot.helper;

import com.ai.chatbot.interfaces.AIClient;
import java.net.http.*;
import java.net.URI;

public class HuggingFaceClient implements AIClient {

    private static final String API_KEY = "YOUR_NEW_TOKEN";

    private static final String ENDPOINT =
            "https://router.huggingface.co/v1/models/google/gemma-2-2b-it";

    @Override
    public String getResponse(String prompt) throws Exception {

        String body = """
        {
          "inputs": "%s"
        }
        """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
