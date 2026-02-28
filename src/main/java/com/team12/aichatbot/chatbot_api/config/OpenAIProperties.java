package com.team12.aichatbot.chatbot_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for OpenAI API
 */
@Component
@ConfigurationProperties(prefix = "openai.api")
@Getter
@Setter
public class OpenAIProperties {
    
    private String key;
    private Model model;
    private Integer maxTokens;
    private Double temperature;
    
    @Getter
    @Setter
    public static class Model {
        private String chat;
        private String embedding;
    }
}
