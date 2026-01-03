package com.ai.chatbot;

import com.ai.chatbot.helper.OllamaClient;
import com.ai.chatbot.interfaces.AIClient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        AIClient ai = new OllamaClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ollama Chatbot (type 'exit' to quit)");

        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) break;

            String reply = ai.getResponse(input);
            System.out.println("AI: " + reply);
        }
    }
}
