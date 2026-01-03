package com.ai.chatbot.interfaces;

public interface AIClient {
    String getResponse(String prompt) throws Exception;
}