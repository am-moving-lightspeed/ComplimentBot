package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    @Value("${application.bot.token}")
    private final String token;

    @Value("${application.bot.username}")
    private final String username;

    private final Converter<Update, Request> updateToRequestConverter;
    private final BotCommandHandlerChain handlerChain;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var request = updateToRequestConverter.convert(update);
        var handlingResult = handlerChain.handle(request);

        if (handlingResult.isEmpty()) {
            return;
        }
        var sendMessageMethod = SendMessage.builder()
                                           .chatId(request.getUserId().toString())
                                           .text(handlingResult.getMessage())
                                           .build();
        try {
            execute(sendMessageMethod);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
