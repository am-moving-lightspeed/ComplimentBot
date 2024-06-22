package com.github.am_moving_lightspeed.compliment_bot.config.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("application.bot")
public class BotProperties {

    private Admin admin;
    private String username;
    private String token;

    @Getter
    @Setter
    public static class Admin {

        private String id;
    }
}
