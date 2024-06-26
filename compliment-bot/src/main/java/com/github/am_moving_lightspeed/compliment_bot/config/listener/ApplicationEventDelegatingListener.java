package com.github.am_moving_lightspeed.compliment_bot.config.listener;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserSubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserUnsubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.BackupService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.ContentLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.BotSession;

@Component
@RequiredArgsConstructor
public class ApplicationEventDelegatingListener {

    private final BackupService backupService;
    private final BotSession botSession;
    private final ContentLoaderService contentLoaderService;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        contentLoaderService.loadContent();
        backupService.scheduleBackup();
    }

    @Async
    @EventListener({
        UserSubscribedEvent.class,
        UserUnsubscribedEvent.class
    })
    public void onUserEvent() {
        backupService.performBackup();
    }

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
