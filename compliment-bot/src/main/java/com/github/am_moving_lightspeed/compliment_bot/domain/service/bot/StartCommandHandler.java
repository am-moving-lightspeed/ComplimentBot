package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.BotCommands.START;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Messages.BOT_START_COMMAND_RESPONSE;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserSubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.ContentCacheService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class StartCommandHandler extends BaseCommandHandler {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ContentCacheService contentCacheService;

    public StartCommandHandler(MessageSource messages,
                               ApplicationEventPublisher applicationEventPublisher,
                               ContentCacheService contentCacheService) {
        super(messages);
        this.applicationEventPublisher = applicationEventPublisher;
        this.contentCacheService = contentCacheService;
    }

    @Override
    protected boolean canHandle(Request request) {
        return START.equals(request.getCommand());
    }

    @Override
    protected CommandHandlerResult handleInternally(Request request) {
        var userId = request.getUserId();
        var exists = contentCacheService.getUsers().stream()
                                        .anyMatch(user -> user.getId().equals(userId));
        if (exists) {
            return CommandHandlerResult.empty();
        }
        contentCacheService.cacheUser(new User(userId));
        applicationEventPublisher.publishEvent(new UserSubscribedEvent(userId));

        var responseMessage = getResponseMessage(BOT_START_COMMAND_RESPONSE);
        return new CommandHandlerResult(responseMessage);
    }
}
