package com.team12.aichatbot.chatbot_api.config;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Discord bot integration
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "discord.bot", name = "enabled", havingValue = "true")
public class DiscordConfig {
    
    private final DiscordProperties discordProperties;
    
    @Bean
    public GatewayDiscordClient discordClient() {
        log.info("Initializing Discord bot...");
        
        return DiscordClientBuilder.create(discordProperties.getToken())
                .build()
                .login()
                .block();
    }
}
