package com.team12.aichatbot.chatbot_api.service;

import com.team12.aichatbot.chatbot_api.config.ChatbotProperties;
import com.team12.aichatbot.chatbot_api.dto.ChatRequest;
import com.team12.aichatbot.chatbot_api.dto.ChatResponse;
import com.team12.aichatbot.chatbot_api.entity.ChatMessage;
import com.team12.aichatbot.chatbot_api.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Main orchestration service for chat functionality
 * Implements the three-tier response strategy: Rules -> RAG -> LLM
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final RuleService ruleService;
    private final OpenAIClientService openAIClientService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatbotProperties chatbotProperties;
    
    /**
     * Process a chat request using the three-tier strategy
     */
    public ChatResponse processMessage(ChatRequest request) {
        log.info("Processing message from user: {}", request.getUserId());
        
        String question = request.getQuestion();
        ChatResponse response;
        
        // Tier 1: Try rule-based response
        Optional<String> ruleAnswer = ruleService.findMatchingRule(question);
        if (ruleAnswer.isPresent()) {
            response = ChatResponse.success(
                    ruleAnswer.get(),
                    ChatMessage.ResponseType.RULE_BASED,
                    0
            );
            log.info("Responded with rule-based answer");
        } 
        // Tier 2: Try RAG (to be implemented in iteration 3)
        else if (Boolean.TRUE.equals(chatbotProperties.getRag().getEnabled())) {
            // RAG implementation will go here
            log.debug("RAG not yet implemented, falling back to LLM");
            response = generateLLMResponse(question);
        } 
        // Tier 3: Fallback to LLM
        else if (Boolean.TRUE.equals(chatbotProperties.getLlm().getFallbackEnabled())) {
            response = generateLLMResponse(question);
        } 
        else {
            response = ChatResponse.error("Vabandust, ei suutnud küsimusele vastust leida.");
        }
        
        // Save the conversation to database
        saveChatMessage(request.getUserId(), question, response);
        
        return response;
    }
    
    private ChatResponse generateLLMResponse(String question) {
        try {
            String answer = openAIClientService.generateResponse(question);
            log.info("Responded with LLM-generated answer");
            return ChatResponse.success(
                    answer,
                    ChatMessage.ResponseType.LLM,
                    null // Token count can be tracked separately if needed
            );
        } catch (Exception e) {
            log.error("Error generating LLM response", e);
            return ChatResponse.error("Vabandust, tekkis viga vastuse genereerimisel.");
        }
    }
    
    private void saveChatMessage(String userId, String question, ChatResponse response) {
        try {
            ChatMessage message = new ChatMessage();
            message.setUserId(userId);
            message.setQuestion(question);
            message.setAnswer(response.getAnswer() != null ? response.getAnswer() : "Error occurred");
            message.setResponseType(response.getResponseType());
            message.setTokensUsed(response.getTokensUsed());
            
            chatMessageRepository.save(message);
            log.debug("Saved chat message to database");
        } catch (Exception e) {
            log.error("Error saving chat message", e);
            // Don't throw - this shouldn't break the user experience
        }
    }
}
