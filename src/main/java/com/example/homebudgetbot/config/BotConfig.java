package com.example.homebudgetbot.config;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/application.properties")
@Data
public class BotConfig {
    @Value("${bot.token}")
    String token;
    @Value("${bot.name}")
    String name;

}
