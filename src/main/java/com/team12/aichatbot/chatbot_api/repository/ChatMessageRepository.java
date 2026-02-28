package com.team12.aichatbot.chatbot_api.repository;

import com.team12.aichatbot.chatbot_api.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for ChatMessage entity
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByUserIdOrderByCreatedAtDesc(String userId);
}
