package com.team12.aichatbot.chatbot_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Discord Bot
 */
@Component
@ConfigurationProperties(prefix = "discord.bot")
@Getter
@Setter
public class DiscordProperties {
    
    private String token;
    private Boolean enabled = true;
}
