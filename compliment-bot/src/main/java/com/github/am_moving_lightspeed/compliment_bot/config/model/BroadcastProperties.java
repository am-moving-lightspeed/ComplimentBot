package com.github.am_moving_lightspeed.compliment_bot.config.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("application.broadcast")
public class BroadcastProperties {

    private Blackout blackout;
    private String initialTime;
    private Integer interval;

    @Getter
    @Setter
    public static class Blackout {

        private String start;
        private String end;
    }
}
