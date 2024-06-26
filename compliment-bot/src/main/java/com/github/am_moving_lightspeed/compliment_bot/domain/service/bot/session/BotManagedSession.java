package com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session;

import org.springframework.context.Lifecycle;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotManagedSession
    extends DefaultBotSession
    implements Lifecycle, BotSessionRunner {

    public void run() {
        super.start();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }
}
