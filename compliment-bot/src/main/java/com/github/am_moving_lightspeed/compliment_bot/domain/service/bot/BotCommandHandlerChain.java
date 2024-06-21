package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_HANDLE_USER_REQUEST;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BotCommandHandlerChain {

    private static final Logger log = LoggerFactory.getLogger(BotCommandHandlerChain.class);
    private final BotCommandHandler commandHandler;

    public BotCommandHandlerChain(List<BotCommandHandler> commandHandlers) {
        BotCommandHandler head = null;
        BotCommandHandler previous = null;

        for (var handler : commandHandlers) {
            if (head == null) {
                head = handler;
            }
            if (previous != null) {
                previous.setNext(handler);
            }
            previous = handler;
        }
        this.commandHandler = head;
    }

    public CommandHandlerResult handle(Request request) {
        try {
            return commandHandler.handle(request);
        } catch (Exception exception) {
            log.error("Some error occurred while handling user's request:", exception);
            throw new ServiceException(FAILED_TO_HANDLE_USER_REQUEST, exception);
        }
    }
}
