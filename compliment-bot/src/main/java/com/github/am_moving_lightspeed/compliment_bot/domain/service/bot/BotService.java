package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import com.github.am_moving_lightspeed.compliment_bot.config.model.BotProperties;
import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.BotApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    private final BotCommandHandlerChain handlerChain;
    private final BotProperties properties;
    private final Converter<Update, Request> updateToRequestConverter;

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
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
            throw new BotApiException(e);
        }
    }

    public void sendMessage(String chatId, String message) {
        sendMessage(chatId, message, false);
    }

    public void sendMessage(String chatId, String message, boolean silent) {
        var sendMessageMethod = SendMessage.builder()
                                           .chatId(chatId)
                                           .text(message)
                                           .disableNotification(silent)
                                           .build();
        try {
            execute(sendMessageMethod);
        } catch (TelegramApiException e) {
            throw new BotApiException(e);
        }
    }
}
