package com.team12.aichatbot.chatbot_api.controller;

import com.team12.aichatbot.chatbot_api.entity.Rule;
import com.team12.aichatbot.chatbot_api.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller for managing rules
 */
@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@Slf4j
public class RuleController {
    
    private final RuleService ruleService;
    
    /**
     * Get all active rules
     */
    @GetMapping
    public ResponseEntity<List<Rule>> getAllRules() {
        log.info("Fetching all active rules");
        List<Rule> rules = ruleService.getAllActiveRules();
        return ResponseEntity.ok(rules);
    }
    
    /**
     * Add a new rule
     */
    @PostMapping
    public ResponseEntity<Rule> addRule(@RequestBody Rule rule) {
        log.info("Adding new rule: {}", rule.getQuestion());
        Rule savedRule = ruleService.addRule(rule);
        return ResponseEntity.ok(savedRule);
    }
}
