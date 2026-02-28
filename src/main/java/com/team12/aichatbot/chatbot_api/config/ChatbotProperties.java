package com.team12.aichatbot.chatbot_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application-specific chatbot configuration
 */
@Component
@ConfigurationProperties(prefix = "chatbot")
@Getter
@Setter
public class ChatbotProperties {
    
    private Rules rules;
    private Rag rag;
    private Llm llm;
    
    @Getter
    @Setter
    public static class Rules {
        private Boolean enabled = true;
    }
    
    @Getter
    @Setter
    public static class Rag {
        private Boolean enabled = false;
    }
    
    @Getter
    @Setter
    public static class Llm {
        private Boolean fallbackEnabled = true;
        private Integer maxRetries = 3;
    }
}
