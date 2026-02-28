package com.team12.aichatbot.chatbot_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming chat requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    @NotBlank(message = "User ID on nõutud")
    private String userId;
    
    @NotBlank(message = "Küsimus on nõutud")
    @Size(max = 2000, message = "Küsimus ei tohi olla pikem kui 2000 tähemärki")
    private String question;
}
