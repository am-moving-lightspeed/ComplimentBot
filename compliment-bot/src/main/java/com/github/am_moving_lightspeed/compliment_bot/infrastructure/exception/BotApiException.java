package com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.BOT_API_EXCEPTION;

public class BotApiException extends ServiceException {

    public BotApiException(Throwable cause) {
        super(BOT_API_EXCEPTION, cause);
    }
}
