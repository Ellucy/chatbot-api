package com.team12.aichatbot.chatbot_api.repository;

import com.team12.aichatbot.chatbot_api.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Rule entity
 */
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    
    List<Rule> findByActiveOrderByPriorityDesc(Boolean active);
    
    List<Rule> findByQuestionIgnoreCaseAndActive(String question, Boolean active);
}
