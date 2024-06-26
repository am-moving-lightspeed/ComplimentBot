package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public interface BotSessionRunner {

    /**
     * To be invoked manually. Postpones execution of {@link  DefaultBotSession#start()} method.
     */
    void run();
}
