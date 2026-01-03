package com.ai.chatbot.helper;

import com.ai.chatbot.interfaces.AIClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class OllamaClient implements AIClient {

    private static final String URL = "http://192.168.1.2:11434/api/generate";
    private static final String MODEL = "llama2";

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String getResponse(String prompt) throws Exception {

        String body = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(MODEL, escape(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return extract(response.body());
    }

    private String extract(String json) {
        int start = json.indexOf("\"response\":\"");
        start += 12;
        int end = json.indexOf("\"", start);
        return json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
