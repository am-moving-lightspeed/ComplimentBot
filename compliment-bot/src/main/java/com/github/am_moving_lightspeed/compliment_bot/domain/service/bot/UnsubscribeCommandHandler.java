package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.BotCommands.UNSUBSCRIBE;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Messages.BOT_UNSUBSCRIBE_COMMAND_RESPONSE;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserUnsubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.ContentCacheService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class UnsubscribeCommandHandler extends BaseCommandHandler {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ContentCacheService contentCacheService;

    public UnsubscribeCommandHandler(MessageSource messages,
                                     ApplicationEventPublisher applicationEventPublisher,
                                     ContentCacheService contentCacheService) {
        super(messages);
        this.applicationEventPublisher = applicationEventPublisher;
        this.contentCacheService = contentCacheService;
    }

    @Override
    protected boolean canHandle(Request request) {
        return UNSUBSCRIBE.equals(request.getCommand());
    }

    @Override
    protected CommandHandlerResult handleInternally(Request request) {
        var userId = request.getUserId();
        var exists = contentCacheService.getUsers().stream()
                                        .anyMatch(user -> user.getId().equals(userId));
        if (!exists) {
            return CommandHandlerResult.empty();
        }
        contentCacheService.removeUser(new User(userId));
        applicationEventPublisher.publishEvent(new UserUnsubscribedEvent(userId));

        var responseMessage = getResponseMessage(BOT_UNSUBSCRIBE_COMMAND_RESPONSE);
        return new CommandHandlerResult(responseMessage);
    }
}
