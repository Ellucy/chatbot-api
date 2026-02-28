package com.team12.aichatbot.chatbot_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team12.aichatbot.chatbot_api.config.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Service for handling interactions with OpenAI API using direct HTTP calls
 * This is the fallback tier when rules and RAG don't match
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIClientService {
    
    private final OpenAIProperties openAIProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SYSTEM_PROMPT = """
            Sa oled abistav vestlusrobot, kes vastab juhtide küsimustele.
            VÄGA OLULINE REEGEL: Kui sa ei tea vastust kindlalt, ära arva ega hallutsineerida.
            Vasta ausalt: "Kahjuks ei tea ma sellele küsimusele vastust. Palun otsi lisainfot veebist või konsulteeri asjatundjaga."
            Ole viisakas, konkreetne ja lühike oma vastustes (max 500 tokenit).
            """;
    
    /**
     * Generate a response using OpenAI LLM via direct HTTP calls
     */
    public String generateResponse(String question) {
        try {
            log.info("Sending question to OpenAI: {}", question);
            
            // Get model name with fallback
            String modelName = (openAIProperties.getModel() != null && openAIProperties.getModel().getChat() != null) 
                ? openAIProperties.getModel().getChat() 
                : "gpt-4o-mini";
            
            log.info("Using model: {}", modelName);
            
            // Build the request JSON
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", modelName);
            requestBody.put("max_completion_tokens", openAIProperties.getMaxTokens() != null ? openAIProperties.getMaxTokens() : 500);
            // Note: o4-mini and similar models only support default temperature (1.0), so we don't set it
            
            log.info("Request body before messages: {}", requestBody.toString());
            
            ArrayNode messages = requestBody.putArray("messages");
            ObjectNode systemMessage = messages.addObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", SYSTEM_PROMPT);
            
            ObjectNode userMessage = messages.addObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            
            log.info("Complete request body: {}", requestBody.toString());
            
            // Convert to JSON string
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.info("JSON string to send: {}", jsonBody);
            
            // Make HTTP request
            RestClient restClient = RestClient.create();
            String response = restClient.post()
                    .uri(OPENAI_API_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAIProperties.getKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
            
            // Parse response
            JsonNode responseJson = objectMapper.readTree(response);
            String answer = responseJson
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText();
            
            log.info("Received response from OpenAI");
            return answer;
            
        } catch (Exception e) {
            log.error("Error communicating with OpenAI API: {} - {}", e.getClass().getName(), e.getMessage(), e);
            return "Vabandust, tekkis viga AI teenusega suhtlemisel: " + e.getMessage();
        }
    }
    
    /**
     * Generate embeddings for RAG implementation (iteration 3)
     */
    @SuppressWarnings("java:S1172") // Parameter will be used in iteration 3
    public List<Double> generateEmbedding(String text) {
        // To be implemented in iteration 3
        log.warn("Embedding generation not yet implemented");
        return List.of();
    }
}
