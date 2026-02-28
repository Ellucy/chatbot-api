package com.team12.aichatbot.chatbot_api.service;

import com.team12.aichatbot.chatbot_api.config.ChatbotProperties;
import com.team12.aichatbot.chatbot_api.entity.Rule;
import com.team12.aichatbot.chatbot_api.repository.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RuleService
 */
@ExtendWith(MockitoExtension.class)
class RuleServiceTest {
    
    @Mock
    private RuleRepository ruleRepository;
    
    @Mock
    private ChatbotProperties chatbotProperties;
    
    @Mock
    private ChatbotProperties.Rules rulesConfig;
    
    @InjectMocks
    private RuleService ruleService;
    
    @BeforeEach
    void setUp() {
        when(chatbotProperties.getRules()).thenReturn(rulesConfig);
    }
    
    @Test
    void testFindMatchingRule_WhenExactMatchExists_ShouldReturnAnswer() {
        // Arrange
        when(rulesConfig.getEnabled()).thenReturn(true);
        
        Rule mockRule = new Rule();
        mockRule.setQuestion("Test question");
        mockRule.setAnswer("Test answer");
        
        when(ruleRepository.findByQuestionIgnoreCaseAndActive(anyString(), anyBoolean()))
                .thenReturn(List.of(mockRule));
        
        // Act
        Optional<String> result = ruleService.findMatchingRule("Test question");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test answer", result.get());
        verify(ruleRepository, times(1))
                .findByQuestionIgnoreCaseAndActive("Test question", true);
    }
    
    @Test
    void testFindMatchingRule_WhenNoMatchExists_ShouldReturnEmpty() {
        // Arrange
        when(rulesConfig.getEnabled()).thenReturn(true);
        when(ruleRepository.findByQuestionIgnoreCaseAndActive(anyString(), anyBoolean()))
                .thenReturn(List.of());
        
        // Act
        Optional<String> result = ruleService.findMatchingRule("Unknown question");
        
        // Assert
        assertFalse(result.isPresent());
    }
    
    @Test
    void testFindMatchingRule_WhenRulesDisabled_ShouldReturnEmpty() {
        // Arrange
        when(rulesConfig.getEnabled()).thenReturn(false);
        
        // Act
        Optional<String> result = ruleService.findMatchingRule("Any question");
        
        // Assert
        assertFalse(result.isPresent());
        verify(ruleRepository, never()).findByQuestionIgnoreCaseAndActive(anyString(), anyBoolean());
    }
    
    @Test
    void testAddRule_ShouldSaveAndReturnRule() {
        // Arrange
        Rule inputRule = new Rule();
        inputRule.setQuestion("New question");
        inputRule.setAnswer("New answer");
        
        when(ruleRepository.save(any(Rule.class))).thenReturn(inputRule);
        
        // Act
        Rule result = ruleService.addRule(inputRule);
        
        // Assert
        assertNotNull(result);
        assertEquals("New question", result.getQuestion());
        assertEquals("New answer", result.getAnswer());
        verify(ruleRepository, times(1)).save(inputRule);
    }
}
