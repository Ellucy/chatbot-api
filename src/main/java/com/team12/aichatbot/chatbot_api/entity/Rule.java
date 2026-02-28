package com.team12.aichatbot.chatbot_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a predefined rule for standard questions
 */
@Entity
@Table(name = "rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String question;
    
    @Column(nullable = false, length = 2000)
    private String answer;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column
    private String category;
    
    @Column
    private Integer priority = 0;
}
