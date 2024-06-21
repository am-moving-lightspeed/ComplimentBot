package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.BotCommands.HELP;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Messages.BOT_HELP_COMMAND_RESPONSE;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.CommandHandlerResult;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class HelpCommandHandler extends BaseCommandHandler {

    public HelpCommandHandler(MessageSource messages) {
        super(messages);
    }

    @Override
    protected boolean canHandle(Request request) {
        return HELP.equals(request.getCommand());
    }

    @Override
    protected CommandHandlerResult handleInternally(Request request) {
        var responseMessage = getResponseMessage(BOT_HELP_COMMAND_RESPONSE);
        return new CommandHandlerResult(responseMessage);
    }
}
