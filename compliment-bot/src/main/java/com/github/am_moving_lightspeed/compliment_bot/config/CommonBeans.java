package com.github.am_moving_lightspeed.compliment_bot.config;

import com.github.am_moving_lightspeed.compliment_bot.domain.service.BotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Configuration
public class CommonBeans {

    @Bean
    public BotSession botSession(BotService botService) throws TelegramApiException {
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        return telegramBotsApi.registerBot(botService);
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        var applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        applicationEventMulticaster.setTaskExecutor(newSingleThreadExecutor());
        return applicationEventMulticaster;
    }
}
