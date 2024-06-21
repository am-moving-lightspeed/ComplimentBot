package com.github.am_moving_lightspeed.compliment_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BotRunner {

    public static void main(String[] args) {
        SpringApplication.run(BotRunner.class, args);
    }
}
