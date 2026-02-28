package com.team12.aichatbot.chatbot_api.service;

import com.team12.aichatbot.chatbot_api.config.ChatbotProperties;
import com.team12.aichatbot.chatbot_api.entity.Rule;
import com.team12.aichatbot.chatbot_api.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling rule-based responses
 * This is the first tier in the response strategy: Rules -> RAG -> LLM
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {
    
    private final RuleRepository ruleRepository;
    private final ChatbotProperties chatbotProperties;
    
    /**
     * Try to find a matching rule for the given question
     */
    public Optional<String> findMatchingRule(String question) {
        if (!Boolean.TRUE.equals(chatbotProperties.getRules().getEnabled())) {
            log.debug("Rule-based responses are disabled");
            return Optional.empty();
        }
        
        // First, try exact match (case-insensitive)
        List<Rule> exactMatches = ruleRepository.findByQuestionIgnoreCaseAndActive(question, true);
        if (!exactMatches.isEmpty()) {
            log.info("Found exact rule match for question: {}", question);
            return Optional.of(exactMatches.get(0).getAnswer());
        }
        
        // Strip common punctuation and try again (handles "Tere!" -> "Tere")
        String cleanedQuestion = question.replaceAll("[!?.,:;]", "").trim();
        if (!cleanedQuestion.equals(question)) {
            List<Rule> cleanMatches = ruleRepository.findByQuestionIgnoreCaseAndActive(cleanedQuestion, true);
            if (!cleanMatches.isEmpty()) {
                log.info("Found rule match after cleaning punctuation: {} -> {}", question, cleanedQuestion);
                return Optional.of(cleanMatches.get(0).getAnswer());
            }
        }
        
        log.debug("No rule match found for question: {}", question);
        return Optional.empty();
    }
    
    /**
     * Get all active rules
     */
    public List<Rule> getAllActiveRules() {
        return ruleRepository.findByActiveOrderByPriorityDesc(true);
    }
    
    /**
     * Add a new rule
     */
    public Rule addRule(Rule rule) {
        log.info("Adding new rule: {}", rule.getQuestion());
        return ruleRepository.save(rule);
    }
}
