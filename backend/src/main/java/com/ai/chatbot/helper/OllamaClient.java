package com.ai.chatbot.helper;

import com.ai.chatbot.interfaces.AIClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OllamaClient implements AIClient {

    private static final String OLLAMA_URL = "http://192.168.1.2:11434/api/generate";
    private static final String MODEL = "llama2";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getResponse(String prompt) throws Exception {

        String requestBody = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(MODEL, escapeJson(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return extractResponse(response.body());
    }

    private String extractResponse(String json) {
        int start = json.indexOf("\"response\":\"");
        if (start == -1) return "";

        start += 12;
        int end = json.indexOf("\"", start);
        if (end == -1) return "";

        return json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
