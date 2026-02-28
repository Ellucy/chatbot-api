package com.team12.aichatbot.chatbot_api.repository;

import com.team12.aichatbot.chatbot_api.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Document entity - RAG support
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByCategory(String category);
    
    // For future RAG implementation with pgvector
    // Vector similarity search methods will be added in iteration 3
}
