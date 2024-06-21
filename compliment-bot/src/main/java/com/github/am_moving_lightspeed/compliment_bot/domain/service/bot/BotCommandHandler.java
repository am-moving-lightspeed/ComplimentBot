package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;

public interface BotCommandHandler {

    CommandHandlerResult handle(Request request);

    void setNext(BotCommandHandler next);
}
