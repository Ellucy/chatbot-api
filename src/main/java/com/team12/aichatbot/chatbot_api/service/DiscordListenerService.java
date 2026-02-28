package com.team12.aichatbot.chatbot_api.service;

import com.team12.aichatbot.chatbot_api.dto.ChatRequest;
import com.team12.aichatbot.chatbot_api.dto.ChatResponse;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service for listening and responding to Discord messages
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(GatewayDiscordClient.class)
public class DiscordListenerService {
    
    private final GatewayDiscordClient discordClient;
    private final ChatService chatService;
    
    @PostConstruct
    public void init() {
        log.info("Starting Discord bot listener...");
        
        discordClient.on(MessageCreateEvent.class)
                .filter(event -> !event.getMessage().getAuthor().map(discord4j.core.object.entity.User::isBot).orElse(true))
                .flatMap(this::handleMessage)
                .subscribe();
        
        log.info("Discord bot is now listening for messages");
    }
    
    private Mono<Void> handleMessage(MessageCreateEvent event) {
        return Mono.fromRunnable(() -> {
            String messageContent = event.getMessage().getContent();
            String userId = event.getMessage().getAuthor()
                    .map(user -> user.getId().asString())
                    .orElse("unknown");
            
            log.info("Received Discord message from user {}: {}", userId, messageContent);
            
            try {
                // Process the message through our chat service
                ChatRequest request = new ChatRequest(userId, messageContent);
                ChatResponse response = chatService.processMessage(request);
                
                // Send the response back to Discord
                if (response.getAnswer() != null) {
                    event.getMessage().getChannel()
                            .flatMap(channel -> channel.createMessage(response.getAnswer()))
                            .subscribe();
                } else if (response.getErrorMessage() != null) {
                    event.getMessage().getChannel()
                            .flatMap(channel -> channel.createMessage("Viga: " + response.getErrorMessage()))
                            .subscribe();
                }
            } catch (Exception e) {
                log.error("Error processing Discord message", e);
                event.getMessage().getChannel()
                        .flatMap(channel -> channel.createMessage("Vabandust, tekkis viga sõnumi töötlemisel."))
                        .subscribe();
            }
        });
    }
}
