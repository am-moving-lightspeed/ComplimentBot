package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static java.util.Locale.getDefault;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public abstract class BaseCommandHandler implements BotCommandHandler {

    private final MessageSource messages;

    private BotCommandHandler next;

    protected abstract boolean canHandle(Request request);

    protected abstract CommandHandlerResult handleInternally(Request request);

    @Override
    public CommandHandlerResult handle(Request request) {
        if (canHandle(request)) {
            return handleInternally(request);
        }
        return hasNext() ? next.handle(request) : CommandHandlerResult.empty();
    }

    @Override
    public void setNext(BotCommandHandler next) {
        this.next = next;
    }

    protected boolean hasNext() {
        return next != null;
    }

    protected String getResponseMessage(String code) {
        return messages.getMessage(code, null, getDefault());
    }
}
