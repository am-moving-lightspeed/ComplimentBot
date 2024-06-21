package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.BotCommands.START;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Messages.BOT_START_COMMAND_RESPONSE;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class StartCommandHandler extends BaseCommandHandler {

    private final StoragePersistenceService storagePersistenceService;

    public StartCommandHandler(MessageSource messages,
                               StoragePersistenceService storagePersistenceService) {
        super(messages);
        this.storagePersistenceService = storagePersistenceService;
    }

    @Override
    protected boolean canHandle(Request request) {
        return START.equals(request.getCommand());
    }

    @Override
    protected CommandHandlerResult handleInternally(Request request) {
        storagePersistenceService.saveUser(new User(request.getUserId()));
        var responseMessage = getResponseMessage(BOT_START_COMMAND_RESPONSE);
        return new CommandHandlerResult(responseMessage);
    }
}
