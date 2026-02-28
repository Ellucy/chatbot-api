package com.team12.aichatbot.chatbot_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing a chat message in the system
 */
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false, length = 2000)
    private String question;
    
    @Column(nullable = false, length = 5000)
    private String answer;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseType responseType;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private Integer tokensUsed;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum ResponseType {
        RULE_BASED,  // Vastatud reeglipõhiselt
        RAG,         // Vastatud RAG põhjal
        LLM          // Vastatud LLM-iga
    }
}
