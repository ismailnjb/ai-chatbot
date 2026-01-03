package com.ai.chatbot.controller;

import com.ai.chatbot.interfaces.AIClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class ChatBotController {

    private final AIClient aiClient;

    public ChatBotController(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> body) throws Exception {
        String prompt = body.get("prompt");
        String response = aiClient.getResponse(prompt);
        return Map.of("response", response);
    }
}
