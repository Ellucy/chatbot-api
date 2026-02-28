package com.team12.aichatbot.chatbot_api.controller;

import com.team12.aichatbot.chatbot_api.dto.ChatRequest;
import com.team12.aichatbot.chatbot_api.dto.ChatResponse;
import com.team12.aichatbot.chatbot_api.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller for chat functionality
 * Provides endpoints for direct API access (alternative to Discord)
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    
    private final ChatService chatService;
    
    /**
     * Send a message to the chatbot via REST API
     */
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("Received REST API chat request from user: {}", request.getUserId());
        
        try {
            ChatResponse response = chatService.processMessage(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing chat message", e);
            return ResponseEntity.internalServerError()
                    .body(ChatResponse.error("Vabandust, tekkis serveri viga."));
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chatbot API is running");
    }
}
