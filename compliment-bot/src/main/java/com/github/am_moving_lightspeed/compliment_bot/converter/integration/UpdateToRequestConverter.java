package com.github.am_moving_lightspeed.compliment_bot.converter.integration;

import static java.util.Optional.ofNullable;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class UpdateToRequestConverter implements Converter<Update, Request> {

    @Override
    public Request convert(Update source) {
        var optionalMessage = ofNullable(source).map(Update::getMessage);
        var command = optionalMessage.map(Message::getText)
                                     .orElse(null);
        var userId = optionalMessage.map(Message::getFrom)
                                    .map(User::getId)
                                    .orElse(null);
        return new Request(command, userId);
    }

    @Override
    public Update reverseConvert(Request source) {
        throw new UnsupportedOperationException();
    }
}
