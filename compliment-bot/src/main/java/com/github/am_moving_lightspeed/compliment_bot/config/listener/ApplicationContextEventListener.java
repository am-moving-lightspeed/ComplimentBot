package com.github.am_moving_lightspeed.compliment_bot.config.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.BotSession;

@Component
@RequiredArgsConstructor
public class ApplicationContextEventListener {

    private final BotSession botSession;

    // TODO: check if working properly
    @Async
    @EventListener(ContextClosedEvent.class)
    public void onApplicationContextClosedEvent() {
        if (botSession.isRunning()) {
            try {
                botSession.stop();
            } catch (Exception ignore) {
            }
        }
    }
}
