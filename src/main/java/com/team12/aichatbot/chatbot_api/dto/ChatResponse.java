package com.team12.aichatbot.chatbot_api.dto;

import com.team12.aichatbot.chatbot_api.entity.ChatMessage.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chat responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {
    
    private String answer;
    private ResponseType responseType;
    private Integer tokensUsed;
    private String errorMessage;
    
    public static ChatResponse success(String answer, ResponseType responseType, Integer tokensUsed) {
        return ChatResponse.builder()
                .answer(answer)
                .responseType(responseType)
                .tokensUsed(tokensUsed)
                .build();
    }
    
    public static ChatResponse error(String errorMessage) {
        return ChatResponse.builder()
                .errorMessage(errorMessage)
                .build();
    }
}
