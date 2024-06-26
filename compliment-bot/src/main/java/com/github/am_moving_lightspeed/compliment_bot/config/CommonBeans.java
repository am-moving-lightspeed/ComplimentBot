package com.github.am_moving_lightspeed.compliment_bot.config;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Encodings.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session.BotManagedSession;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session.BotSessionRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration(proxyBeanMethods = false)
public class CommonBeans {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(INDENT_OUTPUT);
    }

    @Bean
    public BotSessionRunner botSession(BotService botService) throws TelegramApiException {
        var telegramBotsApi = new TelegramBotsApi(BotManagedSession.class);
        return (BotManagedSession) telegramBotsApi.registerBot(botService);
    }

    @Bean
    public MessageSource messageSource() {
        var resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages/messages");
        resourceBundleMessageSource.setDefaultEncoding(UTF_8);
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        resourceBundleMessageSource.setFallbackToSystemLocale(false);
        return resourceBundleMessageSource;
    }
}
