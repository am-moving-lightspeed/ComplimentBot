package com.github.am_moving_lightspeed.compliment_bot.config.listener;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserSubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserUnsubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.BackupService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.BroadcastService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.ContentLoaderService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session.BotSessionRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationEventDelegatingListener {

    private final BackupService backupService;
    private final BroadcastService broadcastService;
    private final BotSessionRunner botSessionRunner;
    private final ContentLoaderService contentLoaderService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            contentLoaderService.loadContent();
            broadcastService.scheduleBroadcast();
            backupService.scheduleBackup();
            botSessionRunner.run();
        } catch (Throwable throwable) {
            log.error("Application setup failed", throwable);
        }
    }

    @Async
    @EventListener({
        UserSubscribedEvent.class,
        UserUnsubscribedEvent.class
    })
    public void onUserEvent() {
        backupService.performBackup();
    }
}
